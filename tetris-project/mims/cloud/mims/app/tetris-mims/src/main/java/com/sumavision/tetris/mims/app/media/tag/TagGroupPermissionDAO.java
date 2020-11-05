package com.sumavision.tetris.mims.app.media.tag;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = TagGroupPermissionPO.class, idClass = Long.class)
public interface TagGroupPermissionDAO extends BaseDAO<TagGroupPermissionPO>{
	
	
}
