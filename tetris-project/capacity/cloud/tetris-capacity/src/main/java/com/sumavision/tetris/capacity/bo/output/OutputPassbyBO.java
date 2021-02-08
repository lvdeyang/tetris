package com.sumavision.tetris.capacity.bo.output;

import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.business.common.MissionBO;
import com.sumavision.tetris.business.common.Util.IpV4Util;
import com.sumavision.tetris.capacity.bo.task.EncodeBO;
import com.sumavision.tetris.capacity.bo.task.TaskBO;

/**
 * passby输出参数<br/>
 * <b>作者:</b>wjw<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年11月4日 上午10:16:46
 */
public class OutputPassbyBO {

	private String ip;
	
	private Integer port;
	
	private String local_ip;
	
	private BaseMediaBO media;

	public String getIp() {
		return ip;
	}

	public OutputPassbyBO setIp(String ip) {
		this.ip = ip;
		return this;
	}

	public Integer getPort() {
		return port;
	}

	public OutputPassbyBO setPort(Integer port) {
		this.port = port;
		return this;
	}

	public String getLocal_ip() {
		return local_ip;
	}

	public OutputPassbyBO setLocal_ip(String local_ip) {
		this.local_ip = local_ip;
		return this;
	}

	public BaseMediaBO getMedia() {
		return media;
	}

	public OutputPassbyBO setMedia(BaseMediaBO media) {
		this.media = media;
		return this;
	}

	public OutputPassbyBO() {
	}

	public OutputPassbyBO(String url, String local_ip) {
		this.ip = IpV4Util.getIpFromUrl(url);
		this.port = IpV4Util.getPortFromUrl(url);
		this.local_ip = local_ip;
	}

	public OutputPassbyBO(MissionBO missionBO,JSONObject outputObj){
		String url = outputObj.getString("url");
		this.ip=IpV4Util.getIpFromUrl(url);
		this.port=IpV4Util.getPortFromUrl(url);
		if (outputObj.containsKey("local_ip")){
			this.local_ip=outputObj.getString("local_ip");
		}else{
			this.local_ip = missionBO.getDevice_ip();//没写出流网口IP的话，直接用控制口IP
		}
		TaskBO taskBO = missionBO.getTask_array().get(0);
		EncodeBO encodeBO = taskBO.getEncode_array().get(0);
		BaseMediaBO baseMediaBO = new BaseMediaBO().setTask_id(taskBO.getId()).setEncode_id(encodeBO.getEncode_id());
		this.media=baseMediaBO;
	}
}
