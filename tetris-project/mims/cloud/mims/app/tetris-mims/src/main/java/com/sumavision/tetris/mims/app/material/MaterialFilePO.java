package com.sumavision.tetris.mims.app.material;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Lob;
import javax.persistence.Table;
import com.sumavision.tetris.mims.app.folder.FolderPO;
import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * 素材文件类型<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2018年11月22日 上午9:54:57
 */
@Entity
@Table(name = "MIMS_MATERIAL_FILE")
public class MaterialFilePO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;

	/** 最后更新时间 */
	private Long lastModified;
	
	/** 文件名称 */
	private String name;
	
	/** 文件大小 */
	private Long size;
	
	/** 媒体类型 */
	private String mimetype;
	
	/** 素材类型 */
	private MaterialType type;
	
	/** 临时存储路径 */
	private String tmpPath;
	
	/** 存储路径 */
	private String storePath;
	
	/** 预览链接 */
	private String previewUrl;
	
	/** 素材隶属文件夹 */
	private Long folderId;
	
	/** 上传状态 */
	private MaterialFileUploadStatus uploadStatus;

	@Column(name = "LAST_MODIFIED")
	public Long getLastModified() {
		return lastModified;
	}

	public void setLastModified(Long lastModified) {
		this.lastModified = lastModified;
	}

	@Column(name = "NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "SIZE")
	public Long getSize() {
		return size;
	}

	public void setSize(Long size) {
		this.size = size;
	}
	
	@Column(name = "MIME_TYPE")
	public String getMimetype() {
		return mimetype;
	}

	public void setMimetype(String mimetype) {
		this.mimetype = mimetype;
	}

	@Column(name = "TYPE")
	public MaterialType getType() {
		return type;
	}

	public void setType(MaterialType type) {
		this.type = type;
	}
	
	@Lob
	@Column(columnDefinition = "TEXT",nullable = true, name = "TMP_PATH")
	public String getTmpPath() {
		return tmpPath;
	}

	public void setTmpPath(String tmpPath) {
		this.tmpPath = tmpPath;
	}

	@Column(name = "STORE_PATH")
	public String getStorePath() {
		return storePath;
	}

	public void setStorePath(String storePath) {
		this.storePath = storePath;
	}

	@Column(name = "PREVIEW_URL")
	public String getPreviewUrl() {
		return previewUrl;
	}

	public void setPreviewUrl(String previewUrl) {
		this.previewUrl = previewUrl;
	}

	@Column(name = "FOLDER_ID")
	public Long getFolderId() {
		return folderId;
	}

	public void setFolderId(Long folderId) {
		this.folderId = folderId;
	}
	
	@Enumerated(value = EnumType.STRING)
	@Column(name = "UPLOAD_STATUS")
	public MaterialFileUploadStatus getUploadStatus() {
		return uploadStatus;
	}

	public void setUploadStatus(MaterialFileUploadStatus uploadStatus) {
		this.uploadStatus = uploadStatus;
	}

	/**
	 * 复制完整素材信息（含文件夹信息）<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年11月26日 上午10:25:02
	 * @param Object folder 隶属目录
	 * 			1.folder为null 取this.folderId
	 * 			2.folder为Long
	 * 			3.folder为FolderPO
	 * @return MaterialFilePO 复制的素材
	 */
	public MaterialFilePO copy(Object folder){
		MaterialFilePO copy_material = new MaterialFilePO();
		copy_material.setUuid(this.getUuid());
		copy_material.setUpdateTime(new Date());
		copy_material.setLastModified(this.getLastModified());
		copy_material.setName(this.getName());
		copy_material.setSize(this.getSize());
		copy_material.setPreviewUrl(this.getPreviewUrl());
		copy_material.setTmpPath(this.getTmpPath());
		copy_material.setStorePath(this.getStorePath());
		copy_material.setType(this.getType());
		copy_material.setFolderId(folderId==null?this.getFolderId():((folder instanceof Long)?(Long)folder:((FolderPO)folder).getId()));
		return copy_material;
	}
	
	/**
	 * 复制素材基本信息（不含文件夹信息）<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年11月26日 上午11:02:58
	 * @return MaterialFilePO 素材基本信息
	 */
	public MaterialFilePO copy(){
		MaterialFilePO copy_material = new MaterialFilePO();
		copy_material.setUuid(this.getUuid());
		copy_material.setUpdateTime(new Date());
		copy_material.setLastModified(this.getLastModified());
		copy_material.setName(this.getName());
		copy_material.setSize(this.getSize());
		copy_material.setPreviewUrl(this.getPreviewUrl());
		copy_material.setTmpPath(this.getTmpPath());
		copy_material.setStorePath(this.getStorePath());
		copy_material.setType(this.getType());
		copy_material.setMimetype(this.getMimetype());
		copy_material.setUploadStatus(this.getUploadStatus());
		return copy_material;
	}
	
}
