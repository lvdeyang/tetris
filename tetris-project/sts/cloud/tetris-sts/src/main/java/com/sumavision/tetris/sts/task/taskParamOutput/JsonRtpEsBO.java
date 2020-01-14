package com.sumavision.tetris.sts.task.taskParamOutput;

import java.io.Serializable;
import java.util.ArrayList;

public class JsonRtpEsBO implements OutputCommon,Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8218797930444850270L;
	private String ip;
	private Integer port;
	private String local_ip;
	private Integer max_bitrate;
	private Integer mtu;
	private Integer time_scale;
	private ArrayList<OutputMediaBO> media;
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
	public Integer getMax_bitrate() {
		return max_bitrate;
	}
	public void setMax_bitrate(Integer max_bitrate) {
		this.max_bitrate = max_bitrate;
	}
	public Integer getMtu() {
		return mtu;
	}
	public void setMtu(Integer mtu) {
		this.mtu = mtu;
	}
	public Integer getTime_scale() {
		return time_scale;
	}
	public void setTime_scale(Integer time_scale) {
		this.time_scale = time_scale;
	}
	public ArrayList<OutputMediaBO> getMedia() {
		return media;
	}
	public void setMedia(ArrayList<OutputMediaBO> media) {
		this.media = media;
	}
	
}
