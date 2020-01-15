package com.sumavision.bvc.device.group.bo;

import com.sumavision.bvc.common.group.po.CommonCombineVideoSrcPO;
import com.sumavision.bvc.device.group.enumeration.CombineVideoSrcType;
import com.sumavision.bvc.device.group.enumeration.PollingSourceVisible;
import com.sumavision.bvc.device.group.po.CombineVideoSrcPO;

/**
 * @Title: 源协议数据 
 * @author lvdeyang
 * @date 2018年8月7日 上午11:14:55 
 */
public class SourceBO {
	
	private String type;
	
	private String layerId = "";
	
	private String bundleId = "";
	
	private String channelId = "";
	
	/** 该源是否可见，1/0 */
	private int visible = 1;
	
	private String uuid;
	
	public String getLayerId() {
		return layerId;
	}
	
	public SourceBO setLayerId(String layerId) {
		this.layerId = layerId;
		return this;
	}
	
	public String getBundleId() {
		return bundleId;
	}
	
	public SourceBO setBundleId(String bundleId) {
		this.bundleId = bundleId;
		return this;
	}
	
	public String getChannelId() {
		return channelId;
	}
	
	public SourceBO setChannelId(String channelId) {
		this.channelId = channelId;
		return this;
	}

	public int getVisible() {
		return visible;
	}

	public SourceBO setVisible(int visible) {
		this.visible = visible;
		return this;
	}
	
	public String getType() {
		return type;
	}

	public SourceBO setType(String type) {
		this.type = type;
		return this;
	}

	public String getUuid() {
		return uuid;
	}

	public SourceBO setUuid(String uuid) {
		this.uuid = uuid;
		return this;
	}

	/**
	 * @Title: 生成协议层视频源数据<br/> 
	 * @param src 业务层视频源数据
	 * @return SourceBO 
	 */
	public SourceBO set(CombineVideoSrcPO src){
		if(CombineVideoSrcType.CHANNEL.equals(src.getType())){
			if(src.getVisible() == null){
				src.setVisible(PollingSourceVisible.VISIBLE);
			}
			this.setType("channel")
				.setLayerId(src.getLayerId())
				.setBundleId(src.getBundleId())
				.setChannelId(src.getChannelId())
				.setVisible(src.getVisible().getProtocal());
		}else if(CombineVideoSrcType.VIRTUAL.equals(src.getType())){
			this.setType("combineVideo")
				.setUuid(src.getVirtualUuid());
		}

		return this;
	}
	public SourceBO set(CommonCombineVideoSrcPO src){
		if(CombineVideoSrcType.CHANNEL.equals(src.getType())){
			if(src.getVisible() == null){
				src.setVisible(PollingSourceVisible.VISIBLE);
			}
			this.setType("channel")
				.setLayerId(src.getLayerId())
				.setBundleId(src.getBundleId())
				.setChannelId(src.getChannelId())
				.setVisible(src.getVisible().getProtocal());
		}else if(CombineVideoSrcType.VIRTUAL.equals(src.getType())){
			this.setType("combineVideo")
				.setUuid(src.getVirtualUuid());
		}

		return this;
	}

}
