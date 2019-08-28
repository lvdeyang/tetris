package com.sumavision.tetris.mims.app.media.audio;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.sumavision.tetris.commons.context.SpringContext;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.mims.app.folder.FolderPO;
import com.sumavision.tetris.mims.app.media.StoreType;
import com.sumavision.tetris.mims.config.server.ServerProps;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class MediaAudioVO extends AbstractBaseVO<MediaAudioVO, MediaAudioPO>{

	private String name;
	
	private String authorName;
	
	private String size;
	
	private String createTime;
	
	private String version;
	
	private String remarks;
	
	private StoreType storeType;
	
	private String uploadTmpPath;
	
	private List<String> tags;
	
	private List<String> keyWords;
	
	private Long downloadCount;
	
	private String type;
	
	private boolean removeable;
	
	private String icon;
	
	private String style;
	
	private String mimetype;
	
	private Integer progress;
	
	private String previewUrl;
	
	private String reviewStatus;
	
	private String processInstanceId;
	
	private List<MediaAudioVO> children;
	
	public String getName() {
		return name;
	}

	public MediaAudioVO setName(String name) {
		this.name = name;
		return this;
	}

	public String getAuthorName() {
		return authorName;
	}

	public MediaAudioVO setAuthorName(String authorName) {
		this.authorName = authorName;
		return this;
	}

	public String getSize() {
		return size;
	}

	public MediaAudioVO setSize(String size) {
		this.size = size;
		return this;
	}

	public String getCreateTime() {
		return createTime;
	}

	public MediaAudioVO setCreateTime(String createTime) {
		this.createTime = createTime;
		return this;
	}

	public String getVersion() {
		return version;
	}

	public MediaAudioVO setVersion(String version) {
		this.version = version;
		return this;
	}

	public String getRemarks() {
		return remarks;
	}

	public MediaAudioVO setRemarks(String remarks) {
		this.remarks = remarks;
		return this;
	}

	public StoreType getStoreType() {
		return storeType;
	}

	public MediaAudioVO setStoreType(StoreType storeType) {
		this.storeType = storeType;
		return this;
	}

	public String getUploadTmpPath() {
		return uploadTmpPath;
	}

	public MediaAudioVO setUploadTmpPath(String uploadTmpPath) {
		this.uploadTmpPath = uploadTmpPath;
		return this;
	}

	public List<String> getTags() {
		return tags;
	}

	public MediaAudioVO setTags(List<String> tags) {
		this.tags = tags;
		return this;
	}

	public List<String> getKeyWords() {
		return keyWords;
	}

	public MediaAudioVO setKeyWords(List<String> keyWords) {
		this.keyWords = keyWords;
		return this;
	}
	
	public Long getDownloadCount() {
		return downloadCount;
	}

	public MediaAudioVO setDownloadCount(Long downloadCount) {
		this.downloadCount = downloadCount;
		return this;
	}

	public String getType() {
		return type;
	}

	public MediaAudioVO setType(String type) {
		this.type = type;
		return this;
	}

	public boolean isRemoveable() {
		return removeable;
	}

	public MediaAudioVO setRemoveable(boolean removeable) {
		this.removeable = removeable;
		return this;
	}

	public String getIcon() {
		return icon;
	}

	public MediaAudioVO setIcon(String icon) {
		this.icon = icon;
		return this;
	}

	public String getStyle() {
		return style;
	}

	public MediaAudioVO setStyle(String style) {
		this.style = style;
		return this;
	}
	
	public String getMimetype() {
		return mimetype;
	}

	public MediaAudioVO setMimetype(String mimetype) {
		this.mimetype = mimetype;
		return this;
	}

	public Integer getProgress() {
		return progress;
	}

	public MediaAudioVO setProgress(Integer progress) {
		this.progress = progress;
		return this;
	}
	
	public String getPreviewUrl() {
		return previewUrl;
	}

	public MediaAudioVO setPreviewUrl(String previewUrl) {
		this.previewUrl = previewUrl;
		return this;
	}
	
	public String getReviewStatus() {
		return reviewStatus;
	}

	public MediaAudioVO setReviewStatus(String reviewStatus) {
		this.reviewStatus = reviewStatus;
		return this;
	}

	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public MediaAudioVO setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
		return this;
	}

	public List<MediaAudioVO> getChildren() {
		return children;
	}

	public MediaAudioVO setChildren(List<MediaAudioVO> children) {
		this.children = children;
		return this;
	}

	@Override
	public MediaAudioVO set(MediaAudioPO entity) throws Exception {
		ServerProps serverProps = SpringContext.getBean(ServerProps.class);
		this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setName(entity.getName())
			.setAuthorName(entity.getAuthorName())
			.setSize(entity.getSize() != null ? entity.getSize().toString() : "-")
			.setCreateTime(entity.getCreateTime()==null?"":DateUtil.format(entity.getCreateTime(), DateUtil.dateTimePattern))
			.setVersion(entity.getVersion())
			.setRemarks(entity.getRemarks())
			.setType(MediaAudioItemType.AUDIO.toString())
			.setRemoveable(true)
			.setIcon(MediaAudioItemType.AUDIO.getIcon())
			.setStyle(MediaAudioItemType.AUDIO.getStyle()[0])
			.setMimetype(entity.getMimetype())
			.setStoreType(entity.getStoreType())
			.setUploadTmpPath(entity.getUploadTmpPath())
			.setDownloadCount(entity.getDownloadCount())
			.setProgress(0)
			.setPreviewUrl((entity.getStoreType() == StoreType.REMOTE) ? entity.getPreviewUrl() : new StringBufferWrapper().append("http://").append(serverProps.getIp()).append(":").append(serverProps.getPort()).append("/").append(entity.getPreviewUrl()).toString())
			.setReviewStatus(entity.getReviewStatus()==null?"":entity.getReviewStatus().getName())
			.setProcessInstanceId(entity.getProcessInstanceId());
		if(entity.getTags() != null && !entity.getTags().isEmpty()) this.setTags(Arrays.asList(entity.getTags().split(MediaAudioPO.SEPARATOR_TAG))); else this.setTags(new ArrayList<String>());
		if(entity.getKeyWords() != null) this.setKeyWords(Arrays.asList(entity.getKeyWords().split(MediaAudioPO.SEPARATOR_KEYWORDS)));	 
		return this;
	}
	
	public MediaAudioVO set(FolderPO entity) throws Exception {
		this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setName(entity.getName())
			.setAuthorName(entity.getAuthorName())
			.setSize("-")
			.setCreateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setVersion("-")
			.setRemarks("-")
			.setType(MediaAudioItemType.FOLDER.toString())
			.setRemoveable(entity.getDepth().intValue()==2?false:true)
			.setIcon(MediaAudioItemType.FOLDER.getIcon())
			.setStyle(MediaAudioItemType.FOLDER.getStyle()[0])
			.setReviewStatus("-");
		return this;
	}
	
}
