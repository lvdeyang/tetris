package com.sumavision.tetris.mims.app.media.tag;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = TagMimsPermissionPO.class, idClass = Long.class)
public interface TagMimsPermissionDAO extends BaseDAO<TagMimsPermissionPO>{
	
}
