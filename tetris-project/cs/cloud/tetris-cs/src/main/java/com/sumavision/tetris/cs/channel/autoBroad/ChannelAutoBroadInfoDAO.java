package com.sumavision.tetris.cs.channel.autoBroad;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = ChannelAutoBroadInfoPO.class, idClass = Long.class)
public interface ChannelAutoBroadInfoDAO extends BaseDAO<ChannelAutoBroadInfoPO>{
	public ChannelAutoBroadInfoPO findByChannelId(Long channelId);
}
