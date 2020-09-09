package com.sumavision.tetris.guide.BO;

public class GuideEncodeArrayBO {
	
	/**编码ID */
	private String encode_id;
	
	/**编码对象，可编码h264,h265,mpeg2,avs+,passby(不用编码，直接透传)  */
	private GuideH264BO h264;
	
	/**编码格式aac,"mp2","mp3",dolby,passby */
	private GuideAac aac;
	
	public String getEncode_id() {
		return encode_id;
	}

	public void setEncode_id(String encode_id) {
		this.encode_id = encode_id;
	}

	public GuideH264BO getH264() {
		return h264;
	}

	public void setH264(GuideH264BO h264) {
		this.h264 = h264;
	}
}
