package com.suma.venus.resource.service;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.suma.venus.resource.dao.ExtraInfoDao;
import com.suma.venus.resource.pojo.ExtraInfoPO;

@Service
public class ExtraInfoService extends CommonService<ExtraInfoPO>{

	@Autowired
	private ExtraInfoDao extraInfoDao;
	
	public List<ExtraInfoPO> findByBundleId(String bundleId){
		
		return extraInfoDao.findByBundleId(bundleId);
	}
	
	public ExtraInfoPO findByBundleIdAndName(String bundleId,String name){
		return extraInfoDao.findByBundleIdAndName(bundleId, name);
	}
	
	public List<ExtraInfoPO> findByNameAndValue(String name,String value){
		return extraInfoDao.findByNameAndValue(name, value);
	}
	
	public Set<String> queryBundleIdByNameAndValue(String name,String value){
		return extraInfoDao.queryBundleIdByNameAndValue(name, value);
	}
	
	public Set<String> queryBundleIdByValueLike(String value){
		return extraInfoDao.queryBundleIdByValueLike(value);
	}
	
	public Set<String> queryNames(){
		return extraInfoDao.queryNames();
	}
	
	public int deleteByBundleId(String bundleId){
		return extraInfoDao.deleteByBundleId(bundleId);
	}
}
