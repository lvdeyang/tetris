package com.sumavision.tetris.patrol.address;

import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class AddressVO extends AbstractBaseVO<AddressVO, AddressPO>{

	private String name;
	
	private String url;
	
	public String getName() {
		return name;
	}

	public AddressVO setName(String name) {
		this.name = name;
		return this;
	}

	public String getUrl() {
		return url;
	}

	public AddressVO setUrl(String url) {
		this.url = url;
		return this;
	}

	@Override
	public AddressVO set(AddressPO entity) throws Exception {
		this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setName(entity.getName());
		return this;
	}

}
