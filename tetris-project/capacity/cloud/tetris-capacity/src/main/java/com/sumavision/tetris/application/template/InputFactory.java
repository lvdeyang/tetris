package com.sumavision.tetris.application.template;/**
 * Created by Poemafar on 2020/11/4 15:12
 */

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.business.common.MissionBO;
import com.sumavision.tetris.business.common.Util.IdConstructor;
import com.sumavision.tetris.business.common.enumeration.MediaType;
import com.sumavision.tetris.business.common.enumeration.ProtocolType;
import com.sumavision.tetris.business.common.enumeration.TaskType;
import com.sumavision.tetris.capacity.bo.input.*;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: InputFactory
 * @Description TODO
 * @Author: Poemafar
 * @Version：1.0
 * @Date 2020/11/4 15:12
 */
public class InputFactory{

    private static final Logger LOGGER = LoggerFactory.getLogger(InputFactory.class);

    public InputBO getInputBO() {
        return null;
    }

    public InputBO getInputByTemplateInput(MissionBO missionBO, SourceVO tmplInputBO) throws BaseException {
        InputBO inputBO = new InputBO();
        Integer idx = missionBO.getInputMap().size();
        inputBO.setId(missionBO.getIdCtor().getId(idx, IdConstructor.IdType.INPUT));
        ProtocolType inputType = ProtocolType.getProtocolType(tmplInputBO.getType());
        JSONObject inputObj = JSONObject.parseObject(JSON.toJSONString(tmplInputBO));
        switch (inputType) {
            case UDP_TS:
                if (null == tmplInputBO.getLocal_ip()){
                    tmplInputBO.setLocal_ip(missionBO.getDevice_ip());
                }
                inputBO.setUdp_ts(new CommonTsBO(tmplInputBO.getUrl(),tmplInputBO.getLocal_ip()));
                break;
            case RTP_TS:
                if (null == tmplInputBO.getLocal_ip()){
                    tmplInputBO.setLocal_ip(missionBO.getDevice_ip());
                }
                inputBO.setRtp_ts(new CommonTsBO(tmplInputBO.getUrl(),tmplInputBO.getLocal_ip()));
                break;
            case HTTP_TS:
                inputBO.setHttp_ts(new SourceUrlBO(tmplInputBO.getUrl(),inputType.name().toLowerCase()));
                break;
            case SRT_TS:
                inputBO.setSrt_ts(new SrtTsBO(tmplInputBO));
                break;
            case HLS:
                inputBO.setHls(new SourceUrlBO(tmplInputBO.getUrl(),inputType.name().toLowerCase()));
                break;
            case DASH:
                inputBO.setDash(new SourceUrlBO(tmplInputBO.getUrl(),inputType.name().toLowerCase()));
                break;
            case MSS:
                inputBO.setMss(new SourceUrlBO(tmplInputBO.getUrl(),inputType.name().toLowerCase()));
                break;
            case RTSP:
                inputBO.setRtsp(new SourceUrlBO(tmplInputBO.getUrl(),inputType.name().toLowerCase()));
                break;
            case RTMP:
                inputBO.setRtmp(new SourceUrlBO(tmplInputBO.getUrl(),inputType.name().toLowerCase()));
                break;
            case HTTP_FLV:
                inputBO.setHttp_flv(new SourceUrlBO(tmplInputBO.getUrl(),inputType.name().toLowerCase()));
                break;
            case SDI:
                inputBO.setSdi(new SdiBO(tmplInputBO.getUrl()));
                break;
            case RTP_ES:
                if (null == tmplInputBO.getLocal_ip()){
                    tmplInputBO.setLocal_ip(missionBO.getDevice_ip());
                }
                inputBO.setRtp_es(new RtpEsBO(tmplInputBO));
                break;
            case UDP_PCM:
                if (null == tmplInputBO.getLocal_ip()){
                    tmplInputBO.setLocal_ip(missionBO.getDevice_ip());
                }
                inputBO.setUdp_pcm(new UdpPcmBO(tmplInputBO));
                break;
            case FILE:
                inputBO.setFile(new InputFileBO(tmplInputBO.getFile_array()));
                break;
            case ZIXI_TS:
                inputBO.setZixi(new InputZiXiBO(tmplInputBO));
                break;
            case SCHEDULE://schedule要最后处理
                inputBO.setSchedule(new InputScheduleBO(tmplInputBO));
                break;
            case BACKUP://特殊输入，单独处理
                if (missionBO.getTaskType()== TaskType.PASSBY) {
                    inputBO.setBack_up_passby(new BackUpPassByBO(missionBO,inputObj));
                }else if (missionBO.getTaskType()==TaskType.PACKAGE){
                    inputBO.setBack_up_es(new BackUpEsAndRawBO(missionBO,inputObj));
                }else if (missionBO.getTaskType()==TaskType.TRANS){
                    inputBO.setBack_up_raw(new BackUpEsAndRawBO(missionBO,inputObj));
                }else{
                    throw new BaseException(StatusCode.ERROR,"not support task type: " + missionBO.getTaskType());
                }
                break;
            default:
                throw new BaseException(StatusCode.ERROR,"not support input package type" + tmplInputBO.getType());
        }

        setProgramArrayForInput(inputBO,inputObj);

        return inputBO;

    }

    /**
     * @MethodName: setProgramArrayForInput
     * @Description: 为INPUTBO 生成并设置 PROGRAM_ARRAY
     * @param inputBO 1
     * @param inputObj 2
     * @Return: void
     * @Author: Poemafar
     * @Date: 2020/12/9 11:06
     **/
    public void setProgramArrayForInput(InputBO inputBO,JSONObject inputObj) throws BaseException {
        List<ProgramBO> programBOList = new ArrayList<>();
        if (inputObj.containsKey("program_array")){ //模板里有了节目组就不用拼接了
            programBOList = JSONArray.parseArray(inputObj.getString("program_array"),ProgramBO.class);
            if (inputObj.containsKey("mediaType") && inputObj.getString("mediaType").equals("audio")){
                programBOList.get(0).setVideo_array(null);
            }
        }else{
            if(!inputObj.containsKey("mediaType") || inputObj.getString("mediaType").equals("video")){
                ProgramBO program = new ProgramBO().setProgram_number(1)
                        .setVideo_array(new ArrayList())
                        .setAudio_array(new ArrayList())
                        .setMedia_type_once_map(new JSONObject());

                ProgramVideoBO video = new ProgramVideoBO().setPid(513)
                        .setDecode_mode("cpu");
                ProgramAudioBO audio = new ProgramAudioBO().setPid(514)
                        .setDecode_mode("cpu");
                program.getVideo_array().add(video);
                program.getAudio_array().add(audio);

                programBOList.add(program);
            }else if(inputObj.getString("mediaType").equals("audio")){

                ProgramBO program = new ProgramBO().setProgram_number(1)
                        .setAudio_array(new ArrayList())
                        .setMedia_type_once_map(new JSONObject());

                ProgramAudioBO audio = new ProgramAudioBO().setPid(514)
                        .setDecode_mode("cpu");
                program.getAudio_array().add(audio);

                programBOList.add(program);
            }else{
                throw new BaseException(StatusCode.ERROR,"not support input media type"+inputObj.getString("mediaType"));
            }
        }
        inputBO.setProgram_array(programBOList);
    }

}
