package com.sumavision.tetris.cms.article;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

@Entity
@Table(name = "TETRIS_CMS_ARTICLE_MEDIA_PERMISSION")
public class ArticleMediaPermissionPO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;

	/** 文章id */
	private Long articleId;
	
	/** 媒资id */
	private Long mediaId;
	
	/** 媒资类型 */
	private MediaType mediaType;

	@Column(name = "ARTICLE_ID")
	public Long getArticleId() {
		return articleId;
	}

	public void setArticleId(Long articleId) {
		this.articleId = articleId;
	}

	@Column(name = "MEDIA_ID")
	public Long getMediaId() {
		return mediaId;
	}

	public void setMediaId(Long mediaId) {
		this.mediaId = mediaId;
	}

	@Enumerated(value = EnumType.STRING)
	@Column(name = "MEDIA_TYPE")
	public MediaType getMediaType() {
		return mediaType;
	}

	public void setMediaType(MediaType mediaType) {
		this.mediaType = mediaType;
	}
	
}
