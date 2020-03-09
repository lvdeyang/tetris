package com.sumavision.tetris.zoom.history;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;

@Component
public class HistoryQuery {

	@Autowired
	private UserQuery userQuery;
	
	@Autowired
	private HistoryDAO historyDao;
	
	/**
	 * 分页查询用户历史记录<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月4日 下午5:26:27
	 * @param int currentPage 当前页
	 * @param int pageSize 每页数据量
	 * @return total long 总数据量
	 * @return rows List<HistoryVO> 历史记录列表
	 */
	public Map<String, Object> zoomList(
			int currentPage,
			int pageSize) throws Exception{
		
		UserVO user = userQuery.current();
		
		long total = historyDao.countByUserIdAndType(user.getId().toString(), HistoryType.ZOOM_CODE);
		
		List<HistoryPO> entities = findByUserIdAndTypeOrderByUpdateTimeDesc(user.getId().toString(), HistoryType.ZOOM_CODE, currentPage, pageSize);
		
		List<HistoryVO> rows = HistoryVO.getConverter(HistoryVO.class).convert(entities, HistoryVO.class);
		
		return new HashMapWrapper<String, Object>().put("total", total)
												   .put("rows", rows)
												   .getMap();
	}
	
	/**
	 * 查询用户所有历史记录<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月4日 下午5:58:34
	 * @return List<HistoryVO> 历史记录列表
	 */
	public List<HistoryVO> zoomListAll() throws Exception{
		
		UserVO user = userQuery.current();
		
		List<HistoryPO> entities = historyDao.findByUserIdAndTypeOrderByUpdateTimeDesc(user.getId().toString(), HistoryType.ZOOM_CODE);
		
		return HistoryVO.getConverter(HistoryVO.class).convert(entities, HistoryVO.class);
	}
	
	/**
	 * 分类型分页查询用户历史记录<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月4日 下午5:30:32
	 * @param String userId 用户id
	 * @param HistoryType type 历史记录类型
	 * @param int currentPage 当前页
	 * @param int pageSize 每页数据量
	 * @return List<HistoryPO> 历史记录列表
	 */
	public List<HistoryPO> findByUserIdAndTypeOrderByUpdateTimeDesc(String userId, HistoryType type, int currentPage, int pageSize) throws Exception{
		Pageable page = new PageRequest(currentPage-1, pageSize);
		Page<HistoryPO> pagedEntities = historyDao.findByUserIdAndTypeOrderByUpdateTimeDesc(userId, type, page);
		return pagedEntities.getContent();
	}
	
}
