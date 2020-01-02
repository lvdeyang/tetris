package com.sumavision.tetris.mims.app.media.stream.video;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.mims.app.media.UploadStatus;
import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = MediaVideoStreamPO.class, idClass = Long.class)
public interface MediaVideoStreamDAO extends BaseDAO<MediaVideoStreamPO>{

	/**
	 * 获取文件夹下特定上传状态以及特定例外审核状态的视频流媒资（批量）<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月29日 下午3:33:50
	 * @param Collection<Long> folderIds 文件夹id列表
	 * @param UploadStatus status 上传状态
	 * @param Collection<ReviewStatus> reviewStatus 例外审核状态列表
	 * @param String authorId 作者id
	 * @return List<MediaVideoStreamPO> 媒资视频流列表
	 */
	@Query(value = "SELECT * FROM MIMS_MEDIA_VIDEO_STREAM WHERE FOLDER_ID IN ?1 AND ((UPLOAD_STATUS=?2 AND (REVIEW_STATUS IS NULL OR REVIEW_STATUS NOT IN ?3)) OR AUTHORID=?4)", nativeQuery = true)
	public List<MediaVideoStreamPO> findByFolderIdInAndUploadStatusAndReviewStatusNotInOrAuthorId(Collection<Long> folderIds, String status, Collection<String> reviewStatus, String authorId);
	
	/**
	 * 获取文件夹下特定上传状态的视频流媒资<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月28日 上午10:33:08
	 * @param Long folderId 文件夹id
	 * @param UploadStatus uploadStatus 
	 * @return List<MediaVideoStreamPO> 视频流媒资
	 */
	public List<MediaVideoStreamPO> findByFolderIdAndUploadStatusOrderByName(Long folderId, UploadStatus uploadStatus);
	
	/**
	 * 获取文件夹下特定上传状态的视频流媒资（批量）<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月29日 下午3:33:50
	 * @param Collection<Long> folderIds 文件夹id列表
	 * @param UploadStatus status 上传状态
	 * @return List<MediaVideoStreamPO> 媒资视频流列表
	 */
	public List<MediaVideoStreamPO> findByFolderIdInAndUploadStatus(Collection<Long> folderIds, UploadStatus status);
	
	/**
	 * 获取文件夹下的视频流媒资（批量）<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月28日 下午3:21:18
	 * @param Long folderId 文件夹id
	 * @return List<MediaVideoStreamPO> 视频流媒资列表
	 */
	public List<MediaVideoStreamPO> findByFolderIdIn(Collection<Long> folderId);
	
	/**
	 * 获取文件夹下的视频流媒资（批量）<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月28日 下午3:21:18
	 * @param Long folderId 文件夹id
	 * @param Collection<ReviewStatus> reviewStatus 例外审核状态列表
	 * @return List<MediaVideoStreamPO> 视频流媒资列表
	 */
	@Query(value = "SELECT * FROM MIMS_MEDIA_VIDEO_STREAM WHERE FOLDER_ID IN ?1 AND (REVIEW_STATUS IS NULL OR REVIEW_STATUS NOT IN ?2)", nativeQuery = true)
	public List<MediaVideoStreamPO> findByFolderIdIn(Collection<Long> folderId, Collection<String> reviewStatus);
	
	/**
	 * 根据预览地址查询视频流<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月5日 上午11:28:10
	 * @param Collection<String> previewUrls 预览地址列表
	 * @return List<MediaVideoStreamPO> 视频流列表
	 */
	public List<MediaVideoStreamPO> findByPreviewUrlIn(Collection<String> previewUrls);
	
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
	 * @return List<MediaVideoStreamPO> 查询结果
	 */
	@Query(value = "SELECT * FROM MIMS_MEDIA_VIDEO_STREAM video WHERE IF(?1 is null, true, video.id = ?1) "
			+ "AND IF(?2 is null, true, video.name LIKE CONCAT('%',?2,'%')) "
			+ "AND IF(?3 is null, true, video.update_time >= ?3) "
			+ "AND IF(?4 is null, true, video.update_time <= ?4) "
			+ "AND IF(?5 is null, true, video.tags LIKE CONCAT('%',?5,'%')) "
			+ "AND video.folder_id in ?6 "
			+ "AND video.review_status is null "
			+ "OR video.review_status NOT IN ?7", nativeQuery = true)
	public List<MediaVideoStreamPO> findByCondition(Long id, String name, String startTime, String endTime, String tag, List<Long> folderIds, Collection<String> reviewStatus);
	
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
	 * @return List<MediaVideoStreamPO> 查询结果
	 */
	@Query(value = "SELECT * FROM MIMS_MEDIA_VIDEO_STREAM video "
			+ "JOIN MIMS_MEDIA_VIDEO_STREAM_URL_RELATION relation ON video.id=relation.video_stream_id "
			+ "WHERE IF(?1 is null, true, video.id = ?1) "
			+ "AND IF(?2 is null, true, video.name LIKE CONCAT('%',?2,'%')) "
			+ "AND IF(?3 is null, true, video.update_time >= ?3) "
			+ "AND IF(?4 is null, true, video.update_time <= ?4) "
			+ "AND IF(?5 is null, true, video.tags LIKE CONCAT('%',?5,'%')) "
			+ "AND video.folder_id in ?6 "
			+ "AND IF(?7 is null, true, relation.url LIKE CONCAT('',?7,'%')) "
			+ "AND video.review_status is null "
			+ "OR video.review_status NOT IN ?8", nativeQuery = true)
	public List<MediaVideoStreamPO> findByCondition(Long id, String name, String startTime, String endTime, String tag, List<Long> folderIds, String uri, Collection<String> reviewStatus);
}
