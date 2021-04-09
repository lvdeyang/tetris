package com.sumavision.bvc.device.group.bo;

import java.util.List;

/**
 * @ClassName: 成员变更通知协议 
 * @author wjw 
 * @date 2018年10月11日 上午9:46:57
 */
public class MemberUpdateBO {
	
	private String groupUuid;

	private List<BundleBO> bundles;

	public String getGroupUuid() {
		return groupUuid;
	}

	public MemberUpdateBO setGroupUuid(String groupUuid) {
		this.groupUuid = groupUuid;
		return this;
	}

	public List<BundleBO> getBundles() {
		return bundles;
	}

	public MemberUpdateBO setBundles(List<BundleBO> bundles) {
		this.bundles = bundles;
		return this;
	}
}
