package com.sumavision.tetris.cs.program;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = ProgramPO.class, idClass = Long.class)
public interface ProgramDAO extends BaseDAO<ProgramPO>{
	
	@Query(value = "SELECT * FROM TETRIS_CS_PROGRAM WHERE CHANNEL_ID LIKE ?1", nativeQuery = true)
	public ProgramPO findByChannelId(Long reg1);
	
}
