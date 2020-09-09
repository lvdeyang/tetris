package com.sumavision.tetris.bvc.model.agenda.combine;

import java.util.Comparator;

import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class CombineVideoSrcVO extends AbstractBaseVO<CombineVideoSrcVO, CombineVideoSrcPO>{

	public static CombineVideoSrcComparator COMPARATOR = new CombineVideoSrcComparator();
	
	private String srcId;
	
	private String combineVideoSrcType;
	
	private String combineVideoSrcTypeName;
	
	private Long combineVideoPositionId;
	
	private Integer serial;
	
	private Boolean visible;
	
	public String getSrcId() {
		return srcId;
	}

	public CombineVideoSrcVO setSrcId(String srcId) {
		this.srcId = srcId;
		return this;
	}

	public String getCombineVideoSrcType() {
		return combineVideoSrcType;
	}

	public CombineVideoSrcVO setCombineVideoSrcType(String combineVideoSrcType) {
		this.combineVideoSrcType = combineVideoSrcType;
		return this;
	}

	public String getCombineVideoSrcTypeName() {
		return combineVideoSrcTypeName;
	}

	public CombineVideoSrcVO setCombineVideoSrcTypeName(String combineVideoSrcTypeName) {
		this.combineVideoSrcTypeName = combineVideoSrcTypeName;
		return this;
	}

	public Long getCombineVideoPositionId() {
		return combineVideoPositionId;
	}

	public CombineVideoSrcVO setCombineVideoPositionId(Long combineVideoPositionId) {
		this.combineVideoPositionId = combineVideoPositionId;
		return this;
	}

	public Integer getSerial() {
		return serial;
	}

	public CombineVideoSrcVO setSerial(Integer serial) {
		this.serial = serial;
		return this;
	}

	public Boolean getVisible() {
		return visible;
	}

	public CombineVideoSrcVO setVisible(Boolean visible) {
		this.visible = visible;
		return this;
	}

	@Override
	public CombineVideoSrcVO set(CombineVideoSrcPO entity) throws Exception {
		this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setSrcId(entity.getSrcId())
			.setCombineVideoSrcType(entity.getCombineVideoSrcType().toString())
			.setCombineVideoSrcTypeName(entity.getCombineVideoSrcType().getName())
			.setCombineVideoPositionId(entity.getCombineVideoPositionId())
			.setSerial(entity.getSerial())
			.setVisible(entity.getVisible());
		return this;
	}

	public static class CombineVideoSrcComparator implements Comparator<CombineVideoSrcVO>{

		@Override
		public int compare(CombineVideoSrcVO o1, CombineVideoSrcVO o2) {
			return o1.getSerial().intValue() - o2.getSerial().intValue();
		}
		
	}
	
}
