package com.sumavision.tetris.cms.article;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * 文章地区关联表<br/>
 * <b>作者:</b>ldy<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年3月4日 下午2:19:47
 */
@Entity
@Table(name = "TETRIS_CMS_ARTICLE_REGION_PERMISSION")
public class ArticleRegionPermissionPO extends AbstractBasePO {

	private static final long serialVersionUID = 1L;
	
	/** 文章id */
	private Long articleId;
	
	/** 文章名称 */
	private String articleName;
	
	/** 地区id */
	private Long regionId;
	
	/** 地区名称 */
	private String regionName;

	@Column(name = "ARTICLE_ID")
	public Long getArticleId() {
		return articleId;
	}

	public void setArticleId(Long articleId) {
		this.articleId = articleId;
	}

	@Column(name = "ARTICLE_NAME")
	public String getArticleName() {
		return articleName;
	}

	public void setArticleName(String articleName) {
		this.articleName = articleName;
	}

	@Column(name = "REGION_ID")
	public Long getRegionId() {
		return regionId;
	}

	public void setRegionId(Long regionId) {
		this.regionId = regionId;
	}

	@Column(name = "REGION_NAME")
	public String getRegionName() {
		return regionName;
	}

	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}

}
