package com.sumavision.bvc.device.jv230.po;

import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * @ClassName: 组合jv230
 * @author lvdeyang
 * @date 2018年8月23日 下午3:03:27 
 */
@Entity
@Table(name = "BVC_USER_RESOURCE_COMBINE_JV230")
public class CombineJv230PO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;

	/** 组合设备名称名称 */
	private String name;
	
	/** 模仿设备类型 */
	private String model = "combineJv230";
	
	/** 模仿资源设备类型*/
	private String type = "VenusVideoMatrix";
	
	/** 隶属文件夹id */
	private Long folderId;
	
	/** 创建用户id */
	private Long userId;
	
	/** 创建用户名称 */
	private String userName;
	
	/** 前端布局 */
	private String websiteDraw;
	
	/** jv230设备信息 */
	private Set<Jv230PO> bundles;
	
	/** 大屏配置信列表*/
	private Set<CombineJv230ConfigPO> configs;

	@Column(name = "NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Column(name = "MODEL")
	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	@Column(name = "TYPE")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Column(name = "FOLDER_ID")
	public Long getFolderId() {
		return folderId;
	}

	public void setFolderId(Long folderId) {
		this.folderId = folderId;
	}

	@Column(name = "USER_ID")
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	@Column(name = "USER_NAME")
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Column(name = "WEBSITEDRAW")
	public String getWebsiteDraw() {
		return websiteDraw;
	}

	public void setWebsiteDraw(String websiteDraw) {
		this.websiteDraw = websiteDraw;
	}

	@OneToMany(mappedBy = "combineJv230", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	public Set<Jv230PO> getBundles() {
		return bundles;
	}

	public void setBundles(Set<Jv230PO> bundles) {
		this.bundles = bundles;
	}

	@OneToMany(mappedBy = "combineJv230", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	public Set<CombineJv230ConfigPO> getConfigs() {
		return configs;
	}

	public void setConfigs(Set<CombineJv230ConfigPO> configs) {
		this.configs = configs;
	}
	
}
