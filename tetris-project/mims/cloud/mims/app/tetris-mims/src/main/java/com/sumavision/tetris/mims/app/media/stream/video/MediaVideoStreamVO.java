package com.sumavision.tetris.mims.app.media.stream.video;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.mims.app.folder.FolderPO;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class MediaVideoStreamVO extends AbstractBaseVO<MediaVideoStreamVO, MediaVideoStreamPO>{

	private List<String> previewUrl;
	
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
	
	private List<MediaVideoStreamVO> children;
	
	public List<String> getPreviewUrl() {
		return previewUrl;
	}

	public MediaVideoStreamVO setPreviewUrl(List<String> previewUrl) {
		this.previewUrl = previewUrl;
		return this;
	}

	public String getName() {
		return name;
	}

	public MediaVideoStreamVO setName(String name) {
		this.name = name;
		return this;
	}

	public String getAuthorName() {
		return authorName;
	}

	public MediaVideoStreamVO setAuthorName(String authorName) {
		this.authorName = authorName;
		return this;
	}

	public String getCreateTime() {
		return createTime;
	}

	public MediaVideoStreamVO setCreateTime(String createTime) {
		this.createTime = createTime;
		return this;
	}

	public String getRemarks() {
		return remarks;
	}

	public MediaVideoStreamVO setRemarks(String remarks) {
		this.remarks = remarks;
		return this;
	}

	public List<String> getTags() {
		return tags;
	}

	public MediaVideoStreamVO setTags(List<String> tags) {
		this.tags = tags;
		return this;
	}

	public List<String> getKeyWords() {
		return keyWords;
	}

	public MediaVideoStreamVO setKeyWords(List<String> keyWords) {
		this.keyWords = keyWords;
		return this;
	}
	
	public String getThumbnail() {
		return thumbnail;
	}

	public MediaVideoStreamVO setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
		return this;
	}

	public String getStreamType() {
		return streamType;
	}

	public MediaVideoStreamVO setStreamType(String streamType) {
		this.streamType = streamType;
		return this;
	}

	public String getIgmpv3Status() {
		return igmpv3Status;
	}

	public MediaVideoStreamVO setIgmpv3Status(String igmpv3Status) {
		this.igmpv3Status = igmpv3Status;
		return this;
	}

	public String getIgmpv3Mode() {
		return igmpv3Mode;
	}

	public MediaVideoStreamVO setIgmpv3Mode(String igmpv3Mode) {
		this.igmpv3Mode = igmpv3Mode;
		return this;
	}

	public List<String> getIgmpv3IpArray() {
		return igmpv3IpArray;
	}

	public MediaVideoStreamVO setIgmpv3IpArray(List<String> igmpv3IpArray) {
		this.igmpv3IpArray = igmpv3IpArray;
		return this;
	}

	public String getType() {
		return type;
	}

	public MediaVideoStreamVO setType(String type) {
		this.type = type;
		return this;
	}

	public String getResourceType() {
		return resourceType;
	}

	public MediaVideoStreamVO setResourceType(String resourceType) {
		this.resourceType = resourceType;
		return this;
	}

	public boolean isRemoveable() {
		return removeable;
	}

	public MediaVideoStreamVO setRemoveable(boolean removeable) {
		this.removeable = removeable;
		return this;
	}

	public String getIcon() {
		return icon;
	}

	public MediaVideoStreamVO setIcon(String icon) {
		this.icon = icon;
		return this;
	}

	public String getStyle() {
		return style;
	}

	public MediaVideoStreamVO setStyle(String style) {
		this.style = style;
		return this;
	}
	
	public String getReviewStatus() {
		return reviewStatus;
	}

	public MediaVideoStreamVO setReviewStatus(String reviewStatus) {
		this.reviewStatus = reviewStatus;
		return this;
	}

	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public MediaVideoStreamVO setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
		return this;
	}

	public String getAddition() {
		return addition;
	}

	public MediaVideoStreamVO setAddition(String addition) {
		this.addition = addition;
		return this;
	}

	public List<MediaVideoStreamVO> getChildren() {
		return children;
	}

	public MediaVideoStreamVO setChildren(List<MediaVideoStreamVO> children) {
		this.children = children;
		return this;
	}

	@Override
	public MediaVideoStreamVO set(MediaVideoStreamPO entity) throws Exception {
		this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setName(entity.getName())
			.setAuthorName(entity.getAuthorName())
			.setCreateTime(entity.getCreateTime()==null?"":DateUtil.format(entity.getCreateTime(), DateUtil.dateTimePattern))
			.setThumbnail(entity.getThumbnail())
			.setRemarks(entity.getRemarks())
			.setStreamType(entity.getStreamType())
			.setIgmpv3Status(entity.getIgmpv3Status()==null?"close":entity.getIgmpv3Status())
			.setIgmpv3Mode(entity.getIgmpv3Mode())
			.setType(MediaVideoStreamItemType.VIDEO_STREAM.toString())
			.setRemoveable(true)
			.setIcon(MediaVideoStreamItemType.VIDEO_STREAM.getIcon())
			.setStyle(MediaVideoStreamItemType.VIDEO_STREAM.getStyle()[0])
			.setReviewStatus(entity.getReviewStatus()==null?"":entity.getReviewStatus().getName())
			.setProcessInstanceId(entity.getProcessInstanceId())
			.setAddition(entity.getAddition())
			.setTags(entity.getTags() != null && !entity.getTags().isEmpty() ? Arrays.asList(entity.getTags().split(MediaVideoStreamPO.SEPARATOR_TAG)) : new ArrayList<String>());
		if(entity.getKeyWords() != null) this.setKeyWords(Arrays.asList(entity.getKeyWords().split(MediaVideoStreamPO.SEPARATOR_KEYWORDS)));
		if (entity.getPreviewUrl() != null) this.setPreviewUrl(new ArrayListWrapper<String>().add(entity.getPreviewUrl()).getList());
		if(entity.getIgmpv3Ips() != null) this.setIgmpv3IpArray(Arrays.asList(entity.getIgmpv3Ips().split(MediaVideoStreamPO.SEPARATOR_IPS)));
		return this;
	}
	
	public MediaVideoStreamVO set(FolderPO entity) throws Exception {
		this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setName(entity.getName())
			.setAuthorName(entity.getAuthorName())
			.setCreateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setRemarks("-")
			.setType(MediaVideoStreamItemType.FOLDER.toString())
			.setResourceType(entity.getType().toString())
			.setRemoveable(entity.getDepth().intValue()==2?false:true)
			.setIcon(MediaVideoStreamItemType.FOLDER.getIcon())
			.setStyle(MediaVideoStreamItemType.FOLDER.getStyle()[0])
			.setReviewStatus("-");
		return this;
	}
	
}
