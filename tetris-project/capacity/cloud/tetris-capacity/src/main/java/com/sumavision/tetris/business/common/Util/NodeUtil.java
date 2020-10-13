package com.sumavision.tetris.business.common.Util;/**
 * Created by Poemafar on 2020/9/18 15:47
 */

import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.business.common.bo.MediaSourceBO;
import com.sumavision.tetris.business.common.enumeration.ProtocolType;
import com.sumavision.tetris.capacity.bo.input.InputBO;
import com.sumavision.tetris.capacity.bo.output.*;
import com.sumavision.tetris.capacity.bo.task.EncodeBO;
import com.sumavision.tetris.capacity.bo.task.TaskBO;
import com.sumavision.tetris.capacity.bo.task.TaskSourceBO;
import com.sumavision.tetris.commons.exception.BaseException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @ClassName: NodeUtil
 * @Description TODO
 * @Author: Poemafar
 * @Version：1.0
 * @Date 2020/9/18 15:47
 */
@Component
public class NodeUtil {


    public InputBO getPassbyInputInCommand(IdConstructor idCtor,ProtocolType type, String url,String srtMode, String localIp){
        InputBO inputBO = new InputBO();
        inputBO.setId(idCtor.getId(0, IdConstructor.IdType.INPUT));
        inputBO.setNormal_map(new JSONObject());
        MediaSourceBO mediaSourceBO = new MediaSourceBO();
        mediaSourceBO.setProtocolType(type);
        mediaSourceBO.setUrl(url);
        mediaSourceBO.setMode(srtMode);
        mediaSourceBO.setLocalIp(localIp);
        inputBO.setEncapsulateInfo(mediaSourceBO);
        return inputBO;
    }

    public List<TaskBO> getPassbyTasksInCommond(IdConstructor idCtor){

        List<TaskBO> taskBOS = new ArrayList<>();

        TaskBO taskBO = new TaskBO()
                .setId(idCtor.getId(0, IdConstructor.IdType.TASK))
                .setType("passby")
                .setPassby_source(new TaskSourceBO()
                        .setInput_id(idCtor.getId(0, IdConstructor.IdType.INPUT)));

        EncodeBO encodeBO = new EncodeBO()
                .setEncode_id(idCtor.getId(0, IdConstructor.IdType.ENCODE))
                .setPassby(new JSONObject());

        taskBO.setEncode_array(new ArrayList<>())
                .getEncode_array()
                .add(encodeBO);

        taskBOS.add(taskBO);
        return taskBOS;
    }



    public OutputBO getPassbyOutputInCommond(IdConstructor idCtor,ProtocolType type, String url, String localIp){

        OutputBO outputBO = new OutputBO();

        outputBO.setId(idCtor.getId(0, IdConstructor.IdType.OUTPUT));

        BaseMediaBO mediaBO = new BaseMediaBO()
                .setTask_id(idCtor.getId(0, IdConstructor.IdType.TASK))
                .setEncode_id(idCtor.getId(0,IdConstructor.IdType.ENCODE));
        OutputPassbyBO outputPassbyBO = new OutputPassbyBO(url,localIp);
        outputPassbyBO.setMedia(mediaBO);

        switch (type){
            case UDP_TS:
                outputBO.setUdp_passby(outputPassbyBO);
                break;
            case RTP_TS:
                outputBO.setRtp_ts_passby(outputPassbyBO);
                break;
            case HTTP_TS:
                OutputHttpTsPassbyBO passbyBO = new OutputHttpTsPassbyBO(url,localIp);
                passbyBO.setMedia(mediaBO);
                outputBO.setHttp_ts_passby(passbyBO);
                break;
            case SRT_TS:
                //todo 暂不支持
//                OutputSrtTsPassbyBO srtBO = new OutputSrtTsPassbyBO(url,localIp);
//                srtBO.setMode(mode);
//                srtBO.setMedia(mediaBO);
//                outputBO.setSrt_ts_passby(srtBO);
        }

        return outputBO;
    }

}
