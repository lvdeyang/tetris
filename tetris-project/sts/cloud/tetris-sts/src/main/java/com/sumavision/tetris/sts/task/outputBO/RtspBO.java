package com.sumavision.tetris.sts.task.outputBO;

import java.util.ArrayList;

import com.sumavision.tetris.sts.task.source.ProgramPO;
import com.sumavision.tetris.sts.task.taskParamOutput.JsonRtspBO;
import com.sumavision.tetris.sts.task.taskParamOutput.OutputCommon;
import com.sumavision.tetris.sts.task.taskParamOutput.OutputMediaBO;
import com.sumavision.tetris.sts.task.taskParamOutput.OutputMediaEncodeMessage;
import com.sumavision.tetris.sts.task.tasklink.OutputPO;


public class RtspBO extends CommonOutputBO{


	/**
	 * 
	 */
	private static final long serialVersionUID = 3914127038211347815L;

	private String pubName;
	
	private Long streamMediaId;
	
	private Integer vidDestPort;
	
	private Integer audDestPort;
	
	private String vidSelect;
	private String audSelect;
	
	private Integer MTU;
	
	private Integer autoBitrate;
	
	private String vidBitrates;
	
	private String audBitrates;
	
	
	@Override
	public Boolean isSameWithCfg(CommonOutputBO outputBO) {
		
		if(outputBO == null){
			return false;
		}
		RtspBO bo = (RtspBO)outputBO;
		
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
		if (this.vidDestPort == null) {
			if (bo.vidDestPort != null)
				return false;
		} else if (!this.vidDestPort.equals(bo.vidDestPort))
			return false;
		if (this.audDestPort == null) {
			if (bo.audDestPort != null)
				return false;
		} else if (!this.audDestPort.equals(bo.audDestPort))
			return false;
		if (this.MTU == null) {
			if (bo.MTU != null)
				return false;
		} else if (!this.MTU.equals(bo.MTU))
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

	public Integer getVidDestPort() {
		return vidDestPort;
	}

	public void setVidDestPort(Integer vidDestPort) {
		this.vidDestPort = vidDestPort;
	}

	public Integer getAudDestPort() {
		return audDestPort;
	}

	public void setAudDestPort(Integer audDestPort) {
		this.audDestPort = audDestPort;
	}

	public Integer getMTU() {
		return MTU;
	}

	public void setMTU(Integer mTU) {
		MTU = mTU;
	}

	public Integer getAutoBitrate() {
		return autoBitrate;
	}

	public void setAutoBitrate(Integer autoBitrate) {
		this.autoBitrate = autoBitrate;
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

	@Override
	public OutputCommon generateOutputCommon(OutputPO outputPO, ProgramPO programPO, OutputMediaEncodeMessage outputMediaEncodeMessage) {
		// TODO Auto-generated method stub
		JsonRtspBO jsonRtspBO = new JsonRtspBO();
		jsonRtspBO.setType("live");
		jsonRtspBO.setSdp_name(outputPO.getPubName());
		jsonRtspBO.setIp(outputPO.getIp());
		jsonRtspBO.setPort(outputPO.getPort());
		jsonRtspBO.setLocal_ip(outputPO.getLocalIp());
		jsonRtspBO.setMtu(getMTU());
		jsonRtspBO.setAv_sync("true");
		
		
		ArrayList<OutputMediaBO> media_array = new ArrayList<OutputMediaBO>();
		//video
		if (!getVidSelect().equals("0")) {
			String[] vidSelects = getVidSelect().split(",");
			for(int index = 0; index < vidSelects.length; index++){
				OutputMediaBO outputMediaBO = new OutputMediaBO(outputMediaEncodeMessage.getVideoTaskId().get(Integer.parseInt(vidSelects[index])-1),
						outputMediaEncodeMessage.getVideoEncodeId().get(Integer.parseInt(vidSelects[index])-1), null,null,"video");
				media_array.add(outputMediaBO);
			}
		}
		
		
		//audio
		if (!getAudSelect().equals("0")) {
			String[] audSelects = getAudSelect().split(",");
			//String[] audioSelect = getAudSelect().split(",");
			for(int index = 0; index < audSelects.length; index++){
				OutputMediaBO outputMediaBO = new OutputMediaBO(outputMediaEncodeMessage.getAudioTaskId().get(Integer.parseInt(audSelects[index])-1),
						outputMediaEncodeMessage.getAudioEncodeId().get(Integer.parseInt(audSelects[index])-1), 
						null,null,"audio");
				media_array.add(outputMediaBO);
			}
		}
		
		jsonRtspBO.setMedia_array(media_array);
		return jsonRtspBO;
	}

}
