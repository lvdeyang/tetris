package com.suma.venus.resource.base.bo;

import java.util.List;

/**
 * 批量lockBundle参数
 * 
 * @author chenmo
 *
 */
public class BatchLockBundleParam {

	private Long userId;

	private List<LockBundleParam> bundles;

	private boolean mustLockAll;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public List<LockBundleParam> getBundles() {
		return bundles;
	}

	public void setBundles(List<LockBundleParam> bundles) {
		this.bundles = bundles;
	}

	public boolean isMustLockAll() {
		return mustLockAll;
	}

	public void setMustLockAll(boolean mustLockAll) {
		this.mustLockAll = mustLockAll;
	}

}
