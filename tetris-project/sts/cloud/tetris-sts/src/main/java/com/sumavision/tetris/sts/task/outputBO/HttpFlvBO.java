package com.sumavision.tetris.sts.task.outputBO;

import com.sumavision.tetris.sts.task.source.ProgramPO;
import com.sumavision.tetris.sts.task.taskParamOutput.OutputCommon;
import com.sumavision.tetris.sts.task.taskParamOutput.OutputMediaEncodeMessage;
import com.sumavision.tetris.sts.task.tasklink.OutputPO;


public class HttpFlvBO extends CommonOutputBO{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8826746903523517933L;

	private Long streamMediaId;
	
	private String pubPoint;
	
	private Integer autoBitrate;
	
	private String resolutions;
	
	private String vidSelect;
	private String audSelect;
	
	private String audBitrates;
	private String vidBitrates;
	
	@Override
	public Boolean isSameWithCfg(CommonOutputBO outputBO) {
		
		if(outputBO == null){
			return false;
		}
		HttpFlvBO bo = (HttpFlvBO)outputBO;
		
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
		
		if (this.resolutions == null) {
			if (bo.resolutions != null)
				return false;
		} else if (!this.resolutions.equals(bo.resolutions))
			return false;
		if (this.pubPoint == null) {
			if (bo.pubPoint != null)
				return false;
		} else if (!this.pubPoint.equals(bo.pubPoint))
			return false;
		
		return true;
	}

	public Long getStreamMediaId() {
		return streamMediaId;
	}

	public void setStreamMediaId(Long streamMediaId) {
		this.streamMediaId = streamMediaId;
	}
	
	public String getPubPoint() {
		return pubPoint;
	}

	public void setPubPoint(String pubPoint) {
		this.pubPoint = pubPoint;
	}

	public Integer getAutoBitrate() {
		return autoBitrate;
	}

	public void setAutoBitrate(Integer autoBitrate) {
		this.autoBitrate = autoBitrate;
	}

	public String getResolutions() {
		return resolutions;
	}

	public void setResolutions(String resolutions) {
		this.resolutions = resolutions;
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

	public String getAudBitrates() {
		return audBitrates;
	}

	public void setAudBitrates(String audBitrates) {
		this.audBitrates = audBitrates;
	}

	public String getVidBitrates() {
		return vidBitrates;
	}

	public void setVidBitrates(String vidBitrates) {
		this.vidBitrates = vidBitrates;
	}

	@Override
	public OutputCommon generateOutputCommon(OutputPO outputPO, ProgramPO programPO,OutputMediaEncodeMessage outputMediaEncodeMessage) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
