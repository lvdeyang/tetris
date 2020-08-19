package com.sumavision.tetris.cms.relation;

import java.util.Comparator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.sumavision.tetris.cms.article.ArticleVO;
import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * 栏目关联文章元数据<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年2月26日 下午3:45:09
 */
@Table
@Entity(name = "TETRIS_CMS_COLUMN_RELATION_ARTICLE")
public class ColumnRelationArticlePO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;
	
	/** 文章id */
	private Long articleId;
	
	/** 文章名称 */
	private String articleName;
	
	/** 文章备注 */
	private String articleRemark;
	
	/** 文章顺序--由小到大排序  */
	private Long articleOrder;
	
	/** 文章是否推荐 */
	private Boolean command;
	
	/** 栏目id */
	private Long columnId;
	
	/** 栏目名称 */
	private String columnName;
	
	/** 栏目编号 */
	private String columnCode;
	
	/** 栏目备注 */
	private String columnRemark;

	@Column(name = "ARTICLE_ID")
	public Long getArticleId() {
		return articleId;
	}

	public void setArticleId(Long articleId) {
		this.articleId = articleId;
	}

	@Column(name = "COLUMN_ID")
	public Long getColumnId() {
		return columnId;
	}

	public void setColumnId(Long columnId) {
		this.columnId = columnId;
	}

	@Column(name = "ARTICLE_NAME")
	public String getArticleName() {
		return articleName;
	}

	public void setArticleName(String articleName) {
		this.articleName = articleName;
	}

	@Column(name = "ARTICLE_REMARK")
	public String getArticleRemark() {
		return articleRemark;
	}

	public void setArticleRemark(String articleRemark) {
		this.articleRemark = articleRemark;
	}

	@Column(name = "COLUMN_NAME")
	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}	

	@Column(name = "COLUMN_CODE")
	public String getColumnCode() {
		return columnCode;
	}

	public void setColumnCode(String columnCode) {
		this.columnCode = columnCode;
	}

	@Column(name = "COLUMN_REMARK")
	public String getColumnRemark() {
		return columnRemark;
	}

	public void setColumnRemark(String columnRemark) {
		this.columnRemark = columnRemark;
	}

	@Column(name = "ARTICLE_ORDER")
	public Long getArticleOrder() {
		return articleOrder;
	}

	public void setArticleOrder(Long articleOrder) {
		this.articleOrder = articleOrder;
	}

	@Column(name = "COMMAND")
	public Boolean getCommand() {
		return command;
	}

	public void setCommand(Boolean command) {
		this.command = command;
	}

	/**
	 * @ClassName: 排序器，从小到大排列<br/> 
	 * @author lvdeyang
	 * @date 2019年2月27日 上午8:36:10 
	 */
	public static final class ArticleOrderComparator implements Comparator<ColumnRelationArticlePO>{
		@Override
		public int compare(ColumnRelationArticlePO o1, ColumnRelationArticlePO o2) {
			
			if(o1.getArticleOrder() > o2.getArticleOrder()){
				return 1;
			}
			if(o1.getArticleOrder() == o2.getArticleOrder()){
				return 0;
			}
			return -1;
		}
	}

	public static final class ArticleVoOrderComparator implements Comparator<ArticleVO>{
		@Override
		public int compare(ArticleVO o1, ArticleVO o2) {
			
			if(o1.getOrder() > o2.getOrder()){
				return 1;
			}
			if(o1.getOrder() == o2.getOrder()){
				return 0;
			}
			return -1;
		}
	}
}
