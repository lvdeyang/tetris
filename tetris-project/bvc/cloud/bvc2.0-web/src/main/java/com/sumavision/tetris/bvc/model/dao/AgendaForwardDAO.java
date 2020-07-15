package com.sumavision.tetris.bvc.model.dao;

import java.util.List;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.bvc.model.agenda.AgendaDestinationType;
import com.sumavision.tetris.bvc.model.agenda.AgendaForwardPO;
import com.sumavision.tetris.bvc.model.agenda.AgendaSourceType;
import com.sumavision.tetris.orm.dao.MetBaseDAO;

@RepositoryDefinition(domainClass = AgendaForwardPO.class, idClass = long.class)
public interface AgendaForwardDAO extends MetBaseDAO<AgendaForwardPO>{

	public List<AgendaForwardPO> findByAgendaId(Long agendaId);
	
	public List<AgendaForwardPO> findBySourceTypeAndSourceId(AgendaSourceType sourceType, Long sourceId);
	
	public List<AgendaForwardPO> findBySourceTypeAndSourceIdIn(AgendaSourceType sourceType, List<Long> sourceIds);
	
	public List<AgendaForwardPO> findByDestinationTypeAndSourceId(AgendaDestinationType destinationType, Long sourceId);
	
	public List<AgendaForwardPO> findByDestinationTypeAndSourceIdIn(AgendaDestinationType destinationType, List<Long> sourceIds);
}
