package com.sumavision.bvc.control.device.jv230.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.sumavision.bvc.device.jv230.po.CombineJv230ConfigPO;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class CombineJv230ConfigVO extends AbstractBaseVO<CombineJv230ConfigVO, CombineJv230ConfigPO>{

	//大屏配置名称
	private String name;
	
	//上屏状态
	private String status;
	
	//备注
	private String remark;
	
	//创建时间
	private Date createTime;
	
	private Long combineJv230Id;
	
	//大屏配置任务
	private List <ConfigTaskVO> configTaskVoList = new ArrayList<ConfigTaskVO>(); 

	public String getName() {
		return name;
	}

	public CombineJv230ConfigVO setName(String name) {
		this.name = name;
		return this;
	}


	public String getStatus() {
		return status;
	}


	public CombineJv230ConfigVO setStatus(String status) {
		this.status = status;
		return this;
	}


	public String getRemark() {
		return remark;
	}


	public CombineJv230ConfigVO setRemark(String remark) {
		this.remark = remark;
		return this;
	}


	public Date getCreateTime() {
		return createTime;
	}


	public CombineJv230ConfigVO setCreateTime(Date createTime) {
		this.createTime = createTime;
		return this;
	}

	public Long getCombineJv230Id() {
		return combineJv230Id;
	}

	public CombineJv230ConfigVO setCombineJv230Id(Long combineJv230Id) {
		this.combineJv230Id = combineJv230Id;
		return this;
	}

	public List<ConfigTaskVO> getConfigTaskVoList() {
		return configTaskVoList;
	}

	public CombineJv230ConfigVO setConfigTaskVoList(List<ConfigTaskVO> configTaskVoList) {
		this.configTaskVoList = configTaskVoList;
		return this;
	}

	@Override
	public CombineJv230ConfigVO set(CombineJv230ConfigPO entity) throws Exception {
		this.setId(entity.getId())
			.setName(entity.getName())
			.setStatus(entity.getStatus())
			.setRemark(entity.getRemark())
			.setCreateTime(entity.getCreateTime())
			.setCombineJv230Id(entity.getCombineJv230().getId());
		
		return this;
	}

}
