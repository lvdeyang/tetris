package com.sumavision.tetris.capacity.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.sumavision.tetris.capacity.bo.request.AddTaskEncodeRequest;
import com.sumavision.tetris.capacity.bo.request.AllRequest;
import com.sumavision.tetris.capacity.bo.request.CreateInputsRequest;
import com.sumavision.tetris.capacity.bo.request.CreateOutputsRequest;
import com.sumavision.tetris.capacity.bo.request.CreateProgramsRequest;
import com.sumavision.tetris.capacity.bo.request.CreateTaskRequest;
import com.sumavision.tetris.capacity.bo.request.DeleteInputsRequest;
import com.sumavision.tetris.capacity.bo.request.DeleteOutputsRequest;
import com.sumavision.tetris.capacity.bo.request.DeleteProgramRequest;
import com.sumavision.tetris.capacity.bo.request.DeleteTaskEncodeResponse;
import com.sumavision.tetris.capacity.bo.request.DeleteTasksRequest;
import com.sumavision.tetris.capacity.bo.request.GetEntiretiesResponse;
import com.sumavision.tetris.capacity.bo.request.PatchDecodeRequest;
import com.sumavision.tetris.capacity.bo.request.PutInputsRequest;
import com.sumavision.tetris.capacity.bo.request.PutOutputRequest;
import com.sumavision.tetris.capacity.bo.request.PutRealIndexRequest;
import com.sumavision.tetris.capacity.bo.request.PutScheduleRequest;
import com.sumavision.tetris.capacity.bo.request.PutTaskDecodeProcessRequest;
import com.sumavision.tetris.capacity.bo.request.PutTaskEncodeRequest;
import com.sumavision.tetris.capacity.bo.request.PutTaskSourceRequest;
import com.sumavision.tetris.capacity.bo.request.ResultCodeResponse;
import com.sumavision.tetris.capacity.bo.response.AllResponse;
import com.sumavision.tetris.capacity.bo.response.AnalysisResponse;
import com.sumavision.tetris.capacity.bo.response.CreateInputsResponse;
import com.sumavision.tetris.capacity.bo.response.CreateOutputsResponse;
import com.sumavision.tetris.capacity.bo.response.CreateProgramResponse;
import com.sumavision.tetris.capacity.bo.response.CreateTaskResponse;
import com.sumavision.tetris.capacity.bo.response.GetInputsResponse;
import com.sumavision.tetris.capacity.bo.response.GetOutputResponse;
import com.sumavision.tetris.capacity.bo.response.GetOutputsResponse;
import com.sumavision.tetris.capacity.bo.response.GetTaskResponse;
import com.sumavision.tetris.capacity.bo.response.PutInputResponse;
import com.sumavision.tetris.capacity.bo.response.TaskBaseResponse;
import com.sumavision.tetris.capacity.bo.response.TaskEncodeResponse;
import com.sumavision.tetris.capacity.bo.response.TaskEncodeResultResponse;
import com.sumavision.tetris.capacity.bo.response.TaskRealIndexResponse;
import com.sumavision.tetris.capacity.config.CapacityProps;
import com.sumavision.tetris.capacity.constant.UrlConstant;
import com.sumavision.tetris.capacity.exception.HttpTimeoutException;
import com.sumavision.tetris.capacity.util.http.HttpUtil;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

@Service
public class CapacityService {
	
	@Autowired
	private CapacityProps capacityProps;

	/**
	 * 获取输入<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月11日 下午1:41:19
	 * @return GetInputsResponse
	 */
	public GetInputsResponse getInputs() throws Exception{
		
		String msg_id = UUID.randomUUID().toString().replaceAll("-", "");
		
		return getInputs(msg_id);
	}
	
	/**
	 * 获取输入<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月4日 上午11:26:59
	 * @param msg_id 消息id
	 * @return GetInputsRespBO 输入信息
	 */
	private GetInputsResponse getInputs(String msg_id) throws Exception{
		
		String url = new StringBufferWrapper().append(UrlConstant.URL_PREFIX)
										      .append(capacityProps.getIp())
										      .append(":")
										      .append(capacityProps.getPort())
										      .append(UrlConstant.URL_INPUT)
										      .append("?msg_id=")
										      .append(msg_id)
										      .toString();
		
		JSONObject res = HttpUtil.httpGet(url);
		
		if(res == null) throw new HttpTimeoutException(capacityProps.getIp());
		
		GetInputsResponse response = JSONObject.parseObject(res.toJSONString(), GetInputsResponse.class);
		
		return response;
		
	}
	
	/**
	 * 创建all,加msg_id<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月28日 下午2:39:30
	 * @param AllRequest all
	 * @return AllResponse  
	 */
	public AllResponse createAllAddMsgId(AllRequest all, String ip, Long port) throws Exception{
		
		String msg_id = UUID.randomUUID().toString().replaceAll("-", "");
		all.setMsg_id(msg_id);
		
		Date date = new Date();
        String str = "yyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(str);
        System.out.println(sdf.format(date));
		System.out.println("create:  " + JSONObject.toJSONString(all));
		
		return createAll(all, ip, port);
	}
	
	/**
	 * 创建all<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月28日 下午2:39:30
	 * @param AllRequest all
	 * @return AllResponse  
	 */
	private AllResponse createAll(AllRequest all, String ip, Long port) throws Exception{
		
		String url = new StringBufferWrapper().append(UrlConstant.URL_PREFIX)
										      .append(ip)
										      .append(":")
										      .append(port)
										      .append(UrlConstant.URL_COMBINE)
										      .toString();
		
		JSONObject request = JSONObject.parseObject(JSON.toJSONString(all, SerializerFeature.DisableCircularReferenceDetect));
		
		JSONObject res = HttpUtil.httpPost(url, request);
		
		if(res == null) throw new HttpTimeoutException(ip);
		
		AllResponse response = JSONObject.parseObject(res.toJSONString(), AllResponse.class);
		
		return response;
		
	}
	
	/**
	 * 删除all加msg_id<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月28日 下午2:47:06
	 * @param AllRequest all
	 */
	public void deleteAllAddMsgId(AllRequest all, String ip, Long port) throws Exception{
		
		String msg_id = UUID.randomUUID().toString().replaceAll("-", "");
		all.setMsg_id(msg_id);
		
		Date date = new Date();
        String str = "yyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(str);
        System.out.println(sdf.format(date));
		System.out.println("delete:  " + JSONObject.toJSONString(all));
		
		deleteAll(all, ip, port);
	}
	
	/**
	 * 删除all<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月28日 下午3:20:44
	 * @param AllRequest all
	 */
	private void deleteAll(AllRequest all, String ip, Long port) throws Exception{
		
		String url = new StringBufferWrapper().append(UrlConstant.URL_PREFIX)
										      .append(ip)
										      .append(":")
										      .append(port)
										      .append(UrlConstant.URL_COMBINE)
										      .toString();
		
		JSONObject request = JSONObject.parseObject(JSON.toJSONString(all));
		
		HttpUtil.httpDelete(url, request);
		
	}

	/**
	 * 创建输入<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月11日 下午1:43:40
	 * @param CreateInputsRequest input 不带msg_id的input
	 * @return CreateInputsResponse
	 */
	public CreateInputsResponse createInputsAddMsgId(CreateInputsRequest input) throws Exception{
		
		String msg_id = UUID.randomUUID().toString().replaceAll("-", "");
		input.setMsg_id(msg_id);
		
		System.out.println(JSONObject.toJSONString(input));
		
		return createInputs(input);
		
	}
	
	/**
	 * 创建输入<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月4日 下午7:34:04
	 * @param CreateInputsRequest input 输入参数
	 * @return CreateInputsResponse 创建输入返回
	 */
	private CreateInputsResponse createInputs(CreateInputsRequest input) throws Exception{
		
		String url = new StringBufferWrapper().append(UrlConstant.URL_PREFIX)
										      .append(capacityProps.getIp())
										      .append(":")
										      .append(capacityProps.getPort())
										      .append(UrlConstant.URL_INPUT)
										      .toString();
		
		JSONObject request = JSONObject.parseObject(JSON.toJSONString(input, SerializerFeature.DisableCircularReferenceDetect));
		
		JSONObject res = HttpUtil.httpPost(url, request);
		
		if(res == null) throw new HttpTimeoutException(capacityProps.getIp());
		
		CreateInputsResponse response = JSONObject.parseObject(res.toJSONString(), CreateInputsResponse.class);
		
		return response;
		
	}
	
	/**
	 * 删除输入<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月11日 下午1:46:11
	 * @param DeleteInputsRequest input 不带msg_id的input
	 */
	public void deleteInputsAddMsgId(DeleteInputsRequest input, String capacityIp) throws Exception{
		
		String msg_id = UUID.randomUUID().toString().replaceAll("-", "");
		input.setMsg_id(msg_id);
		
		deleteInputs(input, capacityIp);
		
	}
	
	/**
	 * 删除输入<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月5日 上午9:51:20
	 * @param DeleteInputsRequest input 删除输入请求
	 */
	private void deleteInputs(DeleteInputsRequest input, String capacityIp) throws Exception{
		
		String url = new StringBufferWrapper().append(UrlConstant.URL_PREFIX)
										      .append(capacityIp)
										      .append(":")
										      .append(capacityProps.getPort())
										      .append(UrlConstant.URL_INPUT)
										      .toString();

		JSONObject request = JSONObject.parseObject(JSON.toJSONString(input));
		
		HttpUtil.httpDelete(url, request);
		
	}
	
	/**
	 * 修改输入<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月11日 下午1:49:16
	 * @param String inputId
	 * @param PutInputsRequest input 不带msg_id的input
	 * @return PutInputResponse
	 */
	public PutInputResponse modifyInputsAddMsgId(String inputId, PutInputsRequest input) throws Exception{
		
		String msg_id = UUID.randomUUID().toString().replaceAll("-", "");
		input.setMsg_id(msg_id);
		
		return modifyInputs(inputId, input);
		
	}
	
	/**
	 * 修改输入<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月5日 上午10:37:27
	 * @param String inputId 输入id
	 * @param PutInputsRequest input 修改输入参数
	 * @return PutInputResponse 修改输入返回参数
	 */
	private PutInputResponse modifyInputs(String inputId, PutInputsRequest input) throws Exception{
		
		String url = new StringBufferWrapper().append(UrlConstant.URL_PREFIX)
										      .append(capacityProps.getIp())
										      .append(":")
										      .append(capacityProps.getPort())
										      .append(UrlConstant.URL_INPUT)
										      .append("/")
										      .append(inputId)
										      .append(UrlConstant.URL_INPUT_PARAM)
										      .toString();

		JSONObject request = JSONObject.parseObject(JSON.toJSONString(input));
		
		JSONObject res = HttpUtil.httpPut(url, request);
		
		if(res == null) throw new HttpTimeoutException(capacityProps.getIp());
		
		PutInputResponse response = JSONObject.parseObject(res.toJSONString(), PutInputResponse.class);
		
		return response;
		
	}
	
	/**
	 * 创建节目<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月11日 下午1:51:37
	 * @param String inputId
	 * @param CreateProgramsRequest program 不带msg_id的program
	 * @return CreateProgramResponse
	 */
	public CreateProgramResponse createProgramAddMsgId(String inputId, CreateProgramsRequest program) throws Exception{
		
		String msg_id = UUID.randomUUID().toString().replaceAll("-", "");
		program.setMsg_id(msg_id);
		
		return createProgram(inputId, program);
		
	}
	
	/**
	 * 创建节目<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月5日 上午11:17:34
	 * @param String inputId 输入id
	 * @param CreateProgramsRequest program 节目参数
	 * @return CreateProgramResponse CreateProgramResponse 创建节目返回
	 */
	private CreateProgramResponse createProgram(String inputId, CreateProgramsRequest program) throws Exception{
		
		String url = new StringBufferWrapper().append(UrlConstant.URL_PREFIX)
										      .append(capacityProps.getIp())
										      .append(":")
										      .append(capacityProps.getPort())
										      .append(UrlConstant.URL_INPUT)
										      .append("/")
										      .append(inputId)
										      .append(UrlConstant.URL_INPUT_PROGRAM)
										      .toString();

		JSONObject request = JSONObject.parseObject(JSON.toJSONString(program, SerializerFeature.DisableCircularReferenceDetect));
		
		JSONObject res = HttpUtil.httpPost(url, request);
		
		if(res == null) throw new HttpTimeoutException(capacityProps.getIp());
		
		CreateProgramResponse response = JSONObject.parseObject(res.toJSONString(), CreateProgramResponse.class);
		
		return response;
		
	}
	
	/**
	 * 删除节目<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月11日 下午1:54:29
	 * @param String inputId
	 * @param DeleteProgramRequest program 不带msg_id的program
	 */
	public void deleteProgramAddMsgId(String inputId, DeleteProgramRequest program) throws Exception{
		
		String msg_id = UUID.randomUUID().toString().replaceAll("-", "");
		program.setMsg_id(msg_id);
		
		deleteProgram(inputId, program);
		
	}
	
	/**
	 * 删除节目<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月5日 上午11:34:35
	 * @param String inputId 输入id
	 * @param DeleteProgramRequest program 删除节目参数
	 */
	private void deleteProgram(String inputId, DeleteProgramRequest program) throws Exception{
		
		String url = new StringBufferWrapper().append(UrlConstant.URL_PREFIX)
										      .append(capacityProps.getIp())
										      .append(":")
										      .append(capacityProps.getPort())
										      .append(UrlConstant.URL_INPUT)
										      .append("/")
										      .append(inputId)
										      .append(UrlConstant.URL_INPUT_PROGRAM)
										      .toString();

		JSONObject request = JSONObject.parseObject(JSON.toJSONString(program));
		
		HttpUtil.httpDelete(url, request);
		
	}
	
	/**
	 * 修改指定节目解码<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月11日 下午1:59:40
	 * @param String inputId
	 * @param String programId
	 * @param String pid
	 * @param PatchDecodeRequest decode
	 * @return PutInputResponse
	 */
	public PutInputResponse modifyProgramDecodeAddMsgId(
			String inputId, 
			String programId, 
			String pid, 
			PatchDecodeRequest decode) throws Exception{
		
		String msg_id = UUID.randomUUID().toString().replaceAll("-", "");
		decode.setMsg_id(msg_id);
		
		return modifyProgramDecode(inputId, programId, pid, decode);
		
	}
	
	/**
	 * 修改指定节目解码<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月11日 下午1:59:40
	 * @param String inputId
	 * @param String programId
	 * @param String pid
	 * @param PatchDecodeRequest decode
	 * @return PutInputResponse
	 */
	private PutInputResponse modifyProgramDecode(
			String inputId, 
			String programId, 
			String pid, 
			PatchDecodeRequest decode) throws Exception{
		
		String url = new StringBufferWrapper().append(UrlConstant.URL_PREFIX)
										      .append(capacityProps.getIp())
										      .append(":")
										      .append(capacityProps.getPort())
										      .append(UrlConstant.URL_INPUT)
										      .append("/")
										      .append(inputId)
										      .append(UrlConstant.URL_INPUT_PROGRAM)
										      .append("/")
										      .append(programId)
										      .append(UrlConstant.URL_INPUT_PROGRAM_ElEMENTS)
										      .append("/")
										      .append(pid)
										      .append(UrlConstant.URL_INPUT_PROGRAM_DECODE)
										      .toString();

		JSONObject request = JSONObject.parseObject(JSON.toJSONString(decode));
		
		JSONObject res = HttpUtil.httpPatch(url, request);
		
		if(res == null) throw new HttpTimeoutException(capacityProps.getIp());
		
		PutInputResponse response = JSONObject.parseObject(res.toJSONString(), PutInputResponse.class);
		
		return response;
		
	}
	
	/**
	 * 获取指定输入源信息<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月11日 下午2:17:19
	 * @param String inputId
	 * @return AnalysisResponse 
	 */
	public AnalysisResponse getAnalysis(String inputId, String ip) throws Exception{
		
		String msg_id = UUID.randomUUID().toString().replaceAll("-", "");
		
		return getAnalysis(inputId, msg_id, ip);
		
	}
	
	/**
	 * 获取指定输入源信息<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月5日 下午2:26:14
	 * @param String msg_id 消息id
	 * @return AnalysisResponse 刷源返回
	 */
	private AnalysisResponse getAnalysis(String inputId, String msg_id, String ip) throws Exception{
		
		String url = new StringBufferWrapper().append(UrlConstant.URL_PREFIX)
										      .append(ip)
										      .append(":")
										      .append(capacityProps.getPort())
										      .append(UrlConstant.URL_INPUT)
										      .append("/")
										      .append(inputId)
										      .append(UrlConstant.URL_INPUT_ANALYSIS)
										      .append("?msg_id=")
										      .append(msg_id)
										      .toString();

		JSONObject res = HttpUtil.httpGet(url);
		
		if(res == null) throw new HttpTimeoutException(ip);
		
		AnalysisResponse response = JSONObject.parseObject(res.toJSONString(), AnalysisResponse.class);
		
		return response;
		
	}
	
	/**
	 * 获取所有任务<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月11日 下午2:18:39
	 * @return GetTaskResponse
	 */
	public GetTaskResponse getTasks() throws Exception{
		
		String msg_id = UUID.randomUUID().toString().replaceAll("-", "");
		
		return getTasks(msg_id);
	}
	
	/**
	 * 获取所有任务<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月5日 下午2:54:39
	 * @param String msg_id 消息id
	 * @return TaskResponse 任务返回
	 */
	private GetTaskResponse getTasks(String msg_id) throws Exception{
		
		String url = new StringBufferWrapper().append(UrlConstant.URL_PREFIX)
										      .append(capacityProps.getIp())
										      .append(":")
										      .append(capacityProps.getPort())
										      .append(UrlConstant.URL_TASK)
										      .append("?msg_id=")
										      .append(msg_id)
										      .toString();
	
		JSONObject res = HttpUtil.httpGet(url);
		
		if(res == null) throw new HttpTimeoutException(capacityProps.getIp());
		
		GetTaskResponse response = JSONObject.parseObject(res.toJSONString(), GetTaskResponse.class);
		
		return response;
		
	}
	
	/**
	 * 创建任务<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月11日 下午2:21:59
	 * @param CreateTaskRequest task 不带msg_id的task
	 * @return CreateTaskResponse
	 */
	public CreateTaskResponse createTasksAddMsgId(CreateTaskRequest task) throws Exception{
		
		String msg_id = UUID.randomUUID().toString().replaceAll("-", "");
		task.setMsg_id(msg_id);
		
		System.out.println(JSONObject.toJSONString(task));
		
		return createTasks(task);
	}
	
	/**
	 * 创建任务<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月5日 下午3:38:08
	 * @param CreateTaskRequest task 创建任务参数
	 * @return CreateTaskResponse 创建任务返回
	 */
	private CreateTaskResponse createTasks(CreateTaskRequest task) throws Exception{
		
		String url = new StringBufferWrapper().append(UrlConstant.URL_PREFIX)
										      .append(capacityProps.getIp())
										      .append(":")
										      .append(capacityProps.getPort())
										      .append(UrlConstant.URL_TASK)
										      .toString();

		JSONObject request = JSONObject.parseObject(JSON.toJSONString(task, SerializerFeature.DisableCircularReferenceDetect));
		
		JSONObject res = HttpUtil.httpPost(url, request);
		
		if(res == null) throw new HttpTimeoutException(capacityProps.getIp());
		
		CreateTaskResponse response = JSONObject.parseObject(res.toJSONString(), CreateTaskResponse.class);
		
		return response;
		
	}
	
	/**
	 * 删除任务<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月11日 下午2:53:36
	 * @param DeleteTasksRequest task 不带msg_id的task
	 */
	public void deleteTasksAddMsgId(DeleteTasksRequest task, String capacityIp) throws Exception{
		
		String msg_id = UUID.randomUUID().toString().replaceAll("-", "");
		task.setMsg_id(msg_id);
		
		deleteTasks(task, capacityIp);
	}
	
	/**
	 * 删除任务<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月5日 下午3:56:56
	 * @param DeleteTasksRequest task 需要删除的任务
	 */
	private void deleteTasks(DeleteTasksRequest task, String capacityIp) throws Exception{
		
		String url = new StringBufferWrapper().append(UrlConstant.URL_PREFIX)
										      .append(capacityIp)
										      .append(":")
										      .append(capacityProps.getPort())
										      .append(UrlConstant.URL_TASK)
										      .toString();
		
		JSONObject request = JSONObject.parseObject(JSON.toJSONString(task));
		
		HttpUtil.httpDelete(url, request);
		
	}
	
	/**
	 * 添加任务编码<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月11日 下午3:25:31
	 * @param String taskId
	 * @param AddTaskEncodeRequest encode 不带msg_id的encode
	 * @return TaskEncodeResponse
	 */
	public TaskEncodeResponse addTaskEncodeAddMsgId(String taskId, AddTaskEncodeRequest encode) throws Exception{
		
		String msg_id = UUID.randomUUID().toString().replaceAll("-", "");
		encode.setMsg_id(msg_id);
		
		return addTaskEncode(taskId, encode);
	}
	
	/**
	 * 添加任务编码<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月5日 下午4:28:22
	 * @param AddTaskEncodeRequest encode 添加任务编码参数
	 * @return TaskEncodeResponse 添加任务编码返回
	 */
	private TaskEncodeResponse addTaskEncode(String taskId, AddTaskEncodeRequest encode) throws Exception{
		
		String url = new StringBufferWrapper().append(UrlConstant.URL_PREFIX)
										      .append(capacityProps.getIp())
										      .append(":")
										      .append(capacityProps.getPort())
										      .append(UrlConstant.URL_TASK)
										      .append("/")
										      .append(taskId)
										      .append(UrlConstant.URL_TASK_ENCODE)
										      .toString();

		JSONObject request = JSONObject.parseObject(JSON.toJSONString(encode, SerializerFeature.DisableCircularReferenceDetect));
		
		JSONObject res = HttpUtil.httpPost(url, request);
		
		if(res == null) throw new HttpTimeoutException(capacityProps.getIp());
		
		TaskEncodeResponse response = JSONObject.parseObject(res.toJSONString(), TaskEncodeResponse.class);
		
		return response;
		
	}
	
	/**
	 * 删除任务编码<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月11日 下午3:29:39
	 * @param String taskId
	 * @param DeleteTaskEncodeResponse encode 不带msg_id的encode
	 * @return TaskEncodeResponse
	 */
	public TaskEncodeResponse deleteTaskEncodeAddMsgId(String taskId, DeleteTaskEncodeResponse encode) throws Exception{
		
		String msg_id = UUID.randomUUID().toString().replaceAll("-", "");
		encode.setMsg_id(msg_id);
		
		return deleteTaskEncode(taskId, encode);
	}
	
	/**
	 * 删除任务编码<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月5日 下午4:41:13
	 * @param DeleteTaskEncodeResponse encode 删除任务编码请求参数
	 * @return TaskEncodeResponse 删除任务编码返回
	 */
	private TaskEncodeResponse deleteTaskEncode(String taskId, DeleteTaskEncodeResponse encode) throws Exception{
		
		String url = new StringBufferWrapper().append(UrlConstant.URL_PREFIX)
										      .append(capacityProps.getIp())
										      .append(":")
										      .append(capacityProps.getPort())
										      .append(UrlConstant.URL_TASK)
										      .append("/")
										      .append(taskId)
										      .append(UrlConstant.URL_TASK_ENCODE)
										      .toString();

		JSONObject request = JSONObject.parseObject(JSON.toJSONString(encode));
		
		JSONObject res = HttpUtil.httpDelete(url, request);
		
		if(res == null) throw new HttpTimeoutException(capacityProps.getIp());
		
		TaskEncodeResponse response = JSONObject.parseObject(res.toJSONString(), TaskEncodeResponse.class);
		
		return response;
		
	}
	
	/**
	 * 修改任务编码<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月11日 下午3:41:14
	 * @param String taskId 
	 * @param String encodeId
	 * @param PutTaskEncodeRequest encode 不带msg_id的encode
	 * @return TaskEncodeResultResponse
	 */
	public TaskEncodeResultResponse modifyTaskEncodeAddMsgId(String taskId, String encodeId, PutTaskEncodeRequest encode) throws Exception{
		
		String msg_id = UUID.randomUUID().toString().replaceAll("-", "");
		encode.setMsg_id(msg_id);
		
		return modifyTaskEncode(taskId, encodeId, encode);
	}
	
	/**
	 * 修改任务编码<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月5日 下午6:04:13
	 * @param String taskId 任务id
	 * @param String encodeId 编码id
	 * @param PutTaskEncodeRequest encode 修改编码参数
	 * @return TaskEncodeResultResponse 返回
	 */
	private TaskEncodeResultResponse modifyTaskEncode(String taskId, String encodeId, PutTaskEncodeRequest encode) throws Exception{
		
		String url = new StringBufferWrapper().append(UrlConstant.URL_PREFIX)
										      .append(capacityProps.getIp())
										      .append(":")
										      .append(capacityProps.getPort())
										      .append(UrlConstant.URL_TASK)
										      .append("/")
										      .append(taskId)
										      .append(UrlConstant.URL_TASK_ENCODE)
										      .append("/")
										      .append(encodeId)
										      .toString();
		
		JSONObject request = JSONObject.parseObject(JSON.toJSONString(encode));
		
		JSONObject res = HttpUtil.httpPut(url, request);
		
		if(res == null) throw new HttpTimeoutException(capacityProps.getIp());
		
		TaskEncodeResultResponse response = JSONObject.parseObject(res.toJSONString(), TaskEncodeResultResponse.class);
		
		return response;
		
	}
	
	/**
	 * 修改解码后处理<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月11日 下午3:48:50
	 * @param String taskId
	 * @param PutTaskDecodeProcessRequest decode
	 * @return TaskResponse 
	 */
	public TaskBaseResponse modifyTaskDecodeProcessAddMsgId(String taskId, PutTaskDecodeProcessRequest decode) throws Exception{
		
		String msg_id = UUID.randomUUID().toString().replaceAll("-", "");
		decode.setMsg_id(msg_id);
		
		return modifyTaskDecodeProcess(taskId, decode);
		
	}
	
	/**
	 * 修改解码后处理<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月6日 上午9:26:49
	 * @param String taskId 任务id
	 * @param PutTaskDecodeProcessRequest decode 修改解码后处理参数
	 * @return TaskResponse 返回
	 */
	private TaskBaseResponse modifyTaskDecodeProcess(String taskId, PutTaskDecodeProcessRequest decode) throws Exception{
		
		String url = new StringBufferWrapper().append(UrlConstant.URL_PREFIX)
										      .append(capacityProps.getIp())
										      .append(":")
										      .append(capacityProps.getPort())
										      .append(UrlConstant.URL_TASK)
										      .append("/")
										      .append(taskId)
										      .append(UrlConstant.URL_TASK_DECODE_PROCESS)
										      .toString();
		
		JSONObject request = JSONObject.parseObject(JSON.toJSONString(decode));
		
		JSONObject res = HttpUtil.httpPut(url, request);
		
		if(res == null) throw new HttpTimeoutException(capacityProps.getIp());
		
		TaskBaseResponse response = JSONObject.parseObject(res.toJSONString(), TaskBaseResponse.class);
		
		return response;
		
	}
	
	/**
	 * 修改任务源<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月11日 下午4:05:13
	 * @param String taskId
	 * @param PutTaskSourceRequest source 不带msg_id的source
	 * @return TaskBaseResponse
	 */
	public TaskBaseResponse modifyTaskSourceAddMsgId(String taskId, String capacityIp, PutTaskSourceRequest source) throws Exception{
		
		String msg_id = UUID.randomUUID().toString().replaceAll("-", "");
		source.setMsg_id(msg_id);
		
		return modifyTaskSource(taskId, capacityIp, source);
		
	}
	
	/**
	 * 修改任务源<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月6日 上午10:01:30
	 * @param String taskId 任务id
	 * @param PutTaskSourceRequest source 修改任务源参数
	 * @return TaskResponse 返回
	 */
	private TaskBaseResponse modifyTaskSource(String taskId, String capacityIp, PutTaskSourceRequest source) throws Exception{
		
		String url = new StringBufferWrapper().append(UrlConstant.URL_PREFIX)
										      .append(capacityIp)
										      .append(":")
										      .append(capacityProps.getPort())
										      .append(UrlConstant.URL_TASK)
										      .append("/")
										      .append(taskId)
										      .append(UrlConstant.URL_TASK_SOURCE)
										      .toString();

		JSONObject request = JSONObject.parseObject(JSON.toJSONString(source));
		
		JSONObject res = HttpUtil.httpPut(url, request);
		
		if(res == null) throw new HttpTimeoutException(capacityIp);

		TaskBaseResponse response = JSONObject.parseObject(res.toJSONString(), TaskBaseResponse.class);
		
		return response;
		
	}
	
	/**
	 * 源节目切换<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月11日 下午4:12:22
	 * @param String taskId
	 * @param PutRealIndexRequest realIndex 不带msg_id的realIndex
	 * @return TaskRealIndexResponse
	 */
	public TaskRealIndexResponse switchTaskSourceAddMsgId(String taskId, PutRealIndexRequest realIndex) throws Exception{
		
		String msg_id = UUID.randomUUID().toString().replaceAll("-", "");
		realIndex.setMsg_id(msg_id);
		
		return switchTaskSource(taskId, realIndex);
		
	}
	
	/**
	 * 源节目切换<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月6日 上午10:32:05
	 * @param String taskId 任务id
	 * @param PutRealIndexRequest realIndex 待切换节目索引
	 * @return TaskRealIndexResponse 返回
	 */
	private TaskRealIndexResponse switchTaskSource(String taskId, PutRealIndexRequest realIndex) throws Exception{
		
		String url = new StringBufferWrapper().append(UrlConstant.URL_PREFIX)
										      .append(capacityProps.getIp())
										      .append(":")
										      .append(capacityProps.getPort())
										      .append(UrlConstant.URL_TASK)
										      .append("/")
										      .append(taskId)
										      .append(UrlConstant.URL_TASK_SOURCE_INDEX)
										      .toString();

		JSONObject request = JSONObject.parseObject(JSON.toJSONString(realIndex));
		
		JSONObject res = HttpUtil.httpPut(url, request);
		
		if(res == null) throw new HttpTimeoutException(capacityProps.getIp());
		
		TaskRealIndexResponse response = JSONObject.parseObject(res.toJSONString(), TaskRealIndexResponse.class);
		
		return response;
		
	}
	
	/**
	 * 方法概述<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月11日 下午4:28:27
	 * @return GetOutputsResponse
	 */
	public GetOutputsResponse getOutputs() throws Exception{
		
		String msg_id = UUID.randomUUID().toString().replaceAll("-", "");

		return getOutputs(msg_id);
		
	}
	
	/**
	 * 获取所有输出返回<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月6日 上午11:10:12
	 * @param String msg_id 消息id
	 * @return GetOutputsResponse 返回
	 */
	private GetOutputsResponse getOutputs(String msg_id) throws Exception{
		
		String url = new StringBufferWrapper().append(UrlConstant.URL_PREFIX)
										      .append(capacityProps.getIp())
										      .append(":")
										      .append(capacityProps.getPort())
										      .append(UrlConstant.URL_OUTPUT)
										      .append("?msg_id=")
										      .append(msg_id)
										      .toString();

		JSONObject res = HttpUtil.httpGet(url);
		
		if(res == null) throw new HttpTimeoutException(capacityProps.getIp());
		
		GetOutputsResponse response = JSONObject.parseObject(res.toJSONString(), GetOutputsResponse.class);
		
		return response;
		
	}
	
	/**
	 * 创建输出<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月12日 上午8:53:18
	 * @param CreateOutputsRequest output 不带msg_id的 输出
	 * @return CreateOutputsResponse
	 */
	public CreateOutputsResponse createOutputsAddMsgId(CreateOutputsRequest output, String capacityIp) throws Exception{
		
		String msg_id = UUID.randomUUID().toString().replaceAll("-", "");
		output.setMsg_id(msg_id);
		
		Date date = new Date();
        String str = "yyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(str);
        System.out.println(sdf.format(date));
		System.out.println("create-output" + JSONObject.toJSONString(output));
		
		return createOutputs(output, capacityIp);
	}
	
	/**
	 * 创建输出<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月6日 上午11:34:36
	 * @param CreateOutputsRequest output 创建输出参数
	 * @return CreateOutputsResponse 返回
	 */
	private CreateOutputsResponse createOutputs(CreateOutputsRequest output, String capacityIp) throws Exception{
		
		String url = new StringBufferWrapper().append(UrlConstant.URL_PREFIX)
										      .append(capacityIp)
										      .append(":")
										      .append(capacityProps.getPort())
										      .append(UrlConstant.URL_OUTPUT)
										      .toString();
		
		JSONObject request = JSONObject.parseObject(JSON.toJSONString(output, SerializerFeature.DisableCircularReferenceDetect));

		JSONObject res = HttpUtil.httpPost(url, request);
		
		if(res == null) throw new HttpTimeoutException(capacityIp);
		
		CreateOutputsResponse response = JSONObject.parseObject(res.toJSONString(), CreateOutputsResponse.class);
		
		return response;
		
	}
	
	/**
	 * 删除输出<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月12日 上午8:55:13
	 * @param DeleteOutputsRequest output 不带msg_id的output
	 */
	public void deleteOutputsAddMsgId(DeleteOutputsRequest output, String capacityIp) throws Exception{
		
		String msg_id = UUID.randomUUID().toString().replaceAll("-", "");
		output.setMsg_id(msg_id);
		
		Date date = new Date();
        String str = "yyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(str);
        System.out.println(sdf.format(date));
		System.out.println("delete-output" + JSONObject.toJSONString(output));
		
		deleteOutputs(output, capacityIp);
		
	}
	
	/**
	 * 删除输出<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月6日 上午11:50:09
	 * @param DeleteOutputsRequest output 输出
	 */
	private void deleteOutputs(DeleteOutputsRequest output, String capacityIp) throws Exception{
		
		String url = new StringBufferWrapper().append(UrlConstant.URL_PREFIX)
										      .append(capacityIp)
										      .append(":")
										      .append(capacityProps.getPort())
										      .append(UrlConstant.URL_OUTPUT)
										      .toString();

		JSONObject request = JSONObject.parseObject(JSON.toJSONString(output));
		
		HttpUtil.httpDelete(url, request);
		
	}
	
	/**
	 * 获取指定输出<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月12日 上午8:57:31
	 * @param String id 
	 * @return GetOutputResponse
	 */
	public GetOutputResponse getOutputById(String id) throws Exception{
		
		String msg_id = UUID.randomUUID().toString().replaceAll("-", "");
		
		return getOutputById(id, msg_id);
		
	}
	
	/**
	 * 获取指定输出<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月6日 下午1:15:00
	 * @param String id 输出id
	 * @param String msg_id 消息id
	 * @return GetOutputResponse 返回
	 */
	private GetOutputResponse getOutputById(String id, String msg_id) throws Exception{
		
		String url = new StringBufferWrapper().append(UrlConstant.URL_PREFIX)
										      .append(capacityProps.getIp())
										      .append(":")
										      .append(capacityProps.getPort())
										      .append(UrlConstant.URL_OUTPUT)
										      .append("/")
										      .append(id)
										      .append("?msg=")
										      .append(msg_id)
										      .toString();
		
		JSONObject res = HttpUtil.httpGet(url);
		
		if(res == null) throw new HttpTimeoutException(capacityProps.getIp());
		
		GetOutputResponse response = JSONObject.parseObject(res.toJSONString(), GetOutputResponse.class);
		
		return response;
		
	}
	
	/**
	 * 修改指定输出<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月12日 上午9:00:07
	 * @param String id
	 * @param PutOutputRequest output 不带msg_id的output
	 * @return ResultCodeResponse
	 */
	public ResultCodeResponse modifyOutputByIdAddMsgId(String id, PutOutputRequest output) throws Exception{
		
		String msg_id = UUID.randomUUID().toString().replaceAll("-", "");
		output.setMsg_id(msg_id);
		
		return modifyOutputById(id, output);
		
	}
	
	/**
	 * 修改指定输出<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月6日 下午1:51:31
	 * @param String id 输出id
	 * @param PutOutputRequest output 输出参数
	 * @return ResultCodeResponse 返回
	 */
	private ResultCodeResponse modifyOutputById(String id, PutOutputRequest output) throws Exception{
		
		String url = new StringBufferWrapper().append(UrlConstant.URL_PREFIX)
											  .append(capacityProps.getIp())
											  .append(":")
											  .append(capacityProps.getPort())
											  .append(UrlConstant.URL_OUTPUT)
											  .append("/")
											  .append(id)
											  .append(UrlConstant.URL_INPUT_PARAM)
											  .toString();
		
		JSONObject request = JSONObject.parseObject(JSON.toJSONString(output));
		
		JSONObject res = HttpUtil.httpPut(url, request);
		
		if(res == null) throw new HttpTimeoutException(capacityProps.getIp());
		
		ResultCodeResponse response = JSONObject.parseObject(res.toJSONString(), ResultCodeResponse.class);
		
		return response;
		
	}
	
	/**
	 * 获取所有<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月12日 上午9:02:11
	 * @return GetEntiretiesResponse
	 */
	public GetEntiretiesResponse getEntireties(String capacityIp) throws Exception{
		
		String msg_id = UUID.randomUUID().toString().replaceAll("-", "");
		
		return getEntireties(msg_id, capacityIp);
		
	}
	
	/**
	 * 获取所有<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月6日 下午2:10:05
	 * @param String msg_id 消息id
	 * @return GetEntiretiesResponse 返回
	 */
	private GetEntiretiesResponse getEntireties(String msg_id, String capacityIp) throws Exception{
		
		String url = new StringBufferWrapper().append(UrlConstant.URL_PREFIX)
											  .append(capacityIp)
											  .append(":")
											  .append(capacityProps.getPort())
											  .append(UrlConstant.URL_ENTIRETY)
											  .append("?msg_id=")
											  .append(msg_id)
											  .toString();
		
		JSONObject res = HttpUtil.httpGet(url);
		
		if(res == null) throw new HttpTimeoutException(capacityIp);
		
		GetEntiretiesResponse response = JSONObject.parseObject(res.toJSONString(), GetEntiretiesResponse.class);
		
		return response;
	}
	
	/**
	 * 清空所有<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月12日 上午9:03:40
	 */
	public void removeAll() throws Exception{
		
		String msg_id = UUID.randomUUID().toString().replaceAll("-", "");
		removeAll(msg_id);
		
	}
	
	/**
	 * 清空所有<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月6日 下午2:19:02
	 * @param String msg_id 消息id
	 */
	private void removeAll(String msg_id) throws Exception{
		
		String url = new StringBufferWrapper().append(UrlConstant.URL_PREFIX)
											  .append(capacityProps.getIp())
											  .append(":")
											  .append(capacityProps.getPort())
											  .append(UrlConstant.URL_ENTIRETY)
											  .toString();
		
		JSONObject json = new JSONObject();
		json.put("msg_id", msg_id);
		
		HttpUtil.httpDelete(url, json);
		
	}
	
	/**
	 * 获取授权<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月5日 下午2:39:50
	 */
	public JSONObject getAuthorizationAddMsgId(String ip, Long port) throws Exception{
		
		String msg_id = UUID.randomUUID().toString().replaceAll("-", "");
		
		return getAuthorization(msg_id, ip, port);
	}
	
	/**
	 * 获取授权<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月5日 下午2:27:54
	 */
	private JSONObject getAuthorization(String msg_id, String ip, Long port) throws Exception{
		
		String url = new StringBufferWrapper().append(UrlConstant.URL_PREFIX)
											  .append(ip)
											  .append(":")
											  .append(port)
											  .append(UrlConstant.URL_AUTHORIZATION)
											  .append("?msg_id=")
											  .append(msg_id)
											  .toString();
		
		return HttpUtil.httpGet(url);
	}
	
	/**
	 * 更改节目备份源<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月25日 上午11:48:39
	 * @param String inputId 输入id(back_up)
	 * @param String index 索引
	 * @param String ip 能力ip
	 * @param Long port 能力端口
	 * @return ResultCodeResponse
	 */
	public ResultCodeResponse changeBackUp(String inputId, String index, String ip, Long port) throws Exception{
		
		String msg_id = UUID.randomUUID().toString().replaceAll("-", "");
		
		String url = new StringBufferWrapper().append(UrlConstant.URL_PREFIX)
											  .append(ip)
											  .append(":")
											  .append(port)
											  .append(UrlConstant.URL_INPUT)
											  .append("/")
											  .append(inputId)
											  .append(UrlConstant.URL_TASK_SOURCE_INDEX)
											  .toString();
		
		JSONObject post = new JSONObject();
		post.put("msg_id", msg_id);
		post.put("select_index", index);
		
		JSONObject res = HttpUtil.httpPost(url, post);
		
		if(res == null) throw new HttpTimeoutException(ip);
		
		ResultCodeResponse response = JSONObject.parseObject(res.toJSONString(), ResultCodeResponse.class);
		
		return response;
	}
	
	/**
	 * 设置告警地址请求<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年1月13日 下午1:46:57
	 * @param String ip 能力ip
	 * @param Long port 能力port
	 * @param String alarmUrl 告警url
	 * @return ResultCodeResponse
	 */
	public ResultCodeResponse putAlarmUrl(String ip, Long port, String alarmUrl) throws Exception{
		
		String msg_id = UUID.randomUUID().toString().replaceAll("-", "");
		
		String url = new StringBufferWrapper().append(UrlConstant.URL_PREFIX)
											  .append(ip)
											  .append(":")
											  .append(port)
											  .append(UrlConstant.URL_ALARM)
											  .toString();
		
		JSONObject put = new JSONObject();
		put.put("msg_id", msg_id);
		put.put("alarm_url", alarmUrl);
		put.put("max_count", 100);
		
		JSONObject res = HttpUtil.httpPut(url, put);
		
		if(res == null) throw new HttpTimeoutException(ip);
		
		ResultCodeResponse response = JSONObject.parseObject(res.toJSONString(), ResultCodeResponse.class);
		
		return response;
											  
	}
	
	/**
	 * 设置心跳地址请求<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年1月14日 上午11:51:24
	 * @param String ip 能力ip
	 * @param Long port 能力端口
	 * @param String heartbeatUrl 心跳地址
	 * @return ResultCodeResponse
	 */
	public ResultCodeResponse putHeartbeatUrl(String ip, Long port, String heartbeatUrl) throws Exception{
		
		String msg_id = UUID.randomUUID().toString().replaceAll("-", "");
		
		String url = new StringBufferWrapper().append(UrlConstant.URL_PREFIX)
											  .append(ip)
											  .append(":")
											  .append(port)
											  .append(UrlConstant.URL_HEARTBEAT)
											  .toString();
		
		JSONObject put = new JSONObject();
		put.put("msg_id", msg_id);
		put.put("heartbeat_url", heartbeatUrl);
		put.put("span_second", 10);
		
		JSONObject res = HttpUtil.httpPut(url, put);
		
		if(res == null) throw new HttpTimeoutException(ip);
		
		ResultCodeResponse response = JSONObject.parseObject(res.toJSONString(), ResultCodeResponse.class);
		
		return response;
											  
	}
	
	/**
	 * 追加排期请求<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月28日 下午1:27:17
	 * @param String ip 转换模块ip
	 * @param Long port 转换模块端口
	 * @param String inputId schedule输入id
	 * @param PutScheduleRequest scheduleRequest 追加排期参数
	 * @return ResultCodeResponse
	 */
	public ResultCodeResponse putSchedule(String ip, Long port, String inputId, PutScheduleRequest scheduleRequest) throws Exception{
		
		String msg_id = UUID.randomUUID().toString().replaceAll("-", "");
		scheduleRequest.setMsg_id(msg_id);
		
		String url = new StringBufferWrapper().append(UrlConstant.URL_PREFIX)
											  .append(ip)
											  .append(":")
											  .append(port)
											  .append(UrlConstant.URL_INPUT)
											  .append("/")
											  .append(inputId)
											  .append(UrlConstant.URL_INPUT_SCHEDULE)
											  .toString();

		JSONObject request = JSONObject.parseObject(JSON.toJSONString(scheduleRequest));
		
		JSONObject res = HttpUtil.httpPut(url, request);
		
		if(res == null) throw new HttpTimeoutException(ip);
		
		ResultCodeResponse response = JSONObject.parseObject(res.toJSONString(), ResultCodeResponse.class);
		
		return response;
		
	}
	
	/**
	 * 设置告警列表<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年5月29日 下午3:27:19
	 * @param String ip 转换模块ip
	 * @param Long port 转换模块端口
	 * @param String alarmList 告警列表信息
	 * @return ResultCodeResponse
	 */
	public ResultCodeResponse putAlarmList(String ip, Long port, String alarmList) throws Exception{
		
		String msg_id = UUID.randomUUID().toString().replaceAll("-", "");
		
		JSONObject request = JSONObject.parseObject(alarmList);
		request.put("msg_id", msg_id);
		
		String url = new StringBufferWrapper().append(UrlConstant.URL_PREFIX)
											  .append(ip)
											  .append(":")
											  .append(port)
											  .append(UrlConstant.URL_ALARMLIST)
											  .toString();
		
		JSONObject res = HttpUtil.httpPut(url, request);
		
		if(res == null) throw new HttpTimeoutException(ip);
		
		ResultCodeResponse response = JSONObject.parseObject(res.toJSONString(), ResultCodeResponse.class);
		
		return response;
	}
	
}
