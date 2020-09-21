package com.sumavision.bvc.device.group.bo;

import com.suma.venus.resource.pojo.BundlePO;
import com.sumavision.bvc.device.group.po.DeviceGroupMemberChannelPO;
import com.sumavision.bvc.resource.dto.ChannelSchemeDTO;

/**
 * @ClassName: ConnectBO 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author zy
 * @date 2018年8月7日 上午11:14:03 
 */
public class ConnectBO {
	/**
	 * 任务Id
	 */
	private String taskId = "";
	
	/**
	 * 锁类型 write/read 
	 */
	private String lock_type = "";
	
	/**
	 * 接入层Id
	 */
	private String layerId = "";
	/**
	 * 设备ID
	 */
	private String bundleId = "";
	
	/**
	 * 设备能力通道ID
	 */
	private String channelId = "";
	
	/**
	 * 设备通道状态：Open/Close
	 */
	private String channel_status = "";
	
	/** VenusVideoIn VenusVideoOut VenusAudioIn VenusAudioOut */
	private String base_type = "";
	
	/**
	 * 单播/组播multicast模式
	 */
	private String mode = "single";
	
	/**
	 * 组播地址
	 */
	private String multi_addr;
	
	/**
	 * 组播源地址
	 */
	private String src_multi_addr;
	
	/**
	 * 编解码参数
	 */
	private CodecParamBO codec_param = new CodecParamBO();

	private ForwardSetSrcBO source_param;
	
	/** 字幕信息 */
	private OsdWrapperBO osds;
	
	public String getTaskId() {
		return taskId;
	}

	public ConnectBO setTaskId(String taskId) {
		this.taskId = taskId;
		return this;
	}

	public String getLock_type() {
		return lock_type;
	}

	public ConnectBO setLock_type(String lock_type) {
		this.lock_type = lock_type;
		return this;
	}

	public String getLayerId() {
		return layerId;
	}

	public ConnectBO setLayerId(String layerId) {
		this.layerId = layerId;
		return this;
	}

	public String getBundleId() {
		return bundleId;
	}

	public ConnectBO setBundleId(String bundleId) {
		this.bundleId = bundleId;
		return this;
	}

	public String getChannelId() {
		return channelId;
	}

	public ConnectBO setChannelId(String channelId) {
		this.channelId = channelId;
		return this;
	}

	public String getChannel_status() {
		return channel_status;
	}

	public ConnectBO setChannel_status(String channel_status) {
		this.channel_status = channel_status;
		return this;
	}

	public String getBase_type() {
		return base_type;
	}

	public ConnectBO setBase_type(String base_type) {
		this.base_type = base_type;
		return this;
	}

	public String getMode() {
		return mode;
	}

	public ConnectBO setMode(String mode) {
		this.mode = mode;
		return this;
	}

	public String getMulti_addr() {
		return multi_addr;
	}

	public ConnectBO setMulti_addr(String multi_addr) {
		this.multi_addr = multi_addr;
		return this;
	}

	public String getSrc_multi_addr() {
		return src_multi_addr;
	}

	public ConnectBO setSrc_multi_addr(String src_multi_addr) {
		this.src_multi_addr = src_multi_addr;
		return this;
	}

	public CodecParamBO getCodec_param() {
		return codec_param;
	}

	public ConnectBO setCodec_param(CodecParamBO codec_param) {
		this.codec_param = codec_param;
		return this;
	}
	
	public ForwardSetSrcBO getSource_param() {
		return source_param;
	}

	public ConnectBO setSource_param(ForwardSetSrcBO source_param) {
		this.source_param = source_param;
		return this;
	}

	public OsdWrapperBO getOsds() {
		return osds;
	}

	public ConnectBO setOsds(OsdWrapperBO osds) {
		this.osds = osds;
		return this;
	}

	/**
	 * @Title: 生成通道占用打开协议 
	 * @param channel 业务层通道数据
	 * @param codec 参数模板
	 * @return ConnectBO
	 */
	public ConnectBO set(DeviceGroupMemberChannelPO channel, CodecParamBO codec){
		this.setLock_type("write")
		    .setLayerId(channel.getMember().getLayerId())
		    .setBase_type(channel.getChannelType())
		    .setBundleId(channel.getBundleId())
		    .setChannelId(channel.getChannelId())
		    .setChannel_status("Open")
		    .setCodec_param(codec);
		return this;
	}
	
	/**
	 * 生成通道占用打开协议 <br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月16日 下午2:47:56
	 * @param channel
	 * @param forwardSrc 描述源信息，如果为null则表示无源，不需要设置源
	 * @param bundle 提供接入层id
	 * @param codec
	 * @return
	 */
	public ConnectBO set(ChannelSchemeDTO channel, ForwardSetSrcBO forwardSrc, BundlePO bundle, CodecParamBO codec){
		this.setLock_type("write")
		    .setLayerId(bundle.getAccessNodeUid())
		    .setBase_type(channel.getBaseType())
		    .setBundleId(channel.getBundleId())
		    .setChannelId(channel.getChannelId())
		    .setChannel_status("Open")
		    .setCodec_param(codec);
		if(forwardSrc != null){
			this.setSource_param(forwardSrc);
		}
		return this;
	}
	
}
