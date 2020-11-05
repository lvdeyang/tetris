package com.sumavision.tetris.mims.event.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sumavision.tetris.business.role.event.BusinessRoleRemovedEvent;
import com.sumavision.tetris.mims.app.folder.FolderRolePermissionService;

/**
 * 业务角色删除事件<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年1月25日 下午4:02:30
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class BusinessRoleRemovedListener implements ApplicationListener<BusinessRoleRemovedEvent>{

	@Autowired
	private FolderRolePermissionService folderRolePermissionService;
	
	@Override
	public void onApplicationEvent(BusinessRoleRemovedEvent event) {
		try {
			folderRolePermissionService.deletePermissionByRoleId(event.getRoleId());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
}
