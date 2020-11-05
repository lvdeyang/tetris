package com.sumavision.tetris.mims.app.folder;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.user.UserQuery;

@Controller
@RequestMapping(value = "/folder/role/permission")
public class FolderRolePermissionController {

	@Autowired
	private UserQuery userQuery;
	
	@Autowired
	private FolderRolePermissionDAO folderRolePermissionDao;
	
	@Autowired
	private FolderRolePermissionService folderRolePermissionService;
	
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
	 * 删除文件夹授权<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年7月16日 上午10:25:04
	 * @param Long roleId 角色id
	 * @param Long folderId 文件夹id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/delete")
	public Object delete(
			Long folderId,
			Long roleId,
			HttpServletRequest request) throws Exception{
		folderRolePermissionService.deletePermission(roleId, folderId);
		return null;
	}
	
	/**
	 * 添加授权<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年8月7日 下午2:20:37
	 * @param Long roleId 角色id
	 * @param String roleName 角色名称
	 * @param Long folderId 文件夹id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add")
	public Object add(
			Long folderId,
			Long roleId,
			String roleName,
			HttpServletRequest request) throws Exception{
		folderRolePermissionService.addPermission(roleId, roleName, folderId);
		return null;
	}
	
}
