package com.sumavision.tetris.cms.template;

import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class TemplateVO extends AbstractBaseVO<TemplateVO, TemplatePO>{

	private String name;
	
	private String remark;
	
	private String type;
	
	private String templateId;
	
	private String icon;
	
	private String style;
	
	private String html;
	
	private String js;
	
	public String getName() {
		return name;
	}

	public TemplateVO setName(String name) {
		this.name = name;
		return this;
	}

	public String getRemark() {
		return remark;
	}

	public TemplateVO setRemark(String remark) {
		this.remark = remark;
		return this;
	}

	public String getType() {
		return type;
	}

	public TemplateVO setType(String type) {
		this.type = type;
		return this;
	}
	
	public String getTemplateId() {
		return templateId;
	}

	public TemplateVO setTemplateId(String templateId) {
		this.templateId = templateId;
		return this;
	}

	public String getIcon() {
		return icon;
	}

	public TemplateVO setIcon(String icon) {
		this.icon = icon;
		return this;
	}

	public String getStyle() {
		return style;
	}

	public TemplateVO setStyle(String style) {
		this.style = style;
		return this;
	}

	public String getHtml() {
		return html;
	}

	public TemplateVO setHtml(String html) {
		this.html = html;
		return this;
	}

	public String getJs() {
		return js;
	}

	public TemplateVO setJs(String js) {
		this.js = js;
		return this;
	}

	@Override
	public TemplateVO set(TemplatePO entity) throws Exception {
		this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setName(entity.getName())
			.setRemark(entity.getRemark())
			.setType(entity.getType().getName())
			.setTemplateId(entity.getTemplateId()==null?"":entity.getTemplateId().getName())
			.setIcon(entity.getIcon())
			.setStyle(entity.getStyle());
		return this;
	}

	public TemplateVO setWithHtmlAndJs(TemplatePO entity) throws Exception {
		this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setName(entity.getName())
			.setRemark(entity.getRemark())
			.setType(entity.getType().getName())
			.setIcon(entity.getIcon())
			.setStyle(entity.getStyle())
			.setHtml(entity.getHtml())
			.setJs(entity.getJs());
		return this;
	}

}
