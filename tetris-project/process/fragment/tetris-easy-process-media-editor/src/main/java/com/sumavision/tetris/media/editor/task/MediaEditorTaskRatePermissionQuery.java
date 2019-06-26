package com.sumavision.tetris.media.editor.task;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MediaEditorTaskRatePermissionQuery {
	@Autowired
	MediaEditorTaskRatePermissionDAO mediaEditorTaskRatePermissionDAO;
	
	/**
	 * 获取未完成转码任务的转码任务id<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:06:57
	 * 
	 * @return List<String> 转码任务id
	 */
	public List<String> getAllTranscodingTaskId(){
		return mediaEditorTaskRatePermissionDAO.findTranscodeIdsExceptRate(100);
	}
	
	/**
	 * 根据流程任务id获取转码任务列表<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:06:57
	 * 
	 * @return List<String> 转码任务id
	 */
	public List<MediaEditorTaskRatePermissionVO> getByTaskId(Long taskId) throws Exception{
		List<MediaEditorTaskRatePermissionPO> permissions = mediaEditorTaskRatePermissionDAO.findByTaskId(taskId);
		if (permissions == null || permissions.size() <= 0) return null;
		return MediaEditorTaskRatePermissionVO.getConverter(MediaEditorTaskRatePermissionVO.class).convert(permissions, MediaEditorTaskRatePermissionVO.class);
	}
}
