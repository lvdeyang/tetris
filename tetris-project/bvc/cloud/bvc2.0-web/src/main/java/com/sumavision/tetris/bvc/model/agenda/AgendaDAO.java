package com.sumavision.tetris.bvc.model.agenda;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.bvc.business.BusinessInfoType;
import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = AgendaPO.class, idClass = Long.class)
public interface AgendaDAO extends BaseDAO<AgendaPO>{
	
	@Query(value = "select * from TETRIS_BVC_MODEL_AGENDA agenda left join TETRIS_BVC_BUSINESS_RUNNING_AGENDA m on agenda.id = m.agenda_id where m.group_id = ?1", nativeQuery = true)
	public List<AgendaPO> findRunningAgendasByGroupId(Long groupId);
	
	//目前不可用，这样得到的List不能被解析
	@Query(value = "select agenda.id from TETRIS_BVC_MODEL_AGENDA agenda left join TETRIS_BVC_BUSINESS_RUNNING_AGENDA m on agenda.id = m.agenda_id where m.group_id = ?1", nativeQuery = true)
	public List<Long> findRunningAgendaIdsByGroupId(Long groupId);
	
	public AgendaPO findByBusinessInfoType(BusinessInfoType businessInfoType);
	
}
