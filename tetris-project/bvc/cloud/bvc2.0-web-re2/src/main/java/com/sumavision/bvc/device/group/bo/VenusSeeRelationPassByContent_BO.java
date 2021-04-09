package com.sumavision.bvc.device.group.bo;

/**
 * 业务主动发起联网双向呼叫<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年5月21日 下午3:23:17
 */
public class VenusSeeRelationPassByContent_BO implements BasePassByContent{

	private String cmd = "venus_see_relation";
	
	/** 系统内部用户 */
	private String venus_user;
	
	/** 联网内部用户—ldap上获取的对端用户 */
	private String relation_user;
	
	/** start—开启，stop—停止 */
	private String operate;

	/** 编码参数设置 */
	private CodecParamBO vparam;

	public String getCmd() {
		return cmd;
	}

	public VenusSeeRelationPassByContent_BO setCmd(String cmd) {
		this.cmd = cmd;
		return this;
	}

	public String getVenus_user() {
		return venus_user;
	}

	public VenusSeeRelationPassByContent_BO setVenus_user(String venus_user) {
		this.venus_user = venus_user;
		return this;
	}

	public String getRelation_user() {
		return relation_user;
	}

	public VenusSeeRelationPassByContent_BO setRelation_user(String relation_user) {
		this.relation_user = relation_user;
		return this;
	}

	public String getOperate() {
		return operate;
	}

	public VenusSeeRelationPassByContent_BO setOperate(String operate) {
		this.operate = operate;
		return this;
	}

	public CodecParamBO getVparam() {
		return vparam;
	}

	public VenusSeeRelationPassByContent_BO setVparam(CodecParamBO vparam) {
		this.vparam = vparam;
		return this;
	}
	
}
