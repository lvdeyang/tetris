package com.suma.venus.resource.event.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.suma.venus.resource.service.BundleService;
import com.sumavision.tetris.user.event.TouristDeleteEvent;

@Service
@Transactional(rollbackFor = Exception.class)
public class TouristDeleteListener implements ApplicationListener<TouristDeleteEvent>{

	@Autowired
	private BundleService bundleService;
	
	@Override
	public void onApplicationEvent(TouristDeleteEvent event) {
		try{
			bundleService.deleteByUserId(Long.valueOf(event.getUserId()));
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
