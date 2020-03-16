package com.sumavision.tetris.agent.vo;

public class LocalPublishVO {

	private String status;
	
	private String bundle_id;
	
	private String video1_name;
	
	private String audio1_name;
	
	private CodecParamVO codec_param;

	public String getStatus() {
		return status;
	}

	public LocalPublishVO setStatus(String status) {
		this.status = status;
		return this;
	}

	public String getBundle_id() {
		return bundle_id;
	}

	public LocalPublishVO setBundle_id(String bundle_id) {
		this.bundle_id = bundle_id;
		return this;
	}

	public String getVideo1_name() {
		return video1_name;
	}

	public LocalPublishVO setVideo1_name(String video1_name) {
		this.video1_name = video1_name;
		return this;
	}

	public String getAudio1_name() {
		return audio1_name;
	}

	public LocalPublishVO setAudio1_name(String audio1_name) {
		this.audio1_name = audio1_name;
		return this;
	}

	public CodecParamVO getCodec_param() {
		return codec_param;
	}

	public LocalPublishVO setCodec_param(CodecParamVO codec_param) {
		this.codec_param = codec_param;
		return this;
	}
	
}
