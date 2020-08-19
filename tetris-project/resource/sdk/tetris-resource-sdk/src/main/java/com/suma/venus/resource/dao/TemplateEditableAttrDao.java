package com.suma.venus.resource.dao;

import java.util.List;

import org.springframework.data.repository.RepositoryDefinition;

import com.suma.venus.resource.pojo.TemplateEditableAttrPO;

@RepositoryDefinition(domainClass = TemplateEditableAttrPO.class, idClass = Long.class)
public interface TemplateEditableAttrDao extends CommonDao<TemplateEditableAttrPO>{

	public List<TemplateEditableAttrPO> findByDeviceModel(String deviceModel);
	
	public TemplateEditableAttrPO findByDeviceModelAndName(String deviceModel,String name);
	
}
