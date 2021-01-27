package com.sumavision.tetris.record.strategy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * 收录策略PO
 * 
 * 一个收录策略对应一个源信息，对应多个子任务 recordStrategyItemPO
 * 
 * 主要字段： 源信息（包括type url 可选节目号 及媒资id等） 任务类型 任务名称 创建人 任务状态
 * 
 * 
 * @author chenmo
 *
 */
@Entity
@Table(name = "record_strategy")
public class RecordStrategyPO extends AbstractBasePO {

	private static final long serialVersionUID = 1L;

	// 收录策略名称
	@Column(name = "name")
	private String name;

	// 策略类型
	@Enumerated(EnumType.STRING)
	@Column(name = "type")
	private EStrategyType type;

	// 策略执行状态
	@Enumerated(EnumType.STRING)
	@Column(name = "status")
	private EStrategyStatus status;

	// 源的id
	@Column(name = "sourceId")
	private String sourceId;

	// 源的封装格式，从媒资系统获取
	@Column(name = "sourceType")
	private String sourceType;

	// 源的Url，以后考虑从媒资系统获取
	@Column(name = "sourceUrl")
	private String sourceUrl;

	@Column(name = "sourceName")
	private String sourceName;

	// 源的program id，暂不涉及，备用
	@Column(name = "programId")
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

	@Column(name = "deviceId")
	private Long deviceId;

	@Column(name = "storageId")
	private Long storageId;

	@Column(name = "capacityTaskId")
	private String capacityTaskId;

	/**
	 * 策略删除的状态：0为正常，1标识策略假删除，假删除与真删除的区别就是是否特殊情况下可显示，用于删策略不删文件。yzx add on 20190409
	 */
	@Column(name = "delStatus")
	private Integer delStatus = 0;

	@Enumerated(EnumType.STRING)
	private EAutoInject autoInjectSel;
	
	@Enumerated(EnumType.STRING)
	private EAutoFFMpegTranscode autoFFMpegTranscode = EAutoFFMpegTranscode.MANUAL;

	public enum EStrategyStatus {
		NEW("新建"), RUNNING("执行中"), STOP("停止"), ERROR("流异常"), SUCCESS("已完成");// 添加异常状态，异常是运行态，但未录制，yzx add on 20190402

		private String name;

		private EStrategyStatus(String name) {
			this.name = name;
		}

		public String getName() {
			return this.name;
		}

		public static EStrategyStatus fromName(String name) throws Exception {
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

		public static EStrategyStatus fromString(String s) throws Exception {
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

	public enum EStrategyType {
		MANUAL("手动启停"), CYCLE_SCHEDULE("循环排期"), CUSTOM_SCHEDULE("自定义排期");

		private String name;

		private EStrategyType(String name) {
			this.name = name;
		}

		public String getName() {
			return this.name;
		}

		public static EStrategyType fromName(String name) throws Exception {
			if ("手动启停".equals(name)) {
				return MANUAL;
			} else if ("循环排期".equals(name)) {
				return CYCLE_SCHEDULE;
			} else if ("自定义排期".equals(name)) {
				return CUSTOM_SCHEDULE;
			} else {
				throw new Exception("错误的状态类型：" + name);
			}
		}

		public static EStrategyType fromString(String s) throws Exception {
			if ("MANUAL".equals(s)) {
				return MANUAL;
			} else if ("CYCLE_SCHEDULE".equals(s)) {
				return CYCLE_SCHEDULE;
			} else if ("CUSTOM_SCHEDULE".equals(s)) {
				return CUSTOM_SCHEDULE;
			} else {
				throw new Exception("错误的状态类型：" + s);
			}
		}
	}

	public enum EAutoInject {
		MANUAL("手动注入"), AUTO_INJECT_MIMS("自动注入媒资系统");

		private String name;

		private EAutoInject(String name) {
			this.name = name;
		}

		public String getName() {
			return this.name;
		}
	}
	
	public enum EAutoFFMpegTranscode {
		MANUAL("手动ff转码"), AUTO_FFMPEG_TRANSCODE("自动ff转码");

		private String name;

		private EAutoFFMpegTranscode(String name) {
			this.name = name;
		}

		public String getName() {
			return this.name;
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public EStrategyStatus getStatus() {
		return status;
	}

	public void setStatus(EStrategyStatus status) {
		this.status = status;
	}

	public EStrategyType getType() {
		return type;
	}

	public void setType(EStrategyType type) {
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

	public Long getStorageId() {
		return storageId;
	}

	public void setStorageId(Long storageId) {
		this.storageId = storageId;
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

	public Integer getProgramId() {
		return programId;
	}

	public void setProgramId(Integer programId) {
		this.programId = programId;
	}

	public int getDelStatus() {
		return delStatus;
	}

	public void setDelStatus(int delStatus) {
		this.delStatus = delStatus;
	}

	public Long getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(Long deviceId) {
		this.deviceId = deviceId;
	}

	public void setDelStatus(Integer delStatus) {
		this.delStatus = delStatus;
	}

	public String getCapacityTaskId() {
		return capacityTaskId;
	}

	public void setCapacityTaskId(String capacityTaskId) {
		this.capacityTaskId = capacityTaskId;
	}

	public String getSourceName() {
		return sourceName;
	}

	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}

	public EAutoInject getAutoInjectSel() {
		return autoInjectSel;
	}

	public void setAutoInjectSel(EAutoInject autoInjectSel) {
		this.autoInjectSel = autoInjectSel;
	}

	public EAutoFFMpegTranscode getAutoFFMpegTranscode() {
		return autoFFMpegTranscode;
	}

	public void setAutoFFMpegTranscode(EAutoFFMpegTranscode autoFFMpegTranscode) {
		this.autoFFMpegTranscode = autoFFMpegTranscode;
	}

}
