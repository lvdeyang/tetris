package com.sumavision.tetris.mims.app.media.txt;

import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import com.sumavision.tetris.mims.app.media.UploadStatus;
import com.sumavision.tetris.orm.po.AbstractBasePO;


@Entity
@Table(name = "MIMS_MEDIA_TXT")
public class MediaTxtPO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;
	
	/** 原始媒资 */
	public static final String VERSION_OF_ORIGIN = "0.0";
	
	/** 二次生产媒资 */
	public static final String VERSION_OF_SECONDARY = "0.1";
	
	/** 标签分隔符 */
	public static final String SEPARATOR_TAG = ",";
	
	/** 关键字分隔符 */
	public static final String SEPARATOR_KEYWORDS = ",";
	
	/** 媒资别名 */
	private String name;
	
	/** 文本内容 */
	private String content;
	
	/** 上传者id */
	private String authorId;
	
	/** 上传者 */
	private String authorName;
	
	/** 媒资创建时间 */
	private Date createTime;
	
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
	
	/** 文件名称 */
	private String fileName;
	
	/** 最后更新时间 */
	private Long lastModified;
	
	/** 预览地址 */
	private String previewUrl;
	
	/** 上传时临时存储位置（storeType.LOCAL时有效） */
	private String uploadTmpPath;
	
	/** 文件大小 */
	private Long size;

	@Column(name = "NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "CONTENT", columnDefinition = "MEDIUMTEXT")
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
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

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATE_TIME")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
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
	
	@Column(name = "FILE_NAME")
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	@Column(name = "LAST_MODIFIED")
	public Long getLastModified() {
		return lastModified;
	}

	public void setLastModified(Long lastModified) {
		this.lastModified = lastModified;
	}

	@Column(name = "PREVIEW_URL")
	public String getPreviewUrl() {
		return previewUrl;
	}

	public void setPreviewUrl(String previewUrl) {
		this.previewUrl = previewUrl;
	}

	@Column(name = "UPLOAD_TMP_PATH")
	public String getUploadTmpPath() {
		return uploadTmpPath;
	}

	public void setUploadTmpPath(String uploadTmpPath) {
		this.uploadTmpPath = uploadTmpPath;
	}

	@Column(name = "size")
	public Long getSize() {
		return size;
	}

	public void setSize(Long size) {
		this.size = size;
	}

	/**
	 * 复制文本媒资<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月29日 下午3:45:49
	 * @return MediaTxtPO 复制的文本媒资
	 */
	public MediaTxtPO copy(){
		MediaTxtPO copy_txt = new MediaTxtPO();
		copy_txt.setUuid(this.getUuid());
		copy_txt.setUpdateTime(new Date());
		copy_txt.setName(this.getName());
		copy_txt.setContent(this.getContent());
		copy_txt.setAuthorId(this.getAuthorId());
		copy_txt.setAuthorName(this.getAuthorName());
		copy_txt.setCreateTime(this.getCreateTime());
		copy_txt.setRemarks(this.getRemarks());
		copy_txt.setTags(this.getTags());
		copy_txt.setKeyWords(this.getKeyWords());
		copy_txt.setUploadStatus(this.getUploadStatus());
		copy_txt.setSize(this.getSize());
		return copy_txt;
	}
	
}
