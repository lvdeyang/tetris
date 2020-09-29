package com.sumavision.tetris.patrol.sign;

import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class SignVO extends AbstractBaseVO<SignVO, SignPO>{

	private String name;
	
	private String phone;
	
	private String signTime;
	
	private String addressName;
	
	private Long addressId;
	
	public String getName() {
		return name;
	}

	public SignVO setName(String name) {
		this.name = name;
		return this;
	}

	public String getPhone() {
		return phone;
	}

	public SignVO setPhone(String phone) {
		this.phone = phone;
		return this;
	}

	public String getSignTime() {
		return signTime;
	}

	public SignVO setSignTime(String signTime) {
		this.signTime = signTime;
		return this;
	}

	public String getAddressName() {
		return addressName;
	}

	public SignVO setAddressName(String addressName) {
		this.addressName = addressName;
		return this;
	}

	public Long getAddressId() {
		return addressId;
	}

	public SignVO setAddressId(Long addressId) {
		this.addressId = addressId;
		return this;
	}

	@Override
	public SignVO set(SignPO entity) throws Exception {
		this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setName(entity.getName())
			.setPhone(entity.getPhone())
			.setSignTime(entity.getSignTime()==null?"":DateUtil.format(entity.getSignTime(), DateUtil.dateTimePattern))
			.setAddressId(entity.getAddressId());
		return this;
	}

}
