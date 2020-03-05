package com.sumavision.tetris.record.task;

import java.util.Date;

import javax.persistence.Column;

import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * 收录任务po
 * 
 * 一个收录任务对应一个源信息，对应多个子任务 recordTaskItemPO
 * 
 * 主要字段： 源信息（包括type url 可选节目号 及媒资id等） 任务类型 任务名称 创建人 任务状态
 * 
 * 
 * @author chenmo
 *
 */
public class RecordTaskPO extends AbstractBasePO {

	private static final long serialVersionUID = 1L;

	// 收录任务名称
	private String name;

	private Status status;

	private Type type;

	// 源的id，此处只媒资系统内源的Id
	private String sourceId;

	// 源的封装格式，从媒资系统获取
	private String sourceType;

	// 源的Url，从媒资系统获取
	private String sourceUrl;

	// 源的program id，暂不涉及，备用
	private Integer programId;

	/*
	 * startDate endDate表示录制的开始和结束日期，taskListJson表示
	 * 排期单的json串，recordTimeSlots表示录制的多时间段，loopCycles表示多个循环周期（逗号分隔）
	 **/
	@Column(name = "startDate")
	private String startDate;

	@Column(name = "endDate")
	private String endDate;

	@Column(name = "recordTimeSlotJson", length = 1000)
	private String recordTimeSlotJson;

	@Column(name = "loopCycles")
	private String loopCycles;

	@Column(name = "createTime")
	private String createTime;

	@Column(name = "creater")
	private String creater;

	@Column(name = "updater")
	private String updater;

	public enum Status {
		NEW("新建"), RUNNING("执行中"), STOP("停止"), ERROR("流异常");// 添加异常状态，异常是运行态，但未录制，yzx add on 20190402

		private String name;

		private Status(String name) {
			this.name = name;
		}

		public String getName() {
			return this.name;
		}

		public static Status fromName(String name) throws Exception {
			if ("新建".equals(name)) {
				return NEW;
			} else if ("执行中".equals(name)) {
				return RUNNING;
			} else if ("停止".equals(name)) {
				return STOP;
			} else if ("流异常".equals(name)) {
				return ERROR;
			} else {
				throw new Exception("错误的状态类型：" + name);
			}
		}

		public static Status fromString(String s) throws Exception {
			if ("NEW".equals(s)) {
				return NEW;
			} else if ("RUNNING".equals(s)) {
				return RUNNING;
			} else if ("STOP".equals(s)) {
				return STOP;
			} else if ("ERROR".equals(s)) {
				return ERROR;
			} else {
				throw new Exception("错误的状态类型：" + s);
			}
		}
	}

	public enum Type {
		CONTINUOUS("持续录制"), MANUAL("手动启停"), SCHEDULE("定时录制"), CUSTOM("自定义");

		private String name;

		private Type(String name) {
			this.name = name;
		}

		public String getName() {
			return this.name;
		}

		public static Type fromName(String name) throws Exception {
			if ("持续录制".equals(name)) {
				return CONTINUOUS;
			} else if ("手动启停".equals(name)) {
				return MANUAL;
			} else if ("定时录制".equals(name)) {
				return SCHEDULE;
			} else if ("自定义".equals(name)) {
				return CUSTOM;
			} else {
				throw new Exception("错误的状态类型：" + name);
			}
		}

		public static Type fromString(String s) throws Exception {
			if ("CONTINUOUS".equals(s)) {
				return CONTINUOUS;
			} else if ("MANUAL".equals(s)) {
				return MANUAL;
			} else if ("SCHEDULE".equals(s)) {
				return SCHEDULE;
			} else if ("CUSTOM".equals(s)) {
				return CUSTOM;
			} else {
				throw new Exception("错误的状态类型：" + s);
			}
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public String getSourceId() {
		return sourceId;
	}

	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}

	public String getSourceType() {
		return sourceType;
	}

	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}

	public String getSourceUrl() {
		return sourceUrl;
	}

	public void setSourceUrl(String sourceUrl) {
		this.sourceUrl = sourceUrl;
	}

	public Integer getProgramId() {
		return programId;
	}

	public void setProgramId(Integer programId) {
		this.programId = programId;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getRecordTimeSlotJson() {
		return recordTimeSlotJson;
	}

	public void setRecordTimeSlotJson(String recordTimeSlotJson) {
		this.recordTimeSlotJson = recordTimeSlotJson;
	}

	public String getLoopCycles() {
		return loopCycles;
	}

	public void setLoopCycles(String loopCycles) {
		this.loopCycles = loopCycles;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getCreater() {
		return creater;
	}

	public void setCreater(String creater) {
		this.creater = creater;
	}

	public String getUpdater() {
		return updater;
	}

	public void setUpdater(String updater) {
		this.updater = updater;
	}

}
