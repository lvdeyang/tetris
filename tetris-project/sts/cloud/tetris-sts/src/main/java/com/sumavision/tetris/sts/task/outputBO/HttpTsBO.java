package com.sumavision.tetris.sts.task.outputBO;

import java.util.ArrayList;

import com.sumavision.tetris.sts.task.source.ProgramPO;
import com.sumavision.tetris.sts.task.taskParamOutput.JsonTsHttpBO;
import com.sumavision.tetris.sts.task.taskParamOutput.OutputCommon;
import com.sumavision.tetris.sts.task.taskParamOutput.OutputMediaBO;
import com.sumavision.tetris.sts.task.taskParamOutput.OutputMediaEncodeMessage;
import com.sumavision.tetris.sts.task.taskParamOutput.OutputProgramBO;
import com.sumavision.tetris.sts.task.tasklink.OutputPO;


public class HttpTsBO extends CommonOutputBO {


	/**
	 * 
	 */
	private static final long serialVersionUID = -996552333465212955L;

	// 节目名称，页签属性名也是name，顾命名前加上program
	private String name;

	private Long sysRate;

	private Integer pmtPid;

	private Integer pcrPid;
	
	/**
	 * 下面这两个参数分别是video-pid,audio-pid的输出选择，例
	 * vidselect : 1,3
	 * audselect : 1,4
	 * 表示视频输出选择十路中的1和3，音频输出选择十路中的1和4
	 */
	private String vidSelect;
	private String audSelect;
	private String subSelect;
	/**
	 * 下面两个参数，对应的给工具发的命令对应的是如下两个 vid1-pid,vid2-pid ... aud1-pid,aud2-pid ...
	 * 这里以逗号间隔的字符串形式，依次存放输出的pid的值，具体属于哪个轨道，由outputpo中的select来决定，所以顺序很严格！！
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

	/**
	 * 码率控制方式， （TS码率控制方式有CBR，Near-CBR，VBR，ABR）
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
	
	private Long streamMediaId;

	@Override
	public Boolean isSameWithCfg(CommonOutputBO outputBO) {
		
		if(outputBO == null){
			return false;
		}
		HttpTsBO bo = (HttpTsBO)outputBO;
		
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
		if (this.subSelect == null) {
			if (bo.subSelect != null)
				return false;
		} else if (!this.subSelect.equals(bo.subSelect))
			return false;
		if (this.name == null) {
			if (bo.name != null)
				return false;
		} else if (!this.name.equals(bo.name))
			return false;
		if (this.getProvider() == null) {
			if (bo.getProvider() != null)
				return false;
		} else if (!this.getProvider().equals(bo.getProvider()))
			return false;
		if (this.getProgramNum() == null) {
			if (bo.getProgramNum() != null)
				return false;
		} else if (!this.getProgramNum().equals(bo.getProgramNum()))
			return false;
		if (this.getDstType() == null) {
			if (bo.getDstType() != null)
				return false;
		} else if (!this.getDstType().equals(bo.getDstType()))
			return false;
		if (this.sysRate == null) {
			if (bo.sysRate != null)
				return false;
		} else if (!this.sysRate.equals(bo.sysRate/1000))
			return false;
		if (this.pmtPid == null) {
			if (bo.pmtPid != null)
				return false;
		} else if (!this.pmtPid.equals(bo.pmtPid))
			return false;
		if (this.pcrPid == null) {
			if (bo.pcrPid != null)
				return false;
		} else if (!this.pcrPid.equals(bo.pcrPid))
			return false;
		if (this.pcrInt == null) {
			if (bo.pcrInt != null)
				return false;
		} else if (!this.pcrInt.equals(bo.pcrInt))
			return false;
		if (this.patInt == null) {
			if (bo.patInt != null)
				return false;
		} else if (!this.patInt.equals(bo.patInt))
			return false;
		if (this.pmtInt == null) {
			if (bo.pmtInt != null)
				return false;
		} else if (!this.pmtInt.equals(bo.pmtInt))
			return false;
		if (this.sdtInt == null) {
			if (bo.sdtInt != null)
				return false;
		} else if (!this.sdtInt.equals(bo.sdtInt))
			return false;
		if (this.tsidPid == null) {
			if (bo.tsidPid != null)
				return false;
		} else if (!this.tsidPid.equals(bo.tsidPid))
			return false;
		if (this.rateCtrl == null) {
			if (bo.rateCtrl != null)
				return false;
		} else if (!this.rateCtrl.equals(bo.rateCtrl))
			return false;
		if (this.avMode == null) {
			if (bo.avMode != null)
				return false;
		} else if (!this.avMode.equals(bo.avMode))
			return false;
		if (this.maxBitrate == null) {
			if (bo.maxBitrate != null)
				return false;
		} else if (!this.maxBitrate.equals(bo.maxBitrate/1000))
			return false;
		if (this.buf == null) {
			if (bo.buf != null)
				return false;
		} else if (!this.buf.equals(bo.buf))
			return false;
		if (this.vidPids == null) {
			if (bo.vidPids != null)
				return false;
		} else if (!this.vidPids.equals(bo.vidPids))
			return false;
		if (this.audPids == null) {
			if (bo.audPids != null)
				return false;
		} else if (!this.audPids.equals(bo.audPids))
			return false;
		
		return true;
	}
	
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

	public Long getStreamMediaId() {
		return streamMediaId;
	}

	public void setStreamMediaId(Long streamMediaId) {
		this.streamMediaId = streamMediaId;
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

	@Override
	public OutputCommon generateOutputCommon(OutputPO outputPO, ProgramPO programPO,OutputMediaEncodeMessage outputMediaEncodeMessage) {
		// TODO Auto-generated method stub
		JsonTsHttpBO jsonTsHttpBO = new JsonTsHttpBO();
		jsonTsHttpBO.setName(outputPO.getPubName());
		jsonTsHttpBO.setIp(outputPO.getIp());
		jsonTsHttpBO.setPort(outputPO.getPort());
		jsonTsHttpBO.setLocal_ip(outputPO.getLocalIp());
		jsonTsHttpBO.setPcr_int(getPcrInt());
		jsonTsHttpBO.setPat_int(getPatInt());
		jsonTsHttpBO.setPmt_int(getPmtInt());
		jsonTsHttpBO.setSdt_int(getSdtInt());
		jsonTsHttpBO.setTsid_pid(getTsidPid());
		jsonTsHttpBO.setRate_ctrl(getRateCtrl()==null?"VBR":getRateCtrl());
		if (getRateCtrl()=="VBR") {
			jsonTsHttpBO.setBitrate(getMaxBitrate()==null?8000000:getMaxBitrate()*1000);
		}else {
			jsonTsHttpBO.setBitrate(getSysRate()==null?8000000:getSysRate().intValue()*1000);
		}
		jsonTsHttpBO.setBuf_init(getBuf());
		
		ArrayList<OutputProgramBO> program_array = new ArrayList<OutputProgramBO>();
		OutputProgramBO outputProgramBO = new OutputProgramBO();
		outputProgramBO.setName(getName());
		outputProgramBO.setProvider(getProvider());
		outputProgramBO.setCharacter_set("UTF-8");
		outputProgramBO.setPcr_pid(programPO.getPcrPid());
		outputProgramBO.setPmt_pid(programPO.getPmtPid());
		
		ArrayList<OutputMediaBO> media_array = new ArrayList<OutputMediaBO>();
		//video
		String[] vidPids = getVidPids().split(",");
		//String[] vidSelect = getVidSelect().split(",");
		for(int index = 0; index < vidPids.length; index++){
			OutputMediaBO outputMediaBO = new OutputMediaBO(outputMediaEncodeMessage.getVideoTaskId().get(index),
					outputMediaEncodeMessage.getVideoEncodeId().get(index), 
					Integer.parseInt(vidPids[index]), null, "video");
			media_array.add(outputMediaBO);
		}
		
		//audio
		String[] audPids = getAudPids().split(",");
		//String[] audioSelect = getAudSelect().split(",");
		for(int index = 0; index < audPids.length; index++){
			OutputMediaBO outputMediaBO = new OutputMediaBO(outputMediaEncodeMessage.getAudioTaskId().get(index),
					outputMediaEncodeMessage.getAudioEncodeId().get(index), 
					Integer.parseInt(audPids[index]),null, "audio");
			media_array.add(outputMediaBO);
		}
		
		//subtitle
		if (null!=getSubPids() && getSubPids().length()>0) {
			String[] subPids = getSubPids().split(",");
			//String[] subSelect = getSubSelect().split(",");
			for(int index = 0; index < subPids.length; index++){
				OutputMediaBO outputMediaBO = new OutputMediaBO(outputMediaEncodeMessage.getSubtitleTaskId().get(index),
						outputMediaEncodeMessage.getSubtitleEncodeId().get(index), 
						Integer.parseInt(subPids[index]),null, "subtitle");
				media_array.add(outputMediaBO);
			}
		}
		outputProgramBO.setMedia_array(media_array);
		program_array.add(outputProgramBO);
		jsonTsHttpBO.setProgram_array(program_array);
		
		return jsonTsHttpBO;
	}
	
	
}
