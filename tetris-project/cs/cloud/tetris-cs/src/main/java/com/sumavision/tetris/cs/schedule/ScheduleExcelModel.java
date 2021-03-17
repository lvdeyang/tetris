package com.sumavision.tetris.cs.schedule;

import java.io.Serializable;
import java.util.Date;

import com.alibaba.excel.annotation.ExcelProperty;

public class ScheduleExcelModel implements Serializable {
	
	 @ExcelProperty(value = "排期日期", index = 0)
	 private String scheduleDate;
	 
	 @ExcelProperty(value = "节目名称", index = 1)
	 private String programName;

	 @ExcelProperty(value = "节目开始时间", index = 2)
	 private String pStartTime;

	 @ExcelProperty(value = "节目结束时间", index = 3)
	 private String pEndTime;

	 
	 public ScheduleExcelModel() {
	}
	 
	 public ScheduleExcelModel(String scheduleDate, String programName, String pStartTime, String pEndTime) {
		// TODO Auto-generated constructor stub
	}

	 public String getScheduleDate() {
		return scheduleDate;
	}

	 public void setScheduleDate(String scheduleDate) {
		this.scheduleDate = scheduleDate;
	}

	 public String getProgramName() {
		return programName;
	}

	 public void setProgramName(String programName) {
		this.programName = programName;
	}

	 public String getpStartTime() {
		return pStartTime;
	}

	 public void setpStartTime(String pStartTime) {
		this.pStartTime = pStartTime;
	}

	 public String getpEndTime() {
		return pEndTime;
	}

	 public void setpEndTime(String pEndTime) {
		this.pEndTime = pEndTime;
	}
	    
}
