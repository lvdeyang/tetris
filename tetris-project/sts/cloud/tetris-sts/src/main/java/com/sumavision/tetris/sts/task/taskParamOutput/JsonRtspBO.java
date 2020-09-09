package com.sumavision.tetris.sts.task.taskParamOutput;

import java.io.Serializable;
import java.util.ArrayList;

public class JsonRtspBO  implements OutputCommon,Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -416203645319753581L;

	private String type;
	private String sdp_name;
	private String ip;
	private Integer port;
	private String local_ip;
	private Integer mtu;
	private String av_sync;
	private ArrayList<OutputMediaBO> media_array;
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getSdp_name() {
		return sdp_name;
	}
	public void setSdp_name(String sdp_name) {
		this.sdp_name = sdp_name;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public Integer getPort() {
		return port;
	}
	public void setPort(Integer port) {
		this.port = port;
	}
	public String getLocal_ip() {
		return local_ip;
	}
	public void setLocal_ip(String local_ip) {
		this.local_ip = local_ip;
	}
	public Integer getMtu() {
		return mtu;
	}
	public void setMtu(Integer mtu) {
		this.mtu = mtu;
	}
	public String getAv_sync() {
		return av_sync;
	}
	public void setAv_sync(String av_sync) {
		this.av_sync = av_sync;
	}
	public ArrayList<OutputMediaBO> getMedia_array() {
		return media_array;
	}
	public void setMedia_array(ArrayList<OutputMediaBO> media_array) {
		this.media_array = media_array;
	}
	
	
}
