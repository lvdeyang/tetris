package com.sumavision.bvc.basic.bo;

/**
 * 基础角色业务数据<br/>
 * <b>作者:</b>wjw<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年10月14日 上午9:33:09
 */
public class BasicRoleBO {

	//名称
	private String name;
	
	//类型
	private String special;
	
	//是否是虚拟设备
	private boolean isVirtualDevice;
	
	public BasicRoleBO(){};
	
	public BasicRoleBO (String name, String special, boolean isVirtualDevice){
		this.setName(name);
		this.setSpecial(special);
		this.setVirtualDevice(isVirtualDevice);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSpecial() {
		return special;
	}

	public void setSpecial(String special) {
		this.special = special;
	}

	public boolean isVirtualDevice() {
		return isVirtualDevice;
	}

	public void setVirtualDevice(boolean isVirtualDevice) {
		this.isVirtualDevice = isVirtualDevice;
	}
	
}
