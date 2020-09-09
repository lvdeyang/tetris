package com.sumavision.tetris.easy.process.access.point;

import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class JointConstraintExpressionVO extends AbstractBaseVO<JointConstraintExpressionVO, JointConstraintExpressionPO>{

	private String name;
	
	private String expression;
	
	public String getName() {
		return name;
	}

	public JointConstraintExpressionVO setName(String name) {
		this.name = name;
		return this;
	}

	public String getExpression() {
		return expression;
	}

	public JointConstraintExpressionVO setExpression(String expression) {
		this.expression = expression;
		return this;
	}

	@Override
	public JointConstraintExpressionVO set(JointConstraintExpressionPO entity) throws Exception {
		this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setName(entity.getName())
			.setExpression(entity.getExpression());
		return this;
	}

}
