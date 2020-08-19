package com.sumavision.tetris.cms.article;

import java.util.Comparator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.sumavision.tetris.cms.relation.ColumnRelationArticlePO;
import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * 文章历史关联表<br/>
 * <b>作者:</b>lzp<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年2月24日 下午3:45:09
 */
@Table
@Entity(name = "TETRIS_CMS_ARTICLE_HISTORY")
public class ArticleHistoryPO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;
	
	/** 文章id */
	private Long articleId;
	
	/** 用户id */
	private Long userId;
	
	/** 栏目id */
	private Long columnId;

	@Column(name="ARTICLE_ID")
	public Long getArticleId() {
		return articleId;
	}

	public void setArticleId(Long articleId) {
		this.articleId = articleId;
	}

	@Column(name="USER_ID")
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	@Column(name="COLUMN_ID")
	public Long getColumnId() {
		return columnId;
	}

	public void setColumnId(Long columnId) {
		this.columnId = columnId;
	}



	/**
	 * @ClassName: 排序器，从大到小排列<br/> 
	 * @author lzp
	 * @date 2019年4月17日下午3:36:10 
	 */
	public static final class ArticleTimeComparator implements Comparator<ArticleHistoryPO>{
		@Override
		public int compare(ArticleHistoryPO o1, ArticleHistoryPO o2) {
			
			if(o1.getUpdateTime().getTime() < o2.getUpdateTime().getTime()){
				return 1;
			}
			if(o1.getUpdateTime().getTime() == o2.getUpdateTime().getTime()){
				return 0;
			}
			return -1;
		}
	}
}
