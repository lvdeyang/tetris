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
