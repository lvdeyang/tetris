package com.sumavision.tetris.bvc.model.terminal;

import java.util.List;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = TerminalLayoutPermissionPO.class, idClass = Long.class)
public interface TerminalLayoutPermissionDAO extends BaseDAO<TerminalLayoutPermissionPO>{

	/**
	 * 根据终端查询屏幕<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月28日 上午10:16:27
	 * @param Long terminalId 终端id
	 * @return List<TerminalLayoutPermissionPO> 终端屏幕映射
	 */
	public List<TerminalLayoutPermissionPO> findByTerminalId(Long terminalId);
	
}
