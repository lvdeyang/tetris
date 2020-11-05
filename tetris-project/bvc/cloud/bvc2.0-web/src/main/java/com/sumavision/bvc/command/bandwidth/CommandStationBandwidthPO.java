package com.sumavision.bvc.command.bandwidth;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.sumavision.tetris.bvc.business.OriginType;
import com.sumavision.tetris.orm.po.AbstractBasePO;

@Entity
@Table(name="BVC_STATION_BANDWIDTH")
public class CommandStationBandwidthPO extends AbstractBasePO{
	
	private static final long serialVersionUID = 1L;

	/** 机构名称*/
	private String stationName;
	
	/** 总带宽*/
	private Integer totalWidth;
	
	/** 单路带宽*/
	private Integer singleWidth;
	
	/** 标识符*/
	private String identity;
	
	/** 来源：内部/外部系统 */
	private OriginType originType = OriginType.INNER;
	
	 /** 外部服务节点的uuid，仅当 originType = OriginType.OUTER 时有效 */
	private String serNodeUuid;

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

	@Column(name = "ORIGIN_TYPE")
	@Enumerated(value = EnumType.STRING)
	public OriginType getOriginType() {
		return originType;
	}

	public void setOriginType(OriginType originType) {
		this.originType = originType;
	}

	@Column(name = "SER_NODE_UUID")
	public String getSerNodeUuid() {
		return serNodeUuid;
	}

	public void setSerNodeUuid(String serNodeUuid) {
		this.serNodeUuid = serNodeUuid;
	}

}
