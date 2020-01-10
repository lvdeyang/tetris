package com.suma.venus.resource.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.suma.venus.resource.dao.LockChannelParamDao;
import com.suma.venus.resource.pojo.LockChannelParamPO;

@Service
public class LockChannelParamService extends CommonService<LockChannelParamPO>{

	@Autowired
	private LockChannelParamDao lockChannelParamDao;
	
	public LockChannelParamPO findByBundleIdAndChannelId(String bundleId,String channelId){
		return lockChannelParamDao.findByBundleIdAndChannelId(bundleId, channelId);
	}
	
	public int deleteByBundleId(String bundleId){
		return lockChannelParamDao.deleteByBundleId(bundleId);
	}
	
	
	public int deleteByBundleIdAndChannelId(String bundleId,String channelId){
		return lockChannelParamDao.deleteByBundleIdAndChannelId(bundleId, channelId);
	}
	
}
