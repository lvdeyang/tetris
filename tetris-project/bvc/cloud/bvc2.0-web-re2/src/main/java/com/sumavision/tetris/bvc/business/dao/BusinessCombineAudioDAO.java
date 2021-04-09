package com.sumavision.tetris.bvc.business.dao;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.bvc.business.po.combine.audio.BusinessCombineAudioPO;
import com.sumavision.tetris.orm.dao.MetBaseDAO;

@RepositoryDefinition(domainClass = BusinessCombineAudioPO.class, idClass = long.class)
public interface BusinessCombineAudioDAO extends MetBaseDAO<BusinessCombineAudioPO>{
	
}
