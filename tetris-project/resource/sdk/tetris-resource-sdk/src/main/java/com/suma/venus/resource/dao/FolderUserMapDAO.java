package com.suma.venus.resource.dao;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;

import com.suma.venus.resource.pojo.FolderUserMap;

@RepositoryDefinition(domainClass = FolderUserMap.class, idClass = Long.class)
public interface FolderUserMapDAO extends CommonDao<FolderUserMap>{

	public FolderUserMap findByUserId(Long userId);
	
	public List<FolderUserMap> findByUserIdIn(Collection<Long> userIds);
	
	public List<FolderUserMap> findByFolderUuid(String folderUuid);
	
	public List<FolderUserMap> findByFolderIdAndUserIdIn(Long folderId, Collection<Long> userIds);
	
	public List<FolderUserMap> findByFolderIdInAndUserIdIn(Collection<Long> folderIds, Collection<Long> userIds);
	
	@Query(value = "select m.* from folder_user_map m LEFT JOIN folderpo f on m.folder_id = f.id where m.creator != 'ldap' and (f.source_type='SYSTEM' or f.source_type is null) and f.to_ldap = 'Y'", nativeQuery = true)
	public List<FolderUserMap> findLocalLdapMap();
	
	@Query(value = "select m.* from folder_user_map m where m.creator = 'ldap'", nativeQuery = true)
	public List<FolderUserMap> findFromLdapMap();
	
	public List<FolderUserMap> findByUserNoIn(Collection<String> userNos);
	
	public FolderUserMap findByUserNo(String userNo);
	
	public List<FolderUserMap> findByUserIdNotIn(Collection<Long> userIds);
	
}
