package com.sumavision.tetris.business.common.po;

import javax.persistence.*;

import com.sumavision.tetris.business.common.enumeration.BusinessType;
import com.sumavision.tetris.orm.po.AbstractBasePO;

import java.util.Date;

@Entity
@Table(name = "TETRIS_CAPACITY_TASK_INPUT", uniqueConstraints = {@UniqueConstraint(columnNames={"uniq"})})
public class TaskInputPO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;

	private Long version;
	
	private String taskUuid;
	
	private String capacityIp;
	
	private String input;
	
	/** 保证源的唯一性，yjgb: ip@port; 直播: url; 收录: url */
	private String uniq;
	
	/** 计数 */
	private Integer count = 1;
	
	private BusinessType type;

	private Date createTime;

	@Version
	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	public String getTaskUuid() {
		return taskUuid;
	}

	public void setTaskUuid(String taskUuid) {
		this.taskUuid = taskUuid;
	}

	@Column(name = "INPUT", columnDefinition = "longtext")
	public String getInput() {
		return input;
	}

	public void setInput(String input) {
		this.input = input;
	}

	public String getUniq() {
		return uniq;
	}

	public void setUniq(String uniq) {
		this.uniq = uniq;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	@Enumerated(EnumType.STRING)
	public BusinessType getType() {
		return type;
	}

	public void setType(BusinessType type) {
		this.type = type;
	}

	public String getCapacityIp() {
		return capacityIp;
	}

	public void setCapacityIp(String capacityIp) {
		this.capacityIp = capacityIp;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATE_TIME")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

}
