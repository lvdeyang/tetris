package com.sumavision.tetris.mims.app.media.txt;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.sumavision.tetris.commons.context.SpringContext;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.mims.app.folder.FolderPO;
import com.sumavision.tetris.mims.config.server.MimsServerPropsQuery;
import com.sumavision.tetris.mims.config.server.ServerProps;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class MediaTxtVO extends AbstractBaseVO<MediaTxtVO, MediaTxtPO>{

	private String content;
	
	private String name;
	
	private String authorName;
	
	private String createTime;
	
	private String remarks;
	
	private List<String> tags;
	
	private List<String> keyWords;
	
	private String thumbnail;
	
	private String type;
	
	private String resourceType;
	
	private boolean removeable;
	
	private String icon;
	
	private String style;
	
	private String previewUrl;
	
	private String uploadTmpPath;
	
	private String reviewStatus;
	
	private String processInstanceId;
	
	private Long size;
	
	private Integer progress;
	
	private String addition;
	
	private List<MediaTxtVO> children;
	
	public String getContent() {
		return content;
	}

	public MediaTxtVO setContent(String content) {
		this.content = content;
		return this;
	}

	public String getName() {
		return name;
	}

	public MediaTxtVO setName(String name) {
		this.name = name;
		return this;
	}

	public String getAuthorName() {
		return authorName;
	}

	public MediaTxtVO setAuthorName(String authorName) {
		this.authorName = authorName;
		return this;
	}

	public String getCreateTime() {
		return createTime;
	}

	public MediaTxtVO setCreateTime(String createTime) {
		this.createTime = createTime;
		return this;
	}

	public String getRemarks() {
		return remarks;
	}

	public MediaTxtVO setRemarks(String remarks) {
		this.remarks = remarks;
		return this;
	}

	public List<String> getTags() {
		return tags;
	}

	public MediaTxtVO setTags(List<String> tags) {
		this.tags = tags;
		return this;
	}

	public List<String> getKeyWords() {
		return keyWords;
	}

	public MediaTxtVO setKeyWords(List<String> keyWords) {
		this.keyWords = keyWords;
		return this;
	}
	
	public String getThumbnail() {
		return thumbnail;
	}

	public MediaTxtVO setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
		return this;
	}

	public String getType() {
		return type;
	}

	public MediaTxtVO setType(String type) {
		this.type = type;
		return this;
	}

	public String getResourceType() {
		return resourceType;
	}

	public MediaTxtVO setResourceType(String resourceType) {
		this.resourceType = resourceType;
		return this;
	}

	public boolean isRemoveable() {
		return removeable;
	}

	public MediaTxtVO setRemoveable(boolean removeable) {
		this.removeable = removeable;
		return this;
	}

	public String getIcon() {
		return icon;
	}

	public MediaTxtVO setIcon(String icon) {
		this.icon = icon;
		return this;
	}

	public String getStyle() {
		return style;
	}

	public MediaTxtVO setStyle(String style) {
		this.style = style;
		return this;
	}
	
	public String getPreviewUrl() {
		return previewUrl;
	}

	public MediaTxtVO setPreviewUrl(String previewUrl) {
		this.previewUrl = previewUrl;
		return this;
	}
	
	public String getUploadTmpPath() {
		return uploadTmpPath;
	}

	public MediaTxtVO setUploadTmpPath(String uploadTmpPath) {
		this.uploadTmpPath = uploadTmpPath;
		return this;
	}

	public String getReviewStatus() {
		return reviewStatus;
	}

	public MediaTxtVO setReviewStatus(String reviewStatus) {
		this.reviewStatus = reviewStatus;
		return this;
	}

	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public MediaTxtVO setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
		return this;
	}

	public Long getSize() {
		return size;
	}

	public MediaTxtVO setSize(Long size) {
		this.size = size;
		return this;
	}

	public Integer getProgress() {
		return progress;
	}

	public MediaTxtVO setProgress(Integer progress) {
		this.progress = progress;
		return this;
	}

	public String getAddition() {
		return addition;
	}

	public MediaTxtVO setAddition(String addition) {
		this.addition = addition;
		return this;
	}

	public List<MediaTxtVO> getChildren() {
		return children;
	}

	public void setChildren(List<MediaTxtVO> children) {
		this.children = children;
	}

	@Override
	public MediaTxtVO set(MediaTxtPO entity) throws Exception {
		ServerProps serverProps = SpringContext.getBean(ServerProps.class);
        MimsServerPropsQuery serverPropsQuery = SpringContext.getBean(MimsServerPropsQuery.class);

		this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setContent(entity.getContent())
			.setName(entity.getName())
			.setAuthorName(entity.getAuthorName())
			.setCreateTime(entity.getCreateTime()==null?"":DateUtil.format(entity.getCreateTime(), DateUtil.dateTimePattern))
			.setThumbnail(entity.getThumbnail())
			.setRemarks(entity.getRemarks())
			.setPreviewUrl(new StringBufferWrapper().append("http://").append(serverPropsQuery.queryProps().getFtpIp()).append(":").append(serverProps.getPort()).append("/").append(entity.getPreviewUrl()).toString())
			.setUploadTmpPath(entity.getUploadTmpPath())
			.setReviewStatus(entity.getReviewStatus()==null?"":entity.getReviewStatus().getName())
			.setProcessInstanceId(entity.getProcessInstanceId())
			.setSize(entity.getSize())
			.setType(MediaTxtItemType.TXT.toString())
			.setRemoveable(true)
			.setIcon(MediaTxtItemType.TXT.getIcon())
			.setStyle(MediaTxtItemType.TXT.getStyle()[0])
			.setAddition(entity.getAddition())
			.setTags(entity.getTags() != null && !entity.getTags().isEmpty() ? Arrays.asList(entity.getTags().split(MediaTxtPO.SEPARATOR_TAG)) : new ArrayList<String>());
		if(entity.getKeyWords() != null) this.setKeyWords(Arrays.asList(entity.getKeyWords().split(MediaTxtPO.SEPARATOR_KEYWORDS)));	 
		return this;
	}
	
	public MediaTxtVO set(FolderPO entity) throws Exception {
		this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setContent("-")
			.setName(entity.getName())
			.setAuthorName(entity.getAuthorName())
			.setCreateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setRemarks("-")
			.setType(MediaTxtItemType.FOLDER.toString())
			.setResourceType(entity.getType().toString())
			.setRemoveable(entity.getDepth().intValue()==2?false:true)
			.setIcon(MediaTxtItemType.FOLDER.getIcon())
			.setStyle(MediaTxtItemType.FOLDER.getStyle()[0])
			.setReviewStatus("-");
		return this;
	}
	
}
