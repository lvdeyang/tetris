package com.sumavision.tetris.agent.vo;

public class PassByVO {

	private String identify_id;
	
	private String operate;
	
	private String local_user_no;
	
	private String local_user_identify;
	
	private String remote_user_no;
	
	private String remote_meeting_no;
	
	private PassByParamVO param;

	public String getIdentify_id() {
		return identify_id;
	}

	public PassByVO setIdentify_id(String identify_id) {
		this.identify_id = identify_id;
		return this;
	}

	public String getOperate() {
		return operate;
	}

	public PassByVO setOperate(String operate) {
		this.operate = operate;
		return this;
	}

	public String getLocal_user_no() {
		return local_user_no;
	}

	public PassByVO setLocal_user_no(String local_user_no) {
		this.local_user_no = local_user_no;
		return this;
	}

	public String getLocal_user_identify() {
		return local_user_identify;
	}

	public PassByVO setLocal_user_identify(String local_user_identify) {
		this.local_user_identify = local_user_identify;
		return this;
	}

	public String getRemote_user_no() {
		return remote_user_no;
	}

	public PassByVO setRemote_user_no(String remote_user_no) {
		this.remote_user_no = remote_user_no;
		return this;
	}

	public String getRemote_meeting_no() {
		return remote_meeting_no;
	}

	public PassByVO setRemote_meeting_no(String remote_meeting_no) {
		this.remote_meeting_no = remote_meeting_no;
		return this;
	}

	public PassByParamVO getParam() {
		return param;
	}

	public PassByVO setParam(PassByParamVO param) {
		this.param = param;
		return this;
	}
}
