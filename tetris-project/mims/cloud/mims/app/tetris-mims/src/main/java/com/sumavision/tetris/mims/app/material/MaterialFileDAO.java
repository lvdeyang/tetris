package com.sumavision.tetris.mims.app.material;

import java.util.Collection;
import java.util.List;
import org.springframework.data.repository.RepositoryDefinition;
import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = MaterialFilePO.class, idClass = Long.class)
public interface MaterialFileDAO extends BaseDAO<MaterialFilePO>{

	/**
	 * 获取文件夹（多个）下所有的文件（全部状态）<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年11月29日 下午1:38:56
	 * @param Collection<Long> folderIds 文件夹id列表
	 * @return List<MaterialFilePO> 素材文件列表
	 */
	public List<MaterialFilePO> findByFolderIdIn(Collection<Long> folderIds);
	
	/**
	 * 获取文件夹下的素材（特定状态）<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年11月22日 上午11:52:09
	 * @param Long folderId 文件夹id
	 * @param MaterialFileUploadStatus status 素材上传状态
	 * @return List<MaterialFilePO> 素材列表
	 */
	public List<MaterialFilePO> findByFolderIdAndUploadStatusOrderByTypeAscNameAsc(Long folderId, MaterialFileUploadStatus status);
	
	/**
	 * 获取文件夹下的素材（例外状态）<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年11月29日 下午1:18:02
	 * @param Long folderId 文件夹id
	 * @param MaterialFileUploadStatus status 例外状态
	 * @return List<MaterialFilePO> 素材列表
	 */
	public List<MaterialFilePO> findByFolderIdAndUploadStatusNotOrderByTypeAscNameAsc(Long folderId, MaterialFileUploadStatus status);
	
	/**
	 * 根据文件夹（多个）下的素材（特定状态）<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年11月23日 下午3:36:50
	 * @param Collection<Long> folderIds 文件夹id列表
	 * @param MaterialFileUploadStatus status 素材上传状态
	 * @return List<MaterialFilePO> 素材列表
	 */
	public List<MaterialFilePO> findByFolderIdInAndUploadStatus(Collection<Long> folderIds, MaterialFileUploadStatus status);
	
	/**
	 * 根据文件夹（多个）下的素材（例外状态）<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年11月29日 下午1:22:43
	 * @param Collection<Long> folderIds 文件夹id列表
	 * @param MaterialFileUploadStatus status 例外状态
	 * @return List<MaterialFilePO> 素材列表
	 */
	public List<MaterialFilePO> findByFolderIdInAndUploadStatusNotOrderByIdAsc(Collection<Long> folderIds, MaterialFileUploadStatus status);
	
	/**
	 * 判断临时文件是否可以删除<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月4日 上午10:03:21
	 * @param String tmpPath 临时文件路径
	 * @param Collection<Long> ids 例外素材id
	 * @return List<MaterialFilePO> 查询结果
	 */
	public List<MaterialFilePO> findByTmpPathAndIdNotIn(String tmpPath, Collection<Long> ids);
}
