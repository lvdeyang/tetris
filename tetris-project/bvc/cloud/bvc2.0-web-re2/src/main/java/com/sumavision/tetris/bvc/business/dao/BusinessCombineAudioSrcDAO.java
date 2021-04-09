package com.sumavision.tetris.bvc.business.dao;

import java.util.Collection;
import java.util.List;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.bvc.business.po.combine.audio.BusinessCombineAudioSrcPO;
import com.sumavision.tetris.orm.dao.MetBaseDAO;

@RepositoryDefinition(domainClass = BusinessCombineAudioSrcPO.class, idClass = Long.class)
public interface BusinessCombineAudioSrcDAO extends MetBaseDAO<BusinessCombineAudioSrcPO>{

}
