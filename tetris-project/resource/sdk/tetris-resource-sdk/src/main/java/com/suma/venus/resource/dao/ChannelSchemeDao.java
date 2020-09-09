package com.suma.venus.resource.dao;

import java.util.Collection;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;

import com.suma.venus.resource.pojo.ChannelSchemePO;
import com.suma.venus.resource.pojo.ChannelSchemePO.LockStatus;

@RepositoryDefinition(domainClass = ChannelSchemePO.class, idClass = Long.class)
public interface ChannelSchemeDao extends CommonDao<ChannelSchemePO>{
	
	public List<ChannelSchemePO> findByBundleId(String bundleId);
	
	public List<ChannelSchemePO> findByBundleIdIn(Collection<String> bundleIds);
	
	public List<ChannelSchemePO> findByBundleIdAndChannelTemplateID(String bundleId,Long channelTemplateID);
	
	public ChannelSchemePO findByBundleIdAndChannelId(String bundleId,String channelId);
	
	public List<ChannelSchemePO> findByChannelName(String channelName);
	
	public List<ChannelSchemePO> findByChannelTemplateID(Long channelTemplateID);
	
	public List<ChannelSchemePO> findByBundleIdAndChannelStatus(String bundleId,LockStatus channelStatus);
	
	@Query("select c.channelId from ChannelSchemePO c where c.bundleId=?1")
	public List<String> findChannelIdsByBundleId(String bundleId);

	@Query("select c from ChannelSchemePO c where c.bundleId=?1 and (c.channelName='VenusAudioIn' or c.channelName='VenusVideoIn')")
	public List<ChannelSchemePO> findEncodeChannelByBundleId(String bundleId);
	
	@Query("select c from ChannelSchemePO c where c.bundleId=?1 and (c.channelName='VenusAudioOut' or c.channelName='VenusVideoOut')")
	public List<ChannelSchemePO> findDecodeChannelByBundleId(String bundleId);
	
	@Modifying
	@Transactional
	@Query("delete from ChannelSchemePO po where po.bundleId = ?1")
	public int deleteByBundleId(String bundleId);
	
	@Modifying
	@Transactional
	@Query("delete from ChannelSchemePO po where po.bundleId = ?1 and po.channelId = ?2")
	public int deleteByBundleIdAndChannelId(String bundleId,String channelId);
	
	@Modifying
	@Transactional
	@Query("delete from ChannelSchemePO po where po.channelName = ?1")
	public int deleteByChannelName(String channelName);
	
}
