package com.sumavision.tetris.capacity.bo.task;

public class SvtBO {

	private String pixel_format;
	
	private Integer enc_mode;
	
	private String encoding_type;
	
	private String bframe_reference;

	public String getPixel_format() {
		return pixel_format;
	}

	public SvtBO setPixel_format(String pixel_format) {
		this.pixel_format = pixel_format;
		return this;
	}

	public Integer getEnc_mode() {
		return enc_mode;
	}

	public SvtBO setEnc_mode(Integer enc_mode) {
		this.enc_mode = enc_mode;
		return this;
	}

	public String getEncoding_type() {
		return encoding_type;
	}

	public SvtBO setEncoding_type(String encoding_type) {
		this.encoding_type = encoding_type;
		return this;
	}

	public String getBframe_reference() {
		return bframe_reference;
	}

	public SvtBO setBframe_reference(String bframe_reference) {
		this.bframe_reference = bframe_reference;
		return this;
	}
	
}
