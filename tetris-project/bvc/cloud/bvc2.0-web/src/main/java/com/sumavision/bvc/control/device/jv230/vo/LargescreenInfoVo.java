package com.sumavision.bvc.control.device.jv230.vo;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sumavision.bvc.device.jv230.po.CombineJv230PO;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class LargescreenInfoVo extends AbstractBaseVO<LargescreenInfoVo, CombineJv230PO>{

	private Long id;

	//uuid，站点间设备唯一识别码
	private String uuid;
	
	//大屏名称
	private String	name;
	
	//行数
	private Long rows;
	
	//列数
	private Long columns;
	
	//所在大屏信令
	private String signalUuid;
	
	//物理屏（能力号）
	private List<PhyscreenVo> physcreens = new ArrayList<PhyscreenVo>();

	public Long getId() {
		return id;
	}

	public LargescreenInfoVo setId(Long id) {
		this.id = id;
		return this;
	}

	public String getUuid() {
		return uuid;
	}

	public LargescreenInfoVo setUuid(String uuid) {
		this.uuid = uuid;
		return this;
	}

	public String getName() {
		return name;
	}

	public LargescreenInfoVo setName(String name) {
		this.name = name;
		return this;
	}

	public Long getRows() {
		return rows;
	}

	public LargescreenInfoVo setRows(Long rows) {
		this.rows = rows;
		return this;
	}

	public Long getColumns() {
		return columns;
	}

	public LargescreenInfoVo setColumns(Long columns) {
		this.columns = columns;
		return this;
	}

	public List<PhyscreenVo> getPhyscreens() {
		return physcreens;
	}

	public LargescreenInfoVo setPhyscreens(List<PhyscreenVo> physcreens) {
		this.physcreens = physcreens;
		return this;
	}

	public String getSignalUuid() {
		return signalUuid;
	}

	public LargescreenInfoVo setSignalUuid(String signalUuid) {
		this.signalUuid = signalUuid;
		return this;
	}

	@Override
	public LargescreenInfoVo set(CombineJv230PO entity) throws Exception {
		this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setName(entity.getName());
		
		if(entity.getWebsiteDraw() != null){
			JSONObject scopWebsiteDraw = JSON.parseObject(entity.getWebsiteDraw());
			this.setColumns(Long.valueOf(scopWebsiteDraw.getJSONObject("basic").getString("column")))
				.setRows(Long.valueOf(scopWebsiteDraw.getJSONObject("basic").getString("row")));
		}
		
		return this;
	}
}
