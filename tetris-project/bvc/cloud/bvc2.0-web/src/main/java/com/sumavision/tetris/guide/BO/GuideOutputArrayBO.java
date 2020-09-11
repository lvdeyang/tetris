package com.sumavision.tetris.guide.BO;

public class GuideOutputArrayBO {
	
	private String id;
	
	/**输出可能有多种封装类型，包括udp_ts,rtp_ts,rtmp,hls,...具体有哪些业务定 */
	private UdpTsBO udp_ts;

	public String getId() {
		return id;
	}

	public GuideOutputArrayBO setId(String id) {
		this.id = id;
		return this;
	}

	public UdpTsBO getUdp_ts() {
		return udp_ts;
	}

	public GuideOutputArrayBO setUdp_ts(UdpTsBO udp_ts) {
		this.udp_ts = udp_ts;
		return this;
	}
	

}
