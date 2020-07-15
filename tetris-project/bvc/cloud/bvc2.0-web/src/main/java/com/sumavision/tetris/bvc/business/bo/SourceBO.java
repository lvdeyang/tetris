package com.sumavision.tetris.bvc.business.bo;

import com.suma.venus.resource.pojo.BundlePO;
import com.sumavision.bvc.resource.dto.ChannelSchemeDTO;
import com.sumavision.tetris.bvc.business.BusinessInfoType;

/**
 * 描述源<br/>
 * <p>详细描述</p>
 * <b>作者:</b>zsy<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年7月2日 下午3:50:16
 */
public class SourceBO {
	
	ChannelSchemeDTO videoSource;
	
	ChannelSchemeDTO audioSource;
	
	BundlePO videoBundle;
	
	BundlePO audioBundle;

	BusinessInfoType businessInfoType;
	
	String businessId;
	
	Long memberId;
	
	String srcName;
	
	String srcCode;

	public ChannelSchemeDTO getVideoSource() {
		return videoSource;
	}

	public SourceBO setVideoSource(ChannelSchemeDTO videoSource) {
		this.videoSource = videoSource;
		return this;
	}

	public ChannelSchemeDTO getAudioSource() {
		return audioSource;
	}

	public SourceBO setAudioSource(ChannelSchemeDTO audioSource) {
		this.audioSource = audioSource;
		return this;
	}
	
	public BundlePO getVideoBundle() {
		return videoBundle;
	}

	public SourceBO setVideoBundle(BundlePO videoBundle) {
		this.videoBundle = videoBundle;
		return this;
	}

	public BundlePO getAudioBundle() {
		return audioBundle;
	}

	public SourceBO setAudioBundle(BundlePO audioBundle) {
		this.audioBundle = audioBundle;
		return this;
	}

	public BusinessInfoType getBusinessInfoType() {
		return businessInfoType;
	}

	public SourceBO setBusinessInfoType(BusinessInfoType businessInfoType) {
		this.businessInfoType = businessInfoType;
		return this;
	}

	public String getBusinessId() {
		return businessId;
	}

	public SourceBO setBusinessId(String businessId) {
		this.businessId = businessId;
		return this;
	}

	public Long getMemberId() {
		return memberId;
	}

	public SourceBO setMemberId(Long memberId) {
		this.memberId = memberId;
		return this;
	}

	public String getSrcName() {
		return srcName;
	}

	public SourceBO setSrcName(String srcName) {
		this.srcName = srcName;
		return this;
	}

	public String getSrcCode() {
		return srcCode;
	}

	public SourceBO setSrcCode(String srcCode) {
		this.srcCode = srcCode;
		return this;
	}
	
}
