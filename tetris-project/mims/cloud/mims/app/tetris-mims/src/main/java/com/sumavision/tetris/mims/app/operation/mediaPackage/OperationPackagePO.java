package com.sumavision.tetris.mims.app.operation.mediaPackage;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

@Entity
@Table(name = "MIMS_OPERATION_PACKAGE")
public class OperationPackagePO extends AbstractBasePO{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/** 套餐名称 */
	private String name;
	
	/** 套餐价格 */
	private Long price;
	
	/** 套餐状态 */
	private OperationPackageStatus status;
	
	/** 备注 */
	private String remark;
	
	/** 组织id */
	private String groupId;

	@Column(name = "NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "PRICE")
	public Long getPrice() {
		return price;
	}

	public void setPrice(Long price) {
		this.price = price;
	}
	
	@Enumerated(value = EnumType.STRING)
	@Column(name = "STATUS")
	public OperationPackageStatus getStatus() {
		return status;
	}

	public void setStatus(OperationPackageStatus status) {
		this.status = status;
	}

	@Column(name = "REMARK")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(name = "GROUP_ID")
	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
}
