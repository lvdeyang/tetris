package com.sumavision.tetris.bvc.business.dao;

import java.util.Collection;
import java.util.List;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.bvc.business.po.combine.video.BusinessCombineVideoSrcPO;
import com.sumavision.tetris.orm.dao.MetBaseDAO;

@RepositoryDefinition(domainClass = BusinessCombineVideoSrcPO.class, idClass = Long.class)
public interface BusinessCombineVideoSrcDAO extends MetBaseDAO<BusinessCombineVideoSrcPO>{
	
	public List<BusinessCombineVideoSrcPO> findByAgendaForwardSourceIdIn(Collection<Long> agendaForwardSourceIds);
}
