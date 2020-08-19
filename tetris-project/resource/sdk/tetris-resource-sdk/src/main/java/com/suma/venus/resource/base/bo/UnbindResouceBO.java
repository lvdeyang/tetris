package com.suma.venus.resource.base.bo;

public class UnbindResouceBO {

	private String resourceCode;
	
	private boolean bDelete;
	
	public UnbindResouceBO() {
	}
	
	public UnbindResouceBO(String resourceCode, boolean bDelete) {
		this.resourceCode = resourceCode;
		this.bDelete = bDelete;
	}

	public String getResourceCode() {
		return resourceCode;
	}

	public void setResourceCode(String resourceCode) {
		this.resourceCode = resourceCode;
	}

	public boolean isbDelete() {
		return bDelete;
	}

	public void setbDelete(boolean bDelete) {
		this.bDelete = bDelete;
	}
	
}
