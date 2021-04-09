package com.sumavision.bvc.BO;

import java.util.List;

import com.sumavision.bvc.BO.ForwardSetBO.DstBO;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @ClassName:  BusinessOperateBO   
 * @Description:业务层传下来的业务操作对象  
 * @author: 
 * @date:   2018年7月13日 下午3:48:19   
 *     
 * @Copyright: 2018 Sumavision. All rights reserved. 
 * 注意：本内容仅限于北京数码视讯科技股份有限公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Getter
@Setter
public class BusinessOperateBO {
	
	private Long userId;
	
	private boolean mustLockAllBundle = true;
	
//	private String systemMode;
	
	private List<IncomingCallParam> incoming_call_request;
	
	private List<HangupParam> hang_up_request;
	
	private List<ConnectBO> connect;
	
	private List<ConnectBundleBO> connectBundle;
	
	private List<DisconnectBO> disconnect;
	
    private List<DisconnectBundleBO> disconnectBundle;
	
    private List<ForwardSetBO> forwardSet;
	
	private List<ForwardDelBo> forwardDel;
	
	private List<CombineVideoOperateBO> combineVideoSet;
	
	private List<CombineVideoOperateBO> combineVideoUpdate;
	
	private CombineVideoDelBO combineVideoDel;
	
	private List<CombineAudioOperateBO> combineAudioSet;
	
	private List<CombineAudioOperateBO> combineAudioUpdate;
	
	private CombineAudioDelBO combineAudioDel;
	
	private List<DstBO> lock;
	
	private List<DstBO> unlock;
	
	private List<DstBO> lockBundle;

	private List<DstBO> unlockBundle;
	
	private List<MediaMuxOutBO> OutConnMediaMuxSet;
	
	private List<MediaMuxOutBO> OutConnMediaMuxUpdate;
	
	private List<DstBO> OutConnMediaMuxDel;
	
	private List<MediaPushOperateBO> mediaPushSet;
	
//	private List<MediaPushBO> MediaPushUpdate;
	
	private List<DstBO> mediaPushDel;
	
	private List<MediaMuxOutBO> jv230ForwardSet;
	
	private List<DstBO> jv230ForwardDel;
	
	private List<MediaMuxOutBO> jv230AudioSet;
	
	private List<DstBO> jv230AudioDel;
	
	private List<PassByBO> pass_by;
	
	public Long getAdminUserId(){
		return -1L;
	}
		
}
