package com.sumavision.tetris.stream.transcoding.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sumavision.tetris.commons.util.httprequest.HttpRequestUtil;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.streamTranscoding.ToolRequestType;
import com.sumavision.tetris.user.UserVO;

@Service
@Transactional(rollbackFor = Exception.class)
public class StreamTranscodingTaskService {
	@Autowired
	private StreamTranscodingTaskDAO streamTranscodingTaskDAO;
	
	@Autowired
	private StreamTranscodingTaskQuery streamTranscodingTaskQuery;
	
	public StreamTranscodingTaskVO addTaskForAddInput(UserVO user, boolean record, String recordCallback, Integer progNum, Integer esType, String vCodec, String aCodec, Long uniqId) throws Exception{
		StreamTranscodingTaskPO taskPO = new StreamTranscodingTaskPO();
		
		taskPO.setRecord(record);
		taskPO.setRecordCallback(recordCallback);
		taskPO.setProgNum(progNum);
		taskPO.setEsType(esType);
		taskPO.setInputId(streamTranscodingTaskQuery.getNextInputId());
		taskPO.setAction(ToolRequestType.ADD_TASK.getAction());
		taskPO.setUserId(user.getId());
		taskPO.setvCodec(vCodec);
		taskPO.setaCodec(aCodec);
		taskPO.setUniqId(uniqId);
		
		streamTranscodingTaskDAO.save(taskPO);
		
		return new StreamTranscodingTaskVO().set(taskPO);
	}
	
	public StreamTranscodingTaskVO addTaskForDeleteInput(UserVO user, Long inputId) throws Exception{
		StreamTranscodingTaskPO taskPO = new StreamTranscodingTaskPO();
		
		taskPO.setAction(ToolRequestType.DELETE_TASK.getAction());
		taskPO.setInputId(inputId);
		taskPO.setUserId(user.getId());
		
		streamTranscodingTaskDAO.save(taskPO);
		
		return new StreamTranscodingTaskVO().set(taskPO);
	}
	
	public StreamTranscodingTaskVO addTaskForAddOutput(UserVO user, Long inputId, Integer progNum) throws Exception{
		StreamTranscodingTaskPO taskPO = new StreamTranscodingTaskPO();
		
		taskPO.setAction(ToolRequestType.ADD_OUTPUT.getAction());
		taskPO.setInputId(inputId);
		taskPO.setProgNum(progNum);
		taskPO.setUserId(user.getId());
		
		streamTranscodingTaskDAO.save(taskPO);
		
		return new StreamTranscodingTaskVO().set(taskPO);
	}
	
	public StreamTranscodingTaskVO addTaskForDeleteOutput(UserVO user, Long inputId, Integer progNum) throws Exception{
		StreamTranscodingTaskPO taskPO = new StreamTranscodingTaskPO();
		
		taskPO.setAction(ToolRequestType.DELETE_OUTPUT.getAction());
		taskPO.setInputId(inputId);
		taskPO.setProgNum(progNum);
		taskPO.setUserId(user.getId());
		
		streamTranscodingTaskDAO.save(taskPO);
		
		return new StreamTranscodingTaskVO().set(taskPO);
	}
	
	public void sendStopCallback(Long messageId, Long assetId) throws Exception{
		StreamTranscodingTaskPO taskPO = streamTranscodingTaskDAO.findOne(messageId);
		
		if (taskPO == null) return;
		String recordCallbackUrl = taskPO.getRecordCallback();
		if (recordCallbackUrl == null || recordCallbackUrl.isEmpty()) return;
		
		String url = new StringBufferWrapper().append("http://")
				.append("ip")
				.append(":")
				.append("port")
				.append(recordCallbackUrl)
				.append("?uuiqId=")
				.append(messageId)
				.append("&assetId=")
				.append(assetId)
				.toString();
		
		HttpRequestUtil.httpGet(url);
	}
}
