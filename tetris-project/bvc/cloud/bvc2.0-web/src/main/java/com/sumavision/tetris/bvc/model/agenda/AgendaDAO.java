package com.sumavision.tetris.bvc.model.agenda;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = AgendaPO.class, idClass = Long.class)
public interface AgendaDAO extends BaseDAO<AgendaPO>{
	
	@Query(value = "select * from AgendaPO agenda left join TETRIS_BVC_BUSINESS_RUNNING_AGENDA m on agenda.id = m.agenda_id where m.group_id = ?1", nativeQuery = true)
	public List<AgendaPO> findRunningAgendasByGroupId(Long groupId);
	
	@Query(value = "select *.id from AgendaPO agenda left join TETRIS_BVC_BUSINESS_RUNNING_AGENDA m on agenda.id = m.agenda_id where m.group_id = ?1", nativeQuery = true)
	public List<Long> findRunningAgendaIdsByGroupId(Long groupId);
	
}
