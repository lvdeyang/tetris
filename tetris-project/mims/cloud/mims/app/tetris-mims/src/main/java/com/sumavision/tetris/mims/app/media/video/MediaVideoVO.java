package com.sumavision.tetris.mims.app.media.video;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.sumavision.tetris.commons.context.SpringContext;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.mims.app.folder.FolderPO;
import com.sumavision.tetris.mims.app.folder.FolderType;
import com.sumavision.tetris.mims.app.media.StoreType;
import com.sumavision.tetris.mims.app.media.editor.MediaFileEditorDAO;
import com.sumavision.tetris.mims.app.media.editor.MediaFileEditorPO;
import com.sumavision.tetris.mims.app.media.editor.MediaFileEditorVO;
import com.sumavision.tetris.mims.app.media.upload.MediaFileEquipmentPermissionPO;
import com.sumavision.tetris.mims.config.server.MimsServerPropsQuery;
import com.sumavision.tetris.mims.config.server.ServerProps;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class MediaVideoVO extends AbstractBaseVO<MediaVideoVO, MediaVideoPO>{

	private String name;
	
	private String authorName;
	
	private String size;
	
	private String createTime;
	
	private String duration;
	
	private String version;
	
	private String remarks;
	
	private StoreType storeType;
	
	private String uploadTmpPath;
	
	private Long folderId;
	
	private List<String> tags;
	
	private List<String> keyWords;
	
	private String thumbnail;
	
	private String type;
	
	private String resourceType;
	
	private boolean removeable;
	
	private String icon;
	
	private String style;
	
	private String mimetype;
	
	private Integer progress;
	
	private String previewUrl;
	
	private String reviewStatus;
	
	private String processInstanceId;
	
	private Boolean encryption;
	
	private String encryptionUrl;
	
	private String addition;
	
	private MediaFileEditorVO editorInfo;
	
	private String fileName;
	
	private List<MediaFileEquipmentPermissionPO> deviceUpload;
	
	private List<MediaVideoVO> children;
	
	private String ftpUrl;
	
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

	public String getDuration() {
		return duration;
	}

	public MediaVideoVO setDuration(String duration) {
		this.duration = duration;
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

	public StoreType getStoreType() {
		return storeType;
	}

	public MediaVideoVO setStoreType(StoreType storeType) {
		this.storeType = storeType;
		return this;
	}

	public String getUploadTmpPath() {
		return uploadTmpPath;
	}

	public MediaVideoVO setUploadTmpPath(String uploadTmpPath) {
		this.uploadTmpPath = uploadTmpPath;
		return this;
	}

	public Long getFolderId() {
		return folderId;
	}

	public MediaVideoVO setFolderId(Long folderId) {
		this.folderId = folderId;
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
	
	public String getThumbnail() {
		return thumbnail;
	}

	public MediaVideoVO setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
		return this;
	}

	public String getType() {
		return type;
	}

	public MediaVideoVO setType(String type) {
		this.type = type;
		return this;
	}
	
	public String getResourceType() {
		return resourceType;
	}

	public MediaVideoVO setResourceType(String resourceType) {
		this.resourceType = resourceType;
		return this;
	}

	public boolean isRemoveable() {
		return removeable;
	}

	public MediaVideoVO setRemoveable(boolean removeable) {
		this.removeable = removeable;
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

	public MediaFileEditorVO getEditorInfo() {
		return editorInfo;
	}

	public MediaVideoVO setEditorInfo(MediaFileEditorVO editorInfo) {
		this.editorInfo = editorInfo;
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

	public Boolean getEncryption() {
		return encryption;
	}

	public MediaVideoVO setEncryption(Boolean encryption) {
		this.encryption = encryption;
		return this;
	}

	public String getEncryptionUrl() {
		return encryptionUrl;
	}

	public MediaVideoVO setEncryptionUrl(String encryptionUrl) {
		this.encryptionUrl = encryptionUrl;
		return this;
	}

	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public MediaVideoVO setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
		return this;
	}

	public String getAddition() {
		return addition;
	}

	public MediaVideoVO setAddition(String addition) {
		this.addition = addition;
		return this;
	}

	public String getFileName() {
		return fileName;
	}

	public MediaVideoVO setFileName(String fileName) {
		this.fileName = fileName;
		return this;
	}

	public List<MediaFileEquipmentPermissionPO> getDeviceUpload() {
		return deviceUpload;
	}

	public MediaVideoVO setDeviceUpload(List<MediaFileEquipmentPermissionPO> deviceUpload) {
		this.deviceUpload = deviceUpload;
		return this;
	}

	@Override
	public MediaVideoVO set(MediaVideoPO entity) throws Exception {
		ServerProps serverProps = SpringContext.getBean(ServerProps.class);
        MimsServerPropsQuery serverPropsQuery = SpringContext.getBean(MimsServerPropsQuery.class);
		MediaFileEditorDAO mediaFileEditorDAO = SpringContext.getBean(MediaFileEditorDAO.class);
		MediaFileEditorPO mediaEditorPO = mediaFileEditorDAO.findByMediaIdAndMediaType(entity.getId(), FolderType.COMPANY_AUDIO);
		this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setName(entity.getName())
			.setAuthorName(entity.getAuthorName())
			.setSize(entity.getSize() != null ? entity.getSize().toString() : "-")
			.setCreateTime(entity.getCreateTime()==null?"":DateUtil.format(entity.getCreateTime(), DateUtil.dateTimePattern))
			.setDuration(entity.getDuration()==null?"-":entity.getDuration().toString())
			.setVersion(entity.getVersion())
			.setFolderId(entity.getFolderId())
			.setThumbnail(entity.getThumbnail())
			.setRemarks(entity.getRemarks())
			.setType(MediaVideoItemType.VIDEO.toString())
			.setRemoveable(true)
			.setIcon(MediaVideoItemType.VIDEO.getIcon())
			.setStyle(MediaVideoItemType.VIDEO.getStyle()[0])
			.setMimetype(entity.getMimetype())
			.setStoreType(entity.getStoreType())
			.setUploadTmpPath(entity.getUploadTmpPath())
			.setProgress(0)
			.setPreviewUrl((entity.getStoreType() == StoreType.REMOTE) ? entity.getPreviewUrl() : new StringBufferWrapper().append("http://").append(serverPropsQuery.queryProps().getFtpIp()).append(":").append(serverProps.getPort()).append("/").append(entity.getPreviewUrl()).toString())
			.setFtpUrl((entity.getStoreType() == StoreType.REMOTE) ? "" : new StringBufferWrapper().append("ftp://").
					append(serverProps.getFtpUsername()).append(":").append(serverProps.getFtpPassword()).append("@").
					append(serverPropsQuery.queryProps().getFtpIp()).append(":").append(serverProps.getFtpPort()).append("/").append(entity.getPreviewUrl()).toString())
			.setEncryption(entity.getEncryption() != null && entity.getEncryption() ? true : false)
			.setEncryptionUrl(entity.getEncryptionUrl())
			.setReviewStatus(entity.getReviewStatus()==null?"":entity.getReviewStatus().getName())
			.setProcessInstanceId(entity.getProcessInstanceId())
			.setAddition(entity.getAddition())
			.setFileName(entity.getFileName())
			.setTags(entity.getTags() != null && !entity.getTags().isEmpty() ? Arrays.asList(entity.getTags().split(MediaVideoPO.SEPARATOR_TAG)) : new ArrayList<String>());
		if(entity.getKeyWords() != null) this.setKeyWords(Arrays.asList(entity.getKeyWords().split(MediaVideoPO.SEPARATOR_KEYWORDS)));
		if(mediaEditorPO != null) this.setEditorInfo(new MediaFileEditorVO().set(mediaEditorPO));
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
			.setDuration("-")
			.setVersion("-")
			.setRemarks("-")
			.setType(MediaVideoItemType.FOLDER.toString())
			.setResourceType(entity.getType().toString())
			.setRemoveable(entity.getDepth().intValue()==2?false:true)
			.setIcon(MediaVideoItemType.FOLDER.getIcon())
			.setStyle(MediaVideoItemType.FOLDER.getStyle()[0])
			.setReviewStatus("-");
		return this;
	}

	public String getFtpUrl() {
		return ftpUrl;
	}

	public MediaVideoVO setFtpUrl(String ftpUrl) {
		this.ftpUrl = ftpUrl;
		return this;
	}
	
}
