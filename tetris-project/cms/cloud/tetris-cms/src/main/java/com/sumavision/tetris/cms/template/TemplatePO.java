package com.sumavision.tetris.cms.template;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Lob;

import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * 内容模板<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年2月14日 下午2:18:25
 */
@Entity
@Table(name = "TETRIS_CMS_TEMPLATE")
public class TemplatePO extends AbstractBasePO{

	/** 这是一个常量的说明 */
	private static final long serialVersionUID = 1L;
	
	/** 内容模板名称 */
	private String name;
	
	/** 作者id */
	private String authorId;
	
	/** 作者名称 */
	private String authorName;
	
	/** 备注 */
	private String remark;
	
	/** 模板分类标签 */
	private Long templateTagId;
	
	/** 模板用途 */
	private TemplateType type;
	
	/** 模板html内容 */
	private String html;
	
	/** 模板样式 */
	private String css;
	
	/** 模板脚本 */
	private String js;

	@Column(name = "NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "AUTHOR_ID")
	public String getAuthorId() {
		return authorId;
	}

	public void setAuthorId(String authorId) {
		this.authorId = authorId;
	}

	@Column(name = "AUTHOR_NAME")
	public String getAuthorName() {
		return authorName;
	}

	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}

	@Column(name = "REMARK")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(name = "TEMPLATE_TAG_ID")
	public Long getTemplateTagId() {
		return templateTagId;
	}

	public void setTemplateTagId(Long templateTagId) {
		this.templateTagId = templateTagId;
	}
	
	@Enumerated(value = EnumType.STRING)
	@Column(name = "TYPE")
	public TemplateType getType() {
		return type;
	}

	public void setType(TemplateType type) {
		this.type = type;
	}

	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "HTML", columnDefinition = "MEDIUMTEXT", nullable = true)
	public String getHtml() {
		return html;
	}

	public void setHtml(String html) {
		this.html = html;
	}

	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "CSS", columnDefinition = "MEDIUMTEXT", nullable = true)
	public String getCss() {
		return css;
	}

	public void setCss(String css) {
		this.css = css;
	}

	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "JS", columnDefinition = "MEDIUMTEXT", nullable = true)
	public String getJs() {
		return js;
	}

	public void setJs(String js) {
		this.js = js;
	}

}
