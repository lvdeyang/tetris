package com.suma.venus.resource.base.bo;

/**
 * 
 * @author lxw
 *
 */
public class LockBundleRespParam extends ResponseBody{

	private Integer operate_index;

	private Integer operate_count;
	
	public Integer getOperate_index() {
		return operate_index;
	}

	public void setOperate_index(Integer operate_index) {
		this.operate_index = operate_index;
	}

	public Integer getOperate_count() {
		return operate_count;
	}

	public void setOperate_count(Integer operate_count) {
		this.operate_count = operate_count;
	}
	
}
