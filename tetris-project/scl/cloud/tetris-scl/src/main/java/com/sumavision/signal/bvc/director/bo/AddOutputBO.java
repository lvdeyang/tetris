package com.sumavision.signal.bvc.director.bo;

import java.util.List;

/**
 * 添加导播输出参数<br/>
 * <b>作者:</b>wjw<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年2月14日 上午8:03:02
 */
public class AddOutputBO {

	private String taskId;
	
	private List<BundleBO> bundles;

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public List<BundleBO> getBundles() {
		return bundles;
	}

	public void setBundles(List<BundleBO> bundles) {
		this.bundles = bundles;
	}
	
}
