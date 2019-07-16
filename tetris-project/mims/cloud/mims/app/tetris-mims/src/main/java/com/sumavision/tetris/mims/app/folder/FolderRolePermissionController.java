package com.sumavision.tetris.mims.app.folder;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.sumavision.tetris.mims.app.folder.exception.FolderNotExistException;
import com.sumavision.tetris.mims.app.folder.exception.UserHasNoPermissionForFolderException;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.subordinate.role.SubordinateRoleQuery;
import com.sumavision.tetris.subordinate.role.SubordinateRoleVO;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;

@Controller
@RequestMapping(value = "/folder/role/permission")
public class FolderRolePermissionController {

	@Autowired
	private UserQuery userQuery;
	
	@Autowired
	private FolderDAO folderDao;
	
	@Autowired
	private FolderRolePermissionDAO folderRolePermissionDao;
	
	@Autowired
	private FolderQuery folderQuery;
	
	@Autowired
	private FolderRolePermissionService folderRolePermissionService;
	
	@Autowired
	private SubordinateRoleQuery subordinateRoleQuery;
	
	/**
	 * 查询没有授权当前文件夹的角色<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年7月12日 下午5:21:21
	 * @param @PathVariable Long folderId 文件夹id
	 * @return List<SubordinateRoleVO> 业务角色列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/query/role/with/folder/permission/except/{folderId}")
	public Object queryRoleWithFolderPermissionExcept(
			@PathVariable Long folderId,
			HttpServletRequest request) throws Exception{
		
		List<FolderRolePermissionPO> permissions = folderRolePermissionDao.findByFolderId(folderId);
		Set<Long> exceptRoleIds = new HashSet<Long>();
		if(permissions!=null && permissions.size()>0){
			for(FolderRolePermissionPO permission:permissions){
				exceptRoleIds.add(permission.getRoleId());
			}
		}
		
		UserVO user = userQuery.current();
		
		return subordinateRoleQuery.findByCompanyIdWithExcept(Long.valueOf(user.getGroupId()), exceptRoleIds);
	}
	
	/**
	 * 获取文件夹授权情况<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月11日 下午1:40:17
	 * @param Long folderId 文件夹id
	 * @return List<FolderRolePermissionVO> 授权情况
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/list")
	public Object list(
			Long folderId,
			HttpServletRequest request) throws Exception{
		List<FolderRolePermissionPO> entities = folderRolePermissionDao.findByFolderIdAndAutoGeneration(folderId, false);
		List<FolderRolePermissionVO> permissions = FolderRolePermissionVO.getConverter(FolderRolePermissionVO.class).convert(entities, FolderRolePermissionVO.class);
		return permissions;
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
	@RequestMapping(value = "/delete/{id}")
	public Object delete(
			@PathVariable Long id,
			HttpServletRequest request) throws Exception{
		folderRolePermissionService.deletePermission(id);
		return null;
	}
	
	/**
	 * 文件夹授权<br/>
	 * <p>文件夹的所有子文件夹也会授权</p>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月11日 下午4:09:35
	 * @param Long folderId 文件夹id
	 * @param String roles JSON数据，角色列表
	 * @return List<FolderRolePermissionVO> 授权情况
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add")
	public Object add(
			Long folderId,
			String roles,
			HttpServletRequest request) throws Exception{
		
		FolderPO folder = folderDao.findOne(folderId);
		
		if(folder == null){
			throw new FolderNotExistException(folderId);
		}
		
		List<SubordinateRoleVO> _roles = JSON.parseArray(roles, SubordinateRoleVO.class);
		
		List<FolderRolePermissionPO> entities = folderRolePermissionService.addPermission(folder, _roles);
		
		List<FolderRolePermissionVO> permissions = FolderRolePermissionVO.getConverter(FolderRolePermissionVO.class).convert(entities, FolderRolePermissionVO.class);
		
		return permissions;
	}
	
}
