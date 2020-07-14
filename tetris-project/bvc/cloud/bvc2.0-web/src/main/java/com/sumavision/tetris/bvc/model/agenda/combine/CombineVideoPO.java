package com.sumavision.tetris.bvc.model.agenda.combine;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * 议程合屏<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年6月4日 下午3:32:07
 */
@Entity
@Table(name = "TETRIS_BVC_MODEL_AGENDA_COMBINE_VIDEO")
public class CombineVideoPO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;

	/** 名称 */
	private String name;
	
	/** 前端生成布局的json字符串格式：{basic:{column:4, row:4}, cellspan:[{x,y,r,b}]} */
	private String websiteDraw;
	
	/** 隶属业务id */
	private Long businessId;
	
	/** 业务类型 */
	private CombineBusinessType businessType;

	@Column(name = "NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "WEBSITEDRAW", length = 1024)
	public String getWebsiteDraw() {
		return websiteDraw;
	}

	public void setWebsiteDraw(String websiteDraw) {
		this.websiteDraw = websiteDraw;
	}

	@Column(name = "BUSINESS_ID")
	public Long getBusinessId() {
		return businessId;
	}

	public void setBusinessId(Long businessId) {
		this.businessId = businessId;
	}

	@Column(name = "BUSINESS_TYPE")
	@Enumerated(value = EnumType.STRING)
	public CombineBusinessType getBusinessType() {
		return businessType;
	}

	public void setBusinessType(CombineBusinessType businessType) {
		this.businessType = businessType;
	}
	
}
