package com.sumavision.tetris.easy.process.access.point;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * 接入点联合约束表达式<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2018年12月13日 下午4:26:09
 */
@Entity
@Table(name = "TETRIS_PROCESS_JOINT_CONSTRAINT")
public class JointConstraintExpressionPO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;

	/** 约束名称 */
	private String name;
	
	/** 表达式内容 */
	private String expression;
	
	/** 接入点id */
	private Long accessPointId;

	@Column(name = "NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "EXPRESSION")
	public String getExpression() {
		return expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}

	@Column(name = "ACCESS_POINT_ID")
	public Long getAccessPointId() {
		return accessPointId;
	}

	public void setAccessPointId(Long accessPointId) {
		this.accessPointId = accessPointId;
	}
	
}
