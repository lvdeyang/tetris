package com.sumavision.tetris.cms.relation;

import java.util.Comparator;

import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class ColumnRelationArticleVO extends AbstractBaseVO<ColumnRelationArticleVO, ColumnRelationArticlePO>{

	private Long columnId;
	private String columnName;
	private String columnRemark;
	
	private Long articleId;
	private String articleName;
	private String articleRemark;
	private Boolean command;
	
	@Override
	public ColumnRelationArticleVO set(ColumnRelationArticlePO entity) throws Exception {
		this.setId(entity.getId())
			.setColumnId(entity.getColumnId())
			.setColumnName(entity.getColumnName())
			.setColumnRemark(entity.getColumnRemark())
			.setArticleId(entity.getArticleId())
			.setArticleName(entity.getArticleName())
			.setArticleRemark(entity.getArticleRemark())
			.setCommand(entity.getCommand());
		
		return this;
	}

	public Long getColumnId() {
		return columnId;
	}

	public ColumnRelationArticleVO setColumnId(Long columnId) {
		this.columnId = columnId;
		return this;
	}

	public String getColumnName() {
		return columnName;
	}

	public ColumnRelationArticleVO setColumnName(String columnName) {
		this.columnName = columnName;
		return this;
	}

	public String getColumnRemark() {
		return columnRemark;
	}

	public ColumnRelationArticleVO setColumnRemark(String columnRemark) {
		this.columnRemark = columnRemark;
		return this;
	}

	public Long getArticleId() {
		return articleId;
	}

	public ColumnRelationArticleVO setArticleId(Long articleId) {
		this.articleId = articleId;
		return this;
	}

	public String getArticleName() {
		return articleName;
	}

	public ColumnRelationArticleVO setArticleName(String articleName) {
		this.articleName = articleName;
		return this;
	}

	public String getArticleRemark() {
		return articleRemark;
	}

	public ColumnRelationArticleVO setArticleRemark(String articleRemark) {
		this.articleRemark = articleRemark;
		return this;
	}

	public Boolean getCommand() {
		return command;
	}

	public ColumnRelationArticleVO setCommand(Boolean command) {
		this.command = command;
		return this;
	}
	
}
