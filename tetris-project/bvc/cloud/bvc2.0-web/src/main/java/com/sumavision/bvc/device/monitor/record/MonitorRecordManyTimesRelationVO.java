package com.sumavision.bvc.device.monitor.record;

import java.util.Date;

import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class MonitorRecordManyTimesRelationVO extends AbstractBaseVO<MonitorRecordManyTimesRelationVO, MonitorRecordManyTimesRelationPO>{
		
	/** 对应record中的id*/
	private Long businessId;
	
	/** 排期模式*/
	private String timeSegmentMode;
	
	/** 下一次录制开始时间*/
	private String nextStartTime;
	
	/** 下一次录制结束时间*/
	private String nextEndTime;
	
	/** 天为单位开始时间*/
	private String timeSegmentStartTime;
	
	/** 天为单位结束时间*/
	private String timeSegmentEndTime;
	
	/** 周为单位开始时间*/
	private String timeSegmentStartWeek;
	
	/** 周为单位结束时间*/
	private String timeSegmentEndWeek;
	
	/** 月为单位开始时间*/
	private String timeSegmentStartDay;
	
	/** 月为单位结束时间*/
	private String timeSegmentEndDay;
	
	/** 当前状态：停止，等待*/
	private String status;
	
	/** 记录序号，用于拼接存储地址*/
	private Integer indexNumber;
	
	/** 拼好的开始时间*/
	private String startTime;
	
	/** 拼好的结束时间*/
	private String endTime;
	
	public String getStartTime() {
		return startTime;
	}

	public MonitorRecordManyTimesRelationVO setStartTime(String startTime) {
		this.startTime = startTime;
		return this;
	}

	public String getEndTime() {
		return endTime;
	}

	public MonitorRecordManyTimesRelationVO setEndTime(String endTime) {
		this.endTime = endTime;
		return this;
	}

	public Long getBusinessId() {
		return businessId;
	}

	public MonitorRecordManyTimesRelationVO setBusinessId(Long businessId) {
		this.businessId = businessId;
		return this;
	}



	public String getTimeSegmentMode() {
		return timeSegmentMode;
	}



	public MonitorRecordManyTimesRelationVO setTimeSegmentMode(String timeSegmentMode) {
		this.timeSegmentMode = timeSegmentMode;
		return this;
	}



	public String getNextStartTime() {
		return nextStartTime;
	}



	public MonitorRecordManyTimesRelationVO setNextStartTime(String nextStartTime) {
		this.nextStartTime = nextStartTime;
		return this;
	}



	public String getNextEndTime() {
		return nextEndTime;
	}



	public MonitorRecordManyTimesRelationVO setNextEndTime(String nextEndTime) {
		this.nextEndTime = nextEndTime;
		return this;
	}



	public String getTimeSegmentStartTime() {
		return timeSegmentStartTime;
	}



	public MonitorRecordManyTimesRelationVO setTimeSegmentStartTime(String timeSegmentStartTime) {
		this.timeSegmentStartTime = timeSegmentStartTime;
		return this;
	}



	public String getTimeSegmentEndTime() {
		return timeSegmentEndTime;
	}



	public MonitorRecordManyTimesRelationVO setTimeSegmentEndTime(String timeSegmentEndTime) {
		this.timeSegmentEndTime = timeSegmentEndTime;
		return this;
	}



	public String getTimeSegmentStartWeek() {
		return timeSegmentStartWeek;
	}



	public MonitorRecordManyTimesRelationVO setTimeSegmentStartWeek(String timeSegmentStartWeek) {
		this.timeSegmentStartWeek = timeSegmentStartWeek;
		return this;
	}



	public String getTimeSegmentEndWeek() {
		return timeSegmentEndWeek;
	}



	public MonitorRecordManyTimesRelationVO setTimeSegmentEndWeek(String timeSegmentEndWeek) {
		this.timeSegmentEndWeek = timeSegmentEndWeek;
		return this;
	}



	public String getTimeSegmentStartDay() {
		return timeSegmentStartDay;
	}



	public MonitorRecordManyTimesRelationVO setTimeSegmentStartDay(String timeSegmentStartDay) {
		this.timeSegmentStartDay = timeSegmentStartDay;
		return this;
	}



	public String getTimeSegmentEndDay() {
		return timeSegmentEndDay;
	}



	public MonitorRecordManyTimesRelationVO setTimeSegmentEndDay(String timeSegmentEndDay) {
		this.timeSegmentEndDay = timeSegmentEndDay;
		return this;
	}



	public String getStatus() {
		return status;
	}



	public MonitorRecordManyTimesRelationVO setStatus(String status) {
		this.status = status;
		return this;
	}



	public Integer getIndexNumber() {
		return indexNumber;
	}



	public MonitorRecordManyTimesRelationVO setIndexNumber(Integer indexNumber) {
		this.indexNumber = indexNumber;
		return this;
	}



	@Override
	public MonitorRecordManyTimesRelationVO set(MonitorRecordManyTimesRelationPO entity) throws Exception {
		
		if(MonitorRecordManyTimesMode.DAY.equals(entity.getMode())){
			this.setStartTime(entity.getDayStart());
			this.setEndTime(entity.getDayEnd());
		}else if(MonitorRecordManyTimesMode.WEEK.equals(entity.getMode())){
			this.setStartTime(entity.getWeekStart()==null?"":entity.getWeekStart()+"-"+entity.getDayStart()==null?"":entity.getDayStart());
			this.setEndTime(entity.getWeekEnd()==null?"":entity.getWeekEnd()+"-"+entity.getDayEnd()==null?"":entity.getDayEnd());
		}else if(MonitorRecordManyTimesMode.MONTH.equals(entity.getMode())){
			this.setStartTime(entity.getDayOfMonthStart()==null?"":entity.getDayOfMonthStart().toString()+"-"+(entity.getDayStart()==null?"":entity.getDayStart()));
			this.setEndTime(entity.getDayOfMonthEnd()==null?"":entity.getDayOfMonthEnd().toString()+"-"+(entity.getDayEnd()==null?"":entity.getDayEnd()));
		}
		
		return this.setBusinessId(entity.getBusinessId())
				.setTimeSegmentMode(entity.getMode().getName())
				.setNextStartTime(entity.getNextStartTime()==null?"":DateUtil.format(entity.getNextStartTime(), DateUtil.dateTimePattern))
				.setNextEndTime(entity.getNextEndTime()==null?"":DateUtil.format(entity.getNextEndTime(),DateUtil.dateTimePattern))
				.setTimeSegmentStartTime(entity.getDayStart())
				.setTimeSegmentEndTime(entity.getDayEnd())
				.setTimeSegmentStartWeek(entity.getWeekStart())
				.setTimeSegmentEndWeek(entity.getWeekEnd())
				.setTimeSegmentStartDay(entity.getDayOfMonthStart())
				.setTimeSegmentEndDay(entity.getDayOfMonthEnd())
				.setStatus(entity.getStatus().getName())
				.setIndexNumber(entity.getIndexNumber());
	}

}
