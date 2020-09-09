package com.sumavision.tetris.mims.event.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sumavision.tetris.mims.event.UserRegisteredFeign;
import com.sumavision.tetris.user.event.UserRegisteredEvent;

/**
 * 用户注册事件监听<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年1月25日 下午4:02:30
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class UserRegisteredListener implements ApplicationListener<UserRegisteredEvent> {

	@Autowired
	private UserRegisteredFeign userRegisteredFeign;
	
	/**
	 * 用户注册事件监听<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月17日 下午1:05:03
	 */
	@Override
	public void onApplicationEvent(UserRegisteredEvent event){
		try {
			userRegisteredFeign.userRegistered(event.getUserId(), event.getNickname(), event.getCompanyId(), event.getCompanyName(), event.getRoleId(), event.getRoleName());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
