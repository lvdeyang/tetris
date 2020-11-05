package com.sumavision.tetris.bvc.business.bo;

import com.suma.venus.resource.pojo.BundlePO;
import com.sumavision.bvc.resource.dto.ChannelSchemeDTO;
import com.sumavision.tetris.bvc.business.BusinessInfoType;
import com.sumavision.tetris.bvc.business.OriginType;
import com.sumavision.tetris.bvc.business.group.GroupMemberType;
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
	
	/** 来源：内部/外部系统 */
	private OriginType originType = OriginType.INNER;
	
	/** 类型：设备/用户等 */
	private GroupMemberType groupMemberType;
	
	/** 源成员uuid，级联时使用 */
	private String memberUuid;
	
	/** 视频源类型 */
	private AgendaSourceType videoSourceType;

	/** 音频源类型 */
	private AgendaSourceType audioSourceType;
	
	ChannelSchemeDTO videoSourceChannel;
	
	ChannelSchemeDTO audioSourceChannel;
	
	BundlePO videoBundle;
	
	BundlePO audioBundle;

	BusinessInfoType businessInfoType;
	
	String businessId;
	
	AgendaForwardType agendaForwardType;
	
	/** 用户userId/设备bundleId/文件id/合屏uuid */
	String srcVideoId;

	Long srcVideoMemberId;
	
	String srcVideoName;
	
	String srcVideoCode;
	
	/**  音频源信息 */
	String srcAudioId;

	Long srcAudioMemberId;
	
	String srcAudioName;
	
	String srcAudioCode;
	
	public OriginType getOriginType() {
		return originType;
	}

	public SourceBO setOriginType(OriginType originType) {
		this.originType = originType;
		return this;
	}

	public GroupMemberType getGroupMemberType() {
		return groupMemberType;
	}

	public SourceBO setGroupMemberType(GroupMemberType groupMemberType) {
		this.groupMemberType = groupMemberType;
		return this;
	}

	public String getMemberUuid() {
		return memberUuid;
	}

	public SourceBO setMemberUuid(String memberUuid) {
		this.memberUuid = memberUuid;
		return this;
	}

	public AgendaSourceType getVideoSourceType() {
		return videoSourceType;
	}

	public SourceBO setVideoSourceType(AgendaSourceType videoSourceType) {
		this.videoSourceType = videoSourceType;
		return this;
	}

	public AgendaSourceType getAudioSourceType() {
		return audioSourceType;
	}

	public SourceBO setAudioSourceType(AgendaSourceType audioSourceType) {
		this.audioSourceType = audioSourceType;
		return this;
	}

	public ChannelSchemeDTO getVideoSourceChannel() {
		return videoSourceChannel;
	}

	public SourceBO setVideoSourceChannel(ChannelSchemeDTO videoSource) {
		this.videoSourceChannel = videoSource;
		return this;
	}

	public ChannelSchemeDTO getAudioSourceChannel() {
		return audioSourceChannel;
	}

	public SourceBO setAudioSourceChannel(ChannelSchemeDTO audioSource) {
		this.audioSourceChannel = audioSource;
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

	public String getSrcVideoId() {
		return srcVideoId;
	}

	public SourceBO setSrcVideoId(String srcVideoId) {
		this.srcVideoId = srcVideoId;
		return this;
	}

	public Long getSrcVideoMemberId() {
		return srcVideoMemberId;
	}

	public SourceBO setSrcVideoMemberId(Long memberId) {
		this.srcVideoMemberId = memberId;
		return this;
	}

	public String getSrcVideoName() {
		return srcVideoName;
	}

	public SourceBO setSrcVideoName(String srcVideoName) {
		this.srcVideoName = srcVideoName;
		return this;
	}

	public String getSrcVideoCode() {
		return srcVideoCode;
	}

	public SourceBO setSrcVideoCode(String srcVideoCode) {
		this.srcVideoCode = srcVideoCode;
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
