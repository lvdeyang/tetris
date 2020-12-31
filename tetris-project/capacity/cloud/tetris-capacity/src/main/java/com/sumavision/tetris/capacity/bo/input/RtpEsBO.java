package com.sumavision.tetris.capacity.bo.input;

import com.sumavision.tetris.business.common.Util.IpV4Util;
import com.sumavision.tetris.application.template.SourceVO;

/**
 * rtp_es参数<br/>
 * <b>作者:</b>wjw<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年10月29日 上午10:46:26
 */
public class RtpEsBO {

	/** 本地接收数据地址 0~65535 */
	private Integer local_port;

	private String source_ip;

	private String local_ip;
	
	/** 数据类型 video/audio */
	private String type;
	
	/** 媒体格式。如果为auto，则通过rfc3550标准自动根据payloadtype识别。
	      如果payloadtype >=96，则必须设置该字段，来告知媒体格式 */
	private String codec;

	public Integer getLocal_port() {
		return local_port;
	}

	public RtpEsBO setLocal_port(Integer local_port) {
		this.local_port = local_port;
		return this;
	}

	public String getType() {
		return type;
	}

	public RtpEsBO setType(String type) {
		this.type = type;
		return this;
	}

	public String getCodec() {
		return codec;
	}

	public RtpEsBO setCodec(String codec) {
		this.codec = codec;
		return this;
	}

	public String getSource_ip() {
		return source_ip;
	}

	public RtpEsBO setSource_ip(String source_ip) {
		this.source_ip = source_ip;
		return this;
	}

	public String getLocal_ip() {
		return local_ip;
	}

	public RtpEsBO setLocal_ip(String local_ip) {
		this.local_ip = local_ip;
		return this;
	}

	public RtpEsBO() {
	}

	public RtpEsBO(SourceVO sourceVO) {
		this.source_ip = IpV4Util.getIpFromUrl(sourceVO.getUrl());
		this.local_port = IpV4Util.getPortFromUrl(sourceVO.getUrl());
		this.local_ip = sourceVO.getLocal_ip();
		this.type = sourceVO.getMediaType();
		this.codec = "auto"; //假设都符合rfc3550标准
	}
}

