package com.sumavision.bvc.control.device.monitor.device;

import java.util.List;

import com.suma.venus.resource.base.bo.PlayerBundleBO;

/**
 * 网页sip播放器<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年4月26日 下午2:57:00
 */
public class WebSipPlayerVO {

	/** 号码 */
	private String code;
	
	/** 用户名 */
	private String username;
	
	/** 密码 */
	private String password;
	
	/** 客户端ip */
	private String ip;
	
	/** 播放器端口：从配置文件获取 */
	private String port;
	
	/** 设备id */
	private String bundleId;
	
	/** 设备类型，对应BundlePO的bundleType，如VenusTerminal */
	private String bundleType;
	
	/** 设备名称 */
	private String bundleName;
	
	/** 接入层id */
	private String layerId;
	
	/** 接入层注册ip */
	private String registerLayerIp;
	
	/** 接入层注册端口 */
	private String registerLayerPort;
	
	/** 视频通道id */
	private String videoChannelId;
	
	/** 视频通道类型 */
	private String videoBaseType;
	
	/** 音频通道id */
	private String audioChannelId;
	
	/** 音频通道类型 */
	private String audioBaseType;

	public String getCode() {
		return code;
	}

	public WebSipPlayerVO setCode(String code) {
		this.code = code;
		return this;
	}

	public String getUsername() {
		return username;
	}

	public WebSipPlayerVO setUsername(String username) {
		this.username = username;
		return this;
	}

	public String getPassword() {
		return password;
	}

	public WebSipPlayerVO setPassword(String password) {
		this.password = password;
		return this;
	}

	public String getIp() {
		return ip;
	}

	public WebSipPlayerVO setIp(String ip) {
		this.ip = ip;
		return this;
	}

	public String getPort() {
		return port;
	}

	public WebSipPlayerVO setPort(String port) {
		this.port = port;
		return this;
	}

	public String getBundleId() {
		return bundleId;
	}

	public WebSipPlayerVO setBundleId(String bundleId) {
		this.bundleId = bundleId;
		return this;
	}

	public String getBundleType() {
		return bundleType;
	}

	public WebSipPlayerVO setBundleType(String bundleType) {
		this.bundleType = bundleType;
		return this;
	}

	public String getBundleName() {
		return bundleName;
	}

	public WebSipPlayerVO setBundleName(String bundleName) {
		this.bundleName = bundleName;
		return this;
	}

	public String getLayerId() {
		return layerId;
	}

	public WebSipPlayerVO setLayerId(String layerId) {
		this.layerId = layerId;
		return this;
	}

	public String getRegisterLayerIp() {
		return registerLayerIp;
	}

	public WebSipPlayerVO setRegisterLayerIp(String registerLayerIp) {
		this.registerLayerIp = registerLayerIp;
		return this;
	}

	public String getRegisterLayerPort() {
		return registerLayerPort;
	}

	public WebSipPlayerVO setRegisterLayerPort(String registerLayerPort) {
		this.registerLayerPort = registerLayerPort;
		return this;
	}

	public String getVideoChannelId() {
		return videoChannelId;
	}

	public WebSipPlayerVO setVideoChannelId(String videoChannelId) {
		this.videoChannelId = videoChannelId;
		return this;
	}

	public String getVideoBaseType() {
		return videoBaseType;
	}

	public WebSipPlayerVO setVideoBaseType(String videoBaseType) {
		this.videoBaseType = videoBaseType;
		return this;
	}

	public String getAudioChannelId() {
		return audioChannelId;
	}

	public WebSipPlayerVO setAudioChannelId(String audioChannelId) {
		this.audioChannelId = audioChannelId;
		return this;
	}

	public String getAudioBaseType() {
		return audioBaseType;
	}

	public WebSipPlayerVO setAudioBaseType(String audioBaseType) {
		this.audioBaseType = audioBaseType;
		return this;
	}
	
	public WebSipPlayerVO set(PlayerBundleBO entity){
		this.setCode(entity.getUsername())
	        .setUsername(entity.getUsername())
	        .setPassword(entity.getPassword())
	        .setBundleId(entity.getBundleId())
	        .setBundleName(entity.getBundleName())
	        .setBundleType(entity.getBundleType())
	        .setLayerId(entity.getAccessLayerId())
	        .setRegisterLayerIp(entity.getAccessLayerIp()==null?"0.0.0.0":entity.getAccessLayerIp())
	        .setRegisterLayerPort(entity.getAccessLayerPort()==null?"0":entity.getAccessLayerPort().toString());
		
		List<String> channelIds = entity.getChannelIds();
		for(String channelId:channelIds){
			if(channelId.startsWith("VenusVideoOut")){
				this.setVideoChannelId(channelId)
		        	.setVideoBaseType("VenusVideoOut");
			}else if(channelId.startsWith("VenusAudioOut")){
				this.setAudioChannelId(channelId)
		        	.setAudioBaseType("VenusAudioOut");
			}
		}
		return this;
	}
	
}
