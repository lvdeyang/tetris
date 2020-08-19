package com.sumavision.tetris.mims.event.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sumavision.tetris.mims.app.folder.FolderService;
import com.sumavision.tetris.user.event.UserImportEvent;

/**
 * 用户导入事件监听<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年1月25日 下午4:02:30
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class UserImportListener implements ApplicationListener<UserImportEvent> {

	@Autowired
	private FolderService folderService;

	/**
	 * 用户导入事件监听<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月17日 下午1:05:03
	 */
	@Override
	public void onApplicationEvent(UserImportEvent event) {
		try{
			//注册个人网盘--这个地方先不实现了
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
}
