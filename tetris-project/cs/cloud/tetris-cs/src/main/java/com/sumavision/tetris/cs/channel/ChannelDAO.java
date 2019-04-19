package com.sumavision.tetris.cs.channel;

import java.util.Collection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = ChannelPO.class, idClass = Long.class)
public interface ChannelDAO extends BaseDAO<ChannelPO>{
	
}
