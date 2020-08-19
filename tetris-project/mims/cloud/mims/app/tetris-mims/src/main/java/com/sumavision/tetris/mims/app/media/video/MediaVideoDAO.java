package com.sumavision.tetris.mims.app.media.video;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.mims.app.media.UploadStatus;
import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = MediaVideoPO.class, idClass = Long.class)
public interface MediaVideoDAO extends BaseDAO<MediaVideoPO>{

	/**
	 * 获取文件夹下特定上传状态以及特定例外审核状态的视频媒资（批量）<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月29日 下午3:33:50
	 * @param Collection<Long> folderIds 文件夹id列表
	 * @param UploadStatus status 上传状态
	 * @param Collection<ReviewStatus> reviewStatus 例外审核状态列表
	 * @param String authorId 作者id
	 * @return List<MediaVideoPO> 媒资音频列表
	 */
	@Query(value = "SELECT * FROM MIMS_MEDIA_VIDEO WHERE FOLDER_ID IN ?1 AND ((UPLOAD_STATUS=?2 AND (REVIEW_STATUS IS NULL OR REVIEW_STATUS NOT IN ?3)) OR AUTHORID=?4)", nativeQuery = true)
	public List<MediaVideoPO> findByFolderIdInAndUploadStatusAndReviewStatusNotInOrAuthorId(Collection<Long> folderIds, String status, Collection<String> reviewStatus, String authorId);
	
	/**
	 * uuid批量查询<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月28日 上午10:33:08
	 * @param Collection<String> uuids uuid列表
	 * @return List<MediaVideoPO> 视频媒资列表
	 */
	public List<MediaVideoPO> findByUuidIn(Collection<String> uuids);
	
	/**
	 * 获取文件夹下特定上传状态的视频媒资<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月28日 上午10:33:08
	 * @param Long folderId 文件夹id
	 * @param UploadStatus uploadStatus 
	 * @return List<MediaVideoPO> 视频媒资
	 */
	public List<MediaVideoPO> findByFolderIdAndUploadStatusOrderByName(Long folderId, UploadStatus uploadStatus);
	
	/**
	 * 获取文件夹下特定上传状态的视频媒资（批量）<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月29日 下午3:33:50
	 * @param Collection<Long> folderIds 文件夹id列表
	 * @param UploadStatus status 上传状态
	 * @return List<MediaVideoPO> 媒资视频列表
	 */
	public List<MediaVideoPO> findByFolderIdInAndUploadStatus(Collection<Long> folderIds, UploadStatus status);
	
	/**
	 * 获取文件夹下的视频媒资（批量）<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月28日 下午3:21:18
	 * @param Long folderId 文件夹id
	 * @return List<MediaVideoPO> 视频媒资列表
	 */
	public List<MediaVideoPO> findByFolderIdIn(Collection<Long> folderId);
	
	/**
	 * 判断临时文件是否可以删除<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月4日 上午10:03:21
	 * @param String tmpPath 临时文件路径
	 * @param Collection<Long> ids 例外素材id
	 * @return List<MaterialFilePO> 查询结果
	 */
	public List<MediaVideoPO> findByUploadTmpPathAndIdNotIn(String tmpPath, Collection<Long> ids);
	
	/**
	 * 获取文件夹下的视频媒资（批量过滤审核状态）<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年9月26日 下午3:21:18
	 * @param Long folderId 文件夹id
	 * @param Collection<String> reviewStatus 过滤的审核状态
	 * @return List<MediaVideoPO> 视频媒资列表
	 */
	@Query(value = "SELECT * FROM MIMS_MEDIA_VIDEO WHERE FOLDER_ID IN ?1 AND (REVIEW_STATUS IS NULL OR REVIEW_STATUS NOT IN ?2)", nativeQuery = true)
	public List<MediaVideoPO> findByFolderIdIn(Collection<Long> folderId, Collection<String> reviewStatus);
	
	/**
	 * 根据用户标签获取文件夹下的视频媒资（批量）<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年8月11日 下午3:21:18
	 * @param Long folderId 文件夹id
	 * @param String tags
	 * @return List<MediaVideoPO> 视频媒资列表
	 */
	@Query(value = "SELECT * FROM MIMS_MEDIA_VIDEO WHERE FOLDER_ID IN ?1 AND tags like CONCAT('%',?2,'%') AND (REVIEW_STATUS IS NULL OR REVIEW_STATUS NOT IN ?3)", nativeQuery = true)
	public List<MediaVideoPO> findByFolderIdInAndTag(Collection<Long> folderId, String tag, Collection<String> reviewStatus);
	
	/**
	 * 根据条件查询媒资<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年9月19日 下午3:17:30
	 * @param Long id 媒资id
	 * @param String name 名称(模糊匹配)
	 * @param String startTime updateTime起始查询
	 * @param Stinrg endTime updateTime终止查询
	 * @param Long tagId 标签id
	 * @return List<MediaVideoPO> 查询结果
	 */
	@Query(value = "SELECT * FROM MIMS_MEDIA_VIDEO video WHERE IF(?1 is null, true, video.id = ?1) "
			+ "AND IF(?2 is null, true, video.name LIKE CONCAT('%',?2,'%')) "
			+ "AND IF(?3 is null, true, video.update_time >= ?3) "
			+ "AND IF(?4 is null, true, video.update_time <= ?4) "
			+ "AND IF(?5 is null, true, video.tags LIKE CONCAT('%',?5,'%')) "
			+ "AND video.folder_id in ?6 "
			+ "AND video.review_status is null "
			+ "OR video.review_status NOT IN ?7", nativeQuery = true)
	public List<MediaVideoPO> findByCondition(Long id, String name, String startTime, String endTime, String tag, List<Long> folderIds, Collection<String> reviewStatus);
	
	/**
	 * 根据预览地址查询视频列表<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月5日 上午10:49:17
	 * @param Collection<String> previewUrls 预览地址列表
	 * @return List<MediaVideoPO> 视频列表
	 */
	public List<MediaVideoPO> findByPreviewUrlIn(Collection<String> previewUrls);
	
}
