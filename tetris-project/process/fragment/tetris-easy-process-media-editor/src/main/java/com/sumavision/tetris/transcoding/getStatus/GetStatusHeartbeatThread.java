package com.sumavision.tetris.transcoding.getStatus;

import java.util.List;

import com.sumavision.tetris.commons.context.SpringContext;
import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.media.editor.task.MediaEditorTaskRatePermissionQuery;
import com.sumavision.tetris.media.editor.task.MediaEditorTaskRatePermissionService;

public class GetStatusHeartbeatThread extends Thread{
	private MediaEditorTaskRatePermissionQuery mediaEditorTaskRatePermissionQuery;
	private MediaEditorTaskRatePermissionService mediaEditorTaskRatePermissionService;
	private GetStatusService getStatusService;
	
	private long sleep = 5000;

	@Override
	public void run() {
		while(true){
			try{
				this.startHeartbeat();
				synchronized (GetStatusHeartbeatThread.class) {
					Thread.sleep(this.getSleep());
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 获取转码任务进度<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:06:57
	 * 
	 * @return
	 */
	private void startHeartbeat(){
		if (mediaEditorTaskRatePermissionQuery == null || mediaEditorTaskRatePermissionService == null || getStatusService == null) {
			mediaEditorTaskRatePermissionQuery = SpringContext.getBean(MediaEditorTaskRatePermissionQuery.class);
			mediaEditorTaskRatePermissionService = SpringContext.getBean(MediaEditorTaskRatePermissionService.class);
			getStatusService = SpringContext.getBean(GetStatusService.class);
		}
		List<String> ids =  mediaEditorTaskRatePermissionQuery.getAllTranscodingTaskId();
		HashMapWrapper<String, Integer> status = getStatusService.getFromAdapter(ids);
		mediaEditorTaskRatePermissionService.saveRate(status);
		if (ids.size() <= 0) {
			this.setSleep(60000);
		}
	}

	public long getSleep() {
		return sleep;
	}

	public void setSleep(long sleep) {
		this.sleep = sleep;
	}

	public MediaEditorTaskRatePermissionQuery getMediaEditorTaskRatePermissionQuery() {
		return mediaEditorTaskRatePermissionQuery;
	}

	public void setMediaEditorTaskRatePermissionQuery(
			MediaEditorTaskRatePermissionQuery mediaEditorTaskRatePermissionQuery) {
		this.mediaEditorTaskRatePermissionQuery = mediaEditorTaskRatePermissionQuery;
	}

	public MediaEditorTaskRatePermissionService getMediaEditorTaskRatePermissionService() {
		return mediaEditorTaskRatePermissionService;
	}

	public void setMediaEditorTaskRatePermissionService(
			MediaEditorTaskRatePermissionService mediaEditorTaskRatePermissionService) {
		this.mediaEditorTaskRatePermissionService = mediaEditorTaskRatePermissionService;
	}

	public GetStatusService getGetStatusService() {
		return getStatusService;
	}

	public void setGetStatusService(GetStatusService getStatusService) {
		this.getStatusService = getStatusService;
	}
}
