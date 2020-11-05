package com.sumavision.tetris.mims.app.media.stream.audio;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

@Entity
@Table(name = "MIMS_MEDIA_AUDIO_STREAM_URL_RELATION")
public class MediaAudioStreamUrlRelationPO extends AbstractBasePO{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long videoStreamId;
	private String url;
	private Long visitCount;

	@Column(name = "VIDEO_STREAM_ID")
	public Long getVideoStreamId() {
		return videoStreamId;
	}

	public void setVideoStreamId(Long videoStreamId) {
		this.videoStreamId = videoStreamId;
	}

	@Column(name = "URL")
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Column(name = "VISIT_COUNT")
	public Long getVisitCount() {
		return visitCount;
	}

	public void setVisitCount(Long visitCount) {
		this.visitCount = visitCount;
	}
}
