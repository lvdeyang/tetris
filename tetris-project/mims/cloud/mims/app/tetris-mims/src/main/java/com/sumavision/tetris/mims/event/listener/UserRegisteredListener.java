package com.sumavision.tetris.mims.event.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sumavision.tetris.mims.app.folder.FolderService;
import com.sumavision.tetris.user.event.UserRegisteredEvent;

/**
 * 用户注册事件<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年1月25日 下午4:02:30
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class UserRegisteredListener implements ApplicationListener<UserRegisteredEvent> {

	@Autowired
	private FolderService folderService;
	
	@Override
	public void onApplicationEvent(UserRegisteredEvent event){
		try{
			if(event.getCompanyId() != null&&event.getRoleId() != null){
				//注册企业网盘
				folderService.createCompanyDisk(event.getCompanyId(), event.getCompanyName(), event.getUserId(), event.getRoleId(), event.getRoleName());
			}else if(event.getCompanyId() != null&&event.getRoleId() == null){
				//普通用户加入组织
			}
			//注册个人网盘
			folderService.createPersonalDisk(event.getUserId(), event.getNickname());
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}

}
