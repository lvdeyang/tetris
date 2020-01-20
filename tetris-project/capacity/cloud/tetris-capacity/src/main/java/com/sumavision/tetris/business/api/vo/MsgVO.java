package com.sumavision.tetris.business.api.vo;

/**
 * 告警服务<br/>
 * <b>作者:</b>sm<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年1月14日 上午10:18:59
 */
public class MsgVO {

	private String msg_id;
	
	private AlarmVO alarm;

	public String getMsg_id() {
		return msg_id;
	}

	public void setMsg_id(String msg_id) {
		this.msg_id = msg_id;
	}

	public AlarmVO getAlarm() {
		return alarm;
	}

	public void setAlarm(AlarmVO alarm) {
		this.alarm = alarm;
	}
	
}
