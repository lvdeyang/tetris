package com.sumavision.bvc.BO;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.suma.venus.resource.base.bo.ChannelBody;
import com.suma.venus.resource.pojo.ChannelSchemePO;
import com.sumavision.bvc.BO.CombineVideoLayoutBO.DecodeLayoutBO;
import com.sumavision.bvc.BO.CombineVideoLayoutBO.LayoutPosBO;
import com.sumavision.bvc.BO.ForwardSetBO.DstBO;
import com.sumavision.bvc.BO.ForwardSetBO.SrcBO;
import com.sumavision.bvc.common.CommonConstant;
import com.sumavision.bvc.common.CommonConstant.ChannelTypeInfo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChannelSchemeBO {
	
	/**
	 * 任务Id
	 */
	private String taskId;
	
	/**
	 * 锁类型 write/read 
	 */
	private String lock_type;
	
	/**
	 * 合屏用到，通道UUID
	 */
	private String uuid;
	
	/**
	 * 接入层Id
	 */
	private String layerId;
	
	/**
	 * 所属bundle的ID
	 */
	private String bundleId;
	
	private String bundleType;
	
	/**
	 * 配置的通道标号
	 */
	private String channelId;
	
	private String channelName;
	
	/**
	 * 通道参数,有可能临时存放codecParam
	 */
	private JSONObject channelParam;
	
	/**
	 * 字幕信息
	 */
	private JSONObject osds;
	
	/**
	 * 合屏的解码通道用到，布局BO
	 */
	private DecodeLayoutBO position;
	
	/**
	 * 单播/组播模式
	 */
	private String mode;
	
	/**
	 * 组播地址
	 */
	private String multi_addr;
	
	/**
	 * 源
	 */
	private List<SrcBO> src;
	
	/**
	 * 合屏/混音删除用到，
	 */
	private ChannelTypeInfo combineType;
	
	private String baseType;
	
	private JSONArray screens;
	
	/**
	 * 合屏的编码通道需要的解码路数，仅合屏用
	 */
	private Integer decodeCntUse = 0;
	
	/**
	 * 通道状态，作为bundle操作时的channel状态
	 */
	private String channel_status;
	
	/**
	 * 存储当前合屏通道的所有src
	 */
	private JSONArray combineVideoSrc;
	public static ChannelSchemeBO fromPO(ChannelSchemePO po){
		ChannelSchemeBO bo= new ChannelSchemeBO();
		BeanUtils.copyProperties(po, bo);
		return bo;
	}		
	
	public static ChannelSchemeBO transFromChannelBody(ChannelBody channelBody){
		ChannelSchemeBO channelSchemeBO = new ChannelSchemeBO();
		channelSchemeBO.setBundleId(channelBody.getBundle_id());
		channelSchemeBO.setChannelId(channelBody.getChannel_id());
		channelSchemeBO.setChannelName(channelBody.getChannel_name());
		channelSchemeBO.setBaseType(channelBody.getBase_type());
		return channelSchemeBO;
	}
	
	
	public ChannelBody transToChannelBody(){
		ChannelBody channelBody = new ChannelBody();
		channelBody.setChannel_id(channelId);
		channelBody.setChannel_param(channelParam);
		return channelBody;
	}
	
	
	public void fillCombineVideoEncodeChannel(CombineVideoOperateBO layoutBO){
		if(!StringUtils.isEmpty(layoutBO.getBundleId())){
			this.setBundleId(layoutBO.getBundleId());			
		}
		if(!StringUtils.isEmpty(layoutBO.getChannelId())){			
			this.setChannelId(layoutBO.getChannelId());
		}
		if(!StringUtils.isEmpty(layoutBO.getLayerId())){
			this.setLayerId(layoutBO.getLayerId());
		}
		this.setTaskId(StringUtils.isEmpty(layoutBO.getTaskId())?UUID.randomUUID().toString():layoutBO.getTaskId());
		this.setUuid(layoutBO.getUuid());		
		this.setChannelParam(layoutBO.getCodec_param());
		this.setLock_type(layoutBO.getLock_type());
		this.setCombineType(ChannelTypeInfo.VIDEOENCODE);
		if(!StringUtils.isEmpty(layoutBO.getBase_type())){			
			this.setBaseType(layoutBO.getBase_type());
		}
	}
	
	public void fillCombineAudioEncodeChannel(CombineAudioOperateBO layoutBO){
		if(!StringUtils.isEmpty(layoutBO.getBundleId())){
			this.setBundleId(layoutBO.getBundleId());			
		}
		if(!StringUtils.isEmpty(layoutBO.getChannelId())){			
			this.setChannelId(layoutBO.getChannelId());
		}
		if(!StringUtils.isEmpty(layoutBO.getLayerId())){
			this.setLayerId(layoutBO.getLayerId());
		}
		this.setTaskId(StringUtils.isEmpty(layoutBO.getTaskId())?UUID.randomUUID().toString():layoutBO.getTaskId());
		this.setUuid(layoutBO.getUuid());		
		this.setChannelParam(layoutBO.getCodec_param());
		this.setLock_type(layoutBO.getLock_type());
		this.setCombineType(ChannelTypeInfo.AUDIOENCODE);
		if(!StringUtils.isEmpty(layoutBO.getBase_type())){			
			this.setBaseType(layoutBO.getBase_type());
		}
		
		this.setSrc(layoutBO.getSrc()==null?null:layoutBO.getSrc());
		
	}
	
	public void fillMediaMuxEncodeChannel(MediaMuxOutBO layoutBO){
		if(!StringUtils.isEmpty(layoutBO.getLayerId())){
			this.setLayerId(layoutBO.getLayerId());
		}
		if(!StringUtils.isEmpty(layoutBO.getChannelId())){			
			this.setChannelId(layoutBO.getChannelId());
		}
		if(!StringUtils.isEmpty(layoutBO.getBundleId())){
			this.setBundleId(layoutBO.getBundleId());
		}
		
		this.setTaskId(StringUtils.isEmpty(layoutBO.getTaskId())?UUID.randomUUID().toString():layoutBO.getTaskId());
		this.setUuid(layoutBO.getUuid());		
		this.setChannelParam(layoutBO.getChannel_param());
		this.setLock_type(CommonConstant.WRITE_LOCK);
		this.setCombineType(ChannelTypeInfo.MEDIAMUXENCODE);
		
	}
	
	public void fillMediaPushEncodeChannel(MediaPushOperateBO layoutBO){
		if(!StringUtils.isEmpty(layoutBO.getLayerId())){
			this.setLayerId(layoutBO.getLayerId());
		}
		if(!StringUtils.isEmpty(layoutBO.getChannelId())){			
			this.setChannelId(layoutBO.getChannelId());
		}
		if(!StringUtils.isEmpty(layoutBO.getBundleId())){
			this.setBundleId(layoutBO.getBundleId());
		}
		
		this.setTaskId(StringUtils.isEmpty(layoutBO.getTaskId())?UUID.randomUUID().toString():layoutBO.getTaskId());
		this.setUuid(layoutBO.getUuid());
		JSONObject channelParam = new JSONObject();
//		channelParam.put("base_param", new JSONObject());
		channelParam.put("base_param", layoutBO.getCodec_param());
		channelParam.getJSONObject("base_param").put("file_source", layoutBO.getFile_source());
		this.setChannelParam(channelParam);
		this.setLock_type(CommonConstant.WRITE_LOCK);
		this.setCombineType(ChannelTypeInfo.MEDIAPUSHENCODE);
		
	}
	
	public static void fillCombineVideoDecodeChannel(List<ChannelSchemeBO> decodeChannels, CombineVideoOperateBO videoSetBO){
		if(decodeChannels == null || videoSetBO == null || videoSetBO.getPosition() == null){
			return;
		}
		
		if(decodeChannels.size() != videoSetBO.getPosition().size()){
			return;
		}
		int i=0;
		for(LayoutPosBO pos:videoSetBO.getPosition()){
			ChannelSchemeBO decodeChannel = decodeChannels.get(i);
			i++;
//			decodeChannel.setTaskId(pos.getTaskId());
//			decodeChannel.setChannelParam(pos.getCodec_param());
//			decodeChannel.setLock_type(pos.getLock_type());
			decodeChannel.setUuid(pos.getUuid());
			decodeChannel.setPosition(DecodeLayoutBO.transFromLayoutBO(pos));
			decodeChannel.setSrc(pos.getSrc()==null?null:new ArrayList<>(pos.getSrc()));
			decodeChannel.setCombineType(ChannelTypeInfo.VIDEODECODE);
		}
	}
	
	public static ChannelSchemeBO transFromLayoutPosBO(LayoutPosBO posBO){
		ChannelSchemeBO channel = new ChannelSchemeBO();
		channel.setBundleId(posBO.getBundleId());
		channel.setChannelId(posBO.getChannelId());
		channel.setChannelParam(posBO.getCodec_param());
		channel.setLayerId(posBO.getLayerId());
		channel.setLock_type(posBO.getLock_type());
		channel.setPosition(DecodeLayoutBO.transFromLayoutBO(posBO));
		channel.setSrc(posBO.getSrc()==null?null:new ArrayList<>(posBO.getSrc()));
		channel.setTaskId(StringUtils.isEmpty(posBO.getTaskId())?UUID.randomUUID().toString():posBO.getTaskId());
		channel.setUuid(posBO.getUuid());
		channel.setCombineType(ChannelTypeInfo.VIDEODECODE);
		return channel;
	}
	
	public static List<ChannelSchemeBO> transFromDst(List<DstBO> dstBOs){
		if(dstBOs == null || dstBOs.isEmpty()){
			return null;
		}
		List<ChannelSchemeBO> channels = new ArrayList<ChannelSchemeBO>();
		for(DstBO dst:dstBOs){
			ChannelSchemeBO channel = new ChannelSchemeBO();
			channel.setBundleId(dst.getBundleId());
			channel.setChannelId(dst.getChannelId());
			channel.setLayerId(dst.getLayerId());
			channel.setLock_type(dst.getLock_type()==null?CommonConstant.WRITE_LOCK:dst.getLock_type());
			channel.setTaskId(StringUtils.isEmpty(dst.getTaskId())?UUID.randomUUID().toString():dst.getTaskId());
			channels.add(channel);
		}
		return channels;
	}
	
	public static List<ChannelSchemeBO> transFromConnectBOs(List<ConnectBO> connectBOs){
		if(connectBOs == null || connectBOs.isEmpty()){
			return null;
		}
		List<ChannelSchemeBO> channels = new ArrayList<ChannelSchemeBO>();
		for(ConnectBO connectBO:connectBOs){
			ChannelSchemeBO channel = new ChannelSchemeBO();
			channel.setBundleId(connectBO.getBundleId());
			channel.setChannelId(connectBO.getChannelId());
			channel.setChannelParam(connectBO.getCodec_param());
			channel.setLayerId(connectBO.getLayerId());
			channel.setLock_type(connectBO.getLock_type());
			channel.setTaskId(StringUtils.isEmpty(connectBO.getTaskId())?UUID.randomUUID().toString():connectBO.getTaskId());
			channel.setBaseType(connectBO.getBase_type());
			if(connectBO.getSource_param() != null){
				channel.setSrc(new ArrayList<ForwardSetBO.SrcBO>());
				channel.getSrc().add(connectBO.getSource_param());
			}
			channel.setScreens(connectBO.getScreens());
			channel.setOsds(connectBO.getOsds());
			channels.add(channel);
		}
		return channels;
	}
	
	public static List<ChannelSchemeBO> transFromDisConnectBOs(List<DisconnectBO> disconnectBOs){
		if(disconnectBOs == null || disconnectBOs.isEmpty()){
			return null;
		}
		List<ChannelSchemeBO> channels = new ArrayList<ChannelSchemeBO>();
		for(DisconnectBO connectBO:disconnectBOs){
			ChannelSchemeBO channel = new ChannelSchemeBO();
			channel.setBundleId(connectBO.getBundleId());
			channel.setChannelId(connectBO.getChannelId());
			channel.setLayerId(connectBO.getLayerId());
			channel.setLock_type(connectBO.getLock_type());
			channel.setTaskId(StringUtils.isEmpty(connectBO.getTaskId())?UUID.randomUUID().toString():connectBO.getTaskId());
			channel.setBaseType(connectBO.getBase_type());
			channel.setChannelParam(connectBO.getCodec_param());
			channel.setOsds(connectBO.getOsds());
			channels.add(channel);
		}
		return channels;
	}
	
	public static List<ChannelSchemeBO> transFromForwardSetBOs(List<ForwardSetBO> forwardSetBOs){
		if(forwardSetBOs == null || forwardSetBOs.isEmpty()){
			return null;
		}
		List<ChannelSchemeBO> channels = new ArrayList<ChannelSchemeBO>();
		for(ForwardSetBO forwardSetBO:forwardSetBOs){
			ChannelSchemeBO channel = new ChannelSchemeBO();
			channel.setBundleId(forwardSetBO.getDst().getBundleId());
			channel.setChannelId(forwardSetBO.getDst().getChannelId());
			
			channel.setChannelParam(forwardSetBO.getDst().getCodec_param());
			channel.setLayerId(forwardSetBO.getDst().getLayerId());
			//todo 转发没有locktype
			channel.setLock_type(null);
			channel.setBaseType(forwardSetBO.getDst().getBase_type());
			channel.setTaskId(StringUtils.isEmpty(forwardSetBO.getTaskId())?UUID.randomUUID().toString():forwardSetBO.getTaskId());
			if(forwardSetBO.getSrc() != null){
				channel.setSrc(new ArrayList<ForwardSetBO.SrcBO>());
				channel.getSrc().add(forwardSetBO.getSrc());
			}
			channel.setScreens(forwardSetBO.getScreens());
			channels.add(channel);
		}
		return channels;
	}
	
	public static List<ChannelSchemeBO> transFromForwardDelBOs(List<ForwardDelBo> forwardDelBOs){
		if(forwardDelBOs == null || forwardDelBOs.isEmpty()){
			return null;
		}
		List<ChannelSchemeBO> channels = new ArrayList<ChannelSchemeBO>();
		for(ForwardDelBo forwardDelBO:forwardDelBOs){
			ChannelSchemeBO channel = new ChannelSchemeBO();
			channel.setBundleId(forwardDelBO.getDst().getBundleId());
			channel.setChannelId(forwardDelBO.getDst().getChannelId());
			
			channel.setChannelParam(forwardDelBO.getDst().getCodec_param());
			channel.setBaseType(forwardDelBO.getDst().getBase_type());
			channel.setLayerId(forwardDelBO.getDst().getLayerId());
			//todo 转发暂时没有locktype
			channel.setLock_type(null);
			channel.setTaskId(StringUtils.isEmpty(forwardDelBO.getTaskId())?UUID.randomUUID().toString():forwardDelBO.getTaskId());			
			channels.add(channel);
		}
		return channels;
	}
	
	public static List<ChannelSchemeBO> transFromCombineVideoDelBOs(CombineVideoDelBO videoDelBO){
		if(videoDelBO == null || (videoDelBO.getEncode() == null && videoDelBO.getSrc() == null)){
			return null;
		}
		List<ChannelSchemeBO> channels = new ArrayList<ChannelSchemeBO>();
		/*for(SrcBO srcBO:videoDelBO.getSrc()){
			ChannelSchemeBO channel = new ChannelSchemeBO();
			channel.setBundleId(srcBO.getBundleId());
			channel.setChannelId(Integer.valueOf(srcBO.getChannelId()));
			channel.setLayerId(srcBO.getLayerId());
			//这块应该有锁类型
			channel.setLock_type(srcBO.getLock_type());
			channel.setTaskId(srcBO.getTaskId());
			channel.setCombineType(CombineType.VIDEODECODE);
			channels.add(channel);
		}*/
		if(videoDelBO.getEncode() != null){
			for(DstBO dstBO:videoDelBO.getEncode()){
				ChannelSchemeBO channel = new ChannelSchemeBO();
				channel.setBundleId(dstBO.getBundleId());
				channel.setChannelId(dstBO.getChannelId());
				channel.setLayerId(dstBO.getLayerId());
				channel.setLock_type(dstBO.getLock_type());
				channel.setTaskId(StringUtils.isEmpty(dstBO.getTaskId())?UUID.randomUUID().toString():dstBO.getTaskId());
				channel.setCombineType(ChannelTypeInfo.VIDEOENCODE);
				channels.add(channel);
			}
		}
		
		return channels;
	}
	
	public static List<ChannelSchemeBO> transFromCombineAudioDelBOs(CombineAudioDelBO audioDelBO){
		if(audioDelBO == null || audioDelBO.getEncode() == null){
			return null;
		}
		List<ChannelSchemeBO> channels = new ArrayList<ChannelSchemeBO>();
		
		if(audioDelBO.getEncode() != null){
			for(DstBO dstBO:audioDelBO.getEncode()){
				ChannelSchemeBO channel = new ChannelSchemeBO();
				channel.setBundleId(dstBO.getBundleId());
				channel.setChannelId(dstBO.getChannelId());
				channel.setLayerId(dstBO.getLayerId());
				channel.setLock_type(dstBO.getLock_type());
				channel.setTaskId(StringUtils.isEmpty(dstBO.getTaskId())?UUID.randomUUID().toString():dstBO.getTaskId());
				channel.setCombineType(ChannelTypeInfo.AUDIOENCODE);
				channels.add(channel);
			}
		}
		
		return channels;
	}
	
	public static List<ChannelSchemeBO> transFromBundleChannelBOs(List<Bundle_ChannelBO> bundle_ChannelBOs, String bundleId){
		if(bundle_ChannelBOs == null || bundle_ChannelBOs.isEmpty()){
			return null;
		}
		List<ChannelSchemeBO> channels = new ArrayList<ChannelSchemeBO>();
		for(Bundle_ChannelBO bundle_Channel:bundle_ChannelBOs){
			ChannelSchemeBO channel = new ChannelSchemeBO();
			channel.setChannelId(bundle_Channel.getChannelId());
			channel.setChannelParam(bundle_Channel.getCodec_param());
			channel.setBaseType(bundle_Channel.getBase_type());
			channel.setMode(bundle_Channel.getMode());
			channel.setMulti_addr(bundle_Channel.getMulti_addr());
			if(bundle_Channel.getSource_param() != null){
				channel.setSrc(new ArrayList<ForwardSetBO.SrcBO>());
				channel.getSrc().add(bundle_Channel.getSource_param());
			}
			channel.setChannel_status(bundle_Channel.getChannel_status());
			channel.setBundleId(bundleId);
			channel.setOsds(bundle_Channel.getOsds());
			channels.add(channel);
		}
		return channels;
	}

	@Override
	public String toString() {
		return "ChannelSchemeBO [taskId=" + taskId + ", lock_type=" + lock_type + ", layerId=" + layerId + ", bundleId="
				+ bundleId + ", channelId=" + channelId + ", channelName=" + channelName + ", channelParam="
				+ channelParam + ", position=" + position + ", src=" + src + "]";
	}
	
	
	
	
}
