package com.sumavision.bvc.device.group.bo;

import com.alibaba.fastjson.JSONObject;

/**
 * 业务主动发起联网双向呼叫<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年5月21日 下午3:23:17
 */
public class VenusSeeRelationPassByContentBO implements BasePassByContent{

	private String cmd = "venus_see_relation";
	
	/** 为通话生成确定的uuid */
	private String uuid;
	
	/** play/call 单向/双向 */
	private String type;
	
	/** 系统内部用户 */
	private JSONObject venus_user;
	
	/** 联网内部用户—ldap上获取的对端用户 */
	private JSONObject relation_user;
	
	/** start—开启，stop—停止 */
	private String operate;

	/** 编码参数设置 */
	private CodecParamBO vparam;

	public String getCmd() {
		return cmd;
	}

	public VenusSeeRelationPassByContentBO setCmd(String cmd) {
		this.cmd = cmd;
		return this;
	}
	
	public String getUuid() {
		return uuid;
	}

	public VenusSeeRelationPassByContentBO setUuid(String uuid) {
		this.uuid = uuid;
		return this;
	}

	public String getType() {
		return type;
	}

	public VenusSeeRelationPassByContentBO setType(String type) {
		this.type = type;
		return this;
	}

	public JSONObject getVenus_user() {
		return venus_user;
	}

	public VenusSeeRelationPassByContentBO setVenus_user(JSONObject venus_user) {
		this.venus_user = venus_user;
		return this;
	}

	public JSONObject getRelation_user() {
		return relation_user;
	}

	public VenusSeeRelationPassByContentBO setRelation_user(JSONObject relation_user) {
		this.relation_user = relation_user;
		return this;
	}

	public String getOperate() {
		return operate;
	}

	public VenusSeeRelationPassByContentBO setOperate(String operate) {
		this.operate = operate;
		return this;
	}

	public CodecParamBO getVparam() {
		return vparam;
	}

	public VenusSeeRelationPassByContentBO setVparam(CodecParamBO vparam) {
		this.vparam = vparam;
		return this;
	}
	
}
