package com.sumavision.tetris.cms.article;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.Table;
import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * 文章元数据<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年2月24日 下午3:45:09
 */
@Table
@Entity(name = "TETRIS_CMS_ARTICLE")
public class ArticlePO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;

	/** 文章名称 */
	private String name;
	
	/** 缩略图 */
	private String thumbnail;
	
	/** 作者 */
	private String author;
	
	/** 发表时间 */
	private String publishTime;
	
	/** 预览地址 */
	private String previewUrl;

	/** 文本存储路径 */
	private String storePath;
	
	/** 文章排版的模块json */
	private String modules;
	
	/** 文章备注 */
	private String remark;
	
	@Column(name = "NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Column(name = "THUMBNAIL")
	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	@Column(name = "AUTHOR")
	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	@Column(name = "PUBLISH_TIME")
	public String getPublishTime() {
		return publishTime;
	}

	public void setPublishTime(String publishTime) {
		this.publishTime = publishTime;
	}

	@Column(name = "PREVIEW_URL")
	public String getPreviewUrl() {
		return previewUrl;
	}

	public void setPreviewUrl(String previewUrl) {
		this.previewUrl = previewUrl;
	}

	@Column(name = "STORE_PATH")
	public String getStorePath() {
		return storePath;
	}

	public void setStorePath(String storePath) {
		this.storePath = storePath;
	}

	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "MODULES", columnDefinition = "MEDIUMTEXT", nullable = true)
	public String getModules() {
		return modules;
	}

	public void setModules(String modules) {
		this.modules = modules;
	}

	@Column(name = "REMARK")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
}
