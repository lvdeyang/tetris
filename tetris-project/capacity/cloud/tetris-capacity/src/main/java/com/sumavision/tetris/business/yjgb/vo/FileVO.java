package com.sumavision.tetris.business.yjgb.vo;

public class FileVO {

	/** 文件地址 */
	private String url;
	
	/** 文件播放次数 */
	private Integer count;
	
	/** 首个文件seek时间 */
	private Long seek;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public Long getSeek() {
		return seek;
	}

	public void setSeek(Long seek) {
		this.seek = seek;
	}
	
}
