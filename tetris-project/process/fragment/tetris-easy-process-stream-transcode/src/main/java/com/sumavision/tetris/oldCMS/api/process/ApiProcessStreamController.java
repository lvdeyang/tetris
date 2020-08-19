package com.sumavision.tetris.oldCMS.api.process;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.oldCMS.addtask.StreamAddTaskService;
import com.sumavision.tetris.oldCMS.api.server.StreamVO;
import com.sumavision.tetris.streamTranscodingProcessVO.RecordVO;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;

@Controller
@RequestMapping(value = "/api/process/stream")
public class ApiProcessStreamController {
	@Autowired
	private UserQuery userQuery;
	
	@Autowired
	private StreamAddTaskService streamAddTaskService;

	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add/task")
	public Object addTask(String assetPath, String transcode_streamTranscodingInfo, String transcode_recordInfo, HttpServletRequest request) throws Exception{
		UserVO user = userQuery.current();
		
		StreamVO info = JSON.parseObject(transcode_streamTranscodingInfo, StreamVO.class);
		
		RecordVO recordInfo = null;
		if (transcode_recordInfo != null && !transcode_recordInfo.isEmpty()) {
			recordInfo = JSON.parseObject(transcode_recordInfo, RecordVO.class);
		}
		
		if (assetPath != null && !assetPath.isEmpty()) {
			info.setAssetPath(assetPath);
		}
		
		Long messageId = null;
		if (info.getIsTranscoding()) {
			messageId = streamAddTaskService.addStreamTask(user, info, recordInfo);
		}
		
		return new HashMapWrapper<String, Object>().put("record_recordInfo", transcode_recordInfo).put("record_messageId", messageId).getMap();
	}
}
