package com.sumavision.tetris.application.template;/**
 * Created by Poemafar on 2020/11/3 16:40
 */

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.business.common.MissionBO;
import com.sumavision.tetris.business.common.Util.CommonUtil;
import com.sumavision.tetris.business.common.Util.IdConstructor;
import com.sumavision.tetris.business.common.enumeration.BusinessType;
import com.sumavision.tetris.business.common.enumeration.MediaType;
import com.sumavision.tetris.business.common.enumeration.TaskType;
import com.sumavision.tetris.business.common.exception.CommonException;
import com.sumavision.tetris.business.transcode.service.TranscodeTaskService;
import com.sumavision.tetris.capacity.bo.input.InputBO;
import com.sumavision.tetris.capacity.bo.input.ProgramAudioBO;
import com.sumavision.tetris.capacity.bo.input.ProgramBO;
import com.sumavision.tetris.capacity.bo.input.ProgramVideoBO;
import com.sumavision.tetris.capacity.bo.output.OutputBO;
import com.sumavision.tetris.capacity.bo.task.*;
import com.sumavision.tetris.capacity.constant.EncodeConstant;
import com.sumavision.tetris.capacity.template.TemplateService;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

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

    public void addTask(TemplateVO taskVO) throws Exception {
        TemplatePO tmpPO = getTemplateByName(taskVO.getTemplate());
        TemplateVO templateVO = JSONObject.parseObject(tmpPO.getBody(), TemplateVO.class);
        LOGGER.info("template params: {}",JSON.toJSONString(templateVO));
        //组合
//        TemplateVO tmpBO = CommonUtil.combineSydwCore(templateVO,taskVO);
        JSONObject templateObj = JSONObject.parseObject(JSON.toJSONString(templateVO));
        JSONObject taskObj = JSONObject.parseObject(JSON.toJSONString(taskVO));
        JSONObject combineTaskObj = CommonUtil.coverJSONObject(templateObj,taskObj);
        LOGGER.info("task and template combine params: {}",combineTaskObj.toJSONString());

        TemplateVO tmpBO = JSONObject.parseObject(combineTaskObj.toJSONString(),TemplateVO.class);
        //开始转换参数
        MissionBO missionBO = new MissionBO();
        missionBO.setIdCtor(new IdConstructor());
        missionBO.setTaskType(tmpPO.getTaskType());
        missionBO.setDevice_ip(taskVO.getTask_ip());

        generateInputBOS(missionBO,tmpBO);
        if (TaskType.TRANS.equals(tmpPO.getTaskType())) {
            generateTaskBOS(missionBO, tmpBO);
        }else if (TaskType.PACKAGE.equals(tmpPO.getTaskType())){
            generatePackageTaskBOS(missionBO);
        }else if (TaskType.PASSBY.equals(tmpPO.getTaskType())){
//            generateTaskBOS();
        }
        generateOutputBOS(missionBO,tmpBO);

        transcodeTaskService.save(missionBO.getIdCtor().getJobId(), taskVO.getTask_ip(), missionBO.getInput_array()  , missionBO.getTask_array(), missionBO.getOutput_array(), BusinessType.DEFAULT);
    }


    public TemplatePO getTemplateByName(String tmpName) throws BaseException {
        TemplatePO tmp = templateDAO.findByName(tmpName);
        if (tmp==null){
            throw new BaseException(StatusCode.ERROR,"template not exist, name:"+tmpName);
        }
        return tmp;
    }

    /**
     * 添加模板
     * 从前台页面生成
     * @param templateVO
     */
    public void addTemplate(TemplateVO templateVO){

    }

    /**
     * 删除模板
     */
    public void deleteTemplate(Long id){
        templateDAO.delete(id);
    }

    public void generateInputBOS(MissionBO missionBO,TemplateVO tmplBO) throws  BaseException{
        List<InputBO> inputs = new ArrayList();
        InputFactory inputFactory = new InputFactory();
        for (int i = 0; i < tmplBO.getMap_sources().size(); i++) {
            SourceVO sourceVO = tmplBO.getMap_sources().get(i);
            InputBO inputBO = inputFactory.getInputByTemplateInput(missionBO, sourceVO);
            inputs.add(inputBO);
        }
        missionBO.setInput_array(inputs);
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
                .setType("video")
                .setEncode_array(vEncodeBOS);
        videoTaskBO.setTaskSource(missionBO,null);
        videoTaskBO.setEncode_array(vEncodeBOS);
        taskBOS.add(videoTaskBO);
//audio
        aEncodeBOS.add( new EncodeBO()
                .setPassby(new JSONObject())
                .setEncode_id(missionBO.getIdCtor().getId(1, IdConstructor.IdType.ENCODE)));
        missionBO.getOutEncodeMap().put(1, missionBO.getIdCtor().getId(1, IdConstructor.IdType.ENCODE));
        audioTaskBO.setId(missionBO.getIdCtor().getId(1, IdConstructor.IdType.TASK))
                .setType("audio")
                .setEncode_array(aEncodeBOS);
        audioTaskBO.setTaskSource(missionBO,null);
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
    public void generateTaskBOS(MissionBO missionBO,TemplateVO tmplBO) throws BaseException, CommonException {
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
                if (MediaType.VIDEO.equals(mediaType)){
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
                    encodeBO.setProcess_array(handleProcessList(tmplTaskObj,videoEncObj));

                    switch (videoType){
                        case h264:
                            encodeBO.setH264(videoEncObj);
                            break;
                        case hevc:
                            encodeBO.setHevc(videoEncObj);
                            break;
                        case mpeg2:
                            encodeBO.setMpeg2(videoEncObj);
                            break;
                        case avs:
                            encodeBO.setAvs2(videoEncObj);
                            break;
                    }
                    vEncodeBOS.add(encodeBO);
                    videoTaskBO.setDecode_process_array(new ArrayList<>());//解码预处理
                    videoTaskBO.setTaskSource(missionBO,null);//有透传有不透传，该发哪个source
                }else if(MediaType.AUDIO.equals(mediaType)){
                    if (audioTaskBO.getId()==null || audioTaskBO.getId().isEmpty()) {
                        audioTaskBO.setId(missionBO.getIdCtor().getId(mediaOrder++, IdConstructor.IdType.TASK));
                        audioTaskBO.setType("audio");
                    }
                    EncodeConstant.TplAudioEncoder audioType = EncodeConstant.TplAudioEncoder.getTplAudioEncoder(codec);
                    JSONObject audioEncObj = combineAudioEncode(tmplTaskObj, templateService.getAudioEncodeMap(audioType.name()));
                    encodeBO.setProcess_array(handleProcessList(tmplTaskObj,audioEncObj));
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
                } else if (MediaType.SUBTITLE.equals(mediaType)){
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
        JSONObject combineObj = CommonUtil.coverJSONObject( JSONObject.parseObject(params),tmplTaskObj);
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
        }
        if (tmplTaskObj.containsKey("bitrate")){
            Long bitrate = tmplTaskObj.getLong("bitrate");
            combineObj.put("bitrate",bitrate);
            if (bitrate > combineObj.getLong("max_bitrate")){
                combineObj.put("max_bitrate",bitrate);
            }
        }
        return combineObj;
    }

    public JSONObject combineAudioEncode(JSONObject tmplTaskObj,String params){
        JSONObject combineObj = CommonUtil.coverJSONObject(JSONObject.parseObject(params),tmplTaskObj);
        if (tmplTaskObj.containsKey("abitrate")){
            combineObj.put("bitrate",tmplTaskObj.getLong("abitrate"));
        }
        if (tmplTaskObj.containsKey("sample_rate")){
            combineObj.put("sample_rate",String.valueOf(tmplTaskObj.getInteger("sample_rate")/1000.0f));
        }
        return combineObj;
    }

    public List handleProcessList(JSONObject tmplTaskObj,JSONObject combineEncodeObj){
        List<PreProcessingBO> processingBOS = new ArrayList<>();

        if (tmplTaskObj.containsKey("resolution")) {
            String resolv = tmplTaskObj.getString("resolution");
            StringTokenizer resolution = new StringTokenizer(resolv,"*xX");
            int width = Integer.parseInt(resolution.nextToken());
            int height = Integer.parseInt(resolution.nextToken()) ;
            ScaleBO scale = new ScaleBO().setWidth(width)
                    .setHeight(height);
            processingBOS.add(new PreProcessingBO().setScale(scale));
        }
        if (tmplTaskObj.containsKey("sample_rate")) {
            Integer smpRate = tmplTaskObj.getInteger("sample_rate");
            ResampleBO resample = new ResampleBO().setSample_rate(smpRate)
                    .setChannel_layout(tmplTaskObj.containsKey("channel_layout")? tmplTaskObj.getString("channel_layout"): combineEncodeObj.getString("channel_layout"))
                    .setFormat(tmplTaskObj.containsKey("sample_fmt")?tmplTaskObj.getString("sample_fmt"):combineEncodeObj.getString("sample_fmt"));
            processingBOS.add(new PreProcessingBO().setResample(resample));
        }

        //预处理
        combineEncodeObj.remove("pretreatments");
        if (tmplTaskObj.containsKey("pretreatments")) {
            JSONArray pretreatments = tmplTaskObj.getJSONArray("pretreatments");
            for (int i = 0; i < pretreatments.size(); i++) {
                ProcessVO processVO = JSONObject.parseObject(pretreatments.getString(i), ProcessVO.class);
                switch (processVO.getTreat_type()){
                    case SCALE:
                        ScaleBO scaleBO = new ScaleBO(processVO);
                        processingBOS.add(new PreProcessingBO().setScale(scaleBO));
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

    public JSONObject coverNameAndValue(TaskVO taskVO,JSONObject target){
        Class sourceBeanClass = taskVO.getClass();
        Field[] sourceFields = sourceBeanClass.getDeclaredFields();
        for(int i=0; i<sourceFields.length; i++) {
            Field sourceField = sourceFields[i];
            if (Modifier.isStatic(sourceField.getModifiers())) {
                continue;
            }
            sourceField.setAccessible(true);
            try {
                if (!(sourceField.get(taskVO) == null) && !"serialVersionUID".equals(sourceField.getName().toString())) {
                    target.put(sourceField.getName(), sourceField.get(taskVO));
                }
            } catch (IllegalArgumentException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return target;
    }

    public void generateOutputBOS(MissionBO missionBO, TemplateVO combineTaskObj) throws Exception {

        List<OutputBO> outputs = new ArrayList();

        for (int i = 0; i < combineTaskObj.getMap_outputs().size(); i++) {
            JSONObject taskOutput = combineTaskObj.getMap_outputs().getJSONObject(i);


            //处理
            OutputFactory outputFactory = new OutputFactory();
            OutputBO outputBO = outputFactory.getOutputByTemplateOutput(missionBO,taskOutput);
            outputs.add(outputBO);

        }

        missionBO.setOutput_array(outputs);
    }


    public void init(){
        //初始化任务模板
    }
}
