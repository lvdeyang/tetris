package com.suma.venus.resource.event.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.suma.venus.resource.service.BundleService;
import com.sumavision.tetris.user.event.TouristCreateEvent;

@Service
@Transactional(rollbackFor = Exception.class)
public class TouristCreateListener implements ApplicationListener<TouristCreateEvent>{

	@Autowired
	private BundleService bundleService;
	
	@Override
	public void onApplicationEvent(TouristCreateEvent event) {
		try{
			bundleService.createTouristBundle(event.getUserId(), event.getNickname(), event.getUserno());
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
