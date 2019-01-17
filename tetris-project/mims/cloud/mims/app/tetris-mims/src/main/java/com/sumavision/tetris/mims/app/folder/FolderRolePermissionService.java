package com.sumavision.tetris.mims.app.folder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sumavision.tetris.mims.app.role.RoleDAO;
import com.sumavision.tetris.mims.app.role.RolePO;

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
	private RoleDAO roleDao;
	
	@Autowired
	private FolderRolePermissionDAO folderRolePermissionDao;
	
	@Autowired
	private FolderQuery folderTool;
	
	/**
	 * 解除文件夹授权<br/>
	 * <p>包括文件夹的子文件夹一并解除授权</p>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月12日 上午11:30:46
	 * @param FolderPO folder 待解除授权的文件夹
	 * @param RolePO role 待解除授权的角色
	 */
	public void deletePermission(FolderPO folder, RolePO role) throws Exception{
		
		List<FolderPO> subFolders = folderTool.findSubFolders(folder.getId());
		List<FolderPO> totalFolders = new ArrayList<FolderPO>();
		totalFolders.add(folder);
		
		if(subFolders!=null && subFolders.size()>0) totalFolders.addAll(subFolders);
		Set<Long> folderIds = new HashSet<Long>();
		for(FolderPO scope:totalFolders){
			folderIds.add(scope.getId());
		}
		
		List<FolderRolePermissionPO> permissions = folderRolePermissionDao.findByFolderIdInAndRoleId(folderIds, role.getId());
		
		folderRolePermissionDao.deleteInBatch(permissions);
		
	}
	
	/**
	 * 企业文件夹角色授权<br/>
	 * <p>包括文件夹的子文件夹一并授权</p>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月11日 下午4:05:05
	 * @param String groupId 企业id
	 * @param FolderPO folder 文件夹
	 * @param Collection<Long> roleIds 角色id列表
	 * @return List<RolePO> 角色列表
	 */
	public List<RolePO> addPermission(String groupId, FolderPO folder, Collection<Long> roleIds) throws Exception{
		
		List<RolePO> roles = roleDao.findByGroupIdAndIdIn(groupId, roleIds);
		
		if(roles!=null && roles.size()>0){
			
			List<FolderPO> subFolders = folderTool.findSubFolders(folder.getId());
			List<FolderPO> totalFolders = new ArrayList<FolderPO>();
			totalFolders.add(folder);
			if(subFolders!=null && subFolders.size()>0) totalFolders.addAll(subFolders);
			Set<Long> folderIds = new HashSet<Long>();
			for(FolderPO scope:totalFolders){
				folderIds.add(scope.getId());
			}
			
			List<FolderRolePermissionPO> permissions = new ArrayList<FolderRolePermissionPO>();
			for(RolePO role:roles){
				List<Long> permited = folderRolePermissionDao.permissionDuplicateChecking(role.getId(), folderIds);
				for(FolderPO scope:totalFolders){
					if(permited!=null && permited.contains(scope.getId())) continue;
					FolderRolePermissionPO permission = new FolderRolePermissionPO();
					permission.setFolderId(scope.getId());
					permission.setRoleId(role.getId());
					permission.setUpdateTime(new Date());
					permissions.add(permission);
				}
			}
			folderRolePermissionDao.save(permissions);
		}
		
		return roles;
	}
	
}
