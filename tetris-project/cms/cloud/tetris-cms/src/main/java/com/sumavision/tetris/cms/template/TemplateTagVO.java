package com.sumavision.tetris.cms.template;

import java.util.List;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class TemplateTagVO extends AbstractBaseVO<TemplateTagVO, TemplateTagPO>{

	private String name;
	
	private String remark;
	
	private List<TemplateTagVO> subTags;
	
	private String icon;
	
	private String style;
	
	public String getName() {
		return name;
	}

	public TemplateTagVO setName(String name) {
		this.name = name;
		return this;
	}

	public String getRemark() {
		return remark;
	}

	public TemplateTagVO setRemark(String remark) {
		this.remark = remark;
		return this;
	}

	public List<TemplateTagVO> getSubTags() {
		return subTags;
	}

	public TemplateTagVO setSubTags(List<TemplateTagVO> subTags) {
		this.subTags = subTags;
		return this;
	}

	public String getIcon() {
		return icon;
	}

	public TemplateTagVO setIcon(String icon) {
		this.icon = icon;
		return this;
	}

	public String getStyle() {
		return style;
	}

	public TemplateTagVO setStyle(String style) {
		this.style = style;
		return this;
	}

	@Override
	public TemplateTagVO set(TemplateTagPO entity) throws Exception {
		this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setName(entity.getName())
			.setRemark(entity.getRemark())
			.setIcon("icon-tag")
			.setStyle("font-size:15px; position:relative; top:1px; margin-right:1px;");
		return this;
	}

}
