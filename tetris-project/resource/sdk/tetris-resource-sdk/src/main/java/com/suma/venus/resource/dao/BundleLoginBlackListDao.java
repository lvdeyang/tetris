package com.suma.venus.resource.dao;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;

import com.suma.venus.resource.pojo.BundleLoginBlackListPO;

@RepositoryDefinition(domainClass=BundleLoginBlackListPO.class,idClass=Long.class)
public interface BundleLoginBlackListDao extends CommonDao<BundleLoginBlackListPO>{

	public BundleLoginBlackListPO findByLoginId(String loginId);
	
	
	@Modifying
	@Transactional
	@Query("delete from BundleLoginBlackListPO po where po.loginId = ?1")
	public int deleteByLoginId(String loginId);
}
