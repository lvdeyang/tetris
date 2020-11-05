package com.suma.venus.resource.dao;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;

import com.suma.venus.resource.pojo.FolderPO;
import com.suma.venus.resource.pojo.BundlePO.SOURCE_TYPE;
import com.suma.venus.resource.pojo.FolderPO.FolderType;

@RepositoryDefinition(domainClass = FolderPO.class, idClass = Long.class)
public interface FolderDao extends CommonDao<FolderPO>{
	
	@Query("select folder.parentPath from com.suma.venus.resource.pojo.FolderPO folder where folder.id in ?1")
	public List<String> queryParentPathByIds(Collection<Long> ids);
	
	public List<FolderPO> findByParentId(Long parentId);
	
	public List<FolderPO> findByParentPath(String parentPath);
	
	public List<FolderPO> findByParentPathAndFolderType(String parentPath, String folderType);
	
	public FolderPO findByName(String name);

	public FolderPO findTopByUuid(String uuid);
	
	public List<FolderPO> findByUuidIn(Collection<String> uuids);
	
	public List<FolderPO> findByFolderType(FolderType folderType);
	
	public List<FolderPO> findBySourceType(SOURCE_TYPE sourceType);
	
	@Query("select f from FolderPO f where f.folderType=?1 and f.parentPath is null")
	public FolderPO findRootFolderByType(FolderType folderType);
	
	@Query("select f from FolderPO f where f.folderType=?1 and f.parentId=?2")
	public FolderPO findFolderByTypeAndParent(FolderType folderType, Long parentId);

	@Query("select folder.id from FolderPO folder where folder.name=?1")
	public Long findIdByName(String name);
	
	@Query("select f from FolderPO f where (f.syncStatus='ASYNC' or f.syncStatus is null) and (f.sourceType='SYSTEM' or f.sourceType is null) and f.toLdap=true")
	public List<FolderPO> findFoldersSyncToLdap();

	//查找所有的非根目录
	@Query("select f from FolderPO f where f.parentPath is not null")
	public List<FolderPO> findNoneRootFolders();
	
	//查询可作为根目录选项的数据(将外部系统的目录数据作为根目录可选项？)
	@Query("select f from FolderPO f where f.sourceType='EXTERNAL'")
	public List<FolderPO> findRootOptions();
	
	//查询bvc内部的默认根节点
	@Query("select f from FolderPO f where f.sourceType='SYSTEM' and f.beBvcRoot=true")
	public List<FolderPO> findBvcRootFolders();
	
	public List<FolderPO> findByParentPathLike(String path);
	
	/**
	 * 查询根目录<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年8月19日 下午1:18:10
	 * @param Collection<FolderPO.FolderType> folderTypes 不查的文件夹类型
	 * @param Long parentId 父文件夹id
	 * @return List<FolderPO> 文件夹列表
	 */
	@Query(value = "SELECT * FROM folderpo WHERE parent_id=?1 AND (folder_type IS NULL OR folder_type NOT IN ('ON_DEMAND'))", nativeQuery = true)
	public List<FolderPO> findByParentIdAndFolderTypeNotIn(Long parentId);
}
