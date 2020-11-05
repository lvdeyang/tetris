package com.sumavision.tetris.bvc.business.location;

import java.util.List;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.MetBaseDAO;

@RepositoryDefinition(domainClass=LocationTemplateLayoutPO.class,idClass=Long.class)
public interface LocationTemplateLayoutDAO extends MetBaseDAO<LocationTemplateLayoutPO>{
	
	public List<LocationTemplateLayoutPO> findByTemplateNameContainingAndUserId(String templateName, Long userId);
	
}
