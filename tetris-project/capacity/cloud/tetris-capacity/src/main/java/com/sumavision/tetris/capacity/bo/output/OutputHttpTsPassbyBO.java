package com.sumavision.tetris.capacity.bo.output;

import com.sumavision.tetris.business.common.Util.IpV4Util;

/**
 * http_ts_passby输出参数<br/>
 * <b>作者:</b>wjw<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年11月4日 上午10:23:18
 */
public class OutputHttpTsPassbyBO extends OutputBaseMediaBO<OutputHttpTsPassbyBO>{

	private String ip;

	private Integer port;

	private String local_ip;

	/** local/remote */
	private String dst_type;
	
	private String name;

	private BaseMediaBO media;

	public String getDst_type() {
		return dst_type;
	}

	public OutputHttpTsPassbyBO setDst_type(String dst_type) {
		this.dst_type = dst_type;
		return this;
	}

	public String getName() {
		return name;
	}

	public OutputHttpTsPassbyBO setName(String name) {
		this.name = name;
		return this;
	}

	public String getIp() {
		return ip;
	}

	public OutputHttpTsPassbyBO setIp(String ip) {
		this.ip = ip;
		return this;
	}

	public Integer getPort() {
		return port;
	}

	public OutputHttpTsPassbyBO setPort(Integer port) {
		this.port = port;
		return this;
	}

	public String getLocal_ip() {
		return local_ip;
	}

	public OutputHttpTsPassbyBO setLocal_ip(String local_ip) {
		this.local_ip = local_ip;
		return this;
	}

	public BaseMediaBO getMedia() {
		return media;
	}

	public OutputHttpTsPassbyBO setMedia(BaseMediaBO media) {
		this.media = media;
		return this;
	}

	public OutputHttpTsPassbyBO() {
	}

	public OutputHttpTsPassbyBO(String url, String local_ip) {
		this.ip = IpV4Util.getIpFromUrl(url);
		this.port = IpV4Util.getPortFromUrl(url);
		//切割name

		this.local_ip = local_ip;
	}
}
