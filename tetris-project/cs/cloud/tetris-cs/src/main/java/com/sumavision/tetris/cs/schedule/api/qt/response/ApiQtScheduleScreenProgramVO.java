package com.sumavision.tetris.cs.schedule.api.qt.response;

import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.sumavision.tetris.cs.program.ScreenVO;

public class ApiQtScheduleScreenProgramVO {
	/** 播放顺序 */
	private Integer index;
	
	/** 类型(file/fileToStream/stream/text/time/picture) */
	private String mediaType;
	
	/** type为text时标记文字内容 */
	private String textContent;
	
	/** 时长 */
	private Long duration;
	
	/** 预览地址 */
	private String previewUrl;
	
	/** type为fileToStream时标记源文件地址 */
	private String source;
	
	/** 节目名 */
	private String name;
	
	/** 源文件下载次数 */
	private Integer downloadCount;
	
	/** 源文件推荐指数 */
	private Integer hotWeight;
	
	/** 是否加密 */
	private Boolean ifEncrypted;
	
	/** 加密文件地址 */
	private String encryptionUrl;
	
	/** 密钥 */
	private String encryptKey;
	
	/** 源uuid */
	private String mediaUuid;

	public Integer getIndex() {
		return index;
	}

	public ApiQtScheduleScreenProgramVO setIndex(Integer index) {
		this.index = index;
		return this;
	}

	public String getMediaType() {
		return mediaType;
	}

	public ApiQtScheduleScreenProgramVO setMediaType(String mediaType) {
		this.mediaType = mediaType;
		return this;
	}

	public String getTextContent() {
		return textContent;
	}

	public ApiQtScheduleScreenProgramVO setTextContent(String textContent) {
		this.textContent = textContent;
		return this;
	}

	public Long getDuration() {
		return duration;
	}

	public ApiQtScheduleScreenProgramVO setDuration(Long duration) {
		this.duration = duration;
		return this;
	}

	public String getPreviewUrl() {
		return previewUrl;
	}

	public ApiQtScheduleScreenProgramVO setPreviewUrl(String previewUrl) {
		this.previewUrl = previewUrl;
		return this;
	}

	public String getSource() {
		return source;
	}

	public ApiQtScheduleScreenProgramVO setSource(String source) {
		this.source = source;
		return this;
	}

	public String getName() {
		return name;
	}

	public ApiQtScheduleScreenProgramVO setName(String name) {
		this.name = name;
		return this;
	}

	public Integer getDownloadCount() {
		return downloadCount;
	}

	public ApiQtScheduleScreenProgramVO setDownloadCount(Integer downloadCount) {
		this.downloadCount = downloadCount;
		return this;
	}

	public Integer getHotWeight() {
		return hotWeight;
	}

	public ApiQtScheduleScreenProgramVO setHotWeight(Integer hotWeight) {
		this.hotWeight = hotWeight;
		return this;
	}

	public Boolean getIfEncrypted() {
		return ifEncrypted;
	}

	public ApiQtScheduleScreenProgramVO setIfEncrypted(Boolean ifEncrypted) {
		this.ifEncrypted = ifEncrypted;
		return this;
	}

	public String getEncryptionUrl() {
		return encryptionUrl;
	}

	public ApiQtScheduleScreenProgramVO setEncryptionUrl(String encryptionUrl) {
		this.encryptionUrl = encryptionUrl;
		return this;
	}

	public String getEncryptKey() {
		return encryptKey;
	}

	public ApiQtScheduleScreenProgramVO setEncryptKey(String encryptKey) {
		this.encryptKey = encryptKey;
		return this;
	}

	public String getMediaUuid() {
		return mediaUuid;
	}

	public ApiQtScheduleScreenProgramVO setMediaUuid(String mediaUuid) {
		this.mediaUuid = mediaUuid;
		return this;
	}
	
	public ApiQtScheduleScreenProgramVO setFromScreenVO(ScreenVO screenVO) {
		String screenDuration = screenVO.getDuration();
		String encryptionUrl = screenVO.getEncryptionUrl();
		
		String previewUrl = screenVO.getPreviewUrl();
		if ("VIDEO_STREAM".equals(screenVO.getType()) && previewUrl != null && !previewUrl.isEmpty()) {
			List<String> urls = JSONArray.parseArray(previewUrl, String.class);
			previewUrl = urls.isEmpty() ? "" : urls.get(0);
		}
		
		return this.setTextContent(screenVO.getTextContent())
				.setDuration(screenDuration == null || screenDuration.isEmpty() || screenDuration.equals("-") ? null : Long.parseLong(screenDuration))
				.setPreviewUrl(screenVO.getPreviewUrl())
				.setSource(previewUrl)
				.setName(screenVO.getName())
				.setDownloadCount(screenVO.getDownloadCount())
				.setHotWeight(screenVO.getHotWeight())
				.setIfEncrypted(encryptionUrl != null && !encryptionUrl.isEmpty())
				.setEncryptionUrl(encryptionUrl)
				.setMediaUuid(screenVO.getMimsUuid());
	}
}
