package com.sumavision.bvc.device.group.dao;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.bvc.device.group.po.GroupRecordPO;
import com.sumavision.tetris.orm.dao.MetBaseDAO;

@RepositoryDefinition(domainClass = GroupRecordPO.class, idClass = long.class)
public interface GroupRecordDAO extends MetBaseDAO<GroupRecordPO>{
	
}
