package com.sumavision.tetris.zoom.vod.user;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = VodUserPO.class, idClass = Long.class)
public interface VodUserDAO extends BaseDAO<VodUserPO>{

}
