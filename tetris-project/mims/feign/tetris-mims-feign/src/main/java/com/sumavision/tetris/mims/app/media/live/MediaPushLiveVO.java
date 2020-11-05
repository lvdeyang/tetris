package com.sumavision.tetris.mims.app.media.live;

import java.util.List;

public class MediaPushLiveVO {
	private Long id;
	
	private String uuid;

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
	
	private String audioType;
	
	private String videoType;
	
	private List<MediaPushLiveVO> children;

	public Long getId() {
		return id;
	}

	public MediaPushLiveVO setId(Long id) {
		this.id = id;
		return this;
	}

	public String getUuid() {
		return uuid;
	}

	public MediaPushLiveVO setUuid(String uuid) {
		this.uuid = uuid;
		return this;
	}

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

	public String getAudioType() {
		return audioType;
	}

	public MediaPushLiveVO setAudioType(String audioType) {
		this.audioType = audioType;
		return this;
	}

	public String getVideoType() {
		return videoType;
	}

	public MediaPushLiveVO setVideoType(String videoType) {
		this.videoType = videoType;
		return this;
	}

	public List<MediaPushLiveVO> getChildren() {
		return children;
	}

	public MediaPushLiveVO setChildren(List<MediaPushLiveVO> children) {
		this.children = children;
		return this;
	}
}
