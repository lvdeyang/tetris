package com.suma.venus.resource.dao;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;

import com.suma.venus.resource.pojo.LockScreenParamPO;

@RepositoryDefinition(domainClass=LockScreenParamPO.class,idClass=Long.class)
public interface LockScreenParamDao extends CommonDao<LockScreenParamPO>{
	
	public LockScreenParamPO findByBundleIdAndScreenId(String bundleId,String screenId);
	
	public List<LockScreenParamPO> findByBundleIdIn(Collection<String> bundleIds);

	
	@Modifying
	@Transactional
	@Query("delete from LockScreenParamPO po where po.bundleId = ?1")
	public int deleteByBundleId(String bundleId);

	@Modifying
	@Transactional
	@Query("delete from LockScreenParamPO po where po.bundleId = ?1 and po.screenId = ?2")
	public int deleteByBundleIdAndScreenId(String bundleId,String screenId);
	
	
    @Modifying
    @Transactional
    @Query("delete from LockScreenParamPO po where po.bundleId in (?1)")
    void deleteBatchByBundleIds(Set<String> ids);
    
    @Modifying
    @Transactional
    @Query("delete from LockScreenParamPO po where po.bundleId in (?1)")
    void deleteBatchByBundleIds(List<String> ids);
	
	
}
