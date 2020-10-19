package com.sumavision.tetris.mims.app.media.picture;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.sumavision.tetris.commons.context.SpringContext;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.mims.app.folder.FolderPO;
import com.sumavision.tetris.mims.app.media.StoreType;
import com.sumavision.tetris.mims.app.media.upload.MediaFileEquipmentPermissionPO;
import com.sumavision.tetris.mims.config.server.MimsServerPropsQuery;
import com.sumavision.tetris.mims.config.server.ServerProps;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class MediaPictureVO extends AbstractBaseVO<MediaPictureVO, MediaPicturePO>{

	private String name;
	
	private String authorName;
	
	private String size;
	
	private String createTime;
	
	private String version;
	
	private String remarks;
	
	private StoreType storeType;
	
	private List<String> tags;
	
	private List<String> keyWords;
	
	private String type;
	
	private String resourceType;
	
	private String uploadTmpPath;
	
	private String fileName;
	
	private boolean removeable;
	
	private String icon;
	
	private String style;
	
	private String mimetype;
	
	private Integer progress;
	
	private String previewUrl;
	
	private String reviewStatus;
	
	private String processInstanceId;
	
	private String addition;
	
	private Long folderId;
	
	private List<MediaFileEquipmentPermissionPO> deviceUpload;
	
	private List<MediaPictureVO> children;
	
	private Boolean synchro;
	
	
	public Boolean getSynchro() {
		return synchro;
	}

	public MediaPictureVO setSynchro(Boolean synchro) {
		this.synchro = synchro;
		return this;
	}

	public String getName() {
		return name;
	}

	public MediaPictureVO setName(String name) {
		this.name = name;
		return this;
	}

	public String getAuthorName() {
		return authorName;
	}

	public MediaPictureVO setAuthorName(String authorName) {
		this.authorName = authorName;
		return this;
	}

	public String getSize() {
		return size;
	}

	public MediaPictureVO setSize(String size) {
		this.size = size;
		return this;
	}

	public String getCreateTime() {
		return createTime;
	}

	public MediaPictureVO setCreateTime(String createTime) {
		this.createTime = createTime;
		return this;
	}

	public String getVersion() {
		return version;
	}

	public MediaPictureVO setVersion(String version) {
		this.version = version;
		return this;
	}

	public String getRemarks() {
		return remarks;
	}

	public MediaPictureVO setRemarks(String remarks) {
		this.remarks = remarks;
		return this;
	}

	public StoreType getStoreType() {
		return storeType;
	}

	public MediaPictureVO setStoreType(StoreType storeType) {
		this.storeType = storeType;
		return this;
	}

	public List<String> getTags() {
		return tags;
	}

	public MediaPictureVO setTags(List<String> tags) {
		this.tags = tags;
		return this;
	}

	public List<String> getKeyWords() {
		return keyWords;
	}

	public MediaPictureVO setKeyWords(List<String> keyWords) {
		this.keyWords = keyWords;
		return this;
	}
	
	public String getType() {
		return type;
	}

	public MediaPictureVO setType(String type) {
		this.type = type;
		return this;
	}

	public String getResourceType() {
		return resourceType;
	}

	public MediaPictureVO setResourceType(String resourceType) {
		this.resourceType = resourceType;
		return this;
	}

	public String getUploadTmpPath() {
		return uploadTmpPath;
	}

	public MediaPictureVO setUploadTmpPath(String uploadTmpPath) {
		this.uploadTmpPath = uploadTmpPath;
		return this;
	}

	public String getFileName() {
		return fileName;
	}

	public MediaPictureVO setFileName(String fileName) {
		this.fileName = fileName;
		return this;
	}

	public boolean isRemoveable() {
		return removeable;
	}

	public MediaPictureVO setRemoveable(boolean removeable) {
		this.removeable = removeable;
		return this;
	}

	public String getIcon() {
		return icon;
	}

	public MediaPictureVO setIcon(String icon) {
		this.icon = icon;
		return this;
	}

	public String getStyle() {
		return style;
	}

	public MediaPictureVO setStyle(String style) {
		this.style = style;
		return this;
	}
	
	public String getMimetype() {
		return mimetype;
	}

	public MediaPictureVO setMimetype(String mimetype) {
		this.mimetype = mimetype;
		return this;
	}

	public Integer getProgress() {
		return progress;
	}

	public MediaPictureVO setProgress(Integer progress) {
		this.progress = progress;
		return this;
	}
	
	public String getPreviewUrl() {
		return previewUrl;
	}

	public MediaPictureVO setPreviewUrl(String previewUrl) {
		this.previewUrl = previewUrl;
		return this;
	}

	public List<MediaPictureVO> getChildren() {
		return children;
	}

	public MediaPictureVO setChildren(List<MediaPictureVO> children) {
		this.children = children;
		return this;
	}

	public String getReviewStatus() {
		return reviewStatus;
	}

	public MediaPictureVO setReviewStatus(String reviewStatus) {
		this.reviewStatus = reviewStatus;
		return this;
	}

	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public MediaPictureVO setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
		return this;
	}

	public Long getFolderId() {
		return folderId;
	}

	public MediaPictureVO setFolderId(Long folderId) {
		this.folderId = folderId;
		return this;
	}

	public String getAddition() {
		return addition;
	}

	public MediaPictureVO setAddition(String addition) {
		this.addition = addition;
		return this;
	}

	public List<MediaFileEquipmentPermissionPO> getDeviceUpload() {
		return deviceUpload;
	}

	public MediaPictureVO setDeviceUpload(List<MediaFileEquipmentPermissionPO> deviceUpload) {
		this.deviceUpload = deviceUpload;
		return this;
	}

	@Override
	public MediaPictureVO set(MediaPicturePO entity) throws Exception {
		ServerProps props = SpringContext.getBean(ServerProps.class);
        MimsServerPropsQuery serverPropsQuery = SpringContext.getBean(MimsServerPropsQuery.class);

		this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setName(entity.getName())
			.setAuthorName(entity.getAuthorName())
			.setSize(entity.getSize().toString())
			.setCreateTime(entity.getCreateTime()==null?"":DateUtil.format(entity.getCreateTime(), DateUtil.dateTimePattern))
			.setVersion(entity.getVersion())
			.setRemarks(entity.getRemarks())
			.setType(MediaPictureItemType.PICTURE.toString())
			.setRemoveable(true)
			.setIcon(MediaPictureItemType.PICTURE.getIcon())
			.setStyle(MediaPictureItemType.PICTURE.getStyle()[0])
			.setMimetype(entity.getMimetype())
			.setStoreType(entity.getStoreType())
			.setProgress(0)
			.setPreviewUrl(new StringBufferWrapper().append("http://").append(serverPropsQuery.queryProps().getFtpIp()).append(":").append(props.getPort()).append("/").append(entity.getPreviewUrl()).toString())
			.setReviewStatus(entity.getReviewStatus()==null?"":entity.getReviewStatus().getName())
			.setProcessInstanceId(entity.getProcessInstanceId())
			.setUploadTmpPath(entity.getUploadTmpPath())
			.setFileName(entity.getFileName())
			.setFolderId(entity.getFolderId())
			.setAddition(entity.getAddition())
			.setTags(entity.getTags() != null && !entity.getTags().isEmpty() ? Arrays.asList(entity.getTags().split(MediaPicturePO.SEPARATOR_TAG)) : new ArrayList<String>())
			.setSynchro(entity.getSynchro());
		if(entity.getKeyWords() != null) this.setKeyWords(Arrays.asList(entity.getKeyWords().split(MediaPicturePO.SEPARATOR_KEYWORDS)));	 
		return this;
	}
	
	public MediaPictureVO set(FolderPO entity) throws Exception {
		this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setName(entity.getName())
			.setAuthorName(entity.getAuthorName())
			.setSize("-")
			.setCreateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setVersion("-")
			.setRemarks("-")
			.setType(MediaPictureItemType.FOLDER.toString())
			.setResourceType(entity.getType().toString())
			.setRemoveable(entity.getDepth().intValue()==2?false:true)
			.setIcon(MediaPictureItemType.FOLDER.getIcon())
			.setStyle(MediaPictureItemType.FOLDER.getStyle()[0])
			.setReviewStatus("-");
		return this;
	}
	
}
