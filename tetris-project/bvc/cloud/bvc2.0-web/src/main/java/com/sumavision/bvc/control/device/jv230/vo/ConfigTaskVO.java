package com.sumavision.bvc.control.device.jv230.vo;

import java.util.ArrayList;
import java.util.List;

import com.sumavision.bvc.device.jv230.po.ConfigTaskPO;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class ConfigTaskVO extends AbstractBaseVO<ConfigTaskVO, ConfigTaskPO>{

	private Long id;
	
	//uuid，站点间设备唯一识别码
	private String uuid;
	
	//任务内容
	private String content;
	
	private String type;
	
	//轮询时间（轮询时才会用）
	private String time;
	
	//任务位置
	private List <ConfigLocationVo> configLocationList = new ArrayList<ConfigLocationVo>();

	public Long getId() {
		return id;
	}

	public ConfigTaskVO setId(Long id) {
		this.id = id;
		return this;
	}

	public String getUuid() {
		return uuid;
	}

	public ConfigTaskVO setUuid(String uuid) {
		this.uuid = uuid;
		return this;
	}

	public String getContent() {
		return content;
	}

	public ConfigTaskVO setContent(String content) {
		this.content = content;
		return this;
	}

	public String getType() {
		return type;
	}

	public ConfigTaskVO setType(String type) {
		this.type = type;
		return this;
	}

	public String getTime() {
		return time;
	}

	public ConfigTaskVO setTime(String time) {
		this.time = time;
		return this;
	}

	public List<ConfigLocationVo> getConfigLocationList() {
		return configLocationList;
	}

	public ConfigTaskVO setConfigLocationList(List<ConfigLocationVo> configLocationList) {
		this.configLocationList = configLocationList;
		return this;
	}

	@Override
	public ConfigTaskVO set(ConfigTaskPO entity) throws Exception {
		this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setType(entity.getType())
			.setTime(entity.getTime())
			.setContent(new String(entity.getContent()));
		return this;
	}
}
