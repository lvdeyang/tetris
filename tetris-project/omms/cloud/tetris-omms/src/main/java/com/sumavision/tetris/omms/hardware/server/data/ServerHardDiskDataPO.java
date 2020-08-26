package com.sumavision.tetris.omms.hardware.server.data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * 服务器硬盘数据<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年8月24日 下午5:24:38
 */
@Entity
@Table(name = "TETRIS_OMMS_SERVER_HARD_DISK_DATA")
public class ServerHardDiskDataPO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;

	/** 名称 */
	private String name;
	
	/** 存储总数 kb*/
	private Long total;
	
	/** 存储占用  kb*/
	private Long used;
	
	/** 存储空闲  kb*/
	private Long free;
	
	/** 服务器id */
	private Long serverId;

	@Column(name = "NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "TOTAL")
	public Long getTotal() {
		return total;
	}

	public void setTotal(Long total) {
		this.total = total;
	}

	@Column(name = "USED")
	public Long getUsed() {
		return used;
	}

	public void setUsed(Long used) {
		this.used = used;
	}

	@Column(name = "FREE")
	public Long getFree() {
		return free;
	}

	public void setFree(Long free) {
		this.free = free;
	}

	@Column(name = "SERVER_ID")
	public Long getServerId() {
		return serverId;
	}

	public void setServerId(Long serverId) {
		this.serverId = serverId;
	}
	
}
