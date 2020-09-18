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
 * <p>排期录制的规则对应表</p>
 * <b>作者:</b>lixin<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年9月17日 上午9:32:29
 */
@Entity
@Table(name="MONITOR_RECORD_MANY_TIMES_RELATION")
public class MonitorRecordManyTimesRelationPO extends AbstractBasePO{
	
	private static final long serialVersionUID = 1L;

	/** 对应record中的id*/
	private Long businessId;
	
	/** 排期模式*/
	private MonitorRecordManyTimesMode mode;
	
	/** 下一次录制开始时间*/
	private Date nextStartTime;
	
	/** 下一次录制结束时间*/
	private Date nextEndTime;
	
	/** 排期规定时间：意义，防止以后扩展改前边代码*/
	private String timeQuantum;
	
	/** 天为单位开始时间*/
	private String dayStart;
	
	/** 天为单位结束时间*/
	private String dayEnd;
	
	/** 周为单位开始时间*/
	private String weekStart;
	
	/** 周为单位结束时间*/
	private String weekEnd;
	
	/** 月为单位开始时间*/
	private String dayOfMonthStart;
	
	/** 月为单位结束时间*/
	private String dayOfMonthEnd;
	
	/** 当前状态：停止，等待*/
	private MonitorRecordStatus status;
	
	/** 记录序号，用于拼接存储地址*/
	private int indexNumber;

	@Column(name="BUSINESSID")
	public Long getBusinessId() {
		return businessId;
	}
	
	public MonitorRecordManyTimesRelationPO setBusinessId(Long businessId) {
		this.businessId = businessId;
		return this;
	}
	
	@Enumerated(value=EnumType.STRING)
	@Column(name="MODE")
	public MonitorRecordManyTimesMode getMode() {
		return mode;
	}

	public MonitorRecordManyTimesRelationPO setMode(MonitorRecordManyTimesMode mode) {
		this.mode = mode;
		return this;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="NEXT_START_TIME")
	public Date getNextStartTime() {
		return nextStartTime;
	}

	public MonitorRecordManyTimesRelationPO setNextStartTime(Date nextStartTime) {
		this.nextStartTime = nextStartTime;
		return this;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="NEXT_END_TIME")
	public Date getNextEndTime() {
		return nextEndTime;
	}

	public MonitorRecordManyTimesRelationPO setNextEndTime(Date nextEndTime) {
		this.nextEndTime = nextEndTime;
		return this;
	}

	@Column(name="TIME_QUANTUM")
	public String getTimeQuantum() {
		return timeQuantum;
	}

	public MonitorRecordManyTimesRelationPO setTimeQuantum(String timeQuantum) {
		this.timeQuantum = timeQuantum;
		return this;
	}

	@Column(name="DAY_START")
	public String getDayStart() {
		return dayStart;
	}

	public MonitorRecordManyTimesRelationPO setDayStart(String dayStart) {
		this.dayStart = dayStart;
		return this;
	}

	@Column(name="DAY_END")
	public String getDayEnd() {
		return dayEnd;
	}

	public MonitorRecordManyTimesRelationPO setDayEnd(String dayEnd) {
		this.dayEnd = dayEnd;
		return this;
	}

	@Column(name="WEEK_START")
	public String getWeekStart() {
		return weekStart;
	}

	public MonitorRecordManyTimesRelationPO setWeekStart(String weekStart) {
		this.weekStart = weekStart;
		return this;
	}

	@Column(name="WEEK_END")
	public String getWeekEnd() {
		return weekEnd;
	}

	public MonitorRecordManyTimesRelationPO setWeekEnd(String weekEnd) {
		this.weekEnd = weekEnd;
		return this;
	}

	@Column(name="DAY_OF_MONTH_START")
	public String getDayOfMonthStart() {
		return dayOfMonthStart;
	}

	public MonitorRecordManyTimesRelationPO setDayOfMonthStart(String dayOfMonthStart) {
		this.dayOfMonthStart = dayOfMonthStart;
		return this;
	}

	@Column(name="DAY_OF_MONTH_END")
	public String getDayOfMonthEnd() {
		return dayOfMonthEnd;
	}

	public MonitorRecordManyTimesRelationPO setDayOfMonthEnd(String dayOfMonthEnd) {
		this.dayOfMonthEnd = dayOfMonthEnd;
		return this;
	}

	@Column(name="STATUS")
	public MonitorRecordStatus getStatus() {
		return status;
	}

	public MonitorRecordManyTimesRelationPO setStatus(MonitorRecordStatus status) {
		this.status = status;
		return this;
	}

	@Column(name="INDEX_NUMBER")
	public int getIndexNumber() {
		return indexNumber;
	}

	public MonitorRecordManyTimesRelationPO setIndexNumber(int indexNumber) {
		this.indexNumber = indexNumber;
		return this;
	}
	
}
