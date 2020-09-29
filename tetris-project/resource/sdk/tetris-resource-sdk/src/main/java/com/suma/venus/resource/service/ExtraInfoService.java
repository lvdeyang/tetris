package com.suma.venus.resource.service;

import java.util.ArrayList;
import java.util.Collection;
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
	
	public List<ExtraInfoPO> findByBundleIdIn(Collection<String> bundleIds){		
		return extraInfoDao.findByBundleIdIn(bundleIds);
	}
	
	public List<ExtraInfoPO> findByWorknodeId(String worknodeId){		
		return extraInfoDao.findByWorknodeId(worknodeId);
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
	
	public int deleteByWorknodeId(String worknodeId){
		return extraInfoDao.deleteByWorknodeId(worknodeId);
	}
	
	public ExtraInfoPO queryExtraInfoByName(Collection<ExtraInfoPO> extraInfos, String name){
		if(name == null) return null;
		for(ExtraInfoPO extraInfo : extraInfos){
			if(name.equals(extraInfo.getName())){
				return extraInfo;
			}
		}
		return null;
	}
	
	public String queryExtraInfoValueByName(Collection<ExtraInfoPO> extraInfos, String name){
		ExtraInfoPO extraInfo = queryExtraInfoByName(extraInfos, name);
		if(extraInfo != null) return extraInfo.getValue();
		return null;
	}
	
	public List<ExtraInfoPO> queryExtraInfoBundleId(Collection<ExtraInfoPO> extraInfos, String bundleId){
		if(bundleId == null) return null;
		List<ExtraInfoPO> infos = new ArrayList<ExtraInfoPO>();
		for(ExtraInfoPO extraInfo : extraInfos){
			if(bundleId.equals(extraInfo.getBundleId())){
				infos.add(extraInfo);
			}
		}
		return infos;
	}
	
}
