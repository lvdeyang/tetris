package com.sumavision.bvc.system.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * @ClassName: 通道别名 
 * @author lvdeyang
 * @date 2018年7月24日 下午3:43:40 
 */
@Entity
@Table(name="BVC_SYSTEM_CHANNEL_NAME")
public class ChannelNamePO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;

	/** 别名 */
	private String name;
	
	/** 通道名称 */
	private String channelName;
	
	/** 通道类型 */
	private String channelType;
	
	@Column(name = "NAME")
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
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
	
}
