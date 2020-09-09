package com.sumavision.tetris.zoom.history;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = HistoryPO.class, idClass = Long.class)
public interface HistoryDAO extends BaseDAO<HistoryPO>{

	/**
	 * 分类型分页查询用户历史记录<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月4日 下午5:45:24
	 * @param String userId 用户id
	 * @param HistoryType type 历史记录类型
	 * @param Pageable page 分页信息
	 * @return Page<HistoryPO> 分页数据
	 */
	public Page<HistoryPO> findByUserIdAndTypeOrderByUpdateTimeDesc(String userId, HistoryType type, Pageable page);
	
	/**
	 * 分类型统计用户历史记录数量<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月4日 下午5:52:40
	 * @param String userId 用户id
	 * @param HistoryType type 历史记录类型
	 * @return long 数据量
	 */
	public long countByUserIdAndType(String userId, HistoryType type);
	
	/**
	 * 分类型查询用户历史记录<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月4日 下午5:55:41
	 * @param String userId 用户id
	 * @param HistoryType type 历史记录类型
	 * @return List<HistoryPO> 历史记录列表
	 */
	public List<HistoryPO> findByUserIdAndTypeOrderByUpdateTimeDesc(String userId, HistoryType type);
	
	/**
	 * 查询资源的历史存储情况<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月9日 上午10:18:36
	 * @param String historyId 资源id
	 * @param HistoryType type 历史类型
	 * @return List<HistoryPO> 历史记录列表
	 */
	public List<HistoryPO> findByHistoryIdAndType(String historyId, HistoryType type);
	
}
