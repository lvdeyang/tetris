package com.sumavision.signal.bvc.entity.vo;

import java.util.Date;

import com.sumavision.signal.bvc.entity.po.InternetAccessPO;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class InternetAccessVO extends AbstractBaseVO<InternetAccessVO, InternetAccessPO>{

	private Long id;
	
	private String address;
	
	private String type;
	
	public Long getId() {
		return id;
	}

	public InternetAccessVO setId(Long id) {
		this.id = id;
		return this;
	}

	public String getAddress() {
		return address;
	}

	public InternetAccessVO setAddress(String address) {
		this.address = address;
		return this;
	}

	public String getType() {
		return type;
	}

	public InternetAccessVO setType(String type) {
		this.type = type;
		return this;
	}

	@Override
	public InternetAccessVO set(InternetAccessPO entity) throws Exception {
		
		this.setId(entity.getId())
			.setAddress(entity.getAddress())
			.setType(entity.getType().getName())
			.setUpdateTime(new Date());
		
		return this;
	}

}
