package com.sumavision.tetris.business.common.Util;/**
 * Created by Poemafar on 2020/9/18 15:47
 */

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.business.common.bo.MediaSourceBO;
import com.sumavision.tetris.business.common.dao.TaskInputDAO;
import com.sumavision.tetris.business.common.enumeration.ProtocolType;
import com.sumavision.tetris.business.common.po.TaskInputPO;
import com.sumavision.tetris.capacity.bo.input.InputBO;
import com.sumavision.tetris.capacity.bo.output.*;
import com.sumavision.tetris.capacity.bo.task.EncodeBO;
import com.sumavision.tetris.capacity.bo.task.TaskBO;
import com.sumavision.tetris.capacity.bo.task.TaskSourceBO;
import com.sumavision.tetris.capacity.constant.Constant;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @ClassName: NodeUtil
 * @Description TODO
 * @Author: Poemafar
 * @Version：1.0
 * @Date 2020/9/18 15:47
 */
@Component
public class NodeUtil {

    @Autowired
    TaskInputDAO taskInputDao;

    public String getUrl(InputBO inputBO){
        if (inputBO.getUdp_ts() != null) {
            return "udp://"+inputBO.getUdp_ts().getSource_ip()+":"+inputBO.getUdp_ts().getSource_port();
        }
        if (inputBO.getRtp_ts() != null) {
            return "rtp://"+inputBO.getRtp_ts().getSource_ip()+":"+inputBO.getRtp_ts().getSource_port();
        }
        if (inputBO.getHttp_ts() != null) {
            return inputBO.getHttp_ts().getUrl();
        }
        if (inputBO.getSrt_ts() != null) {
            return "srt://"+inputBO.getSrt_ts().getSource_ip()+":"+inputBO.getSrt_ts().getSource_port();
        }
        if (inputBO.getHls() != null) {
            return inputBO.getHls().getUrl();
        }
        if (inputBO.getDash() != null) {
            return inputBO.getDash().getUrl();
        }
        if (inputBO.getRtsp() != null) {
            return inputBO.getRtsp().getUrl();
        }
        if (inputBO.getRtmp() != null) {
            return inputBO.getRtmp().getUrl();
        }
        if (inputBO.getHttp_flv() != null) {
            return inputBO.getHttp_flv().getUrl();
        }
        if (inputBO.getZixi() != null) {
            return inputBO.getZixi().getUrl();
        }
        if (inputBO.getMss() != null) {
            return inputBO.getMss().getUrl();
        }
        return null;
    }

    public Boolean beBackupInput(InputBO inputBO){
        return inputBO.getBack_up_raw()!=null || inputBO.getBack_up_es()!=null || inputBO.getBack_up_passby()!=null;
    }

    public Boolean beBackupInput(List<InputBO> inputBOS){
        return inputBOS.stream().anyMatch(i->i.getBack_up_raw()!=null || i.getBack_up_es()!=null || i.getBack_up_passby()!=null);
    }

    /**
     * @MethodName: adjustOrderForInputBOS
     * @Description: 调整顺序，保证下任务的时候，输入节点如果有备份，备份放在最后
     * @param inputBOS 1
     * @Return: void
     * @Author: Poemafar
     * @Date: 2021/2/4 11:57
     **/
    public void adjustOrderForInputBOS(List<InputBO> inputBOS){
        List<InputBO> temp = new ArrayList<>();
        Iterator<InputBO> iterator = inputBOS.iterator();
        while(iterator.hasNext()){
            InputBO inputBO =  iterator.next();
            if (beBackupInput(inputBO)) {
                temp.add(inputBO);
                iterator.remove();
            }
        }
        inputBOS.addAll(temp);
    }

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


    public OutputBO getPassbyOutputInCommond(IdConstructor idCtor,ProtocolType type, String url, String localIp) throws BaseException {

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
            default:
                throw new BaseException(StatusCode.FORBIDDEN,"not support passby:"+type);
        }

        return outputBO;
    }


    public Integer getPortByDevice(String deviceIp){
        Set<Integer> usedPorts = new HashSet<>();
        List<TaskInputPO> all = taskInputDao.findByUrlNotNull();
        for (int i = 0; i < all.size(); i++) {
            TaskInputPO inputPO = all.get(i);
            if (inputPO.getCount()>0 && inputPO.getUrl().contains(deviceIp)) {
                usedPorts.add(IpV4Util.getPortFromUrl(inputPO.getUrl()));
            }
        }
        for (int port = Constant.MIN_PORT;port < Constant.MAX_PORT; port++){
            if (!usedPorts.contains(port)){
                return port;
            }
        }
        return null;
    }
}
