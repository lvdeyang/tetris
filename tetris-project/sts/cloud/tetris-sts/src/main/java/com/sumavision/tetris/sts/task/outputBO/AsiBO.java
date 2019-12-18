package com.sumavision.tetris.sts.task.outputBO;



import com.sumavision.tetris.sts.task.source.ProgramPO;
import com.sumavision.tetris.sts.task.taskParamOutput.OutputCommon;
import com.sumavision.tetris.sts.task.taskParamOutput.OutputMediaEncodeMessage;
import com.sumavision.tetris.sts.task.tasklink.OutputPO;

public class AsiBO extends CommonOutputBO{


	private static final long serialVersionUID = -5756447258377667563L;
	/**
	 * 以下是子页签参数，当前根据ASI参数实现，后续还会有其它类型
	 */
	private String name;

	private Long sysRate;
	
	private Integer audioDelay;
	
	private Integer pmtPid;
	
	private Integer pcrPid;
	
	/**
	 * 下面这两个参数分别是video-pid,audio-pid的输出选择，例
	 * vidselect : 1,3
	 * audselect : 1,4
	 * 表示视频输出选择十路中的1和3，音频输出选择十路中的1和4
	 */
	private String vidSelect = "";
	private String audSelect = "";
	private String subSelect = "";
	
	/**
	 * 下面两个参数，对应的给工具发的命令对应的是如下两个
	 * vid1-pid,vid2-pid ...
	 * aud1-pid,aud2-pid ...
	 * 这里以逗号间隔的字符串形式，依次存放输出的pid的值，具体属于哪个轨道，由select来决定，所以顺序很严格！！
	 * 以后看此字段是否公共字段，可以的话提到commonoutbo中
	 */
	private String vidPids = "";
	private String audPids = "";
	private String subPids = "";
	
	private Integer pcrInt;
	
	private Integer patInt;
	
	private Integer pmtInt;
	
	private Integer sdtInt;
	
	private Integer tsidPid;
	
	/**转码特有参数**/
	private Integer tdtInt;
	
	private Integer totInt;
	
	/**网关特有参数**/
	/**
	 * 码率控制方式，
	 * （TS码率控制方式有CBR，Near-CBR，VBR，ABR）
	 */
	private String rateCtrl;
	
	/**
	 * 媒体模式,sync、async
	 */
	private String avMode;
	/**
	 * 峰值码率，200000--100000000
	 */
	private Integer maxBitrate;
	/**
	 * 缓冲区
	 */
	private Integer buf;
	
	private Integer portIndex;

	private Integer packetLength;

	private Integer sCardIndex;

	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public Long getSysRate() {
		return sysRate;
	}
	public void setSysRate(Long sysRate) {
		this.sysRate = sysRate;
	}
	public Integer getAudioDelay() {
		return audioDelay;
	}
	public void setAudioDelay(Integer audioDelay) {
		this.audioDelay = audioDelay;
	}

	public Integer getPmtPid() {
		return pmtPid;
	}
	public void setPmtPid(Integer pmtPid) {
		this.pmtPid = pmtPid;
	}
	public Integer getPcrPid() {
		return pcrPid;
	}
	public void setPcrPid(Integer pcrPid) {
		this.pcrPid = pcrPid;
	}
	public String getVidPids() {
		return vidPids;
	}
	public void setVidPids(String vidPids) {
		this.vidPids = vidPids;
	}
	public String getAudPids() {
		return audPids;
	}
	public void setAudPids(String audPids) {
		this.audPids = audPids;
	}
	public Integer getPcrInt() {
		return pcrInt;
	}
	public void setPcrInt(Integer pcrInt) {
		this.pcrInt = pcrInt;
	}
	public Integer getPatInt() {
		return patInt;
	}
	public void setPatInt(Integer patInt) {
		this.patInt = patInt;
	}
	public Integer getPmtInt() {
		return pmtInt;
	}
	public void setPmtInt(Integer pmtInt) {
		this.pmtInt = pmtInt;
	}
	public Integer getSdtInt() {
		return sdtInt;
	}
	public void setSdtInt(Integer sdtInt) {
		this.sdtInt = sdtInt;
	}
	public Integer getTsidPid() {
		return tsidPid;
	}
	public void setTsidPid(Integer tsidPid) {
		this.tsidPid = tsidPid;
	}
	public String getRateCtrl() {
		return rateCtrl;
	}
	public void setRateCtrl(String rateCtrl) {
		this.rateCtrl = rateCtrl;
	}
	public String getAvMode() {
		return avMode;
	}
	public void setAvMode(String avMode) {
		this.avMode = avMode;
	}
	public Integer getMaxBitrate() {
		return maxBitrate;
	}
	public void setMaxBitrate(Integer maxBitrate) {
		this.maxBitrate = maxBitrate;
	}
	public Integer getBuf() {
		return buf;
	}
	public void setBuf(Integer buf) {
		this.buf = buf;
	}
	public Integer getTdtInt() {
		return tdtInt;
	}
	public void setTdtInt(Integer tdtInt) {
		this.tdtInt = tdtInt;
	}
	public Integer getTotInt() {
		return totInt;
	}
	public void setTotInt(Integer totInt) {
		this.totInt = totInt;
	}

	public String getVidSelect() {
		return vidSelect;
	}

	public void setVidSelect(String vidSelect) {
		this.vidSelect = vidSelect;
	}

	public String getAudSelect() {
		return audSelect;
	}

	public void setAudSelect(String audSelect) {
		this.audSelect = audSelect;
	}

	public String getSubSelect() {
		return subSelect;
	}

	public void setSubSelect(String subSelect) {
		this.subSelect = subSelect;
	}

	public String getSubPids() {
		return subPids;
	}

	public void setSubPids(String subPids) {
		this.subPids = subPids;
	}

	public Integer getPortIndex() {
		return portIndex;
	}

	public void setPortIndex(Integer portIndex) {
		this.portIndex = portIndex;
	}

	public Integer getPacketLength() {
		return packetLength;
	}

	public void setPacketLength(Integer packetLength) {
		this.packetLength = packetLength;
	}

	public Integer getsCardIndex() {
		return sCardIndex;
	}

	public void setsCardIndex(Integer sCardIndex) {
		this.sCardIndex = sCardIndex;
	}

	@Override
	public Boolean isSameWithCfg(CommonOutputBO outputBO) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OutputCommon generateOutputCommon(OutputPO outputPO, ProgramPO programPO, OutputMediaEncodeMessage outputMediaEncodeMessage) {
		// TODO Auto-generated method stub
		return null;
	}
}
