package com.sumavision.tetris.cms.article;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * 文章分类关联表<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年2月28日 下午5:04:19
 */
@Entity
@Table(name = "TETRIS_CMS_ARTICLE_CLASSIFY_PERMISSION")
public class ArticleClassifyPermissionPO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;
	
	/** 文章id */
	private Long articleId;
	
	/** 分类id */
	private Long classifyId;

	@Column(name = "ARTICLE_ID")
	public Long getArticleId() {
		return articleId;
	}

	public void setArticleId(Long articleId) {
		this.articleId = articleId;
	}

	@Column(name = "CLASSIFY_ID")
	public Long getClassifyId() {
		return classifyId;
	}

	public void setClassifyId(Long classifyId) {
		this.classifyId = classifyId;
	}
	
}
