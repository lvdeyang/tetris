package com.sumavision.tetris.application.template;/**
 * Created by Poemafar on 2020/11/4 15:12
 */

import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.business.common.MissionBO;
import com.sumavision.tetris.business.common.Util.IdConstructor;
import com.sumavision.tetris.business.common.Util.IpV4Util;
import com.sumavision.tetris.business.common.enumeration.ProtocolType;
import com.sumavision.tetris.business.common.enumeration.TaskType;
import com.sumavision.tetris.capacity.bo.output.*;
import com.sumavision.tetris.capacity.bo.task.EncodeBO;
import com.sumavision.tetris.capacity.bo.task.TaskBO;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import static com.sumavision.tetris.business.common.enumeration.ProtocolType.*;

/**
 * @ClassName: OutputFactory
 * @Description TODO
 * @Author: Poemafar
 * @Version：1.0
 * @Date 2020/11/4 15:12
 */
public class OutputFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(OutputFactory.class);
    /**
     * 模板输出转化成转换输出
     */
    public OutputBO getOutputByTemplateOutput(MissionBO missionBO,String outputId, JSONObject taskOutputObj) throws BaseException {
        OutputBO transOutputBO = new OutputBO();
        transOutputBO.setId(outputId);
        ProtocolType outType =  ProtocolType.getProtocolType(taskOutputObj.getString("type"));
        if (missionBO.getTaskType()== TaskType.PASSBY) {
            outType = getPassbyType(outType);
        }
        switch (outType){
            case UDP_TS:
                transOutputBO.setUdp_ts(getCommonTsOutputBO(missionBO,taskOutputObj));
                break;
            case UDP_PASSBY:
                transOutputBO.setUdp_passby(new OutputPassbyBO(missionBO,taskOutputObj));
                break;
            case RTP_TS:
                transOutputBO.setRtp_ts(getCommonTsOutputBO(missionBO,taskOutputObj));
                break;
            case RTP_TS_PASSBY:
                transOutputBO.setRtp_ts_passby(new OutputPassbyBO(missionBO,taskOutputObj));
                break;
            case HTTP_TS:
                transOutputBO.setHttp_ts(getOutputHttpTsBO(missionBO,taskOutputObj));
                break;
            case HTTP_TS_PASSBY:
                transOutputBO.setHttp_ts_passby(new OutputHttpTsPassbyBO(missionBO,taskOutputObj));
                break;
            case SRT_TS:
                transOutputBO.setSrt_ts(getOutputSrtTsBO(missionBO,taskOutputObj));
                break;
            case SRT_TS_PASSBY:
                transOutputBO.setSrt_ts_passby(getOutputSrtTsPassbyBO(missionBO,taskOutputObj));
                break;
            case HLS:
                transOutputBO.setHls(getOutputHlsBO(missionBO,taskOutputObj));
                break;
            case DASH:
                transOutputBO.setDash(getOutputDashBO(missionBO,taskOutputObj));
                break;
            case RTSP:
                transOutputBO.setRtsp(getOutputRtspBO(missionBO,taskOutputObj));
                break;
            case RTP_ES:
                transOutputBO.setRtp_es(getOutputRtpEsBO(missionBO,taskOutputObj));
                break;
            case RTMP:
                transOutputBO.setRtmp(getOutputRtmpBO(missionBO,taskOutputObj));
                break;
            case HTTP_FLV:
                transOutputBO.setHttp_flv(getOutputRtmpBO(missionBO,taskOutputObj));
                break;
            case HLS_RECORD:
                transOutputBO.setHls_record(getOutputHlsRecordBO(missionBO,taskOutputObj));
                break;
            case ZIXI_TS:
                transOutputBO.setZixi_ts(getOutputZiXiBO(missionBO,taskOutputObj));
                break;
            case ZIXI_TS_PASSBY:
                transOutputBO.setZixi_passby(getOutputZiXiBO(missionBO,taskOutputObj));
                break;
            case ASI:

            default:
                 throw new BaseException(StatusCode.ERROR,"not support out type"+taskOutputObj.getString("type"));
        }

        return transOutputBO;

    }

    public CommonTsOutputBO getCommonTsOutputBO(MissionBO missionBO, JSONObject taskOutput) throws BaseException {

        CommonTsOutputBO outputBO  = JSONObject.parseObject(taskOutput.toJSONString(),CommonTsOutputBO.class);
        if (taskOutput.containsKey("bitrate")){
            outputBO.setBitrate(taskOutput.getInteger("bitrate")*1000);
        }else{
            outputBO.setBitrate(8000*1000);
        }
        if (taskOutput.containsKey("rate_ctrl")){
            outputBO.setRate_ctrl(outputBO.getRate_ctrl().toUpperCase(Locale.ENGLISH));
        }else{
            outputBO.setRate_ctrl("VBR");
        }
        if (outputBO.getIp()==null){
            outputBO.setIp(IpV4Util.getIpFromUrl(taskOutput.getString("url")));
        }
        if (outputBO.getPort()==null){
            outputBO.setPort(IpV4Util.getPortFromUrl(taskOutput.getString("url")));
        }
        if (outputBO.getLocal_ip()==null){
            missionBO.getDevice_ip();//没写出流网口IP的话，直接用控制口IP
        }

        List<OutputProgramBO> outputProgramBOS =new ArrayList();
        if (!taskOutput.containsKey("programs")){
            OutputProgramBO outputProgramBO = new OutputProgramBO();
            outputProgramBO.setProgram_number(1);
            outputProgramBO.setProvider("suma");
            outputProgramBO.setPcr_pid(101);
            outputProgramBO.setPmt_pid(100);
            outputProgramBO.setName("Suma");
            outputProgramBO.setCharacter_set("default");
            outputProgramBO.setOutputMedias(missionBO,null);
            outputProgramBOS.add(outputProgramBO);//直接按转换默认的走
        }else{
            for (int i = 0; i < taskOutput.getJSONArray("programs").size(); i++) {
                JSONObject outputProgramObj = taskOutput.getJSONArray("programs").getJSONObject(i);
                OutputProgramBO program = JSONObject.parseObject(outputProgramObj.toJSONString(), OutputProgramBO.class) ;
                program.setOutputMedias(missionBO,outputProgramObj.getJSONArray("medias"));
                outputProgramBOS.add(program);
            }
        }

        outputBO.setProgram_array(outputProgramBOS);

        return outputBO;
    }

    public OutputHttpTsBO getOutputHttpTsBO(MissionBO missionBO, JSONObject taskOutput) throws BaseException {
        OutputHttpTsBO outputBO  = JSONObject.parseObject(taskOutput.toJSONString(),OutputHttpTsBO.class);
        if (taskOutput.containsKey("bitrate")){
            outputBO.setBitrate(taskOutput.getInteger("bitrate")*1000);
        }
        outputBO.setRate_ctrl(outputBO.getRate_ctrl().toUpperCase(Locale.ENGLISH));
        if (outputBO.getIp()==null){
            outputBO.setIp(IpV4Util.getIpFromUrl(taskOutput.getString("url")));
        }
        if (outputBO.getPort()==null){
            outputBO.setPort(IpV4Util.getPortFromUrl(taskOutput.getString("url")));
        }
        if (outputBO.getName()==null){
            outputBO.setName(IpV4Util.getPathFromUrl(taskOutput.getString("url")));
        }
        if (outputBO.getLocal_ip()==null){
            outputBO.setLocal_ip(missionBO.getDevice_ip());//没写出流网口IP的话，直接用控制口IP
        }

        List<OutputProgramBO> outputProgramBOS =new ArrayList();
        if (taskOutput.containsKey("programs")){
            for (int i = 0; i < taskOutput.getJSONArray("programs").size(); i++) {
                JSONObject outputProgramObj = taskOutput.getJSONArray("programs").getJSONObject(i);
                OutputProgramBO program = JSONObject.parseObject(outputProgramObj.toJSONString(), OutputProgramBO.class) ;
                program.setOutputMedias(missionBO,outputProgramObj.getJSONArray("medias"));
                outputProgramBOS.add(program);
            }
        }else{
            OutputProgramBO outputProgramBO = new OutputProgramBO();
            outputProgramBO.setProgram_number(1);
            outputProgramBO.setProvider("suma");
            outputProgramBO.setPcr_pid(101);
            outputProgramBO.setPmt_pid(100);
            outputProgramBO.setName("Suma");
            outputProgramBO.setCharacter_set("default");
            outputProgramBO.setOutputMedias(missionBO,null);
            outputProgramBOS.add(outputProgramBO);//直接按转换默认的走
        }

        outputBO.setProgram_array(outputProgramBOS);

        return outputBO;
    }

    public OutputSrtTsBO getOutputSrtTsBO(MissionBO missionBO, JSONObject taskOutput) throws BaseException {
        OutputSrtTsBO outputBO  = JSONObject.parseObject(taskOutput.toJSONString(),OutputSrtTsBO.class);
        if (taskOutput.containsKey("maxbw")){
            outputBO.setMaxbw(taskOutput.getInteger("maxbw")*1000);
        }
        if (taskOutput.containsKey("bitrate")){
            outputBO.setBitrate(taskOutput.getInteger("bitrate")*1000);
        }
        outputBO.setRate_ctrl(outputBO.getRate_ctrl().toUpperCase(Locale.ENGLISH));
        if (outputBO.getIp()==null){
            outputBO.setIp(IpV4Util.getIpFromUrl(taskOutput.getString("url")));
        }
        if (outputBO.getPort()==null){
            outputBO.setPort(IpV4Util.getPortFromUrl(taskOutput.getString("url")));
        }
        if (outputBO.getLocal_ip()==null){
            outputBO.setLocal_ip(missionBO.getDevice_ip());//没写出流网口IP的话，直接用控制口IP
        }

        List<OutputProgramBO> outputProgramBOS =new ArrayList();
        if (taskOutput.containsKey("programs")){
            for (int i = 0; i < taskOutput.getJSONArray("programs").size(); i++) {
                JSONObject outputProgramObj = taskOutput.getJSONArray("programs").getJSONObject(i);
                OutputProgramBO program = JSONObject.parseObject(outputProgramObj.toJSONString(), OutputProgramBO.class) ;
                program.setOutputMedias(missionBO,outputProgramObj.getJSONArray("medias"));
                outputProgramBOS.add(program);
            }
        }else{
            OutputProgramBO outputProgramBO = new OutputProgramBO();
            outputProgramBO.setProgram_number(1);
            outputProgramBO.setProvider("suma");
            outputProgramBO.setPcr_pid(101);
            outputProgramBO.setPmt_pid(100);
            outputProgramBO.setName("Suma");
            outputProgramBO.setCharacter_set("default");
            outputProgramBO.setOutputMedias(missionBO,null);
            outputProgramBOS.add(outputProgramBO);//直接按转换默认的走
        }

        outputBO.setProgram_array(outputProgramBOS);
        return outputBO;

    }

    public OutputSrtTsPassbyBO getOutputSrtTsPassbyBO(MissionBO missionBO, JSONObject taskOutput) throws BaseException {
        OutputSrtTsPassbyBO outputBO  = JSONObject.parseObject(taskOutput.toJSONString(),OutputSrtTsPassbyBO.class);
        if (taskOutput.containsKey("maxbw")){
            outputBO.setMaxbw(taskOutput.getInteger("maxbw")*1000);
        }
        if (outputBO.getIp()==null){
            outputBO.setIp(IpV4Util.getIpFromUrl(taskOutput.getString("url")));
        }
        if (outputBO.getPort()==null){
            outputBO.setPort(IpV4Util.getPortFromUrl(taskOutput.getString("url")));
        }
        if (outputBO.getLocal_ip()==null){
            outputBO.setLocal_ip(missionBO.getDevice_ip());//没写出流网口IP的话，直接用控制口IP
        }

        TaskBO taskBO = missionBO.getTask_array().get(0);
        EncodeBO encodeBO = taskBO.getEncode_array().get(0);
        BaseMediaBO baseMediaBO = new BaseMediaBO().setTask_id(taskBO.getId()).setEncode_id(encodeBO.getEncode_id());
        outputBO.setMedia(baseMediaBO);
        return outputBO;
    }


    public OutputHlsBO getOutputHlsBO(MissionBO missionBO,JSONObject taskOutput) {
        OutputHlsBO outputBO  = JSONObject.parseObject(taskOutput.toJSONString(),OutputHlsBO.class);

        if (outputBO.getPlaylist_name()==null){
           outputBO.setPlaylist_name(IpV4Util.getNameFromUrl(taskOutput.getString("url")));
        }
        OutputStorageBO outputStorageBO = JSONObject.parseObject(taskOutput.toJSONString(),OutputStorageBO.class);

        String url = taskOutput.getString("url");
        if (url.contains("m3u8")){
            url = url.substring(0,url.lastIndexOf("/"));
        }
        if (IpV4Util.getIpFromUrl(url).equals(missionBO.getDevice_ip())){
            if (outputStorageBO.getDir_name()==null){
                outputStorageBO.setDir_name(IpV4Util.getPathFromUrl(url));
            }
            url = "file";
        }
        outputStorageBO.setUrl(url);
        List<OutputStorageBO> outputStorageBOS = new ArrayList<>();
        outputStorageBOS.add(outputStorageBO);
        outputBO.setStorage_array(outputStorageBOS);

        List<OutputMediaGroupBO> medias = new ArrayList<>();
        if (taskOutput.containsKey("medias")){
//按视频多码率下

        }else{
            List<OutputAudioBO> outAudBOs = new ArrayList<>();
            List<OutputSubtitleBO> outSubBOs = new ArrayList<>();
            for (int i = 0; i < missionBO.getTask_array().size(); i++) {
                TaskBO taskBO = missionBO.getTask_array().get(i);
                if ("audio".equals(taskBO.getType())) {
                    taskBO.getEncode_array().stream().forEach(e->{
                        outAudBOs.add(new OutputAudioBO().setTask_id(taskBO.getId()).setEncode_id(e.getEncode_id()));
                    });
                }
                if ("subtitle".equals(taskBO.getType())) {
                    taskBO.getEncode_array().stream().forEach(e->{
                        outSubBOs.add(new OutputSubtitleBO().setTask_id(taskBO.getId()).setEncode_id(e.getEncode_id()));
                    });
                }
            }
            for (int i = 0; i < missionBO.getTask_array().size(); i++) {
                TaskBO taskBO = missionBO.getTask_array().get(i);
                if ("video".equals(taskBO.getType())) {
                    taskBO.getEncode_array().stream().forEach(e->{
                        OutputMediaGroupBO mediaGroupBO = new OutputMediaGroupBO()
                                .setVideo_task_id(taskBO.getId())
                                .setVideo_encode_id(e.getEncode_id());
                        if (!outAudBOs.isEmpty()) {
                            mediaGroupBO.setAudio_array(outAudBOs);
                        }
                        if (!outSubBOs.isEmpty()) {
                            mediaGroupBO.setSubtitle_array(outSubBOs);
                        }
                        medias.add(mediaGroupBO);
                    });
                }
            }
        }

        outputBO.setMedia_group_array(medias);

//todo 暂不搞加密
        return outputBO;

    }

    public OutputDashBO getOutputDashBO(MissionBO missionBO,JSONObject taskOutput) {
        OutputDashBO outputBO  = JSONObject.parseObject(taskOutput.toJSONString(),OutputDashBO.class);
        if (outputBO.getPlaylist_name()==null){
            outputBO.setPlaylist_name(IpV4Util.getNameFromUrl(taskOutput.getString("url")));
        }
        OutputStorageBO outputStorageBO = JSONObject.parseObject(taskOutput.toJSONString(),OutputStorageBO.class);

        String url = taskOutput.getString("url");
        if (url.contains("mpd")){
            url = url.substring(0,url.lastIndexOf("/"));
        }
        if (IpV4Util.getIpFromUrl(url).equals(missionBO.getDevice_ip())){ //IP一样说明本地发布
            if (outputStorageBO.getDir_name()==null){
                outputStorageBO.setDir_name(IpV4Util.getPathFromUrl(url));
            }
            url = "file";
        }
        outputStorageBO.setUrl(url);
        outputBO.setStorage(outputStorageBO);

        List<OutputMediaGroupBO> medias = new ArrayList<>();


        if (taskOutput.containsKey("medias")){
//按视频多码率下

        }else{
            List<OutputAudioBO> outAudBOs = new ArrayList<>();
            List<OutputSubtitleBO> outSubBOs = new ArrayList<>();
            for (int i = 0; i < missionBO.getTask_array().size(); i++) {
                TaskBO taskBO = missionBO.getTask_array().get(i);
                if ("audio".equals(taskBO.getType())) {
                    taskBO.getEncode_array().stream().forEach(e->{
                        outAudBOs.add(new OutputAudioBO().setTask_id(taskBO.getId()).setEncode_id(e.getEncode_id()));
                    });
                }
                if ("subtitle".equals(taskBO.getType())) {
                    taskBO.getEncode_array().stream().forEach(e->{
                        outSubBOs.add(new OutputSubtitleBO().setTask_id(taskBO.getId()).setEncode_id(e.getEncode_id()));
                    });
                }
            }
            for (int i = 0; i < missionBO.getTask_array().size(); i++) {
                TaskBO taskBO = missionBO.getTask_array().get(i);
                if ("video".equals(taskBO.getType())) {
                    taskBO.getEncode_array().stream().forEach(e->{
                        OutputMediaGroupBO mediaGroupBO = new OutputMediaGroupBO()
                                .setVideo_task_id(taskBO.getId())
                                .setVideo_encode_id(e.getEncode_id());
                        if (!outAudBOs.isEmpty()) {
                            mediaGroupBO.setAudio_array(outAudBOs);
                        }
                        if (!outSubBOs.isEmpty()) {
                            mediaGroupBO.setSubtitle_array(outSubBOs);
                        }
                        medias.add(mediaGroupBO);
                    });
                }
            }
        }
        outputBO.setMedia_group_array(medias);

        return outputBO;

    }

    public OutputRtspBO getOutputRtspBO(MissionBO missionBO, JSONObject taskOutput) throws BaseException {
        OutputRtspBO outputBO  = JSONObject.parseObject(taskOutput.toJSONString(),OutputRtspBO.class);
        if (outputBO.getIp()==null){
            outputBO.setIp(IpV4Util.getIpFromUrl(taskOutput.getString("url")));
        }
        if (outputBO.getPort()==null){
            outputBO.setPort(IpV4Util.getPortFromUrl(taskOutput.getString("url")));
        }
        if (outputBO.getLocal_ip()==null){
            outputBO.setLocal_ip(missionBO.getDevice_ip());//没写出流网口IP的话，直接用控制口IP
        }
        if (outputBO.getSdp_name()==null){
            outputBO.setSdp_name(IpV4Util.getNameFromUrl(taskOutput.getString("url")));
        }

        List<OutputMediaBO> mediaBOS = new ArrayList<>();

        if (!taskOutput.containsKey("medias")){
            missionBO.getTask_array().forEach(t->{
                Integer pid = null;
                if (t.getEs_source()!=null){
                    pid = t.getEs_source().getElement_pid();
                }else if (t.getRaw_source()!=null){
                    pid = t.getRaw_source().getElement_pid();
                }
                String outMediaType = "none";
                if (t.getType().equals("passby")){
                    try {
                        outMediaType = TemplateUtil.getInstance().getTaskInputElementType(missionBO.getInputMap().values().stream().collect(Collectors.toList()), pid);
                    } catch (BaseException e) {
                        e.printStackTrace();
                    }
                }else{
                    outMediaType = t.getType();
                }
                String finalOutMediaType = outMediaType;
                t.getEncode_array().stream().forEach(e->{
                    mediaBOS.add(new OutputMediaBO()
                            .setTask_id(t.getId())
                            .setEncode_id(e.getEncode_id())
                            .setType(finalOutMediaType)
                    );
                });
            });

        }else{
            for (int i = 0; i < taskOutput.getJSONArray("medias").size(); i++) {
                JSONObject mediaObj = taskOutput.getJSONArray("medias").getJSONObject(i);
                String encodeId = missionBO.getOutEncodeMap().get(mediaObj.getInteger("track_id"));
                OutputMediaBO outputMediaBO = new OutputMediaBO();
                for (int j = 0; j < missionBO.getTask_array().size(); j++) {
                    TaskBO taskBO = missionBO.getTask_array().get(j);
                    Integer pid = null;
                    String outMediaType = "none";
                    if (taskBO.getType().equals("passby")){
                        if (taskBO.getEs_source()!=null){
                            pid = taskBO.getEs_source().getElement_pid();
                        }else if (taskBO.getRaw_source()!=null){
                            pid = taskBO.getRaw_source().getElement_pid();
                        }
                        outMediaType = TemplateUtil.getInstance().getTaskInputElementType(missionBO.getInputMap().values().stream().collect(Collectors.toList()), pid);
                    }else{
                        outMediaType = taskBO.getType();
                    }
                    for (int k = 0; k < taskBO.getEncode_array().size(); k++) {
                        EncodeBO encodeBO = taskBO.getEncode_array().get(k);
                        if (encodeBO.getEncode_id().equals(encodeId)){
                            outputMediaBO.setTask_id(taskBO.getId()).setEncode_id(encodeBO.getEncode_id()).setType(outMediaType);
                            break;
                        }
                    }
                }
                mediaBOS.add(outputMediaBO);
            }
        }


        outputBO.setMedia_array(mediaBOS);
        return outputBO;
    }

    public OutputRtpEsBO getOutputRtpEsBO(MissionBO missionBO, JSONObject taskOutput) {
        OutputRtpEsBO outputBO  = JSONObject.parseObject(taskOutput.toJSONString(),OutputRtpEsBO.class);
        if (outputBO.getIp()==null){
            outputBO.setIp(IpV4Util.getIpFromUrl(taskOutput.getString("url")));
        }
        if (outputBO.getPort()==null){
            outputBO.setPort(IpV4Util.getPortFromUrl(taskOutput.getString("url")));
        }
        if (outputBO.getLocal_ip()==null){
            outputBO.setLocal_ip(missionBO.getDevice_ip());//没写出流网口IP的话，直接用控制口IP
        }
        if (outputBO.getMax_bitrate()==null && taskOutput.containsKey("bitrate")){
            outputBO.setMax_bitrate(taskOutput.getInteger("bitrate")*1000);
        }

        OutputRtpesMediaBO media = new OutputRtpesMediaBO();

        if (!taskOutput.containsKey("medias")){
            media.setTask_id(missionBO.getTask_array().get(0).getId())
                    .setEncode_id(missionBO.getTask_array().get(0).getEncode_array().get(0).getEncode_id())
                    .setPayload_type(-1);
        }else{
            TaskBO taskBO = missionBO.getTask_array().get(0);
            media.setTask_id(taskBO.getId())
                    .setEncode_id(taskBO.getEncode_array().get(0).getEncode_id())
                    .setPayload_type(taskOutput.getJSONArray("medias").getJSONObject(0).getInteger("payload_type"));
        }

        outputBO.setMedia(media);;

        return outputBO;
    }

    public OutputRtmpBO getOutputRtmpBO(MissionBO missionBO, JSONObject taskOutput) throws BaseException {
        OutputRtmpBO outputBO  = JSONObject.parseObject(taskOutput.toJSONString(),OutputRtmpBO.class);
        if (outputBO.getServer_url()==null){
            outputBO.setServer_url(taskOutput.getString("url"));
        }
        //校验rtmp url头
        if (!outputBO.getServer_url().startsWith("rtmp")){
            throw new BaseException(StatusCode.FORBIDDEN,"rtmp url format error");
        }
        List<BaseMediaBO> mediaBOS = new ArrayList<>();
        if (!taskOutput.containsKey("medias")){
            missionBO.getTask_array().stream().forEach(t->{
                t.getEncode_array().stream().forEach(e->{
                    mediaBOS.add(new BaseMediaBO().setTask_id(t.getId()).setEncode_id(e.getEncode_id()));
                });
            });
        }else{
            for (int i = 0; i < taskOutput.getJSONArray("medias").size(); i++) {
                JSONObject mediaObj = taskOutput.getJSONArray("medias").getJSONObject(i);
                String encodeId = missionBO.getOutEncodeMap().get(mediaObj.getString("track_id"));
                BaseMediaBO outputMediaBO = new BaseMediaBO();
                for (int j = 0; j < missionBO.getTask_array().size(); j++) {
                    TaskBO taskBO = missionBO.getTask_array().get(j);
                    for (int k = 0; k < taskBO.getEncode_array().size(); k++) {
                        EncodeBO encodeBO = taskBO.getEncode_array().get(k);
                        if (encodeBO.getEncode_id().equals(encodeId)){
                            outputMediaBO.setTask_id(taskBO.getId()).setEncode_id(encodeBO.getEncode_id());
                            break;
                        }
                    }
                }
                mediaBOS.add(outputMediaBO);
            }
        }

        outputBO.setMedia_array(mediaBOS);

        return outputBO;
    }

    public OutputHlsRecordBO getOutputHlsRecordBO(MissionBO missionBO,JSONObject taskOutput) {
        OutputHlsRecordBO outputBO  = JSONObject.parseObject(taskOutput.toJSONString(),OutputHlsRecordBO.class);
        if (outputBO.getName()==null){
            outputBO.setName(taskOutput.getString("url"));
        }

        List<BaseMediaBO> mediaBOS = new ArrayList<>();

        if (!taskOutput.containsKey("medias")){
            missionBO.getTask_array().stream().forEach(t->{
                t.getEncode_array().stream().forEach(e->{
                    mediaBOS.add(new BaseMediaBO().setTask_id(t.getId()).setEncode_id(e.getEncode_id()));
                });
            });
        }else{
            for (int i = 0; i < taskOutput.getJSONArray("medias").size(); i++) {
                JSONObject mediaObj = taskOutput.getJSONArray("medias").getJSONObject(i);
                String encodeId = missionBO.getOutEncodeMap().get(mediaObj.getInteger("track_id"));
                BaseMediaBO outputMediaBO = new BaseMediaBO();
                for (int j = 0; j < missionBO.getTask_array().size(); j++) {
                    TaskBO taskBO = missionBO.getTask_array().get(j);
                    for (int k = 0; k < taskBO.getEncode_array().size(); k++) {
                        EncodeBO encodeBO = taskBO.getEncode_array().get(k);
                        if (encodeBO.getEncode_id().equals(encodeId)){
                            outputMediaBO.setTask_id(taskBO.getId()).setEncode_id(encodeBO.getEncode_id());
                            break;
                        }
                    }
                }
                mediaBOS.add(outputMediaBO);
            }
        }

        outputBO.setMedia_array(mediaBOS);
        return outputBO;
    }

    public OutputZiXiBO getOutputZiXiBO(MissionBO missionBO, JSONObject taskOutput) throws BaseException {
        OutputZiXiBO outputBO = JSONObject.parseObject(taskOutput.toJSONString(), OutputZiXiBO.class);

        List<OutputProgramBO> outputProgramBOS = new ArrayList();

        if (taskOutput.containsKey("programs")) {
            for (int i = 0; i < taskOutput.getJSONArray("programs").size(); i++) {
                JSONObject outputProgramObj = taskOutput.getJSONArray("programs").getJSONObject(i);
                OutputProgramBO program = JSONObject.parseObject(outputProgramObj.toJSONString(), OutputProgramBO.class);
                program.setOutputMedias(missionBO, outputProgramObj.getJSONArray("medias"));
                outputProgramBOS.add(program);
            }
        } else {

        }

        outputBO.setProgram_array(outputProgramBOS);

        return outputBO;
    }

}
