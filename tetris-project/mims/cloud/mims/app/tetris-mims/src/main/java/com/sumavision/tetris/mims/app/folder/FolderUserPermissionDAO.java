package com.sumavision.tetris.mims.app.folder;

import java.util.Collection;
import java.util.List;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = FolderUserPermissionPO.class, idClass = Long.class)
public interface FolderUserPermissionDAO extends BaseDAO<FolderUserPermissionPO>{

	/**
	 * 判断素材库是否已经创建<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年11月21日 下午1:33:19
	 * @param String userId 用户id
	 * @return List<FolderUserPermissionPO> 素材库列表
	 */
	public List<FolderUserPermissionPO> findByUserId(String userId);
	
	/**
	 * 根据文件夹（单个）以及用户获取权限信息<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年11月22日 下午3:16:33
	 * @param String userId 用户id
	 * @param String folderId 文件夹id
	 * @return FolderUserPermissionPO 权限数据
	 */
	public FolderUserPermissionPO findByUserIdAndFolderId(String userId, Long folderId);
	
	/**
	 * 根据文件夹（多个）以及用户获取权限信息<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年11月23日 下午3:23:59
	 * @param String userId 用户id
	 * @param Collection<Long> folderIds 文件夹列表
	 * @return List<FolderUserPermissionPO> 权限数据列表
	 */
	public List<FolderUserPermissionPO> findByUserIdAndFolderIdIn(String userId, Collection<Long> folderIds);
	
}
