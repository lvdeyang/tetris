package com.sumavision.tetris.sts.task.outputBO;

import com.sumavision.tetris.sts.task.source.ProgramPO;
import com.sumavision.tetris.sts.task.taskParamOutput.OutputCommon;
import com.sumavision.tetris.sts.task.taskParamOutput.OutputMediaEncodeMessage;
import com.sumavision.tetris.sts.task.tasklink.OutputPO;


public class MssBO extends CommonOutputBO{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2052317208090209398L;
	private Long streamMediaId;
	private String pubName;
	
	private String pubIp;
	private Integer streamserverPort;
	
	private String srvName;
	private String srvPwd;
	
	private Integer autoBitrate;
	private String vidSelect;
	private String audSelect;
	
	private String vidBitrates;
	private String audBitrates;
	
	
	@Override
	public Boolean isSameWithCfg(CommonOutputBO outputBO) {
		
		if(outputBO == null){
			return false;
		}
		MssBO bo = (MssBO)outputBO;
		
		if (this.vidSelect == null) {
			if (bo.vidSelect != null)
				return false;
		} else if (!this.vidSelect.equals(bo.vidSelect))
			return false;
		if (this.audSelect == null) {
			if (bo.audSelect != null)
				return false;
		} else if (!this.audSelect.equals(bo.audSelect))
			return false;
		if (this.autoBitrate == null) {
			if (bo.autoBitrate != null)
				return false;
		} else if (!this.autoBitrate.equals(bo.autoBitrate))
			return false;
		if (this.vidBitrates == null) {
			if (bo.vidBitrates != null)
				return false;
		} else if (!this.vidBitrates.equals(bo.vidBitrates))
			return false;
		if (this.audBitrates == null) {
			if (bo.audBitrates != null)
				return false;
		} else if (!this.audBitrates.equals(bo.audBitrates))
			return false;
		if (this.getDstType() == null) {
			if (bo.getDstType() != null)
				return false;
		} else if (!this.getDstType().equals(bo.getDstType()))
			return false;
		if (this.srvName == null) {
			if (bo.srvName != null)
				return false;
		} else if (!this.srvName.equals(bo.srvName))
			return false;
		if (this.srvPwd == null) {
			if (bo.srvPwd != null)
				return false;
		} else if (!this.srvPwd.equals(bo.srvPwd))
			return false;
		
		return true;
	}

	public Long getStreamMediaId() {
		return streamMediaId;
	}

	public void setStreamMediaId(Long streamMediaId) {
		this.streamMediaId = streamMediaId;
	}

	public String getPubName() {
		return pubName;
	}

	public void setPubName(String pubName) {
		this.pubName = pubName;
	}

	public String getPubIp() {
		return pubIp;
	}

	public void setPubIp(String pubIp) {
		this.pubIp = pubIp;
	}

	public Integer getStreamserverPort() {
		return streamserverPort;
	}

	public void setStreamserverPort(Integer streamserverPort) {
		this.streamserverPort = streamserverPort;
	}

	public String getSrvName() {
		return srvName;
	}

	public void setSrvName(String srvName) {
		this.srvName = srvName;
	}

	public String getSrvPwd() {
		return srvPwd;
	}

	public void setSrvPwd(String srvPwd) {
		this.srvPwd = srvPwd;
	}

	public Integer getAutoBitrate() {
		return autoBitrate;
	}

	public void setAutoBitrate(Integer autoBitrate) {
		this.autoBitrate = autoBitrate;
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

	public String getVidBitrates() {
		return vidBitrates;
	}

	public void setVidBitrates(String vidBitrates) {
		this.vidBitrates = vidBitrates;
	}

	public String getAudBitrates() {
		return audBitrates;
	}

	public void setAudBitrates(String audBitrates) {
		this.audBitrates = audBitrates;
	}


	@Override
	public OutputCommon generateOutputCommon(OutputPO outputPO, ProgramPO programPO,OutputMediaEncodeMessage outputMediaEncodeMessage) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
