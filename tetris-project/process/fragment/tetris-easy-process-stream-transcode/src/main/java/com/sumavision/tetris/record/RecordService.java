package com.sumavision.tetris.record;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.commons.util.httprequest.HttpRequestUtil;
import com.sumavision.tetris.stream.transcoding.task.StreamTranscodingTaskService;
import com.sumavision.tetris.streamTranscoding.exception.HttpRequestErrorException;

@Service
@Transactional(rollbackFor = Exception.class)
public class RecordService {
	
	@Autowired
	private StreamTranscodingTaskRecordPermissionDAO streamTranscodingTaskRecordPermissionDAO;
	
	@Autowired
	private StreamTranscodingTaskService streamTranscodingTaskService;
	
	public void add(Long messageId, String url) throws Exception{
		StreamTranscodingTaskRecordPermissionPO permissionPO = new StreamTranscodingTaskRecordPermissionPO();
		permissionPO.setUpdateTime(new Date());
		permissionPO.setMessageId(messageId);
		permissionPO.setUrl(url);
		streamTranscodingTaskRecordPermissionDAO.save(permissionPO);
		
		
		JSONObject requestJSON = new JSONObject();
		requestJSON.put("uniqId", permissionPO.getId());
		requestJSON.put("inputUrl", url);
		
		JSONObject response = HttpRequestUtil.httpPost(RecordRequestType.ADD_RECORD.getUrl(), requestJSON);
		
		if (response.getString("errMsg") != null) {
			throw new HttpRequestErrorException(RecordRequestType.ADD_RECORD.getAction());
		}
	}
	
	public void delete(Long messageId, Long recordId) throws Exception{
		StreamTranscodingTaskRecordPermissionPO permissionPO;
		if (recordId != null) {
			permissionPO = streamTranscodingTaskRecordPermissionDAO.findOne(recordId);
		} else if (messageId != null) {
			permissionPO = streamTranscodingTaskRecordPermissionDAO.findByMessageId(messageId);
		} else {
			return;
		}
		
		if (permissionPO == null) return;
		JSONObject requestJSON = new JSONObject();
		requestJSON.put("uniqId", permissionPO.getId());
		
		JSONObject response = HttpRequestUtil.httpPost(RecordRequestType.DELETE_RECORD.getUrl(), requestJSON);
		
		String errMsg = response.getString("errMsg");
		if (errMsg != null && !errMsg.isEmpty()) {
			throw new HttpRequestErrorException(RecordRequestType.DELETE_RECORD.getAction());
		}
		
		Long id = response.getLong("uploadId");
		String uuid = response.getString("uploadUuid");
		permissionPO.setMediaId(id.toString());
		permissionPO.setMediaUuid(uuid);
		streamTranscodingTaskRecordPermissionDAO.save(permissionPO);
		
		//回调录制结束的请求url
		streamTranscodingTaskService.sendStopCallback(messageId, id);
	}
}
