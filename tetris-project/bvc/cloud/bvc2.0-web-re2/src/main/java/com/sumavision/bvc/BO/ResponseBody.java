package com.sumavision.bvc.BO;

/**
 * 响应消息体
 * @author lxw
 *
 */
public class ResponseBody {

	public static final String SUCCESS = "success";
	
	@Deprecated
	public static final String FAIL = "failed";

	public static final String BUNDLE_NOT_FOUND = "bundle_not_found";

	public static final String BUNDLE_OFFLINE = "bundle_offline";

	public static final String INVALID_OPERATE_INDEX = "invalid_operate_index";

	public static final String TIME_OUT = "time_out";

	public static final String REJECT = "reject";

	public static final String UNKNOWN_FAILED = "unknown_failed";
	
	protected String result;
	
	protected String bundleId;
	
	protected String error_description;
	
	protected String pass_by_str;
	
	public ResponseBody(){}

	public ResponseBody(String result) {
		this.result = result;
	}
	
	public ResponseBody(String result, String error_desc) {
		this.result = result;
		this.error_description = error_desc;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getError_description() {
		return error_description;
	}

	public void setError_description(String error_description) {
		this.error_description = error_description;
	}

	public String getBundleId() {
		return bundleId;
	}

	public void setBundleId(String bundleId) {
		this.bundleId = bundleId;
	}

	public String getPass_by_str() {
		return pass_by_str;
	}

	public void setPass_by_str(String pass_by_str) {
		this.pass_by_str = pass_by_str;
	}

	
}
