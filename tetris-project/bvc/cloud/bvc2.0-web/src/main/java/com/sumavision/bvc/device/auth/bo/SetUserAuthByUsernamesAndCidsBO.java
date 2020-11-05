package com.sumavision.bvc.device.auth.bo;

import java.util.Set;

public class SetUserAuthByUsernamesAndCidsBO extends SetUserAuthByUsernamesCommonBO {

	private Set<String> cids;

	public Set<String> getCids() {
		return cids;
	}

	public void setCids(Set<String> cids) {
		this.cids = cids;
	}
	
	
}
