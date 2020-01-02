package com.sumavision.tetris.mims.app.media.picture;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.sumavision.tetris.mims.app.media.ReviewStatus;
import com.sumavision.tetris.mims.app.media.StoreType;
import com.sumavision.tetris.mims.app.media.UploadStatus;
import com.sumavision.tetris.mims.app.media.history.picture.HistoryMediaPicturePO;
import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * 媒资图片<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2018年12月6日 下午2:07:19
 */
@Entity
@Table(name = "MIMS_MEDIA_PICTURE")
public class MediaPicturePO extends AbstractBasePO{

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
	
	/** 审核状态 */
	private ReviewStatus reviewStatus;
	
	/** 审核流程id */
	private String processInstanceId;
	
	/** 附加字段 */
	private String addition;
	
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

	@Enumerated(value = EnumType.STRING)
	@Column(name = "REVIEW_STATUS")
	public ReviewStatus getReviewStatus() {
		return reviewStatus;
	}

	public void setReviewStatus(ReviewStatus reviewStatus) {
		this.reviewStatus = reviewStatus;
	}

	@Column(name = "PROCESS_INSTANCE_ID")
	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	public String getAddition() {
		return addition;
	}

	@Column(name = "ADDITION")
	public void setAddition(String addition) {
		this.addition = addition;
	}

	/**
	 * 转换为历史数据<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月6日 下午3:50:38
	 * @return HistoryMediaPicturePO 历史数据
	 */
	public HistoryMediaPicturePO history(){
		HistoryMediaPicturePO history = new HistoryMediaPicturePO();
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
	 * 复制图片媒资<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月29日 下午3:45:49
	 * @return MediaPicturePO 复制的图片媒资
	 */
	public MediaPicturePO copy(){
		MediaPicturePO copy_picture = new MediaPicturePO();
		copy_picture.setUuid(this.getUuid());
		copy_picture.setUpdateTime(new Date());
		copy_picture.setLastModified(this.getLastModified());
		copy_picture.setName(this.getName());
		copy_picture.setFileName(this.getFileName());
		copy_picture.setStoreType(this.getStoreType());
		copy_picture.setStoreUrl(this.getStoreUrl());
		copy_picture.setPreviewUrl(this.getPreviewUrl());
		copy_picture.setUploadTmpPath(this.getUploadTmpPath());
		copy_picture.setAuthorId(this.getAuthorId());
		copy_picture.setAuthorName(this.getAuthorName());
		copy_picture.setSuffix(this.getSuffix());
		copy_picture.setMimetype(this.getMimetype());
		copy_picture.setSize(this.getSize());
		copy_picture.setCreateTime(this.getCreateTime());
		copy_picture.setVersion(this.getVersion());
		copy_picture.setRemarks(this.getRemarks());
		copy_picture.setTags(this.getTags());
		copy_picture.setKeyWords(this.getKeyWords());
		copy_picture.setUploadStatus(this.getUploadStatus());
		copy_picture.setAddition(this.getAddition());
		return copy_picture;
	}
	
}
