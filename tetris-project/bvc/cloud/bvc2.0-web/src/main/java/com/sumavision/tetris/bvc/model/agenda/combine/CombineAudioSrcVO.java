package com.sumavision.tetris.bvc.model.agenda.combine;

import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class CombineAudioSrcVO extends AbstractBaseVO<CombineAudioSrcVO, CombineAudioSrcPO>{

	private String srcId;
	
	private String combineAudioSrcType;
	
	private String combineAudioSrcTypeName;
	
	private String name;
	
	private Long combineAudioId;
	
	public String getSrcId() {
		return srcId;
	}

	public CombineAudioSrcVO setSrcId(String srcId) {
		this.srcId = srcId;
		return this;
	}

	public String getCombineAudioSrcType() {
		return combineAudioSrcType;
	}

	public CombineAudioSrcVO setCombineAudioSrcType(String combineAudioSrcType) {
		this.combineAudioSrcType = combineAudioSrcType;
		return this;
	}

	public String getCombineAudioSrcTypeName() {
		return combineAudioSrcTypeName;
	}

	public CombineAudioSrcVO setCombineAudioSrcTypeName(String combineAudioSrcTypeName) {
		this.combineAudioSrcTypeName = combineAudioSrcTypeName;
		return this;
	}

	public String getName() {
		return name;
	}

	public CombineAudioSrcVO setName(String name) {
		this.name = name;
		return this;
	}

	public Long getCombineAudioId() {
		return combineAudioId;
	}

	public CombineAudioSrcVO setCombineAudioId(Long combineAudioId) {
		this.combineAudioId = combineAudioId;
		return this;
	}

	@Override
	public CombineAudioSrcVO set(CombineAudioSrcPO entity) throws Exception {
		this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setSrcId(entity.getSrcId())
			.setCombineAudioSrcType(entity.getCombineAudioSrcType().toString())
			.setCombineAudioSrcTypeName(entity.getCombineAudioSrcType().getName())
			.setCombineAudioId(entity.getCombineAudioId());
		return this;
	}

}
