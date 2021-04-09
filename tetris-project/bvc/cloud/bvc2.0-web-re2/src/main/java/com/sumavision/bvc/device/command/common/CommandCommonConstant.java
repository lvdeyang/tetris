package com.sumavision.bvc.device.command.common;

import org.springframework.beans.factory.annotation.Value;

/**
 * 指挥公用常量<br/>
 * <b>作者:</b>wjw<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年10月20日 下午12:10:35
 */
public class CommandCommonConstant {
	
	/**
	 * 用来设置
	 */
	@Value("${command.commandString}")
	public String commandString;
	
	/**
	 * 创建本地预览的模式，0关闭，1使用摄像头，2使用绑定的编码器
	 */
	@Value("${command.localPreviewMode}")
	public int localPreviewMode;
	
	/** 管理员用户名 */
	public static final String USER_NAME= "系统管理员";
	
	public String getCommandString() {
		return commandString;
	}

	public void setCommandString(String commandString) {
		this.commandString = commandString;
	}

	public int getLocalPreviewMode() {
		return localPreviewMode;
	}

	public void setLocalPreviewMode(int localPreviewMode) {
		this.localPreviewMode = localPreviewMode;
	}

	/** 呼叫消息 */
	public static final String MESSAGE_CALL = "callUser";
	
	/** 呼叫拒绝消息 */
	public static final String MESSAGE_CALL_REFUSE = "callUserRefuse";
	
	/** 呼叫停止消息 */
	public static final String MESSAGE_CALL_STOP = "callUserStop";
	
	/** 语音对讲消息 */
	public static final String MESSAGE_VOICE = "voiceIntercom";
	
	/** 语音对讲拒绝消息 */
	public static final String MESSAGE_VOICE_REFUSE = "voiceIntercomRefuse";
	
	/** 语音对讲停止消息 */
	public static final String MESSAGE_VOICE_STOP = "voiceIntercomStop";
	
}
