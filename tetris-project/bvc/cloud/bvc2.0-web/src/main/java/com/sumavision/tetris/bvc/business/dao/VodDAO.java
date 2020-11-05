package com.sumavision.tetris.bvc.business.dao;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.bvc.business.vod.VodPO;
import com.sumavision.tetris.orm.dao.MetBaseDAO;

@RepositoryDefinition(domainClass = VodPO.class, idClass = long.class)
public interface VodDAO extends MetBaseDAO<VodPO>{

	public VodPO findByGroupId(Long groupId);
}
