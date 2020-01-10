package com.suma.venus.resource.base.bo;

public class BatchLockBundleRespBody {

	private String bundleId;

	private boolean operateResult;

	private Integer operate_index;

	private Integer operate_count;

	public String getBundleId() {
		return bundleId;
	}

	public void setBundleId(String bundleId) {
		this.bundleId = bundleId;
	}

	public boolean isOperateResult() {
		return operateResult;
	}

	public void setOperateResult(boolean operateResult) {
		this.operateResult = operateResult;
	}

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
