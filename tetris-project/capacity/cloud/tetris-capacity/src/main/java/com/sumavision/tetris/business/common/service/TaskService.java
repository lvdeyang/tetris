package com.sumavision.tetris.business.common.service;/**
 * Created by Poemafar on 2020/9/18 15:39
 */

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.business.common.ResultBO;
import com.sumavision.tetris.business.common.Util.IdConstructor;
import com.sumavision.tetris.business.common.Util.NodeUtil;
import com.sumavision.tetris.business.common.dao.TaskInputDAO;
import com.sumavision.tetris.business.common.dao.TaskOutputDAO;
import com.sumavision.tetris.business.common.enumeration.BusinessType;
import com.sumavision.tetris.business.common.enumeration.ProtocolType;
import com.sumavision.tetris.business.common.exception.CommonException;
import com.sumavision.tetris.business.common.po.TaskInputPO;
import com.sumavision.tetris.business.common.po.TaskOutputPO;
import com.sumavision.tetris.business.transcode.service.TranscodeTaskService;
import com.sumavision.tetris.business.transcode.vo.TranscodeTaskVO;
import com.sumavision.tetris.capacity.bo.input.InputBO;
import com.sumavision.tetris.capacity.bo.input.InputBaseBO;
import com.sumavision.tetris.capacity.bo.output.OutputBO;
import com.sumavision.tetris.capacity.bo.request.AllRequest;
import com.sumavision.tetris.capacity.bo.request.GetEntiretiesResponse;
import com.sumavision.tetris.capacity.bo.response.AllResponse;
import com.sumavision.tetris.capacity.bo.response.GetInputsResponse;
import com.sumavision.tetris.capacity.bo.response.ResultResponse;
import com.sumavision.tetris.capacity.bo.task.TaskBO;
import com.sumavision.tetris.capacity.config.CapacityProps;
import com.sumavision.tetris.capacity.service.CapacityService;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName: TaskService
 * @Description TODO
 * @Author: Poemafar
 * @Version：1.0
 * @Date 2020/9/18 15:39
 */
@Service
public class TaskService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskService.class);

    @Autowired
    NodeUtil nodeUtil;

    @Autowired
    TaskOutputDAO taskOutputDao;

    @Autowired
    TaskInputDAO taskInputDao;

    @Autowired
    CapacityProps capacityProps;

    @Autowired
    CapacityService capacityService;

    @Autowired
    TranscodeTaskService transcodeTaskService;

    /**
     * 转发流，将信源转发到另一地址进行输出
     * @param transModuleIp
     * @param inType
     * @param inUrl
     * @param outType
     * @param outUrl
     */
    public ResultBO transferStream(String transModuleIp,String missionId, ProtocolType inType, String inUrl,String srtMode, ProtocolType outType, String outUrl,BusinessType busType) throws Exception {
        ResultBO resultBO = new ResultBO();

        IdConstructor idCtor = new IdConstructor(missionId);

        List<InputBO> inputBOS = new ArrayList<>();
        List<TaskBO> taskBOS = new ArrayList<>();
        List<OutputBO> outputBOS = new ArrayList<>();
        //拼接输入
        InputBO inputBO = nodeUtil.getPassbyInputInCommand(idCtor,inType,inUrl,srtMode,transModuleIp);

        inputBOS.add(inputBO);
        //任务
        taskBOS = nodeUtil.getPassbyTasksInCommond(idCtor);
        //输出
        OutputBO outputBO = nodeUtil.getPassbyOutputInCommond(idCtor,outType,outUrl,transModuleIp);
        outputBOS.add(outputBO);
        transcodeTaskService.save(idCtor.getJobId(), transModuleIp, inputBOS, taskBOS, outputBOS, busType);
        resultBO.setMissionId(idCtor.getJobId());
        return resultBO;
    }

    public void deleteTranscodeTask(String id) throws Exception{
        TaskOutputPO output = delete(id,BusinessType.TRANSCODE);
        if(output != null){
            taskOutputDao.delete(output);
        }
    }


    public TaskOutputPO delete(String taskUuid,BusinessType busType) throws Exception {
        return delete(taskUuid,busType,false);
    }

    /**
     *
     * @param taskUuid
     * @param busType
     * @param isForce 是否强制删除，强删情况：主备切换
     * @return
     * @throws Exception
     */
    public TaskOutputPO delete(String taskUuid, BusinessType busType, Boolean isForce) throws Exception {
        TaskOutputPO output = taskOutputDao.findByTaskUuidAndType(taskUuid, busType);

        if (output != null) {
            AllRequest allRequest = new AllRequest();

            List<Long> inputIds = new ArrayList<>();
            if (output.getCoverId()!=null){
                inputIds.add(output.getCoverId());
            }
            if (output.getInputId()!=null){
                inputIds.add(output.getInputId());
            }
            if (output.getInputList()!=null && !output.getInputList().isEmpty()) {
                inputIds.addAll(JSONArray.parseArray(output.getInputList(), Long.class));
            }
            List<TaskInputPO> inputs = taskInputDao.findByIdIn(inputIds);
            if (inputs != null && inputs.size() > 0) {
                allRequest.setInput_array(new ArrayList());
                for (TaskInputPO input : inputs) {
                    if (!beUseForInputWithoutTask(input.getId(), taskUuid)) {
                        InputBO inputBO = JSONObject.parseObject(input.getInput(), InputBO.class);
                        allRequest.getInput_array().add(inputBO);
                        input.setUpdateTime(new Date());
                        input.setCount(0);
                        input.setSyncStatus(0);//假设可以删除成功，那此处定是0
                    }else{
                        input.setCount(input.getCount()-1);
                    }
                }
            }

            List<OutputBO> outputBOs = JSONObject.parseArray(output.getOutput(), OutputBO.class);
            List<TaskBO> tasks = JSONObject.parseArray(output.getTask(), TaskBO.class);

            if (tasks != null) {
                allRequest.setTask_array(new ArrayListWrapper<TaskBO>().addAll(tasks).getList());
            }
            if (outputBOs != null) {
                allRequest.setOutput_array(new ArrayListWrapper<OutputBO>().addAll(outputBOs).getList());
            }

            if (isForce) {
                capacityService.deleteAllIgnoreTransError(allRequest,output.getCapacityIp(),capacityProps.getPort());
            }else{
                try {
                    AllResponse allResponse = capacityService.deleteAllAddMsgId(allRequest, output.getCapacityIp(), capacityProps.getPort());
                } catch (Exception e) {
                    LOGGER.error("delete task fail"+taskUuid,e);
                    //如果抛异常，走一次查询对比，如果查不出来就是通信异常不同步了
                    beDeleteTaskByCompareTaskInTransform(inputs,output);
                }
            }
            try {
                output.setCoverId(null);
                output.setOutput(null);
                output.setTask(null);
                taskInputDao.save(inputs);
                taskOutputDao.save(output);
            } catch (ObjectOptimisticLockingFailureException e) {
                // 版本不对，version校验
                System.out.println("delete校验version版本不对");
                Thread.sleep(300);
                output = delete(taskUuid,busType);
            }
        }else{
            LOGGER.warn("need delete task not find in database, taskId: {}",taskUuid);
        }

        return output;
    }

    /**
     * 通过跟转换上现存的链路进行对比判断任务是否删除成功
     * 注：有一个链路没删掉就认为任务删失败了
     * @param deviceIp
     * @param inputs
     * @throws Exception
     */
    public void beDeleteTaskByCompareTaskInTransform(List<TaskInputPO> inputs,TaskOutputPO output) throws Exception {
        LOGGER.info("start sync delete task, id:{}",output.getTaskUuid());
        List<String> errInputIds = new ArrayList<>(); //存删失败的节点
        List<String> errTaskIds = new ArrayList<>();
        List<String> errOutputIds = new ArrayList<>();

        GetEntiretiesResponse entirety= null;
        try {
            entirety = capacityService.getEntireties(output.getCapacityIp());
        } catch (Exception e) {
            inputs.stream().filter(i->i.getCount()==0).forEach(i->{
                i.setCount(1);
                i.setSyncStatus(1);//不知道转换有没有这个输入了
                taskInputDao.save(i);
            });
            throw e;
        }
        List tInputIds = entirety.getInput_array().stream().map(InputBO::getId).collect(Collectors.toList());
        List tTaskIds = entirety.getTask_array().stream().map(TaskBO::getId).collect(Collectors.toList());
        List tOutputIds = entirety.getOutput_array().stream().map(OutputBO::getId).collect(Collectors.toList());
        for (int i = 0; i < inputs.size(); i++) {
            TaskInputPO inputPO = inputs.get(i);
            if (0!=inputPO.getCount()) {
                continue;
            }
            if (!tInputIds.contains(inputPO.getNodeId())) {
                taskInputDao.save(inputPO);
            }else{
                LOGGER.error("input cannot delete, nodeId: {}",inputPO.getNodeId());
                errInputIds.add(inputPO.getNodeId());
            }
        }
        List<TaskBO> tasks = JSONObject.parseArray(output.getTask(), TaskBO.class);
        for (int i = 0; i < tasks.size(); i++) {
            TaskBO taskBO = tasks.get(i);
            if (tTaskIds.contains(taskBO.getId())) {
                LOGGER.error("task cannot delete, nodeId: {}",taskBO.getId());
                errTaskIds.add(taskBO.getId());
            }else{
                tasks.remove(taskBO);
            }
        }
        if (tasks.isEmpty()){
            output.setTask(null);
        }else{
            output.setTask(JSONArray.toJSONString(tasks));
        }
        List<OutputBO> outputs = JSONObject.parseArray(output.getOutput(), OutputBO.class);
        for (int i = 0; i < outputs.size(); i++) {
            OutputBO outputBO = outputs.get(i);
            if (tOutputIds.contains(outputBO.getId())) {
                LOGGER.error("output cannot delete, nodeId: {}",outputBO.getId());
                errOutputIds.add(outputBO.getId());
            }
        }
        if (outputs.isEmpty()){
            output.setOutput(null);
        }else{
            output.setOutput(JSONArray.toJSONString(outputs));
        }
        taskOutputDao.save(output);
        if (!errInputIds.isEmpty() || !errTaskIds.isEmpty() || !errOutputIds.isEmpty()){
            throw new BaseException(StatusCode.ERROR,"capacity delete task error");
        }
    }





    /**
     * 判断某个输入是否有其他任务占用
     * @param inputId
     * @return
     */
    public Boolean beUseForInputWithoutTask(Long inputId,String taskId){
        Integer beExistTask = taskOutputDao.countDistinctByInputIdAndTaskUuidNotAndTaskUuidNotNullAndOutputNotNullAndTaskNotNull(inputId,taskId);
        if (beExistTask > 0){
            return true;
        }
        List<TaskOutputPO> existTasks = taskOutputDao.findByTaskUuidNotAndTaskUuidNotNullAndOutputNotNullAndTaskNotNull(taskId);
        for (int i = 0; i < existTasks.size(); i++) {
            TaskOutputPO taskOutput = existTasks.get(i);
            if (taskOutput.getInputList()!=null && taskOutput.getInputList().contains(inputId.toString())){
                return true;
            }
        }

        return false;
    }

    public Boolean beRepeatInput(String transformIp,InputBO inputBO){

        Boolean beRepeat = false;

        try {
            GetInputsResponse inputs = capacityService.getInputs(transformIp);
            if (inputBO.getUdp_ts() != null) {
                beRepeat = inputs.getInput_array().stream().anyMatch(i-> i.getUdp_ts()!=null
                        && i.getUdp_ts().getSource_ip()==inputBO.getUdp_ts().getSource_ip()
                        && i.getUdp_ts().getSource_port().equals(inputBO.getUdp_ts().getSource_port())
                        && i.getUdp_ts().getLocal_ip().equals(inputBO.getUdp_ts().getLocal_ip()));
            }
            if (inputBO.getRtp_ts() != null) {
                beRepeat = inputs.getInput_array().stream().anyMatch(i->i.getRtp_ts()!=null
                        && i.getRtp_ts().getSource_ip()==inputBO.getRtp_ts().getSource_ip()
                        && i.getRtp_ts().getSource_port().equals(inputBO.getRtp_ts().getSource_port())
                        && i.getRtp_ts().getLocal_ip().equals(inputBO.getRtp_ts().getLocal_ip()));
            }
            if(inputBO.getHttp_ts() != null){
                beRepeat = inputs.getInput_array().stream().anyMatch(i->i.getHttp_ts()!=null && i.getHttp_ts().getUrl()==inputBO.getHttp_ts().getUrl());
            }
            if(inputBO.getSrt_ts() != null){
                beRepeat = inputs.getInput_array().stream().anyMatch(i->i.getSrt_ts()!=null
                        && i.getSrt_ts().getSource_ip()==inputBO.getSrt_ts().getSource_ip()
                        && i.getSrt_ts().getSource_port().equals(inputBO.getSrt_ts().getSource_port()));
            }
            if(inputBO.getHls() != null){
                beRepeat = inputs.getInput_array().stream().anyMatch(i->i.getHls()!=null && i.getHls().getUrl()==inputBO.getHls().getUrl());
            }
            if(inputBO.getDash() != null){
                beRepeat = inputs.getInput_array().stream().anyMatch(i->i.getDash()!=null && i.getDash().getUrl()==inputBO.getDash().getUrl());
            }
            if(inputBO.getMss() != null){
                beRepeat = inputs.getInput_array().stream().anyMatch(i->i.getMss()!=null && i.getMss().getUrl()==inputBO.getMss().getUrl());
            }
            if(inputBO.getRtmp() != null){
                beRepeat = inputs.getInput_array().stream().anyMatch(i->i.getRtmp()!=null && i.getRtmp().getUrl()==inputBO.getRtmp().getUrl());
            }
            if(inputBO.getRtsp() != null){
                beRepeat = inputs.getInput_array().stream().anyMatch(i->i.getRtsp()!=null && i.getRtsp().getUrl()==inputBO.getRtsp().getUrl());
            }
            if(inputBO.getHttp_flv() != null){
                beRepeat = inputs.getInput_array().stream().anyMatch(i->i.getHttp_flv()!=null && i.getHttp_flv().getUrl()==inputBO.getHttp_flv().getUrl());
            }
            if(inputBO.getSdi() != null){
                beRepeat = inputs.getInput_array().stream().anyMatch(i->i.getSdi()!=null
                        && i.getSdi().getCard_no()==inputBO.getSdi().getCard_no()
                        && i.getSdi().getCard_port()==inputBO.getSdi().getCard_port());
            }
            if(inputBO.getRtp_es() != null){
                beRepeat = inputs.getInput_array().stream().anyMatch(i->i.getRtp_es()!=null && i.getRtp_es().getLocal_port()==inputBO.getRtp_es().getLocal_port());
            }
            if(inputBO.getFile() != null){
                beRepeat = false;
            }
            if(inputBO.getUdp_pcm() != null){
                beRepeat = inputs.getInput_array().stream().anyMatch(i->i.getUdp_pcm()!=null
                        && i.getUdp_pcm().getSource_ip()==inputBO.getUdp_pcm().getSource_ip()
                        && i.getUdp_pcm().getSource_port()==inputBO.getUdp_pcm().getSource_port()
                );
            }
            if(inputBO.getBack_up_es() != null || inputBO.getBack_up_passby() != null || inputBO.getBack_up_raw() != null){
                beRepeat = false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            //拿不到就从数据库判断吧


        }

        return beRepeat;
    }

}


