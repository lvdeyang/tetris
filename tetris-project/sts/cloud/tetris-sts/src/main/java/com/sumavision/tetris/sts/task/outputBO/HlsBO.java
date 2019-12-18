package com.sumavision.tetris.sts.task.outputBO;

import com.sumavision.tetris.sts.task.source.ProgramPO;
import com.sumavision.tetris.sts.task.taskParamOutput.JsonHlsBO;
import com.sumavision.tetris.sts.task.taskParamOutput.OutputCommon;
import com.sumavision.tetris.sts.task.taskParamOutput.OutputMediaEncodeMessage;
import com.sumavision.tetris.sts.task.tasklink.OutputPO;

public class HlsBO extends CommonOutputBO{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3117111657408864336L;

	private String url;
	
	private String m3u8Name;
	
	private Integer groupCount;
	
	private Integer groupFileCount;
	
	private Integer chunkSpan;
	
	private String resolutions;
	
	private String vidSelect = "";
	private String audSelect = "";
	private String subSelect = "";

	private String vidBitrates = "";
	private String audBitrates = "";

	private String subdirFlag;

	/**
	 * 格式：[{video: 1 , audio: [1,2] , subs : [1,2]}]
	 */
	private String mediaGroup;
	
	private String mode;
	
	//是否加密
	private String encrypt;

	
	@Override
	public Boolean isSameWithCfg(CommonOutputBO outputBO) {
		
		if(outputBO == null){
			return false;
		}
		HlsBO bo = (HlsBO)outputBO;
		
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
		if (this.resolutions == null) {
			if (bo.resolutions != null)
				return false;
		} else if (!this.resolutions.equals(bo.resolutions))
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
		if (this.subdirFlag == null) {
			if (bo.subdirFlag != null)
				return false;
		} else if (!this.subdirFlag.equals(bo.subdirFlag))
			return false;
		if (this.m3u8Name == null) {
			if (bo.m3u8Name != null)
				return false;
		} else if (!this.m3u8Name.equals(bo.m3u8Name))
			return false;
		if (this.groupCount == null) {
			if (bo.groupCount != null)
				return false;
		} else if (!this.groupCount.equals(bo.groupCount))
			return false;
		if (this.groupFileCount == null) {
			if (bo.groupFileCount != null)
				return false;
		} else if (!this.groupFileCount.equals(bo.groupFileCount))
			return false;
		if (this.chunkSpan == null) {
			if (bo.chunkSpan != null)
				return false;
		} else if (!this.chunkSpan.equals(bo.chunkSpan))
			return false;
		if (this.encrypt == null) {
			if (bo.encrypt != null)
				return false;
		} else if (!this.encrypt.equals(bo.encrypt))
			return false;
		if(this.getDstType() == 1){
			if (this.url == null) {
				if (bo.url != null)
					return false;
			} else if (!this.url.equals(bo.url))
				return false;
		}
		if (this.mediaGroup == null) {
			if (bo.mediaGroup != null)
				return false;
		} else if (!this.mediaGroup.equals(bo.mediaGroup))
			return false;	
	
		return true;
	}


	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getM3u8Name() {
		return m3u8Name;
	}

	public void setM3u8Name(String m3u8Name) {
		this.m3u8Name = m3u8Name;
	}

	public Integer getGroupCount() {
		return groupCount;
	}

	public void setGroupCount(Integer groupCount) {
		this.groupCount = groupCount;
	}

	public Integer getGroupFileCount() {
		return groupFileCount;
	}

	public void setGroupFileCount(Integer groupFileCount) {
		this.groupFileCount = groupFileCount;
	}

	public Integer getChunkSpan() {
		return chunkSpan;
	}

	public void setChunkSpan(Integer chunkSpan) {
		this.chunkSpan = chunkSpan;
	}

	public String getResolutions() {
		return resolutions;
	}

	public void setResolutions(String resolutions) {
		this.resolutions = resolutions;
	}

	public String getSubSelect() {
		return subSelect;
	}

	public void setSubSelect(String subSelect) {
		this.subSelect = subSelect;
	}

	
	public String getMediaGroup() {
		return mediaGroup;
	}

	public void setMediaGroup(String mediaGroup) {
		this.mediaGroup = mediaGroup;
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

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public String getSubdirFlag() {
		return subdirFlag;
	}

	public void setSubdirFlag(String subdirFlag) {
		this.subdirFlag = subdirFlag;
	}

	public String getEncrypt() {
		return encrypt;
	}

	public void setEncrypt(String encrypt) {
		this.encrypt = encrypt;
	}


	@Override
	public OutputCommon generateOutputCommon(OutputPO outputPO, ProgramPO programPO,OutputMediaEncodeMessage outputMediaEncodeMessage) {
		// TODO Auto-generated method stub
		
		JsonHlsBO jsonHlsBO = new JsonHlsBO();
		jsonHlsBO.setGroup_count(getGroupCount());
		jsonHlsBO.setGroup_file_count(getGroupFileCount());
		jsonHlsBO.setChunk_span(getChunkSpan());
		jsonHlsBO.setMode(getMode());
		jsonHlsBO.setEnc_mode(getEncrypt());
		jsonHlsBO.setDrm_provider(getProvider());
		jsonHlsBO.setPrimary_m3u8(getM3u8Name());
		
		
		
		return jsonHlsBO;
	}
	
	
	
	
}
