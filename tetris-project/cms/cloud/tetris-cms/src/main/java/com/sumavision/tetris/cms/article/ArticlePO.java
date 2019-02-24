package com.sumavision.tetris.cms.article;

import javax.persistence.Entity;
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
	
	/** 预览地址 */
	private String previewUrl;

	/** 文本存储路径 */
	private String storePath;
	
	/** 文章备注 */
	private String remark;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPreviewUrl() {
		return previewUrl;
	}

	public void setPreviewUrl(String previewUrl) {
		this.previewUrl = previewUrl;
	}

	public String getStorePath() {
		return storePath;
	}

	public void setStorePath(String storePath) {
		this.storePath = storePath;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
}
