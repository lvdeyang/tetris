package com.sumavision.tetris.bvc.model.terminal.screen;

import java.util.List;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = TerminalScreenPO.class, idClass = Long.class)
public interface TerminalScreenDAO extends BaseDAO<TerminalScreenPO>{

	/**
	 * 查询终端屏幕<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月22日 下午4:48:29
	 * @param Long terminalId 终端id
	 * @return List<TerminalScreenPO> 屏幕列表
	 */
	public List<TerminalScreenPO> findByTerminalId(Long terminalId);
	
}
