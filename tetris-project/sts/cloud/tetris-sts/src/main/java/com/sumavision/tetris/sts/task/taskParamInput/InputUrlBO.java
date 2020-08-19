package com.sumavision.tetris.sts.task.taskParamInput;

import java.io.Serializable;

/**
 * http_ts  hls  dash mss rtsp rtmp http_flv
 */
public class InputUrlBO implements InputCommon,Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2262502725852718959L;
	public InputUrlBO(String url){
		this.url = url;
	}
	private String url;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	

}
