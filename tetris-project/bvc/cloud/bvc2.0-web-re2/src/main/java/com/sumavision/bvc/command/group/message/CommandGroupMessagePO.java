package com.sumavision.bvc.command.group.message;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Lob;
import javax.persistence.Table;

import com.sumavision.bvc.command.group.enumeration.MessageStatus;
import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * @ClassName: 指挥中的消息<br/>
 * @Description: <br/>
 * @author zsy 
 * @date 2019年11月18日 下午1:06:00
 */
@Entity
@Table(name="BVC_COMMAND_GROUP_MESSAGE")
public class CommandGroupMessagePO extends AbstractBasePO {

	private static final long serialVersionUID = 1L;
	
	/** 指挥id */
	private Long groupId;
	
	/** 成员的userId串，以“-”分隔 */	
	private String userIds;
	
	/** 消息状态 */
	private MessageStatus status;
	
	/** 通知内容 */
	private String content;
	
	/** 通知内容未占满屏幕时允许滚动 */
	private boolean forcedRolling;
	
	/** 样式，json串 */
	private String style;
	
	
//	/****** 样式 style ******/
//	
//	/** 字体 */
//	private String fontFamily;//宋体，黑体，楷体，仿宋，微软雅黑
//	
//	/** 字号 */
//	private String fontSize;//1~9
//
//	/** 字体样式 */
//	private String textDecoration;//常规，加粗，斜体，下划线
//
//	/** 颜色 */
//	private String color;//黑色，红色，黄色，绿色，白色，紫色，橙色，蓝色
//
//	/** 滚动速度 */
//	private String rollingSpeed;//最快，较快，正常，较慢，最慢
//
//	/** 滚动模式 */
//	private String rollingMode;//从右到左，从左到右
//
//	/** 滚动位置 */
//	private String rollingLocation;//左对齐，居中，右对齐
//
//	/** 滚动时间 */
//	private String rollingTime;//'10'
//
//	/** 滚动时间有无限制 */
//	private boolean rollingTimeUnlimited;//false 为true时忽略rollingTime	
	
	@Column(name = "CONTENT")
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Column(name = "FORCED_ROLLING")
	public boolean isForcedRolling() {
		return forcedRolling;
	}

	public void setForcedRolling(boolean forcedRolling) {
		this.forcedRolling = forcedRolling;
	}

	@Column(name = "GROUP_ID")
	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	@Column(name = "USER_IDS")
	public String getUserIds() {
		return userIds;
	}

	public void setUserIds(String userIds) {
		this.userIds = userIds;
	}

	@Lob
	@Column(name = "STYLE", columnDefinition="TEXT")
	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	@Enumerated(value = EnumType.STRING)
	@Column(name = "STATUS")
	public MessageStatus getStatus() {
		return status;
	}

	public void setStatus(MessageStatus status) {
		this.status = status;
	}

//	@Column(name = "FONT_FAMILY")
//	public String getFontFamily() {
//		return fontFamily;
//	}
//
//	public void setFontFamily(String fontFamily) {
//		this.fontFamily = fontFamily;
//	}
//
//	@Column(name = "FONT_SIZE")
//	public String getFontSize() {
//		return fontSize;
//	}
//
//	public void setFontSize(String fontSize) {
//		this.fontSize = fontSize;
//	}
//
//	@Column(name = "TEXT_DECORATION")
//	public String getTextDecoration() {
//		return textDecoration;
//	}
//
//	public void setTextDecoration(String textDecoration) {
//		this.textDecoration = textDecoration;
//	}
//
//	@Column(name = "COLOR")
//	public String getColor() {
//		return color;
//	}
//
//	public void setColor(String color) {
//		this.color = color;
//	}
//
//	@Column(name = "ROLLING_SPEED")
//	public String getRollingSpeed() {
//		return rollingSpeed;
//	}
//
//	public void setRollingSpeed(String rollingSpeed) {
//		this.rollingSpeed = rollingSpeed;
//	}
//
//	@Column(name = "ROLLING_MODE")
//	public String getRollingMode() {
//		return rollingMode;
//	}
//
//	public void setRollingMode(String rollingMode) {
//		this.rollingMode = rollingMode;
//	}
//
//	@Column(name = "ROLLING_LOCATION")
//	public String getRollingLocation() {
//		return rollingLocation;
//	}
//
//	public void setRollingLocation(String rollingLocation) {
//		this.rollingLocation = rollingLocation;
//	}
//
//	@Column(name = "ROLLING_TIME")
//	public String getRollingTime() {
//		return rollingTime;
//	}
//
//	public void setRollingTime(String rollingTime) {
//		this.rollingTime = rollingTime;
//	}
//
//	@Column(name = "ROLLING_TIME_UNLIMITED")
//	public boolean isRollingTimeUnlimited() {
//		return rollingTimeUnlimited;
//	}
//
//	public void setRollingTimeUnlimited(boolean rollingTimeUnlimited) {
//		this.rollingTimeUnlimited = rollingTimeUnlimited;
//	}
	
	public CommandGroupMessagePO(){}
	
	public CommandGroupMessagePO(
			Long groupId,
			String userIds,
			MessageStatus status,
			String content,
			boolean forcedRolling,
			String style){
		this.groupId = groupId;
		this.userIds = userIds;
		this.status = status;
		this.content = content;
		this.forcedRolling = forcedRolling;
		this.style = style;
	}
	
}
