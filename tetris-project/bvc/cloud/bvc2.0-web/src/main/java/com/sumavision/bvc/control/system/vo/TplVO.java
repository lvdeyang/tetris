package com.sumavision.bvc.control.system.vo;

import java.util.List;

import com.sumavision.bvc.system.po.TplPO;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class TplVO extends AbstractBaseVO<TplVO, TplPO>{
	
	private String Name;

	//业务角色
	private	List<BusinessRoleVO>  roles;
	
	//屏幕布局
	private List<ScreenLayoutVO> layouts;
	
	//录制方案
	private List<RecordSchemeVO> records;
	
	//默认议程
	private List<AutoBuildAgendaVO> autoBuildAgendas;

	//默认议程id，逗号分隔，放在这里以便从TplPO拷贝过来
	private String autoBuildAgendaIds;
	
	public String getName() {
		return Name;
	}

	public TplVO setName(String name) {
		this.Name = name;
		return this;
	}

	public List<BusinessRoleVO> getRoles() {
		return roles;
	}

	public TplVO setRoles(List<BusinessRoleVO> roles) {
		this.roles = roles;
		return this;
	}

	public List<ScreenLayoutVO> getLayouts() {
		return layouts;
	}

	public TplVO setLayouts(List<ScreenLayoutVO> layouts) {
		this.layouts = layouts;
		return this;
	}

	public List<RecordSchemeVO> getRecords() {
		return records;
	}

	public TplVO setRecords(List<RecordSchemeVO> records) {
		this.records = records;
		return this;
	}

	public String getAutoBuildAgendaIds() {
		return autoBuildAgendaIds;
	}

	public TplVO setAutoBuildAgendaIds(String autoBuildAgendaIds) {
		this.autoBuildAgendaIds = autoBuildAgendaIds;
		return this;
	}

	public List<AutoBuildAgendaVO> getAutoBuildAgendas() {
		return autoBuildAgendas;
	}

	public TplVO setAutoBuildAgendas(List<AutoBuildAgendaVO> autoBuildAgendas) {
		this.autoBuildAgendas = autoBuildAgendas;
		return this;
	}

	@Override
	public TplVO set(TplPO entity) throws Exception {
		this.setId(entity.getId())
			.setName(entity.getName())
			.setAutoBuildAgendaIds(entity.getAutoBuildAgendaIds())
			.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setUuid(entity.getUuid());
		return this;
	}

}
