package com.sumavision.tetris.mims.app.operation.mediaPackage;

import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class OperationPackageVO extends AbstractBaseVO<OperationPackageVO, OperationPackagePO>{
	private String name;
	
	private Long price;
	
	private String remark;
	
	private String status;

	public String getName() {
		return name;
	}

	public OperationPackageVO setName(String name) {
		this.name = name;
		return this;
	}

	public Long getPrice() {
		return price;
	}

	public OperationPackageVO setPrice(Long price) {
		this.price = price;
		return this;
	}

	public String getRemark() {
		return remark;
	}

	public OperationPackageVO setRemark(String remark) {
		this.remark = remark;
		return this;
	}
	
	public String getStatus() {
		return status;
	}

	public OperationPackageVO setStatus(String status) {
		this.status = status;
		return this;
	}

	@Override
	public OperationPackageVO set(OperationPackagePO entity) throws Exception {
		if (entity.getStatus() != null) this.setStatus(entity.getStatus().getName());
		return this.setId(entity.getId())
				.setUuid(entity.getUuid())
				.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
				.setName(entity.getName())
				.setPrice(entity.getPrice())
				.setRemark(entity.getRemark());
	}
}
