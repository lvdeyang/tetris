package com.suma.venus.resource.event.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.suma.venus.resource.service.BundleService;
import com.sumavision.tetris.user.event.TouristDeleteBatchEvent;

@Service
@Transactional(rollbackFor = Exception.class)
public class TouristDeleteBatchListener implements ApplicationListener<TouristDeleteBatchEvent>{

	@Autowired
	private BundleService bundleService;
	
	@Override
	public void onApplicationEvent(TouristDeleteBatchEvent event) {
		try{
			bundleService.deleteByUserIdIn(event.getTouristIds());
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
