package com.sumavision.tetris.bvc.business;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

/**
 * @ClassName: 转发的业务类型：协同，专向，各种点播，
 * @author zsy
 * @date 2019年9月23日 上午10:11:22 
 */
public enum BusinessInfoType {
	
	NONE("未使用", "", "none", false),
	COMMON("默认", "", "common", true),
	BASIC_COMMAND("普通指挥业务", "指挥", "command", true),//主席用commandMember，其他成员用command
	BASIC_MEETING("普通会议业务", "会议", "command", true),//给议程使用
	MEETING_DISCUSS("会议讨论", "会议", "command", true),//给议程使用
	CHAIRMAN_BASIC_COMMAND("主席的普通会议业务", "", "commandMember", false),//主席用commandMember，其他成员用command
	COOPERATE_COMMAND("协同会议业务", "指挥", "cooperation", false),
//	COOPERATE_MEMBER_COMMAND("协同成员的业务", "cooperationMember"),//协同指挥成员观看其它成员的是时候使用
	SPEAK_MEETING("会议发言业务", "会议", "speak", false),
	SECRET_COMMAND("专向会议业务", "专向", "secret", false),
	COMMAND_FORWARD("会议转发", "媒体转发", "commandForward", true),
	COMMAND_FORWARD_DEVICE("会议转发设备", "媒体转发", "commandForwardDevice", true),
	COMMAND_FORWARD_FILE("会议转发文件", "媒体转发", "commandForwardFile", true),
	COMMAND_FORWARD_USER("会议转发用户", "媒体转发", "commandForwardUser", true),
	PLAY_COMMAND_RECORD("播放会议录像", "录像", "vodRecordFile", true),
	PLAY_VOD("点播", "点播", "vod", true),
	PLAY_FILE("点播文件", "点播", "vodResourceFile", true),
	PLAY_USER("点播用户", "点播", "vodUser", true),
	PLAY_USER_ONESELF("本地视频", "本地预览", "vodUserLocal", false),//看自己的编码器，如果vodUser，则按照普通点播来处理；2月20日新增localPreview用于观看摄像头；4月23日新增vodUserLocal专用于点播本人
	PLAY_DEVICE("点播设备", "点播", "vodDevice", true),
	PLAY_RECORD("播放录像", "录像", "vodRecordFile", true),
	USER_CALL("用户呼叫", "通话", "callUser", true),
	USER_VOICE("语音对讲", "语音", "voiceIntercom", true);
	
	private String name;
	
	 /** 用于展现在播放器上的业务类型信息 */
	private String info;
	
	private String code;
	
	private boolean show;
	
	private BusinessInfoType(String name, String info, String code, boolean show){
		this.name = name;
		this.info = info;
		this.code = code;
		this.setShow(show);
	}

	public String getName(){
		return this.name;
	}

	public String getInfo(){
		return this.info;
	}
	
	public String getCode() {
		return code;
	}
	
	public boolean isShow() {
		return show;
	}

	public void setShow(boolean show) {
		this.show = show;
	}

	public boolean isCommandOrMeeting(){
		switch(this){
		case BASIC_COMMAND:
		case CHAIRMAN_BASIC_COMMAND:
		case COOPERATE_COMMAND:
		case BASIC_MEETING:
		case SPEAK_MEETING:
		case COMMAND_FORWARD_DEVICE:
		case COMMAND_FORWARD_FILE:
		case COMMAND_FORWARD_USER:
			return true;
		default:
			return false;
		}
	}
	
	public boolean isPlayFile(){
		switch(this){
		case PLAY_COMMAND_RECORD:
		case PLAY_FILE:
		case PLAY_RECORD:
			return true;
		default:
			return false;
		}
	}

	/**
	 * @Title: 根据名称获取转发目的类型 
	 * @param name 名称
	 * @throws Exception 
	 * @return ForwardDstType 转发目的类型
	 */
	public static BusinessInfoType fromName(String name) throws Exception{
		BusinessInfoType[] values = BusinessInfoType.values();
		for(BusinessInfoType value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
	
	
	
}
