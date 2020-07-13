package com.sumavision.tetris.bvc.model.agenda;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = AgendaPO.class, idClass = Long.class)
public interface AgendaDAO extends BaseDAO<AgendaPO>{

}
