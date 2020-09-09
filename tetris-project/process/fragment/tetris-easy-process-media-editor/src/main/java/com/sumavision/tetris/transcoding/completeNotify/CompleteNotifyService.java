package com.sumavision.tetris.transcoding.completeNotify;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.easy.process.core.ProcessService;
import com.sumavision.tetris.media.editor.task.MediaEditorTaskRatePermissionVO;
import com.sumavision.tetris.media.editor.task.MediaEditorTaskService;
import com.sumavision.tetris.media.editor.task.MediaEditorTaskVO;
import com.sumavision.tetris.transcoding.addTask.AddTaskService;
import com.sumavision.tetris.transcoding.completeNotify.VO.NotifyResponseVO;
import com.sumavision.tetris.transcoding.completeNotify.VO.TranscodeVO;

@Service
@Transactional(rollbackFor = Exception.class)
public class CompleteNotifyService {
	@Autowired
	ProcessService processService;
	
	@Autowired
	AddTaskService addTaskService;
	
	@Autowired
	MediaEditorTaskService mediaEditorTaskService;
	
	/**
	 * 流程任务完成的云转码回调<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:06:57
	 * 
	 * @return
	 */
	public void dealNotify(NotifyResponseVO response) throws Exception{
		List<TranscodeVO> transcodes = response.getTranscodeJobs().getTranscodes();
		List<String> ids = new ArrayList<String>();
		for(TranscodeVO transcode : transcodes){
			if (transcode.getResultCode().equals("0") && transcode.getResultString().equals("OK")) {
				ids.add(transcode.getId());
			}
		}
		
		//取数据库，存新进度
		MediaEditorTaskVO mediaEditorTask = mediaEditorTaskService.setMediaEditorStatus(ids);
		
		if (mediaEditorTask == null) return;
		
		//回调流程
		List<String> urlList = new ArrayList<String>();
		Long folderId = null;
		String tags = null;
		for (MediaEditorTaskRatePermissionVO permission : mediaEditorTask.getTranscodes()) {
			urlList.add(permission.getSaveUrl());
			folderId = permission.getFolderId();
			tags = permission.getTags();
		}
		JSONObject listObject = new JSONObject();
		listObject.put("urlList", String.join(",", urlList));
		listObject.put("parentFolderId", folderId);
		listObject.put("mediaTags", tags);
		
		processService.receiveTaskTrigger(mediaEditorTask.getProcessInstanceId(), mediaEditorTask.getAccessPointId(), listObject.toJSONString());
	}
}
