package com.suma.venus.resource.base.bo;

import java.util.List;

public class BatchLockBundleRespParam extends ResponseBody {

	private Integer successCnt;

	private List<BatchLockBundleRespBody> operateBundlesResult;

	public Integer getSuccessCnt() {
		return successCnt;
	}

	public void setSuccessCnt(Integer successCnt) {
		this.successCnt = successCnt;
	}

	public List<BatchLockBundleRespBody> getOperateBundlesResult() {
		return operateBundlesResult;
	}

	public void setOperateBundlesResult(List<BatchLockBundleRespBody> operateBundlesResult) {
		this.operateBundlesResult = operateBundlesResult;
	}

}
