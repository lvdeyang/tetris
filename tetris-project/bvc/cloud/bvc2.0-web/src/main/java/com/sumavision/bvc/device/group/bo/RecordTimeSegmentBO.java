package com.sumavision.bvc.device.group.bo;

import java.util.Date;

import com.sumavision.bvc.device.monitor.record.MonitorRecordManyTimesMode;
import com.sumavision.bvc.device.monitor.record.MonitorRecordManyTimesRelationPO;
import com.sumavision.tetris.commons.util.date.DateUtil;

public class RecordTimeSegmentBO {

	/** day、week、month*/
	private String mode;
	
	/** 创建时间*/
	private String create_date;
	
	private RecordDateTimeBO day;
	
	private RecordDateTimeBO week;
	
	private RecordDateTimeBO month;

	public String getCreate_date() {
		return create_date;
	}

	public RecordTimeSegmentBO setCreate_date(String create_date) {
		this.create_date = create_date;
		return this;
	}

	public String getMode() {
		return mode;
	}

	public RecordTimeSegmentBO setMode(String mode) {
		this.mode = mode;
		return this;
	}

	public RecordDateTimeBO getDay() {
		return day;
	}

	public RecordTimeSegmentBO setDay(RecordDateTimeBO day) {
		this.day = day;
		return this;
	}

	public RecordDateTimeBO getWeek() {
		return week;
	}

	public RecordTimeSegmentBO setWeek(RecordDateTimeBO week) {
		this.week = week;
		return this;
	}

	public RecordDateTimeBO getMonth() {
		return month;
	}

	public RecordTimeSegmentBO setMonth(RecordDateTimeBO month) {
		this.month = month;
		return this;
	}
	
	public RecordTimeSegmentBO set(MonitorRecordManyTimesRelationPO relation){
		MonitorRecordManyTimesMode mode=relation.getMode();
		this.setCreate_date(DateUtil.format(new Date(), DateUtil.dateTimePattern));
		this.setMode(mode.getName());
		if(MonitorRecordManyTimesMode.DAY.equals(mode)){
			RecordDateTimeBO day=new RecordDateTimeBO();
			day.setStart(relation.getDayStart());
			day.setEnd(relation.getDayEnd());
			this.setDay(day);
		}else if(MonitorRecordManyTimesMode.WEEK.equals(mode)){
			RecordDateTimeBO week=new RecordDateTimeBO();
			week.setStart(relation.getWeekStart()+"-"+relation.getDayStart());
			week.setEnd(relation.getWeekEnd()+"-"+relation.getDayEnd());
			this.setDay(week);
		}else if(MonitorRecordManyTimesMode.MONTH.equals(mode)){
			RecordDateTimeBO month=new RecordDateTimeBO();
			month.setStart(relation.getDayOfMonthStart()+"-"+relation.getDayStart());
			month.setEnd(relation.getDayOfMonthEnd()+"-"+relation.getDayEnd());
			this.setDay(month);
		}
		return this;
	}
	
}
