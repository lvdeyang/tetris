package com.sumavision.signal.bvc.capacity.bo.input;

public class CoverFileBO {

	/** video/audio/all/image,all表示音视频全盖，image的时候，音频盖静音 */
	private String type;
	
	private String start_time;
	
	private String end_time;
	
	private String url;
	
	private String file_start;
	
	private String file_end;
	
	public String getType() {
		return type;
	}

	public CoverFileBO setType(String type) {
		this.type = type;
		return this;
	}

	public String getStart_time() {
		return start_time;
	}

	public CoverFileBO setStart_time(String start_time) {
		this.start_time = start_time;
		return this;
	}

	public String getEnd_time() {
		return end_time;
	}

	public CoverFileBO setEnd_time(String end_time) {
		this.end_time = end_time;
		return this;
	}

	public String getUrl() {
		return url;
	}

	public CoverFileBO setUrl(String url) {
		this.url = url;
		return this;
	}

	public String getFile_start() {
		return file_start;
	}

	public CoverFileBO setFile_start(String file_start) {
		this.file_start = file_start;
		return this;
	}

	public String getFile_end() {
		return file_end;
	}

	public CoverFileBO setFile_end(String file_end) {
		this.file_end = file_end;
		return this;
	}
	
}
