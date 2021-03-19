package com.sumavision.signal.bvc.mq.PassbyMsg;/**
 * Created by Poemafar on 2020/9/24 17:07
 */

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.suma.venus.resource.pojo.BundlePO;
import com.sumavision.signal.bvc.capacity.TransformModuleService;
import com.sumavision.signal.bvc.capacity.bo.MissionBO;
import com.sumavision.signal.bvc.capacity.bo.input.*;
import com.sumavision.signal.bvc.capacity.bo.output.*;
import com.sumavision.signal.bvc.capacity.bo.source.MediaSourceBO;
import com.sumavision.signal.bvc.capacity.bo.task.*;
import com.sumavision.signal.bvc.common.IdConstructor;
import com.sumavision.signal.bvc.common.enumeration.CommonConstants;
import com.sumavision.signal.bvc.entity.dao.CapacityPermissionPortDAO;
import com.sumavision.signal.bvc.entity.dao.ProgramDAO;
import com.sumavision.signal.bvc.entity.po.CapacityPermissionPortPO;
import com.sumavision.signal.bvc.entity.po.ProgramPO;
import com.sumavision.signal.bvc.entity.po.SourcePO;
import com.sumavision.signal.bvc.mq.bo.PassbyBO;
import com.sumavision.signal.bvc.resource.dao.ResourceBundleDAO;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ClassUtils;
import org.springframework.util.ObjectUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @ClassName: CreateTask
 * @Description TODO
 * @Author: Poemafar
 * @Version：1.0
 * @Date 2020/9/24 17:07
 */
@Component
@Transactional(rollbackFor = Exception.class)
public class CreateTask extends AbstractPassbyMsg  {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateTask.class);


    @Autowired
    TransformModuleService transformModuleService;

    @Autowired
    ResourceBundleDAO resourceBundleDao;

    @Autowired
    CapacityPermissionPortDAO capacityPermissionPortDao;

    @Autowired
    ProgramDAO programDao;

    @Override
    public synchronized void exec(PassbyBO passby) throws Exception {

        Thread.sleep(1500);
        // 解析passby消息，找到所有的输入地址
        List<InputBO> inputBOList = new ArrayList<>();
        String bundleId = passby.getBundle_id();
        if (bundleId==null || bundleId.isEmpty()){
            throw new BaseException(StatusCode.ERROR, "cannot find bundle id from mq message");
        }


//check start
        JSONArray sources = passby.getPass_by_content().getJSONArray("map_sources");
        if (sources==null || sources.isEmpty()){
            throw new BaseException(StatusCode.ERROR,"sources is empty");
        }

        JSONArray outputs = passby.getPass_by_content().getJSONArray("map_outputs");
        if (outputs==null || outputs.isEmpty()){
            throw new BaseException(StatusCode.ERROR,"output_array not exist");
        }
//check end

        CommonConstants.TaskType taskType = CommonConstants.TaskType.TRANSCODE;
        if (passby.getPass_by_content().containsKey("task_common_type")) {
            taskType = CommonConstants.TaskType.valueOf(passby.getPass_by_content().getString("task_common_type"));
        }


        CapacityPermissionPortPO outPerm = capacityPermissionPortDao.findByBundleId(bundleId);
        if (outPerm == null){
            Set<String> inputDeviceIps = new HashSet();
            if (sources==null || sources.isEmpty()){
                throw new BaseException(StatusCode.ERROR,"sources is empty");
            }
            for (int i = 0; i < sources.size(); i++) {
                JSONObject source = sources.getJSONObject(i);
                if (source.containsKey("video_source")) {
                    JSONObject video_source = source.getJSONObject("video_source");
                    String srcBid = video_source.getString("bundle_id");
                    CapacityPermissionPortPO permission = capacityPermissionPortDao.findByBundleId(srcBid);
                    if (permission == null){
                        throw new BaseException(StatusCode.ERROR,"cannot find resource bundle, id: "+srcBid);
                    }
                    if (transformModuleService.beSpecificModuleToReceiveStream(permission.getSourcePOs().get(0).getUrl(),permission.getSourcePOs().get(0).getProtocolType())) {
                        inputDeviceIps.add(permission.getCapacityIp());
                    }
                }
            }


            //找个转换模块 用于建多备源任务-----如果输入源全在一个设备就用那个设备；如果不在可能要转发了
            BundlePO transformModule = null;
            if (inputDeviceIps.size()==1){
                String ip = inputDeviceIps.iterator().next();
                transformModule = transformModuleService.getSpecificTransformModule(ip);
            }else {
                transformModule = transformModuleService.autoChosedTransformModule();
                //todo 此处可能还需个转发逻辑
            }
            if (transformModule == null){
                throw new BaseException(StatusCode.ERROR,"未找到接受收入转换模块");
            }

            outPerm = new CapacityPermissionPortPO();
            outPerm.setBundleId(passby.getBundle_id());
            outPerm.setLayerId(passby.getLayer_id());

//todo
            MissionBO missionBO = new MissionBO();
            missionBO.setDevice_ip(transformModule.getDeviceIp());
            missionBO.setTaskType(taskType);

            if (CommonConstants.TaskType.PASSBY.equals(taskType)){
                createPassbyTask(missionBO,sources,outputs);
            }else {
                CommonConstants.SwitchMode mode = CommonConstants.SwitchMode.FRAME;
                if (passby.getPass_by_content().containsKey("task_common_mode")) {
                    mode = CommonConstants.SwitchMode.valueOf(passby.getPass_by_content().getString("task_common_mode"));
                }
                if (CommonConstants.SwitchMode.DIRECTOR.equals(mode)) {
                    createTsMultiSourceTask(missionBO, sources, outputs, outPerm);
                } else {
                    for (int i = 0; i < sources.size(); i++) {
                        JSONObject source = sources.getJSONObject(i);
                        if (source.containsKey("video_source")) {
                            JSONObject video_source = source.getJSONObject("video_source");
                            String srcBid = video_source.getString("bundle_id");
                            CapacityPermissionPortPO permission = capacityPermissionPortDao.findByBundleId(srcBid);
                            if (permission == null) {
                                throw new BaseException(StatusCode.ERROR, "cannot find resource bundle, id: " + srcBid);
                            }

                            String inputId = permission.getBundleId();//         missionBO.getIdCtor().getId(i, IdConstructor.IdType.INPUT);
                            String url = permission.getSourcePOs().get(0).getUrl();
                            CommonConstants.ProtocolType type = permission.getSourcePOs().get(0).getProtocolType();
                            List<ProgramPO> programPOS = new ArrayList<>();// programDao.findByPermissionPortId(permission.getId().toString());
                            //如果没有节目信息怎么搞
                            InputBO inputBO = transformModuleService.generateInputInCommand(missionBO,inputId, programPOS, type, url, missionBO.getDevice_ip());
                            inputBOList.add(inputBO);
                        }
                    }


                    InputBO inputBackupBO = generateBackupInputBO(inputBOList, outPerm, taskType, mode);
                    inputBOList.add(inputBackupBO);
                    missionBO.setInput_array(inputBOList);

                    if (CommonConstants.TaskType.TRANSCODE.equals(taskType)) {
                        createTranscodeTask(missionBO, inputBackupBO, outputs);
                    } else if (CommonConstants.TaskType.STREAM.equals(taskType)) {
                        createPackageTask(missionBO, inputBackupBO, outputs);
                    }
                }
            }
            outPerm.setCapacityIp(missionBO.getDevice_ip());
            outPerm.setTaskId(missionBO.getIdCtor().getJobId());
            capacityPermissionPortDao.save(outPerm);
//要记录这个任务
//            outPerm.setUrl(); todo 暂不设置，多输出的情况下没法记
//            outPerm.setProtocolType();

        }else{
            //虚拟输出bundle存在的话是不会进这里的
            LOGGER.warn("director task has exist, bundle id:",bundleId);

        }

    }


    public void  createTsMultiSourceTask(MissionBO missionBO,JSONArray sources,JSONArray outputs, CapacityPermissionPortPO outPerm) throws BaseException {

        List<InputBO> inputBOList = new ArrayList<>();

        for (int i = 0; i < sources.size(); i++) {
            JSONObject source = sources.getJSONObject(i);
            if (source.containsKey("video_source")) {
                JSONObject video_source = source.getJSONObject("video_source");
                String srcBid = video_source.getString("bundle_id");
                CapacityPermissionPortPO permission = capacityPermissionPortDao.findByBundleId(srcBid);
                if (permission == null){
                    throw new BaseException(StatusCode.ERROR,"cannot find resource bundle, id: "+srcBid);
                }
                String inputId = permission.getBundleId();
                SourcePO sourcePO = permission.getSourcePOs().get(0);
                MediaSourceBO mediaSourceBO = new MediaSourceBO(sourcePO,missionBO.getDevice_ip());
                InputBO inputBO = transformModuleService.generateTsPassbyInputInCommand(inputId,mediaSourceBO);
                inputBOList.add(inputBO);
            }
        }

        InputBO inputBackupBO = generateTsPassbyBackupInputBO(inputBOList,outPerm);
        inputBOList.add(inputBackupBO);

        missionBO.setInput_array(inputBOList);
        setPassbyTasksInCommond(missionBO, inputBackupBO);
        setPassbyOutputsInCommond(missionBO,outputs);

        transformModuleService.addDirectorTask(missionBO);
    }

    public void setPassbyOutputsInCommond(MissionBO missionBO,JSONArray outputs) throws BaseException {
        List<OutputBO> outputBOs = new ArrayList<>();

        for (int i = 0; i < outputs.size(); i++) {
            JSONObject outputSame =  outputs.getJSONObject(i);
            OutputBO outputBO = new OutputBO();
            String outputId = missionBO.getIdCtor().getId(0, IdConstructor.IdType.OUTPUT);
            outputBO.setId(outputId);

            BaseMediaBO mediaBO = new BaseMediaBO()
                    .setTask_id(missionBO.getIdCtor().getId(0, IdConstructor.IdType.TASK))
                    .setEncode_id(missionBO.getIdCtor().getId(0,IdConstructor.IdType.ENCODE));

            String url = outputSame.getString("url");
            CommonConstants.ProtocolType type = CommonConstants.ProtocolType.getProtocolType(outputSame.getString("type"));
            OutputPassbyBO outputPassbyBO = new OutputPassbyBO(url,missionBO.getDevice_ip());
            outputPassbyBO.setMedia(mediaBO);

            switch (type){
                case UDP_TS:
                    outputBO.setUdp_passby(outputPassbyBO);
                    break;
                case RTP_TS:
                    outputBO.setRtp_ts_passby(outputPassbyBO);
                    break;
                case HTTP_TS:
                    OutputHttpTsPassbyBO passbyBO = new OutputHttpTsPassbyBO(url,missionBO.getDevice_ip());
                    passbyBO.setMedia(mediaBO);
                    outputBO.setHttp_ts_passby(passbyBO);
                    break;
                case SRT_TS:
//                    OutputSrtTsPassbyBO srtBO = new OutputSrtTsPassbyBO(url,missionBO.getDevice_ip());
//                    srtBO.setMode("caller");
//                    srtBO.setMedia(mediaBO);
//                outputBO.setSrt_ts_passby(srtBO);
            }
            outputBOs.add(outputBO);
        }

        missionBO.setOutput_array(outputBOs);
    }


    public void setPassbyTasksInCommond(MissionBO missionBO, InputBO inputBO){

        List<TaskBO> taskBOS = new ArrayList<>();

        TaskBO taskBO = new TaskBO()
                .setId(missionBO.getIdCtor().getId(0, IdConstructor.IdType.TASK))
                .setType("passby")
                .setPassby_source(new TaskSourceBO().setInput_id(inputBO.getId()));

        EncodeBO encodeBO = new EncodeBO()
                .setEncode_id(missionBO.getIdCtor().getId(0, IdConstructor.IdType.ENCODE))
                .setPassby(new JSONObject());

        taskBO.setEncode_array(new ArrayList<>())
                .getEncode_array()
                .add(encodeBO);

        taskBOS.add(taskBO);
        missionBO.setTask_array(taskBOS);
    }


    public InputBO generateTsPassbyBackupInputBO(List<InputBO> inputBOS, CapacityPermissionPortPO outPerm){

        InputBO inputBO = new InputBO();

        inputBO.setId(outPerm.getBundleId());
        BackUpPassByBO backUpPassByBO = new BackUpPassByBO();
//trigger_list不生成
        backUpPassByBO.setMode("manual");
        backUpPassByBO.setTrigger_list(new TriggerListBO().setMedia_lost(false).setPlp_high(false).setCutoff(false));
        backUpPassByBO.setSelect_index("0");

        List<BackUpProgramBO> programPOS = new ArrayList<>();
        for (int i = 0; i < inputBOS.size(); i++) {
            InputBO curInput = inputBOS.get(i);
            BackUpProgramBO backUpProgramBO = new BackUpProgramBO().setInput_id(curInput.getId());
            programPOS.add(backUpProgramBO);
        }

        backUpPassByBO.setProgram_array(programPOS);

        inputBO.setBack_up_passby(backUpPassByBO);
        return inputBO;
    }


    public String getFileToStreamTemplate() throws BaseException {
        InputStream fps = null;
        BufferedReader bfR= null;
        String line = null;
        String content = "";
        try {
            fps = ClassUtils.getDefaultClassLoader().getResourceAsStream("director_template.json");
            bfR = new BufferedReader(new InputStreamReader(fps));
            while((line = bfR.readLine())!=null) {
                content += line;
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new BaseException(StatusCode.ERROR,"fail to get file to stream template");
        }
        return content;
    }

    public void createTranscodeTask(MissionBO missionBO,InputBO inputBackupBO,JSONArray outputs) throws BaseException {
        //读文件
        List<OutputBO> outputBOS = new ArrayList<>();
        List<TaskBO> taskBOS = new ArrayList<>();

        String tpl = getFileToStreamTemplate();
        tpl = tpl.replace("__TASKID1__", missionBO.getIdCtor().getId(0, IdConstructor.IdType.TASK));
        tpl = tpl.replace("__TASKID2__", missionBO.getIdCtor().getId(1, IdConstructor.IdType.TASK));
        tpl = tpl.replace("__ENCODEID1__", missionBO.getIdCtor().getId(0, IdConstructor.IdType.ENCODE));
        tpl = tpl.replace("__ENCODEID2__", missionBO.getIdCtor().getId(1, IdConstructor.IdType.ENCODE));

        JSONObject fileObj = JSONObject.parseObject(tpl);
        JSONObject contentObj = fileObj.getJSONObject("content");
        List<TaskBO> taskBOList = JSONArray.parseArray(contentObj.getString("task_array"),TaskBO.class);
        for (int i = 0; i < taskBOList.size(); i++) {
            TaskBO taskBO = taskBOList.get(i);
            if ("video".equals(taskBO.getType())) { //补充视频编码参数
                for (int j = 0; j < taskBO.getEncode_array().size(); j++) {
                    EncodeBO encodeBO = taskBO.getEncode_array().get(j);
                    String resolution = "";
                    if (encodeBO.getH264() != null && !encodeBO.getH264().containsKey("x264")) {
                        encodeBO.getH264().put("x264", new X264BO());
                        resolution = encodeBO.getH264().getString("resolution");
                    }
                    if (encodeBO.getHevc() != null && !encodeBO.getHevc().containsKey("x265")) {
                        encodeBO.getHevc().put("x265", new X265BO());
                        resolution = encodeBO.getHevc().getString("resolution");
                    }
                    if (encodeBO.getMpeg2() != null && !encodeBO.getMpeg2().containsKey("mpeg2")) {
                        encodeBO.getMpeg2().put("m2v", new Mpeg2BO());
                        resolution = encodeBO.getMpeg2().getString("resolution");
                    }
                    ScaleBO scaleBO = new ScaleBO()
                            .setWidth(Integer.valueOf(resolution.split("x")[0]))
                            .setHeight(Integer.valueOf(resolution.split("x")[1]))
                            .setPlat("cpu")
                            .setNv_card_idx(0)
                            .setMode("slow");
                    encodeBO.setProcess_array(new ArrayList<>()).getProcess_array().add(new PreProcessingBO().setScale(scaleBO));
                }
            }
            if ("audio".equals(taskBO.getType())) {
                taskBO.setDecode_process_array(new ArrayList<>());
                taskBO.getEncode_array().stream().forEach(e -> {
                    if (!e.getAac().containsKey("sample_rate")) {
                        e.getAac().put("sample_rate", "44.1");
                    }
                    ResampleBO resampleBO = new ResampleBO()
                            .setSample_rate(Float.valueOf(Float.valueOf(e.getAac().getString("sample_rate")) * 1000).intValue())
                            .setFormat(e.getAac().containsKey("sample_fmt") ? e.getAac().getString("smaple_fmt") : "s16")
                            .setChannel_layout(e.getAac().containsKey("channel_layout") ? e.getAac().getString("channel_layout") : "mono");
                    AudioGainBO audioGainBO = new AudioGainBO()
                            .setGain_mode("auto")
                            .setVolume(0);
                    e.setProcess_array(new ArrayList<>()).getProcess_array().add(new PreProcessingBO().setResample(resampleBO).setAudioGainBO(audioGainBO));
                });
            }
            String inputId = inputBackupBO.getId();
            Integer programNo = 1;
            Integer elementPid = null;
            if (inputBackupBO.getBack_up_raw() != null) {
                programNo = inputBackupBO.getBack_up_raw().getOutput_program().getProgram_number();
                if ("video".equals(taskBO.getType())) {
                    elementPid = inputBackupBO.getBack_up_raw().getOutput_program().getElement_array().get(0).getPid();
                }
                if ("audio".equals(taskBO.getType())) {
                    elementPid = inputBackupBO.getBack_up_raw().getOutput_program().getElement_array().get(1).getPid();
                }
                if ("passby".equals(taskBO.getType())) {
                    elementPid = inputBackupBO.getBack_up_raw().getOutput_program().getElement_array().get(i).getPid();
                }
            } else if (inputBackupBO.getBack_up_es() != null) {
                programNo = inputBackupBO.getBack_up_es().getOutput_program().getProgram_number();
                if ("video".equals(taskBO.getType())) {
                    elementPid = inputBackupBO.getBack_up_es().getOutput_program().getElement_array().get(0).getPid();
                }
                if ("audio".equals(taskBO.getType())) {
                    elementPid = inputBackupBO.getBack_up_es().getOutput_program().getElement_array().get(1).getPid();
                }
                if ("passby".equals(taskBO.getType())) {
                    elementPid = inputBackupBO.getBack_up_es().getOutput_program().getElement_array().get(i).getPid();
                }
            }
            if (elementPid == null) {
                throw new BaseException(StatusCode.ERROR, "fail to set element pid");
            }
            TaskSourceBO taskSourceBO = generateRawSourceAtTaskInCommand(programNo, elementPid, inputId);
            taskBO.setRaw_source(taskSourceBO);
            taskBOS.add(taskBO);
        }


        missionBO.setTask_array(taskBOS);


        List<OutputMediaBO> mediaBOS = new ArrayList<>();
        mediaBOS.add(new OutputMediaBO()
                .setType("video")
                .setTask_id(missionBO.getIdCtor().getId(0, IdConstructor.IdType.TASK))
                .setEncode_id(missionBO.getIdCtor().getId(0, IdConstructor.IdType.ENCODE))
                .setPid(256)
        );
        mediaBOS.add(new OutputMediaBO()
                .setType("audio")
                .setTask_id(missionBO.getIdCtor().getId(1, IdConstructor.IdType.TASK))
                .setEncode_id(missionBO.getIdCtor().getId(1, IdConstructor.IdType.ENCODE))
                .setPid(257)
        );

        for (int i = 0; i < outputs.size(); i++) {
            JSONObject output = outputs.getJSONObject(i);
            OutputBO curOutputBO = new OutputBO();
            curOutputBO.setId(missionBO.getIdCtor().getId(0, IdConstructor.IdType.OUTPUT));
            String url = output.getString("url");
            CommonConstants.ProtocolType type = CommonConstants.ProtocolType.getProtocolType(output.getString("type"));
            switch (type){
                case UDP_TS:
                    BaseTsOutputBO baseTsOutputBO = new BaseTsOutputBO(url,missionBO.getDevice_ip());
                    baseTsOutputBO.setRate_ctrl("VBR");
                    baseTsOutputBO.setBitrate(3000000);
                    baseTsOutputBO.setProgram_array(new ArrayList<>()).getProgram_array().add(
                            new OutputProgramBO().setProgram_number(301)
                                    .setMedia_array(mediaBOS)
                    );
                    curOutputBO.setUdp_ts(baseTsOutputBO);
                    break;
                case RTP_TS:
                    BaseTsOutputBO rtpBO = new BaseTsOutputBO(url,missionBO.getDevice_ip());
                    rtpBO.setRate_ctrl("VBR");
                    rtpBO.setBitrate(3000000);
                    rtpBO.setProgram_array(new ArrayList<>()).getProgram_array().add(
                            new OutputProgramBO().setProgram_number(301)
                                    .setMedia_array(mediaBOS)
                    );
                    curOutputBO.setRtp_ts(rtpBO);
                    break;
                default:
                    throw new BaseException(StatusCode.ERROR,"not support package type :"+type.name());
            }
            outputBOS.add(curOutputBO);
        }
        missionBO.setOutput_array(outputBOS);

        transformModuleService.addDirectorTask(missionBO);
    }

    public void createPackageTask(MissionBO missionBO, InputBO inputBO, JSONArray outputs) throws BaseException {
        //直接透传
        List<TaskBO> taskBOS = new ArrayList<>();
        List<OutputBO> outputBOS = new ArrayList<>();
        Integer programNum = inputBO.getBack_up_es().getOutput_program().getProgram_number();
        Integer vPid = inputBO.getBack_up_es().getOutput_program().getElement_array().get(0).getPid();
        Integer aPid = inputBO.getBack_up_es().getOutput_program().getElement_array().get(1).getPid();


        TaskBO vTaskBO = new TaskBO();
        vTaskBO.setId(missionBO.getIdCtor().getId(0, IdConstructor.IdType.TASK));
        List<EncodeBO> vEncodeBOS = new ArrayList<>();
        vEncodeBOS.add(
                new EncodeBO()
                .setEncode_id(missionBO.getIdCtor().getId(0, IdConstructor.IdType.ENCODE))
                .setPassby(new JSONObject())
        );
        vTaskBO.setType("passby");
        vTaskBO.setEncode_array(vEncodeBOS);
        TaskSourceBO taskSourceBO = generateEsSourceAtTaskInCommand(programNum,vPid,inputBO.getId());
        vTaskBO.setEs_source(taskSourceBO);
        taskBOS.add(vTaskBO);

        TaskBO aTaskBO = new TaskBO();
        aTaskBO.setId(missionBO.getIdCtor().getId(1, IdConstructor.IdType.TASK));
        List<EncodeBO> aEncodeBOS = new ArrayList<>();
        aEncodeBOS.add(
                new EncodeBO()
                        .setEncode_id(missionBO.getIdCtor().getId(1, IdConstructor.IdType.ENCODE))
                        .setPassby(new JSONObject())
        );
        aTaskBO.setType("passby");
        aTaskBO.setEncode_array(aEncodeBOS);
        TaskSourceBO aTaskSourceBO = generateEsSourceAtTaskInCommand(programNum,aPid,inputBO.getId());
        aTaskBO.setEs_source(aTaskSourceBO);
        taskBOS.add(aTaskBO);

        missionBO.setTask_array(taskBOS);


        List<OutputMediaBO> mediaBOS = new ArrayList<>();
        mediaBOS.add(new OutputMediaBO()
                .setType("video")
                .setTask_id(missionBO.getIdCtor().getId(0, IdConstructor.IdType.TASK))
                .setEncode_id(missionBO.getIdCtor().getId(0, IdConstructor.IdType.ENCODE))
                .setPid(256)
        );
        mediaBOS.add(new OutputMediaBO()
                .setType("audio")
                .setTask_id(missionBO.getIdCtor().getId(1, IdConstructor.IdType.TASK))
                .setEncode_id(missionBO.getIdCtor().getId(1, IdConstructor.IdType.ENCODE))
                .setPid(257)
        );


        for (int i = 0; i < outputs.size(); i++) {
            JSONObject output = outputs.getJSONObject(i);
            OutputBO curOutputBO = new OutputBO();
            curOutputBO.setId(missionBO.getIdCtor().getId(0, IdConstructor.IdType.OUTPUT));
            String url = output.getString("url");
            CommonConstants.ProtocolType type = CommonConstants.ProtocolType.getProtocolType(output.getString("type"));
            switch (type){
                case UDP_TS:
                    BaseTsOutputBO baseTsOutputBO = new BaseTsOutputBO(url);
                    baseTsOutputBO.setRate_ctrl("VBR");
                    baseTsOutputBO.setBitrate(8000000);
                    baseTsOutputBO.setProgram_array(new ArrayList<>()).getProgram_array().add(
                            new OutputProgramBO().setProgram_number(301)
                            .setMedia_array(mediaBOS)
                    );
                    curOutputBO.setUdp_ts(baseTsOutputBO);
                    break;
                default:
                    throw new BaseException(StatusCode.ERROR,"not support package type :"+type.name());
            }

            outputBOS.add(curOutputBO);
        }
        missionBO.setOutput_array(outputBOS);

        transformModuleService.addDirectorTask(missionBO);
    }


    public void createPassbyTask(MissionBO missionBO, JSONArray sources, JSONArray outputs) throws BaseException {

        for (int i = 0; i < sources.size(); i++) {
            JSONObject source = sources.getJSONObject(i);
            if (source.containsKey("video_source")) {
                JSONObject video_source = source.getJSONObject("video_source");
                String srcBid = video_source.getString("bundle_id");
                CapacityPermissionPortPO permission = capacityPermissionPortDao.findByBundleId(srcBid);
                if (permission == null){
                    throw new BaseException(StatusCode.ERROR,"cannot find resource bundle, id: "+srcBid);
                }
                missionBO.setInUrl(permission.getSourcePOs().get(0).getUrl());
                missionBO.setInType(permission.getSourcePOs().get(0).getProtocolType());
                missionBO.setSrtMode(permission.getSourcePOs().get(0).getSrtMode());
                break;
            }
        }
        if (outputs.size()==1){
            JSONObject output = outputs.getJSONObject(0);
            CommonConstants.ProtocolType outType = CommonConstants.ProtocolType.getProtocolType(output.getString("type"));
            missionBO.setOutType(outType);
            missionBO.setOutUrl(output.getString("url"));

        }else {
            throw new BaseException(StatusCode.ERROR,"map_outputs size not right");
        }

        transformModuleService.addTransferTask(missionBO);
    }


    public TaskSourceBO generateRawSourceAtTaskInCommand(Integer programNo, Integer element_pid,String inputId){
        TaskSourceBO taskSourceBO = new TaskSourceBO();
        taskSourceBO.setElement_pid(element_pid);
        taskSourceBO.setInput_id(inputId);
        taskSourceBO.setProgram_number(programNo);
        return taskSourceBO;
    }

    public TaskSourceBO generateEsSourceAtTaskInCommand(Integer programNo, Integer element_pid,String inputId){
        TaskSourceBO taskSourceBO = new TaskSourceBO();
        taskSourceBO.setElement_pid(element_pid);
        taskSourceBO.setInput_id(inputId);
        taskSourceBO.setProgram_number(programNo);
        return taskSourceBO;
    }

    public InputBO generateBackupInputBO(List<InputBO> inputBOS, CapacityPermissionPortPO outPerm, CommonConstants.TaskType taskType, CommonConstants.SwitchMode mode) throws BaseException {
        InputBO inputBO = new InputBO();
        inputBO.setId(outPerm.getBundleId());
        BackUpEsAndRawBO backUpEsAndRawBO = new BackUpEsAndRawBO();
//trigger_list不生成
        backUpEsAndRawBO.setMode("manual");
        List<BackUpProgramBO> programPOS = new ArrayList<>();
        for (int i = 0; i < inputBOS.size(); i++) {
            InputBO curInput = inputBOS.get(i);
            ProgramBO curProg = curInput.getProgram_array().get(0);//取每个输入的第一个节目
            BackUpProgramBO backUpProgramBO = new BackUpProgramBO();
            backUpProgramBO.setProgram_number(curProg.getProgram_number());
            backUpProgramBO.setInput_id(curInput.getId());
            backUpProgramBO.setElement_array(new ArrayList<>());
            if (curProg.getVideo_array()==null || curProg.getVideo_array().isEmpty()){
                LOGGER.error("videos null, input id:{},first prog no:{}",curInput.getId(),curProg.getProgram_number());
                throw new BaseException(StatusCode.ERROR,"program video is null");
            }
            curProg.getVideo_array().stream().forEach(v->{
                backUpProgramBO.getElement_array().add(new ProgramElementBO().setPid(v.getPid()).setType("video"));
            });
            curProg.getAudio_array().stream().forEach(a->{
                backUpProgramBO.getElement_array().add(new ProgramElementBO().setPid(a.getPid()).setType("audio"));
            });
            programPOS.add(backUpProgramBO);
        }
        backUpEsAndRawBO.setProgram_array(programPOS);

        ProgramOutputBO programOutputBO = new ProgramOutputBO();
        programOutputBO.setProgram_number(1);//节目号随便设置

        List<ProgramElementBO> elementBOS = new ArrayList<>();
        Integer[] pidIndex = {0};
        inputBOS.get(0).getProgram_array().get(0).getVideo_array().stream().forEach(v->{
            ProgramElementBO programElementBO = new ProgramElementBO();
            programElementBO.setPid(v.getPid());
            List<PidIndexBO> program_switch_array = new ArrayList<>();
            for (int i=0;i<programPOS.size();i++){
                program_switch_array.add(new PidIndexBO().setPid_index(pidIndex[0]));
            }
            programElementBO.setProgram_switch_array(program_switch_array);
            elementBOS.add(programElementBO);
            pidIndex[0] = pidIndex[0] +1;
        });
        inputBOS.get(0).getProgram_array().get(0).getAudio_array().stream().forEach(a->{
            ProgramElementBO programElementBO = new ProgramElementBO();
            programElementBO.setPid(a.getPid());
            List<PidIndexBO> program_switch_array = new ArrayList<>();
            for (int i=0;i<programPOS.size();i++){
                program_switch_array.add(new PidIndexBO().setPid_index(pidIndex[0]));
            }
            programElementBO.setProgram_switch_array(program_switch_array);
            elementBOS.add(programElementBO);
            pidIndex[0] = pidIndex[0] +1;
        });
        programOutputBO.setElement_array(elementBOS);
        backUpEsAndRawBO.setOutput_program(programOutputBO);//todo 带设置
        backUpEsAndRawBO.setSelect_index("0");

        List<ProgramBO> programs = new ArrayList();
        for (int i = 0; i < inputBOS.get(0).getProgram_array().size(); i++) {
            String progStr = JSON.toJSONString(inputBOS.get(0).getProgram_array().get(i)) ;
            ProgramBO programBO = JSONObject.parseObject(progStr,ProgramBO.class);
            if (CommonConstants.TaskType.TRANSCODE.equals(taskType)) {
                programBO.setNormal_map(new JSONObject());
                programBO.setMedia_type_once_map(null);
            }
            programs.add(programBO);
        }
        inputBO.setProgram_array(programs);

        if (CommonConstants.TaskType.TRANSCODE.equals(taskType)) {
            inputBO.setBack_up_raw(backUpEsAndRawBO);
        }else{
            if (CommonConstants.SwitchMode.FRAME.equals(mode)){
                JSONObject option = new JSONObject();
                option.put("i_frame_switch",true);
                backUpEsAndRawBO.setOptions(option);
                inputBO.setBack_up_es(backUpEsAndRawBO);
            }else if (CommonConstants.SwitchMode.DIRECTOR.equals(mode)){
                JSONObject option = new JSONObject();
                option.put("i_frame_switch",false);
                backUpEsAndRawBO.setOptions(option);
                inputBO.setBack_up_es(backUpEsAndRawBO);
            }else{
                throw new BaseException(StatusCode.ERROR,"not support mode: "+mode);
            }
        }
        return inputBO;
    }






}
