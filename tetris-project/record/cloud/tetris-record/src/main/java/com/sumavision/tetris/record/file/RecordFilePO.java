package com.sumavision.tetris.record.file;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import org.springframework.beans.factory.annotation.Value;

import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * 
 * 具体的收录端，从存储上来说，一个recordFile代表一个媒资（或者说是一个HLS切片子目录） 主要针对
 * 持续或者手动开停的录制策略，在超过能力单目录最长录制时间后（一般是以24小时为界限）
 * 
 * @author chenmo
 *
 */
@Entity
@Table(name = "record_file")
public class RecordFilePO extends AbstractBasePO {

	private static final long serialVersionUID = 1L;

	// 所属录制子任务Id
	@Column(name = "recordStrategyItemId")
	private Long recordStrategyItemId;

	// 所属录制任务Id，不必要，但是方便查询
	@Column(name = "recordStrategyId")
	private Long recordStrategyId;

	// 所属录制任务Id，不必要，但是方便查询
	@Column(name = "storageId")
	private Long storageId;

	// 分段开始时间
	@Column(name = "startTime")
	private Date startTime;

	// 分段结束时间
	@Column(name = "stopTime")
	private Date stopTime;

	@Column(name = "filePath")
	private String filePath;

	@Column(name = "vodPath")
	private String vodPath;

	// 录制状态,0,未录制，1：正在录制，2，录制完成，3：录制失败，4：暂停
	@Enumerated(EnumType.STRING)
	@Column(name = "status")
	private ERecordFileStatus status;

	@Enumerated(EnumType.STRING)
	@Column(name = "injectStatus")
	private EInjectStatus injectStatus;

	@Enumerated(EnumType.STRING)
	@Column(name = "ffMpegTransStatus")
	private EFFMpegTransStatus ffMpegTransStatus;

	@Value("${realIP}")
	private String centerIP;

	public enum ERecordFileStatus {
		RECORD_WAIT("未录制"), RECORD_RUN("正在录制"), RECORD_SUC("录制完成");

		private String name;

		private ERecordFileStatus(String name) {
			this.name = name;
		}

		public String getName() {
			return this.name;
		}
	}

	public enum EInjectStatus {
		INJECT_WAIT("待注入"), INJECT_SUC("已注入"), INJECT_ERROR("注入失败");

		private String name;

		private EInjectStatus(String name) {
			this.name = name;
		}

		public String getName() {
			return this.name;
		}
	}

	public enum EFFMpegTransStatus {
		TRANS_WAIT("待注入"), TRANS_SUCCESS("已注入"), TRANS_ERROR("注入失败");

		private String name;

		private EFFMpegTransStatus(String name) {
			this.name = name;
		}

		public String getName() {
			return this.name;
		}
	}

	public Long getRecordStrategyItemId() {
		return recordStrategyItemId;
	}

	public void setRecordStrategyItemId(Long recordStrategyItemId) {
		this.recordStrategyItemId = recordStrategyItemId;
	}

	public Long getRecordStrategyId() {
		return recordStrategyId;
	}

	public void setRecordStrategyId(Long recordStrategyId) {
		this.recordStrategyId = recordStrategyId;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getStopTime() {
		return stopTime;
	}

	public void setStopTime(Date stopTime) {
		this.stopTime = stopTime;
	}

	public ERecordFileStatus getStatus() {
		return status;
	}

	public void setStatus(ERecordFileStatus status) {
		this.status = status;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getVodPath() {
		return vodPath;
	}

	public void setVodPath(String vodPath) {
		this.vodPath = vodPath;
	}

	public Long getStorageId() {
		return storageId;
	}

	public void setStorageId(Long storageId) {
		this.storageId = storageId;
	}

	public EInjectStatus getInjectStatus() {
		return injectStatus;
	}

	public void setInjectStatus(EInjectStatus injectStatus) {
		this.injectStatus = injectStatus;
	}

	public EFFMpegTransStatus getFfMpegTransStatus() {
		return ffMpegTransStatus;
	}

	public void setFfMpegTransStatus(EFFMpegTransStatus ffMpegTransStatus) {
		this.ffMpegTransStatus = ffMpegTransStatus;
	}

}
