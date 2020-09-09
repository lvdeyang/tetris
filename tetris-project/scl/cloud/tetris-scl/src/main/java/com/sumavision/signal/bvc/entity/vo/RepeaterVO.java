package com.sumavision.signal.bvc.entity.vo;

import com.sumavision.signal.bvc.entity.po.RepeaterPO;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class RepeaterVO extends AbstractBaseVO<RepeaterVO, RepeaterPO>{

	private Long id;
	
	private String name;
	
	private String ip;
	
	private String address;
	
	private String type;
	
	private String repeaterId;
	
	private boolean isOpen;
	
	public Long getId() {
		return id;
	}

	public RepeaterVO setId(Long id) {
		this.id = id;
		return this;
	}

	public String getName() {
		return name;
	}

	public RepeaterVO setName(String name) {
		this.name = name;
		return this;
	}

	public String getIp() {
		return ip;
	}

	public RepeaterVO setIp(String ip) {
		this.ip = ip;
		return this;
	}

	public String getAddress() {
		return address;
	}

	public RepeaterVO setAddress(String address) {
		this.address = address;
		return this;
	}

	public String getType() {
		return type;
	}

	public RepeaterVO setType(String type) {
		this.type = type;
		return this;
	}

	public String getRepeaterId() {
		return repeaterId;
	}

	public RepeaterVO setRepeaterId(String repeaterId) {
		this.repeaterId = repeaterId;
		return this;
	}

	public boolean isOpen() {
		return isOpen;
	}

	public RepeaterVO setOpen(boolean isOpen) {
		this.isOpen = isOpen;
		return this;
	}

	@Override
	public RepeaterVO set(RepeaterPO entity) throws Exception {

		this.setId(entity.getId())
			.setName(entity.getName())
			.setIp(entity.getIp())
			.setAddress(entity.getAddress())
			.setRepeaterId(entity.getRepeaterId())
			.setType(entity.getType().getName())
			.setOpen(entity.isOpen());
		
		return this;
	}

}
