package com.suma.venus.resource.dao;

import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;

import com.suma.venus.resource.pojo.VirtualResourcePO;

@RepositoryDefinition(domainClass=VirtualResourcePO.class,idClass=Long.class)
public interface VirtualResourceDao extends CommonDao<VirtualResourcePO>{
	
	public List<VirtualResourcePO> findByResourceId(String resourceId);
	
	public List<VirtualResourcePO> findByAttrNameAndAttrValue(String attrName,String attrValue);

	public VirtualResourcePO findTopByAttrNameAndAttrValue(String attrName,String attrValue);
	
	public VirtualResourcePO findByResourceIdAndAttrName(String resourceId,String attrName);
	
	@Query("select resource.resourceId from VirtualResourcePO resource where 1=1")
	public Set<String> queryAllResourceId();
	
	@Query("select resource.resourceId from VirtualResourcePO resource where resource.attrName=?1 and resource.attrValue=?2")
	public Set<String> queryResourceIdByAttrNameAndAttrValue(String attrName,String attrValue);
	
	@Query("select resource.attrValue from VirtualResourcePO resource where resource.attrName=?1")
	public Set<String> queryAttrValueByAttrName(String attrName);
	
	@Query("select resource.resourceId from VirtualResourcePO resource where resource.attrValue like %?1%")
	public Set<String> queryResourceIdByKeyword(String keyword);
	
	@Query("select resource.resourceId from VirtualResourcePO resource where resource.attrName='name' and resource.attrValue like %?1%")
	public Set<String> queryResourceIdByNameLike(String name);
	
	@Modifying
	@Transactional
	@Query("delete from VirtualResourcePO po where po.resourceId = ?1")
	public int deleteByResourceId(String resourceId);
	
	@Modifying
	@Transactional
	@Query("delete from VirtualResourcePO po where po.attrName = ?1 and po.attrValue = ?2")
	public int deleteByAttrNameAndAttrValue(String attrName,String attrValue);
	
	@Modifying
	@Transactional
	@Query("delete from VirtualResourcePO po where po.attrName = ?1 and po.resourceId = ?2")
	public int deleteByAttrNameAndResourceId(String attrName,String resourceId);
}
