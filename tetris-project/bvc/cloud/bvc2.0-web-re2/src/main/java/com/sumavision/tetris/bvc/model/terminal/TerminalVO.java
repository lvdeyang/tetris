package com.sumavision.tetris.bvc.model.terminal;

import java.util.List;

import com.sumavision.tetris.bvc.model.terminal.physical.screen.TerminalPhysicalScreenVO;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class TerminalVO extends AbstractBaseVO<TerminalVO, TerminalPO>{

	private String name;
	
	private String type;
	
	//private String singleBundleMode;
	
	private String deviceMode;
	
	private Boolean singleBundle;
	
	private String typeName;
	
	private Boolean physicalScreenLayout;
	
	private Integer columns;
	
	private Integer rows;
	
	private List<TerminalPhysicalScreenVO> physicalScreens;
	
	public String getName() {
		return name;
	}

	public TerminalVO setName(String name) {
		this.name = name;
		return this;
	}

	public String getType() {
		return type;
	}

	public TerminalVO setType(String type) {
		this.type = type;
		return this;
	}

	/*public String getSingleBundleMode() {
		return singleBundleMode;
	}

	public TerminalVO setSingleBundleMode(String singleBundleMode) {
		this.singleBundleMode = singleBundleMode;
		return this;
	}*/

	public String getDeviceMode() {
		return deviceMode;
	}

	public TerminalVO setDeviceMode(String deviceMode) {
		this.deviceMode = deviceMode;
		return this;
	}

	public Boolean getSingleBundle() {
		return singleBundle;
	}

	public TerminalVO setSingleBundle(Boolean singleBundle) {
		this.singleBundle = singleBundle;
		return this;
	}

	public String getTypeName() {
		return typeName;
	}

	public TerminalVO setTypeName(String typeName) {
		this.typeName = typeName;
		return this;
	}

	public Boolean getPhysicalScreenLayout() {
		return physicalScreenLayout;
	}

	public TerminalVO setPhysicalScreenLayout(Boolean physicalScreenLayout) {
		this.physicalScreenLayout = physicalScreenLayout;
		return this;
	}
	
	public Integer getColumns() {
		return columns;
	}

	public TerminalVO setColumns(Integer columns) {
		this.columns = columns;
		return this;
	}

	public Integer getRows() {
		return rows;
	}

	public TerminalVO setRows(Integer rows) {
		this.rows = rows;
		return this;
	}

	public List<TerminalPhysicalScreenVO> getPhysicalScreens() {
		return physicalScreens;
	}

	public TerminalVO setPhysicalScreens(List<TerminalPhysicalScreenVO> physicalScreens) {
		this.physicalScreens = physicalScreens;
		return this;
	}

	@Override
	public TerminalVO set(TerminalPO entity) throws Exception {
		this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setName(entity.getName())
			.setType(entity.getType()==null?"":entity.getType().toString())
			.setDeviceMode(entity.getType()==null?"":entity.getType().getDevicModel())
			.setSingleBundle(entity.getType()==null?null:entity.getType().getSingleBundle())
			.setTypeName(entity.getType()==null?"":entity.getType().getName())
			.setPhysicalScreenLayout(entity.getPhysicalScreenLayout()==null ? false:entity.getPhysicalScreenLayout())
			.setColumns(entity.getColumns())
			.setRows(entity.getRows());
		return this;
	}

}
