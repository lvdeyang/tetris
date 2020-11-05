/*
 * 文件名：PrivilegePO.java
 * 版权：Copyright 2013-2015 Sumavision. All Rights Reserved. 
 * 修改人: ManerFan
 * 修改时间：2015年12月14日
 *
 */
package com.suma.venus.resource.pojo;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

/**
 * <pre>
 * 权限
 * </pre>
 *
 * @author ManerFan 2015年12月14日
 */
@Entity
@Table(name = "privilegepo")
public class PrivilegePO extends CommonPO<PrivilegePO> {

    
    @Column(name = "name")
    private String name;

    @Enumerated(EnumType.ORDINAL)
    @Column(name="privilege_type")
    private EPrivilegeType privilegeType;
    
    
    /**
     *对应的资源标识，如果是菜单就是菜单的Id，如果是资源就是资源的唯一标识码或者id 
     */
    @Column(name = "resource_indentity",unique = true)
    private String resourceIndentity;
    
    @Column(name = "pCode")
    private String pCode;
    
    
    @Column(name = "url")
    private String url;
    
    /**
               *能否被编辑，外部接口加入的自行管理 
     */
    @Type(type = "yes_no")
    private Boolean bEdit = false;
    
    /**权限级别，1级、2级、3级......*/
    private Integer level;
    
    /**vue菜单权限增加的字段*/
    @Column(name = "service_name")
    private String serviceName;
    
    /**vue菜单权限增加的字段*/
    @Column(name = "path")
    private String path;
    
    /**vue菜单权限增加的字段,路由菜单是否隐藏*/
    @Type(type = "yes_no")
    private Boolean hidden = false;
    
    /**菜单图标*/
    private String icon;
    
    /**是否快捷菜单*/
    @Type(type = "yes_no")
    private Boolean shortCut;

    /**菜单顺序*/
    private Integer orderNum;
    
    private String urlSuffix;
    
    public enum EPrivilegeType{
    	
    	//菜单权限
    	MENU,
    	
    	//自定义
    	CUSTOM,
    	
    	//资源权限
    	RESOURCE,
    	
    	//模块
    	MODULE;
    	
    	//留给以后扩充，如文件、按钮等
    }

	public EPrivilegeType getPrivilegeType() {
		return privilegeType;
	}

	public void setPrivilegeType(EPrivilegeType privilegeType) {
		this.privilegeType = privilegeType;
	}

	public String getResourceIndentity() {
		return resourceIndentity;
	}

	public void setResourceIndentity(String resourceIndentity) {
		this.resourceIndentity = resourceIndentity;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Boolean getbEdit() {
		return bEdit;
	}

	public void setbEdit(Boolean bEdit) {
		this.bEdit = bEdit;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getpCode() {
		return pCode;
	}

	public void setpCode(String pCode) {
		this.pCode = pCode;
	}
	
	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	
	public Boolean getHidden() {
		return hidden;
	}

	public void setHidden(Boolean hidden) {
		this.hidden = hidden;
	}
	
	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public Boolean getShortCut() {
		return shortCut;
	}

	public void setShortCut(Boolean shortCut) {
		this.shortCut = shortCut;
	}

	public Integer getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
	}
	
	public String getUrlSuffix() {
		return urlSuffix;
	}

	public void setUrlSuffix(String urlSuffix) {
		this.urlSuffix = urlSuffix;
	}

	public static List<PrivilegePO> getChild(String PCode, List<PrivilegePO> rootPrivileges) {
	    // 子菜单
	    List<PrivilegePO> childList = new ArrayList<PrivilegePO>();
	    List<PrivilegePO> returnList = new ArrayList<PrivilegePO>();
	    for (PrivilegePO group : rootPrivileges) {
	    	if(null != group.getResourceIndentity() && group.getpCode().equalsIgnoreCase(PCode)){
	    		childList.add(group);
	    	}
	    }
	    // 把子菜单的子菜单再循环一遍
	    for (PrivilegePO node : childList) {// 没有url子菜单还有子菜单
//	        if (StringUtils.isBlank(menu.getUrl())) {
	            // 递归
	    	returnList.addAll(getChild(node.getResourceIndentity(), rootPrivileges));
//	        }
	    } // 递归退出条件
//	    if (childList.size() == 0) {
//	        return null;
//	    }
	    returnList.addAll(childList);
	    return returnList;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((bEdit == null) ? 0 : bEdit.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((pCode == null) ? 0 : pCode.hashCode());
		result = prime * result + ((privilegeType == null) ? 0 : privilegeType.hashCode());
		result = prime * result + ((resourceIndentity == null) ? 0 : resourceIndentity.hashCode());
		result = prime * result + ((url == null) ? 0 : url.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		PrivilegePO other = (PrivilegePO) obj;
		if (bEdit == null) {
			if (other.bEdit != null)
				return false;
		} else if (!bEdit.equals(other.bEdit))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (pCode == null) {
			if (other.pCode != null)
				return false;
		} else if (!pCode.equals(other.pCode))
			return false;
		if (privilegeType != other.privilegeType)
			return false;
		if (resourceIndentity == null) {
			if (other.resourceIndentity != null)
				return false;
		} else if (!resourceIndentity.equals(other.resourceIndentity))
			return false;
		if (url == null) {
			if (other.url != null)
				return false;
		} else if (!url.equals(other.url))
			return false;
		return true;
	}
	
    
}
