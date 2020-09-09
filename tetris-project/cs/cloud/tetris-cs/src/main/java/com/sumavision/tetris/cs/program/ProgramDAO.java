package com.sumavision.tetris.cs.program;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = ProgramPO.class, idClass = Long.class)
public interface ProgramDAO extends BaseDAO<ProgramPO>{
	
	public ProgramPO findByScheduleId(Long scheduleId);
	
}
