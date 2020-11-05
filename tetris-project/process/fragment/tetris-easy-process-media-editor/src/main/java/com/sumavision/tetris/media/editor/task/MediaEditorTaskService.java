package com.sumavision.tetris.media.editor.task;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;

@Service
@Transactional(rollbackFor = Exception.class)
public class MediaEditorTaskService {
	@Autowired
	private MediaEditorTaskDAO mediaEditorTaskDAO;

	@Autowired
	private MediaEditorTaskRatePermissionDAO mediaEditorTaskRatePermissionDAO;

	@Autowired
	private UserQuery userQuery;

	/**
	 * 保存流程任务和转码任务<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:06:57
	 * 
	 * @param processInstanceId
	 *            流程id
	 * @param accessPointId
	 *            节点id
	 * @param transcodeIds
	 *            转码id
	 */
	public MediaEditorTaskVO addMediaEditorTask(String processInstanceId, Long accessPointId,
			HashMapWrapper<String, MediaEditorTaskRatePermissionVO> transcodes) throws Exception {
		UserVO userVO = userQuery.current();

		if (transcodes == null || transcodes.size() <= 0)
			return null;

		MediaEditorTaskPO mediaPO = new MediaEditorTaskPO();

		mediaPO.setUserId(userVO.getUuid());
		mediaPO.setStatus(MediaEditorTaskStatus.APPROVED);
		mediaPO.setProcessInstanceId(processInstanceId);
		mediaPO.setAccessPointId(accessPointId);
		mediaPO.setCompleteRate(0);
		mediaPO.setUpdateTime(new Date());

		mediaEditorTaskDAO.save(mediaPO);

		List<MediaEditorTaskRatePermissionPO> permissions = new ArrayList<MediaEditorTaskRatePermissionPO>();
		List<String> transcodeIds = new ArrayList<String>();
		for (String transcodeId : transcodes.keySet()) {
			transcodeIds.add(transcodeId);
			MediaEditorTaskRatePermissionVO permission = transcodes.getMap().get(transcodeId);
			MediaEditorTaskRatePermissionPO permissionPO = new MediaEditorTaskRatePermissionPO();
			permissionPO.setTaskId(mediaPO.getId());
			permissionPO.setTranscodeId(transcodeId);
			permissionPO.setSaveUrl(permission.getSaveUrl());
			permissionPO.setFolderId(permission.getFolderId());
			permissionPO.setRate(0);
			permissionPO.setUpdateTime(new Date());
			permissionPO.setTags(permission.getTags());
			permissions.add(permissionPO);
		}
		mediaEditorTaskRatePermissionDAO.save(permissions);

		return new MediaEditorTaskVO().set(mediaPO).setTranscodeIds(transcodeIds);
	}

	/**
	 * 流程转码任务完成，更新数据库<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:06:57
	 * 
	 * @param transcodeIds
	 *            转码任务id
	 * @return MediaEditorTaskVO 流程任务
	 */
	public MediaEditorTaskVO setMediaEditorStatus(List<String> transcodeIds) throws Exception {
		// 存transcodeId进度关系表
		List<MediaEditorTaskRatePermissionPO> transcodePOs = mediaEditorTaskRatePermissionDAO
				.findByTranscodeIdIn(transcodeIds);

		if (transcodePOs == null || transcodePOs.size() <= 0)
			return null;

		Long taskId = null;

		for (MediaEditorTaskRatePermissionPO permissionPO : transcodePOs) {
			taskId = permissionPO.getTaskId();
			permissionPO.setRate(100);
		}
		mediaEditorTaskRatePermissionDAO.save(transcodePOs);

		// 存流程进度表
		MediaEditorTaskPO taskPO = mediaEditorTaskDAO.findOne(taskId);

		if (taskPO == null)
			return null;

		taskPO.setCompleteRate(100);
		mediaEditorTaskDAO.save(taskPO);

		return new MediaEditorTaskVO().set(taskPO)
				.setTranscodes(MediaEditorTaskRatePermissionVO.getConverter(MediaEditorTaskRatePermissionVO.class)
						.convert(transcodePOs, MediaEditorTaskRatePermissionVO.class));
	}

	/**
	 * 根据转码任务更新流程任务进度<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:06:57
	 */
	public void freshMediaEditorStatus() {
		List<MediaEditorTaskPO> tasks = mediaEditorTaskDAO.findAllExceptTempleteRate();
		if (tasks == null || tasks.size() <= 0)
			return;

		List<MediaEditorTaskRatePermissionPO> permissions = mediaEditorTaskRatePermissionDAO
				.findExceptCompleteTask();
		if (permissions == null || permissions.size() <= 0)
			return;

		HashMapWrapper<Long, List<Integer>> status = new HashMapWrapper<Long, List<Integer>>();
		for (MediaEditorTaskRatePermissionPO permission : permissions) {
			Long taskId = permission.getTaskId();
			if (status.containsKey(taskId)) {
				status.getMap().get(taskId).add(permission.getRate());
			} else {
				List<Integer> rates = new ArrayList<Integer>();
				rates.add(permission.getRate());
				status.put(taskId, rates);
			}
		}

		for (MediaEditorTaskPO task : tasks) {
			Long taskId = task.getId();
			if (status.containsKey(taskId)) {
				List<Integer> rates = status.getMap().get(taskId);
				int sum = 0;
				for (Integer rate : rates) {
					sum += rate;
				}
				int divide = sum / rates.size();
				task.setCompleteRate(divide);
			}
		}

		mediaEditorTaskDAO.save(tasks);
	}
}
