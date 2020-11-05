package com.sumavision.tetris.guide.BO;

import com.sumavision.tetris.guide.control.RateCtrl;

public class GuideOutputArrayBO {
	
//	private String id;
	
	/**输出可能有多种封装类型，包括udp_ts,rtp_ts,rtmp,hls,...具体有哪些业务定 */
//	private UdpTsBO udp_ts;

//	public String getId() {
//		return id;
//	}
//
//	public GuideOutputArrayBO setId(String id) {
//		this.id = id;
//		return this;
//	}

//	public UdpTsBO getUdp_ts() {
//		return udp_ts;
//	}
//
//	public GuideOutputArrayBO setUdp_ts(UdpTsBO udp_ts) {
//		this.udp_ts = udp_ts;
//		return this;
//	}
	
//  输出可能有多种封装类型，包括"udp_ts","rtp_ts","rtmp","srt"等
	private String type="udp_ts";
	
	private String url;
	
	private RateCtrl rate_ctrl;
	
	private Long bitrate;

	public String getType() {
		return type;
	}

	public GuideOutputArrayBO setType(String type) {
		this.type = type;
		return this;
	}

	public String getUrl() {
		return url;
	}

	public GuideOutputArrayBO setUrl(String url) {
		this.url = url;
		return this;
	}

	public RateCtrl getRate_ctrl() {
		return rate_ctrl;
	}

	public GuideOutputArrayBO setRate_ctrl(RateCtrl rate_ctrl) {
		this.rate_ctrl = rate_ctrl;
		return this;
	}

	public Long getBitrate() {
		return bitrate;
	}

	public GuideOutputArrayBO setBitrate(Long bitrate) {
		this.bitrate = bitrate;
		return this;
	}
	
	
}
