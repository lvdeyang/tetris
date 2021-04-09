package com.sumavision.tetris.bvc.model.layout;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;

@Component
public class LayoutQuery {

	@Autowired
	private LayoutDAO layoutDao;
	
	@Autowired
	private LayoutPositionDAO layoutPositionDao;
	
	/**
	 * 分页查询虚拟源br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年10月26日 下午3:55:28
	 * @param int currentPage 当前页
	 * @param int pageSize 每页数据量
	 * @return total long 总数据量
	 * @return rows List<LayoutVO> 虚拟源列表
	 */
	public Map<String, Object> load(
			int currentPage, 
			int pageSize) throws Exception{
		Pageable page = new PageRequest(currentPage-1, pageSize);
		Page<LayoutPO> pagedEntities = layoutDao.findAll(page);
		List<LayoutVO> rows = LayoutVO.getConverter(LayoutVO.class).convert(pagedEntities.getContent(), LayoutVO.class);
		Long total = pagedEntities.getTotalElements();
		return new HashMapWrapper<String, Object>().put("total", total)
												   .put("rows", rows)
												   .getMap();
	}
	
	/**
	 * 查询虚拟源<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月23日 下午4:41:22
	 * @return List<LayoutVO> 虚拟源列表
	 */
	public List<LayoutVO> loadAll() throws Exception{
		List<LayoutPO> entities = layoutDao.findAll();
		return LayoutVO.getConverter(LayoutVO.class).convert(entities, LayoutVO.class);
	}
	
	/**
	 * 查询全部的虚拟源（带布局信息）<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月29日 下午12:05:11
	 * @return List<LayoutVO> 布局列表
	 */
	public List<LayoutVO> loadAllWithPosition() throws Exception{
		List<LayoutPO> entities = layoutDao.findAll();
		List<LayoutVO> layouts = LayoutVO.getConverter(LayoutVO.class).convert(entities, LayoutVO.class);
		List<LayoutPositionPO> layoutPositionEntities = layoutPositionDao.findAll();
		if(layouts!=null && layouts.size()>0 && 
				layoutPositionEntities!=null && layoutPositionEntities.size()>0){
			for(LayoutVO layout:layouts){
				if(layout.getPositions() == null) layout.setPositions(new ArrayList<LayoutPositionVO>());
				for(LayoutPositionPO layoutPositionEntity:layoutPositionEntities){
					if(layout.getId().equals(layoutPositionEntity.getLayoutId())){
						layout.getPositions().add(new LayoutPositionVO().set(layoutPositionEntity));
					}
				}
			}
		}
		return layouts;
	}
	
}
