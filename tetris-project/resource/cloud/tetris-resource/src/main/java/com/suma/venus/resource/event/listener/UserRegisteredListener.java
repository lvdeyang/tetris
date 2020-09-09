package com.suma.venus.resource.event.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.suma.venus.resource.service.BundleService;
import com.sumavision.tetris.user.event.UserRegisteredEvent;

/**
 * 用户注册事件<br/>
 * <b>作者:</b>wjw<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年1月6日 下午4:02:30
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class UserRegisteredListener implements ApplicationListener<UserRegisteredEvent> {
	
	@Autowired
	private BundleService bundleService;

	@Override
	public void onApplicationEvent(UserRegisteredEvent event){
		try{
			bundleService.createUserBundle(event.getUserId(), event.getNickname(), event.getUserno());
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
