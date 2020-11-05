package com.sumavision.tetris.mims.app.folder;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = FolderRolePermissionPO.class, idClass = Long.class)
public interface FolderRolePermissionDAO extends BaseDAO<FolderRolePermissionPO>{

	/**
	 * 查询文件夹可编辑授权情况<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年7月12日 下午3:49:29
	 * @param Long folderId 文件夹id
	 * @param Boolean autoGeneration 是否可编辑
	 * @return List<FolderRolePermissionPO> 文件夹授权信息
	 */
	public List<FolderRolePermissionPO> findByFolderIdAndAutoGeneration(Long folderId, Boolean autoGeneration);
	
	/**
	 * 根据文件夹查询授权<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月11日 下午1:36:07
	 * @param Long folderId 文件夹信息
	 * @return List<FolderRolePermissionPO> 文件夹授权信息
	 */
	public List<FolderRolePermissionPO> findByFolderId(Long folderId);
	
	/**
	 * 根据文件夹查询授权(过滤管理员角色)<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年7月3日 下午1:36:07
	 * @param Long folderId 文件夹信息
	 * @return List<FolderRolePermissionPO> 文件夹授权信息
	 */
	@Query(value = "select * from MIMS_FOLDER_ROLE_PERMISSION where folder_id = ?1 AND role_id <> ?2", nativeQuery = true)
	public List<FolderRolePermissionPO> findByFolderIdExceptRoleId(Long folderId, Long roleId);
	
	/**
	 * 根据文件夹查询授权（批量）<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月28日 下午3:09:26
	 * @param Collection<Long> folderIds 文件夹id列表
	 * @return List<FolderRolePermissionPO> 权限列表
	 */
	public List<FolderRolePermissionPO> findByFolderIdIn(Collection<Long> folderIds);
	
	/**
	 * 根据角色id查询授权<br/>
	 * <b>作者:</b>ql<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 下午3:37:06
	 * @param Long roleId 角色id
	 * @return List<FolderRolePermissionPO> 权限数据
	 */
	public List<FolderRolePermissionPO> findByRoleId(Long roleId);
	
	/**
	 * 查询具体授权<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月11日 下午3:37:06
	 * @param Long folderId 文件夹id
	 * @param Long roleId 角色id
	 * @return FolderRolePermissionPO 权限数据
	 */
	public FolderRolePermissionPO findByFolderIdAndRoleId(Long folderId, Long roleId);
	
	/**
	 * 文件夹授权查重<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月12日 上午10:08:11
	 * @param Long roleId 角色id
	 * @param Collection<Long> folderIds 待授权文件夹id列表
	 * @return List<Long> 已鉴权文件夹id列表
	 */
	@Query(value = "SELECT permission.folder_id FROM mims_folder_role_permission permission WHERE permission.role_id=?1 AND permission.folder_id IN ?2", nativeQuery = true)
	public List<Long> permissionDuplicateChecking(Long roleId, Collection<Long> folderIds);
	
	/**
	 * 获取角色授权的文件夹<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月12日 上午11:56:27
	 * @param Collection<Long> folderIds 文件夹id列表
	 * @param Long roleId 角色id
	 * @return List<FolderRolePermissionPO> 授权列表
	 */
	public List<FolderRolePermissionPO> findByFolderIdInAndRoleId(Collection<Long> folderIds, Long roleId);
	
	/**
	 * 判断用户对媒资文件夹是否有权限<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月29日 下午2:34:08
	 * @param Long folderId 媒资文件夹id
	 * @param String userId 用户id
	 * @return List<FolderRolePermissionPO> 权限列表
	 */
	@Query(value = "SELECT permission0.* FROM mims_folder_role_permission permission0 LEFT JOIN mims_role_user_permission permission1 ON permission0.role_id=permission1.role_id WHERE permission1.user_id=?1 AND permission0.folder_id=?2", nativeQuery = true)
	public List<FolderRolePermissionPO> findByFolderIdAndUserId(String userId, Long folderId);
}
