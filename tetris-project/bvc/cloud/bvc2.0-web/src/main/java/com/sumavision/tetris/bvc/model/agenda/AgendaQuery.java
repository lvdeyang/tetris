package com.sumavision.tetris.bvc.model.agenda;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;

@Component
public class AgendaQuery {

	@Autowired
	private AgendaDAO agendaDao;
	
	/**
	 * 查询类型<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月30日 下午4:26:16
	 * @return Map<String, String> 音频类型
	 */
	public Map<String, String> queryTypes() throws Exception{
		Map<String, String> types = new HashMap<String, String>();
		AudioOperationType[] values= AudioOperationType.values();
		for(AudioOperationType value:values){
			types.put(value.toString(), value.getName());
		}
		return types;
	}
	
	/**
	 * 分页查询议程<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月30日 下午4:32:18
	 * @param int currentPage 当前页
	 * @param int pageSize 每页数据量
	 * @return total long 总数据量
	 * @return rows List<AgendaVO> 议程列表
	 */
	public Map<String, Object> load(
			int currentPage, 
			int pageSize) throws Exception{
		long total = agendaDao.count();
		Pageable page = new PageRequest(currentPage-1, pageSize);
		Page<AgendaPO> pagedEntities = agendaDao.findAll(page);
		List<AgendaVO> agendas = AgendaVO.getConverter(AgendaVO.class).convert(pagedEntities.getContent(), AgendaVO.class);
		return new HashMapWrapper<String, Object>().put("total", total)
												   .put("rows", agendas)
												   .getMap();
	}
	
}
