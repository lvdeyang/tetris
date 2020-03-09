package com.sumavision.tetris.resouce.event.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sumavision.tetris.resouce.event.ResourceUserImportFeign;
import com.sumavision.tetris.user.event.UserImportEvent;

@Service
@Transactional(rollbackFor = Exception.class)
public class ResourceUserImportListener implements ApplicationListener<UserImportEvent> {

	@Autowired
	private ResourceUserImportFeign userImportFeign;
	
	/**
	 * 用户导入事件监听<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月9日 下午3:05:03
	 */
	@Override
	public void onApplicationEvent(UserImportEvent event) {
		try {
			userImportFeign.userImport(event.getUserId(), event.getNickname(), event.getUserno());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
