package com.sumavision.tetris.cs.channel.broad.terminal;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = BroadTerminalBroadInfoPO.class, idClass = Long.class)
public interface BroadTerminalBroadInfoDAO extends BaseDAO<BroadTerminalBroadInfoPO>{
	public BroadTerminalBroadInfoPO findByChannelId(Long channelId);
}
