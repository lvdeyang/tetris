package com.sumavision.tetris.mims.app.folder;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;

/**
 * 文件夹授权相关操作<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2018年12月12日 上午11:27:23
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class FolderRolePermissionService {
	
	@Autowired
	private FolderRolePermissionDAO folderRolePermissionDao;
	
	/**
	 * 删除文件夹授权<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年7月16日 上午10:25:04
	 * @param Long permissionId 授权id
	 */
	public void deletePermission(Long permissionId) throws Exception{
		FolderRolePermissionPO permission = folderRolePermissionDao.findOne(permissionId);
		if(permission != null){
			folderRolePermissionDao.delete(permission);
		}
	}
	
	/**
	 * 删除文件夹授权<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年7月16日 上午10:25:04
	 * @param Long roleId 角色id
	 * @param Long folderId 文件夹id
	 */
	public void deletePermission(Long roleId, Long folderId) throws Exception{
		List<FolderRolePermissionPO> permissions = folderRolePermissionDao.findByFolderIdInAndRoleId(new ArrayListWrapper<Long>().add(folderId).getList(), roleId);
		if(permissions!=null && permissions.size()>0){
			folderRolePermissionDao.deleteInBatch(permissions);
		}
	}
	
	/**
	 * 解除一个角色的所有授权<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年8月7日 下午5:23:43
	 * @param Long roleId 角色id
	 */
	public void deletePermissionByRoleId(Long roleId) throws Exception{
		List<FolderRolePermissionPO> permissions = folderRolePermissionDao.findByRoleId(roleId);
		if(permissions!=null && permissions.size()>0){
			folderRolePermissionDao.deleteInBatch(permissions);
		}
	}
	
	/**
	 * 添加授权<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年8月7日 下午2:20:37
	 * @param Long roleId 角色id
	 * @param String roleName 角色名称
	 * @param Long folderId 文件夹id
	 * @return FolderRolePermissionPO 授权信息
	 */
	public FolderRolePermissionPO addPermission(Long roleId, String roleName, Long folderId) throws Exception{
		FolderRolePermissionPO permission = new FolderRolePermissionPO();
		permission.setRoleId(roleId);
		permission.setRoleName(roleName);
		permission.setFolderId(folderId);
		permission.setAutoGeneration(false);
		permission.setUpdateTime(new Date());
		folderRolePermissionDao.save(permission);
		return permission;
	}
	
}
