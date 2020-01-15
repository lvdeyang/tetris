package com.suma.venus.resource.dao;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;

import com.suma.venus.resource.pojo.MQMessagePO;

@RepositoryDefinition(domainClass=MQMessagePO.class,idClass=Long.class)
public interface MQMessageDao extends CommonDao<MQMessagePO>{

	public MQMessagePO findByRequestId(String requestId);
	
	@Modifying
	@Transactional
	@Query("delete from MQMessagePO po where po.requestId = ?1")
	public int deleteByRequestId(String requestId);
}
