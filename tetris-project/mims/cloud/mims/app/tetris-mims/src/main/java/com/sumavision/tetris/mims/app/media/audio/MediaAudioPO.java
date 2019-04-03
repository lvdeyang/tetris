package com.sumavision.tetris.mims.app.media.audio;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import com.sumavision.tetris.mims.app.media.StoreType;
import com.sumavision.tetris.mims.app.media.UploadStatus;
import com.sumavision.tetris.mims.app.media.history.audio.HistoryMediaAudioPO;
import com.sumavision.tetris.orm.po.AbstractBasePO;

@Entity
@Table(name = "MIMS_MEDIA_AUDIO")
public class MediaAudioPO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;
	
	/** 原始媒资 */
	public static final String VERSION_OF_ORIGIN = "0.0";
	
	/** 二次生产媒资 */
	public static final String VERSION_OF_SECONDARY = "0.1";
	
	/** 标签分隔符 */
	public static final String SEPARATOR_TAG = ",";
	
	/** 关键字分隔符 */
	public static final String SEPARATOR_KEYWORDS = ",";
	
	/** 最后更新时间 */
	private Long lastModified;
	
	/** 媒资别名 */
	private String name;
	
	/** 文件名称 */
	private String fileName;
	
	/** 存储方式 */
	private StoreType storeType;
	
	/** 存储路径（storeType为REMOTE时记录远程路径） */
	private String storeUrl;
	
	/** 预览地址 */
	private String previewUrl;
	
	/** 上传时临时存储位置（storeType.LOCAL时有效） */
	private String uploadTmpPath;
	
	/** 上传者id */
	private String authorId;
	
	/** 上传者 */
	private String authorName;
	
	/** 文件后缀 */
	private String suffix;
	
	/** http mimetype类型 */
	private String mimetype;
	
	/** 文件大小 */
	private Long size;
	
	/** 媒资创建时间 */
	private Date createTime;
	
	/** 版本号，格式：类型.timestamp, 0.0（原始媒资） 0.1（二次生产媒资）*/
	private String version;
	
	/** 备注 */
	private String remarks;
	
	/** 标签，格式：,分割 */
	private String tags;
	
	/** 文件夹id */
	private Long folderId;
	
	/** 关键字， 格式：,分割*/
	private String keyWords;
	
	/** 文件上传状态 */
	private UploadStatus uploadStatus;

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

	@Column(name = "FILE_NAME")
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	@Column(name = "STORE_TYPE")
	public StoreType getStoreType() {
		return storeType;
	}

	public void setStoreType(StoreType storeType) {
		this.storeType = storeType;
	}

	@Column(name = "STORE_URL")
	public String getStoreUrl() {
		return storeUrl;
	}

	public void setStoreUrl(String storeUrl) {
		this.storeUrl = storeUrl;
	}

	@Column(name = "PREVIEW_URL", length = 1024)
	public String getPreviewUrl() {
		return previewUrl;
	}

	public void setPreviewUrl(String previewUrl) {
		this.previewUrl = previewUrl;
	}

	@Column(name = "UPLOAD_TMP_PATH", length = 1024)
	public String getUploadTmpPath() {
		return uploadTmpPath;
	}

	public void setUploadTmpPath(String uploadTmpPath) {
		this.uploadTmpPath = uploadTmpPath;
	}

	@Column(name = "AUTHORID")
	public String getAuthorId() {
		return authorId;
	}

	public void setAuthorId(String authorId) {
		this.authorId = authorId;
	}

	@Column(name = "AUTHOR_NAME")
	public String getAuthorName() {
		return authorName;
	}

	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}

	@Column(name = "SUFFIX")
	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	@Column(name = "MIMETYPE")
	public String getMimetype() {
		return mimetype;
	}

	public void setMimetype(String mimetype) {
		this.mimetype = mimetype;
	}

	@Column(name = "SIZE")
	public Long getSize() {
		return size;
	}

	public void setSize(Long size) {
		this.size = size;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATE_TIME")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Column(name = "VERSION")
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	@Column(name = "REMARKS")
	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	@Column(name = "TAGS")
	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	@Column(name = "FOLDER_ID")
	public Long getFolderId() {
		return folderId;
	}

	public void setFolderId(Long folderId) {
		this.folderId = folderId;
	}

	@Column(name = "KEY_WORDS")
	public String getKeyWords() {
		return keyWords;
	}

	public void setKeyWords(String keyWords) {
		this.keyWords = keyWords;
	}
	
	@Enumerated(value = EnumType.STRING)
	@Column(name = "UPLOAD_STATUS")
	public UploadStatus getUploadStatus() {
		return uploadStatus;
	}

	public void setUploadStatus(UploadStatus uploadStatus) {
		this.uploadStatus = uploadStatus;
	}
	
	/**
	 * 转换为历史数据<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月6日 下午3:50:38
	 * @return HistoryMediaVideoPO 历史数据
	 */
	public HistoryMediaAudioPO history(){
		HistoryMediaAudioPO history = new HistoryMediaAudioPO();
		history.setUpdateTime(new Date());
		history.setName(this.getName());
		history.setFileName(this.getFileName());
		history.setStoreType(this.getStoreType());
		history.setStoreUrl(this.getStoreUrl());
		history.setPreviewUrl(this.getPreviewUrl());
		history.setUploadTmpPath(this.getUploadTmpPath());
		history.setAuthorId(this.getAuthorId());
		history.setAuthorName(this.getAuthorName());
		history.setSuffix(this.getSuffix());
		history.setMimetype(this.getMimetype());
		history.setSize(this.getSize());
		history.setCreateTime(this.getCreateTime());
		history.setVersion(this.getVersion());
		history.setRemarks(this.getRemarks());
		history.setTags(this.getTags());
		history.setFolderId(this.getFolderId());
		history.setKeyWords(this.getKeyWords());
		history.setOriginUuid(this.getUuid());
		return history;
	}
	
	/**
	 * 复制音频媒资<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月29日 下午3:45:49
	 * @return MediaVideoPO 复制的音频媒资
	 */
	public MediaAudioPO copy(){
		MediaAudioPO copy_video = new MediaAudioPO();
		copy_video.setUuid(this.getUuid());
		copy_video.setUpdateTime(new Date());
		copy_video.setLastModified(this.getLastModified());
		copy_video.setName(this.getName());
		copy_video.setFileName(this.getFileName());
		copy_video.setStoreType(this.getStoreType());
		copy_video.setStoreUrl(this.getStoreUrl());
		copy_video.setPreviewUrl(this.getPreviewUrl());
		copy_video.setUploadTmpPath(this.getUploadTmpPath());
		copy_video.setAuthorId(this.getAuthorId());
		copy_video.setAuthorName(this.getAuthorName());
		copy_video.setSuffix(this.getSuffix());
		copy_video.setMimetype(this.getMimetype());
		copy_video.setSize(this.getSize());
		copy_video.setCreateTime(this.getCreateTime());
		copy_video.setVersion(this.getVersion());
		copy_video.setRemarks(this.getRemarks());
		copy_video.setTags(this.getTags());
		copy_video.setKeyWords(this.getKeyWords());
		copy_video.setUploadStatus(this.getUploadStatus());
		return copy_video;
	}
	
}
