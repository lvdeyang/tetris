package com.sumavision.bvc.device.monitor.subtitle;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

@Entity
@Table(name = "BVC_MONITOR_SUBTITLE")
public class MonitorSubtitlePO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;

	/** 字幕名称 */
	private String name;
	
	/** 字幕内容 */
	private String content;
	
	/** 字体默认是黑体 */
	private MonitorSubtitleFont font;
	
	/** 字体高度，单位：像素，取值0-100 */
	private Integer height = 20;
	
	/** 16进制颜色 没有# */
	private String color = "ffffff";
	
	/** 创建字幕的用户 */
	private String userId;
	
	/** 创建用户名 */
	private String username;

	@Column(name = "NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Lob 
	@Basic(fetch = FetchType.EAGER) 
	@Column(name = "CONTENT", columnDefinition = "text")
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Enumerated(value = EnumType.STRING)
	@Column(name = "FONT")
	public MonitorSubtitleFont getFont() {
		return font;
	}

	public void setFont(MonitorSubtitleFont font) {
		this.font = font;
	}

	@Column(name = "HEIGHT")
	public Integer getHeight() {
		return height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	@Column(name = "COLOR")
	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	@Column(name = "USER_ID")
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Column(name = "USERNAME")
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
}
