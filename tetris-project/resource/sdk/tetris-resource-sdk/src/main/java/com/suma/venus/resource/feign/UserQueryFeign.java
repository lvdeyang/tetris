package com.suma.venus.resource.feign;

import java.util.List;
import java.util.Map;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSONObject;
import com.suma.venus.resource.base.bo.CheckUserResultBO;
import com.suma.venus.resource.base.bo.ResourceIdListBO;
import com.suma.venus.resource.base.bo.ResultBO;
import com.suma.venus.resource.base.bo.RoleAndResourceIdBO;
import com.suma.venus.resource.base.bo.RoleBO;
import com.suma.venus.resource.base.bo.RoleResultBO;
import com.suma.venus.resource.base.bo.UnbindRolePrivilegeBO;
import com.suma.venus.resource.base.bo.UnbindUserPrivilegeBO;
import com.suma.venus.resource.base.bo.UserAndResourceIdBO;
import com.suma.venus.resource.base.bo.UserBO;
import com.suma.venus.resource.base.bo.UserPrivilegeBO;
import com.suma.venus.resource.base.bo.UserResultBO;

/**
 * 和用户权限服务通信的feign
 * 
 * @author lxw
 *
 */
@FeignClient(name = "suma-venus-user")
@RequestMapping("/")
public interface UserQueryFeign {

	@RequestMapping(value = "api/queryAllUserBaseInfo", method = RequestMethod.POST)
	public Map<String, List<UserBO>> queryUsers();

	@RequestMapping(value = "api/queryUsersByNameLike", method = RequestMethod.POST)
	public Map<String, List<UserBO>> queryUsersByNameLike(@RequestParam("username") String username);

	@RequestMapping(value = "api/queryUsersByNameLikeAndNoFolder", method = RequestMethod.POST)
	public Map<String, List<UserBO>> queryUsersByNameLikeAndNoFolder(@RequestParam("username") String username);

	@RequestMapping(value = "api/queryAllRoles", method = RequestMethod.POST)
	public Map<String, List<RoleBO>> queryRoles();

	@RequestMapping(value = "api/queryNonVirtualRoles", method = RequestMethod.POST)
	public Map<String, List<RoleBO>> queryNonVirtualRoles();

	@RequestMapping(value = "api/queryRolePrivilege", method = RequestMethod.POST)
	public ResourceIdListBO queryResourceByRoleId(@RequestParam("roleId") Long roleId);

	@RequestMapping(value = "api/queryUserBaseInfo", method = RequestMethod.POST)
	public Map<String, UserBO> queryUserInfo(@RequestParam("userName") String userName);

	@RequestMapping(value = "api/queryUserInfoById", method = RequestMethod.POST)
	public Map<String, UserBO> queryUserInfoById(@RequestParam("userId") Long userId);
	
	@RequestMapping(value = "api/queryUserListByIds", method = RequestMethod.POST)
	public Map<String, List<UserBO>> queryUserListByIds(@RequestParam("ids") String ids);

	@RequestMapping(value = "api/queryUserInfoByUserNo", method = RequestMethod.POST)
	public Map<String, UserBO> queryUserInfoByUserNo(@RequestParam("userNo") String userNo);

	@RequestMapping(value = "api/queryUserPrivilege", method = RequestMethod.POST)
	public ResourceIdListBO queryResourceByUserId(@RequestParam("userId") Long userId);

	@RequestMapping(value = "api/userHasPrivilegeOrNot", method = RequestMethod.POST)
	public UserPrivilegeBO hasPrivilege(@RequestBody UserAndResourceIdBO userIdAndResourceIds);

	//TODO
	@RequestMapping(value = "api/bindUserPrivilege", method = RequestMethod.POST)
	public ResultBO bindUserPrivilege(@RequestBody UserAndResourceIdBO userIdAndResourceIds);

	//TODO
	@RequestMapping(value = "api/unbindUserPrivilege", method = RequestMethod.POST)
	public ResultBO unbindUserPrivilege(@RequestBody UnbindUserPrivilegeBO unbindUserPrivilege);

	@RequestMapping(value = "api/bindRolePrivilege", method = RequestMethod.POST)
	public ResultBO bindRolePrivilege(@RequestBody RoleAndResourceIdBO roleIdAndResourceIds);

	@RequestMapping(value = "api/unbindRolePrivilege", method = RequestMethod.POST)
	public ResultBO unbindRolePrivilege(@RequestBody UnbindRolePrivilegeBO unbindRolePrivilege);

	@RequestMapping(value = "api/createVirtualUser", method = RequestMethod.POST)
	public UserResultBO createVirtualUser(@RequestParam(value = "userName") String userName, @RequestParam(value = "password") String password,
			@RequestParam(value = "beDevice") Boolean beDevice);

	@RequestMapping(value = "api/createVirtualRole", method = RequestMethod.POST)
	public RoleResultBO createVirtualRole(@RequestParam(value = "userName") String userName);

	@RequestMapping(value = "api/checkUserAndPassword", method = RequestMethod.POST)
	public CheckUserResultBO checkUserAndPassword(@RequestParam(value = "userName") String userName, @RequestParam(value = "password") String password,
			@RequestParam(value = "password_type") String password_type);

	@RequestMapping(value = "api/queryResourceId", method = RequestMethod.POST)
	public Map<String, String> queryResourceId(@RequestParam(value = "userName") String userName);

	@RequestMapping(value = "user/queryCurrentUser", method = RequestMethod.POST)
	public Map<String, UserBO> getUserByToken(@RequestHeader(value = "Authorization") String Authorization);

	@RequestMapping(value = "api/delVirtualUser", method = RequestMethod.POST)
	public ResultBO delVirtualUser(@RequestParam(value = "userName") String userName);

	/** 删除所有绑定该资源的权限 */
	@RequestMapping(value = "api/delbindPrivilege", method = RequestMethod.POST)
	public ResultBO delbindPrivilege(@RequestParam(value = "resourceCode") String resourceCode);

	@RequestMapping(value = "api/queryExtraInfo", method = RequestMethod.POST)
	public Map<String, Object> queryExtraInfo(@RequestParam(value = "userName") String userName);

	@RequestMapping(value = "api/queryBatchExtraInfoMap", method = RequestMethod.POST)
	public Map<String, Object> queryExtraInfoMap(@RequestParam(value = "userNameList") List<String> userName);

	@RequestMapping(value = "api/queryRoleByName", method = RequestMethod.POST)
	public Map<String, Object> queryRoleByName(@RequestParam(value = "roleName") String roleName);

	//TODO
	@RequestMapping(value = "api/queryUserWithEncoderDecoder", method = RequestMethod.POST)
	public Map<String, List<UserBO>> queryUserWithEncoderDecoder();

	//TODO
	@RequestMapping(value = "api/queryLdapUserWithEncoderDecoder", method = RequestMethod.POST)
	public Map<String, List<UserBO>> queryLdapUserWithEncoderDecoder();

	//TODO
	@RequestMapping(value = "api/queryLdapUserByUserNo", method = RequestMethod.POST)
	public Map<String, UserBO> queryLdapUserByUserNo(@RequestParam(value = "userNo") String userNo);

	//TODO
	@RequestMapping(value = "api/notifyUserStatus", method = RequestMethod.POST)
	public Map<String, Object> notifyUserStatus(@RequestBody JSONObject usersJson);

	//TODO
	@RequestMapping(value = "api/queryLocalUserStatus", method = RequestMethod.POST)
	public Map<String, List<UserBO>> queryLocalUserStatus();

	//TODO:用户服务重做
	@RequestMapping(value = "api/userHeartbeat", method = RequestMethod.POST)
	public Map<String, Object> userHeartbeat(@RequestParam("username") String username);

	// 查询绑定了某个角色的所有用户
	@RequestMapping(value = "api/queryUsersByRole", method = RequestMethod.POST)
	public Map<String, List<UserBO>> queryUsersByRole(@RequestParam("roleId") Long roleId);

	@RequestMapping(value = "api/queryUsersByFolderUuid", method = RequestMethod.POST)
	public Map<String, List<UserBO>> queryUsersByFolderUuid(@RequestParam("folderUuid") String folderUuid);

	@RequestMapping(value = "api/setFolderToUsers", method = RequestMethod.POST)
	public Map<String, Object> setFolderToUsers(@RequestParam("folderUuid") String folderUuid, @RequestParam("usernames") String usernames,
			@RequestParam("startIndex") Integer startIndex);

	@RequestMapping(value = "api/resetFolderOfUsers", method = RequestMethod.POST)
	public Map<String, Object> resetFolderOfUsers(@RequestParam("usernames") String usernames);

	//TODO
	@RequestMapping(value = "api/notifySyncLdap", method = RequestMethod.POST)
	public Map<String, Object> notifySyncLdap();

	@RequestMapping(value = "api/unbindEncoderAndDecoder", method = RequestMethod.POST)
	public Map<String, Object> unbindEncoderAndDecoder(@RequestParam("bundleId") String bundleId);
}
