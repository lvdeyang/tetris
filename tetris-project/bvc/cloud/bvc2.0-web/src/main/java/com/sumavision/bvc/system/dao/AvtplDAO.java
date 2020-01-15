package com.sumavision.bvc.system.dao;

import java.util.List;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.bvc.system.enumeration.AvtplUsageType;
import com.sumavision.bvc.system.po.AvtplPO;
import com.sumavision.tetris.orm.dao.MetBaseDAO;


@RepositoryDefinition(domainClass = AvtplPO.class, idClass = long.class)
public interface AvtplDAO extends MetBaseDAO<AvtplPO>{

	public List<AvtplPO> findByUsageType(AvtplUsageType usageType);
	
	public List<AvtplPO> findByName(String name);
	
}
