package com.sumavision.tetris.bvc.model.layout.forward;

import javax.jdo.annotations.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

@Entity
@Table(name = "TETRIS_BVC_MODEL_COMBINE_TEMPLATE_POSITION")
public class CombineTemplatePositionPO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;
	
	/** 画面布局id */
	private Long layoutPositionId;
	
	/** 画面布局的序号，直接通过序号索引，可以不用layoutPositionId */
	private Integer layoutPositionSerialNum;
	
	/** 左偏移 */
	private String x;
	
	/** 上偏移 */
	private String y;
	
	/** 宽度 */
	private String width;
	
	/** 高度 */
	private String height;
	
	/** 涂层 */
	private String zIndex;
	
	/** 隶属合屏模板id */
	private Long combineTemplateId;

	@Column(name = "LAYOUT_POSITION_ID")
	public Long getLayoutPositionId() {
		return layoutPositionId;
	}

	public void setLayoutPositionId(Long layoutPositionId) {
		this.layoutPositionId = layoutPositionId;
	}

	@Column(name = "LAYOUT_POSITION_SERIAL_NUM")
	public Integer getLayoutPositionSerialNum() {
		return layoutPositionSerialNum;
	}

	public void setLayoutPositionSerialNum(Integer layoutPositionSerialNum) {
		this.layoutPositionSerialNum = layoutPositionSerialNum;
	}

	@Column(name = "X")
	public String getX() {
		return x;
	}

	public void setX(String x) {
		this.x = x;
	}

	@Column(name = "Y")
	public String getY() {
		return y;
	}

	public void setY(String y) {
		this.y = y;
	}

	@Column(name = "WIDTH")
	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	@Column(name = "HEIGHT")
	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	@Column(name = "ZINDEX")
	public String getzIndex() {
		return zIndex;
	}

	public void setzIndex(String zIndex) {
		this.zIndex = zIndex;
	}

	@Column(name = "COMBINE_TEMPLATE_ID")
	public Long getCombineTemplateId() {
		return combineTemplateId;
	}

	public void setCombineTemplateId(Long combineTemplateId) {
		this.combineTemplateId = combineTemplateId;
	}
	
}
