package com.sumavision.signal.bvc.capacity;
/**
 * Created by Poemafar on 2020/9/1 11:21
 */

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.suma.venus.resource.pojo.BundlePO;
import com.sumavision.signal.bvc.capacity.bo.MissionBO;
import com.sumavision.signal.bvc.capacity.bo.input.*;
import com.sumavision.signal.bvc.capacity.bo.source.MediaSourceBO;
import com.sumavision.signal.bvc.common.CommonUtil;
import com.sumavision.signal.bvc.common.enumeration.CommonConstants;
import com.sumavision.signal.bvc.common.enumeration.CommonConstants.ProtocolType;
import com.sumavision.signal.bvc.entity.dao.CapacityPermissionPortDAO;
import com.sumavision.signal.bvc.entity.dao.ProgramDAO;
import com.sumavision.signal.bvc.entity.po.ProgramPO;
import com.sumavision.signal.bvc.resource.dao.ResourceBundleDAO;
import com.sumavision.tetris.bvc.business.dispatch.bo.AudioParamBO;
import com.sumavision.tetris.capacity.server.CapacityService;
import com.sumavision.tetris.capacity.server.DirectorService;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @ClassName: TransformService
 * @Description TODO
 * @Author: Poemafar
 * @Version：1.0
 * @Date 2020/9/1 11:21
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TransformModuleService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransformModuleService.class);

    @Autowired
    CapacityService capacityService;

    @Autowired
    DirectorService directorService;

    @Autowired
    private ResourceBundleDAO resourceBundleDao;

    @Autowired
    private CapacityPermissionPortDAO capacityPermissionPortDao;

    @Autowired
    private ProgramDAO programDAO;


    /**
     * udp,rtmp单播必须特定模块才能收到流的
     * @param url
     * @param type
     * @return true 必须指定模块
     */
    public Boolean beSpecificModuleToReceiveStream(String url,ProtocolType type){
        Boolean flag = false;
        if (type.equals(ProtocolType.UDP_TS)||type.equals(ProtocolType.RTP_TS)){
            String sourceIp = CommonUtil.getIpFromUrl(url);
            Boolean beMulti =  CommonUtil.isMulticast(sourceIp);
            if (!beMulti){
                flag = true;
            }else{
                flag = false;
            }
        }else{
            flag = false;
        }
        return flag;
    }

    /**
     * 按IP查特定设备
     * @param ip
     * @return
     */
    public BundlePO getSpecificTransformModule(String ip){
        BundlePO chosedModule = null;

        List<BundlePO> bundles = resourceBundleDao.findByDeviceModel("transcode");
        if (bundles == null || bundles.isEmpty()){
            LOGGER.error("no transform module");
            return null;
        }
        for (int i = 0; i < bundles.size(); i++) {
            BundlePO curBundle  = bundles.get(i);
            if (ip.equals(curBundle.getDeviceIp())) {
                chosedModule = bundles.get(i);
            }
        }
        return chosedModule;
    }

    public BundlePO autoChosedTransformModule(){
        BundlePO chosedModule = null;
        List<BundlePO> bundles = resourceBundleDao.findByDeviceModel("transcode");
        if (bundles.isEmpty()){
            LOGGER.error("no transform module");
            return null;
        }

        //todo 需要判断设备是否离线

        //判断下转换上的任务排序
        Integer taskNum = Integer.MAX_VALUE;
        for (int i = 0; i < bundles.size(); i++) {
            BundlePO curBundle = bundles.get(i);
            Integer curBundleTaskNum = capacityPermissionPortDao.countAllByCapacityIpAndTaskIdNotNull(curBundle.getDeviceIp());
            if (curBundleTaskNum < taskNum) {
                taskNum = curBundleTaskNum;
                chosedModule = curBundle;
            }
        }

        return chosedModule;
    }
//todo 此处有问题
    public void refreshSourceAndSaveSourceInfo(String permissionId, String transformIp, MediaSourceBO mediaSourceBO) throws Exception {
        String sourceParseInfo = refreshSource(transformIp, mediaSourceBO);
        JSONObject inputObject = JSONObject.parseObject(sourceParseInfo);
        JSONArray programArray = inputObject.getJSONArray("program_array");
        for (int i=0; i<programArray.size();i++) {
            ProgramPO programPO  = JSONObject.toJavaObject(programArray.getJSONObject(i), ProgramPO.class);
            if(programPO.getProgramNum() == null){ // 节目号为空是要报错的
                throw new BaseException(StatusCode.ERROR,"program number is null");
            }
            programDAO.save(programPO);
        }
    }

    /**
     * @param transformIp
     * 返回刷源信息
     * @throws Exception
     */
    public String refreshSource(String transformIp, MediaSourceBO mediaSourceBO) throws Exception {

        JSONObject refreshSource = new JSONObject();

        InputBO inputBO = new InputBO();
        inputBO.setNormal_map(new JSONObject());
        inputBO.setEncapsulateInfo(mediaSourceBO);
        refreshSource.put("device_ip",transformIp);
        refreshSource.put("input",inputBO);
        String uuid = UUID.randomUUID().toString();
        LOGGER.info("[capacity]<analysis-input> req, uid:{}, body: {}", uuid, refreshSource.toJSONString());
        String response = capacityService.analysisInput(refreshSource.toString());
        LOGGER.info("[capacity]<analysis-input> resp, uid:{}, result: {}",uuid, response);
        JSONObject inputObject = JSON.parseObject(response);
        if (inputObject.containsKey("result_code") && !inputObject.getInteger("result_code").equals(0)){
            throw new BaseException(StatusCode.ERROR,"transform module refresh source error");
        }
        return response;
    }

    /**
     * 能力服务创建任务
     */
    public String addDirectorTask(MissionBO missionBO) throws BaseException {
        String result = "";
        JSONObject params = new JSONObject();
        params.put("device_ip",missionBO.getDevice_ip());
        params.put("task_id",missionBO.getIdCtor().getJobId());
        params.put("input_array",missionBO.getInput_array());
        params.put("task_array",missionBO.getTask_array());
        params.put("output_array",missionBO.getOutput_array());
        try {
            String uuid = UUID.randomUUID().toString();
            LOGGER.info("[capacity]<add-task> send, msg:{}, params: {}",uuid, params.toJSONString());
            result = directorService.addDirectorTask(params.toJSONString());
            LOGGER.info("[capacity]<add-task> ack, msg:{}, result: {}", uuid, result);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BaseException(StatusCode.ERROR,"capacity service add task error");
        }
        return result;
    }

    /**
     * 能力服务创建任务
     */
    public String addTransferTask(MissionBO missionBO) throws BaseException {
        String result = "";
        JSONObject params = new JSONObject();
        params.put("device_ip",missionBO.getDevice_ip());
        params.put("mission_id",missionBO.getIdCtor().getJobId());
        params.put("inUrl",missionBO.getInUrl());
        params.put("outUrl",missionBO.getOutUrl());
        params.put("srtMode",missionBO.getSrtMode());
        params.put("inType",missionBO.getInType().name().toLowerCase());
        params.put("outType",missionBO.getOutType().name().toLowerCase());
        try {
            String uuid = UUID.randomUUID().toString();
            LOGGER.info("[capacity]<transfer-task> send, msg:{}, params: {}",uuid, params.toJSONString());
            result = directorService.transferTask(params.toJSONString());
            LOGGER.info("[capacity]<transfer-task> ack, msg:{}, result: {}", uuid, result);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BaseException(StatusCode.ERROR,"capacity service add task error");
        }
        return result;
    }

    public String delDirectorTask(String taskId) throws Exception {
        JSONObject delTaskObj = new JSONObject();
        delTaskObj.put("task_id",taskId);
        String result = directorService.delDirectorTask(delTaskObj.toJSONString());
        return result;
    }

    /**
     * 生成节目数组
     * @param programPOS
     * @param decode_mode
     * @return
     */
    public List generateProgramArrayAtInputInCommand(List<ProgramPO> programPOS, String decode_mode  ){
         List<ProgramBO> inputProgramBOS = new ArrayList<>();
         List<ProgramVideoBO> programVideoBOS = new ArrayList<>();
         List<ProgramAudioBO> programAudioBOS = new ArrayList<>();

         if (programPOS.isEmpty()||programPOS==null){
             if (decode_mode!=null && !decode_mode.isEmpty()) {
                 programVideoBOS.add(new ProgramVideoBO().setPid(512).setDecode_mode(decode_mode));
                 programAudioBOS.add(new ProgramAudioBO().setPid(513).setDecode_mode(decode_mode));
             }else{
                 programVideoBOS.add(new ProgramVideoBO().setPid(512));
                 programAudioBOS.add(new ProgramAudioBO().setPid(513));
             }
             inputProgramBOS.add(new ProgramBO()
                     .setProgram_number(1)
                     .setMedia_type_once_map(new JSONObject())
                     .setVideo_array(programVideoBOS)
                     .setAudio_array(programAudioBOS)
             );
         }else {

             for (int i = 0; i < programPOS.size(); i++) {
                 ProgramPO programPO = programPOS.get(i);
                 ProgramBO programBO = generateProgramInCommand(programPO, decode_mode);
                 inputProgramBOS.add(programBO);
             }
         }
        return inputProgramBOS;
    }

    public InputBO generateTsPassbyInputInCommand(String inputId, MediaSourceBO mediaSourceBO){
        InputBO inputBO = new InputBO();
        inputBO.setId(inputId);
        inputBO.setEncapsulateInfo(mediaSourceBO);
        return inputBO;
    }


    public InputBO generateInputInCommand(String inputId, List<ProgramPO> programPOS, ProtocolType type, String url,  String localIp) throws BaseException {
        return generateInputInCommand(null,inputId,programPOS,type,url,localIp);
    }
    /**
     * 生成input
     * @param inputId  输入id
     * @param programPOS 信源节目信息
     * @param type 信源封装类型
     * @param url 信源地址
     * @param localIp 收流网口
     * @return
     * @throws BaseException
     */
    public InputBO generateInputInCommand(MissionBO missionBO, String inputId, List<ProgramPO> programPOS, ProtocolType type, String url,  String localIp) throws BaseException {
        InputBO inputBO = new InputBO();

        List<ProgramBO> program_array = new ArrayList<>();
        if (CommonConstants.TaskType.TRANSCODE.equals(missionBO.getTaskType())) {
            program_array = generateProgramArrayAtInputInCommand(programPOS, "cpu");
        }else{
            program_array = generateProgramArrayAtInputInCommand(programPOS, null);
        }

        inputBO.setProgram_array(program_array);

        if (type.equals(ProtocolType.FILE)){
            inputBO.setMedia_type_once_map(new JSONObject());
        }else {
            inputBO.setNormal_map(new JSONObject());
        }
        inputBO.setId(inputId);
        MediaSourceBO mediaSourceBO =new MediaSourceBO()
                .setUrl(url)
                .setLocalIp(localIp)
                .setProtocolType(type);
        inputBO.setEncapsulateInfo(mediaSourceBO);
        return inputBO;
    }


    public ProgramBO generateProgramInCommand(ProgramPO programPO, String programDecodeType){
        ProgramBO createInputProgramBO = new ProgramBO();

        createInputProgramBO.setName(programPO.getName());
        createInputProgramBO.setPcr_pid(programPO.getPcrPid());
        createInputProgramBO.setProgram_number(programPO.getProgramNum());
        createInputProgramBO.setProvider(programPO.getProvider());
        createInputProgramBO.setPmt_pid(programPO.getPmtPid());
        switch (programPO.getProgramMapType()){
            case  "normal_map":
                createInputProgramBO.setNormal_map(new JSONObject());
                break;
            case "media_type_once_map":
                createInputProgramBO.setMedia_type_once_map(new JSONObject());
                break;
            case "program_number_map":
                createInputProgramBO.setProgram_number_map(new JSONObject());
                break;
            case "pid_map":
                createInputProgramBO.setPid_map(new JSONObject());
                break;
            default:
                createInputProgramBO.setNormal_map(new JSONObject());
        }

        //decode_mode修改，后续在页面上添加后修改
        List<ProgramVideoBO> video_array = JSON.parseArray(programPO.getVideoJson(), ProgramVideoBO.class);
        if (video_array!=null && video_array.size()>0){
            for (ProgramVideoBO programVideoBO : video_array) {
                programVideoBO.setDecode_mode(programDecodeType);
                programVideoBO.setBackup_mode(programPO.getBackup_mode());
                programVideoBO.setPattern_path(programPO.getPattern_path());
                programVideoBO.setCutoff_time(programPO.getCutoff_time());
            }
            createInputProgramBO.setVideo_array(video_array);
        }

        List<ProgramAudioBO> audio_array = JSON.parseArray(programPO.getAudioJson(), ProgramAudioBO.class);
        if (audio_array!=null && audio_array.size()>0){
            for (ProgramAudioBO programAudioBO : audio_array) {
                programAudioBO.setBackup_mode(programPO.getAudio_backup_mode());
                programAudioBO.setCutoff_time(programPO.getCutoff_time());
                programAudioBO.setDecode_mode(programDecodeType);
            }
            createInputProgramBO.setAudio_array(audio_array);
        }

        if (programPO.getSubtitleJson()!=null) {
            List<ProgramSubtitleBO> subtitle_array = JSON.parseArray(programPO.getSubtitleJson(), ProgramSubtitleBO.class);
            for (ProgramSubtitleBO programSubtitleBO : subtitle_array) {
                programSubtitleBO.setDecode_mode(programDecodeType);
            }
            createInputProgramBO.setSubtitle_array(subtitle_array);
        }
        return createInputProgramBO;
    }

    public String switchSource(String jobId, String inputId) throws Exception {
        JSONObject switchObj = new JSONObject();
        switchObj.put("jobId",jobId);
        switchObj.put("inputId",inputId);
        String uuid = UUID.randomUUID().toString();
        LOGGER.info("[capacity]<switch-task> send, msg:{}, params: {}",uuid, switchObj.toJSONString());
        String result = directorService.switchDirectorTask(switchObj.toJSONString());
        LOGGER.info("[capacity]<switch-task> send, msg:{}",uuid);
        return result;

    }

}
