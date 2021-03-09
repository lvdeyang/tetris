package com.suma.venus.resource.dao;

import org.springframework.data.repository.RepositoryDefinition;

import com.suma.venus.resource.pojo.OutlandPermissionCheckPO;

@RepositoryDefinition(domainClass = OutlandPermissionCheckPO.class, idClass = Long.class)
public interface OutlandPermissionCheckDao extends CommonDao<OutlandPermissionCheckPO>{

}
