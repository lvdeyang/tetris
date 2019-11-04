package com.sumavision.tetris.oldCMS.api.server;

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
import com.sumavision.tetris.oldCMS.addOutput.StreamAddOutputService;
import com.sumavision.tetris.oldCMS.deleteOutput.StreamDeleteOutputService;
import com.sumavision.tetris.oldCMS.deleteTask.StreamDeleteTaskService;
import com.sumavision.tetris.streamTranscoding.api.server.OutParamVO;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;

@Controller
@RequestMapping(value = "/api/server/stream")
public class ApiServerStreamController {
	@Autowired
	private UserQuery userQuery;
	
	@Autowired
	private ApiServerStreamService apiServerStreamService;
	
	@Autowired
	private StreamDeleteTaskService deleteTaskService;
	
	@Autowired
	private StreamAddOutputService addOutputService;
	
	@Autowired
	private StreamDeleteOutputService deleteOutputService;
	
	@Autowired
	private MediaAVideoQuery mediaAVideoQuery;
	
	@Autowired
	private ProcessService processService;
	
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add/task/file")
	public Object addTaskFile(
			Long assetId,
			Integer playTime,
			String mediaType,
			Integer audioType,
			String audioParam,
			Integer esType,
			Boolean record,
			String recordCallback,
			Integer streamPubType,
			String sipParam,
			String videoParam,
			String outputParam,
			String stopCallBack,
			HttpServletRequest request) throws Exception{
		UserVO user = userQuery.current();
		
		MediaAVideoVO media = mediaAVideoQuery.loadByIdAndType(assetId, mediaType);
		if (media == null) return null;
		
		StreamProcessVO processVO = apiServerStreamService.fileParamFormat(
				media.getPreviewUrl(),
				media.getDuration(),
				playTime,
				mediaType,
				audioType,
				audioParam,
				esType,
				record,
				recordCallback,
				streamPubType,
				sipParam,
				videoParam,
				outputParam,
				stopCallBack);
		JSONObject variables = new JSONObject();
		variables.put("_pa50_file_fileToStreamInfo", JSON.toJSONString(processVO.getFileToStreamVO()));
		variables.put("_pa50_file_streamTranscodingInfo", JSON.toJSONString(processVO.getStreamVO()));
		variables.put("_pa50_file_recordInfo", JSON.toJSONString(processVO.getRecordVO()));
		
		String processInstanceId = processService.startByKey("_file_stream_transcoding_by_server", variables.toJSONString(), null, null);
		
		return new HashMapWrapper<String, Object>().put("processId", processInstanceId)
				.getMap();
	}
	
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add/task")
	public Object addTask(
			String assetPath,
			String mediaType,
			Integer audioType,
			String audioParam,
			Integer bePCM,
			Integer esType,
			Boolean record,
			String recordCallback,
			Integer streamPubType,
			String inputParam,
			String sipParam,
			String videoParam,
			Integer progNum,
			String outputParam,
			String dataParam,
			HttpServletRequest request) throws Exception{
		UserVO user = userQuery.current();
		
		StreamProcessVO processVO = apiServerStreamService.streamParamFormat(
				assetPath,
				mediaType,
				audioType,
				audioParam,
				bePCM,
				esType,
				record,
				recordCallback,
				streamPubType,
				inputParam,
				sipParam,
				videoParam,
				progNum,
				outputParam,
				dataParam);
		JSONObject variables = new JSONObject();
		variables.put("_pa50_file_fileToStreamInfo", JSON.toJSONString(processVO.getFileToStreamVO()));
		variables.put("_pa50_file_streamTranscodingInfo", JSON.toJSONString(processVO.getStreamVO()));
		variables.put("_pa50_file_recordInfo", JSON.toJSONString(processVO.getRecordVO()));
		
		String processInstanceId = processService.startByKey("_file_stream_transcoding_by_server", variables.toJSONString(), null, null);
		
		return new HashMapWrapper<String, Object>().put("processId", processInstanceId)
				.getMap();
	}
	
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/delete/task")
	public Object deleteTask(Long id, HttpServletRequest request) throws Exception{
		UserVO user = userQuery.current();
		
		deleteTaskService.delete(user, id);
		
		return null;
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
		
		deleteOutputService.delete(user, id, outParamVO);
		
		return null;
	}
}
