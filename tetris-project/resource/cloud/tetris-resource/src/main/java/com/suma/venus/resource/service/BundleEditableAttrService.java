package com.suma.venus.resource.service;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.suma.venus.resource.dao.BundleEditableAttrDao;
import com.suma.venus.resource.pojo.BundleEditableAttrPO;

@Service
public class BundleEditableAttrService extends CommonService<BundleEditableAttrPO>{

	@Autowired
	private BundleEditableAttrDao bundleEditableAttrDao;
	
	public List<BundleEditableAttrPO> findByBundleId(String bundleId){
		return bundleEditableAttrDao.findByBundleId(bundleId);
	}
	
	public BundleEditableAttrPO findByBundleIdAndName(String bundleId,String name){
		return bundleEditableAttrDao.findByBundleIdAndName(bundleId, name);
	}
	
	public Set<String> findBundleIdByNameAndValue(String name,String value){
		return bundleEditableAttrDao.findBundleIdByNameAndValue(name, value);
	}
}
