package com.sumavision.tetris.capacity.bo.input;

/**
 * 源地址参数<br/>
 * <b>作者:</b>wjw<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年10月28日 下午4:41:23
 */
public class SourceUrlBO {

	/** 原地址 */
	private String url;
	
	/** 传输协议类型 */
	private String type;

	public String getUrl() {
		return url;
	}

	public SourceUrlBO setUrl(String url) {
		this.url = url;
		return this;
	}

	public String getType() {
		return type;
	}

	public SourceUrlBO setType(String type) {
		this.type = type;
		return this;
	}

	public SourceUrlBO() {
	}

	public SourceUrlBO(String url, String type) {
		this.url = url;
		this.type = type;
	}
}
