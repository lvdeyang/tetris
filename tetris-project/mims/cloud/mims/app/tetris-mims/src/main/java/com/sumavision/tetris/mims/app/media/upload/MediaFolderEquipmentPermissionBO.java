package com.sumavision.tetris.mims.app.media.upload;

public class MediaFolderEquipmentPermissionBO {

	private Long folderId;
	
	private String storeUrl;
	
	private String folderName;

	public Long getFolderId() {
		return folderId;
	}

	public MediaFolderEquipmentPermissionBO setFolderId(Long folderId) {
		this.folderId = folderId;
		return this;
	}

	public String getStoreUrl() {
		return storeUrl;
	}

	public MediaFolderEquipmentPermissionBO setStoreUrl(String storeUrl) {
		this.storeUrl = storeUrl;
		return this;
	}

	public String getFolderName() {
		return folderName;
	}

	public MediaFolderEquipmentPermissionBO setFolderName(String folderName) {
		this.folderName = folderName;
		return this;
	}
	
}
