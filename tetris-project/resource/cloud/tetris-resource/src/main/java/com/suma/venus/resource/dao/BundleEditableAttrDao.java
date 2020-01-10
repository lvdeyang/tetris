package com.suma.venus.resource.dao;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;

import com.suma.venus.resource.pojo.BundleEditableAttrPO;

@RepositoryDefinition(domainClass = BundleEditableAttrPO.class, idClass = Long.class)
public interface BundleEditableAttrDao extends CommonDao<BundleEditableAttrPO>{

	public List<BundleEditableAttrPO> findByBundleId(String bundleId);
	
	public BundleEditableAttrPO findByBundleIdAndName(String bundleId,String name);
	
	@Query("select attr.bundleId from BundleEditableAttrPO attr where attr.name=?1 and attr.value=?2")
	public Set<String> findBundleIdByNameAndValue(String name,String value);
	
}
