package com.suma.venus.resource.dao;


import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;

import com.suma.venus.resource.pojo.LockChannelParamPO;

@RepositoryDefinition(domainClass = LockChannelParamPO.class, idClass = Long.class)
public interface LockChannelParamDao extends CommonDao<LockChannelParamPO>{
	
	public List<LockChannelParamPO> findByBundleId(String bundleId);
	
	public List<LockChannelParamPO> findByBundleIdIn(Collection<String> bundleIds);
	
	public LockChannelParamPO findByBundleIdAndChannelId(String bundleId,String channelId);
	
	@Modifying
	@Transactional
	@Query("delete from LockChannelParamPO po where po.bundleId = ?1")
	public int deleteByBundleId(String bundleId);
	
	@Modifying
	@Transactional
	@Query("delete from LockChannelParamPO po where po.bundleId = ?1 and po.channelId = ?2")
	public int deleteByBundleIdAndChannelId(String bundleId,String channelId);
	
    @Modifying
    @Transactional
    @Query("delete from LockChannelParamPO po where po.bundleId in (?1)")
    void deleteBatchByBundleIds(Set<String> ids);
    
    @Modifying
    @Transactional
    @Query("delete from LockChannelParamPO po where po.bundleId in (?1)")
    void deleteBatchByBundleIds(List<String> ids);

	
}
