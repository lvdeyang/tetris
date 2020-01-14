package com.sumavision.tetris.sts.task.outputBO;

import java.util.ArrayList;

import com.sumavision.tetris.sts.task.source.ProgramPO;
import com.sumavision.tetris.sts.task.taskParamOutput.JsonRtmpBO;
import com.sumavision.tetris.sts.task.taskParamOutput.OutputCommon;
import com.sumavision.tetris.sts.task.taskParamOutput.OutputMediaBO;
import com.sumavision.tetris.sts.task.taskParamOutput.OutputMediaEncodeMessage;
import com.sumavision.tetris.sts.task.tasklink.OutputPO;


public class RtmpFlvBO extends CommonOutputBO{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3607303972308315305L;

	private Long streamMediaId;
	
	private String pubPoint;
	
	private String pubUser;
	
	private String pubPasswd;
	
	private Integer autoBitrate;
	
	private String vidBitrates;
	
	private String audBitrates;
	
	private String vidSelect;
	private String audSelect;
	
	private String resolutions;
	
	
	@Override
	public Boolean isSameWithCfg(CommonOutputBO outputBO) {
		
		if(outputBO == null){
			return false;
		}
		RtmpFlvBO bo = (RtmpFlvBO)outputBO;
		
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

	public String getResolutions() {
		return resolutions;
	}

	public void setResolutions(String resolutions) {
		this.resolutions = resolutions;
	}

	@Override
	public OutputCommon generateOutputCommon(OutputPO outputPO, ProgramPO programPO, OutputMediaEncodeMessage outputMediaEncodeMessage) {
		// TODO Auto-generated method stub
		JsonRtmpBO jsonRtmpBO = new JsonRtmpBO();
		jsonRtmpBO.setServer_url("rtmp://"+outputPO.getIp()+":"+outputPO.getPort()+"/live/"+outputPO.getPubName());
		jsonRtmpBO.setPub_user(getPubUser());
		jsonRtmpBO.setPub_password(getPubPasswd());
		jsonRtmpBO.setVid_exist(!getVidSelect().equals("0"));
		jsonRtmpBO.setAud_exist(!getAudSelect().equals("0"));
		
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
		
		jsonRtmpBO.setMedia_array(media_array);
		
		return jsonRtmpBO;
	}
	
}
