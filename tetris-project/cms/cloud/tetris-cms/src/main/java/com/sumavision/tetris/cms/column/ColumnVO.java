package com.sumavision.tetris.cms.column;

import java.util.List;

import com.sumavision.tetris.cms.article.ArticleVO;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class ColumnVO extends AbstractBaseVO<ColumnVO, ColumnPO>{

	private String name;
	private String code;
	private Long parentId;
	/** 额外：标记订阅  */
	private Boolean subscribed;
	/** 额外：标记子栏目订阅 */
	private Boolean subColumnSubscribed;
	/** 新增：栏目顺序 */
	private Long columnOrder;
	private List<ColumnVO> subColumns;
	private List<ArticleVO> articles;
	
	public String getName() {
		return name;
	}

	public ColumnVO setName(String name) {
		this.name = name;
		return this;
	}

	public String getCode() {
		return code;
	}

	public ColumnVO setCode(String code) {
		this.code = code;
		return this;
	}

	public Long getParentId() {
		return parentId;
	}

	public ColumnVO setParentId(Long parentId) {
		this.parentId = parentId;
		return this;
	}

	public Long getColumnOrder() {
		return columnOrder;
	}

	public ColumnVO setColumnOrder(Long columnOrder) {
		this.columnOrder = columnOrder;
		return this;
	}

	public List<ColumnVO> getSubColumns() {
		return subColumns;
	}

	public ColumnVO setSubColumns(List<ColumnVO> subColumns) {
		this.subColumns = subColumns;
		return this;
	}

	public List<ArticleVO> getArticles() {
		return articles;
	}

	public ColumnVO setArticles(List<ArticleVO> articles) {
		this.articles = articles;
		return this;
	}
	
	public Boolean getSubscribed() {
		return subscribed;
	}

	public ColumnVO setSubscribed(Boolean subscribed) {
		this.subscribed = subscribed;
		return this;
	}

	public Boolean getSubColumnSubscribed() {
		return subColumnSubscribed;
	}

	public ColumnVO setSubColumnSubscribed(Boolean subColumnSubscribed) {
		this.subColumnSubscribed = subColumnSubscribed;
		return this;
	}

	@Override
	public ColumnVO set(ColumnPO entity) throws Exception {
		this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setName(entity.getName())
			.setCode(entity.getCode())
			.setColumnOrder(entity.getColumnOrder())
			.setParentId(entity.getParentId());
		return this;
	}

}
