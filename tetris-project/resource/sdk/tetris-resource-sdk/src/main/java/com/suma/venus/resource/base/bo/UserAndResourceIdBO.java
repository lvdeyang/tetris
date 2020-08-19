package com.suma.venus.resource.base.bo;

import java.util.ArrayList;
import java.util.List;

public class UserAndResourceIdBO {

	private Long userId;
	
	private List<String> resourceCodes = new ArrayList<String>();

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public List<String> getResourceCodes() {
		return resourceCodes;
	}

	public void setResourceCodes(List<String> resourceCodes) {
		this.resourceCodes = resourceCodes;
	}

}
