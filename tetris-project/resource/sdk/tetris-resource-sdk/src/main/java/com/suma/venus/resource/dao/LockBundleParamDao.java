package com.suma.venus.resource.dao;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;

import com.suma.venus.resource.pojo.LockBundleParamPO;

@RepositoryDefinition(domainClass=LockBundleParamPO.class,idClass=Long.class)
public interface LockBundleParamDao extends CommonDao<LockBundleParamPO>{

	public LockBundleParamPO findByBundleId(String bundleId);
	
	public List<LockBundleParamPO> findByBundleIdIn(Collection<String> bundleIds);
	
	@Modifying
	@Transactional
	@Query("delete from LockBundleParamPO po where po.bundleId = ?1")
	public int deleteByBundleId(String bundleId);
	
	
    @Modifying
    @Transactional
    @Query("delete from LockBundleParamPO po where po.bundleId in (?1)")
    void deleteBatchByBundleIds(Set<String> ids);
    
    @Modifying
    @Transactional
    @Query("delete from LockBundleParamPO po where po.bundleId in (?1)")
    void deleteBatchByBundleIds(List<String> ids);
	
}
