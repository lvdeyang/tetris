package com.suma.venus.resource.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class VedioCapacityPO extends CommonPO<VedioCapacityPO>{
	
	/**用户容量*/
	private Long userCapacity;
	
	/**图像容量*/
	private Long vedioCapacity;
	
	/**转发能力容量*/
	private Long turnCapacity;
	
	/**回放能力容量*/
	private Long replayCapacity;
	
	private String propKey;
	
	private String propValue;
	
	@Column(name = "turn_capacity")
	public Long getTurnCapacity() {
		return turnCapacity;
	}

	public void setTurnCapacity(Long turnCapacity) {
		this.turnCapacity = turnCapacity;
	}

	@Column(name ="replay_capacity")
	public Long getReplayCapacity() {
		return replayCapacity;
	}

	public void setReplayCapacity(Long replayCapacity) {
		this.replayCapacity = replayCapacity;
	}

	@Column(name = "user_Capacity")
	public Long getUserCapacity() {
		return userCapacity;
	}

	public void setUserCapacity(Long userCapacity) {
		this.userCapacity = userCapacity;
	}

	@Column(name = "vedio_Capacity")
	public Long getVedioCapacity() {
		return vedioCapacity;
	}

	public void setVedioCapacity(Long vedioCapacity) {
		this.vedioCapacity = vedioCapacity;
	}
	
	 

}
