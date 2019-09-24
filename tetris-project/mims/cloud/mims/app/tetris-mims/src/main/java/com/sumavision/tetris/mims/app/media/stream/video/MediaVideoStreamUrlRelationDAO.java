package com.sumavision.tetris.mims.app.media.stream.video;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = MediaVideoStreamUrlRelationPO.class, idClass = Long.class)
public interface MediaVideoStreamUrlRelationDAO extends BaseDAO<MediaVideoStreamUrlRelationPO>{
	
	public List<MediaVideoStreamUrlRelationPO> findByVideoStreamIdOrderByVisitCountAsc(Long streamId);
	
	@Query(value = "select url from MIMS_MEDIA_VIDEO_STREAM_URL_RELATION where video_stream_id=?1 ", nativeQuery = true)
	public List<String> findUrlsByVideoStreamId(Long streamId);
	
	/**
	 * 根据视频流id列表查询url列表<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年8月23日 下午3:58:55
	 * @param Collection<Long> videoStreamIds 视频流id列表
	 * @return List<MediaVideoStreamUrlRelationPO> url列表
	 */
	public List<MediaVideoStreamUrlRelationPO> findByVideoStreamIdIn(Collection<Long> videoStreamIds);
	
	@Query(value = "delete from MIMS_MEDIA_VIDEO_STREAM_URL_RELATION where video_stream_id=?1 ", nativeQuery = true)
	@Modifying
	public void removeByStreamId(Long streamId);
	
	@Modifying
	@Query(value = "delete from MIMS_MEDIA_VIDEO_STREAM_URL_RELATION where video_stream_id=?1 AND url not in ?2 ", nativeQuery = true)
	public void removeByStreamIdWithExceptUrlIn(Long streamId,Collection<String> exceptUrls);
	
	@Modifying
	@Query(value = "delete from MIMS_MEDIA_VIDEO_STREAM_URL_RELATION where video_stream_id in ?1", nativeQuery = true)
	public void removeByStreamIdIn(Collection<Long> ids);
}
