package com.sumavision.tetris.media.editor.task;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = MediaEditorTaskRatePermissionPO.class, idClass = Long.class)
public interface MediaEditorTaskRatePermissionDAO extends BaseDAO<MediaEditorTaskRatePermissionPO>{
	public List<MediaEditorTaskRatePermissionPO> findByTranscodeIdIn(List<String> transcodeIds);
	
	public List<MediaEditorTaskRatePermissionPO> findByTaskIdIn(List<Long> taskIds);
	
	/**
	 * 获取所有转码任务（除某个进度外，主要用于获取未完成任务）<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:06:57
	 * 
	 * @return List<String> 流程id
	 */
	@Query(value = "select transcode_id from TETRIS_MEDIA_EDITOR_TASK_RATE_PERMISSION where rate not like ?1", nativeQuery = true)
	public List<String> findTranscodeIdsExceptRate(int rate);
	
	public List<MediaEditorTaskRatePermissionPO> findByTaskId(Long taskId);
	
	/**
	 * 获取未完成流程的转码任务<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:06:57
	 * 
	 * @return List<MediaEditorTaskRatePermissionPO> 转码任务
	 */
	@Query(value = "SELECT permission.id, permission.uuid, permission.update_time, permission.rate, permission.task_id, permission.transcode_id " +
			"FROM TETRIS_MEDIA_EDITOR_TASK_RATE_PERMISSION permission " +
			"LEFT JOIN TETRIS_MEDIA_EDITOR_TASK task ON permission.task_id=task.id " +
			"WHERE task.complete_rate not like ?1", nativeQuery = true)
	public List<MediaEditorTaskRatePermissionPO> findExceptCompleteTask(String rate);
}
