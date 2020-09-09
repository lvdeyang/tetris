package com.sumavision.tetris.cs.program;

import java.util.Comparator;
import java.util.Date;

import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.cs.menu.CsResourceVO;
import com.sumavision.tetris.mims.app.media.audio.MediaAudioVO;
import com.sumavision.tetris.mims.app.media.avideo.MediaAVideoVO;
import com.sumavision.tetris.mims.app.media.editor.MediaFileEditorVO;
import com.sumavision.tetris.mims.app.media.stream.audio.MediaAudioStreamVO;
import com.sumavision.tetris.mims.app.media.stream.video.MediaVideoStreamVO;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class ScreenVO extends AbstractBaseVO<ScreenVO, ScreenPO> {

	private Long programId;
	private Long serialNum;
	private Long index;
	private String contentType;
	private String textContent;
	private String mimsUuid;
	private Long resourceId;

	private String name;
	private String type;
	private String mimetype;
	private String previewUrl;
	private String encryption;
	private String encryptionUrl;
	private Integer hotWeight;
	private Integer downloadCount;
	private String duration;
	private String size;
	private String freq;
	private String audioPid;
	private String videoPid;
	private String audioType;
	private String videoType;

	@Override
	public ScreenVO set(ScreenPO entity) throws Exception {
		this.setId(entity.getId())
		.setUuid(entity.getUuid())
		.setUpdateTime(entity.getUpdateTime() == null ? "": DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
		.setProgramId(entity.getProgramId())
		.setSerialNum(entity.getSerialNum())
		.setIndex(entity.getScreenIndex())
		.setContentType(entity.getContentType())
		.setTextContent(entity.getTextContent())
		.setName(entity.getName())
		.setType(entity.getType())
		.setMimetype(entity.getMimetype())
		.setPreviewUrl(entity.getPreviewUrl())
		.setEncryption(entity.getEncryptionUrl() == null || entity.getEncryptionUrl().isEmpty() ? "false" : "true")
		.setEncryptionUrl(entity.getEncryptionUrl())
		.setHotWeight(entity.getHotWeight())
		.setDownloadCount(entity.getDownloadCount())
		.setDuration(entity.getDuration())
		.setSize(entity.getSize())
		.setFreq(entity.getFreq())
		.setAudioPid(entity.getAudioPid())
		.setVideoPid(entity.getVideoPid())
		.setAudioType(entity.getAudioType())
		.setVideoType(entity.getVideoType())
		.setMimsUuid(entity.getMimsUuid())
		.setResourceId(entity.getResourceId());
		return this;
	}
	
	public ScreenVO getFromAVideoVO(MediaAVideoVO media) throws Exception {
		this.setPreviewUrl(media.getPreviewUrl())
		.setDuration(media.getDuration())
		.setSize(media.getSize());
		
		MediaFileEditorVO editorVO = media.getEditorInfo();
		if (editorVO != null) {
			String previewUrl = editorVO.getPreviewUrl();
			String duration = editorVO.getDuration();
			Long size = editorVO.getSize();
			if (previewUrl != null && !previewUrl.isEmpty()) this.setPreviewUrl(previewUrl);
			if (duration != null && !duration.isEmpty()) this.setDuration(duration);
			if (size != null) this.setSize(size.toString());
		}
		
		return this.setMimsUuid(media.getUuid())
		.setName(media.getName())
		.setEncryption(media.getEncryption() != null && media.getEncryption() ? "true" : "false")
		.setEncryptionUrl(media.getEncryptionUrl())
		.setType(media.getType())
		.setMimetype(media.getMimetype())
		.setHotWeight(media.getHotWeight())
		.setDownloadCount(media.getDownloadCount());
	}
	
	public ScreenVO getFromAudioStreamVO(MediaAudioStreamVO media) throws Exception {
		return this.setMimsUuid(media.getUuid())
				.setName(media.getName())
				.setPreviewUrl(media.getPreviewUrl())
				.setType(media.getType());
	}
	
	public ScreenVO getFromVideoStreamVO(MediaVideoStreamVO media) throws Exception {
		return this.setMimsUuid(media.getUuid())
				.setName(media.getName())
				.setPreviewUrl(media.getPreviewUrl() != null && !media.getPreviewUrl().isEmpty() ? media.getPreviewUrl().get(0) : "")
				.setType(media.getType());
	}
	
	public ScreenVO getFromAudioVO(MediaAudioVO media) throws Exception {
		this.setPreviewUrl(media.getPreviewUrl())
		.setDuration(media.getDuration())
		.setSize(media.getSize());
		
		MediaFileEditorVO editorVO = media.getEditorInfo();
		if (editorVO != null) {
			String previewUrl = editorVO.getPreviewUrl();
			String duration = editorVO.getDuration();
			Long size = editorVO.getSize();
			if (previewUrl != null && !previewUrl.isEmpty()) this.setPreviewUrl(previewUrl);
			if (duration != null && !duration.isEmpty()) this.setDuration(duration);
			if (size != null) this.setSize(size.toString());
		}
		
		return this.setMimsUuid(media.getUuid())
				.setName(media.getName())
				.setEncryption(media.getEncryption() != null && media.getEncryption() ? "true" : "false")
				.setEncryptionUrl(media.getEncryptionUrl())
				.setType(media.getType())
				.setMimetype(media.getMimetype())
				.setHotWeight(media.getHotWeight())
				.setDownloadCount(media.getDownloadCount());
	}
	
	public ScreenVO getFromCsResourceVO(CsResourceVO resourceVO) throws Exception {
		return this.setResourceId(resourceVO.getId())
				.setMimsUuid(resourceVO.getMimsUuid())
				.setName(resourceVO.getName())
				.setType(resourceVO.getType())
				.setMimetype(resourceVO.getMimetype())
				.setDuration(resourceVO.getDuration())
				.setSize(resourceVO.getSize())
				.setPreviewUrl(resourceVO.getPreviewUrl())
				.setEncryption(resourceVO.getEncryption())
				.setEncryptionUrl(resourceVO.getEncryptionUrl())
				.setDownloadCount(resourceVO.getDownloadCount())
				.setFreq(resourceVO.getFreq())
				.setAudioPid(resourceVO.getAudioPid())
				.setVideoPid(resourceVO.getVideoPid())
				.setAudioType(resourceVO.getAudioType())
				.setVideoType(resourceVO.getVideoType())
				.setMimsUuid(resourceVO.getUuid())
				.setUpdateTime(new Date());
	}
	
	public static ScreenPO getPO(ScreenVO vo, Long programId) throws Exception {
		ScreenPO screenPO = new ScreenPO();
		if (vo == null || programId == null) return screenPO;
		screenPO.setProgramId(programId);
		screenPO.setMimsUuid(vo.getMimsUuid());
		screenPO.setResourceId(vo.getResourceId());
		screenPO.setScreenIndex(vo.getIndex());
		screenPO.setContentType(vo.getContentType());
		screenPO.setTextContent(vo.getTextContent());
		screenPO.setSerialNum(vo.getSerialNum());
		screenPO.setName(vo.getName());
		screenPO.setType(vo.getType());
		screenPO.setMimetype(vo.getMimetype());
		screenPO.setPreviewUrl(vo.getPreviewUrl());
		screenPO.setEncryptionUrl(vo.getEncryptionUrl());
		screenPO.setHotWeight(vo.getHotWeight());
		screenPO.setDownloadCount(vo.getDownloadCount());
		screenPO.setDuration(vo.getDuration());
		screenPO.setSize(vo.getSize());
		screenPO.setFreq(vo.getFreq());
		screenPO.setAudioPid(vo.getAudioPid());
		screenPO.setVideoPid(vo.getVideoPid());
		screenPO.setAudioType(vo.getAudioType());
		screenPO.setVideoType(vo.getVideoType());
		screenPO.setUpdateTime(new Date());
		return screenPO;
	}

	public Long getProgramId() {
		return programId;
	}


	public ScreenVO setProgramId(Long programId) {
		this.programId = programId;
		return this;
	}


	public Long getSerialNum() {
		return serialNum;
	}

	public ScreenVO setSerialNum(Long serialNum) {
		this.serialNum = serialNum;
		return this;
	}
	
	public String getContentType() {
		return contentType;
	}

	public ScreenVO setContentType(String contentType) {
		this.contentType = contentType;
		return this;
	}

	public String getTextContent() {
		return textContent;
	}

	public ScreenVO setTextContent(String textContent) {
		this.textContent = textContent;
		return this;
	}

	public Long getIndex() {
		return index;
	}

	public ScreenVO setIndex(Long index) {
		this.index = index;
		return this;
	}

	public String getMimsUuid() {
		return mimsUuid;
	}

	public ScreenVO setMimsUuid(String mimsUuid) {
		this.mimsUuid = mimsUuid;
		return this;
	}
	
	public Long getResourceId() {
		return resourceId;
	}

	public ScreenVO setResourceId(Long resourceId) {
		this.resourceId = resourceId;
		return this;
	}

	public String getName() {
		return name;
	}

	public ScreenVO setName(String name) {
		this.name = name;
		return this;
	}

	public String getType() {
		return type;
	}

	public ScreenVO setType(String type) {
		this.type = type;
		return this;
	}

	public String getMimetype() {
		return mimetype;
	}

	public ScreenVO setMimetype(String mimetype) {
		this.mimetype = mimetype;
		return this;
	}

	public String getPreviewUrl() {
		return previewUrl;
	}

	public ScreenVO setPreviewUrl(String previewUrl) {
		this.previewUrl = previewUrl;
		return this;
	}
	
	public String getEncryption() {
		return encryption;
	}

	public ScreenVO setEncryption(String encryption) {
		this.encryption = encryption;
		return this;
	}

	public String getEncryptionUrl() {
		return encryptionUrl;
	}

	public ScreenVO setEncryptionUrl(String encryptionUrl) {
		this.encryptionUrl = encryptionUrl;
		return this;
	}

	public Integer getHotWeight() {
		return hotWeight;
	}

	public ScreenVO setHotWeight(Integer hotWeight) {
		this.hotWeight = hotWeight;
		return this;
	}

	public Integer getDownloadCount() {
		return downloadCount;
	}


	public ScreenVO setDownloadCount(Integer downloadCount) {
		this.downloadCount = downloadCount;
		return this;
	}

	public String getDuration() {
		return duration;
	}


	public ScreenVO setDuration(String duration) {
		this.duration = duration;
		return this;
	}

	public String getSize() {
		return size;
	}

	public ScreenVO setSize(String size) {
		this.size = size;
		return this;
	}

	public String getFreq() {
		return freq;
	}

	public ScreenVO setFreq(String freq) {
		this.freq = freq;
		return this;
	}

	public String getAudioPid() {
		return audioPid;
	}

	public ScreenVO setAudioPid(String audioPid) {
		this.audioPid = audioPid;
		return this;
	}

	public String getVideoPid() {
		return videoPid;
	}

	public ScreenVO setVideoPid(String videoPid) {
		this.videoPid = videoPid;
		return this;
	}
	
	public String getAudioType() {
		return audioType;
	}

	public ScreenVO setAudioType(String audioType) {
		this.audioType = audioType;
		return this;
	}

	public String getVideoType() {
		return videoType;
	}

	public ScreenVO setVideoType(String videoType) {
		this.videoType = videoType;
		return this;
	}

	public static final class ScreenVOOrderComparator implements Comparator<ScreenVO>{
		@Override
		public int compare(ScreenVO o1, ScreenVO o2) {
			
			if(o1.getIndex() > o2.getIndex()){
				return 1;
			}
			if(o1.getIndex() == o2.getIndex()){
				return 0;
			}
			return -1;
		}
	}
}
