package com.sumavision.bvc.common.group.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import com.sumavision.bvc.system.enumeration.BusinessRoleSpecial;
import com.sumavision.bvc.system.enumeration.BusinessRoleType;
import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * @ClassName: 设备组关联的业务角色（区别于系统级的业务角色） 
 * @author zy
 * @date 2018年7月31日 上午10:15:01 
 */
@Entity
@Table(name="BVC_COMMON_BUSINESS_ROLE")
public class CommonBusinessRolePO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;
	
	/** 角色名称 */
	private String name;
	
	/** 特殊字段:比如主席，发言人，观众，自定义*/
	private BusinessRoleSpecial special;
	
	/** 角色业务类型：默认，可录制*/
	private BusinessRoleType type;
	
	/** 资源设备id */
	private String bundleId;
	
	/** 资源设备类型 */
	private String baseType;
	
	/** 资源设备layerId */
	private String layerId;
	
	/** 资源设备channels */
	private String channel;

	/** 关联会议 */
	private CommonGroupPO group;
	
	@Column(name = "NAME")
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	@Enumerated(value = EnumType.STRING)
	@Column(name = "SPECIAL")
	public BusinessRoleSpecial getSpecial() {
		return special;
	}
	
	public void setSpecial(BusinessRoleSpecial special) {
		this.special = special;
	}
	
	@Enumerated(value = EnumType.STRING)
	@Column(name = "TYPE")
	public BusinessRoleType getType() {
		return type;
	}

	public void setType(BusinessRoleType type) {
		this.type = type;
	}

	@Column(name = "BUNDLE_ID")
	public String getBundleId() {
		return bundleId;
	}

	public void setBundleId(String bundleId) {
		this.bundleId = bundleId;
	}

	@Column(name = "BASE_TYPE")
	public String getBaseType() {
		return baseType;
	}
	
	public void setBaseType(String baseType) {
		this.baseType = baseType;
	}

	@Column(name = "LAYER_ID")
	public String getLayerId() {
		return layerId;
	}

	public void setLayerId(String layerId) {
		this.layerId = layerId;
	}

	@Column(name = "CHANNEL")
	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	@ManyToOne
	@JoinColumn(name = "GROUP_ID")
	public CommonGroupPO getGroup() {
		return group;
	}

	public void setGroup(CommonGroupPO group) {
		this.group = group;
	}
	
	/**
	 * @Title: 从系统资源复制数据 
	 * @param entity 系统资源
	 * @return BusinessRolePO 设备组资源
	 */
//	public CommonBusinessRolePO set(com.sumavision.bvc.system.po.BusinessRolePO entity){
//		this.setUuid(entity.getUuid());
//		this.setUpdateTime(entity.getUpdateTime());
//		this.setName(entity.getName());
//		this.setSpecial(entity.getSpecial());
//		this.setType(entity.getType());
//		return this;
//	}
	
}
