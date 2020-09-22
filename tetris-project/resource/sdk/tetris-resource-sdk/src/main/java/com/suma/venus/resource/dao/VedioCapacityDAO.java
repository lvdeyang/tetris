package com.suma.venus.resource.dao;

import org.springframework.data.repository.RepositoryDefinition;

import com.suma.venus.resource.pojo.VedioCapacityPO;


@RepositoryDefinition(domainClass = VedioCapacityPO.class, idClass = Long.class)
public interface VedioCapacityDAO extends CommonDao<VedioCapacityPO>{

}
