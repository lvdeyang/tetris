package com.sumavision.tetris.media.editor.task;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;

@Service
@Transactional(rollbackFor = Exception.class)
public class MediaEditorTaskRatePermissionService {
	@Autowired
	MediaEditorTaskRatePermissionDAO mediaEditorTaskRatePermissionDAO;
	
	@Autowired
	MediaEditorTaskQuery mediaEditorTaskQuery;
	
	@Autowired
	MediaEditorTaskService mediaEditorTaskService;
	
	/**
	 * 根据转码任务更新流程任务进度<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:06:57
	 * @param map key:转码任务id value:转码任务进度
	 */
	public void saveRate(HashMapWrapper<String, Integer> map){
		if (map == null || map.isEmpty()) return;
		
		List<String> transcodeIds = new ArrayList<String>();
		for (String transcodeId : map.keySet()) {
			transcodeIds.add(transcodeId);
		}
		
		List<MediaEditorTaskRatePermissionPO> permissions = mediaEditorTaskRatePermissionDAO.findByTranscodeIdIn(transcodeIds);
		
		if (permissions != null && permissions.size() > 0) {
			for (MediaEditorTaskRatePermissionPO permission : permissions) {
				String transcodeId = permission.getTranscodeId();
				int rate = map.getMap().get(transcodeId);
				permission.setRate(rate);
			}
		}
		
		mediaEditorTaskRatePermissionDAO.save(permissions);
		
		mediaEditorTaskService.freshMediaEditorStatus();
	}
}
