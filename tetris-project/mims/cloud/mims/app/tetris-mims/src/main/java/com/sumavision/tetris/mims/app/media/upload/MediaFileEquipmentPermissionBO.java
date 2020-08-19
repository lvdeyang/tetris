package com.sumavision.tetris.mims.app.media.upload;

import com.sumavision.tetris.mims.app.media.picture.MediaPicturePO;
import com.sumavision.tetris.mims.app.media.picture.MediaPictureVO;
import com.sumavision.tetris.mims.app.media.video.MediaVideoPO;
import com.sumavision.tetris.mims.app.media.video.MediaVideoVO;

public class MediaFileEquipmentPermissionBO {
	private Long mediaId;
	
	private String mediaType;
	
	private String storeUrl;
	
	private String fileName;
	
	private Long folderId;

	public Long getMediaId() {
		return mediaId;
	}

	public MediaFileEquipmentPermissionBO setMediaId(Long mediaId) {
		this.mediaId = mediaId;
		return this;
	}

	public String getMediaType() {
		return mediaType;
	}

	public MediaFileEquipmentPermissionBO setMediaType(String mediaType) {
		this.mediaType = mediaType;
		return this;
	}

	public String getStoreUrl() {
		return storeUrl;
	}

	public MediaFileEquipmentPermissionBO setStoreUrl(String storeUrl) {
		this.storeUrl = storeUrl;
		return this;
	}

	public String getFileName() {
		return fileName;
	}

	public MediaFileEquipmentPermissionBO setFileName(String fileName) {
		this.fileName = fileName;
		return this;
	}
	
	public Long getFolderId() {
		return folderId;
	}

	public MediaFileEquipmentPermissionBO setFolderId(Long folderId) {
		this.folderId = folderId;
		return this;
	}

	public MediaFileEquipmentPermissionBO setFromVideoPO(MediaVideoPO media) throws Exception {
		return this.setMediaId(media.getId())
				.setMediaType("video")
				.setStoreUrl(media.getUploadTmpPath())
				.setFileName(media.getFileName())
				.setFolderId(media.getFolderId());
	}
	
	public MediaFileEquipmentPermissionBO setFromVideoVO(MediaVideoVO media) throws Exception {
		return this.setMediaId(media.getId())
				.setMediaType("video")
				.setStoreUrl(media.getUploadTmpPath())
				.setFileName(media.getFileName())
				.setFolderId(media.getFolderId());
	}
	
	public MediaFileEquipmentPermissionBO setFromPicturePO(MediaPicturePO media) throws Exception {
		return this.setMediaId(media.getId())
				.setMediaType("picture")
				.setStoreUrl(media.getUploadTmpPath())
				.setFileName(media.getFileName())
				.setFolderId(media.getFolderId());
	}
	
	public MediaFileEquipmentPermissionBO setFromPictureVO(MediaPictureVO media) throws Exception {
		return this.setMediaId(media.getId())
				.setMediaType("picture")
				.setStoreUrl(media.getUploadTmpPath())
				.setFileName(media.getFileName())
				.setFolderId(media.getFolderId());
	}
}
