package com.sumavision.tetris.bvc.page;

import java.util.Collection;
import java.util.List;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.bvc.business.group.GroupMemberType;
import com.sumavision.tetris.orm.dao.MetBaseDAO;

@RepositoryDefinition(domainClass = PageInfoPO.class, idClass = long.class)
public interface PageInfoDAO extends MetBaseDAO<PageInfoPO>{
	
	public PageInfoPO findByOriginIdAndTerminalIdAndGroupMemberType(String originId, Long terminalId, GroupMemberType groupMemberType);
	
	public List<PageInfoPO> findByOriginIdIn(Collection<String> originIds);
	
}
