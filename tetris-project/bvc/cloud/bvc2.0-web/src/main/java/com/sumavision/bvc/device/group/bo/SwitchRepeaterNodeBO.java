package com.sumavision.bvc.device.group.bo;

/**
 * 切换转发器任务节点透传<br/>
 * <b>作者:</b>wjw<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年5月30日 上午11:41:01
 */
public class SwitchRepeaterNodeBO implements BasePassByContent{

	/** 设备id：bundleId1%bundleId2 */
	private String bundles;

	public String getBundles() {
		return bundles;
	}

	public void setBundles(String bundles) {
		this.bundles = bundles;
	}	
}
