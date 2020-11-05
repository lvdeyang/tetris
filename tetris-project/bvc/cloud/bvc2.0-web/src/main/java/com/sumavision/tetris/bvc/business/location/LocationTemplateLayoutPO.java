package com.sumavision.tetris.bvc.business.location;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

@Entity
@Table(name="TETRIS_BVC_LOCATION_TEMPLATE_LAYOUT")
public class LocationTemplateLayoutPO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;

	/** 所属用户*/
	private Long userId;
	
	/** 模板名字*/
	private String templateName;
	
	/** 模板的x方向屏幕数量*/
	private Integer screenNumberOfX;
	
	/** 模板的y方向屏幕数量*/
	private Integer screenNumberOfY;

	@Column(name="USER_ID")
	public Long getUserId() {
		return userId;
	}

	public LocationTemplateLayoutPO setUserId(Long userId) {
		this.userId = userId;
		return this;
	}

	@Column(name="TEMPLATE_NAME")
	public String getTemplateName() {
		return templateName;
	}

	public LocationTemplateLayoutPO setTemplateName(String templateName) {
		this.templateName = templateName;
		return this;
	}

	@Column(name="SCREEN_NUMBER_OF_X")
	public Integer getScreenNumberOfX() {
		return screenNumberOfX;
	}

	public LocationTemplateLayoutPO setScreenNumberOfX(Integer screenNumberOfX) {
		this.screenNumberOfX = screenNumberOfX;
		return this;
	}

	@Column(name="SCREEN_NUMBER_Y")
	public Integer getScreenNumberOfY() {
		return screenNumberOfY;
	}

	public LocationTemplateLayoutPO setScreenNumberOfY(Integer screenNumberOfY) {
		this.screenNumberOfY = screenNumberOfY;
		return this;
	}
	
}
