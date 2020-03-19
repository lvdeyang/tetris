package com.sumavision.tetris.resouce.event.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sumavision.tetris.resouce.event.ResourceTouristFeign;
import com.sumavision.tetris.user.event.TouristDeleteEvent;

@Service
@Transactional(rollbackFor = Exception.class)
public class ResourceTouristDeleteListener implements ApplicationListener<TouristDeleteEvent>{

	@Autowired
	private ResourceTouristFeign resourceTouristFeign;
	
	@Override
	public void onApplicationEvent(TouristDeleteEvent event) {
		try {
			resourceTouristFeign.touristDelete(event.getUserId());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
