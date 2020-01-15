package com.sumavision.bvc.command.group.message;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * @ClassName: 消息样式模板<br/>
 * @Description: <br/>
 * @author zsy 
 * @date 2019年11月18日 下午1:06:00
 */
@Entity
@Table(name="BVC_COMMAND_GROUP_MESSAGE_STYLE")
public class CommandGroupMessageStylePO extends AbstractBasePO {

	private static final long serialVersionUID = 1L;
	
	/** 创建者的userId */	
	private Long userId;
	
	
	/** 名称 */
	private String name;
	
	/** 字体 */
	private String fontFamily;//宋体，黑体，楷体，仿宋，微软雅黑
	
	/** 字号 */
	private String fontSize;//1~9

	/** 字体样式 */
	private String textDecoration;//常规，加粗，斜体，下划线

	/** 颜色 */
	private String color;//黑色，红色，黄色，绿色，白色，紫色，橙色，蓝色

	/** 滚动速度 */
	private String rollingSpeed;//最快，较快，正常，较慢，最慢

	/** 滚动模式 */
	private String rollingMode;//从右到左，从左到右

	/** 滚动位置 */
	private String rollingLocation;//左对齐，居中，右对齐

	/** 滚动时间 */
	private String rollingTime;//'10'

	/** 滚动时间有无限制 */
	private boolean rollingTimeUnlimited;//false 为true时忽略rollingTime
	
	@Column(name = "NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "FONT_FAMILY")
	public String getFontFamily() {
		return fontFamily;
	}

	public void setFontFamily(String fontFamily) {
		this.fontFamily = fontFamily;
	}

	@Column(name = "FONT_SIZE")
	public String getFontSize() {
		return fontSize;
	}

	public void setFontSize(String fontSize) {
		this.fontSize = fontSize;
	}

	@Column(name = "TEXT_DECORATION")
	public String getTextDecoration() {
		return textDecoration;
	}

	public void setTextDecoration(String textDecoration) {
		this.textDecoration = textDecoration;
	}

	@Column(name = "COLOR")
	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	@Column(name = "ROLLING_SPEED")
	public String getRollingSpeed() {
		return rollingSpeed;
	}

	public void setRollingSpeed(String rollingSpeed) {
		this.rollingSpeed = rollingSpeed;
	}

	@Column(name = "ROLLING_MODE")
	public String getRollingMode() {
		return rollingMode;
	}

	public void setRollingMode(String rollingMode) {
		this.rollingMode = rollingMode;
	}

	@Column(name = "ROLLING_LOCATION")
	public String getRollingLocation() {
		return rollingLocation;
	}

	public void setRollingLocation(String rollingLocation) {
		this.rollingLocation = rollingLocation;
	}

	@Column(name = "ROLLING_TIME")
	public String getRollingTime() {
		return rollingTime;
	}

	public void setRollingTime(String rollingTime) {
		this.rollingTime = rollingTime;
	}

	@Column(name = "ROLLING_TIME_UNLIMITED")
	public boolean isRollingTimeUnlimited() {
		return rollingTimeUnlimited;
	}

	public void setRollingTimeUnlimited(boolean rollingTimeUnlimited) {
		this.rollingTimeUnlimited = rollingTimeUnlimited;
	}

	@Column(name = "USER_ID")
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	public CommandGroupMessageStylePO(){}
	
	public CommandGroupMessageStylePO edit(
			Long userId,
			String name,
			String fontFamily,
		    String fontSize,
			String textDecoration,
			String color,
			String rollingSpeed,
			String rollingMode,
			String rollingLocation,
			String rollingTime,
			boolean rollingTimeUnlimited){

		this.userId = userId;
		this.name = name;
		this.fontFamily = fontFamily;
		this.fontSize = fontSize;
		this.textDecoration = textDecoration;
		this.color = color;
		this.rollingSpeed = rollingSpeed;
		this.rollingMode = rollingMode;
		this.rollingMode = rollingMode;
		this.rollingLocation = rollingLocation;
		this.rollingTime = rollingTime;
		this.rollingTimeUnlimited = rollingTimeUnlimited;
		
		return this;
		
	}
	
}
