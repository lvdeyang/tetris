package com.sumavision.tetris.business.common.service;/**
 * Created by Poemafar on 2021/1/5 19:08
 */

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.business.common.Util.CommonUtil;
import com.sumavision.tetris.business.common.dao.TaskInputDAO;
import com.sumavision.tetris.business.common.dao.TaskOutputDAO;
import com.sumavision.tetris.business.common.enumeration.BusinessType;
import com.sumavision.tetris.business.common.po.TaskInputPO;
import com.sumavision.tetris.business.common.po.TaskOutputPO;
import com.sumavision.tetris.business.transcode.vo.TaskSetVO;
import com.sumavision.tetris.capacity.bo.input.InputBO;
import com.sumavision.tetris.capacity.bo.output.OutputBO;
import com.sumavision.tetris.capacity.bo.request.*;
import com.sumavision.tetris.capacity.bo.task.EncodeBO;
import com.sumavision.tetris.capacity.bo.task.TaskBO;
import com.sumavision.tetris.capacity.constant.UrlConstant;
import com.sumavision.tetris.capacity.service.CapacityService;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @ClassName: TaskModifyService
 * @Description TODO 仅用于修改任务
 * @Author: Poemafar
 * @Version：1.0
 * @Date 2021/1/5 19:08
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TaskModifyService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskModifyService.class);


    @Autowired
    TaskOutputDAO taskOutputDao;

    @Autowired
    TaskInputDAO taskInputDao;

    @Autowired
    TaskService taskService;

    @Autowired
    CapacityService capacityService;

    /**
     * 处理某个设备上关于某个任务的所有任务命令<br/>
     * <b>作者:</b>wjw<br/>
     * <b>版本：</b>1.0<br/>
     * <b>日期：</b>2020年6月5日 下午2:41:38
     * @param String ip 转换模块ip
     */
    public void modifyTask(TaskSetVO taskSetVO,BusinessType businessType) throws Exception {
//先发命令
        String taskUuid = taskSetVO.getTask_link_id().toString();
        String capacityIp = taskSetVO.getDevice_ip();
        //修改任务前，先判断下任务同步着没
//		syncService.checkAndSyncTask(taskUuid,BusinessType.TRANSCODE);
        TaskOutputPO taskOutputPO = taskOutputDao.findByTaskUuidAndType(taskUuid, businessType);
        if (null == taskOutputPO){
            LOGGER.error("task not exist, id: {}", taskUuid);
            throw new BaseException(StatusCode.FORBIDDEN,"task not exist");
        }

        List<Long> inputIds = JSONArray.parseArray(taskOutputPO.getInputList(), Long.class);
        List<TaskInputPO> taskInputPOS = taskInputDao.findByIdIn(inputIds);

        JSONObject request = new JSONObject();
        JSONArray cmdQueue = new JSONArray();

        //增加输入
        if (Objects.nonNull(taskSetVO.getCreate_input())&& CollectionUtils.isNotEmpty(taskSetVO.getCreate_input().getInput_array())){
            List<InputBO> inputBOS = new ArrayList<>();//需要新下发创建输入的
            for (int i = 0; i < taskSetVO.getCreate_input().getInput_array().size(); i++) {
                InputBO curInputBO = taskSetVO.getCreate_input().getInput_array().get(i);
                TaskInputPO dbInput = taskInputDao.findByUniq( taskService.generateUniq(curInputBO));
                if (dbInput==null){
                    TaskInputPO inputPO = taskService.addInputToDB(curInputBO,businessType);
                    inputIds.add(inputPO.getId());
                    inputBOS.add(curInputBO);
                }else{
                    if (dbInput.getCount()==0) {
                        dbInput.setCount(1);
                        dbInput.setUpdateTime(new Date());
                        dbInput.setInput(JSON.toJSONString(curInputBO));
                        dbInput.setNodeId(curInputBO.getId());
                        taskInputDao.save(dbInput);
                        if (!inputIds.contains(dbInput.getId())) {
                            inputIds.add(dbInput.getId());
                        }
                        inputBOS.add(curInputBO);
                    }else{
                        if (!inputIds.contains(dbInput.getId())) {
                            dbInput.setCount(dbInput.getCount()+1);
                            dbInput.setUpdateTime(new Date());
                            taskInputDao.save(dbInput);
                            inputIds.add(dbInput.getId());
                        }
                    }
                }
            }
            if (!inputBOS.isEmpty()){
                CreateInputsRequest createInputsRequest = new CreateInputsRequest().setInput_array(inputBOS).setMsg_id(taskSetVO.getCreate_input().getMsg_id());
                cmdQueue.add(getRequest(RequestMethod.POST, UrlConstant.URL_INPUT,JSON.toJSONString(createInputsRequest)));
//				capacityService.createInputs(capacityIp, createInputsRequest);
            }

        }

        //修改输入参数
        if (CollectionUtils.isNotEmpty(taskSetVO.getModify_input_params())){
            for (int i=0;i<taskSetVO.getModify_input_params().size();i++) {
                PutInputsRequest putInputsRequest = taskSetVO.getModify_input_params().get(i);
                InputBO inputBO = putInputsRequest.getInput();
                TaskInputPO inputPO = taskInputDao.findByUniq(taskService.generateUniq(inputBO));
                if (inputPO==null){
                    throw new BaseException(StatusCode.ERROR,"input not exist");
                }
                putInputsRequest.setInput(JSONObject.parseObject(inputPO.getInput(),InputBO.class));
                StringBuilder sb = new StringBuilder().append(UrlConstant.URL_INPUT)
                        .append("/")
                        .append(putInputsRequest.getInput().getId()).append("/")
                        .append(UrlConstant.URL_INPUT_PARAM);
                cmdQueue.add(getRequest(RequestMethod.PUT, sb.toString(),JSON.toJSONString(putInputsRequest)));
//				capacityService.modifyInputs(capacityIp, putInputsRequest);
                taskService.updateInputToDB(putInputsRequest.getInput(),BusinessType.TRANSCODE);
            }
        }

        //删除输入
        if (Objects.nonNull(taskSetVO.getDelete_input()) && CollectionUtils.isNotEmpty(taskSetVO.getDelete_input().getInput_array())){
            cmdQueue.add(getRequest(RequestMethod.DELETE, UrlConstant.URL_INPUT,JSON.toJSONString(taskSetVO.getDelete_input())));
//			capacityService.deleteInputs(capacityIp, taskSetVO.getDelete_input());
            List<String> delInputList = taskSetVO.getDelete_input().getInput_array().stream().map(IdRequest::getId).collect(Collectors.toList());
            taskInputPOS.stream().forEach(i->{
                InputBO inputBO = JSONObject.parseObject(i.getInput(),InputBO.class);
                if (delInputList.contains(inputBO.getId())){
                    taskInputDao.delete(i);
                    inputIds.remove(i.getId());
                }
            });
            taskOutputPO.setInputList(JSONObject.toJSONString(inputIds));
        }

        //增加节目
        addProgramInModifyTask(taskSetVO,taskInputPOS,cmdQueue);

        //删除节目
        deleteProgramInModifyTask(taskSetVO,taskInputPOS,cmdQueue);

        //修改节目备份
        modifyBackupModeInModifyTask(taskSetVO,taskInputPOS,cmdQueue);

        //创建任务
        addTaskInModifyTask(taskSetVO,taskOutputPO,cmdQueue);

        //删除任务
        deleteTaskInModifyTask(taskSetVO,taskOutputPO,cmdQueue);

        //修改解码预处理
        modifyDecodeProcessInModifyTask(taskSetVO,taskOutputPO,cmdQueue);

        //增加编码
        addEncodeInModifyTask(taskSetVO,taskOutputPO,cmdQueue);

        //修改编码
        modifyEncodeInModifyTask(taskSetVO,taskOutputPO,cmdQueue);

        //修改解码模式
        modifyDecodeModeInModifyTask(taskSetVO,taskInputPOS,cmdQueue);

        //删除编码
        deleteEncodeInModifyTask(taskSetVO,taskOutputPO,cmdQueue);

        //修改源
        modifySourceInModifyTask(taskSetVO,taskOutputPO,cmdQueue);

        //增加输出
        addOutputInModifyTask(taskSetVO,taskOutputPO,cmdQueue);

        //修改输出
        modifyOutputInModifyTask(taskSetVO,taskOutputPO,cmdQueue);

        //删除输出
        deleteOutputInModifyTask(taskSetVO,taskOutputPO,cmdQueue);

        request.put("queue_id", UUID.randomUUID().toString());
        request.put("queue_request",cmdQueue);

        capacityService.sendCommandsByQueue(request,capacityIp,5656);

        taskOutputPO.setInputList(JSONObject.toJSONString(inputIds));
        taskOutputDao.save(taskOutputPO);
    }

    public JSONObject getRequest(RequestMethod method,String uri,String body){
        JSONObject request = new JSONObject();
        request.put("msg_id",UUID.randomUUID().toString());
        request.put("method",method.name().toLowerCase(Locale.ENGLISH));
        request.put("uri",uri);
        request = CommonUtil.coverJSONObject(request,JSONObject.parseObject(body));
        return request;
    }

    /**
     * 修改任务之添加节目
     * @param taskSetVO
     * @param taskInputPOS
     * @param cmdQueue
     */
    public void addProgramInModifyTask(TaskSetVO taskSetVO,List<TaskInputPO> taskInputPOS,JSONArray cmdQueue){
        if (Objects.nonNull(taskSetVO.getCreate_program()) && CollectionUtils.isNotEmpty(taskSetVO.getCreate_program().getProgram_array())){
            CreateProgramsRequest createProgramsRequest = taskSetVO.getCreate_program();
            StringBuilder sb = new StringBuilder().append(UrlConstant.URL_INPUT).append("/")
                    .append(createProgramsRequest.getInput_id()).append("/")
                    .append(UrlConstant.URL_INPUT_PROGRAM);
            cmdQueue.add(getRequest(RequestMethod.POST, sb.toString(),JSON.toJSONString(createProgramsRequest)));
//			capacityService.createProgram(capacityIp, createProgramsRequest);
            taskInputPOS.stream().forEach(i-> {
                InputBO inputBO = JSONObject.parseObject(i.getInput(), InputBO.class);
                if (inputBO.getId().equals(createProgramsRequest.getInput_id())){
                    inputBO.getProgram_array().addAll(createProgramsRequest.getProgram_array());
                    i.setInput(JSONObject.toJSONString(inputBO));
                    i.setNodeId(inputBO.getId());
                    taskInputDao.save(i);
                }
            });
        }

    }

    /**
     * 修改任务之删节目
     * @param taskSetVO
     * @param taskInputPOS
     * @param cmdQueue
     */
    public void deleteProgramInModifyTask(TaskSetVO taskSetVO,List<TaskInputPO> taskInputPOS,JSONArray cmdQueue){
        if (Objects.nonNull(taskSetVO.getDelete_program()) && CollectionUtils.isNotEmpty(taskSetVO.getDelete_program().getProgram_array())){
            DeleteProgramRequest deleteProgramRequest = taskSetVO.getDelete_program();
            StringBuilder sb = new StringBuilder().append(UrlConstant.URL_INPUT).append("/")
                    .append(deleteProgramRequest.getInput_id()).append("/")
                    .append(UrlConstant.URL_INPUT_PROGRAM);
            cmdQueue.add(getRequest(RequestMethod.DELETE, sb.toString(),JSON.toJSONString(deleteProgramRequest)));
//			capacityService.deleteProgram(capacityIp,deleteProgramRequest );

            taskInputPOS.stream().forEach(i-> {
                InputBO inputBO = JSONObject.parseObject(i.getInput(), InputBO.class);
                if (inputBO.getId().equals(deleteProgramRequest.getInput_id())){
                    inputBO.getProgram_array().removeIf(p->{
                        return deleteProgramRequest.getProgram_array().stream().anyMatch(d->d.getProgram_number().equals(p.getProgram_number()));
                    });
                    i.setInput(JSONObject.toJSONString(inputBO));
                    i.setNodeId(inputBO.getId());
                    taskInputDao.save(i);
                }
            });
        }
    }

    /**
     * 修改任务之修改源
     * @param taskSetVO
     * @param taskOutputPO
     * @param cmdQueue
     */
    public void modifySourceInModifyTask(TaskSetVO taskSetVO,TaskOutputPO taskOutputPO,JSONArray cmdQueue){
        if (CollectionUtils.isNotEmpty(taskSetVO.getModify_source())){
            for (int i=0;i<taskSetVO.getModify_source().size();i++) {
                PutTaskSourceRequest putTaskSourceRequest = taskSetVO.getModify_source().get(i);
                StringBuilder sb = new StringBuilder().append(UrlConstant.URL_TASK).append("/")
                        .append(putTaskSourceRequest.getTask_id()).append("/")
                        .append(UrlConstant.URL_TASK_SOURCE);
                cmdQueue.add(getRequest(RequestMethod.PUT, sb.toString(),JSON.toJSONString(putTaskSourceRequest)));
//				capacityService.modifyTaskSource(capacityIp,putTaskSourceRequest);
                List<TaskBO> oriTaskBOS = JSONObject.parseArray(taskOutputPO.getTask(), TaskBO.class);
                oriTaskBOS.stream().filter(t->putTaskSourceRequest.getTask_id().equals(t.getId())).forEach(t->{
                    if (putTaskSourceRequest.getRaw_source()!=null){ t.setRaw_source(putTaskSourceRequest.getRaw_source()); }
                    if (putTaskSourceRequest.getEs_source()!=null){ t.setEs_source(putTaskSourceRequest.getEs_source()); }
                    if (putTaskSourceRequest.getPassby_source()!=null){ t.setPassby_source(putTaskSourceRequest.getPassby_source()); }
                    if (putTaskSourceRequest.getVideo_mix_source()!=null){ t.setVideo_mix_source(putTaskSourceRequest.getVideo_mix_source()); }
                    if (putTaskSourceRequest.getAudio_mix_source()!=null){ t.setAudio_mix_source(putTaskSourceRequest.getAudio_mix_source()); }
                });
                taskOutputPO.setTask(JSON.toJSONString(oriTaskBOS));
            }
        }
    }

    /**
     * 修改任务之增加输出，，，已测
     * @param taskSetVO
     * @param taskOutputPO
     * @param cmdQueue
     */
    public void addOutputInModifyTask(TaskSetVO taskSetVO,TaskOutputPO taskOutputPO,JSONArray cmdQueue){
        if (Objects.nonNull(taskSetVO.getAdd_output()) && CollectionUtils.isNotEmpty(taskSetVO.getAdd_output().getOutput_array())) {
            cmdQueue.add(getRequest(RequestMethod.POST, UrlConstant.URL_OUTPUT,JSON.toJSONString(taskSetVO.getAdd_output())));
//			CreateOutputsResponse outputResponse = capacityService.createOutputsWithMsgId(taskSetVO.getAdd_output(),capacityIp);
//			List<String> outputIds = responseService.outputResponseProcess(outputResponse, null, null, capacityIp);

            JSONArray oriOutputs = JSON.parseArray(taskOutputPO.getOutput());
            oriOutputs.addAll(taskSetVO.getAdd_output().getOutput_array());
            taskOutputPO.setOutput(JSON.toJSONString(oriOutputs));
        }
    }

    /**
     * 修改任务之修改输出，，，已测
     * @param taskSetVO
     * @param taskOutputPO
     * @param cmdQueue
     */
    public void modifyOutputInModifyTask(TaskSetVO taskSetVO,TaskOutputPO taskOutputPO,JSONArray cmdQueue) throws BaseException {
        if (CollectionUtils.isNotEmpty(taskSetVO.getModify_output())){
            for (int i=0;i<taskSetVO.getModify_output().size();i++) {
                List<OutputBO> oriOutputs = JSONObject.parseArray(taskOutputPO.getOutput(), OutputBO.class);
                List<String> needModifyOutIds = taskSetVO.getModify_output().stream().map(PutOutputRequest::getOutput).map(OutputBO::getId).collect(Collectors.toList());
                List<OutputBO> needModifyOutputs = oriOutputs.stream().filter(o->needModifyOutIds.contains(o.getId())).collect(Collectors.toList());
                if (!needModifyOutputs.isEmpty()) {
                    StringBuilder sb = new StringBuilder().append(UrlConstant.URL_OUTPUT).append("/")
                            .append(taskSetVO.getModify_output().get(i).getOutput().getId()).append("/")
                            .append(UrlConstant.URL_INPUT_PARAM);
                    cmdQueue.add(getRequest(RequestMethod.PUT, sb.toString(),JSON.toJSONString(taskSetVO.getModify_output().get(i))));
//					capacityService.modifyOutputById(capacityIp, taskSetVO.getModify_output().get(i));
                    oriOutputs.removeAll(needModifyOutputs);
                    oriOutputs.addAll(taskSetVO.getModify_output().stream().map(PutOutputRequest::getOutput).collect(Collectors.toList()));
                    taskOutputPO.setOutput(JSON.toJSONString(oriOutputs));
                }else{
                    throw new BaseException(StatusCode.FORBIDDEN,"modify output not exist");
                }
            }
        }
    }

    /**
     * 修改任务之删输出，，，已测
     * @param taskSetVO
     * @param taskOutputPO
     * @param cmdQueue
     */
    public void deleteOutputInModifyTask(TaskSetVO taskSetVO,TaskOutputPO taskOutputPO,JSONArray cmdQueue){
        if (Objects.nonNull(taskSetVO.getDelete_output()) && CollectionUtils.isNotEmpty(taskSetVO.getDelete_output().getOutput_array())) {
            List<OutputBO> oriOutputs = JSONObject.parseArray(taskOutputPO.getOutput(), OutputBO.class);
            List<String> needDeleteOutIds = taskSetVO.getDelete_output().getOutput_array().stream().map(IdRequest::getId).collect(Collectors.toList());
            List<OutputBO> needDelOutputs = oriOutputs.stream().filter(o->needDeleteOutIds.contains(o.getId())).collect(Collectors.toList());
            if (!needDeleteOutIds.isEmpty()) {
                cmdQueue.add(getRequest(RequestMethod.DELETE, UrlConstant.URL_OUTPUT,JSON.toJSONString(taskSetVO.getDelete_output())));
//				capacityService.deleteOutputsWithMsgId(taskSetVO.getDelete_output(), capacityIp);
                oriOutputs.removeAll(needDelOutputs);
                taskOutputPO.setOutput(JSON.toJSONString(oriOutputs));
            }else{
                LOGGER.warn("delete output not exist");
            }
        }
    }

    /**
     * 修改输入备份关系
     * @param inputSetVO
     * @param taskInputPOS
     * @throws Exception
     */
    public void modifyBackupModeInModifyTask(TaskSetVO inputSetVO,List<TaskInputPO> taskInputPOS,JSONArray cmdQueue) throws Exception {
        if (CollectionUtils.isNotEmpty(inputSetVO.getModify_backup_mode())) {
            for (int i = 0; i < inputSetVO.getModify_backup_mode().size(); i++) {
                PutBackupModeRequest putBackupModeRequest = inputSetVO.getModify_backup_mode().get(i);
                for (int i1 = 0; i1 < taskInputPOS.size(); i1++) {
                    TaskInputPO inputPO = taskInputPOS.get(i1);
                    InputBO inputBO = JSONObject.parseObject(inputPO.getInput(),InputBO.class);
                    if (inputBO.getId().equals(putBackupModeRequest.getInputId())){
                        if (inputBO.getBack_up_passby() != null) {
                            inputBO.getBack_up_passby().setMode(putBackupModeRequest.getMode());
                            inputBO.getBack_up_passby().setSelect_index(putBackupModeRequest.getSelect_index());
                        }
                        if (inputBO.getBack_up_es() != null) {
                            inputBO.getBack_up_es().setMode(putBackupModeRequest.getMode());
                            inputBO.getBack_up_es().setSelect_index(putBackupModeRequest.getSelect_index());
                        }
                        if (inputBO.getBack_up_raw() != null) {
                            inputBO.getBack_up_raw().setMode(putBackupModeRequest.getMode());
                            inputBO.getBack_up_raw().setSelect_index(putBackupModeRequest.getSelect_index());
                        }
                        taskService.updateInputToDB(inputBO,BusinessType.TRANSCODE);

                        StringBuilder sb = new StringBuilder().append(UrlConstant.URL_INPUT).append("/")
                                .append(putBackupModeRequest.getInputId()).append("/")
                                .append(UrlConstant.URL_TASK_SOURCE_INDEX);
                        cmdQueue.add(getRequest(RequestMethod.PUT,sb.toString(),JSON.toJSONString(putBackupModeRequest)));
//						capacityService.changeBackup(capacityIp, putBackupModeRequest);
                    }
                }
            }
        }
    }

    /**
     * 修改任务之添加任务节点，，，已测
     * @param taskSetVO
     * @param taskOutputPO
     * @param cmdQueue
     */
    public void addTaskInModifyTask(TaskSetVO taskSetVO,TaskOutputPO taskOutputPO,JSONArray cmdQueue){
        if (Objects.nonNull(taskSetVO.getAdd_task()) && CollectionUtils.isNotEmpty(taskSetVO.getAdd_task().getTask_array())){
            CreateTaskRequest createTaskRequest = taskSetVO.getAdd_task();
            cmdQueue.add(getRequest(RequestMethod.POST, UrlConstant.URL_TASK,JSON.toJSONString(createTaskRequest)));
//			capacityService.createTasksWithMsgId(createTaskRequest,capacityIp);
            List<TaskBO> oriTaskBOS = JSONObject.parseArray(taskOutputPO.getTask(), TaskBO.class);
            createTaskRequest.getTask_array().stream().forEach(t->{
                oriTaskBOS.add(t);
            });
            taskOutputPO.setTask(JSON.toJSONString(oriTaskBOS));
        }
    }

    /**
     * 修改任务之删除任务节点，已测
     * @param taskSetVO
     * @param taskOutputPO
     * @param cmdQueue
     */
    public void deleteTaskInModifyTask(TaskSetVO taskSetVO,TaskOutputPO taskOutputPO,JSONArray cmdQueue){
        if (Objects.nonNull(taskSetVO.getDelete_task()) && CollectionUtils.isNotEmpty(taskSetVO.getDelete_task().getTask_array())){
            DeleteTasksRequest deleteTasksRequest = taskSetVO.getDelete_task();
            cmdQueue.add(getRequest(RequestMethod.DELETE, UrlConstant.URL_TASK,JSON.toJSONString(deleteTasksRequest)));
//			capacityService.deleteTasksWithMsgId(deleteTasksRequest,capacityIp);
            List<TaskBO> oriTaskBOS = JSONObject.parseArray(taskOutputPO.getTask(), TaskBO.class);
            deleteTasksRequest.getTask_array().forEach(t->{
                oriTaskBOS.removeIf(o->o.getId().equals(t.getId()));
            });
            taskOutputPO.setTask(JSON.toJSONString(oriTaskBOS));
        }
    }

    /**
     * 修改任务之修改解码预处理
     * @param taskSetVO
     * @param taskOutputPO
     * @param cmdQueue
     */
    public void modifyDecodeProcessInModifyTask(TaskSetVO taskSetVO,TaskOutputPO taskOutputPO,JSONArray cmdQueue){
        if (CollectionUtils.isNotEmpty(taskSetVO.getModify_decode_process())){
            for (int i=0;i<taskSetVO.getModify_decode_process().size();i++) {
                PutTaskDecodeProcessRequest putTaskDecodeProcessRequest = taskSetVO.getModify_decode_process().get(i);
                StringBuilder sb = new StringBuilder().append(UrlConstant.URL_TASK).append("/")
                        .append(putTaskDecodeProcessRequest.getTask_id()).append("/")
                        .append(UrlConstant.URL_TASK_DECODE_PROCESS);
                cmdQueue.add(getRequest(RequestMethod.PUT, sb.toString(),JSON.toJSONString(putTaskDecodeProcessRequest)));
//				capacityService.modifyTaskDecodeProcess(capacityIp, putTaskDecodeProcessRequest);
                List<TaskBO> oriTaskBOS = JSONObject.parseArray(taskOutputPO.getTask(), TaskBO.class);
                oriTaskBOS.stream().filter(t->putTaskDecodeProcessRequest.getTask_id().equals(t.getId())).forEach(t->{
                    t.getDecode_process_array().clear();
                    t.setDecode_process_array(putTaskDecodeProcessRequest.getProcess_array());
                });
                taskOutputPO.setTask(JSON.toJSONString(oriTaskBOS));
            }
        }
    }

    /**
     * 修改任务之增加编码，，，已测
     * @param taskSetVO
     * @param taskOutputPO
     * @param cmdQueue
     */
    public void addEncodeInModifyTask(TaskSetVO taskSetVO,TaskOutputPO taskOutputPO,JSONArray cmdQueue){
        if (CollectionUtils.isNotEmpty(taskSetVO.getAdd_encoders())){
            for (int i=0;i<taskSetVO.getAdd_encoders().size();i++) {
                AddTaskEncodeRequest addTaskEncodeRequest = taskSetVO.getAdd_encoders().get(i);
                List<TaskBO> oriTaskBOS = JSONObject.parseArray(taskOutputPO.getTask(), TaskBO.class);
                for (int j=0;j<oriTaskBOS.size();j++){
                    TaskBO oriTask = oriTaskBOS.get(j);
                    if (oriTask.getId().equals(addTaskEncodeRequest.getTask_id())){
                        StringBuilder sb = new StringBuilder().append(UrlConstant.URL_TASK).append("/")
                                .append(addTaskEncodeRequest.getTask_id()).append("/")
                                .append(UrlConstant.URL_TASK_ENCODE);
                        cmdQueue.add(getRequest(RequestMethod.POST, sb.toString(),JSON.toJSONString(addTaskEncodeRequest)));
//						capacityService.addTaskEncode(capacityIp, addTaskEncodeRequest);
                        oriTask.getEncode_array().addAll(addTaskEncodeRequest.getEncode_array());
                    }
                }
                taskOutputPO.setTask(JSON.toJSONString(oriTaskBOS));
            }
        }
    }

    /**
     * 修改任务之修改编码，，，已测
     * @param taskSetVO
     * @param taskOutputPO
     * @param cmdQueue
     */
    public void modifyEncodeInModifyTask(TaskSetVO taskSetVO,TaskOutputPO taskOutputPO,JSONArray cmdQueue){
        if (CollectionUtils.isNotEmpty(taskSetVO.getModify_encoders())){
            for (int i=0;i<taskSetVO.getModify_encoders().size();i++) {
                PutTaskEncodeRequest putTaskEncodeRequest = taskSetVO.getModify_encoders().get(i);
                List<TaskBO> oriTaskBOS = JSONObject.parseArray(taskOutputPO.getTask(), TaskBO.class);
                for (int j=0;j<oriTaskBOS.size();j++){
                    TaskBO oriTask = oriTaskBOS.get(j);
                    if (oriTask.getId().equals(putTaskEncodeRequest.getTask_id())){
                        StringBuilder sb = new StringBuilder().append(UrlConstant.URL_TASK).append("/")
                                .append(putTaskEncodeRequest.getTask_id()).append("/")
                                .append(UrlConstant.URL_TASK_ENCODE).append("/")
                                .append(putTaskEncodeRequest.getEncode_param().getEncode_id());
                        cmdQueue.add(getRequest(RequestMethod.PUT, sb.toString(),JSON.toJSONString(putTaskEncodeRequest)));
//						capacityService.modifyTaskEncode(capacityIp, putTaskEncodeRequest);
                        oriTask.getEncode_array().removeIf(e->e.getEncode_id().equals(putTaskEncodeRequest.getEncode_param().getEncode_id()));
                        oriTask.getEncode_array().add(putTaskEncodeRequest.getEncode_param());
                    }
                }
                taskOutputPO.setTask(JSON.toJSONString(oriTaskBOS));
            }
        }
    }

    /**
     * 修改任务之修改解码模式
     * @param taskSetVO
     * @param cmdQueue
     * @throws Exception
     */
    public void modifyDecodeModeInModifyTask(TaskSetVO taskSetVO,List<TaskInputPO> taskInputPOS,JSONArray cmdQueue) throws Exception {
        if ( CollectionUtils.isNotEmpty(taskSetVO.getModify_decode_mode())){
            for (int i = 0; i < taskSetVO.getModify_decode_mode().size(); i++) {
                PatchDecodeRequest patchDecodeRequest = taskSetVO.getModify_decode_mode().get(i);

                Integer programNum = Integer.parseInt(patchDecodeRequest.getProgram_num());
                Integer pid = Integer.parseInt(patchDecodeRequest.getPid());
                String encodeMode = patchDecodeRequest.getDecode_mode();
                if (programNum == null || pid == null || encodeMode == null || encodeMode.isEmpty() ){
                    throw new Exception("modify encode param parse error");
                }

                StringBuilder sb = new StringBuilder().append(UrlConstant.URL_INPUT).append("/")
                        .append(patchDecodeRequest.getInput_id()).append("/")
                        .append(UrlConstant.URL_INPUT_PROGRAM).append("/")
                        .append(programNum).append("/")
                        .append(UrlConstant.URL_INPUT_PROGRAM_ElEMENTS).append("/")
                        .append(pid).append("/")
                        .append(UrlConstant.URL_INPUT_PROGRAM_DECODE);
                cmdQueue.add(getRequest(RequestMethod.PUT, sb.toString(),JSON.toJSONString(patchDecodeRequest)));
                //				capacityService.modifyProgramDecodeMode(capacityIp,patchDecodeRequest);

                taskInputPOS.stream().forEach(inputPO-> {
                    InputBO inputBO = JSONObject.parseObject(inputPO.getInput(), InputBO.class);
                    inputBO.getProgram_array().stream().filter(in->programNum.equals(in.getProgram_number())).forEach(p->{
                        if (p.getAudio_array()!=null &&!p.getAudio_array().isEmpty()) { p.getAudio_array().stream().filter(a -> pid.equals(a.getPid())).forEach(a -> a.setDecode_mode(encodeMode)); }
                        if (p.getVideo_array()!=null &&!p.getVideo_array().isEmpty()) {p.getVideo_array().stream().filter(a->pid.equals(a.getPid())).forEach(a->a.setDecode_mode(encodeMode));}
                        if (p.getSubtitle_array()!=null &&!p.getSubtitle_array().isEmpty()) {p.getSubtitle_array().stream().filter(a->pid.equals(a.getPid())).forEach(a->a.setDecode_mode(encodeMode));}
                    });
                    inputPO.setInput(JSONObject.toJSONString(inputBO));
                    inputPO.setNodeId(inputBO.getId());
                    taskInputDao.save(inputPO);
                });
            }
        }
    }

    /**
     * 删除编码,，，已测
     * @param taskSetVO
     * @param taskOutputPO
     * @param cmdQueue
     */
    public void deleteEncodeInModifyTask(TaskSetVO taskSetVO,TaskOutputPO taskOutputPO,JSONArray cmdQueue){
        if (CollectionUtils.isNotEmpty(taskSetVO.getDelete_encoders())){
            for (int i=0;i<taskSetVO.getDelete_encoders().size();i++) {
                DeleteTaskEncodeResponse deleteTaskEncodeResponse = taskSetVO.getDelete_encoders().get(i);
                List<TaskBO> oriTaskBOS = JSONObject.parseArray(taskOutputPO.getTask(), TaskBO.class);
                for (int j=0;j<oriTaskBOS.size();j++){
                    TaskBO oriTask = oriTaskBOS.get(j);
                    if (oriTask.getId().equals(deleteTaskEncodeResponse.getTask_id())){
                        StringBuilder sb = new StringBuilder().append(UrlConstant.URL_TASK).append("/")
                                .append(deleteTaskEncodeResponse.getTask_id()).append("/")
                                .append(UrlConstant.URL_TASK_ENCODE);
                        cmdQueue.add(getRequest(RequestMethod.DELETE, sb.toString(),JSON.toJSONString(deleteTaskEncodeResponse)));
//						capacityService.deleteTaskEncode(capacityIp, deleteTaskEncodeResponse);
                        for (int k=0; k < oriTask.getEncode_array().size(); k++) {
                            EncodeBO encodeBO = oriTask.getEncode_array().get(k);
                            deleteTaskEncodeResponse.getEncode_array().stream()
                                    .filter(e->e.getEncode_id().equals(encodeBO.getEncode_id()))
                                    .forEach(e->{
                                        oriTask.getEncode_array().remove(encodeBO);
                                    });
                        }
                    }
                }
                taskOutputPO.setTask(JSON.toJSONString(oriTaskBOS));
            }
        }

    }

}
