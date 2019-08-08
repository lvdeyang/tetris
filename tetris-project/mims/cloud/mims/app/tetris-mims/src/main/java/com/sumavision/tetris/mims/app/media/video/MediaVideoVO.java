package com.sumavision.tetris.mims.app.media.video;

import java.util.Arrays;
import java.util.List;

import com.sumavision.tetris.commons.context.SpringContext;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.mims.app.folder.FolderPO;
import com.sumavision.tetris.mims.config.server.ServerProps;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class MediaVideoVO extends AbstractBaseVO<MediaVideoVO, MediaVideoPO>{

	private String name;
	
	private String authorName;
	
	private String size;
	
	private String createTime;
	
	private String version;
	
	private String remarks;
	
	private List<String> tags;
	
	private List<String> keyWords;
	
	private String type;
	
	private String icon;
	
	private String style;
	
	private String mimetype;
	
	private Integer progress;
	
	private String previewUrl;
	
	private String reviewStatus;
	
	private String processInstanceId;
	
	private List<MediaVideoVO> children;
	
	public String getName() {
		return name;
	}

	public MediaVideoVO setName(String name) {
		this.name = name;
		return this;
	}

	public String getAuthorName() {
		return authorName;
	}

	public MediaVideoVO setAuthorName(String authorName) {
		this.authorName = authorName;
		return this;
	}

	public String getSize() {
		return size;
	}

	public MediaVideoVO setSize(String size) {
		this.size = size;
		return this;
	}

	public String getCreateTime() {
		return createTime;
	}

	public MediaVideoVO setCreateTime(String createTime) {
		this.createTime = createTime;
		return this;
	}

	public String getVersion() {
		return version;
	}

	public MediaVideoVO setVersion(String version) {
		this.version = version;
		return this;
	}

	public String getRemarks() {
		return remarks;
	}

	public MediaVideoVO setRemarks(String remarks) {
		this.remarks = remarks;
		return this;
	}

	public List<String> getTags() {
		return tags;
	}

	public MediaVideoVO setTags(List<String> tags) {
		this.tags = tags;
		return this;
	}

	public List<String> getKeyWords() {
		return keyWords;
	}

	public MediaVideoVO setKeyWords(List<String> keyWords) {
		this.keyWords = keyWords;
		return this;
	}
	
	public String getType() {
		return type;
	}

	public MediaVideoVO setType(String type) {
		this.type = type;
		return this;
	}

	public String getIcon() {
		return icon;
	}

	public MediaVideoVO setIcon(String icon) {
		this.icon = icon;
		return this;
	}

	public String getStyle() {
		return style;
	}

	public MediaVideoVO setStyle(String style) {
		this.style = style;
		return this;
	}
	
	public String getMimetype() {
		return mimetype;
	}

	public MediaVideoVO setMimetype(String mimetype) {
		this.mimetype = mimetype;
		return this;
	}

	public Integer getProgress() {
		return progress;
	}

	public MediaVideoVO setProgress(Integer progress) {
		this.progress = progress;
		return this;
	}

	public String getPreviewUrl() {
		return previewUrl;
	}

	public MediaVideoVO setPreviewUrl(String previewUrl) {
		this.previewUrl = previewUrl;
		return this;
	}

	public List<MediaVideoVO> getChildren() {
		return children;
	}

	public MediaVideoVO setChildren(List<MediaVideoVO> children) {
		this.children = children;
		return this;
	}

	public String getReviewStatus() {
		return reviewStatus;
	}

	public MediaVideoVO setReviewStatus(String reviewStatus) {
		this.reviewStatus = reviewStatus;
		return this;
	}

	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public MediaVideoVO setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
		return this;
	}

	@Override
	public MediaVideoVO set(MediaVideoPO entity) throws Exception {
		ServerProps serverProps = SpringContext.getBean(ServerProps.class);
		this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setName(entity.getName())
			.setAuthorName(entity.getAuthorName())
			.setSize(entity.getSize().toString())
			.setCreateTime(entity.getCreateTime()==null?"":DateUtil.format(entity.getCreateTime(), DateUtil.dateTimePattern))
			.setVersion(entity.getVersion())
			.setRemarks(entity.getRemarks())
			.setType(MediaVideoItemType.VIDEO.toString())
			.setIcon(MediaVideoItemType.VIDEO.getIcon())
			.setStyle(MediaVideoItemType.VIDEO.getStyle()[0])
			.setMimetype(entity.getMimetype())
			.setProgress(0)
			.setPreviewUrl(new StringBufferWrapper().append("http://").append(serverProps.getIp()).append(":").append(serverProps.getPort()).append("/").append(entity.getPreviewUrl()).toString())
			.setReviewStatus(entity.getReviewStatus()==null?"":entity.getReviewStatus().getName())
			.setProcessInstanceId(entity.getProcessInstanceId());;
		if(entity.getTags() != null) this.setTags(Arrays.asList(entity.getTags().split(MediaVideoPO.SEPARATOR_TAG)));
		if(entity.getKeyWords() != null) this.setKeyWords(Arrays.asList(entity.getKeyWords().split(MediaVideoPO.SEPARATOR_KEYWORDS)));	 
		return this;
	}
	
	public MediaVideoVO set(FolderPO entity) throws Exception {
		this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setName(entity.getName())
			.setAuthorName(entity.getAuthorName())
			.setSize("-")
			.setCreateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setVersion("-")
			.setRemarks("-")
			.setType(MediaVideoItemType.FOLDER.toString())
			.setIcon(MediaVideoItemType.FOLDER.getIcon())
			.setStyle(MediaVideoItemType.FOLDER.getStyle()[0])
			.setReviewStatus("-");
		return this;
	}
	
}
