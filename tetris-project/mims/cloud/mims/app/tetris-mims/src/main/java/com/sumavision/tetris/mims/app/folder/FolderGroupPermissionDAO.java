package com.sumavision.tetris.mims.app.folder;

import java.util.Collection;
import java.util.List;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = FolderGroupPermissionPO.class, idClass = Long.class)
public interface FolderGroupPermissionDAO extends BaseDAO<FolderGroupPermissionPO>{

	/**
	 * 判断一个用户组是否已经创建了媒资根目录<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年11月21日 下午1:29:30
	 * @param String groupId 用户组id
	 * @return List<FolderGroupPermissionPO> 组下的文件夹
	 */
	public List<FolderGroupPermissionPO> findByGroupId(String groupId); 
	
	/**
	 * 判断企业对文件夹是否有权限<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月11日 下午2:58:38
	 * @param String groupId 企业id
	 * @param Long folderId 文件夹id
	 * @return List<FolderGroupPermissionPO> 权限
	 */
	public FolderGroupPermissionPO findByGroupIdAndFolderId(String groupId, Long folderId);
	
	/**
	 * 获取文件夹的组权限（批量）<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月28日 下午3:04:50
	 * @param Collection<Long> folderIds 文件夹id列表
	 * @return List<FolderGroupPermissionPO> 权限列表
	 */
	public List<FolderGroupPermissionPO> findByFolderIdIn(Collection<Long> folderIds);
}
