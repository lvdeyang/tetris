package com.suma.venus.resource.dao;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.data.repository.query.Param;

import com.suma.venus.resource.pojo.ChannelTemplatePO;

@RepositoryDefinition(domainClass = ChannelTemplatePO.class, idClass = Long.class)
public interface ChannelTemplateDao extends CommonDao<ChannelTemplatePO>{

//	public List<ChannelTemplatePO> findByDeviceModelAndChannelVersion(String deviceModel,String channelVersion);
	public List<ChannelTemplatePO> findByIdIn(Collection<Long> templateIds);
	
	public List<ChannelTemplatePO> findByDeviceModel(String deviceModel);
	
	public List<ChannelTemplatePO> findByChannelName(String channelName);
	
	public ChannelTemplatePO findByDeviceModelAndChannelName(String deviceModel,String channelName);
	
	public List<ChannelTemplatePO> findByBaseType(String baseType);
	
	public List<ChannelTemplatePO> findByExternType(String externType);
	
	@Query(value="select s from ChannelTemplatePO s where s.deviceModel like %:keyword%")
	public List<ChannelTemplatePO> findByKeyword(@Param("keyword") String keyword);
	
	@Query(value="select t.deviceModel from ChannelTemplatePO t where 1=1")
	public Set<String> findAllDeviceModel();
	
	@Query(value="select t.channelName from ChannelTemplatePO t where t.deviceModel = ?1")
	public Set<String> findChannelNameByDeviceModel(String deviceModel);
}
