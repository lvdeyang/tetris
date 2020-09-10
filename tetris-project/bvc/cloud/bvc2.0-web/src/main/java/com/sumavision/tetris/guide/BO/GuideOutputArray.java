package com.sumavision.tetris.guide.BO;

public class GuideOutputArray {
	
	private String id;
	
	/**输出可能有多种封装类型，包括udp_ts,rtp_ts,rtmp,hls,...具体有哪些业务定 */
	private UdpTs udp_ts;

	public String getId() {
		return id;
	}

	public GuideOutputArray setId(String id) {
		this.id = id;
		return this;
	}

	public UdpTs getUdp_ts() {
		return udp_ts;
	}

	public GuideOutputArray setUdp_ts(UdpTs udp_ts) {
		this.udp_ts = udp_ts;
		return this;
	}
	

}
