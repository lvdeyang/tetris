package com.sumavision.tetris.mims.app.operation.accessRecord;

import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mims.app.media.audio.MediaAudioVO;
import com.sumavision.tetris.mims.app.media.picture.MediaPictureVO;
import com.sumavision.tetris.mims.app.media.txt.MediaTxtVO;
import com.sumavision.tetris.mims.app.media.video.MediaVideoVO;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class OperationRecordVO extends AbstractBaseVO<OperationRecordVO, OperationRecordPO>{
	private Long userId;
	
	private Long mimsId;
	
	private String mimsName;
	
	private String mimsUuid;
	
	private String mimsType;
	
	private Long num;

	public Long getUserId() {
		return userId;
	}

	public OperationRecordVO setUserId(Long userId) {
		this.userId = userId;
		return this;
	}

	public Long getMimsId() {
		return mimsId;
	}

	public OperationRecordVO setMimsId(Long mimsId) {
		this.mimsId = mimsId;
		return this;
	}

	public String getMimsName() {
		return mimsName;
	}

	public OperationRecordVO setMimsName(String mimsName) {
		this.mimsName = mimsName;
		return this;
	}

	public String getMimsUuid() {
		return mimsUuid;
	}

	public OperationRecordVO setMimsUuid(String mimsUuid) {
		this.mimsUuid = mimsUuid;
		return this;
	}

	public String getMimsType() {
		return mimsType;
	}

	public OperationRecordVO setMimsType(String mimsType) {
		this.mimsType = mimsType;
		return this;
	}

	public Long getNum() {
		return num;
	}

	public OperationRecordVO setNum(Long num) {
		this.num = num;
		return this;
	}
	
	public static OperationRecordVO setFromAudio(MediaAudioVO media) throws Exception {
		return new OperationRecordVO()
				.setMimsId(media.getId())
				.setMimsUuid(media.getUuid())
				.setMimsName(media.getName())
				.setMimsType(media.getType().toLowerCase());
	}
	
	public static OperationRecordVO setFromVideo(MediaVideoVO media) throws Exception {
		return new OperationRecordVO()
				.setMimsId(media.getId())
				.setMimsUuid(media.getUuid())
				.setMimsName(media.getName())
				.setMimsType(media.getType().toLowerCase());
	}
	
	public static OperationRecordVO setFromTxt(MediaTxtVO media) throws Exception {
		return new OperationRecordVO()
				.setMimsId(media.getId())
				.setMimsUuid(media.getUuid())
				.setMimsName(media.getName())
				.setMimsType(media.getType().toLowerCase());
	}
	
	public static OperationRecordVO setFromPicture(MediaPictureVO media) throws Exception {
		return new OperationRecordVO()
				.setMimsId(media.getId())
				.setMimsUuid(media.getUuid())
				.setMimsName(media.getName())
				.setMimsType(media.getType().toLowerCase());
	}

	@Override
	public OperationRecordVO set(OperationRecordPO entity) throws Exception {
		return this.setId(entity.getId())
				.setUuid(entity.getUuid())
				.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
				.setUserId(entity.getUserId())
				.setMimsId(entity.getMimsId())
				.setMimsUuid(entity.getMimsUuid())
				.setMimsType(entity.getMimsType())
				.setNum(entity.getNum());
	}
}
