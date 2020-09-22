package com.suma.venus.resource.vo;

import com.suma.venus.resource.pojo.CommonPO;
import com.suma.venus.resource.pojo.VedioCapacityPO;

public class VedioCapacityVO extends CommonPO<VedioCapacityVO>{

	/**用户容量*/
	private Long userCapacity;
	
	/**图像容量*/
	private Long vedioCapacity;
	
	/**转发能力容量*/
	private Long turnCapacity;
	
	/**回放能力容量*/
	private Long replayCapacity;
	
	private Long userCount;
	
	private Long vedioCount;
	
	private Long turnCount;
	
	private Long reCount;
	
	private Long userIdleCount;
	
	private Long vedioIdleCount;
	
	private Long turnIdleCount;
	
	private Long reIdleCount;
	
	public Long getUserIdleCount() {
		return userIdleCount;
	}

	public VedioCapacityVO setUserIdleCount(Long userIdleCount) {
		this.userIdleCount = userIdleCount;
		return this;
	}

	public Long getVedioIdleCount() {
		return vedioIdleCount;
	}

	public VedioCapacityVO setVedioIdleCount(Long vedioIdleCount) {
		this.vedioIdleCount = vedioIdleCount;
		return this;
	}

	public Long getTurnIdleCount() {
		return turnIdleCount;
	}

	public VedioCapacityVO setTurnIdleCount(Long turnIdleCount) {
		this.turnIdleCount = turnIdleCount;
		return this;
	}

	public Long getReIdleCount() {
		return reIdleCount;
	}

	public VedioCapacityVO setReIdleCount(Long reIdleCount) {
		this.reIdleCount = reIdleCount;
		return this;
	}

	public Long getUserCount() {
		return userCount;
	}

	public VedioCapacityVO setUserCount(Long userCount) {
		this.userCount = userCount;
		return this;
	}

	public Long getVedioCount() {
		return vedioCount;
	}

	public VedioCapacityVO setVedioCount(Long vedioCount) {
		this.vedioCount = vedioCount;
		return this;
	}

	public Long getTurnCount() {
		return turnCount;
	}

	public VedioCapacityVO setTurnCount(Long turnCount) {
		this.turnCount = turnCount;
		return this;
	}

	public Long getReCount() {
		return reCount;
	}

	public VedioCapacityVO setReCount(Long reCount) {
		this.reCount = reCount;
		return this;
	}

	public Long getTurnCapacity() {
		return turnCapacity;
	}

	public VedioCapacityVO setTurnCapacity(Long turnCapacity) {
		this.turnCapacity = turnCapacity;
		return this;
	}

	public Long getReplayCapacity() {
		return replayCapacity;
	}

	public VedioCapacityVO setReplayCapacity(Long replayCapacity) {
		this.replayCapacity = replayCapacity;
		return this;
	}

	public Long getUserCapacity() {
		return userCapacity;
	}

	public VedioCapacityVO setUserCapacity(Long userCapacity) {
		this.userCapacity = userCapacity;
		return this;
	}

	public Long getVedioCapacity() {
		return vedioCapacity;
	}

	public VedioCapacityVO setVedioCapacity(Long vedioCapacity) {
		this.vedioCapacity = vedioCapacity;
		return this;
	}
	

	
	public VedioCapacityVO set(VedioCapacityPO entity) throws Exception {
		this.setUserCapacity(entity.getUserCapacity())
			.setVedioCapacity(entity.getVedioCapacity())
			.setTurnCapacity(entity.getTurnCapacity())
			.setReplayCapacity(entity.getReplayCapacity());
		return this;
	}
}
