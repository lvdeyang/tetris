package com.sumavision.bvc.common.group.po;

import java.util.Comparator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.alibaba.fastjson.JSONObject;
import com.sumavision.bvc.device.group.enumeration.ChannelType;
import com.sumavision.bvc.resource.dto.ChannelSchemeDTO;
import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * @ClassName: 设备组成员通道
 * @author zy
 * @date 2018年7月31日 下午2:42:35 
 */
@Entity
@Table(name="BVC_COMMON_MEMBER_CHANNEL")
public class CommonMemberChannelPO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;
	
	/** 通道别名 */
	private String name;
	
	/** 业务层的通道类型，这个类型与资源层的通道类型一一对应 */
	private ChannelType type;
	
	/** 来自于资源层通道id */
	private String channelId;
	
	/** 来自于资源层通道名称 */
	private String channelName;
	
	/** 来自于资源层通道类型 */
	private String channelType;
	
	/** 来自于资源层设备id */
	private String bundleId;
	
	/** 来自于资源层设备名称 */
	private String bundleName;
	
	/** 来自于资源层设备类型 */
	private String bundleType;
	
	/** 来自资源层venus设备类型 */
	private String venusBundleType;
	
	/** 关联设备组成员 */
	private CommonMemberPO member;

	@Column(name = "NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Enumerated(value = EnumType.STRING)
	@Column(name = "TYPE")
	public ChannelType getType() {
		return type;
	}

	public void setType(ChannelType type) {
		this.type = type;
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
	
	@Column(name = "BUNDLE_TYPE")
	public String getBundleType() {
		return bundleType;
	}

	public void setBundleType(String bundleType) {
		this.bundleType = bundleType;
	}

	@Column(name = "VENUS_BUNDLE_TYPE")
	public String getVenusBundleType() {
		return venusBundleType;
	}

	public void setVenusBundleType(String venusBundleType) {
		this.venusBundleType = venusBundleType;
	}

	@ManyToOne
	@JoinColumn(name = "DEVICE_GROUP_MEMBER_ID")
	public CommonMemberPO getMember() {
		return member;
	}

	public void setMember(CommonMemberPO member) {
		this.member = member;
	}
	
	/**
	 * @ClassName: 通道排序器，id为 类型-编号， 按照编号从小到大排列<br/> 
	 * @author lvdeyang
	 * @date 2018年11月12日 上午8:36:10 
	 */
	public static final class ChannelComparatorFromPO implements Comparator<CommonMemberChannelPO>{
		@Override
		public int compare(CommonMemberChannelPO o1, CommonMemberChannelPO o2) {
			
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
	
	/**
	 * @ClassName: 通道排序器，id为 类型-编号， 按照编号从小到大排列<br/> 
	 * @author lvdeyang
	 * @date 2018年8月27日 上午8:36:10 
	 */
	public static final class ChannelComparatorFromJSON implements Comparator<JSONObject>{
		@Override
		public int compare(JSONObject o1, JSONObject o2) {
			
			long id1 = Long.parseLong(o1.getString("id").split("_")[1]);
			long id2 = Long.parseLong(o2.getString("id").split("_")[1]);
			
			if(id1 > id2){
				return 1;
			}
			if(id1 == id2){
				return 0;
			}
			return -1;
		}
	}
	
	/**
	 * @ClassName: 通道排序器，id为 类型-编号， 按照编号从小到大排列<br/> 
	 * @author lvdeyang
	 * @date 2018年10月23日 上午8:36:10 
	 */
	public static final class ChannelComparatorFromDTO implements Comparator<ChannelSchemeDTO>{
		@Override
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
