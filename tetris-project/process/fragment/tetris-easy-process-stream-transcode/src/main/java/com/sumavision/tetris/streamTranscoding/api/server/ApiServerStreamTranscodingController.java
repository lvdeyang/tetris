package com.sumavision.tetris.streamTranscoding.api.server;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.easy.process.core.ProcessService;
import com.sumavision.tetris.mims.app.media.avideo.MediaAVideoQuery;
import com.sumavision.tetris.mims.app.media.avideo.MediaAVideoVO;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.streamTranscoding.StreamTranscodingAdapter;
import com.sumavision.tetris.streamTranscoding.addOutput.AddOutputService;
import com.sumavision.tetris.streamTranscoding.addTask.StreamTranscodingAddTaskService;
import com.sumavision.tetris.streamTranscoding.deleteOutput.DeleteOutputService;
import com.sumavision.tetris.streamTranscoding.deleteTask.DeleteTaskService;
import com.sumavision.tetris.streamTranscodingProcessVO.StreamTranscodingProcessVO;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;

@Controller
@RequestMapping(value = "/api/server/stream/transcoding")
public class ApiServerStreamTranscodingController {
	
	@Autowired
	private UserQuery userQuery;
	
	@Autowired
	private StreamTranscodingAdapter streamTranscodingAdapter;
	
	@Autowired
	private ApiServerStreamTranscodingService ApiServerStreamTranscodingService;
	
	@Autowired
	private StreamTranscodingAddTaskService streamTranscodingAddTaskService;
	
	@Autowired
	private DeleteTaskService deleteTaskService;
	
	@Autowired
	private AddOutputService addOutputService;
	
	@Autowired
	private DeleteOutputService deleteOutputService;
	
	@Autowired
	private MediaAVideoQuery mediaAVideoQuery;
	
	@Autowired
	private ProcessService processService;
	
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add/task/file")
	public Object addTaskFile(Long assetId, boolean record, Integer playTime, String mediaType, String recordCallback, Integer progNum, String task, String stopCallback, HttpServletRequest request) throws Exception{
		UserVO user = userQuery.current();
		
		MediaAVideoVO media = mediaAVideoQuery.loadByIdAndType(assetId, mediaType);
		if (media == null) return null;
		
		StreamTranscodingProcessVO processVO = ApiServerStreamTranscodingService.fileParamFormat(media, record, playTime, stopCallback, mediaType, recordCallback, progNum, task);
		JSONObject variables = new JSONObject();
		variables.put("_pa17_file_fileToStreamInfo", JSON.toJSONString(processVO.getFileToStreamVO()));
		variables.put("_pa17_file_streamTranscodingInfo", JSON.toJSONString(processVO.getStreamTranscodingVO()));
		variables.put("_pa17_file_recordInfo", JSON.toJSONString(processVO.getRecordVO()));
		
		String processInstanceId = processService.startByKey("_file_stream_transcoding_by_server", variables.toJSONString(), null, null);
		
		return new HashMapWrapper<String, Object>().put("id", processInstanceId)
				.getMap();
	}
	
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add/task")
	public Object addTask(Long assetId, String assetPath, boolean record, Integer bePCM, String mediaType, String recordCallback, Integer progNum, String task,HttpServletRequest request) throws Exception{
		UserVO user = userQuery.current();
		
		StreamTranscodingProcessVO processVO = ApiServerStreamTranscodingService.streamParamFormat(assetId, assetPath, record, bePCM, mediaType, recordCallback, progNum, task);
		JSONObject variables = new JSONObject();
		variables.put("_pa17_file_fileToStreamInfo", JSON.toJSONString(processVO.getFileToStreamVO()));
		variables.put("_pa17_file_streamTranscodingInfo", JSON.toJSONString(processVO.getStreamTranscodingVO()));
		variables.put("_pa17_file_recordInfo", JSON.toJSONString(processVO.getRecordVO()));
		
		String processInstanceId = processService.startByKey("_file_stream_transcoding_by_server", variables.toJSONString(), null, null);
		
		return new HashMapWrapper<String, Object>().put("id", processInstanceId)
				.getMap();
	}
	
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/delete/task")
	public Object deleteTask(Long id, HttpServletRequest request) throws Exception{
		UserVO user = userQuery.current();
		
		JSONObject variables = new JSONObject();
		variables.put("_pa19_messageId", id);
		variables.put("_pa21_messageId", id);
		variables.put("_pa26_messageId", id);
		
		String processInstanceId = processService.startByKey("_delete_file_stream_transcoding_by_server", variables.toJSONString(), null, null);
		
		return new HashMapWrapper<String, Object>().put("id", processInstanceId)
				.getMap();
	}
	
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add/output")
	public Object addOutput(Long id, String outputParam, HttpServletRequest request) throws Exception{
		UserVO user = userQuery.current();
		
		List<OutParamVO> outputParams = JSONObject.parseArray(outputParam, OutParamVO.class);
		
		addOutputService.addOutput(user, id, outputParams);
		
		return null;
	}
	
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "delete/output")
	public Object deleteOutput(Long id, String outputParam, HttpServletRequest request) throws Exception {
		UserVO user = userQuery.current();
		
		OutParamVO outParamVO = JSON.parseObject(outputParam, OutParamVO.class);
		
		deleteOutputService.delete(user, outParamVO);
		
		return null;
	}
}
