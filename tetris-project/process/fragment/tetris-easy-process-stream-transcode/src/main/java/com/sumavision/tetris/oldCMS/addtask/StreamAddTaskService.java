package com.sumavision.tetris.oldCMS.addtask;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.oldCMS.OldCMSRequestType;
import com.sumavision.tetris.oldCMS.StreamAdapter;
import com.sumavision.tetris.oldCMS.api.server.StreamVO;
import com.sumavision.tetris.stream.transcoding.task.StreamTranscodingTaskService;
import com.sumavision.tetris.stream.transcoding.task.StreamTranscodingTaskVO;
import com.sumavision.tetris.streamTranscoding.exception.HttpRequestErrorException;
import com.sumavision.tetris.streamTranscodingProcessVO.RecordVO;
import com.sumavision.tetris.user.UserVO;

@Service
@Transactional(rollbackFor = Exception.class)
public class StreamAddTaskService {
	@Autowired
	private StreamAdapter streamAdapter;
	
	@Autowired
	private StreamTranscodingTaskService streamTranscodingTaskService;
	
	public Long addStreamTask(UserVO user, StreamVO streamVO, RecordVO recordVO) throws Exception {
		Integer esType = streamVO.getBePCM();
		Integer progNum = streamVO.getProgNum() == null ? null : Integer.parseInt(streamVO.getProgNum().toString());
		
		boolean record = recordVO == null ? false : recordVO.isRecord();
		String recordCallback = recordVO == null ? null : recordVO.getRecordCallback();
		
		JSONObject requestJSON = new JSONObject();
		requestJSON.put("url", streamVO.getAssetPath());
		requestJSON.put("mediaType", streamVO.getMediaType());
		requestJSON.put("audioType", streamVO.getAudioType());
		requestJSON.put("audioParam", streamVO.getAudioParam());
		requestJSON.put("bePCM", esType);
		requestJSON.put("esType", streamVO.getEsType());
		requestJSON.put("record", record);
		requestJSON.put("recordCallback", streamVO.getRecordCallback());
		requestJSON.put("streamPubType", streamVO.getStreamPubType());
		requestJSON.put("inputParam", streamVO.getInputParam());
		requestJSON.put("sipParam", streamVO.getSipParam());
		requestJSON.put("videoParam", streamVO.getVideoParam());
		requestJSON.put("progNum", progNum);
		requestJSON.put("outputParam", streamVO.getOutputParam());
		requestJSON.put("dataParam", streamVO.getDataParam());
		requestJSON.put("localIp", streamVO.getLocalIp());
		
		JSONObject response = streamAdapter.addTask(requestJSON);
		
		if (response.get("errMsg").equals("success")) {
			Long uniqId = response.getLong("uniqId");
			StreamTranscodingTaskVO taskPO = streamTranscodingTaskService.addTaskForAddInput(user, record, recordCallback, progNum, esType, null, null, uniqId);
			return uniqId;
		} else {
			throw new HttpRequestErrorException(OldCMSRequestType.ADD_TASK.getAction());
		}
	}
}
