package com.sumavision.tetris.zoom.history;

import java.util.ArrayList;
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
import com.sumavision.tetris.zoom.ZoomDAO;
import com.sumavision.tetris.zoom.ZoomPO;

@Component
public class HistoryQuery {

	@Autowired
	private UserQuery userQuery;
	
	@Autowired
	private HistoryDAO historyDao;
	
	@Autowired
	private ZoomDAO zoomDao;
	
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
	 * @return create List<HistoryZoomVO> 用户创建的会议
	 * @return history List<HistoryZoomVO> 用户加入的会议
	 */
	public Map<String, Object> zoomListAll() throws Exception{
		
		UserVO user = userQuery.current();
		
		List<ZoomPO> zoomEntities = zoomDao.findByCreatorUserId(user.getId());
		List<HistoryZoomVO> createZooms = new ArrayList<HistoryZoomVO>();
		if(zoomEntities!=null && zoomEntities.size()>0){
			for(ZoomPO entity:zoomEntities){
				createZooms.add(new HistoryZoomVO().set(entity));
			}
		}
		
		List<HistoryPO> historyEntities = historyDao.findByUserIdAndTypeOrderByUpdateTimeDesc(user.getId().toString(), HistoryType.ZOOM_CODE);
		List<HistoryZoomVO> historyZooms = new ArrayList<HistoryZoomVO>();
		if(historyEntities!=null && historyEntities.size()>0){
			List<String> codes = new ArrayList<String>();
			for(HistoryPO entity:historyEntities){
				codes.add(entity.getHistoryId());
			}
			List<ZoomPO> joinZooms = zoomDao.findByCodeIn(codes);
			if(joinZooms!=null && joinZooms.size()>0){
				for(ZoomPO zoom:joinZooms){
					for(HistoryPO history:historyEntities){
						if(history.getHistoryId().equals(zoom.getCode())){
							historyZooms.add(new HistoryZoomVO().set(zoom, history));
							break;
						}
					}
				}
			}
		}
		
		return new HashMapWrapper<String, Object>().put("create", createZooms)
												   .put("history", historyZooms)
												   .getMap();
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
