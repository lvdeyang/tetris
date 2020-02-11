package com.sumavision.tetris.omms.software.service.type;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;

public class OmmsSoftwareServiceTypeTreeNodeVO {

	/** 节点类型：文件夹 */
	public static final String TYPE_FOLDER = "FOLDER";
	
	/** 节点类型：服务 */
	public static final String TYPE_SERVICE = "SERVICE";
	
	/** 节点图标：服务 */
	public static final String ICON_FOLDER = "icon-folder-open";
	
	/** 节点图标：服务 */
	public static final String ICON_SERVICE = "feather-icon-server";
	
	private Long id;
	
	private String name;
	
	private boolean isLeaf;
	
	private String icon;
	
	private String type;
	
	private Map<String, String> params;
	
	private List<OmmsSoftwareServiceTypeTreeNodeVO> children;
	
	private String parent;

	public Long getId() {
		return id;
	}

	public OmmsSoftwareServiceTypeTreeNodeVO setId(Long id) {
		this.id = id;
		return this;
	}

	public String getName() {
		return name;
	}

	public OmmsSoftwareServiceTypeTreeNodeVO setName(String name) {
		this.name = name;
		return this;
	}

	public boolean getIsLeaf() {
		return isLeaf;
	}

	public OmmsSoftwareServiceTypeTreeNodeVO setIsLeaf(boolean isLeaf) {
		this.isLeaf = isLeaf;
		return this;
	}

	public String getIcon() {
		return icon;
	}

	public OmmsSoftwareServiceTypeTreeNodeVO setIcon(String icon) {
		this.icon = icon;
		return this;
	}

	public String getType() {
		return type;
	}

	public OmmsSoftwareServiceTypeTreeNodeVO setType(String type) {
		this.type = type;
		return this;
	}

	public Map<String, String> getParams() {
		return params;
	}

	public OmmsSoftwareServiceTypeTreeNodeVO setParams(Map<String, String> params) {
		this.params = params;
		return this;
	}

	public List<OmmsSoftwareServiceTypeTreeNodeVO> getChildren() {
		return children;
	}

	public OmmsSoftwareServiceTypeTreeNodeVO setChildren(List<OmmsSoftwareServiceTypeTreeNodeVO> children) {
		this.children = children;
		return this;
	}

	public String getParent() {
		return parent;
	}

	public OmmsSoftwareServiceTypeTreeNodeVO setParent(String parent) {
		this.parent = parent;
		return this;
	}

	public OmmsSoftwareServiceTypeTreeNodeVO set(GroupType groupType){
		this.setId(0l)
			.setName(groupType.getName())
			.setIsLeaf(false)
			.setType(TYPE_FOLDER)
			.setIcon(ICON_FOLDER)
			.setChildren(new ArrayList<OmmsSoftwareServiceTypeTreeNodeVO>());
		return this;
	}
	
	public OmmsSoftwareServiceTypeTreeNodeVO set(ServiceTypePO serviceType){
		this.setId(serviceType.getId())
			.setName(serviceType.getName())
			.setIsLeaf(true)
			.setType(TYPE_SERVICE)
			.setIcon(ICON_SERVICE)
			.setParent(serviceType.getGroupType().getName())
			.setParams(new HashMapWrapper<String, String>().put("installationDirectory", serviceType.getInstallationDirectory())
														   .put("installScript", serviceType.getInstallScript())
														   .put("installScriptPath", serviceType.getInstallScriptPath())
														   .put("startupScript", serviceType.getStartupScript())
														   .put("startupScriptPath", serviceType.getStartupScriptPath())
														   .put("shutdownScript", serviceType.getShutdownScript())
														   .put("shutdownScriptPath", serviceType.getShutdownScriptPath())
														   .put("logFile", serviceType.getLogFile())
														   .getMap());
		return this;
	}
	
}
