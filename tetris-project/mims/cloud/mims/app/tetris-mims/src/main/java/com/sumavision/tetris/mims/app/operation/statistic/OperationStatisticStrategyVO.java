package com.sumavision.tetris.mims.app.operation.statistic;

import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class OperationStatisticStrategyVO extends AbstractBaseVO<OperationStatisticStrategyVO, OperationStatisticStrategyPO>{
	private Integer producer;
	
	private Integer operator;
	
	private String GroupId;

	public Integer getProducer() {
		return producer;
	}

	public OperationStatisticStrategyVO setProducer(Integer producer) {
		this.producer = producer;
		return this;
	}

	public Integer getOperator() {
		return operator;
	}

	public OperationStatisticStrategyVO setOperator(Integer operator) {
		this.operator = operator;
		return this;
	}

	public String getGroupId() {
		return GroupId;
	}

	public OperationStatisticStrategyVO setGroupId(String groupId) {
		GroupId = groupId;
		return this;
	}

	@Override
	public OperationStatisticStrategyVO set(OperationStatisticStrategyPO entity) throws Exception {
		return this.setId(entity.getId())
				.setUuid(entity.getUuid())
				.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
				.setOperator(entity.getOperator())
				.setProducer(entity.getProducer());
	}
}
