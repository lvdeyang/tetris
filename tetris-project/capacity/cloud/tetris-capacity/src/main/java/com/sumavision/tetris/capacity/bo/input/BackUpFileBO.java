package com.sumavision.tetris.capacity.bo.input;

/**
 * backup中file参数<br/>
 * <b>作者:</b>wjw<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年11月6日 下午2:32:36
 */
public class BackUpFileBO {

	private String url;
	
	private String md5;

	public String getUrl() {
		return url;
	}

	public BackUpFileBO setUrl(String url) {
		this.url = url;
		return this;
	}

	public String getMd5() {
		return md5;
	}

	public BackUpFileBO setMd5(String md5) {
		this.md5 = md5;
		return this;
	}
	
}
