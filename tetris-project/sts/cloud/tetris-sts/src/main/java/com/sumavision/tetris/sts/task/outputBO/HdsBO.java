package com.sumavision.tetris.sts.task.outputBO;

import com.sumavision.tetris.sts.task.source.ProgramPO;
import com.sumavision.tetris.sts.task.taskParamOutput.OutputCommon;
import com.sumavision.tetris.sts.task.taskParamOutput.OutputMediaEncodeMessage;
import com.sumavision.tetris.sts.task.tasklink.OutputPO;


public class HdsBO extends CommonOutputBO{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4151021794706763038L;

	private String pubName;
	
	private Long streamMediaId;
	
	private String pubIp;
	
	private Integer streamserverPort;
	
	private String pubPoint;
	
	private String pubUser;
	
	private String pubPasswd;
	
	private Integer autoBitrate;
	
	private String audSelect;
	
	private String vidSelect;
	
	private String vidBitrates;
	
	private String audBitrates;
	
	@Override
	public Boolean isSameWithCfg(CommonOutputBO outputBO) {
		
		if(outputBO == null){
			return false;
		}
		HdsBO bo = (HdsBO)outputBO;
		
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
		if (this.pubPoint == null) {
			if (bo.pubPoint != null)
				return false;
		} else if (!this.pubPoint.equals(bo.pubPoint))
			return false;
		if (this.pubUser == null) {
			if (bo.pubUser != null)
				return false;
		} else if (!this.pubUser.equals(bo.pubUser))
			return false;
		if (this.pubPasswd == null) {
			if (bo.pubPasswd != null)
				return false;
		} else if (!this.pubPasswd.equals(bo.pubPasswd))
			return false;
		
		return true;
	}

	public String getPubName() {
		return pubName;
	}

	public void setPubName(String pubName) {
		this.pubName = pubName;
	}

	public Long getStreamMediaId() {
		return streamMediaId;
	}

	public void setStreamMediaId(Long streamMediaId) {
		this.streamMediaId = streamMediaId;
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

	public String getPubPoint() {
		return pubPoint;
	}

	public void setPubPoint(String pubPoint) {
		this.pubPoint = pubPoint;
	}

	public String getPubUser() {
		return pubUser;
	}

	public void setPubUser(String pubUser) {
		this.pubUser = pubUser;
	}

	public String getPubPasswd() {
		return pubPasswd;
	}

	public void setPubPasswd(String pubPasswd) {
		this.pubPasswd = pubPasswd;
	}

	public Integer getAutoBitrate() {
		return autoBitrate;
	}

	public void setAutoBitrate(Integer autoBitrate) {
		this.autoBitrate = autoBitrate;
	}

	public String getAudSelect() {
		return audSelect;
	}

	public void setAudSelect(String audSelect) {
		this.audSelect = audSelect;
	}

	public String getVidSelect() {
		return vidSelect;
	}

	public void setVidSelect(String vidSelect) {
		this.vidSelect = vidSelect;
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
	public OutputCommon generateOutputCommon(
			OutputPO outputPO, ProgramPO programPO,
			OutputMediaEncodeMessage outputMediaEncodeMessage) {
		// TODO Auto-generated method stub
		return null;
	}

	
	
	
}
