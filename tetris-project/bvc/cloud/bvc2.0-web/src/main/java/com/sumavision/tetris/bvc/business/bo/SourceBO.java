package com.sumavision.tetris.bvc.business.bo;

import com.suma.venus.resource.pojo.BundlePO;
import com.sumavision.bvc.resource.dto.ChannelSchemeDTO;
import com.sumavision.tetris.bvc.business.BusinessInfoType;
import com.sumavision.tetris.bvc.model.agenda.AgendaForwardType;
import com.sumavision.tetris.bvc.model.agenda.AgendaSourceType;

/**
 * 描述源<br/>
 * <p>详细描述</p>
 * <b>作者:</b>zsy<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年7月2日 下午3:50:16
 */
public class SourceBO {
	
	/** 源类型 */
	private AgendaSourceType sourceType;
	
	ChannelSchemeDTO videoSource;
	
	ChannelSchemeDTO audioSource;
	
	BundlePO videoBundle;
	
	BundlePO audioBundle;

	BusinessInfoType businessInfoType;
	
	String businessId;
	
	AgendaForwardType agendaForwardType;
	
	/** 用户userId/设备bundleId/文件id/合屏uuid */
	String srcId;

	Long srcMemberId;
	
	String srcName;
	
	String srcCode;
	
	/**  音频源信息 */
	String srcAudioId;

	Long srcAudioMemberId;
	
	String srcAudioName;
	
	String srcAudioCode;

	public AgendaSourceType getSourceType() {
		return sourceType;
	}

	public SourceBO setSourceType(AgendaSourceType sourceType) {
		this.sourceType = sourceType;
		return this;
	}

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
	
	public AgendaForwardType getAgendaForwardType() {
		return agendaForwardType;
	}

	public SourceBO setAgendaForwardType(AgendaForwardType agendaForwardType) {
		this.agendaForwardType = agendaForwardType;
		return this;
	}

	public String getSrcId() {
		return srcId;
	}

	public SourceBO setSrcId(String srcId) {
		this.srcId = srcId;
		return this;
	}

	public Long getSrcMemberId() {
		return srcMemberId;
	}

	public SourceBO setSrcMemberId(Long memberId) {
		this.srcMemberId = memberId;
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

	public String getSrcAudioId() {
		return srcAudioId;
	}

	public SourceBO setSrcAudioId(String srcAudioId) {
		this.srcAudioId = srcAudioId;
		return this;
	}

	public Long getSrcAudioMemberId() {
		return srcAudioMemberId;
	}

	public SourceBO setSrcAudioMemberId(Long srcAudioMemberId) {
		this.srcAudioMemberId = srcAudioMemberId;
		return this;
	}

	public String getSrcAudioName() {
		return srcAudioName;
	}

	public SourceBO setSrcAudioName(String srcAudioName) {
		this.srcAudioName = srcAudioName;
		return this;
	}

	public String getSrcAudioCode() {
		return srcAudioCode;
	}

	public SourceBO setSrcAudioCode(String srcAudioCode) {
		this.srcAudioCode = srcAudioCode;
		return this;
	}
	
}
