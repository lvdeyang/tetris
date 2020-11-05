package com.sumavision.bvc.device.group.bo;

import java.util.ArrayList;
import java.util.List;
/**
 * 
* @ClassName: ForwardSetSRCBO 
* @Description: TODO(这里用一句话描述这个类的作用) 
* @author zy
* @date 2018年8月7日 上午11:14:49 
*
 */
public class ForwardSetSrcBO {
	
	/**
	 * 源的类型 channel/combineVideo/combineAudio/mediaPush
	 */
	private String type = "";
	
	/**
	 * 接入层Id
	 */
	private String layerId = "";
	
	/**
	 * 设备标识		
	 */
	private String bundleId = "";
	
	/**
	 * 设备能力通道ID
	 */
	private String channelId = "";
	
	/**
	 * 源的UUID，当类型为combineVideo/combineAudio并且是新建合屏/混音时使用
	 */
	private String uuid = "";
	
	private List<PositionSrcBO> position = new ArrayList<PositionSrcBO>();

	public String getType() {
		return type;
	}

	public ForwardSetSrcBO setType(String type) {
		this.type = type;
		return this;
	}	

	public String getLayerId() {
		return layerId;
	}

	public ForwardSetSrcBO setLayerId(String layerId) {
		this.layerId = layerId;
		return this;
	}

	public String getBundleId() {
		return bundleId;
	}

	public ForwardSetSrcBO setBundleId(String bundleId) {
		this.bundleId = bundleId;
		return this;
	}

	public String getChannelId() {
		return channelId;
	}

	public ForwardSetSrcBO setChannelId(String channelId) {
		this.channelId = channelId;
		return this;
	}

	public String getUuid() {
		return uuid;
	}

	public ForwardSetSrcBO setUuid(String uuid) {
		this.uuid = uuid;
		return this;
	}

	public List<PositionSrcBO> getPosition() {
		return position;
	}

	public ForwardSetSrcBO setPosition(List<PositionSrcBO> position) {
		this.position = position;
		return this;
	}
	
	
}
