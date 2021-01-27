package com.sumavision.tetris.application.template.feign;/**
 * Created by Poemafar on 2020/11/3 16:40
 */

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.application.template.*;
import com.sumavision.tetris.business.common.MissionBO;
import com.sumavision.tetris.business.common.Util.CommonUtil;
import com.sumavision.tetris.business.common.Util.IdConstructor;
import com.sumavision.tetris.business.common.dao.TaskInputDAO;
import com.sumavision.tetris.business.common.dao.TaskOutputDAO;
import com.sumavision.tetris.business.common.enumeration.BusinessType;
import com.sumavision.tetris.business.common.enumeration.MediaType;
import com.sumavision.tetris.business.common.enumeration.TaskType;
import com.sumavision.tetris.business.common.exception.CommonException;
import com.sumavision.tetris.business.common.po.TaskOutputPO;
import com.sumavision.tetris.business.common.service.TaskService;
import com.sumavision.tetris.business.push.service.ScheduleService;
import com.sumavision.tetris.business.transcode.service.TranscodeTaskService;
import com.sumavision.tetris.capacity.bo.input.*;
import com.sumavision.tetris.capacity.bo.output.OutputBO;
import com.sumavision.tetris.capacity.bo.task.*;
import com.sumavision.tetris.capacity.constant.EncodeConstant;
import com.sumavision.tetris.capacity.template.TemplateService;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @ClassName: TemplateService
 * @Description TODO
 * @Author: Poemafar
 * @Version：1.0
 * @Date 2020/11/3 16:40
 */
@Service
public class TemplateTaskService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TemplateTaskService.class);

    @Autowired
    TemplateDAO templateDAO;

    @Autowired
    TemplateService templateService;

    @Autowired
    TranscodeTaskService transcodeTaskService;

    @Autowired
    ScheduleService scheduleService;

    @Autowired
    TaskOutputDAO taskOutputDAO;

    @Autowired
    TaskInputDAO taskInputDAO;

    @Autowired
    TaskService taskService;

    /**
     * @MethodName: addTask
     * @Description: 根据模板下发任务
     * @param taskBO 业务参数
     * @Return: java.lang.String
     * @Author: Poemafar
     * @Date: 2020/12/11 8:37
     **/
    public String addTask(TemplateTaskVO taskBO) throws Exception {
        TemplatePO tmpPO = getTemplateByName(taskBO.getTemplate());
        TemplateTaskVO templateTaskVO = JSON.parseObject(tmpPO.getBody(), TemplateTaskVO.class);
        //组合
//        TemplateTaskVO tmpBO = CommonUtil.combineSydwCore(templateTaskVO,taskVO);
//        JSONObject templateObj = JSONObject.parseObject(JSON.toJSONString(templateTaskVO));
//        JSONObject taskObj = JSONObject.parseObject(JSON.toJSONString(taskVO));
//        JSONObject combineTaskObj = CommonUtil.coverJSONObject(templateObj,taskObj);             // 合并方法1
//        LOGGER.info("task and template combine params: {}",combineTaskObj.toJSONString());
//        TemplateTaskVO tmpBO = JSONObject.parseObject(combineTaskObj.toJSONString(),TemplateTaskVO.class);
        checkBusinessTaskParams(taskBO);//step1: 先检验一遍业务下发的参数；
        TemplateTaskVO combineJobBO = combine(templateTaskVO,taskBO);  //step2: 合并本地模板参数和业务下发的参数
        LOGGER.info("combine params :{}",JSON.toJSONString(combineJobBO));
        //开始转换参数
        MissionBO missionBO = new MissionBO();
        missionBO.setIdCtor(new IdConstructor());
        missionBO.setTaskType(tmpPO.getTaskType());
        missionBO.setDevice_ip(taskBO.getTask_ip());

        generateInputBOS(missionBO,combineJobBO);
        if (TaskType.TRANS==tmpPO.getTaskType()) {
            generateTaskBOS(missionBO, combineJobBO);
        }else if (TaskType.PACKAGE==tmpPO.getTaskType()){
            generatePackageTaskBOS(missionBO);
        }else if (TaskType.PASSBY==tmpPO.getTaskType()){
//            generateTaskBOS();
        }else {
            throw new IllegalArgumentException("unknown taskType: "+tmpPO.getTaskType());
        }
        generateOutputBOS(missionBO,combineJobBO);

        try {
            createTaskByTemplate(missionBO,combineJobBO,tmpPO.getBusinessType());//下发命令
        } catch (Exception e) {
            TaskOutputPO output = taskService.delete(missionBO.getIdCtor().getJobId(), tmpPO.getBusinessType(), true);
            if(output != null){
                taskOutputDAO.delete(output);
            }
            LOGGER.error("任务创建失败",e);
            throw e;//异常要抛出，让业务知道
        }


        ResponseVO responseVO = new ResponseVO();
        responseVO.setTaskId(missionBO.getIdCtor().getJobId());
        return JSON.toJSONString(responseVO);
    }

    public void createTaskByTemplate(MissionBO missionBO,TemplateTaskVO jobBO,BusinessType businessType) throws Exception {
        JSONObject schedule = (JSONObject)jobBO.getMap_sources().stream().filter(s -> "schedule".equals(((JSONObject) s).getString("type").toLowerCase())).findAny().orElse(null);
        if (schedule!=null){ //排期任务的输入只能有个排期
            List<InputBO> taskInputBOS = new ArrayList<>();
            List<InputBO> scheInputBOS = new ArrayList<>();
            taskInputBOS.add(missionBO.getInputMap().get(schedule.getInteger("index")));
            transcodeTaskService.save(missionBO.getIdCtor().getJobId(), missionBO.getDevice_ip(), taskInputBOS, missionBO.getTask_array(), missionBO.getOutput_array(), businessType);
            List<ScheduleProgramBO> schedules = new ArrayList();
            TaskOutputPO taskOutput = taskOutputDAO.findByTaskUuidAndType(missionBO.getIdCtor().getJobId(), businessType);
            for (int i = 0; i < jobBO.getMap_sources().size(); i++) {
                JSONObject sourceObj = jobBO.getMap_sources().getJSONObject(i);
                if (schedule.containsKey("prev") && sourceObj.getInteger("index").equals(schedule.getInteger("prev"))){
                    InputBO preInputBO = missionBO.getInputMap().get(schedule.getInteger("prev"));
                    schedules.add(getScheduleProgram(preInputBO,sourceObj));
                    InputWrapperBO inputWrapperBO = taskService.addInputInDatabase(missionBO.getDevice_ip(),preInputBO,businessType);
                    taskOutput.setPrevId(inputWrapperBO.getTaskInputPO().getId());
                    if (inputWrapperBO.getBeCreate()){
                        scheInputBOS.add(preInputBO);
                    }
                }
                if (schedule.containsKey("next")  && sourceObj.getInteger("index").equals(schedule.getInteger("next") )){
                    InputBO nextInputBO = missionBO.getInputMap().get(schedule.getInteger("next"));
                    schedules.add(getScheduleProgram(nextInputBO,sourceObj));
                    InputWrapperBO inputWrapperBO = taskService.addInputInDatabase(missionBO.getDevice_ip(),nextInputBO,businessType);
                    taskOutput.setNextId(inputWrapperBO.getTaskInputPO().getId());
                    if (inputWrapperBO.getBeCreate()){
                        scheInputBOS.add(nextInputBO);
                    }
                }
            }

            if (!schedules.isEmpty()) {
                InputBO scheduleBO = missionBO.getInputMap().get(schedule.getInteger("index"));
                scheduleService.sendSchedule(missionBO.getDevice_ip(), scheduleBO.getId(), null, scheInputBOS, schedules);
                taskOutput.setScheduleId(taskInputDAO.findByUniq(taskService.generateUniq(scheduleBO)).getId());
                taskOutput.setUpdateTime(new Date());
                taskOutputDAO.save(taskOutput);
            }
        }else {
            transcodeTaskService.save(missionBO.getIdCtor().getJobId(), missionBO.getDevice_ip(), missionBO.getInputMap().values().stream().collect(Collectors.toList()), missionBO.getTask_array(), missionBO.getOutput_array(), businessType);
        }

    }

    public void checkBusinessTaskParams(TemplateTaskVO taskVO) throws BaseException {
        JSONArray map_sources = taskVO.getMap_sources();
        Set<Integer> indexSet = new HashSet<>();
        for (int i = 0; i < map_sources.size(); i++) {
            JSONObject sourceObj = map_sources.getJSONObject(i);
            if (sourceObj.containsKey("index")) {
                Integer index = sourceObj.getInteger("index");
                if (indexSet.contains(index)) {
                    throw new BaseException(StatusCode.FORBIDDEN,"param error,input index must not repeated");
                }else{
                    indexSet.add(index);
                }
            }
        }
        if (map_sources!=null && !(indexSet.isEmpty() || indexSet.size()==map_sources.size())) {//index要么都存在，要么都不存在
            throw new BaseException(StatusCode.FORBIDDEN, "not find input index");
        }

        indexSet.clear();
        JSONArray map_tasks = taskVO.getMap_tasks();
        for (int i = 0; i < map_tasks.size(); i++) {
            JSONObject taskObj = map_tasks.getJSONObject(i);
            if (taskObj.containsKey("index")) {
                Integer index = taskObj.getInteger("index");
                if (indexSet.contains(index)) {
                    throw new BaseException(StatusCode.FORBIDDEN,"param error, task index must not repeated");
                }else{
                    indexSet.add(index);
                }
            }
        }
        //判断输出
        indexSet.clear();
        JSONArray map_outputs = taskVO.getMap_outputs();
        for (int i = 0; i < map_outputs.size(); i++) {
            JSONObject outputObj = map_outputs.getJSONObject(i);
            if (outputObj.containsKey("index")) {
                Integer index = outputObj.getInteger("index");
                if (indexSet.contains(index)) {
                    throw new BaseException(StatusCode.FORBIDDEN,"param error, output index must not repeated");
                }else{
                    indexSet.add(index);
                }
            }
        }
        if (map_outputs!=null && !(indexSet.isEmpty() || indexSet.size()==map_outputs.size())) {//index要么都存在，要么都不存在
            throw new BaseException(StatusCode.FORBIDDEN, "not find output index");
        }

    }

    /**
     * @MethodName: combine
     * @Description: 将模板参数和任务参数进行合并
     * 通过索引进行对比，任务对象可以比模板对象多，匹配不上的模板对象就清除
     * @param tmplVO 1 模板参数
     * @param taskVO 2 任务参数
     * @Return: com.sumavision.tetris.application.template.feign.TemplateTaskVO
     * @Author: Poemafar
     * @Date: 2020/12/10 9:11
     **/
    public TemplateTaskVO combine(TemplateTaskVO tmplVO, TemplateTaskVO taskVO) throws BaseException {
        TemplateTaskVO combineJobBO = new TemplateTaskVO();
        BeanUtils.copyProperties(taskVO,combineJobBO);

        //匹配输入，任务输入可以比模板输入多，匹配不上的模板就清除
        JSONArray combineIns = new JSONArray();

        Boolean hasFlag = taskVO.getMap_sources().getJSONObject(0).containsKey("index");
        if (!hasFlag) { //如果都不存在，就给他手动加个index
            for (int i = 0; i < taskVO.getMap_sources().size(); i++) {
                taskVO.getMap_sources().getJSONObject(i).put("index",i+1);
            }
        }
        if (tmplVO.getMap_sources()!=null && !tmplVO.getMap_sources().isEmpty()){
            for (int i = 0; i < taskVO.getMap_sources().size(); i++) {
                JSONObject tIn = taskVO.getMap_sources().getJSONObject(i);
                Integer tIdx = tIn.getInteger("index");
                Boolean find = false;
                for (int j = 0; j < tmplVO.getMap_sources().size(); j++) {
                    JSONObject pIn = tmplVO.getMap_sources().getJSONObject(j);
                    Integer pIdx = pIn.getInteger("index");
                    if (pIdx.equals(tIdx)){//找到对应的模板输出则覆盖
                        combineIns.add(CommonUtil.coverJSONObject(pIn,tIn));
                        find=true;
                        break;
                    }
                }
                if (!find){//没找到对应的模板输出则直接加入
                    combineIns.add(tIn);
                }
            }
            combineJobBO.setMap_sources(combineIns);
        }else{
            combineJobBO.setMap_sources(taskVO.getMap_sources());
        }
        //task合并
        JSONArray combineTasks = new JSONArray();
        if (taskVO.getMap_tasks()!=null && !taskVO.getMap_tasks().isEmpty()) {
            for (int i = 0; i < taskVO.getMap_tasks().size(); i++) {
                JSONObject tTask = taskVO.getMap_tasks().getJSONObject(i);
                if (tmplVO.getMap_tasks()!=null && !tmplVO.getMap_tasks().isEmpty()) {
                    if (!tTask.containsKey("index")){
                        tTask.put("index",i+1);
//                        throw new BaseException(StatusCode.ERROR,"not find task index");
                    }
                    Integer tIdx = tTask.getInteger("index");
                    Boolean find = false;
                    for (int j = 0; j < tmplVO.getMap_tasks().size(); j++) {
                        JSONObject pTask = tmplVO.getMap_tasks().getJSONObject(j);
                        Integer pIdx = pTask.getInteger("index");
                        if (pIdx.equals(tIdx)){//找到对应的模板输出则覆盖
                            combineTasks.add( CommonUtil.coverJSONObject(pTask,tTask));
                            find=true;
                            break;
                        }
                    }
                    if (!find){//没找到对应的模板输出则直接加入
                        combineTasks.add(tTask);
                    }
                }else{
                    combineTasks.add(tTask);
                }
            }
            combineJobBO.setMap_tasks(combineTasks);
        }else{
            combineJobBO.setMap_tasks(tmplVO.getMap_tasks());
        }


        //匹配输出，任务输出可以比模板输出多，匹配不上的模板就清除
        JSONArray combineOuts = new JSONArray();
        if (taskVO.getMap_outputs()!=null && !taskVO.getMap_outputs().isEmpty()) {
            Boolean extFlag = taskVO.getMap_outputs().getJSONObject(0).containsKey("index");
            if (!extFlag) { //如果都不存在，就给他手动加个index
                for (int i = 0; i < taskVO.getMap_outputs().size(); i++) {
                    taskVO.getMap_outputs().getJSONObject(i).put("index",i+1);
                }
            }
            for (int i = 0; i < taskVO.getMap_outputs().size(); i++) {
                JSONObject tOut = taskVO.getMap_outputs().getJSONObject(i);
                Integer tIdx = tOut.getInteger("index");
                Boolean find = false;
                for (int j = 0; j < tmplVO.getMap_outputs().size(); j++) {
                    JSONObject pOut = tmplVO.getMap_outputs().getJSONObject(j);
                    Integer pIdx = pOut.getInteger("index");
                    if (pIdx.equals(tIdx)) {//找到对应的模板输出则覆盖
                        combineOuts.add(CommonUtil.coverJSONObject(pOut, tOut));
                        find = true;
                        break;
                    }
                }
                if (!find) {//没找到对应的模板输出则直接加入
                    combineOuts.add(tOut);
                }
            }
            combineJobBO.setMap_outputs(combineOuts);
        }else{
            combineJobBO.setMap_outputs(tmplVO.getMap_outputs());
        }
        return combineJobBO;
    }



    /**
     * @MethodName: getScheduleProgram
     * @Description: 生成 schedule program
     * @param inputBO 1
     * @param curSource 2
     * @Return: com.sumavision.tetris.capacity.bo.input.ScheduleProgramBO
     * @Author: Poemafar
     * @Date: 2020/12/9 15:32
     **/
    public ScheduleProgramBO getScheduleProgram(InputBO inputBO,JSONObject curSource) throws BaseException {
        ScheduleProgramBO scheduleProgram = new ScheduleProgramBO();
        scheduleProgram.setInput_id(inputBO.getId())
                .setProgram_number(inputBO.getProgram_array().get(0).getProgram_number())
                .setElement_array(new ArrayList());
        List<ProgramVideoBO> videoBOS = inputBO.getProgram_array().get(0).getVideo_array();
        if (videoBOS != null && !videoBOS.isEmpty()) {
            ProgramElementBO ele = new ProgramElementBO().setType("video").setPid(videoBOS.get(0).getPid());
            scheduleProgram.getElement_array().add(ele);
        }
        List<ProgramAudioBO> audioBOS = inputBO.getProgram_array().get(0).getAudio_array();
        if (audioBOS != null && !audioBOS.isEmpty()) {
            ProgramElementBO ele = new ProgramElementBO().setType("audio").setPid(audioBOS.get(0).getPid());
            scheduleProgram.getElement_array().add(ele);
        }
        if (curSource.getString("type").toLowerCase().equals("file")) {
            JSONArray file_array = curSource.getJSONArray("file_array");
            JSONObject fileJSON = file_array.getJSONObject(0);
            ProgramFileBO programFileBO = new ProgramFileBO(fileJSON);
            scheduleProgram.setFile(programFileBO);
        }else{
            ProgramStreamBO stream = JSON.parseObject(curSource.toJSONString(), ProgramStreamBO.class);
            scheduleProgram.setLive(stream);
        }
        return scheduleProgram;
    }



    public TemplatePO getTemplateByName(String tmpName) throws BaseException {
        TemplatePO tmp = templateDAO.findByName(tmpName);
        if (tmp==null){
            throw new BaseException(StatusCode.ERROR,"template not exist, name:"+tmpName);
        }
        return tmp;
    }

    public String getAllTemplate(){
        List<TemplateVO> templateVOS = new ArrayList<>();
        List<TemplatePO> tplPOs = templateDAO.findByNameNotNullAndBusinessTypeNotNullAndTaskTypeNotNullAndBodyNotNull();
        tplPOs.stream().forEach(t->{
            TemplateVO tplVO = new TemplateVO(t.getName(),t.getBusinessType().name(),t.getTaskType().name(),t.getBody());
            templateVOS.add(tplVO);
        });
        return JSON.toJSONString(templateVOS);
    }

    /**
     * 添加模板
     * 从前台页面生成
     * @param templateTaskVO
     */
    public void addTemplate(TemplateTaskVO templateTaskVO){

    }

    /**
     * 删除模板
     */
    public void deleteTemplate(Long id){
        templateDAO.delete(id);
    }

    public void generateInputBOS(MissionBO missionBO, TemplateTaskVO tmplBO) throws  BaseException{
        //如果有schedule，则它的输入的媒体类型应该和schedule一样
        JSONObject schedule = (JSONObject)tmplBO.getMap_sources().stream().filter(s -> "schedule".equals(((JSONObject) s).getString("type").toLowerCase())).findAny().orElse(null);
        if (schedule!=null) {
            tmplBO.getMap_sources().stream().forEach(s->{
                if (!((JSONObject)s).containsKey("mediaType")) {
                    ((JSONObject) s).put("mediaType",schedule.getString("mediaType"));
                }
            });
        }

        //生成INPUTBO
        InputFactory inputFactory = new InputFactory();
        for (int i = 0; i < tmplBO.getMap_sources().size(); i++) {
            JSONObject inputObj = tmplBO.getMap_sources().getJSONObject(i);
            SourceVO sourceVO = JSON.parseObject(inputObj.toJSONString(),SourceVO.class);
            InputBO inputBO = inputFactory.getInputByTemplateInput(missionBO, sourceVO);
            missionBO.getInputMap().put(sourceVO.getIndex(),inputBO);
        }

    }

    public void generatePackageTaskBOS(MissionBO missionBO) throws BaseException {
        List<TaskBO> taskBOS = new ArrayList();
        TaskBO videoTaskBO = new TaskBO();
        List<EncodeBO> vEncodeBOS = new ArrayList<>();
        TaskBO audioTaskBO = new TaskBO();
        List<EncodeBO> aEncodeBOS = new ArrayList<>();
//video
        EncodeBO vEncodeBO = new EncodeBO()
                .setPassby(new JSONObject())
                .setEncode_id(missionBO.getIdCtor().getId(0, IdConstructor.IdType.ENCODE));
        missionBO.getOutEncodeMap().put(0, missionBO.getIdCtor().getId(0, IdConstructor.IdType.ENCODE));
        vEncodeBOS.add(vEncodeBO);
        videoTaskBO.setId(missionBO.getIdCtor().getId(0, IdConstructor.IdType.TASK))
                .setType("passby")
                .setEncode_array(vEncodeBOS);
        videoTaskBO.setTaskSource(missionBO,MediaType.VIDEO);
        videoTaskBO.setEncode_array(vEncodeBOS);
        taskBOS.add(videoTaskBO);
//audio
        aEncodeBOS.add( new EncodeBO()
                .setPassby(new JSONObject())
                .setEncode_id(missionBO.getIdCtor().getId(1, IdConstructor.IdType.ENCODE)));
        missionBO.getOutEncodeMap().put(1, missionBO.getIdCtor().getId(1, IdConstructor.IdType.ENCODE));
        audioTaskBO.setId(missionBO.getIdCtor().getId(1, IdConstructor.IdType.TASK))
                .setType("passby")
                .setEncode_array(aEncodeBOS);
        audioTaskBO.setTaskSource(missionBO,MediaType.AUDIO);
        audioTaskBO.setEncode_array(aEncodeBOS);
        taskBOS.add(audioTaskBO);

        missionBO.setTask_array(taskBOS);
    }

    /**
     * 生成转换协议-task部分
     * 多码率不能自动匹配
     * @param missionBO
     * @param tmplBO
     * @throws BaseException
     * @throws CommonException
     */
    public void generateTaskBOS(MissionBO missionBO, TemplateTaskVO tmplBO) throws BaseException {
        List<TaskBO> taskBOS = new ArrayList();
        TaskBO videoTaskBO = new TaskBO();
        List<EncodeBO> vEncodeBOS = new ArrayList<>();
        TaskBO audioTaskBO = new TaskBO();
        List<EncodeBO> aEncodeBOS = new ArrayList<>();
        TaskBO passbyTaskBO = new TaskBO();
        List<EncodeBO> pEncodeBOS = new ArrayList<>();
        TaskBO subTaskBO = new TaskBO();
        List<EncodeBO> sEncodeBOS = new ArrayList<>();

        Integer mediaOrder=0;
        for (int i = 0; i < tmplBO.getMap_tasks().size(); i++) {
            JSONObject tmplTaskObj = tmplBO.getMap_tasks().getJSONObject(i);
            String codec = tmplTaskObj.getString("codec");

            MediaType mediaType = null;
            if (tmplTaskObj.containsKey("mediaType")){
                String mediaTypeStr = tmplTaskObj.getString("mediaType");
                mediaType = MediaType.getMediaType(mediaTypeStr);
            }else{
                mediaType = MediaType.getMediaType(codec);
            }

            EncodeBO encodeBO = new EncodeBO();
            encodeBO.setEncode_id(missionBO.getIdCtor().getId(i, IdConstructor.IdType.ENCODE));
            missionBO.getOutEncodeMap().put(i, missionBO.getIdCtor().getId(i, IdConstructor.IdType.ENCODE));

            if (codec.contains("passby")){
                if (passbyTaskBO.getId()==null || passbyTaskBO.getId().isEmpty()) {
                    passbyTaskBO.setId(missionBO.getIdCtor().getId(mediaOrder++, IdConstructor.IdType.TASK));
                    passbyTaskBO.setType("passby");
                }
                pEncodeBOS.add(encodeBO.setPassby(new JSONObject()));
                passbyTaskBO.setTaskSource(missionBO,mediaType);
            }else {
                if (MediaType.VIDEO==mediaType){
                    if (videoTaskBO.getId()==null || videoTaskBO.getId().isEmpty()) {
                        videoTaskBO.setId(missionBO.getIdCtor().getId(mediaOrder++, IdConstructor.IdType.TASK));
                        videoTaskBO.setType("video");
                    }
                    //编码
                    EncodeConstant.VideoType videoType = EncodeConstant.VideoType.getVideoType(codec);
                    EncodeConstant.TplVideoEncoder codelib = null;
                    if (tmplTaskObj.containsKey("code_lib")){
                        codelib = EncodeConstant.TplVideoEncoder.getTplVideoEncoder(tmplTaskObj.getString("code_lib"));
                    }else {
                        codelib = EncodeConstant.TplVideoEncoder.getTplVideoEncoder(codec);
                    }

                    JSONObject videoEncObj = combineVideoEncode(tmplTaskObj,templateService.getVideoEncodeMap(codelib));
                    encodeBO.setProcess_array(handleVideoProcessList(tmplTaskObj,videoEncObj));

                    switch (videoType){
                        case H264:
                            encodeBO.setH264(videoEncObj);
                            break;
                        case HEVC:
                            encodeBO.setHevc(videoEncObj);
                            break;
                        case MPEG2:
                            encodeBO.setMpeg2(videoEncObj);
                            break;
                        case AVS:
                            encodeBO.setAvs2(videoEncObj);
                            break;
                    }
                    vEncodeBOS.add(encodeBO);
                    videoTaskBO.setDecode_process_array(new ArrayList<>());//解码预处理
                    videoTaskBO.setTaskSource(missionBO,null);//有透传有不透传，该发哪个source
                }else if(MediaType.AUDIO==mediaType){
                    if (audioTaskBO.getId()==null || audioTaskBO.getId().isEmpty()) {
                        audioTaskBO.setId(missionBO.getIdCtor().getId(mediaOrder++, IdConstructor.IdType.TASK));
                        audioTaskBO.setType("audio");
                    }
                    EncodeConstant.TplAudioEncoder audioType = EncodeConstant.TplAudioEncoder.getTplAudioEncoder(codec);
                    JSONObject audioEncObj = combineAudioEncode(tmplTaskObj, templateService.getAudioEncodeMap(audioType.name()));
                    encodeBO.setProcess_array(handleAudioProcessList(tmplTaskObj,audioEncObj));
                    switch (audioType){
                        case AENCODER_AACLC:
                            encodeBO.setAac(audioEncObj);
                            break;
                        case AENCODER_HEAAC:
                            encodeBO.setAac(audioEncObj);
                            break;
                        case AENCODER_HEAAC_V2:
                            encodeBO.setAac(audioEncObj);
                            break;
                        case AENCODER_MP2:
                            encodeBO.setMp2(audioEncObj);
                            break;
                        case AENCODER_MP3:
                            encodeBO.setMp3(audioEncObj);
                            break;
                        case AENCODER_AC3:
                            encodeBO.setDolby(audioEncObj);
                            break;
                        case AENCODER_EAC3:
                            encodeBO.setDolby(audioEncObj);
                            break;
                    }
                    aEncodeBOS.add(encodeBO);
                    audioTaskBO.setTaskSource(missionBO,null);
                } else if (MediaType.SUBTITLE==mediaType){
                    if (subTaskBO.getId()==null || subTaskBO.getId().isEmpty()) {
                        subTaskBO.setId(missionBO.getIdCtor().getId(mediaOrder++, IdConstructor.IdType.TASK));
                        subTaskBO.setType("subtitle");
                    }
                }else{
                    throw new BaseException(StatusCode.ERROR,"not support codec: "+codec);
                }
            }

        }

        if (videoTaskBO.getId()!=null && !videoTaskBO.getId().isEmpty() && !vEncodeBOS.isEmpty()) {
            videoTaskBO.setEncode_array(vEncodeBOS);
            taskBOS.add(videoTaskBO);
        }
        if (audioTaskBO.getId()!=null && !audioTaskBO.getId().isEmpty() && !aEncodeBOS.isEmpty()) {
            audioTaskBO.setEncode_array(aEncodeBOS);
            taskBOS.add(audioTaskBO);
        }
        if (passbyTaskBO.getId()!=null && !passbyTaskBO.getId().isEmpty() && !pEncodeBOS.isEmpty()) {
            passbyTaskBO.setEncode_array(pEncodeBOS);
            taskBOS.add(passbyTaskBO);
        }
        if (subTaskBO.getId()!=null && !subTaskBO.getId().isEmpty() && !sEncodeBOS.isEmpty()) {
            subTaskBO.setEncode_array(sEncodeBOS);
            taskBOS.add(subTaskBO);
        }
        missionBO.setTask_array(taskBOS);
    }



    public JSONObject combineVideoEncode(JSONObject tmplTaskObj,String params){
        //合并前处理
        JSONObject combineObj = CommonUtil.coverJSONObject(JSON.parseObject(params),tmplTaskObj);
        //合并后处理
        if (tmplTaskObj.containsKey("resolution")){
            combineObj.put("resolution",tmplTaskObj.getString("resolution").replaceAll("\\*|X","x"));
        }
        if (tmplTaskObj.containsKey("vbitrate")){
            Long bitrate = tmplTaskObj.getLong("vbitrate");
            combineObj.put("bitrate",bitrate);
            if (bitrate > combineObj.getLong("max_bitrate")){
                combineObj.put("max_bitrate",bitrate);
            }
        }else if (tmplTaskObj.containsKey("bitrate")){
            Long bitrate = tmplTaskObj.getLong("bitrate");
            combineObj.put("bitrate",bitrate);
            if (bitrate > combineObj.getLong("max_bitrate")){
                combineObj.put("max_bitrate",bitrate);
            }
        }else{
            combineObj.put("bitrate",6000);
            combineObj.put("max_bitrate",6000);
        }
        return combineObj;
    }

    public JSONObject combineAudioEncode(JSONObject tmplTaskObj,String params){
        JSONObject combineObj = CommonUtil.coverJSONObject(JSON.parseObject(params),tmplTaskObj);
        if (tmplTaskObj.containsKey("abitrate")){
            combineObj.put("bitrate",tmplTaskObj.getString("abitrate"));
        }else if (tmplTaskObj.containsKey("bitrate")) {
            combineObj.put("bitrate",tmplTaskObj.getString("bitrate"));
        }else{
            combineObj.put("bitrate","128");
        }
        if (tmplTaskObj.containsKey("sample_rate")){
            combineObj.put("sample_rate",String.valueOf(tmplTaskObj.getInteger("sample_rate")/1000.0f));
        }
        return combineObj;
    }

    public List handleAudioProcessList(JSONObject tmplTaskObj,JSONObject combineEncodeObj){
        List<PreProcessingBO> processingBOS = new ArrayList<>();
        Integer smpRate = Float.valueOf(combineEncodeObj.getFloat("sample_rate")*1000).intValue();
        ResampleBO resample = new ResampleBO().setSample_rate(smpRate)
                .setChannel_layout(tmplTaskObj.containsKey("channel_layout")? tmplTaskObj.getString("channel_layout"): combineEncodeObj.getString("channel_layout"))
                .setFormat(tmplTaskObj.containsKey("sample_fmt")?tmplTaskObj.getString("sample_fmt"):combineEncodeObj.getString("sample_fmt"));
        processingBOS.add(new PreProcessingBO().setResample(resample));

        //预处理
        combineEncodeObj.remove("pretreatments");
        if (tmplTaskObj.containsKey("pretreatments")) {
            JSONArray pretreatments = tmplTaskObj.getJSONArray("pretreatments");
            for (int i = 0; i < pretreatments.size(); i++) {
                ProcessVO processVO = JSON.parseObject(pretreatments.getString(i), ProcessVO.class);
                switch (processVO.getTreat_type()){
                    case RESAMPLE:
                        processingBOS.add(new PreProcessingBO().setResample(new ResampleBO(processVO)));
                        break;
                    case AUDGAIN:
                        processingBOS.add(new PreProcessingBO().setAud_gain(new AudioGainBO(processVO)));
                        break;
                }
            }
        }
        return processingBOS;
    }
    public List handleVideoProcessList(JSONObject tmplTaskObj,JSONObject combineEncodeObj){
        List<PreProcessingBO> processingBOS = new ArrayList<>();

        String resolv = "";
        if (tmplTaskObj.containsKey("resolution")) {
            resolv = tmplTaskObj.getString("resolution");
        }else if(combineEncodeObj.containsKey("resolution")){
            resolv = combineEncodeObj.getString("resolution");
        }else{
            resolv = "720*576";
        }
        StringTokenizer resolution = new StringTokenizer(resolv,"*xX");
        int width = Integer.parseInt(resolution.nextToken());
        int height = Integer.parseInt(resolution.nextToken()) ;
        ScaleBO scale = new ScaleBO().setWidth(width)
                .setHeight(height);
        processingBOS.add(new PreProcessingBO().setScale(scale));

        //预处理
        combineEncodeObj.remove("pretreatments");
        if (tmplTaskObj.containsKey("pretreatments")) {
            JSONArray pretreatments = tmplTaskObj.getJSONArray("pretreatments");
            for (int i = 0; i < pretreatments.size(); i++) {
                ProcessVO processVO = JSON.parseObject(pretreatments.getString(i), ProcessVO.class);
                switch (processVO.getTreat_type()){
                    case SCALE:
                        ScaleBO scaleBO = new ScaleBO(processVO);
                        processingBOS.add(new PreProcessingBO().setScale(scaleBO));
                        break;
                    case CUT:
                        CutBO cutBO = new CutBO(processVO);
                        processingBOS.add(new PreProcessingBO().setCut(cutBO));
                        break;
                    case FPS_CONVERT:
                        processingBOS.add(new PreProcessingBO().setFps_convert(new FpsConvertBO(processVO)));
                        break;
                    case TEXTOSD:
                        processingBOS.add(new PreProcessingBO().setText_osd(new TextOsdBO(processVO)));
                        break;
                    case STATIC_PIC_OSD:
                        processingBOS.add(new PreProcessingBO().setStatic_pic_osd(new StaticPictureOsdBO(processVO)));
                        break;
                    case DYNAMIC_PIC_OSD:
                        processingBOS.add(new PreProcessingBO().setDynamic_pic_osd(new DynamicPictureOsdBO(processVO)));
                        break;
                    case FUZZY:
                        processingBOS.add(new PreProcessingBO().setFuzzy(new FuzzyBO(processVO)));
                        break;
                    case HDR:
                        processingBOS.add(new PreProcessingBO().setSDRHDRConvert(new HdrBO(processVO)));
                        break;
                    case ENHANCE:
                        processingBOS.add(new PreProcessingBO().setEnhance(new EnhanceBO(processVO)));
                        break;
                    case IMAGEFILTER:
                        processingBOS.add(new PreProcessingBO().setImageFilter(new ImageFilterBO(processVO)));
                        break;
                }
            }
        }
        return processingBOS;
    }



    public void generateOutputBOS(MissionBO missionBO, TemplateTaskVO combineTaskObj) throws Exception {

        List<OutputBO> outputs = new ArrayList();

        for (int i = 0; i < combineTaskObj.getMap_outputs().size(); i++) {
            JSONObject taskOutput = combineTaskObj.getMap_outputs().getJSONObject(i);
            //处理
            OutputFactory outputFactory = new OutputFactory();
            String outputId = missionBO.getIdCtor().getId(i, IdConstructor.IdType.OUTPUT);
            OutputBO outputBO = outputFactory.getOutputByTemplateOutput(missionBO,outputId,taskOutput);
            outputs.add(outputBO);
        }
        missionBO.setOutput_array(outputs);
    }


    public void init(){
        //初始化任务模板
    }
}
