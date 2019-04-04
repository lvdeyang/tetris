package com.sumavision.tetris.media.editor.task;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
}
