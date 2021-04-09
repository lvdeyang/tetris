package com.sumavision.tetris.bvc.model.terminal;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.sumavision.tetris.bvc.model.terminal.physical.screen.TerminalPhysicalScreenDAO;
import com.sumavision.tetris.bvc.model.terminal.physical.screen.TerminalPhysicalScreenPO;
import com.sumavision.tetris.bvc.model.terminal.physical.screen.TerminalPhysicalScreenVO;
import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;

@Component
public class TerminalQuery {

	@Autowired
	private TerminalDAO terminalDao;
	
	@Autowired
	private TerminalPhysicalScreenDAO terminalPhysicalScreenDao;
	
	/**
	 * 查询全部终端<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月29日 下午3:07:03
	 * @return List<TerminalVO> 终端列表
	 */
	public List<TerminalVO> loadAll() throws Exception{
		List<TerminalPO> entities = terminalDao.findAll();
		return TerminalVO.getConverter(TerminalVO.class).convert(entities, TerminalVO.class);
	}
	
	/**
	 * 分页查询终端<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月5日 下午2:59:49
	 * @param int currentPage 当前页
	 * @param int pageSize 每页数据量
	 * @return Long total 总数据量
	 * @return List<TerminalVO> rows 终端列表
	 */
	public Map<String, Object> load(int currentPage, int pageSize) throws Exception{
		Pageable page = new PageRequest(currentPage-1, pageSize);
		Long total = terminalDao.count();
		Page<TerminalPO> pagedEntities = terminalDao.findAll(page);
		List<TerminalPO> entities = pagedEntities.getContent();
		List<TerminalVO> terminals = TerminalVO.getConverter(TerminalVO.class).convert(entities, TerminalVO.class);
		return new HashMapWrapper<String, Object>().put("total", total)
												   .put("rows", terminals)
												   .getMap();
	}
	
	/**
	 * 根据id查询终端<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月15日 下午6:09:19
	 * @param Long id 终端id
	 * @return TerminalVO 终端信息
	 */
	public TerminalVO findById(Long id) throws Exception{
		TerminalPO terminalEntity = terminalDao.findOne(id);
		TerminalVO terminal = new TerminalVO().set(terminalEntity);
		List<TerminalPhysicalScreenPO> physicalScreenEntities = terminalPhysicalScreenDao.findByTerminalId(id);
		List<TerminalPhysicalScreenVO> physicalScreens = TerminalPhysicalScreenVO.getConverter(TerminalPhysicalScreenVO.class).convert(physicalScreenEntities, TerminalPhysicalScreenVO.class);
		terminal.setPhysicalScreens(physicalScreens);
		return terminal;
	}
	
}
