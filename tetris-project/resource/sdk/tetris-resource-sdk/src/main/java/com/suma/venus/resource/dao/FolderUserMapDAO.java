package com.suma.venus.resource.dao;

import java.util.Collection;
import java.util.List;

import org.springframework.data.repository.RepositoryDefinition;

import com.suma.venus.resource.pojo.FolderUserMap;

@RepositoryDefinition(domainClass = FolderUserMap.class, idClass = Long.class)
public interface FolderUserMapDAO extends CommonDao<FolderUserMap>{

	public FolderUserMap findByUserId(Long userId);
	
	public List<FolderUserMap> findByUserIdIn(Collection<Long> userIds);
	
	public List<FolderUserMap> findByFolderUuid(String folderUuid);
	
}
