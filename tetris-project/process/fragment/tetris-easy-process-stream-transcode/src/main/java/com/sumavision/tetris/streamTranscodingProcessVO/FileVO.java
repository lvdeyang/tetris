package com.sumavision.tetris.streamTranscodingProcessVO;

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

	public FileVO setUrl(String url) {
		this.url = url;
		return this;
	}

	public Integer getCount() {
		return count;
	}

	public FileVO setCount(Integer count) {
		this.count = count;
		return this;
	}

	public Long getSeek() {
		return seek;
	}

	public FileVO setSeek(Long seek) {
		this.seek = seek;
		return this;
	}
}
