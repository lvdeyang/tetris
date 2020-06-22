package com.sumavision.tetris.bvc.model.terminal.channel;

import java.util.List;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = TerminalChannelPO.class, idClass = Long.class)
public interface TerminalChannelDAO extends BaseDAO<TerminalChannelPO>{

	/**
	 * 查询终端通道<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月18日 下午3:05:08
	 * @param Long terminalId 终端id
	 * @return List<TerminalChannelPO> 通道列表
	 */
	public List<TerminalChannelPO> findByTerminalIdOrderByTypeAscNameAsc(Long terminalId);
	
}
