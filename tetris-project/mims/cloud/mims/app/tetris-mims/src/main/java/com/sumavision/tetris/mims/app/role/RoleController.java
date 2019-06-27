package com.sumavision.tetris.mims.app.role;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sumavision.tetris.mims.app.folder.FolderDAO;
import com.sumavision.tetris.mims.app.folder.FolderPO;
import com.sumavision.tetris.mims.app.folder.FolderRolePermissionDAO;
import com.sumavision.tetris.mims.app.folder.FolderRolePermissionPO;
import com.sumavision.tetris.mims.app.folder.exception.FolderNotExistException;
//import com.sumavision.tetris.mims.app.role.exception.RoleNotExistException;
//import com.sumavision.tetris.mims.app.role.exception.UserHasNoPermissionForRoleException;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.subordinate.role.SubordinateRoleQuery;
import com.sumavision.tetris.subordinate.role.SubordinateRoleService;
import com.sumavision.tetris.subordinate.role.SubordinateRoleVO;
import com.sumavision.tetris.user.UserClassify;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;

@Controller
@RequestMapping(value = "/role")
public class RoleController {
	@Autowired
	private UserQuery userTool;
	
	@Autowired
	private FolderDAO folderDao;
	
	@Autowired
	private FolderRolePermissionDAO folderRolePermissionDao;
	
	@Autowired
	SubordinateRoleQuery subordinateRoleQuery;
	
	@Autowired
	SubordinateRoleService subordinateRoleService;
	/**
	 * 查询所有的角色<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月8日 下午4:35:33
	 * @return List<RoleVO> 角色列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/list")
	public Object list(HttpServletRequest request) throws Exception{
		
		UserVO user = userTool.current();
		
		if(!UserClassify.COMPANY_ADMIN.equals(UserClassify.valueOf(user.getClassify()))){
			//throw new UserHasNoPermissionForRoleException(user.getUuid());
		}
		
		if(user.getGroupId() == null){
			//throw new UserHasNoPermissionForRoleException(user.getUuid());
		}
		
		List<SubordinateRoleVO> roles = subordinateRoleQuery.queryRolesByCompany(user.getGroupId());
		
		return roles;
	}
	
	
	/**
	 * 查询所有角色（例外：文件夹授权）<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月11日 下午2:42:15
	 * @param Long folderId 文件夹id
	 * @return List<RoleVO> view_roles 角色列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/list/except/folder/permission")
	public Object listExceptFolderPermission(
			Long folderId,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userTool.current();
		
//		if(!UserClassify.COMPANY_ADMIN.equals(UserClassify.valueOf(user.getClassify()))){
//			//throw new UserHasNoPermissionForRoleException(user.getUuid());
//		}
		
		if(user.getGroupId() == null){
			//throw new UserHasNoPermissionForRoleException(user.getUuid());
		}
		
		FolderPO folder = folderDao.findOne(folderId);
		
		if(folder == null){
			throw new FolderNotExistException(folderId);
		}
		
		List<FolderRolePermissionPO> permissions = folderRolePermissionDao.findByFolderId(folder.getId());
		
		Set<Long> exceptRoleIds = new HashSet<Long>();
		if(permissions!=null && permissions.size()>0){
			for(FolderRolePermissionPO permission:permissions){
				exceptRoleIds.add(permission.getRoleId());
			}
		}
		
		List<SubordinateRoleVO> roles = subordinateRoleQuery.queryRolesByCompany(user.getGroupId());
		
		List<SubordinateRoleVO> filtered = new ArrayList<SubordinateRoleVO>();
		
		for (int i = 0; i < roles.size(); i++) {
			SubordinateRoleVO role = roles.get(i);
			if (!(role == null)) {
				if(!exceptRoleIds.contains(role.getId())){
					filtered.add(role);
				}
			}	
		}
		
		return filtered;
	}
	
	/**
	 * 添加角色<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月8日 下午6:18:09
	 * @param String name 角色名称
	 * @return RoleVO 角色数据
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add")
	public Object add(
			String name, 
			HttpServletRequest request) throws Exception{
		
		UserVO user = userTool.current();
		
//		if(!UserClassify.COMPANY_ADMIN.equals(UserClassify.valueOf(user.getClassify()))){
//			//throw new UserHasNoPermissionForRoleException(user.getUuid());
//		}
		
		if(user.getGroupId() == null){
			//throw new UserHasNoPermissionForRoleException(user.getUuid());
		}
		Date date = new Date();
		
		SubordinateRoleVO vo = subordinateRoleService.addRole(user.getId(), Long.parseLong(user.getGroupId()), name, date.toString(), "true", "0");
		
		return vo;
	}
	
	/**
	 * 修改角色<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月8日 下午7:03:12
	 * @param @PathVariable Long id 角色id
	 * @param String name 角色名称
	 * @return RoleVO 角色数据
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/edit/{id}")
	public Object edit(
			@PathVariable Long id,
			String name,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userTool.current();
		
		if(!UserClassify.COMPANY_ADMIN.equals(UserClassify.valueOf(user.getClassify()))){
			//throw new UserHasNoPermissionForRoleException(user.getUuid());
		}
		
		if(user.getGroupId() == null){
			//throw new UserHasNoPermissionForRoleException(user.getUuid());
		}
		
		SubordinateRoleVO role = subordinateRoleQuery.queryRoleById(id);
		
		if(role == null){
			//throw new RoleNotExistException(id);
		}
		
//		if(UserClassify.COMPANY_ADMIN.equals(role.getClassify())){
//			throw new UserHasNoPermissionForRoleException(user.getUuid());
//		}
		
		if(!role.getCompanyId().toString().equals(user.getGroupId())){
			//throw new UserHasNoPermissionForRoleException(user.getUuid());
		}
		
		SubordinateRoleVO vo = subordinateRoleService.editRole(id, name);
	
		return vo;
	}
	
	/**
	 * 删除角色<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月8日 下午7:10:41
	 * @param @PathVariable Long id 角色id
	 * @return RoleVO 删除的角色
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/delet/{id}")
	public Object delet(
			@PathVariable Long id,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userTool.current();
		
//		if(!UserClassify.COMPANY_ADMIN.equals(UserClassify.valueOf(user.getClassify()))){
//			//throw new UserHasNoPermissionForRoleException(user.getUuid());
//		}
		
		if(user.getGroupId() == null){
			//throw new UserHasNoPermissionForRoleException(user.getUuid());
		}
		
		SubordinateRoleVO role = subordinateRoleQuery.queryRoleById(id);
		
		if(role == null){
			//throw new RoleNotExistException(id);
		}
		
//		if(UserClassify.COMPANY_ADMIN.equals(role.getClassify())){
//			throw new UserHasNoPermissionForRoleException(user.getUuid());
//		}
		
		if(!role.getCompanyId().toString().equals(user.getGroupId())){
			//throw new UserHasNoPermissionForRoleException(user.getUuid());
		}
		
		SubordinateRoleVO vo = subordinateRoleService.removeRole(id);
		folderRolePermissionDao.deleteInBatch(folderRolePermissionDao.findByRoleId(id)); 
		return vo;
	}
	
//	/**
//	 * 获取角色绑定用户<br/>
//	 * <b>作者:</b>lvdeyang<br/>
//	 * <b>版本：</b>1.0<br/>
//	 * <b>日期：</b>2018年12月10日 上午9:40:25
//	 * @param @PathVariable Long id 角色id
//	 * @return Integer total 总数据量
//	 * @return List<UserVO> users 绑定用户
//	 */
//	@JsonBody
//	@ResponseBody
//	@RequestMapping(value = "/get/binding/users/{id}")
//	public Object getBindingUsers(
//			@PathVariable Long id,
//			Integer pageSize,
//			Integer currentPage,
//			HttpServletRequest request) throws Exception{
//		
//		UserVO user = userTool.current();
//		
//		if(!UserClassify.COMPANY_ADMIN.equals(UserClassify.valueOf(user.getClassify()))){
//			throw new UserHasNoPermissionForRoleException(user.getUuid());
//		}
//		
//		if(user.getGroupId() == null){
//			throw new UserHasNoPermissionForRoleException(user.getUuid());
//		}
//		
//		RolePO role = roleDao.findOne(id);
//		
//		if(role == null){
//			throw new RoleNotExistException(id);
//		}	
//		
//		List<RoleUserPermissionPO> permissions =  roleUserPermissionDao.findByRoleId(role.getId());
//		
//		List<UserVO> users = null;
//		int total = 0;
//		
//		if(permissions!=null && permissions.size()>0){
//			Set<String> userIds = new HashSet<String>();
//			for(RoleUserPermissionPO permission:permissions){
//				userIds.add(permission.getUserId());
//			}
//			users = userTool.find(userIds, pageSize, currentPage);
//			total = userIds.size();
//		}
//		
//		Map<String, Object> result = new HashMapWrapper<String, Object>().put("total", total)
//																		 .put("users", users)
//																		 .getMap();
//		
//		return result;
//	}
//	
//	/**
//	 * 用户角色解绑<br/>
//	 * <b>作者:</b>lvdeyang<br/>
//	 * <b>版本：</b>1.0<br/>
//	 * <b>日期：</b>2018年12月10日 上午10:52:05
//	 * @param Long roleId 角色id
//	 * @param String userId 用户id
//	 */
//	@JsonBody
//	@ResponseBody
//	@RequestMapping(value = "/user/unbinding")
//	public Object userUnbinding(
//			Long roleId,
//			String userId,
//			HttpServletRequest request) throws Exception{
//		
//		UserVO user = userTool.current();
//		
//		if(!UserClassify.COMPANY_ADMIN.equals(UserClassify.valueOf(user.getClassify()))){
//			throw new UserHasNoPermissionForRoleException(user.getUuid());
//		}
//		
//		if(user.getGroupId() == null){
//			throw new UserHasNoPermissionForRoleException(user.getUuid());
//		}
//		
//		RolePO role = roleDao.findOne(roleId);
//		
//		if(role == null){
//			throw new RoleNotExistException(roleId);
//		}	
//		
//		List<RoleUserPermissionPO> permissions = roleUserPermissionDao.findByRoleIdAndUserId(roleId, userId);
//		if(permissions!=null && permissions.size()>0){
//			roleUserPermissionDao.deleteInBatch(permissions);
//		}
//		
//		return null;
//	}
//	
//	/**
//	 * 用户绑定角色<br/>
//	 * <b>作者:</b>lvdeyang<br/>
//	 * <b>版本：</b>1.0<br/>
//	 * <b>日期：</b>2018年12月10日 下午3:06:12
//	 * @param Long roleId 角色id
//	 * @param String userIds json数据：用户id列表
//	 * @return List<UserVO> 绑定的用户
//	 */
//	@JsonBody
//	@ResponseBody
//	@RequestMapping(value = "/user/binding")
//	public Object userBinding(
//			Long roleId,
//			String userIds,
//			HttpServletRequest request) throws Exception{
//		
//		UserVO user = userTool.current();
//		
//		if(!UserClassify.COMPANY_ADMIN.equals(UserClassify.valueOf(user.getClassify()))){
//			throw new UserHasNoPermissionForRoleException(user.getUuid());
//		}
//		
//		if(user.getGroupId() == null){
//			throw new UserHasNoPermissionForRoleException(user.getUuid());
//		}
//		
//		RolePO role = roleDao.findOne(roleId);
//		
//		if(role == null){
//			throw new RoleNotExistException(roleId);
//		}	
//		
//		List<String> parsedUserIds = JSON.parseArray(userIds, String.class);
//		
//		roleService.userBinding(role, parsedUserIds);
//		
//		List<UserVO> users = userTool.find(parsedUserIds);
//		
//		return users;
//	}
	
}
