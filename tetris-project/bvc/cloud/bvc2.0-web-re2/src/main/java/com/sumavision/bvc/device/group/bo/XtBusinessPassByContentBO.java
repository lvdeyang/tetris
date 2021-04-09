package com.sumavision.bvc.device.group.bo;

import java.util.Map;

/**
 * xt业务透传命令<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年6月21日 上午10:01:58
 */
public class XtBusinessPassByContentBO {

	/** 本地点播xt编码器 */
	public static final String CMD_LOCAL_SEE_XT_ENCODER = "see_xt_encoder";
	
	/** xt点播本地编码器 */
	public static final String CMD_XT_SEE_LOCAL_ENCODER = "see_lc_encoder";
	
	/** 本地点播xt用户 */
	public static final String CMD_LOCAL_SEE_XT_USER = "see_xt_user";
	
	/** xt点播本地用户 */
	public static final String CMD_XT_SEE_LOCAL_USER = "see_lc_user";
	
	/** 本地呼叫xt用户 */
	public static final String CMD_LOCAL_CALL_XT_USER = "call_xt_user";
	
	/** xt呼叫本地用户 */
	public static final String CMD_XT_CALL_LOCAL_USER = "call_lc_user";
	
	/** 开始操作 */
	public static final String OPERATE_START = "start";
	
	/** 停止操作 */
	public static final String OPERATE_STOP = "stop";
	
	/** 呼叫任务类型 */
	private String cmd;
	
	/** 任务uuid */
	private String uuid;
	
	/** 发起方用户号码 */
	private String src_user;
	
	/** 
	 * see_lc_encoder, see_lc_user,call_lc_user,call_xt_user时，必须存在此节点 
	 * layerid:String
	 * bundleid:String
	 * video_channelid:String
	 * audio_channelid:String
	 */
	private Map<String, String> local_encoder;
	
	/** 
	 * see_xt_encoder, see_xt_user,call_xt_user,call_lc_user时，必须存在此节点 
	 * layerid:String
	 * bundleid:String
	 * video_channelid:String
	 * audio_channelid:String
	 */
	private Map<String, String> xt_encoder;
	
	/** 
	 * see_xt_encoder,see_lc_encoder:目标编码器号码
	 * see_xt_user,see_lc_user:目标用户号码
	 * call_xt_user,call_lc_user:目标用户号码
	 */
	private String dst_number;
	
	/** 操作类型 */
	private String operate;
	
	/** 参数模板 */
	private CodecParamBO vparam;

	public String getCmd() {
		return cmd;
	}

	public XtBusinessPassByContentBO setCmd(String cmd) {
		this.cmd = cmd;
		return this;
	}

	public String getUuid() {
		return uuid;
	}

	public XtBusinessPassByContentBO setUuid(String uuid) {
		this.uuid = uuid;
		return this;
	}

	public String getSrc_user() {
		return src_user;
	}

	public XtBusinessPassByContentBO setSrc_user(String src_user) {
		this.src_user = src_user;
		return this;
	}

	public Map<String, String> getLocal_encoder() {
		return local_encoder;
	}

	public XtBusinessPassByContentBO setLocal_encoder(Map<String, String> local_encoder) {
		this.local_encoder = local_encoder;
		return this;
	}

	public Map<String, String> getXt_encoder() {
		return xt_encoder;
	}

	public XtBusinessPassByContentBO setXt_encoder(Map<String, String> xt_encoder) {
		this.xt_encoder = xt_encoder;
		return this;
	}

	public String getDst_number() {
		return dst_number;
	}

	public XtBusinessPassByContentBO setDst_number(String dst_number) {
		this.dst_number = dst_number;
		return this;
	}

	public String getOperate() {
		return operate;
	}

	public XtBusinessPassByContentBO setOperate(String operate) {
		this.operate = operate;
		return this;
	}

	public CodecParamBO getVparam() {
		return vparam;
	}

	public XtBusinessPassByContentBO setVparam(CodecParamBO vparam) {
		this.vparam = vparam;
		return this;
	}
	
}
