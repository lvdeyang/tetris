package com.sumavision.bvc.device.command.system;

import java.util.List;
import com.sumavision.bvc.command.group.basic.CommandGroupMemberPO;
import com.sumavision.bvc.command.group.enumeration.UserCallType;
import com.sumavision.bvc.command.group.enumeration.VodType;
import com.sumavision.bvc.command.group.forward.CommandGroupForwardDemandPO;
import com.sumavision.bvc.command.group.forward.CommandGroupForwardPO;
import com.sumavision.bvc.command.group.user.UserLiveCallPO;
import com.sumavision.bvc.command.group.vod.CommandVodPO;
import com.sumavision.bvc.device.command.common.CommandCommonUtil;
import com.sumavision.tetris.commons.context.SpringContext;
import com.sumavision.tetris.commons.util.date.DateUtil;

import lombok.Getter;
import lombok.Setter;

/**
 * 
* @ClassName: ForwardReturnBO 
* @Description: 描述会议系统中所有的业务转发，包括会议、会议转发、点播、通话
* @author zsy
* @date 2019年12月24日 下午1:14:15 
*
 */
@Getter
@Setter
public class AllForwardBO {

	private String id;
	
	private String time = "";
	
	private String businessType = "";
	
	
	/** 源类型：设备、用户 */
	private String srcType = "";
	
	/** 用于显示的源名称，用户名或设备名 */
	private String srcInfo = "";
	
	private String srcBundleId = "";
	
	private String srcBundleName = "";
	
	private String srcVideoChannelId = "";
	
	private String srcAudioChannelId = "";
	
	
	/** 目的类型：设备、用户 */
	private String dstType = "";
	
	private String dstInstitutionInfo = "";
	
	/** 用于显示的目的名称，用户名或设备名 */
	private String dstInfo = "";
	
	private String dstBundleId = "";
	
	private String dstBundleName = "";
	
	private String dstVideoChannelId = "";
	
	private String dstAudioChannelId = "";
	
	
	private String status = "";
	
	private String srcVideoBitrate = "-";
	
	private String srcAudioBitrate = "-";
	
	/** 视频码率，xxxbps */
	private String dstVideoBitrate = "-";
	
	/** 音频码率，xxxbps */
	private String dstAudioBitrate = "-";
	
	/** 点播设备 */
	public AllForwardBO setByDevice(CommandGroupForwardDemandPO demand){
		this.id = demand.getId().toString();
		this.time = DateUtil.format(demand.getUpdateTime(), DateUtil.dateTimePattern);
		this.businessType = demand.getDemandType().getName();
		this.srcType = demand.getDemandType().getCode();//设备、文件
		this.srcInfo = demand.getVideoBundleName();
		this.srcBundleId = demand.getVideoBundleId();
		this.srcBundleName = demand.getVideoBundleName();
		this.srcVideoChannelId = demand.getVideoChannelId();
		this.srcAudioChannelId = demand.getAudioChannelId();
		this.dstType = "用户";
		this.dstInstitutionInfo = "";
		this.dstInfo = demand.getDstUserName();
		this.dstBundleId = demand.getDstVideoBundleId();
		this.dstBundleName = demand.getDstVideoBundleName();
		this.dstVideoChannelId = demand.getDstVideoChannelId();
		this.dstAudioChannelId = demand.getDstAudioChannelId();
		return this;
	}
	
	/** 点播文件（不需要） */
	@Deprecated
	public AllForwardBO setByFile(CommandGroupForwardDemandPO demand){
		this.id = demand.getId().toString();
		this.time = DateUtil.format(demand.getUpdateTime(), DateUtil.dateTimePattern);
		this.businessType = demand.getDemandType().getName();
		this.srcType = demand.getDemandType().getCode();
		this.srcInfo = demand.getResourceName();
		this.dstType = "用户";
		this.dstInstitutionInfo = "";
		this.dstInfo = demand.getDstUserName();
		return this;
	}
	
	/** 会议中的转发，即观看 */
	public AllForwardBO setByForward(CommandGroupForwardPO forward){
		
		CommandCommonUtil commandCommonUtil = SpringContext.getBean(CommandCommonUtil.class);
		List<CommandGroupMemberPO> members = forward.getGroup().getMembers();
		CommandGroupMemberPO srcMember = commandCommonUtil.queryMemberById(members, forward.getSrcMemberId());
		CommandGroupMemberPO dstMember = commandCommonUtil.queryMemberById(members, forward.getDstMemberId());
		
		this.id = forward.getId().toString();
		this.time = DateUtil.format(forward.getUpdateTime(), DateUtil.dateTimePattern);
		this.businessType = "多人业务";
		this.srcType = "用户";
		this.srcInfo = srcMember==null?forward.getVideoBundleName():srcMember.getUserName();
		this.srcBundleId = forward.getVideoBundleId();
		this.srcBundleName = forward.getVideoBundleName();
		this.srcVideoChannelId = forward.getVideoChannelId();
		this.srcAudioChannelId = forward.getAudioChannelId();
		this.dstType = "用户";
		this.dstInstitutionInfo = "";
		this.dstInfo = dstMember==null?forward.getDstVideoBundleName():dstMember.getUserName();
		this.dstBundleId = forward.getDstVideoBundleId();
		this.dstBundleName = forward.getDstVideoBundleName();
		this.dstVideoChannelId = forward.getDstVideoChannelId();
		this.dstAudioChannelId = forward.getDstAudioChannelId();
		return this;
	}
	
	/** 点播业务 */
	public AllForwardBO setByVod(CommandVodPO vod){
		this.id = vod.getId().toString();
		this.time = DateUtil.format(vod.getUpdateTime(), DateUtil.dateTimePattern);
		VodType vodType = vod.getVodType();
		this.businessType = vodType.getCode();
		if(vodType.equals(VodType.DEVICE)){
			this.srcType = "设备";
			this.srcInfo = vod.getSourceBundleName();
		}else if(vodType.equals(VodType.USER)){
			this.srcType = "用户";
			this.srcInfo = vod.getSourceUserName();
		}else if(vodType.equals(VodType.USER_ONESELF)){
			this.srcType = "本地";
			this.srcInfo = vod.getSourceUserName();
		}
		
		this.srcBundleId = vod.getSourceBundleId();
		this.srcBundleName = vod.getSourceBundleName();
		this.srcVideoChannelId = vod.getSourceVideoChannelId();
		this.srcAudioChannelId = vod.getSourceAudioChannelId();
		this.dstType = "用户";
		this.dstInfo = vod.getDstUserName();
		this.dstInstitutionInfo = "";
		this.dstBundleId = vod.getDstBundleId();
		this.dstBundleName = vod.getDstBundleName();
		this.dstVideoChannelId = vod.getDstVideoChannelId();
		this.dstAudioChannelId = vod.getDstAudioChannelId();
		return this;
	}
	
	/** 呼叫业务被叫->主叫 */
	public AllForwardBO setByUserLiveCallPOCalledToCall(UserLiveCallPO call){
		this.id = call.getId().toString();
		this.time = DateUtil.format(call.getUpdateTime(), DateUtil.dateTimePattern);
		UserCallType callType = call.getType();
		if(callType.equals(UserCallType.CALL)){
			this.businessType = "视频通话";
		}else if(callType.equals(UserCallType.VOICE)){
			this.businessType = "语音对讲";
		}
		
		this.srcInfo = call.getCalledUsername();
		this.dstInfo = call.getCallUsername();		
		this.srcType = "用户";
		this.srcBundleId = call.getCalledEncoderBundleId();
		this.srcBundleName = call.getCalledEncoderBundleName();
		this.srcVideoChannelId = call.getCalledEncoderVideoChannelId();
		this.srcAudioChannelId = call.getCalledEncoderAudioChannelId();
		this.dstType = "用户";
		this.dstInstitutionInfo = "";
		this.dstBundleId = call.getCallDecoderBundleId();
		this.dstBundleName = call.getCallDecoderBundleName();
		this.dstVideoChannelId = call.getCallDecoderVideoChannelId();
		this.dstAudioChannelId = call.getCallDecoderAudioChannelId();
		return this;
	}
	
	/** 呼叫业务主叫->被叫 */
	public AllForwardBO setByUserLiveCallPOCallToCalled(UserLiveCallPO call){
		this.id = call.getId().toString();
		this.time = DateUtil.format(call.getUpdateTime(), DateUtil.dateTimePattern);
		UserCallType callType = call.getType();
		if(callType.equals(UserCallType.CALL)){
			this.businessType = "视频通话";
		}else if(callType.equals(UserCallType.VOICE)){
			this.businessType = "语音对讲";
		}
		
		this.srcInfo = call.getCallUsername();
		this.dstInfo = call.getCalledUsername();		
		this.srcType = "用户";
		this.srcBundleId = call.getCallEncoderBundleId();
		this.srcBundleName = call.getCallEncoderBundleName();
		this.srcVideoChannelId = call.getCallEncoderVideoChannelId();
		this.srcAudioChannelId = call.getCallEncoderAudioChannelId();
		this.dstType = "用户";
		this.dstInstitutionInfo = "";
		this.dstBundleId = call.getCalledDecoderBundleId();
		this.dstBundleName = call.getCalledDecoderBundleName();
		this.dstVideoChannelId = call.getCalledDecoderVideoChannelId();
		this.dstAudioChannelId = call.getCalledDecoderAudioChannelId();
		return this;
	}

}
