package com.sumavision.tetris.record.file;

import java.util.Date;

import javax.persistence.Column;

import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * 
 * 具体的收录端，从存储上来说，一个recordFile代表一个媒资（或者说是一个HLS切片子目录） 主要针对
 * 持续或者手动开停的录制策略，在超过能力单目录最长录制时间后（一般是以24小时为界限）
 * 
 * @author chenmo
 *
 */
public class RecordFilePO extends AbstractBasePO {

	private static final long serialVersionUID = 1L;

	// 所属录制子任务Id
	private Long recordTaskItemId;

	// 所属录制任务Id，不必要，但是方便查询
	private Long recordTaskId;

	// 分段开始时间
	@Column(name = "startTime")
	private Date startTime;

	// 分段结束时间
	@Column(name = "stopTime")
	private Date stopTime;

	// 分段文件路径（hls默认是）
	private String filePath;

	// 录制状态,0,未录制，1：正在录制，2，录制完成，3：录制失败，4：暂停
	@Column(name = "status")
	private Integer status;

	public Long getRecordTaskItemId() {
		return recordTaskItemId;
	}

	public void setRecordTaskItemId(Long recordTaskItemId) {
		this.recordTaskItemId = recordTaskItemId;
	}

	public Long getRecordTaskId() {
		return recordTaskId;
	}

	public void setRecordTaskId(Long recordTaskId) {
		this.recordTaskId = recordTaskId;
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

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

}
