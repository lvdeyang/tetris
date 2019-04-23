package com.sumavision.tetris.cms.article;

import java.util.Date;
import java.util.List;

import com.sumavision.tetris.cms.classify.ClassifyVO;
import com.sumavision.tetris.cms.region.RegionVO;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class ArticleVO extends AbstractBaseVO<ArticleVO, ArticlePO>{

	private String name;
	
	private String thumbnail;
	
	private String author;
	
	private String publishTime;
	
	private String remark;
	
	private String previewUrl;
	
	private Long order;
	
	private String type;
	
	private String articleType;
	
	/** 额外：栏目名称 */
	private String columnName;
	
	/** 额外：栏目id */
	private Long columnId;
	
	/** 额外：历史浏览时间 */
	private Date watchTime;
	
	/** 额外：收藏标识 */
	private Boolean keep;
	
	private List<ClassifyVO> classifies;
	
	private List<RegionVO> regions;
	
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

	public List<ClassifyVO> getClassifies() {
		return classifies;
	}

	public ArticleVO setClassifies(List<ClassifyVO> classifies) {
		this.classifies = classifies;
		return this;
	}

	public List<RegionVO> getRegions() {
		return regions;
	}

	public ArticleVO setRegions(List<RegionVO> regions) {
		this.regions = regions;
		return this;
	}

	public Long getOrder() {
		return order;
	}

	public ArticleVO setOrder(Long order) {
		this.order = order;
		return this;
	}

	public String getType() {
		return type;
	}

	public ArticleVO setType(String type) {
		this.type = type;
		return this;
	}

	public String getArticleType() {
		return articleType;
	}

	public ArticleVO setArticleType(String articleType) {
		this.articleType = articleType;
		return this;
	}

	public String getColumnName() {
		return columnName;
	}

	public ArticleVO setColumnName(String columnName) {
		this.columnName = columnName;
		return this;
	}

	public Long getColumnId() {
		return columnId;
	}

	public ArticleVO setColumnId(Long columnId) {
		this.columnId = columnId;
		return this;
	}

	public Date getWatchTime() {
		return watchTime;
	}

	public ArticleVO setWatchTime(Date watchTime) {
		this.watchTime = watchTime;
		return this;
	}

	public Boolean getKeep() {
		return keep;
	}

	public ArticleVO setKeep(Boolean keep) {
		this.keep = keep;
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
			.setPreviewUrl(entity.getPreviewUrl())
			.setType(entity.getType().toString())
			.setArticleType(entity.getType().getName());
		return this;
	}
	
}
