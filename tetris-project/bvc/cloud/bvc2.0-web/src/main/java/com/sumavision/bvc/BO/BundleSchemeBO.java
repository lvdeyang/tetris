package com.sumavision.bvc.BO;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sumavision.bvc.BO.ForwardSetBO.DstBO;
import com.sumavision.bvc.common.CommonConstant;
import com.sumavision.bvc.meeting.logic.ExecuteBusiness;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BundleSchemeBO {
	
	/**
	 * 独立的userId，transFromConnectBundleBO和transFromDisConnectBOs方法，会根据businessType，决定是否设为Admin的userId
	 */
	private Long userId;
	
	/**
	 * 任务Id
	 */
	private String taskId;
	
	/**
	 * 锁类型 write/read 
	 */
	private String lock_type;
	
	/**
	 * 业务类型 meeting/vod
	 */
	private String businessType;
	
	/**
	 * 操作类型 start/stop/other等。逻辑层对start和stop进行计数
	 */
	private String operateType;
	
	/**
	 * 合屏用到，通道UUID
	 */
	private String uuid;
	
	/**
	 * 对应bundle_type，如VenusTerminal
	 */
	private String base_type;
	
	/**
	 * 对应device_model，如jv210
	 */
	private String device_model;
	
	/**
	 * 接入层Id
	 */
	private String layerId;
	
	/**
	 * 所属bundle的ID
	 */
	private String bundleId;
	
	private List<ChannelSchemeBO> channels;
	
	private JSONArray screens;
	
	private JSONObject pass_by_str;

	public static BundleSchemeBO transFromConnectBundleBO(ConnectBundleBO connectBundleBO){
		if(connectBundleBO == null){
			return null;
		}
		
		BundleSchemeBO bundleScheme = new BundleSchemeBO();
		//如果BusinessType是vod（共享），那么使用Admin的userId
		if("vod".equals(connectBundleBO.getBusinessType())){
			bundleScheme.setUserId(ExecuteBusiness.adminUserId);
		}
		bundleScheme.setBusinessType(connectBundleBO.getBusinessType());
		bundleScheme.setOperateType(connectBundleBO.getOperateType());
		bundleScheme.setBase_type(connectBundleBO.getBundle_type());
		bundleScheme.setDevice_model(connectBundleBO.getDevice_model());
		bundleScheme.setBundleId(connectBundleBO.getBundleId());
		bundleScheme.setLayerId(connectBundleBO.getLayerId());
		bundleScheme.setLock_type(connectBundleBO.getLock_type());
		bundleScheme.setScreens(connectBundleBO.getScreens());
		bundleScheme.setTaskId(StringUtils.isEmpty(connectBundleBO.getTaskId())?UUID.randomUUID().toString():connectBundleBO.getTaskId());
		bundleScheme.setPass_by_str(connectBundleBO.getPass_by_str());
		return bundleScheme;
	}
	
	public static List<BundleSchemeBO> transFromDisConnectBOs(List<DisconnectBundleBO> disconnectBundleBOs){
		if(disconnectBundleBOs == null || disconnectBundleBOs.isEmpty()){
			return null;
		}
		
		List<BundleSchemeBO> bundles = new ArrayList<BundleSchemeBO>();
		for(DisconnectBundleBO disconnectBundle:disconnectBundleBOs){
			BundleSchemeBO bundle = new BundleSchemeBO();
			//如果BusinessType是vod（共享），那么使用Admin的userId
			if("vod".equals(disconnectBundle.getBusinessType())){
				bundle.setUserId(ExecuteBusiness.adminUserId);
			}
			bundle.setBusinessType(disconnectBundle.getBusinessType());
			bundle.setOperateType(disconnectBundle.getOperateType());
			bundle.setBundleId(disconnectBundle.getBundleId());
			bundle.setBase_type(disconnectBundle.getBundle_type());
			bundle.setDevice_model(disconnectBundle.getDevice_model());
			bundle.setLayerId(disconnectBundle.getLayerId());
			bundle.setLock_type(disconnectBundle.getLock_type());
			bundle.setTaskId(StringUtils.isEmpty(disconnectBundle.getTaskId())?UUID.randomUUID().toString():disconnectBundle.getTaskId());
			bundle.setPass_by_str(disconnectBundle.getPass_by_str());
			bundles.add(bundle);
		}
		return bundles;
	}
	//TODO:如果ForwardSet中也添加了businessType和operateType，那么这里也需要加
	public static List<BundleSchemeBO> transFromDst(List<DstBO> dstBOs){
		if(dstBOs == null || dstBOs.isEmpty()){
			return null;
		}
		List<BundleSchemeBO> bundles = new ArrayList<BundleSchemeBO>();
		for(DstBO dst:dstBOs){
			BundleSchemeBO bundle = new BundleSchemeBO();
			bundle.setBundleId(dst.getBundleId());
			bundle.setLayerId(dst.getLayerId());			
			bundle.setLock_type(dst.getLock_type()==null?CommonConstant.WRITE_LOCK:dst.getLock_type());
			bundle.setTaskId(StringUtils.isEmpty(dst.getTaskId())?UUID.randomUUID().toString():dst.getTaskId());
			bundles.add(bundle);
		}
		return bundles;
	}

	@Override
	public String toString() {
		return "BundleSchemeBO [taskId=" + taskId + ", lock_type=" + lock_type + ", uuid=" + uuid + ", base_type="
				+ base_type + ", layerId=" + layerId + ", bundleId=" + bundleId + ", channels=" + channels
				+ ", screens=" + screens + ", pass_by_str=" + pass_by_str + "]";
	}
	
	
}
