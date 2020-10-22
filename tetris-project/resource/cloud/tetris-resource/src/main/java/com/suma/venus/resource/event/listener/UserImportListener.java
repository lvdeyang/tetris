package com.suma.venus.resource.event.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

import com.suma.venus.resource.service.BundleService;
import com.suma.venus.resource.service.UserQueryService;
import com.sumavision.tetris.bvc.business.terminal.user.TerminalBundleUserPermissionService;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.user.event.UserImportEvent;

@Service
public class UserImportListener implements ApplicationListener<UserImportEvent>{

	@Autowired
	private BundleService bundleService;
	
	@Autowired
	private UserQueryService userQueryService;
	
	@Autowired
	private TerminalBundleUserPermissionService terminalBundleUserPermissionService;
	
	@Override
	public void onApplicationEvent(UserImportEvent event) {
		try{
			bundleService.createUserBundle(event.getUserId(), event.getNickname(), event.getUserno(),null);
			
			userQueryService.importUserPrivilage(event.getUserno(), event.getRoleIds());
			
			terminalBundleUserPermissionService.addAll(new ArrayListWrapper<String>().add(event.getUserId()).getList());
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
