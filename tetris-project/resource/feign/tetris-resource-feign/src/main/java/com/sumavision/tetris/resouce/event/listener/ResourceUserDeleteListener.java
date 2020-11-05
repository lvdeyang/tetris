package com.sumavision.tetris.resouce.event.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.sumavision.tetris.resouce.event.ResourceUserRegisteredFeign;
import com.sumavision.tetris.user.event.UserDeletedEvent;

@Service
@Transactional(rollbackFor = Exception.class)
public class ResourceUserDeleteListener implements ApplicationListener<UserDeletedEvent> {

	@Autowired
	private ResourceUserRegisteredFeign resourceUserRegisteredFeign;

	@Override
	public void onApplicationEvent(UserDeletedEvent event) {
		try {
			resourceUserRegisteredFeign.userDelete(JSON.toJSONString(event.getUsers()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
}
