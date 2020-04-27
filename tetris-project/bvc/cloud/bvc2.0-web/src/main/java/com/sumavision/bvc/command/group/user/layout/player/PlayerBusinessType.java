package com.sumavision.bvc.command.group.user.layout.player;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

/**
 * @ClassName: 转发的业务类型：协同，专向，各种点播，
 * @author zsy
 * @date 2019年9月23日 上午10:11:22 
 */
public enum PlayerBusinessType {
	
	//考虑跟ConfigType.java合并
	
	NONE("未使用", "none"),
	BASIC_COMMAND("普通会议业务", "command"),//主席用commandMember，其他成员用command
	CHAIRMAN_BASIC_COMMAND("主席的普通会议业务", "commandMember"),//主席用commandMember，其他成员用command
	COOPERATE_COMMAND("协同会议业务", "cooperation"),
//	COOPERATE_MEMBER_COMMAND("协同成员的业务", "cooperationMember"),//协同指挥成员观看其它成员的是时候使用，目前没有
	SPEAK_MEETING("会议发言业务", "speak"),
	SECRET_COMMAND("专向会议业务", "secret"),
//	DEMAND_FORWARD("会议转发点播", ""),//废弃
	COMMAND_FORWARD_DEVICE("会议转发设备", "commandForwardDevice"),
	COMMAND_FORWARD_FILE("会议转发文件", "commandForwardFile"),
	PLAY_COMMAND_RECORD("播放会议录像", "vodRecordFile"),
	PLAY_FILE("点播文件", "vodResourceFile"),
	PLAY_USER("点播用户", "vodUser"),
	PLAY_USER_ONESELF("本地视频", "vodUserLocal"),//看自己的编码器，如果vodUser，则按照普通点播来处理；2月20日新增localPreview用于观看摄像头；4月23日新增vodUserLocal专用于点播本人
	PLAY_DEVICE("点播设备", "vodDevice"),
	PLAY_RECORD("播放录像", "vodRecordFile"),
	USER_CALL("用户呼叫", "callUser"),
	USER_VOICE("语音对讲", "voiceIntercom");
	
	private String name;
	
	private String code;
	
	private PlayerBusinessType(String name, String code){
		this.name = name;
		this.code = code;
	}

	public String getName(){
		return this.name;
	}
	
	public String getCode() {
		return code;
	}

	/**
	 * @Title: 根据名称获取转发目的类型 
	 * @param name 名称
	 * @throws Exception 
	 * @return ForwardDstType 转发目的类型
	 */
	public static PlayerBusinessType fromName(String name) throws Exception{
		PlayerBusinessType[] values = PlayerBusinessType.values();
		for(PlayerBusinessType value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
	
}
