package com.sumavision.tetris.media.editor.task;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;
import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = MediaEditorTaskPO.class, idClass = Long.class)
public interface MediaEditorTaskDAO extends BaseDAO<MediaEditorTaskPO>{

	/**
	 * 根据用户和状态状态分页查询任务<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年4月2日 上午10:50:50
	 * @param String userId 用户id
	 * @param MediaEditorTaskStatus status 任务状态
	 * @param Pageable page 分页信息
	 * @return Page<MediaEditorTaskPO> 任务列表
	 */
	public Page<MediaEditorTaskPO> findByUserIdAndStatus(String userId, MediaEditorTaskStatus status, Pageable page);
	
	/**
	 * 根据用户和状态统计任务数量<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年4月2日 上午10:52:04
	 * @param String userId 用户id
	 * @param MediaEditorTaskStatus status 任务状态
	 * @return int 任务数量
	 */
	public int countByUserIdAndStatus(String userId, MediaEditorTaskStatus status);
	
	/**
	 * 获取所有任务（除某个进度外，主要用于获取未完成任务）<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午10:52:04
	 * @param String rate 进度
	 * @return List<Long> 任务id
	 */
	@Query(value = "SELECT id FROM TETRIS_MEDIA_EDITOR_TASK WHERE COMPLETE_RATE <> '100'", nativeQuery = true)
	public List<Long> findAllExceptTempleteRateToId();
	
	/**
	 * 获取所有任务（除某个进度外，主要用于获取未完成任务）<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午10:52:04
	 * @param String rate 进度
	 * @return List<MediaEditorTaskPO> 任务
	 */
	@Query(value = "SELECT * FROM TETRIS_MEDIA_EDITOR_TASK WHERE COMPLETE_RATE <> '100'", nativeQuery = true)
	public List<MediaEditorTaskPO> findAllExceptTempleteRate();
	
	/**
	 * 根据流程id获取任务<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午10:52:04
	 * @param String processId 流程id
	 * @return List<MediaEditorTaskPO> 任务
	 */
	public MediaEditorTaskPO findByProcessInstanceId(String processId);
	
	/**
	 * 根据用户id获取任务<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午10:52:04
	 * @param String userId 用户id
	 * @return List<MediaEditorTaskPO> 任务列表
	 */
	public List<MediaEditorTaskPO> findByUserId(String userId);
}
