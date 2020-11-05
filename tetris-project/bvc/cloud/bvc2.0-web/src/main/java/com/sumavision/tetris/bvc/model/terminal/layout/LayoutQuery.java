package com.sumavision.tetris.bvc.model.terminal.layout;

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
	
	/**
	 * 分页查询布局模板<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月24日 上午10:57:26
	 * @param int currentPage 当前页
	 * @param int pageSize 每页数据量
	 * @return long total 总数据量
	 * @return List<LayoutVO> rows 布局列表
	 */
	public Map<String, Object> load(
			int currentPage,
			int pageSize) throws Exception{
		
		Long total = layoutDao.count();
		
		Pageable page = new PageRequest(currentPage-1, pageSize);
		
		Page<LayoutPO> pagedEntities = layoutDao.findAll(page);
		
		List<LayoutPO> entities = pagedEntities.getContent();
		
		List<LayoutVO> layouts = LayoutVO.getConverter(LayoutVO.class).convert(entities, LayoutVO.class);
		
		return new HashMapWrapper<String, Object>().put("total", total)
												   .put("rows", layouts)
												   .getMap();
	}
	
}
