package com.suma.venus.resource.event.listener;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.suma.venus.resource.service.BundleService;
import com.suma.venus.resource.service.UserQueryService;
import com.sumavision.tetris.user.event.UserImportEvent;

@Service
@Transactional(rollbackFor = Exception.class)
public class UserImportListener implements ApplicationListener<UserImportEvent>{

	@Autowired
	private BundleService bundleService;
	
	@Autowired
	private UserQueryService userQueryService;
	
	@Override
	public void onApplicationEvent(UserImportEvent event) {
		try{
			bundleService.createUserBundle(event.getUserId(), event.getNickname(), event.getUserno(),null);
			
			userQueryService.importUserPrivilage(event.getUserno(), event.getRoleIds());
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
