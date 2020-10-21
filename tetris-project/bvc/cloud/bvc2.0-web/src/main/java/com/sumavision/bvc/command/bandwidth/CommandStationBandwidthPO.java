package com.sumavision.bvc.command.bandwidth;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

import net.bytebuddy.agent.builder.AgentBuilder.Matchable.AbstractBase;

@Entity
@Table(name="BVC_STATION_BANDWIDTH")
public class CommandStationBandwidthPO extends AbstractBasePO{
	
	/** 机构名称*/
	private String stationName;
	
	/** 总带宽*/
	private Integer totalWidth;
	
	/** 单路带宽*/
	private Integer singleWidth;
	
	/** 标识符*/
	private String identity;

	@Column(name="IDENTITY")
	public String getIdentity() {
		return identity;
	}

	public CommandStationBandwidthPO setIdentity(String identity) {
		this.identity = identity;
		return this;
	}

	@Column(name="STATION_NAME")
	public String getStationName() {
		return stationName;
	}

	public CommandStationBandwidthPO setStationName(String stationName) {
		this.stationName = stationName;
		return this;
	}

	@Column(name="TOTAL_WIDTH")
	public Integer getTotalWidth() {
		return totalWidth;
	}

	public CommandStationBandwidthPO setTotalWidth(Integer totalWidth) {
		this.totalWidth = totalWidth;
		return this;
	}

	@Column(name="SINGLE_WIDTH")
	public Integer getSingleWidth() {
		return singleWidth;
	}

	public CommandStationBandwidthPO setSingleWidth(Integer singleWidth) {
		this.singleWidth = singleWidth;
		return this;
	}

}
