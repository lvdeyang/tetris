package com.suma.venus.resource.event.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.suma.venus.resource.service.BundleService;
import com.sumavision.tetris.user.event.UserDeletedEvent;

@Service
@Transactional(rollbackFor = Exception.class)
public class UserDeleteListener implements ApplicationListener<UserDeletedEvent>{
	
	@Autowired
	private BundleService bundleService;

	@Override
	public void onApplicationEvent(UserDeletedEvent event) {
		try {
			bundleService.removeUser(event.getUsers());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
