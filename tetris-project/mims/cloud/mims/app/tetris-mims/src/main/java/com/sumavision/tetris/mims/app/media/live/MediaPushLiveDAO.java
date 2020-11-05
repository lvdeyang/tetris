package com.sumavision.tetris.mims.app.media.live;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = MediaPushLivePO.class, idClass = Long.class)
public interface MediaPushLiveDAO extends BaseDAO<MediaPushLivePO>{
	/**
	 * 获取文件夹下特定上传状态以及特定例外审核状态的音频流媒资（批量）<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月29日 下午3:33:50
	 * @param Collection<Long> folderIds 文件夹id列表
	 * @param UploadStatus status 上传状态
	 * @param Collection<ReviewStatus> reviewStatus 例外审核状态列表
	 * @param String authorId 作者id
	 * @return List<MediaAudioPO> 媒资音频流列表
	 */
	@Query(value = "SELECT * FROM MIMS_MEDIA_PUSH_LIVE WHERE FOLDER_ID IN ?1 AND ((REVIEW_STATUS IS NULL OR REVIEW_STATUS NOT IN ?2) OR AUTHORID=?3)", nativeQuery = true)
	public List<MediaPushLivePO> findByFolderIdInAndReviewStatusNotInOrAuthorId(Collection<Long> folderIds, Collection<String> reviewStatus, String authorId);
	
	
	/**
	 * 获取文件夹下特定上传状态的音频流媒资<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月28日 上午10:33:08
	 * @param Long folderId 文件夹id
	 * @param UploadStatus uploadStatus 
	 * @return List<MediaPushLivePO> 音频流媒资
	 */
	public List<MediaPushLivePO> findByFolderIdOrderByName(Long folderId);
	
	/**
	 * 获取文件夹下的音频流媒资（批量）<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月28日 下午3:21:18
	 * @param Long folderId 文件夹id
	 * @return List<MediaPushLivePO> 音频流媒资列表
	 */
	public List<MediaPushLivePO> findByFolderIdIn(Collection<Long> folderId);
	
	/**
	 * 获取文件夹下的视频流媒资（批量）<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月28日 下午3:21:18
	 * @param Long folderId 文件夹id
	 * @param Collection<ReviewStatus> reviewStatus 例外审核状态列表
	 * @return List<MediaPushLivePO> 视频流媒资列表
	 */
	@Query(value = "SELECT * FROM MIMS_MEDIA_PUSH_LIVE WHERE FOLDER_ID IN ?1 AND (REVIEW_STATUS IS NULL OR REVIEW_STATUS NOT IN ?2)", nativeQuery = true)
	public List<MediaPushLivePO> findByFolderIdIn(Collection<Long> folderId, Collection<String> reviewStatus);
	
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
	 * @return List<MediaPushLivePO> 查询结果
	 */
	@Query(value = "SELECT * FROM MIMS_MEDIA_PUSH_LIVE live WHERE IF(?1 is null, true, video.id = ?1) "
			+ "AND IF(?2 is null, true, video.name LIKE CONCAT('%',?2,'%')) "
			+ "AND IF(?3 is null, true, video.update_time >= ?3) "
			+ "AND IF(?4 is null, true, video.update_time <= ?4) "
			+ "AND IF(?5 is null, true, video.tags LIKE CONCAT('%',?5,'%')) "
			+ "AND live.folder_id in ?6 "
			+ "AND live.review_status is null "
			+ "OR live.review_status NOT IN ?7", nativeQuery = true)
	public List<MediaPushLivePO> findByCondition(Long id, String name, String startTime, String endTime, String tag, List<Long> folderIds, Collection<String> reviewStatus);
}
