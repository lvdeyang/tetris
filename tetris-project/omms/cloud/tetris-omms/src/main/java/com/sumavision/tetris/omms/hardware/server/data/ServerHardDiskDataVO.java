package com.sumavision.tetris.omms.hardware.server.data;

import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class ServerHardDiskDataVO extends AbstractBaseVO<ServerHardDiskDataVO, ServerHardDiskDataPO>{

	/** 名称 */
	private String name;
	
	/** 存储总数 kb*/
	private Long total;
	
	/** 存储占用  kb*/
	private Long used;
	
	/** 存储空闲  kb*/
	private Long free;
	
	public String getName() {
		return name;
	}

	public ServerHardDiskDataVO setName(String name) {
		this.name = name;
		return this;
	}

	public Long getTotal() {
		return total;
	}

	public ServerHardDiskDataVO setTotal(Long total) {
		this.total = total;
		return this;
	}

	public Long getUsed() {
		return used;
	}

	public ServerHardDiskDataVO setUsed(Long used) {
		this.used = used;
		return this;
	}

	public Long getFree() {
		return free;
	}

	public ServerHardDiskDataVO setFree(Long free) {
		this.free = free;
		return this;
	}

	@Override
	public ServerHardDiskDataVO set(ServerHardDiskDataPO entity) throws Exception {
		this.setId(entity.getId())
			.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setName(entity.getName())
			.setTotal(entity.getTotal())
			.setUsed(entity.getUsed())
			.setFree(entity.getFree());
		return this;
	}

}
