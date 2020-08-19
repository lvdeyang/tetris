package com.sumavision.tetris.mims.app.operation.statistic;

import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.sumavision.tetris.mims.app.operation.mediaPackage.OperationPackageStatus;
import com.sumavision.tetris.orm.po.AbstractBasePO;

@Entity
@Table(name = "MIMS_OPERATION_STATISTIC_STRATEGY")
public class OperationStatisticStrategyPO extends AbstractBasePO{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** 生产者占比 */
	private Integer producer;
	
	/** 运营商占比 */
	private Integer operator;
	
	/** 组织id */
	private String groupId;
	
	/** 结算策略状态 */
	private OperationPackageStatus status;

	@Column(name = "PRODUCER")
	public Integer getProducer() {
		return producer;
	}

	public void setProducer(Integer producer) {
		this.producer = producer;
	}

	@Column(name = "OPERATOR")
	public Integer getOperator() {
		return operator;
	}

	public void setOperator(Integer operator) {
		this.operator = operator;
	}

	@Column(name = "GROUP_ID")
	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	
	@Enumerated(value = EnumType.STRING)
	@Column(name = "STATUS")
	public OperationPackageStatus getStatus() {
		return status;
	}

	public void setStatus(OperationPackageStatus status) {
		this.status = status;
	}
}
