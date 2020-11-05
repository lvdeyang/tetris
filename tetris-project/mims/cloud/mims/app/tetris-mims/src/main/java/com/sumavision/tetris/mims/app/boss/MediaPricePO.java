package com.sumavision.tetris.mims.app.boss;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.sumavision.tetris.mims.app.media.ReviewStatus;
import com.sumavision.tetris.mims.app.media.StoreType;
import com.sumavision.tetris.mims.app.media.UploadStatus;
import com.sumavision.tetris.mims.app.media.history.audio.HistoryMediaAudioPO;
import com.sumavision.tetris.orm.po.AbstractBasePO;

@Entity
@Table(name = "MIMS_MEDIA_PRICE")
public class MediaPricePO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;
	private Long mediaId;
	private MediaType mediaType;
	private long price;
	private long truePrice;
	
	public long getTruePrice() {
		return truePrice;
	}
	public void setTruePrice(long truePrice) {
		this.truePrice = truePrice;
	}
	public Long getMediaId() {
		return mediaId;
	}
	public void setMediaId(Long mediaId) {
		this.mediaId = mediaId;
	}
	public MediaType getMediaType() {
		return mediaType;
	}
	public void setMediaType(MediaType mediaType) {
		this.mediaType = mediaType;
	}
	public long getPrice() {
		return price;
	}
	public void setPrice(long price) {
		this.price = price;
	}
	
	
}
