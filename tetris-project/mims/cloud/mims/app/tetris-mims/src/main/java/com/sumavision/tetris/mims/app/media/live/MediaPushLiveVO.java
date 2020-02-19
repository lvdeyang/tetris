package com.sumavision.tetris.mims.app.media.live;

import java.util.Arrays;
import java.util.List;

import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mims.app.folder.FolderPO;
import com.sumavision.tetris.mims.app.media.stream.audio.MediaAudioStreamPO;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class MediaPushLiveVO extends AbstractBaseVO<MediaPushLiveVO, MediaPushLivePO>{
	private String name;
	
	private String authorName;
	
	private String createTime;
	
	private String remarks;
	
	private List<String> tags;
	
	private List<String> keyWords;
	
	private String type;
	
	private String resourceType;
	
	private boolean removeable;
	
	private String icon;
	
	private String style;
	
	private String freq;
	
	private String audioPid;
	
	private String videoPid;
	
	private String reviewStatus;
	
	private String processInstanceId;
	
	private List<MediaPushLiveVO> children;

	public String getName() {
		return name;
	}

	public MediaPushLiveVO setName(String name) {
		this.name = name;
		return this;
	}

	public String getAuthorName() {
		return authorName;
	}

	public MediaPushLiveVO setAuthorName(String authorName) {
		this.authorName = authorName;
		return this;
	}

	public String getCreateTime() {
		return createTime;
	}

	public MediaPushLiveVO setCreateTime(String createTime) {
		this.createTime = createTime;
		return this;
	}

	public String getRemarks() {
		return remarks;
	}

	public MediaPushLiveVO setRemarks(String remarks) {
		this.remarks = remarks;
		return this;
	}

	public List<String> getTags() {
		return tags;
	}

	public MediaPushLiveVO setTags(List<String> tags) {
		this.tags = tags;
		return this;
	}

	public List<String> getKeyWords() {
		return keyWords;
	}

	public MediaPushLiveVO setKeyWords(List<String> keyWords) {
		this.keyWords = keyWords;
		return this;
	}

	public String getType() {
		return type;
	}

	public MediaPushLiveVO setType(String type) {
		this.type = type;
		return this;
	}

	public String getResourceType() {
		return resourceType;
	}

	public MediaPushLiveVO setResourceType(String resourceType) {
		this.resourceType = resourceType;
		return this;
	}

	public boolean isRemoveable() {
		return removeable;
	}

	public MediaPushLiveVO setRemoveable(boolean removeable) {
		this.removeable = removeable;
		return this;
	}

	public String getIcon() {
		return icon;
	}

	public MediaPushLiveVO setIcon(String icon) {
		this.icon = icon;
		return this;
	}

	public String getStyle() {
		return style;
	}

	public MediaPushLiveVO setStyle(String style) {
		this.style = style;
		return this;
	}

	public String getFreq() {
		return freq;
	}

	public MediaPushLiveVO setFreq(String freq) {
		this.freq = freq;
		return this;
	}

	public String getAudioPid() {
		return audioPid;
	}

	public MediaPushLiveVO setAudioPid(String audioPid) {
		this.audioPid = audioPid;
		return this;
	}

	public String getVideoPid() {
		return videoPid;
	}

	public MediaPushLiveVO setVideoPid(String videoPid) {
		this.videoPid = videoPid;
		return this;
	}

	public String getReviewStatus() {
		return reviewStatus;
	}

	public MediaPushLiveVO setReviewStatus(String reviewStatus) {
		this.reviewStatus = reviewStatus;
		return this;
	}

	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public MediaPushLiveVO setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
		return this;
	}

	public List<MediaPushLiveVO> getChildren() {
		return children;
	}

	public MediaPushLiveVO setChildren(List<MediaPushLiveVO> children) {
		this.children = children;
		return this;
	}
	
	@Override
	public MediaPushLiveVO set(MediaPushLivePO entity) throws Exception {
		this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setName(entity.getName())
			.setAuthorName(entity.getAuthorName())
			.setCreateTime(entity.getCreateTime()==null?"":DateUtil.format(entity.getCreateTime(), DateUtil.dateTimePattern))
			.setRemarks(entity.getRemarks())
			.setFreq(entity.getFreq())
			.setAudioPid(entity.getAudioPid())
			.setVideoPid(entity.getVideoPid())
			.setType(MediaPushLiveItemType.PUSH_LIVE.toString())
			.setRemoveable(true)
			.setIcon(MediaPushLiveItemType.PUSH_LIVE.getIcon())
			.setStyle(MediaPushLiveItemType.PUSH_LIVE.getStyle()[0])
			.setReviewStatus(entity.getReviewStatus()==null?"":entity.getReviewStatus().getName())
			.setProcessInstanceId(entity.getProcessInstanceId());
		if(entity.getTags() != null) this.setTags(Arrays.asList(entity.getTags().split(MediaPushLivePO.SEPARATOR_TAG)));
		if(entity.getKeyWords() != null) this.setKeyWords(Arrays.asList(entity.getKeyWords().split(MediaAudioStreamPO.SEPARATOR_KEYWORDS)));
		return this;
	}
	
	public MediaPushLiveVO set(FolderPO entity) throws Exception {
		this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setName(entity.getName())
			.setAuthorName(entity.getAuthorName())
			.setCreateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setRemarks("-")
			.setType(MediaPushLiveItemType.FOLDER.toString())
			.setResourceType(entity.getType().toString())
			.setRemoveable(entity.getDepth().intValue()==2?false:true)
			.setIcon(MediaPushLiveItemType.FOLDER.getIcon())
			.setStyle(MediaPushLiveItemType.FOLDER.getStyle()[0])
			.setReviewStatus("-");
		return this;
	}
}
