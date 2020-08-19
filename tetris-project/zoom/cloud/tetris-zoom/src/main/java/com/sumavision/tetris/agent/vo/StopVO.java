package com.sumavision.tetris.agent.vo;

public class StopVO {

	private String operate;
	
	private String local_user_no;
	
	private String remote_meeting_no;
	
	private String local_user_identify;
	
	private String remote_channel_no;

	public String getOperate() {
		return operate;
	}

	public StopVO setOperate(String operate) {
		this.operate = operate;
		return this;
	}

	public String getLocal_user_no() {
		return local_user_no;
	}

	public StopVO setLocal_user_no(String local_user_no) {
		this.local_user_no = local_user_no;
		return this;
	}

	public String getRemote_meeting_no() {
		return remote_meeting_no;
	}

	public StopVO setRemote_meeting_no(String remote_meeting_no) {
		this.remote_meeting_no = remote_meeting_no;
		return this;
	}

	public String getLocal_user_identify() {
		return local_user_identify;
	}

	public StopVO setLocal_user_identify(String local_user_identify) {
		this.local_user_identify = local_user_identify;
		return this;
	}

	public String getRemote_channel_no() {
		return remote_channel_no;
	}

	public StopVO setRemote_channel_no(String remote_channel_no) {
		this.remote_channel_no = remote_channel_no;
		return this;
	}
	
}
