package com.sumavision.tetris.mims.app.group;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;

/**
 * 聊天窗口工具<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2018年11月21日 下午2:38:45
 */
//@Component
public class ChatQuery {
	
	//@Autowired
	private UserQuery userTool;
	
	//@Autowired
	private OrganizationDAO organizationDao;
	
	//@Autowired
	private OrganizationUserPermissionDAO organizationUserPermissionDao;
	
	/**
	 * 生成组织机构<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年11月21日 下午2:39:30
	 * @return List<GroupVO> 组织机构
	 */
	public List<GroupVO> generateOrganization(String groupId, String userId) throws Exception{
		
		List<UserVO> users = userTool.list(groupId);
		
		//过滤用户
		List<UserVO> filteredUsers = filterSelfUser(users, userId);
		
		//获取组织机构
		List<OrganizationPO> organizations = organizationDao.findByGroupIdOrderBySerialAsc(groupId);
		List<OrganizationUserPermissionPO> permissions = queryPermissions(filteredUsers);
		
		List<GroupVO> groups = new ArrayList<GroupVO>();
		
		//我自己
		GroupVO myself = myself();
		groups.add(myself);
		
		//添加我自己
		myself.getUsers().add(findMyself(users, userId));
		
		//添加角色
		for(int i=0; i<organizations.size(); i++){
			OrganizationPO organization = organizations.get(i);
			GroupVO group = new GroupVO().set(organization);
			groups.add(group);
		}
		
		//未分组的组
		GroupVO ungrouped = ungrouped();
		groups.add(ungrouped);
		
		//添加用户
		if(filteredUsers!=null && filteredUsers.size()>0){
			for(int i=0; i<filteredUsers.size(); i++){
				UserVO user = filteredUsers.get(i);
				OrganizationUserPermissionPO permission = null;
				for(OrganizationUserPermissionPO scope:permissions){
					if(scope.getUserId().equals(user.getUuid())){
						permission = scope;
						break;
					}
				}
				if(permission == null){
					ungrouped.getUsers().add(user);
				}else{
					for(GroupVO group:groups){
						if(group.getId().equals(permission.getOrganizationId())){
							if(group.getUsers() == null) group.setUsers(new ArrayList<UserVO>());
							group.getUsers().add(user);
							break;
						}
					}
				}
			}
		}
		
		return groups;
	}
	
	/**
	 * 未分组的组<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年11月21日 下午3:18:31
	 * @return GroupVO 未分组的组
	 */
	private GroupVO ungrouped(){
		return new GroupVO().setId(0l)
						    .setUuid("0")
						    .setUpdateTime(DateUtil.format(new Date(), DateUtil.dateTimePattern))
							 .setName("未分组")
							 .setRemoveable(false)
							 .setUsers(new ArrayList<UserVO>());
	}
	
	/**
	 * 我自己的组<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月6日 下午4:50:25
	 * @return GroupVO 我自己的组
	 */
	private GroupVO myself(){
		return new GroupVO().setId(-1l)
							.setUuid("-1")
							.setUpdateTime(DateUtil.format(new Date(), DateUtil.dateTimePattern))
							.setName("我自己")
							.setRemoveable(false)
							.setUsers(new ArrayList<UserVO>());
	}
	
	/**
	 * 按照userId查找用户<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月6日 下午5:01:08
	 * @param List<UserVO> users 查找范围
	 * @param String userId 查找对象
	 * @return UserVO 查找结果
	 */
	private UserVO findMyself(List<UserVO> users, String userId) throws Exception{
		if(users!=null && users.size()>0){
			for(int i=0; i<users.size(); i++){
				if(users.get(i).getUuid().equals(userId)){
					return users.get(i);
				}
			}
		}
		return null;
	}
	
	/**
	 * 用户列表过滤自己<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年11月21日 下午3:02:36
	 * @param String userId 当前登录用户id
	 * @return List<UserVO> 过滤后的用户列表
	 */
	private List<UserVO> filterSelfUser(List<UserVO> users, String userId) throws Exception{
		List<UserVO> filtered = new ArrayList<UserVO>();
		if(users!=null && users.size()>0){
			for(int i=0; i<users.size(); i++){
				if(!users.get(i).getUuid().equals(userId)){
					filtered.add(users.get(i));
				}
			}
		}
		return filtered;
	}
	
	/**
	 * 获取用户组织机构映射关系<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年11月21日 下午3:38:42
	 * @param List<UserVO> users 用户列表
	 * @return List<RoleUserPermissionPO> 用户角色关系列表
	 */
	private List<OrganizationUserPermissionPO> queryPermissions(List<UserVO> users) throws Exception{
		if(users!=null && users.size()>0){
			Set<String> userIds = new HashSet<String>();
			for(int i=0; i<users.size(); i++){
				userIds.add(users.get(i).getUuid());
			}
			return organizationUserPermissionDao.findByUserIdIn(userIds);
		}
		return null;
	}
	
}
