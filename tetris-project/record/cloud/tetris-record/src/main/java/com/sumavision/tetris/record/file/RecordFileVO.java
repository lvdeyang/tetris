package com.sumavision.tetris.record.file;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.fasterxml.jackson.annotation.JsonFormat;

public class RecordFileVO {

	private Long id;

	// private Long recordStrategyItemId;

	private Long recordStrategyId;
	
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")	
	private Date startTime;
	
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Date stopTime;

	private String filePath;

	private String status;

	public static RecordFileVO fromPO(RecordFilePO po) {
		if (po == null) {
			return null;
		}
		
		RecordFileVO vo = new RecordFileVO();
		BeanUtils.copyProperties(po, vo);
		vo.setStatus(po.getStatus().getName());
		return vo;

	}

	public static List<RecordFileVO> fromPOList(List<RecordFilePO> pos) {

		List<RecordFileVO> vos = new ArrayList<RecordFileVO>();

		for (RecordFilePO po : pos) {
			vos.add(fromPO(po));
		}

		return vos;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
