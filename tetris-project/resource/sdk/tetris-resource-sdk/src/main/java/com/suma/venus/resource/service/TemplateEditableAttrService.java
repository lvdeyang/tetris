package com.suma.venus.resource.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.suma.venus.resource.dao.TemplateEditableAttrDao;
import com.suma.venus.resource.pojo.TemplateEditableAttrPO;

@Service
public class TemplateEditableAttrService extends CommonService<TemplateEditableAttrPO>{

	@Autowired
	private TemplateEditableAttrDao templateEditableAttrDao;

	public List<TemplateEditableAttrPO> findByDeviceModel(String deviceModel){
		return templateEditableAttrDao.findByDeviceModel(deviceModel);
	}
	
	public TemplateEditableAttrPO findByDeviceModelAndName(String deviceModel,String name){
		return templateEditableAttrDao.findByDeviceModelAndName(deviceModel, name);
	}

}
