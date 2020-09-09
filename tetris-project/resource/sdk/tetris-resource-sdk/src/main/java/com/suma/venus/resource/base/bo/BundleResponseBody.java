package com.suma.venus.resource.base.bo;

public class BundleResponseBody extends ResponseBody{
	
	private BundleBody bundle;
	
	/**bundle附加属性**/
	private String bundle_extra_info;
	
	/**用户附加属性**/
	private String user_extra_info;
	
	public BundleResponseBody() {
		super();
	}
	
	public BundleResponseBody(String result) {
		super(result);
	}

	public BundleResponseBody(String result, BundleBody bundle) {
		super(result);
		this.bundle = bundle;
	}

	public BundleBody getBundle() {
		return bundle;
	}

	public void setBundle(BundleBody bundle) {
		this.bundle = bundle;
	}

	public String getBundle_extra_info() {
		return bundle_extra_info;
	}

	public void setBundle_extra_info(String bundle_extra_info) {
		this.bundle_extra_info = bundle_extra_info;
	}

	public String getUser_extra_info() {
		return user_extra_info;
	}

	public void setUser_extra_info(String user_extra_info) {
		this.user_extra_info = user_extra_info;
	}

}
