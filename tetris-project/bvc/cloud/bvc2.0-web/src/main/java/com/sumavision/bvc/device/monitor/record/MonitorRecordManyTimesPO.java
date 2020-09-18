package com.sumavision.bvc.device.monitor.record;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * <p>排期录制（多次录制）的对应关系</p>
 * <b>作者:</b>lixin<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年9月16日 下午3:17:37
 */
@Entity
@Table(name="BVC_MONITOR_RECORD_MANY_TIMES")
public class MonitorRecordManyTimesPO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;
	
	/** 开始录制时间 */
	private Date startTime;
	
	/** 结束录制时间 */
	private Date endTime;
	
	/** 录制状态：录制中，已完成 */
	private MonitorRecordStatus status;
	
	/**MonitorRecordMangTimesRelationPO表中的id */
	private Long relationId;
	
	/** 存储相关序号*/
	private Integer indexNumber;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "START_TIME")
	public Date getStartTime() {
		return startTime;
	}

	public MonitorRecordManyTimesPO setStartTime(Date startTime) {
		this.startTime = startTime;
		return this;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "END_TIME")
	public Date getEndTime() {
		return endTime;
	}

	public MonitorRecordManyTimesPO setEndTime(Date endTime) {
		this.endTime = endTime;
		return this;
	}

	@Enumerated(value = EnumType.STRING)
	@Column(name = "STATUS")
	public MonitorRecordStatus getStatus() {
		return status;
	}

	public MonitorRecordManyTimesPO setStatus(MonitorRecordStatus status) {
		this.status = status;
		return this;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Column(name="RELATIONID")
	public Long getRelationId() {
		return relationId;
	}

	public MonitorRecordManyTimesPO setRelationId(Long relationId) {
		this.relationId = relationId;
		return this;
	}

	@Column(name="INDEX_NUMBER")
	public Integer getIndexNumber() {
		return indexNumber;
	}

	public MonitorRecordManyTimesPO setIndexNumber(Integer indexNumber) {
		this.indexNumber = indexNumber;
		return this;
	}
	
	
}
