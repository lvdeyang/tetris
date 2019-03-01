package com.sumavision.tetris.cms.article;

import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class ArticleVO extends AbstractBaseVO<ArticleVO, ArticlePO>{

	private String name;
	
	private String thumbnail;
	
	private String author;
	
	private String publishTime;
	
	private String remark;
	
	private String previewUrl;
	
	public String getName() {
		return name;
	}

	public ArticleVO setName(String name) {
		this.name = name;
		return this;
	}
	
	public String getThumbnail() {
		return thumbnail;
	}

	public ArticleVO setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
		return this;
	}

	public String getAuthor() {
		return author;
	}

	public ArticleVO setAuthor(String author) {
		this.author = author;
		return this;
	}

	public String getPublishTime() {
		return publishTime;
	}

	public ArticleVO setPublishTime(String publishTime) {
		this.publishTime = publishTime;
		return this;
	}

	public String getRemark() {
		return remark;
	}

	public ArticleVO setRemark(String remark) {
		this.remark = remark;
		return this;
	}

	public String getPreviewUrl() {
		return previewUrl;
	}

	public ArticleVO setPreviewUrl(String previewUrl) {
		this.previewUrl = previewUrl;
		return this;
	}

	@Override
	public ArticleVO set(ArticlePO entity) throws Exception {
		this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setName(entity.getName())
			.setThumbnail(entity.getThumbnail())
			.setAuthor(entity.getAuthor())
			.setPublishTime(entity.getPublishTime())
			.setRemark(entity.getRemark())
			.setPreviewUrl(entity.getPreviewUrl());
		return this;
	}
	
}
