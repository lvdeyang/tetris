package com.sumavision.tetris.capacity.bo.input;

import com.sumavision.tetris.business.common.Util.IpV4Util;
import com.sumavision.tetris.business.common.bo.MediaSourceBO;

/**
 * 通用协议参数 udp/rtp<br/>
 * <b>作者:</b>wjw<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年10月28日 下午4:59:49
 */
public class CommonTsBO {

	/** 源ip */
	private String source_ip;
	
	/** 源端口 */
	private Integer source_port;
	
	/** 本地IP，source_ip为组播时必须指定 */
	private String local_ip;
	
	/** IGMPV3参数，用于指定组播接收控制 */
	private Igmpv3BO igmpv3;

	public String getSource_ip() {
		return source_ip;
	}

	public CommonTsBO setSource_ip(String source_ip) {
		this.source_ip = source_ip;
		return this;
	}

	public Integer getSource_port() {
		return source_port;
	}

	public CommonTsBO setSource_port(Integer source_port) {
		this.source_port = source_port;
		return this;
	}

	public String getLocal_ip() {
		return local_ip;
	}

	public CommonTsBO setLocal_ip(String local_ip) {
		this.local_ip = local_ip;
		return this;
	}

	public Igmpv3BO getIgmpv3() {
		return igmpv3;
	}

	public CommonTsBO setIgmpv3(Igmpv3BO igmpv3) {
		this.igmpv3 = igmpv3;
		return this;
	}

	public CommonTsBO(){

	}

	public CommonTsBO(MediaSourceBO sourceBO){
		this.source_ip = IpV4Util.getIpFromUrl(sourceBO.getUrl());
		this.source_port = IpV4Util.getPortFromUrl(sourceBO.getUrl());
		if (!IpV4Util.isMulticast(this.source_ip)) {
			this.local_ip = source_ip;
		}else {
			this.local_ip = sourceBO.getLocalIp();
		}
		this.igmpv3=sourceBO.getIgmpv3();
	}

	public CommonTsBO(String url,String localIp){
		this.source_ip = IpV4Util.getIpFromUrl(url);
		this.source_port = IpV4Util.getPortFromUrl(url);
		if (!IpV4Util.isMulticast(this.source_ip)) {
			this.local_ip = source_ip;
		}else {
			this.local_ip = localIp;
		}
	}
}
