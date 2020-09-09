package com.sumavision.tetris.resouce.event.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.sumavision.tetris.resouce.event.ResourceTouristFeign;
import com.sumavision.tetris.user.event.TouristDeleteBatchEvent;

@Service
@Transactional(rollbackFor = Exception.class)
public class ResourceTouristDeleteBatchjListener implements ApplicationListener<TouristDeleteBatchEvent>{

	@Autowired
	private ResourceTouristFeign resourceTouristFeign;
	
	@Override
	public void onApplicationEvent(TouristDeleteBatchEvent event) {
		try {
			resourceTouristFeign.touristDeleteBatch(JSON.toJSONString(event.getTouristIds()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
