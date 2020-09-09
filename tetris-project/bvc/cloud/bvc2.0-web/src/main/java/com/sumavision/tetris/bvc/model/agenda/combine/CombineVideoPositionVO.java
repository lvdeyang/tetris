package com.sumavision.tetris.bvc.model.agenda.combine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class CombineVideoPositionVO extends AbstractBaseVO<CombineVideoPositionVO, CombineVideoPositionPO>{

	private int serialnum;
	
	private String x;
	
	private String y;
	
	private String w;
	
	private String h;
	
	private String pictureType;
	
	private String pictureTypeName;
	
	private String pollingTime;
	
	private String pollingStatus;
	
	private String pollingStatusName;
	
	private Long combineVideoId;
	
	private List<CombineVideoSrcVO> srcs;
	
	public int getSerialnum() {
		return serialnum;
	}

	public CombineVideoPositionVO setSerialnum(int serialnum) {
		this.serialnum = serialnum;
		return this;
	}

	public String getX() {
		return x;
	}

	public CombineVideoPositionVO setX(String x) {
		this.x = x;
		return this;
	}

	public String getY() {
		return y;
	}

	public CombineVideoPositionVO setY(String y) {
		this.y = y;
		return this;
	}

	public String getW() {
		return w;
	}

	public CombineVideoPositionVO setW(String w) {
		this.w = w;
		return this;
	}

	public String getH() {
		return h;
	}

	public CombineVideoPositionVO setH(String h) {
		this.h = h;
		return this;
	}

	public String getPictureType() {
		return pictureType;
	}

	public CombineVideoPositionVO setPictureType(String pictureType) {
		this.pictureType = pictureType;
		return this;
	}

	public String getPictureTypeName() {
		return pictureTypeName;
	}

	public CombineVideoPositionVO setPictureTypeName(String pictureTypeName) {
		this.pictureTypeName = pictureTypeName;
		return this;
	}

	public String getPollingTime() {
		return pollingTime;
	}

	public CombineVideoPositionVO setPollingTime(String pollingTime) {
		this.pollingTime = pollingTime;
		return this;
	}

	public String getPollingStatus() {
		return pollingStatus;
	}

	public CombineVideoPositionVO setPollingStatus(String pollingStatus) {
		this.pollingStatus = pollingStatus;
		return this;
	}

	public String getPollingStatusName() {
		return pollingStatusName;
	}

	public CombineVideoPositionVO setPollingStatusName(String pollingStatusName) {
		this.pollingStatusName = pollingStatusName;
		return this;
	}

	public Long getCombineVideoId() {
		return combineVideoId;
	}

	public CombineVideoPositionVO setCombineVideoId(Long combineVideoId) {
		this.combineVideoId = combineVideoId;
		return this;
	}

	public List<CombineVideoSrcVO> getSrcs() {
		return srcs;
	}

	public CombineVideoPositionVO setSrcs(List<CombineVideoSrcVO> srcs) {
		this.srcs = srcs;
		return this;
	}

	public CombineVideoPositionVO addSrc(CombineVideoSrcVO src) {
		if(this.srcs == null) this.srcs = new ArrayList<CombineVideoSrcVO>();
		this.srcs.add(src);
		return this;
	}
	
	public void sortSrc() {
		if(this.srcs==null || this.srcs.size()<=0) return;
		Collections.sort(this.srcs, CombineVideoSrcVO.COMPARATOR);
	}
	
	@Override
	public CombineVideoPositionVO set(CombineVideoPositionPO entity) throws Exception {
		this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setSerialnum(entity.getSerialnum())
			.setX(entity.getX())
			.setY(entity.getY())
			.setW(entity.getW())
			.setH(entity.getH())
			.setPictureType(entity.getPictureType()==null?PictureType.STATIC.toString():entity.getPictureType().toString())
			.setPictureTypeName(entity.getPictureType()==null?PictureType.STATIC.getName():entity.getPictureType().getName())
			.setPollingTime(entity.getPollingTime())
			.setPollingStatus(entity.getPollingStatus()==null?null:entity.getPollingStatus().toString())
			.setPollingStatusName(entity.getPollingStatus()==null?null:entity.getPollingStatus().getName())
			.setCombineVideoId(entity.getCombineVideoId());
		return this;
	}

}
