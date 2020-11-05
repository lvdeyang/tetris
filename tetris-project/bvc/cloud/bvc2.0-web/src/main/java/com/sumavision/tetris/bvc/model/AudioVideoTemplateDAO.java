package com.sumavision.tetris.bvc.model;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = AudioVideoTemplatePO.class, idClass = Long.class)
public interface AudioVideoTemplateDAO extends BaseDAO<AudioVideoTemplatePO>{

}
