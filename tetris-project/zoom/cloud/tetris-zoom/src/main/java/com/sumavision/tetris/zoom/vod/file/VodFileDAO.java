package com.sumavision.tetris.zoom.vod.file;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = VodFilePO.class, idClass = Long.class)
public interface VodFileDAO extends BaseDAO<VodFilePO>{

}
