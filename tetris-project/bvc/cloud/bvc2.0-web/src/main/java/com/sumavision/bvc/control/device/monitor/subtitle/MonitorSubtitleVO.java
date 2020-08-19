package com.sumavision.bvc.control.device.monitor.subtitle;

import com.sumavision.bvc.device.monitor.subtitle.MonitorSubtitlePO;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class MonitorSubtitleVO extends AbstractBaseVO<MonitorSubtitleVO, MonitorSubtitlePO>{

	private String name;
	
	private String content;
	
	private String font;
	
	private String height;
	
	private String color;
	
	private String username;
	
	public String getName() {
		return name;
	}

	public MonitorSubtitleVO setName(String name) {
		this.name = name;
		return this;
	}

	public String getContent() {
		return content;
	}

	public MonitorSubtitleVO setContent(String content) {
		this.content = content;
		return this;
	}

	public String getFont() {
		return font;
	}

	public MonitorSubtitleVO setFont(String font) {
		this.font = font;
		return this;
	}

	public String getHeight() {
		return height;
	}

	public MonitorSubtitleVO setHeight(String height) {
		this.height = height;
		return this;
	}

	public String getColor() {
		return color;
	}

	public MonitorSubtitleVO setColor(String color) {
		this.color = color;
		return this;
	}

	public String getUsername() {
		return username;
	}

	public MonitorSubtitleVO setUsername(String username) {
		this.username = username;
		return this;
	}

	@Override
	public MonitorSubtitleVO set(MonitorSubtitlePO entity) throws Exception {
		this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setName(entity.getName())
			.setContent(entity.getContent())
			.setColor(entity.getColor())
			.setFont(entity.getFont().getName())
			.setHeight(new StringBufferWrapper().append(entity.getHeight()).append("px").toString())
			.setUsername(entity.getUsername());
		return this;
	}

}
