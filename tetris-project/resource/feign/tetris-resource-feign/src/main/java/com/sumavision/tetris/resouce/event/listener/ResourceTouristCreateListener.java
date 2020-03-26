package com.sumavision.tetris.resouce.event.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sumavision.tetris.resouce.event.ResourceTouristFeign;
import com.sumavision.tetris.user.event.TouristCreateEvent;

@Service
@Transactional(rollbackFor = Exception.class)
public class ResourceTouristCreateListener implements ApplicationListener<TouristCreateEvent>{

	@Autowired
	private ResourceTouristFeign resourceTouristFeign;
	
	@Override
	public void onApplicationEvent(TouristCreateEvent event) {
		try {
			resourceTouristFeign.touristCreate(event.getUserId(), event.getNickname(), event.getUserno());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
