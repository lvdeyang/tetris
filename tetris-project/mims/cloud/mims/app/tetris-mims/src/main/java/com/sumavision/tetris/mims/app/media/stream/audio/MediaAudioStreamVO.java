package com.sumavision.tetris.mims.app.media.stream.audio;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mims.app.folder.FolderPO;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class MediaAudioStreamVO extends AbstractBaseVO<MediaAudioStreamVO, MediaAudioStreamPO>{

	private String previewUrl;
	
	private String name;
	
	private String authorName;
	
	private String createTime;
	
	private String remarks;
	
	private List<String> tags;
	
	private List<String> keyWords;
	
	private String thumbnail;
	
	private String streamType;
	
	private String igmpv3Status;
	
	private String igmpv3Mode;
	
	private List<String> igmpv3IpArray;
	
	private String type;
	
	private String resourceType;
	
	private boolean removeable;
	
	private String icon;
	
	private String style;
	
	private String reviewStatus;
	
	private String processInstanceId;
	
	private String addition;
	
	private List<MediaAudioStreamVO> children;
	
	public String getPreviewUrl() {
		return previewUrl;
	}

	public MediaAudioStreamVO setPreviewUrl(String previewUrl) {
		this.previewUrl = previewUrl;
		return this;
	}

	public String getName() {
		return name;
	}

	public MediaAudioStreamVO setName(String name) {
		this.name = name;
		return this;
	}

	public String getAuthorName() {
		return authorName;
	}

	public MediaAudioStreamVO setAuthorName(String authorName) {
		this.authorName = authorName;
		return this;
	}

	public String getCreateTime() {
		return createTime;
	}

	public MediaAudioStreamVO setCreateTime(String createTime) {
		this.createTime = createTime;
		return this;
	}

	public String getRemarks() {
		return remarks;
	}

	public MediaAudioStreamVO setRemarks(String remarks) {
		this.remarks = remarks;
		return this;
	}

	public List<String> getTags() {
		return tags;
	}

	public MediaAudioStreamVO setTags(List<String> tags) {
		this.tags = tags;
		return this;
	}

	public List<String> getKeyWords() {
		return keyWords;
	}

	public MediaAudioStreamVO setKeyWords(List<String> keyWords) {
		this.keyWords = keyWords;
		return this;
	}
	
	public String getThumbnail() {
		return thumbnail;
	}

	public MediaAudioStreamVO setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
		return this;
	}

	public String getStreamType() {
		return streamType;
	}

	public MediaAudioStreamVO setStreamType(String streamType) {
		this.streamType = streamType;
		return this;
	}

	public String getIgmpv3Status() {
		return igmpv3Status;
	}

	public MediaAudioStreamVO setIgmpv3Status(String igmpv3Status) {
		this.igmpv3Status = igmpv3Status;
		return this;
	}

	public String getIgmpv3Mode() {
		return igmpv3Mode;
	}

	public MediaAudioStreamVO setIgmpv3Mode(String igmpv3Mode) {
		this.igmpv3Mode = igmpv3Mode;
		return this;
	}

	public List<String> getIgmpv3IpArray() {
		return igmpv3IpArray;
	}

	public MediaAudioStreamVO setIgmpv3IpArray(List<String> igmpv3IpArray) {
		this.igmpv3IpArray = igmpv3IpArray;
		return this;
	}

	public String getType() {
		return type;
	}

	public MediaAudioStreamVO setType(String type) {
		this.type = type;
		return this;
	}

	public String getResourceType() {
		return resourceType;
	}

	public MediaAudioStreamVO setResourceType(String resourceType) {
		this.resourceType = resourceType;
		return this;
	}

	public boolean isRemoveable() {
		return removeable;
	}

	public MediaAudioStreamVO setRemoveable(boolean removeable) {
		this.removeable = removeable;
		return this;
	}

	public String getIcon() {
		return icon;
	}

	public MediaAudioStreamVO setIcon(String icon) {
		this.icon = icon;
		return this;
	}

	public String getStyle() {
		return style;
	}

	public MediaAudioStreamVO setStyle(String style) {
		this.style = style;
		return this;
	}
	
	public String getReviewStatus() {
		return reviewStatus;
	}

	public MediaAudioStreamVO setReviewStatus(String reviewStatus) {
		this.reviewStatus = reviewStatus;
		return this;
	}

	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public MediaAudioStreamVO setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
		return this;
	}

	public String getAddition() {
		return addition;
	}

	public MediaAudioStreamVO setAddition(String addition) {
		this.addition = addition;
		return this;
	}

	public List<MediaAudioStreamVO> getChildren() {
		return children;
	}

	public MediaAudioStreamVO setChildren(List<MediaAudioStreamVO> children) {
		this.children = children;
		return this;
	}

	@Override
	public MediaAudioStreamVO set(MediaAudioStreamPO entity) throws Exception {
		this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setPreviewUrl(entity.getPreviewUrl())
			.setName(entity.getName())
			.setAuthorName(entity.getAuthorName())
			.setCreateTime(entity.getCreateTime()==null?"":DateUtil.format(entity.getCreateTime(), DateUtil.dateTimePattern))
			.setRemarks(entity.getRemarks())
			.setThumbnail(entity.getThumbnail())
			.setStreamType(entity.getStreamType())
			.setIgmpv3Status(entity.getIgmpv3Status()==null?"close":entity.getIgmpv3Status())
			.setIgmpv3Mode(entity.getIgmpv3Mode())
			.setType(MediaAudioStreamItemType.AUDIO_STREAM.toString())
			.setRemoveable(true)
			.setIcon(MediaAudioStreamItemType.AUDIO_STREAM.getIcon())
			.setStyle(MediaAudioStreamItemType.AUDIO_STREAM.getStyle()[0])
			.setReviewStatus(entity.getReviewStatus()==null?"":entity.getReviewStatus().getName())
			.setProcessInstanceId(entity.getProcessInstanceId())
			.setAddition(entity.getAddition())
			.setTags(entity.getTags() != null && !entity.getTags().isEmpty() ? Arrays.asList(entity.getTags().split(MediaAudioStreamPO.SEPARATOR_TAG)) : new ArrayList<String>());
		if(entity.getKeyWords() != null) this.setKeyWords(Arrays.asList(entity.getKeyWords().split(MediaAudioStreamPO.SEPARATOR_KEYWORDS)));
		if(entity.getIgmpv3Ips() != null) this.setIgmpv3IpArray(Arrays.asList(entity.getIgmpv3Ips().split(MediaAudioStreamPO.SEPARATOR_IPS)));
		return this;
	}
	
	public MediaAudioStreamVO set(FolderPO entity) throws Exception {
		this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setPreviewUrl("-")
			.setName(entity.getName())
			.setAuthorName(entity.getAuthorName())
			.setCreateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setRemarks("-")
			.setType(MediaAudioStreamItemType.FOLDER.toString())
			.setResourceType(entity.getType().toString())
			.setRemoveable(entity.getDepth().intValue()==2?false:true)
			.setIcon(MediaAudioStreamItemType.FOLDER.getIcon())
			.setStyle(MediaAudioStreamItemType.FOLDER.getStyle()[0])
			.setReviewStatus("-");
		return this;
	}
	
}
