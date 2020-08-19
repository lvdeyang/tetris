package com.sumavision.tetris.sts.task.taskParamOutput;

import java.io.Serializable;

public class StorageBO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3223378229054220139L;
	
	private String url;
	private Integer max_upload_bps;
	private Integer can_del;
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Integer getMax_upload_bps() {
		return max_upload_bps;
	}
	public void setMax_upload_bps(Integer max_upload_bps) {
		this.max_upload_bps = max_upload_bps;
	}
	public Integer getCan_del() {
		return can_del;
	}
	public void setCan_del(Integer can_del) {
		this.can_del = can_del;
	}
	

}
