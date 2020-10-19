package com.sumavision.tetris.omms.hardware.server.data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * 服务器网卡流量<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年8月24日 下午5:26:11
 */
@Entity
@Table(name = "TETRIS_OMMS_SERVER_NETWORK_CARD_TRAFFIC_DATA")
public class ServerNetworkCardTrafficDataPO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;

	/** 网卡名称 */
	private String name;
	
	/** 收包速率 kbps*/
	private Long rxkB;
	
	/** 发包速率 kbps*/
	private Long txkB;
	
	/** 服务器id */
	private Long serverId;

	@Column(name = "NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "RXKB")
	public Long getRxkB() {
		return rxkB;
	}

	public void setRxkB(Long rxkB) {
		this.rxkB = rxkB;
	}
	
	public void setRxkB(String rxkB) {
		this.rxkB = parseKbps(rxkB);
	}

	@Column(name = "TXKB")
	public Long getTxkB() {
		return txkB;
	}

	public void setTxkB(Long txkB) {
		this.txkB = txkB;
	}

	public void setTxkB(String txkB) {
		this.txkB = parseKbps(txkB);
	}
	
	@Column(name = "SERVER_ID")
	public Long getServerId() {
		return serverId;
	}

	public void setServerId(Long serverId) {
		this.serverId = serverId;
	}
	
	/**
	 * 转化速率为Kbps<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年8月25日 上午10:38:47
	 * @param String speed 不规则的速率单位
	 * @return Long Kbps速率
	 */
	private Long parseKbps(String speed){
		if(speed.endsWith("Kbps")){
			return Double.valueOf(speed.replace("Kbps", "")).longValue();
		}else if(speed.endsWith("Mbps")){
			return Double.valueOf(speed.replace("Mbps", "")).longValue() * 1024;
		}else if(speed.endsWith("Gbps")){
			return Double.valueOf(speed.replace("Gbps", "")).longValue() * 1024 * 1024;
		}else{
			return Double.valueOf(speed.replace("bps", "")).longValue() / 1024;
		}
	}
	
}
