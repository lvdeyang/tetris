package com.sumavision.tetris.bvc.model.agenda.combine;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * 议程合屏源<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年6月4日 下午3:32:39
 */
@Entity
@Table(name = "TETRIS_BVC_MODEL_AGENDA_COMBINE_VIDEO_SRC")
public class CombineVideoSrcPO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;

	/** 源id */
	private String srcId;
	
	/** 源类型 */
	private CombineVideoSrcType combineVideoSrcType;
	
	/** 隶属分屏id */
	private Long combineVideoPositionId;
	
	/** 轮询时进行源排序 */
	private Integer serial;
	
	/** 轮询时是否展示该源 */
	private Boolean visible;

	@Column(name = "SRC_ID")
	public String getSrcId() {
		return srcId;
	}

	public void setSrcId(String srcId) {
		this.srcId = srcId;
	}

	@Column(name = "COMBINE_VIDEO_SRC_TYPE")
	@Enumerated(value = EnumType.STRING)
	public CombineVideoSrcType getCombineVideoSrcType() {
		return combineVideoSrcType;
	}

	public void setCombineVideoSrcType(CombineVideoSrcType combineVideoSrcType) {
		this.combineVideoSrcType = combineVideoSrcType;
	}

	@Column(name = "COMBINE_VIDEO_POSITION_ID")
	public Long getCombineVideoPositionId() {
		return combineVideoPositionId;
	}

	public void setCombineVideoPositionId(Long combineVideoPositionId) {
		this.combineVideoPositionId = combineVideoPositionId;
	}

	@Column(name = "SERIAL")
	public Integer getSerial() {
		return serial;
	}

	public void setSerial(Integer serial) {
		this.serial = serial;
	}

	@Column(name = "VISIBLE")
	public Boolean getVisible() {
		return visible;
	}

	public void setVisible(Boolean visible) {
		this.visible = visible;
	}
	
}
