package com.sumavision.tetris.cs.program;

import java.util.List;
import java.util.Set;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = ProgramPO.class, idClass = Long.class)
public interface ProgramDAO extends BaseDAO<ProgramPO>{
	
	public ProgramPO findByScheduleId(Long scheduleId);
	
	public List<ProgramPO> findByScheduleIdIn(Set<Long> scheduleId);
	
}
