package com.sumavision.tetris.agent.vo;

import java.util.List;

/**
 * response内容体<br/>
 * <b>作者:</b>wjw<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年3月9日 下午2:01:14
 */
public class ResponseResourceVO {

	/** 唯一标识 */
	private String identify_id;
	
	/** 操作 */
	private String operate;

	private String local_user_no;
	
	private List<ResourceVO> response_users;

	public String getIdentify_id() {
		return identify_id;
	}

	public ResponseResourceVO setIdentify_id(String identify_id) {
		this.identify_id = identify_id;
		return this;
	}

	public String getOperate() {
		return operate;
	}

	public ResponseResourceVO setOperate(String operate) {
		this.operate = operate;
		return this;
	}

	public String getLocal_user_no() {
		return local_user_no;
	}

	public ResponseResourceVO setLocal_user_no(String local_user_no) {
		this.local_user_no = local_user_no;
		return this;
	}

	public List<ResourceVO> getResponse_users() {
		return response_users;
	}

	public ResponseResourceVO setResponse_users(List<ResourceVO> response_users) {
		this.response_users = response_users;
		return this;
	}
}
