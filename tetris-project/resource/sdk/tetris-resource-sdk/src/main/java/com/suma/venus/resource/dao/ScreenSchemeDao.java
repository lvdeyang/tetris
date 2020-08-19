package com.suma.venus.resource.dao;

import java.util.Collection;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;

import com.suma.venus.resource.pojo.ChannelSchemePO.LockStatus;
import com.suma.venus.resource.pojo.BundlePO;
import com.suma.venus.resource.pojo.ScreenSchemePO;

@RepositoryDefinition(domainClass=ScreenSchemePO.class,idClass=Long.class)
public interface ScreenSchemeDao extends CommonDao<ScreenSchemePO>{

	public List<ScreenSchemePO> findByBundleId(String bundleId);
	
	public List<ScreenSchemePO> findByBundleIdIn(Collection<String> bundleIds);
	
	public ScreenSchemePO findByBundleIdAndScreenId(String bundleId,String screenId);

	public List<ScreenSchemePO> findByBundleIdAndStatus(String bundleId,LockStatus status);
	
	@Modifying
	@Transactional
	@Query("delete from ScreenSchemePO po where po.bundleId = ?1")
	public int deleteByBundleId(String bundleId);
	
}
