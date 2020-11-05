package com.sumavision.bvc.device.jv230.po;

import java.util.Comparator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.sumavision.bvc.device.group.enumeration.ChannelType;
import com.sumavision.bvc.resource.dto.ChannelSchemeDTO;
import com.sumavision.tetris.orm.po.AbstractBasePO;

@Entity
@Table(name = "BVC_USER_RESOURCE_JV230_CHANNEL")
public class Jv230ChannelPO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;

	/** 转换后的通道类型 */
	private ChannelType type;
	
	/** 来自于资源管理的接入层id */
	private String layerId;
	
	/** 来自于资源管理的设备id */
	private String bundleId;
	
	/** 来自于资源管理的设备名称 */
	private String bundleName;
	
	/** 来自于资源管理的通道id */
	private String channelId;
	
	/** 来自于资源管理的通道名称 */
	private String channelName;
	
	/** 来自于资源管理的通道类型 */
	private String channelType;
	
	private boolean occupied;
	
	/** 自定义的序列号 */
	private Long serialNum;
	
	/** 关联jv230设备 */
	private Jv230PO jv230;

	@Enumerated(value = EnumType.STRING)
	@Column(name = "TYPE")
	public ChannelType getType() {
		return type;
	}

	public void setType(ChannelType type) {
		this.type = type;
	}
	
	@Column(name = "LAYER_ID")
	public String getLayerId() {
		return layerId;
	}

	public void setLayerId(String layerId) {
		this.layerId = layerId;
	}

	@Column(name = "BUNDLE_ID")
	public String getBundleId() {
		return bundleId;
	}

	public void setBundleId(String bundleId) {
		this.bundleId = bundleId;
	}

	@Column(name = "BUNDLE_NAME")
	public String getBundleName() {
		return bundleName;
	}

	public void setBundleName(String bundleName) {
		this.bundleName = bundleName;
	}

	@Column(name = "CHANNEL_ID")
	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	@Column(name = "CHANNEL_NAME")
	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	@Column(name = "CHANNEL_TYPE")
	public String getChannelType() {
		return channelType;
	}

	public void setChannelType(String channelType) {
		this.channelType = channelType;
	}

	public boolean isOccupied() {
		return occupied;
	}

	public void setOccupied(boolean occupied) {
		this.occupied = occupied;
	}

	@Column(name = "SERIAL_NUM")
	public Long getSerialNum() {
		return serialNum;
	}

	public void setSerialNum(Long serialNum) {
		this.serialNum = serialNum;
	}

	@ManyToOne
	@JoinColumn(name = "JV230_ID")
	public Jv230PO getJv230() {
		return jv230;
	}

	public void setJv230(Jv230PO jv230) {
		this.jv230 = jv230;
	}
	
	/**
	 * @ClassName: 通道排序器，channelId为 类型-编号， 按照编号从小到大排列<br/> 
	 * @author lvdeyang
	 * @date 2018年8月27日 上午8:36:10 
	 */
	public static final class ChannelComparatorFromChannelScheme implements Comparator<ChannelSchemeDTO>{
		public int compare(ChannelSchemeDTO o1, ChannelSchemeDTO o2) {
			
			long id1 = Long.parseLong(o1.getChannelId().split("_")[1]);
			long id2 = Long.parseLong(o2.getChannelId().split("_")[1]);
			
			if(id1 > id2){
				return 1;
			}
			if(id1 == id2){
				return 0;
			}
			return -1;
		}
	}
	
}
