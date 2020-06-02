package com.sumavision.tetris.bvc.model.agenda.combine;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

@Entity
@Table(name = "TETRIS_BVC_MODEL_AGENDA_COMBINE_VIDEO_SRC")
public class CombineVideoSrcPO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;

	/** 源id */
	private Long srcId;
	
	/** 源类型 */
	private CombineVideoSrcType combineVideoSrcType;
	
	/** 隶属合屏id */
	private Long combineVideoId;
	
	/** 轮询时进行源排序 */
	private Integer index;
	
	/** 轮询时是否展示该源 */
	private Boolean visible;

	@Column(name = "SRC_ID")
	public Long getSrcId() {
		return srcId;
	}

	public void setSrcId(Long srcId) {
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

	@Column(name = "COMBINE_VIDEO_ID")
	public Long getCombineVideoId() {
		return combineVideoId;
	}

	public void setCombineVideoId(Long combineVideoId) {
		this.combineVideoId = combineVideoId;
	}

	@Column(name = "INDEX")
	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

	@Column(name = "VISIBLE")
	public Boolean getVisible() {
		return visible;
	}

	public void setVisible(Boolean visible) {
		this.visible = visible;
	}
	
}
