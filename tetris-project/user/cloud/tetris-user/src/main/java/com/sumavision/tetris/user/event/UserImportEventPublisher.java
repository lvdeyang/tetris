package com.sumavision.tetris.user.event;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.context.ApplicationEventPublisher;

import com.sumavision.tetris.system.role.UserSystemRolePermissionPO;
import com.sumavision.tetris.user.UserPO;

public class UserImportEventPublisher implements Runnable{
	
	private ApplicationEventPublisher applicationEventPublisher;
	
	/** 导入的用户 */
	private List<UserPO> users;
	
	/** 用户角色绑定关系 */
	private List<UserSystemRolePermissionPO> userSystemRolePermissions;
	
	/** 用户总数 */
	private Long totalUsers;
	
	/** 当前用户数 */
	private Long currentNum;
	
	/** 公司id */
	private String companyId;
	
	/** 公司名称 */
	private String companyName;
	
	private ConcurrentHashMap<String, UserImportEventPublisher> cache;
	
	public UserImportEventPublisher(
			ApplicationEventPublisher applicationEventPublisher, 
			List<UserPO> users, 
			List<UserSystemRolePermissionPO> userSystemRolePermissions,
			String companyId, 
			String companyName,
			ConcurrentHashMap<String, UserImportEventPublisher> cache){
		this.applicationEventPublisher = applicationEventPublisher;
		this.users = users;
		this.userSystemRolePermissions = userSystemRolePermissions;
		this.companyId = companyId;
		this.companyName = companyName;
		this.cache = cache;
		this.totalUsers = Long.valueOf(users.size());
		this.currentNum = 0l;
	}

	@Override
	public void run() {
		for(UserPO user:users){
			this.currentNum += 1;
			List<String> roleIds = new ArrayList<String>();
			if(userSystemRolePermissions!=null && userSystemRolePermissions.size()>0){
				for(UserSystemRolePermissionPO permission:userSystemRolePermissions){
					if(permission.getUserId().equals(user.getId())){
						roleIds.add(permission.getRoleId().toString());
					}
				}
			}
			UserImportEvent event = new UserImportEvent(applicationEventPublisher, user.getId().toString(), user.getNickname(), user.getUserno(), this.companyId, this.companyName, roleIds);
			applicationEventPublisher.publishEvent(event);
		}
		this.cache.remove(this.companyId);
	}
	
	public void publish(){
		new Thread(this).start();
	}
	
	public Long getTotalUsers(){
		return this.totalUsers;
	}
	
	public Long getCurrentNum(){
		return this.currentNum;
	}
	
}
