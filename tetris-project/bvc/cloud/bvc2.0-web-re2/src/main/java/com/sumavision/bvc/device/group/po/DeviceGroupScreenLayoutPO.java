package com.sumavision.bvc.device.group.po;

import java.util.Date;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * @ClassName: 设备组关联的屏幕布局方案（区别于系统级屏幕布局方案） 
 * @author zy
 * @date 2018年7月31日 上午10:41:41 
 */
@Entity
@Table(name="BVC_DEVICE_GROUP_SCREEN_LAYOUT")
public class DeviceGroupScreenLayoutPO extends AbstractBasePO{
	
	private static final long serialVersionUID = 1L;
	
	/** 方案名称 */
	private String name;
	
	/** 网页端布局数据，格式：{basic:{column:4, row:4}, cellspan:[{x:0, y0, r:1, b:1}]} */
	private String websiteDraw;
	
	/** 关联分屏布局 */
	private Set<DeviceGroupScreenPositionPO> positions;
	
	/** 关联设备组 */
	private DeviceGroupPO group;

	@Column(name = "NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "WEBSITE_DRAW", length = 1024)
	public String getWebsiteDraw() {
		return websiteDraw;
	}

	public void setWebsiteDraw(String websiteDraw) {
		this.websiteDraw = websiteDraw;
	}

	@OneToMany(mappedBy = "layout", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	public Set<DeviceGroupScreenPositionPO> getPositions() {
		return positions;
	}

	public void setPositions(Set<DeviceGroupScreenPositionPO> positions) {
		this.positions = positions;
	}

	@ManyToOne
	@JoinColumn(name = "GROUP_ID")
	public DeviceGroupPO getGroup() {
		return group;
	}

	public void setGroup(DeviceGroupPO group) {
		this.group = group;
	}
	
	/**
	 * @Title: 从系统资源中复制数据 
	 * @param entity 系统资源
	 * @return ScreenLayoutPO 设备组资源
	 */
	public DeviceGroupScreenLayoutPO set(com.sumavision.bvc.system.po.ScreenLayoutPO entity){
		this.setUuid(entity.getUuid());
		this.setUpdateTime(new Date());
		this.setName(entity.getName());
		this.setWebsiteDraw(entity.getWebsiteDraw());
		return this;
	}
	
}
