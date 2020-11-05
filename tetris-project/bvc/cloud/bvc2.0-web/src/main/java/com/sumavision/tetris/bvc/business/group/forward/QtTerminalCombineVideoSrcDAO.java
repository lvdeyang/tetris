package com.sumavision.tetris.bvc.business.group.forward;

import java.util.Collection;
import java.util.List;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = QtTerminalCombineVideoSrcPO.class, idClass = Long.class)
public interface QtTerminalCombineVideoSrcDAO extends BaseDAO<QtTerminalCombineVideoSrcPO>{

	/**
	 * 查询合屏源<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月28日 下午1:37:30
	 * @param Long qtTerminalCombineVideoId 合屏id
	 * @return List<QtTerminalCombineVideoSrcPO> 源列表
	 */
	public List<QtTerminalCombineVideoSrcPO> findByQtTerminalCombineVideoId(Long qtTerminalCombineVideoId);
	
	/**
	 * 查询合屏源<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月28日 下午1:37:30
	 * @param Collection<Long> qtTerminalCombineVideoId 合屏id列表
	 * @return List<QtTerminalCombineVideoSrcPO> 源列表
	 */
	public List<QtTerminalCombineVideoSrcPO> findByQtTerminalCombineVideoIdIn(Collection<Long> qtTerminalCombineVideoId);
}
