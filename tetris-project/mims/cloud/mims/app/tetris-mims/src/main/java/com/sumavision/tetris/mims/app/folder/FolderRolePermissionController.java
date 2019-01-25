package com.sumavision.tetris.mims.app.folder;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.sumavision.tetris.mims.app.folder.exception.FolderNotExistException;
import com.sumavision.tetris.mims.app.folder.exception.UserHasNoPermissionForFolderException;
import com.sumavision.tetris.mims.app.role.RoleDAO;
import com.sumavision.tetris.mims.app.role.RolePO;
import com.sumavision.tetris.mims.app.role.RoleVO;
import com.sumavision.tetris.mims.app.role.exception.RoleNotExistException;
import com.sumavision.tetris.mims.app.role.exception.UserHasNoPermissionForRoleException;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.user.UserClassify;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;

@Controller
@RequestMapping(value = "/folder/role/permission")
public class FolderRolePermissionController {

	@Autowired
	private UserQuery userTool;
	
	@Autowired
	private FolderDAO folderDao;
	
	@Autowired
	private FolderRolePermissionDAO folderRolePermissionDao;
	
	@Autowired
	private RoleDAO roleDao;
	
	@Autowired
	private FolderQuery folderTool;
	
	@Autowired
	private FolderRolePermissionService folderRolePermissionService;
	
	/**
	 * 获取文件夹授权情况<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月11日 下午1:40:17
	 * @param Long folderId 文件夹id
	 * @return List<RoleVO> 授权情况
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/list")
	public Object list(
			Long folderId,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userTool.current();
		
		if(!UserClassify.COMPANY_ADMIN.equals(UserClassify.valueOf(user.getClassify()))){
			throw new UserHasNoPermissionForFolderException(UserHasNoPermissionForFolderException.NOPERMISSION);
		}
		
		if(user.getGroupId() == null){
			throw new UserHasNoPermissionForFolderException(UserHasNoPermissionForFolderException.NOPERMISSION);
		}
		
		FolderPO folder = folderDao.findOne(folderId);
		
		if(folder == null){
			throw new FolderNotExistException(folderId);
		}
		
		List<FolderRolePermissionPO> permissions = folderRolePermissionDao.findByFolderId(folderId);
		
		if(permissions!=null && permissions.size()>0){
			Set<Long> roleIds = new HashSet<Long>();
			for(FolderRolePermissionPO permission:permissions){
				roleIds.add(permission.getRoleId());
			}
			List<RolePO> roles = roleDao.findAll(roleIds);
			List<RoleVO> view_roles = RoleVO.getConverter(RoleVO.class).convert(roles, RoleVO.class);
			
			return view_roles;
		}
		
		return null;
	}
	
	/**
	 * 删除授权<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月11日 下午3:14:47
	 * @param Long folderId 文件夹id
	 * @param Long roleId 角色id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/delete")
	public Object delete(
			Long folderId,
			Long roleId,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userTool.current();
		
		if(!UserClassify.COMPANY_ADMIN.equals(UserClassify.valueOf(user.getClassify()))){
			throw new UserHasNoPermissionForFolderException(UserHasNoPermissionForFolderException.NOPERMISSION);
		}
		
		if(user.getGroupId() == null){
			throw new UserHasNoPermissionForFolderException(UserHasNoPermissionForFolderException.NOPERMISSION);
		}
		
		FolderPO folder = folderDao.findOne(folderId);
		
		if(folder == null){
			throw new FolderNotExistException(folderId);
		}
		
		if(!folderTool.hasGroupPermission(user.getGroupId(), folderId)){
			throw new UserHasNoPermissionForFolderException(UserHasNoPermissionForFolderException.CURRENT);
		}
		
		RolePO role = roleDao.findOne(roleId);
		if(role == null){
			throw new RoleNotExistException(roleId);
		}
		
		if(!user.getGroupId().equals(role.getGroupId())){
			throw new UserHasNoPermissionForRoleException(user.getUuid(), roleId);
		}
		
		folderRolePermissionService.deletePermission(folder, role);
		
		return null;
	}
	
	/**
	 * 文件夹授权<br/>
	 * <p>文件夹的所有子文件夹也会授权</p>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月11日 下午4:09:35
	 * @param Long folderId 文件夹id
	 * @param String roleIds JSON数据，角色id列表
	 * @return List<RoleVO> 授权的角色列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add")
	public Object add(
			Long folderId,
			String roleIds,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userTool.current();
		
		if(!UserClassify.COMPANY_ADMIN.equals(UserClassify.valueOf(user.getClassify()))){
			throw new UserHasNoPermissionForFolderException(UserHasNoPermissionForFolderException.NOPERMISSION);
		}
		
		if(user.getGroupId() == null){
			throw new UserHasNoPermissionForFolderException(UserHasNoPermissionForFolderException.NOPERMISSION);
		}
		
		FolderPO folder = folderDao.findOne(folderId);
		
		if(folder == null){
			throw new FolderNotExistException(folderId);
		}
		
		if(!folderTool.hasGroupPermission(user.getGroupId(), folderId)){
			throw new UserHasNoPermissionForFolderException(UserHasNoPermissionForFolderException.CURRENT);
		}
		
		List<Long> parsedRoleIds = JSON.parseArray(roleIds, Long.class);
		
		List<RolePO> roles = folderRolePermissionService.addPermission(user.getGroupId(), folder, parsedRoleIds);
		
		List<RoleVO> view_roles = RoleVO.getConverter(RoleVO.class).convert(roles, RoleVO.class);
		
		return view_roles;
	}
	
}
