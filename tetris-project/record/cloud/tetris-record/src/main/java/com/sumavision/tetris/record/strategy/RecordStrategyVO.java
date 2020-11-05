package com.sumavision.tetris.record.strategy;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

public class RecordStrategyVO {

	private Long id;

	private String name;

	private String sourceId;

	private String sourceInfo;

	private String status;

	private String type;

	private String createTime;

	private Date updateTime;

	// private String strategyInfo;

	private String startDate;

	private String endDate;

	private String recordTimeSlotJson;

	private int delStatus;

	public int getDelStatus() {
		return delStatus;
	}

	public void setDelStatus(int delStatus) {
		this.delStatus = delStatus;
	}

	public static RecordStrategyVO fromPO(RecordStrategyPO po) {
		RecordStrategyVO vo = new RecordStrategyVO();
		vo.setId(po.getId());
		vo.setName(po.getName());
		vo.setSourceId(po.getSourceId());
		vo.setSourceInfo(po.getSourceType() + " : " + po.getSourceUrl());
		vo.setStatus(po.getStatus().getName());
		vo.setType(po.getType().getName());
		vo.setDelStatus(po.getDelStatus());
		vo.setUpdateTime(po.getUpdateTime());
		vo.setCreateTime(po.getCreateTime());
		vo.setStartDate(po.getStartDate());
		vo.setEndDate(po.getEndDate());
		return vo;
	}

	public static List<RecordStrategyVO> fromPOList(List<RecordStrategyPO> recordStrategyPOList) {

		List<RecordStrategyVO> VOList = new ArrayList<RecordStrategyVO>();

		for (RecordStrategyPO po : recordStrategyPOList) {
			VOList.add(fromPO(po));
		}

		return VOList;
	}

	/*
	 * @MethodName: toPO
	 * 
	 * @Description: TODO VO2PO 方法同PO2VO一样，多添加4个变量的set方法
	 * 
	 * @param 1
	 * 
	 * @Return: com.suma.xianrd.strip.pojo.record.RecordStrategyPO
	 * 
	 * @Author: Poemafar
	 * 
	 * @Date: 2019/3/18 16:47
	 **/
	/*
	 * public RecordStrategyPO toPO() { RecordStrategyPO po = new
	 * RecordStrategyPO(); po.setId(id); po.setName(name);
	 * po.setStartDate(startDate); po.setEndDate(endDate);
	 * po.setDelStatus(delStatus); try { po.setStatus(Status.fromString(status));
	 * po.setType(Type.fromString(type)); } catch (Exception e) {
	 * e.printStackTrace(); } return po; }
	 */

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSourceId() {
		return sourceId;
	}

	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}

	public String getSourceInfo() {
		return sourceInfo;
	}

	public void setSourceInfo(String sourceInfo) {
		this.sourceInfo = sourceInfo;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getRecordTimeSlotJson() {
		return recordTimeSlotJson;
	}

	public void setRecordTimeSlotJson(String recordTimeSlotJson) {
		this.recordTimeSlotJson = recordTimeSlotJson;
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

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

}
