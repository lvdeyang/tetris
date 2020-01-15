package com.sumavision.bvc.device.monitor.osd;

import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * 屏幕显示配置<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年5月6日 上午8:44:14
 */
@Entity
@Table(name = "BVC_MONITOR_OSD")
public class MonitorOsdPO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;

	/** 屏幕显示名称 */
	private String name;
	
	/** 创建用户id */
	private String userId;
	
	/** 创建用户名 */
	private String username;
	
	/** 图层列表 */
	private Set<MonitorOsdLayerPO> layers;

	@Column(name = "NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "USERNAME")
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Column(name = "USER_ID")
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@OneToMany(mappedBy = "osd", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	public Set<MonitorOsdLayerPO> getLayers() {
		return layers;
	}

	public void setLayers(Set<MonitorOsdLayerPO> layers) {
		this.layers = layers;
	}
	
}
