package com.sumavision.tetris.bvc.business.location;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.sumavision.tetris.mvc.converter.AbstractBaseVO;
import com.sumavision.tetris.orm.po.AbstractBasePO;

@Entity
@Table(name="TETRIS_BVC_LOCATION_TEMPLATE_LAYOUT")
public class LocationTemplateLayoutVO extends AbstractBaseVO<LocationTemplateLayoutVO, LocationTemplateLayoutPO>{

	private static final long serialVersionUID = 1L;

	/** 所属用户*/
	private Long userId;
	
	/** 模板名字*/
	private String templateName;
	
	/** 模板的x方向屏幕数量*/
	private Integer screenNumberOfX;
	
	/** 模板的y方向屏幕数量*/
	private Integer screenNumberOfY;

	public Long getUserId() {
		return userId;
	}

	public LocationTemplateLayoutVO setUserId(Long userId) {
		this.userId = userId;
		return this;
	}

	public String getTemplateName() {
		return templateName;
	}

	public LocationTemplateLayoutVO setTemplateName(String templateName) {
		this.templateName = templateName;
		return this;
	}

	public Integer getScreenNumberOfX() {
		return screenNumberOfX;
	}

	public LocationTemplateLayoutVO setScreenNumberOfX(Integer screenNumberOfX) {
		this.screenNumberOfX = screenNumberOfX;
		return this;
	}

	public Integer getScreenNumberOfY() {
		return screenNumberOfY;
	}

	public LocationTemplateLayoutVO setScreenNumberOfY(Integer screenNumberOfY) {
		this.screenNumberOfY = screenNumberOfY;
		return this;
	}

	@Override
	public LocationTemplateLayoutVO set(LocationTemplateLayoutPO entity) throws Exception {
		this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setTemplateName(entity.getTemplateName()==null?"":entity.getTemplateName())
			.setScreenNumberOfX(entity.getScreenNumberOfX())
			.setScreenNumberOfY(entity.getScreenNumberOfY());
		
		return this;
	}


}
