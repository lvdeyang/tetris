package com.sumavision.signal.bvc.fifthg.bo.socket;

/**
 * 5g背包设备socket注册信息<br/>
 * <b>作者:</b>wjw<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年6月1日 上午10:19:34
 */
public class FifthgSocketBO {

	private String ip;
	
	private String name;
	
	private String readCommunity;
	
	private String writeCommunity;
	
	private Long webPort;
	
	private Long port;
	
	private String sn;
	
	private Long onOff;
	
	private Long vRate;
	
	private Long sysRate;

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getReadCommunity() {
		return readCommunity;
	}

	public void setReadCommunity(String readCommunity) {
		this.readCommunity = readCommunity;
	}

	public String getWriteCommunity() {
		return writeCommunity;
	}

	public void setWriteCommunity(String writeCommunity) {
		this.writeCommunity = writeCommunity;
	}

	public Long getWebPort() {
		return webPort;
	}

	public void setWebPort(Long webPort) {
		this.webPort = webPort;
	}

	public Long getPort() {
		return port;
	}

	public void setPort(Long port) {
		this.port = port;
	}

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public Long getOnOff() {
		return onOff;
	}

	public void setOnOff(Long onOff) {
		this.onOff = onOff;
	}

	public Long getvRate() {
		return vRate;
	}

	public void setvRate(Long vRate) {
		this.vRate = vRate;
	}

	public Long getSysRate() {
		return sysRate;
	}

	public void setSysRate(Long sysRate) {
		this.sysRate = sysRate;
	}
}
