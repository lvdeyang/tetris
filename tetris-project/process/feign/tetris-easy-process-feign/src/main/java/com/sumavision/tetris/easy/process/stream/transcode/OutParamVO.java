package com.sumavision.tetris.easy.process.stream.transcode;

public class OutParamVO {
	/** 输出目标地址 */
	private String outputUrl;
	/** 输出网卡ip */
	private String localIp;
	/** pmt间隔 */
	private Integer pmtpid;
	/** pcr间隔 */
	private Integer pcrpid;
	/** 音频输出pid */
	private Integer aud1pid;
	
	private Integer tsidpid;
	/** 视频输出pid */
	private Integer vid1pid;
	/** 节目号 */
	private Integer progNum;
	
	private String cutoff;
	
	public String getOutputUrl() {
		return outputUrl;
	}

	public OutParamVO setOutputUrl(String outputUrl) {
		this.outputUrl = outputUrl;
		return this;
	}
	
	public String getLocalIp() {
		return localIp;
	}

	public OutParamVO setLocalIp(String localIp) {
		this.localIp = localIp;
		return this;
	}

	public Integer getPmtpid() {
		return pmtpid;
	}

	public OutParamVO setPmtpid(Integer pmtpid) {
		this.pmtpid = pmtpid;
		return this;
	}

	public Integer getPcrpid() {
		return pcrpid;
	}

	public OutParamVO setPcrpid(Integer pcrpid) {
		this.pcrpid = pcrpid;
		return this;
	}

	public Integer getAud1pid() {
		return aud1pid;
	}

	public OutParamVO setAud1pid(Integer aud1pid) {
		this.aud1pid = aud1pid;
		return this;
	}

	public Integer getTsidpid() {
		return tsidpid;
	}

	public OutParamVO setTsidpid(Integer tsidpid) {
		this.tsidpid = tsidpid;
		return this;
	}

	public Integer getVid1pid() {
		return vid1pid;
	}

	public OutParamVO setVid1pid(Integer vid1pid) {
		this.vid1pid = vid1pid;
		return this;
	}

	public Integer getProgNum() {
		return progNum;
	}

	public OutParamVO setProgNum(Integer progNum) {
		this.progNum = progNum;
		return this;
	}

	public String getCutoff() {
		return cutoff;
	}

	public OutParamVO setCutoff(String cutoff) {
		this.cutoff = cutoff;
		return this;
	}
	
	public OutParamVO copy(){
		OutParamVO newOutParamVO = new OutParamVO()
				.setAud1pid(this.getAud1pid())
				.setCutoff(this.getCutoff())
				.setLocalIp(this.getLocalIp())
				.setOutputUrl(outputUrl)
				.setPcrpid(this.getPcrpid())
				.setPmtpid(this.getPmtpid())
				.setProgNum(this.getProgNum())
				.setTsidpid(this.getTsidpid())
				.setVid1pid(this.getVid1pid());
		return newOutParamVO;
	}
}
