package com.sumavision.tetris.streamTranscoding.addTask.requestVO;

import javax.xml.bind.annotation.XmlAttribute;

public class TSUDPVO {
	private String type;
	
	private String localIp;
	
	private String ip;
	
	private String port;
	
	private Integer programNum;
	
	private Long sysRate;
	
	private Integer pmtPid;
	
	private Integer pcrPid;
	
	private Integer audlPid;
	
	private String audlType;
	
	private Integer vidlPid;
	
	private String vidlType;
	
	private Integer tsidPid;
	
	private String name;
	
	private String provider;
	
	private Integer pcrInt;
	
	private Integer patInt;
	
	private Integer sdtInt;
	
	private Integer pmtInt;
	
	private Long audioDelay;
	
	private String cutOff;
	
	public TSUDPVO(){}
	
	public TSUDPVO(String localIp, String ip, String port, Integer programNum, String audlType, String cutoff){
		this("TS-UDP", localIp, ip, port, programNum, 464000l, 257, 8190, 650, audlType, 1, "Pname", "Provider", 30, 300, 300, 300, 0l, cutoff);
	}
	
	public TSUDPVO(String type,
			String localIp,
			String ip,
			String port,
			Integer programNum,
			Long sysRate,
			Integer pmtPid,
			Integer pcrPid,
			Integer audlPid,
			String audlType,
			Integer tsidPid,
			String name,
			String provider,
			Integer pcrInt,
			Integer patInt,
			Integer sdtInt,
			Integer pmtInt,
			Long audioDelay,
			String cutOff){
		this.type = type;
		this.localIp = localIp;
		this.ip = ip;
		this.port = port;
		this.programNum = programNum;
		this.sysRate = sysRate;
		this.pmtPid = pmtPid;
		this.pcrPid = pcrPid;
		this.audlPid = audlPid;
		this.audlType = audlType;
		this.tsidPid = tsidPid;
		this.name = name;
		this.provider = provider;
		this.pcrInt = pcrInt;
		this.patInt = patInt;
		this.sdtInt = sdtInt;
		this.pmtInt = pmtInt;
		this.audioDelay = audioDelay;
		this.cutOff = cutOff;
	}

	public String getType() {
		return type;
	}

	@XmlAttribute(name = "type")
	public void setType(String type) {
		this.type = type;
	}

	public String getLocalIp() {
		return localIp;
	}

	@XmlAttribute(name = "localIp")
	public void setLocalIp(String localIp) {
		this.localIp = localIp;
	}

	public String getIp() {
		return ip;
	}

	@XmlAttribute(name = "ip")
	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getPort() {
		return port;
	}

	@XmlAttribute(name = "port")
	public void setPort(String port) {
		this.port = port;
	}

	public Integer getProgramNum() {
		return programNum;
	}

	@XmlAttribute(name = "program-num")
	public void setProgramNum(Integer programNum) {
		this.programNum = programNum;
	}

	public Long getSysRate() {
		return sysRate;
	}

	@XmlAttribute(name = "sys-rate")
	public void setSysRate(Long sysRate) {
		this.sysRate = sysRate;
	}

	public Integer getPmtPid() {
		return pmtPid;
	}

	@XmlAttribute(name = "pmt-pid")
	public void setPmtPid(Integer pmtPid) {
		this.pmtPid = pmtPid;
	}

	public Integer getPcrPid() {
		return pcrPid;
	}

	@XmlAttribute(name = "pcr-pid")
	public void setPcrPid(Integer pcrPid) {
		this.pcrPid = pcrPid;
	}

	public Integer getAudlPid() {
		return audlPid;
	}

	@XmlAttribute(name = "audl-pid")
	public void setAudlPid(Integer audlPid) {
		this.audlPid = audlPid;
	}

	public String getAudlType() {
		return audlType;
	}

	@XmlAttribute(name = "audl-type")
	public void setAudlType(String audlType) {
		this.audlType = audlType;
	}

	public Integer getVidlPid() {
		return vidlPid;
	}

	@XmlAttribute(name = "vidl-pid")
	public void setVidlPid(Integer vidlPid) {
		this.vidlPid = vidlPid;
	}

	public String getVidlType() {
		return vidlType;
	}

	@XmlAttribute(name = "vidl-type")
	public void setVidlType(String vidlType) {
		this.vidlType = vidlType;
	}

	public Integer getTsidPid() {
		return tsidPid;
	}

	@XmlAttribute(name = "tsid-pid")
	public void setTsidPid(Integer tsidPid) {
		this.tsidPid = tsidPid;
	}

	public String getName() {
		return name;
	}

	@XmlAttribute(name = "name")
	public void setName(String name) {
		this.name = name;
	}

	public String getProvider() {
		return provider;
	}

	@XmlAttribute(name = "provider")
	public void setProvider(String provider) {
		this.provider = provider;
	}

	public Integer getPcrInt() {
		return pcrInt;
	}

	@XmlAttribute(name = "pcr-Integer")
	public void setPcrInt(Integer pcrInt) {
		this.pcrInt = pcrInt;
	}

	public Integer getPatInt() {
		return patInt;
	}

	@XmlAttribute(name = "pat-Integer")
	public void setPatInt(Integer patInt) {
		this.patInt = patInt;
	}

	public Integer getSdtInt() {
		return sdtInt;
	}

	@XmlAttribute(name = "sdt-Integer")
	public void setSdtInt(Integer sdtInt) {
		this.sdtInt = sdtInt;
	}

	public Integer getPmtInt() {
		return pmtInt;
	}

	@XmlAttribute(name = "pmt-Integer")
	public void setPmtInt(Integer pmtInt) {
		this.pmtInt = pmtInt;
	}

	public Long getAudioDelay() {
		return audioDelay;
	}

	@XmlAttribute(name = "audio-delay")
	public void setAudioDelay(Long audioDelay) {
		this.audioDelay = audioDelay;
	}

	public String getCutOff() {
		return cutOff;
	}

	@XmlAttribute(name = "cutoff")
	public void setCutOff(String cutOff) {
		this.cutOff = cutOff;
	}
}
