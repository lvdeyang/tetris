package com.sumavision.tetris.resouce.event.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sumavision.tetris.resouce.event.ResourceUserRegisteredFeign;
import com.sumavision.tetris.user.event.UserRegisteredEvent;

/**
 * 用户注册事件监听<br/>
 * <b>作者:</b>wjw<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年1月6日 下午2:58:24
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ResourceUserRegisteredListener implements ApplicationListener<UserRegisteredEvent> {

	@Autowired
	private ResourceUserRegisteredFeign userRegisteredFeign;
	
	/**
	 * 用户注册事件监听<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年1月6日 下午3:05:03
	 */
	@Override
	public void onApplicationEvent(UserRegisteredEvent event){
		try {
			userRegisteredFeign.userRegistered(event.getUserId(), event.getNickname(), event.getUserno());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
