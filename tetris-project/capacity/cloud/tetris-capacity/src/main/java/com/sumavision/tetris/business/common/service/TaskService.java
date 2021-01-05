package com.sumavision.tetris.business.common.service;/**
 * Created by Poemafar on 2020/9/18 15:39
 */

import com.alibaba.fastjson.JSON;
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
import com.sumavision.tetris.business.transcode.vo.TaskVO;
import com.sumavision.tetris.business.transcode.vo.TranscodeTaskVO;
import com.sumavision.tetris.capacity.bo.input.InputBO;
import com.sumavision.tetris.capacity.bo.input.InputBaseBO;
import com.sumavision.tetris.capacity.bo.input.InputWrapperBO;
import com.sumavision.tetris.capacity.bo.output.OutputBO;
import com.sumavision.tetris.capacity.bo.request.*;
import com.sumavision.tetris.capacity.bo.response.AllResponse;
import com.sumavision.tetris.capacity.bo.response.CreateInputsResponse;
import com.sumavision.tetris.capacity.bo.response.GetInputsResponse;
import com.sumavision.tetris.capacity.bo.response.ResultResponse;
import com.sumavision.tetris.capacity.bo.task.TaskBO;
import com.sumavision.tetris.capacity.config.CapacityProps;
import com.sumavision.tetris.capacity.service.CapacityService;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import java.util.*;
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

    public void deleteTranscodeTask(TaskVO taskVO) throws Exception{
        if (taskVO.getBeForce()==null){
            taskVO.setBeForce(false);
        }
        TaskOutputPO output = delete(taskVO.getTask_id(),BusinessType.TRANSCODE,taskVO.getBeForce());
        if(output != null){
            taskOutputDao.delete(output);
        }
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
                    }else{
                        input.setUpdateTime(new Date());
                        input.setCount(input.getCount()-1);
                    }
                }
            }

            List<OutputBO> outputBOs = new ArrayList<>();
            Object outputObj = JSON.parse(output.getOutput());
            if (outputObj instanceof JSONObject) {
                outputBOs.add(JSONObject.parseObject(output.getOutput(),OutputBO.class));
            }else if (outputObj instanceof JSONArray){
                outputBOs = JSONArray.parseArray(output.getOutput(),OutputBO.class);
            }
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
                    //删成功就认为成功了，不进行删除ID的对比
                } catch (Exception e) {
                    LOGGER.error("delete task fail"+taskUuid,e);
                    throw e;
                    //如果抛异常，走一次查询对比，如果查不出来就是通信异常不同步了
//                   beDeleteTaskByCompareTaskInTransform(inputs,output);
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
                output = delete(taskUuid,busType,isForce);
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
            taskOutputDao.updateSyncStatusById(output.getId(),1);
            throw e;
        }
        List tInputIds = entirety.getInput_array().stream().map(InputBO::getId).collect(Collectors.toList());
        List tTaskIds = entirety.getTask_array().stream().map(TaskBO::getId).collect(Collectors.toList());
        List tOutputIds = entirety.getOutput_array().stream().map(OutputBO::getId).collect(Collectors.toList());

        List<Long> inputIds = JSONArray.parseArray(output.getInputList(), Long.class);
        for (int i = 0; i < inputs.size(); i++) {
            TaskInputPO inputPO = inputs.get(i);
            if (0!=inputPO.getCount()) {
                continue;
            }
            if (!tInputIds.contains(inputPO.getNodeId())) {
                inputIds.remove(inputPO.getId());
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
        output.setInputList(JSON.toJSONString(inputIds));
        taskOutputDao.save(output);
        if (!errInputIds.isEmpty() || !errTaskIds.isEmpty() || !errOutputIds.isEmpty()){
            taskOutputDao.updateSyncStatusById(output.getId(),1); //有没删掉的就是删失败，置任务不同步
            throw new BaseException(StatusCode.ERROR,"capacity delete task error");
        }

    }

    /**
     * @MethodName: checkLegalBeforeCreateTask
     * @Description: 校验任务是否合法
     * @param inputBOS 1
     * @Return: void
     * @Author: Poemafar
     * @Date: 2020/12/2 8:56
     **/
    public void checkLegalBeforeCreateTask(List<InputBO> inputBOS) throws BaseException {
        Set<String> uniqSet = new HashSet();
        for (int i = 0; i < inputBOS.size(); i++) {
            InputBO inputBO = inputBOS.get(i);
            String uniq = generateUniq(inputBO);
            if (uniqSet.contains(uniq)){
                throw new BaseException(StatusCode.ERROR, "input must not repeat");
            }else{
                uniqSet.add(uniq);
            }
        }
    }

    /**
     * @MethodName: generateUniq
     * @Description: TODO
     * @param inputBO 1 输入唯一性标识
     * @Return: java.lang.String
     * @Author: Poemafar
     * @Date: 2020/12/2 8:58
     **/
    public String generateUniq(InputBO inputBO){
        String uniq = "";
        if(inputBO.getUdp_ts() != null){
            uniq = new StringBuffer().append(inputBO.getUdp_ts().getSource_ip())
                    .append("%")
                    .append(inputBO.getUdp_ts().getSource_port())
                    .append("%")
                    .append(inputBO.getUdp_ts().getLocal_ip())
                    .toString();
        }
        if(inputBO.getRtp_ts() != null){
            uniq = new StringBuffer().append(inputBO.getRtp_ts().getSource_ip())
                    .append("%")
                    .append(inputBO.getRtp_ts().getSource_port())
                    .append("%")
                    .append(inputBO.getRtp_ts().getLocal_ip())
                    .toString();
        }
        if(inputBO.getHttp_ts() != null){
            uniq = inputBO.getHttp_ts().getUrl();
        }
        if(inputBO.getSrt_ts() != null){
            uniq = new StringBuffer().append(inputBO.getSrt_ts().getSource_ip())
                    .append("%")
                    .append(inputBO.getSrt_ts().getSource_port())
                    .append("%")
                    .toString();
        }
        if(inputBO.getHls() != null){
            uniq = inputBO.getHls().getUrl();
        }
        if(inputBO.getDash() != null){
            uniq = inputBO.getDash().getUrl();
        }
        if(inputBO.getMss() != null){
            uniq = inputBO.getMss().getUrl();
        }
        if(inputBO.getRtsp() != null){
            uniq = inputBO.getRtsp().getUrl();
        }
        if(inputBO.getRtmp() != null){
            uniq = inputBO.getRtmp().getUrl();
        }
        if(inputBO.getHttp_flv() != null){
            uniq = inputBO.getHttp_flv().getUrl();
        }
        if(inputBO.getSdi() != null){
            uniq = new StringBuffer().append(inputBO.getSdi().getCard_no())
                    .append("%")
                    .append(inputBO.getSdi().getCard_port())
                    .toString();
        }
        if(inputBO.getRtp_es() != null){
            uniq = new StringBuffer().append("%")
                    .append(inputBO.getRtp_es().getLocal_port())
                    .append("%")
                    .toString();
        }
        if(inputBO.getFile() != null){
            uniq = inputBO.getId();
        }
        if(inputBO.getUdp_pcm() != null){
            uniq = new StringBuffer().append(inputBO.getUdp_pcm().getSource_ip())
                    .append("%")
                    .append(inputBO.getUdp_pcm().getSource_port())
                    .toString();
        }
        if (inputBO.getSchedule()!=null){
            uniq = inputBO.getId();
        }
        //不管是否同源，每个任务一个备份关系
        if(inputBO.getBack_up_es() != null || inputBO.getBack_up_passby() != null || inputBO.getBack_up_raw() != null){
            uniq = inputBO.getId();
        }

        return uniq;
    }

    /**
     * 判断某个输入是否有其他任务占用
     * @param inputId
     * @return
     */
    public Boolean beUseForInputWithoutTask(Long inputId,String taskId){
        TaskInputPO input = taskInputDao.findOne(inputId);
        if (input.getAnalysis()>0){
            return true;
        }
        List<TaskOutputPO> existTasks = taskOutputDao.findByTaskUuidNotAndTaskUuidNotNullAndOutputNotNullAndTaskNotNull(taskId);
        if (existTasks==null || existTasks.isEmpty()){
            return false;
        }
        for (int i = 0; i < existTasks.size(); i++) {
            TaskOutputPO taskOutput = existTasks.get(i);
            if (inputId.equals(taskOutput.getInputId())
                    || (taskOutput.getInputList()!=null && taskOutput.getInputList().contains(inputId.toString()))
            ){
                return true;
            }
        }

        return false;
    }


    /**
     * @MethodName: addInputsAfterRepeat
     * @Description:
     * 添加码流分析输入
     * 判重后创建输入，如果重复就不创建了
     * @param deviceIp 1
     * @param inputBOS 2
     * @Return: java.lang.String
     * @Author: Poemafar
     * @Date: 2020/12/4 9:44
     **/
    public void addInputsAfterRepeat(String deviceIp, List<InputBO> inputBOS, BusinessType busType) throws Exception {
        List<InputBO> needSendInputArray = new ArrayList<>();
        for (int i = 0; i < inputBOS.size(); i++) {
            InputBO inputBO = inputBOS.get(i);
            String uniq = generateUniq(inputBO);
            TaskInputPO inputPO = taskInputDao.findByUniq(uniq);
            if (inputPO == null) {
                inputPO = new TaskInputPO();
                inputPO.setCreateTime(new Date());
                inputPO.setUpdateTime(inputPO.getCreateTime());
                inputPO.setUniq(uniq);
                inputPO.setType(busType);
                inputPO.setInput(JSON.toJSONString(inputBO));
                inputPO.setNodeId(inputBO.getId());
                inputPO.setCapacityIp(deviceIp);
                taskInputDao.save(inputPO);
                needSendInputArray.add(inputBO);
            } else if (inputPO.getCount().equals(0)) {
                inputPO.setInput(JSON.toJSONString(inputBO));
                inputPO.setNodeId(inputBO.getId());
                inputPO.setType(busType);
                inputPO.setCreateTime(new Date());
                inputPO.setUpdateTime(inputPO.getCreateTime());
                inputPO.setCount(inputPO.getCount() + 1);
                inputPO.setCapacityIp(deviceIp);
                taskInputDao.save(inputPO);
                needSendInputArray.add(inputBO);
            } else {
                inputPO.setUpdateTime(new Date());
                inputPO.setCount(inputPO.getCount()+1);
                taskInputDao.save(inputPO);
            }
        }
        if (needSendInputArray.isEmpty()){
            return;
        }
        CreateInputsRequest createInputsRequest = new CreateInputsRequest();
        createInputsRequest.setMsg_id(UUID.randomUUID().toString());
        createInputsRequest.setInput_array(needSendInputArray);
        capacityService.createInputs(deviceIp, createInputsRequest);
    }


    /**
     * @MethodName: addInputInDatabase 查重并创建输入记录数据库，不发命令
     * @Description: TODO 添加输入
     * @param deviceIp 1 设备IP
     * @param inputBO 2 输入BO
     * @param busType 3 业务类型
     * @Return: com.sumavision.tetris.capacity.bo.input.InputWrapperBO
     * @Author: Poemafar
     * @Date: 2021/1/4 17:59
     **/
    public InputWrapperBO addInputInDatabase(String deviceIp, InputBO inputBO, BusinessType busType) throws Exception {
        Boolean beCreate = null;
        String uniq = generateUniq(inputBO);
        TaskInputPO inputPO = taskInputDao.findByUniq(uniq);
        if (inputPO == null) {
            inputPO = new TaskInputPO();
            inputPO.setCreateTime(new Date());
            inputPO.setUpdateTime(inputPO.getCreateTime());
            inputPO.setUniq(uniq);
            inputPO.setType(busType);
            inputPO.setInput(JSON.toJSONString(inputBO));
            inputPO.setNodeId(inputBO.getId());
            inputPO.setCapacityIp(deviceIp);
            beCreate=Boolean.TRUE;
            taskInputDao.save(inputPO);
        } else if (inputPO.getCount().equals(0)) {
            inputPO.setInput(JSON.toJSONString(inputBO));
            inputPO.setNodeId(inputBO.getId());
            inputPO.setType(busType);
            inputPO.setCreateTime(new Date());
            inputPO.setUpdateTime(inputPO.getCreateTime());
            inputPO.setCount(inputPO.getCount() + 1);
            inputPO.setCapacityIp(deviceIp);
            beCreate=Boolean.TRUE;
            taskInputDao.save(inputPO);
        } else {
            inputPO.setUpdateTime(new Date());
            inputPO.setCount(inputPO.getCount()+1);
            beCreate=Boolean.FALSE;
            taskInputDao.save(inputPO);
        }
        InputWrapperBO inputWrapperBO = new InputWrapperBO().setTaskInputPO(inputPO).setBeCreate(beCreate);
        return inputWrapperBO;
    }

    /**
 * @MethodName: deleteStreamInputsAfterCheckRepeat
 * @Description: 删码流分析输入
 * @param deviceIp 1
 * @param inputBOS 2
 * @Return: void
 * @Author: Poemafar
 * @Date: 2020/12/4 10:42
 **/
    public void deleteInputsAfterCheckRepeat(String deviceIp, List<InputBO> inputBOS) throws Exception {
        DeleteInputsRequest deleteInputsRequest = new DeleteInputsRequest();
        List<IdRequest> needDelInputs = new ArrayList<>();
        for (int i = 0; i < inputBOS.size(); i++) {
            InputBO curInputBO = inputBOS.get(i);
            TaskInputPO input = taskInputDao.findByUniq(generateUniq(curInputBO));
            if (!beUseForInputAtAnyTask(input.getId())) {
                needDelInputs.add(new IdRequest().setId(input.getNodeId()));
                input.setUpdateTime(new Date());
                input.setCount(0);
            }else{
                input.setUpdateTime(new Date());
                input.setCount(input.getCount()-1);
            }
            taskInputDao.save(input);
        }
        deleteInputsRequest.setMsg_id(UUID.randomUUID().toString());
        deleteInputsRequest.setInput_array(needDelInputs);
        capacityService.deleteInputs(deviceIp,deleteInputsRequest);
    }

    public Boolean beUseForInputAtAnyTask(Long inputId){
        List<TaskOutputPO> taskOutputs = taskOutputDao.findByTaskUuidNotNullAndOutputNotNullAndTaskNotNull();
        for (int i = 0; i < taskOutputs.size(); i++) {
            TaskOutputPO o = taskOutputs.get(i);
            if (inputId.equals(o.getInputId())){
                return Boolean.TRUE;
            }
            List<Long> inputs = JSONArray.parseArray(o.getInputList(), Long.class);
            if (inputs!=null && !inputs.isEmpty() && inputs.contains(inputId)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @MethodName: getTransformInput
     * @Description: TODO 判断能力要建的输入是否在转换上存在
     * @param inputs 1 转换上的输入
     * @param inputBO 2 准备建的输入
     * @Return: com.sumavision.tetris.capacity.bo.input.InputBO
     * @Author: Poemafar
     * @Date: 2021/1/5 10:03
     **/
    public InputBO getTransformInput(GetInputsResponse inputs,  InputBO inputBO){
        InputBO targetInputBO = null;
        try {
            if (inputBO.getUdp_ts() != null) {
                targetInputBO = inputs.getInput_array().stream().filter(i-> i.getUdp_ts()!=null
                        && i.getUdp_ts().getSource_ip().equals(inputBO.getUdp_ts().getSource_ip())
                        && i.getUdp_ts().getSource_port().equals(inputBO.getUdp_ts().getSource_port())
                        && i.getUdp_ts().getLocal_ip().equals(inputBO.getUdp_ts().getLocal_ip())).findAny().orElse(null);
            }
            if (inputBO.getRtp_ts() != null) {
                targetInputBO = inputs.getInput_array().stream().filter(i->i.getRtp_ts()!=null
                        && i.getRtp_ts().getSource_ip().equals(inputBO.getRtp_ts().getSource_ip())
                        && i.getRtp_ts().getSource_port().equals(inputBO.getRtp_ts().getSource_port())
                        && i.getRtp_ts().getLocal_ip().equals(inputBO.getRtp_ts().getLocal_ip())).findAny().orElse(null);
            }
            if(inputBO.getHttp_ts() != null){
                targetInputBO = inputs.getInput_array().stream().filter(i->i.getHttp_ts()!=null && i.getHttp_ts().getUrl().equals(inputBO.getHttp_ts().getUrl())).findAny().orElse(null);
            }
            if(inputBO.getSrt_ts() != null){
                targetInputBO = inputs.getInput_array().stream().filter(i->i.getSrt_ts()!=null
                        && i.getSrt_ts().getSource_ip().equals(inputBO.getSrt_ts().getSource_ip())
                        && i.getSrt_ts().getSource_port().equals(inputBO.getSrt_ts().getSource_port())).findAny().orElse(null);
            }
            if(inputBO.getHls() != null){
                targetInputBO = inputs.getInput_array().stream().filter(i->i.getHls()!=null && i.getHls().getUrl()==inputBO.getHls().getUrl()).findAny().orElse(null);
            }
            if(inputBO.getDash() != null){
                targetInputBO = inputs.getInput_array().stream().filter(i->i.getDash()!=null && i.getDash().getUrl()==inputBO.getDash().getUrl()).findAny().orElse(null);
            }
            if(inputBO.getMss() != null){
                targetInputBO = inputs.getInput_array().stream().filter(i->i.getMss()!=null && i.getMss().getUrl()==inputBO.getMss().getUrl()).findAny().orElse(null);
            }
            if(inputBO.getRtmp() != null){
                targetInputBO = inputs.getInput_array().stream().filter(i->i.getRtmp()!=null && i.getRtmp().getUrl()==inputBO.getRtmp().getUrl()).findAny().orElse(null);
            }
            if(inputBO.getRtsp() != null){
                targetInputBO = inputs.getInput_array().stream().filter(i->i.getRtsp()!=null && i.getRtsp().getUrl()==inputBO.getRtsp().getUrl()).findAny().orElse(null);
            }
            if(inputBO.getHttp_flv() != null){
                targetInputBO = inputs.getInput_array().stream().filter(i->i.getHttp_flv()!=null && i.getHttp_flv().getUrl()==inputBO.getHttp_flv().getUrl()).findAny().orElse(null);
            }
            if(inputBO.getSdi() != null){
                targetInputBO = inputs.getInput_array().stream().filter(i->i.getSdi()!=null
                        && i.getSdi().getCard_no().equals(inputBO.getSdi().getCard_no())
                        && i.getSdi().getCard_port().equals(inputBO.getSdi().getCard_port())).findAny().orElse(null);
            }
            if(inputBO.getRtp_es() != null){
                targetInputBO = inputs.getInput_array().stream().filter(i->i.getRtp_es()!=null && i.getRtp_es().getLocal_port().equals(inputBO.getRtp_es().getLocal_port())).findAny().orElse(null);
            }
            if(inputBO.getFile() != null){
                targetInputBO=null;
            }
            if(inputBO.getUdp_pcm() != null){
                targetInputBO = inputs.getInput_array().stream().filter(i->i.getUdp_pcm()!=null
                        && i.getUdp_pcm().getSource_ip().equals(inputBO.getUdp_pcm().getSource_ip())
                        && i.getUdp_pcm().getSource_port().equals(inputBO.getUdp_pcm().getSource_port())
                ).findAny().orElse(null);
            }
            if(inputBO.getBack_up_es() != null || inputBO.getBack_up_passby() != null || inputBO.getBack_up_raw() != null){
                targetInputBO=null;
            }

        } catch (Exception e) {
            e.printStackTrace();
            //拿不到就从数据库判断吧
        }

        return targetInputBO;
    }


    /**
     * 清空转换模块上所有任务<br/>
     * <b>作者:</b>wjw<br/>
     * <b>版本：</b>1.0<br/>
     * <b>日期：</b>2020年6月5日 下午2:41:38
     * @param String ip 转换模块ip
     */
    public void removeAll(String ip) throws Exception{

        List<TaskOutputPO> outputs = taskOutputDao.findByCapacityIp(ip);

        if(outputs != null && outputs.size() > 0){
            Set<Long> inputIds = new HashSet<Long>();
            for(TaskOutputPO outputPO: outputs){
                if(!Objects.isNull(outputPO.getInputId())){
                    //单源
                    inputIds.add(outputPO.getInputId());
                }else if(!StringUtils.isEmpty(outputPO.getInputList())){
                    //备份源
                    inputIds.addAll(JSONArray.parseArray(outputPO.getInputList(), Long.class));
                }else if(!Objects.isNull(outputPO.getCoverId())){
                    //盖播
                    inputIds.add(outputPO.getCoverId());
                }else if(!Objects.isNull(outputPO.getScheduleId())){
                    //排期
                    inputIds.add(outputPO.getScheduleId());
                }else if(!Objects.isNull(outputPO.getPrevId())){
                    //追加排期prev
                    inputIds.add(outputPO.getPrevId());
                }else if(!Objects.isNull(outputPO.getNextId())){
                    //追加排期next
                    inputIds.add(outputPO.getNextId());
                }
            }

            List<TaskInputPO> inputPOs = taskInputDao.findByIdIn(inputIds);
            if(inputPOs != null && inputPOs.size() > 0){
                for(TaskInputPO inputPO: inputPOs){
                    inputPO.setCount(0);
                }
            }

            taskInputDao.save(inputPOs);
            taskOutputDao.deleteInBatch(outputs);

        }

        //清空转换模块上面所有任务
        capacityService.removeAll(ip);
    }

}


