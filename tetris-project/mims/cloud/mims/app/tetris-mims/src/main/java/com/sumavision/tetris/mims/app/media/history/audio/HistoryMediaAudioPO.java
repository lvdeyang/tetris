package com.sumavision.tetris.mims.app.media.history.audio;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import com.sumavision.tetris.mims.app.media.StoreType;
import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * 历史媒资音频<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2018年12月6日 下午2:07:36
 */
@Entity
@Table(name = "MIMS_HISTORY_MEDIA_AUDIO")
public class HistoryMediaAudioPO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;

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
	
	/** 文件播放时长 */
	private Long duration;
	
	/** 版本号，格式：类型.timestamp, 0.0（素材） 0.1（媒资）*/
	private String version;
	
	/** 备注 */
	private String remarks;
	
	/** 标签，格式：,分割 */
	private String tags;
	
	/** 文件夹id */
	private Long folderId;
	
	/** 关键字， 格式：,分割*/
	private String keyWords;
	
	/** 媒资图片uuid */
	private String originUuid;
	
	/**是否置顶1是，0否*/
	private int isTop=0;
	
	private String codec;
	private Integer channelNum;
	private Integer sample;
	private Integer bitrate;//bps
	
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

	@Column(name = "DURATION")
	public Long getDuration() {
		return duration;
	}

	public void setDuration(Long duration) {
		this.duration = duration;
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

	@Column(name = "ORIGIN_UUID")
	public String getOriginUuid() {
		return originUuid;
	}

	public void setOriginUuid(String originUuid) {
		this.originUuid = originUuid;
	}
	
	@Column(name = "ISTOP")
	public int getIsTop() {
		return isTop;
	}

	public void setIsTop(int isTop) {
		this.isTop = isTop;
	}
	
	@Column(name = "CODEC")
	public String getCodec() {
		return codec;
	}

	public void setCodec(String codec) {
		this.codec = codec;
	}
	@Column(name = "CHANNELNUM")
	public Integer getChannelNum() {
		return channelNum;
	}

	public void setChannelNum(Integer channelNum) {
		this.channelNum = channelNum;
	}
	@Column(name = "SAMPLE")
	public Integer getSample() {
		return sample;
	}

	public void setSample(Integer sample) {
		this.sample = sample;
	}
	@Column(name = "BITRATE")
	public Integer getBitrate() {
		return bitrate;
	}

	public void setBitrate(Integer bitrate) {
		this.bitrate = bitrate;
	}

	
}
