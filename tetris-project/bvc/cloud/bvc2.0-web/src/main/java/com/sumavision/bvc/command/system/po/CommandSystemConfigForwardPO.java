package com.sumavision.bvc.command.system.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.sumavision.bvc.command.group.enumeration.ForwardDstType;
import com.sumavision.bvc.command.group.enumeration.ForwardSrcType;
import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * @ClassName: 系统级转发配置
 * @Description 包含音视频，目的必须是同一个角色，源可以是不同的角色
 * @author zsy
 * @date 2019年10月10日
 */
@Entity
@Table(name="BVC_COMMAND_SYSTEM_CONFIG_FORWARD")
public class CommandSystemConfigForwardPO extends AbstractBasePO{
	
	private static final long serialVersionUID = 1L;
	
	/** 转发视频源类型：角色/无 */
	private ForwardSrcType forwardSrcVideoType;
	
	/** 转发音频源类型：角色/无 */
	private ForwardSrcType forwardSrcAudioType;
	
	/***********
	 * ForwardSrcType为ROLE时 *
	 **********/
	
	/** 当forwardSrcVideoType为ROLE时视频角色id */
	private Long srcVideoRoleId;

	/** 当forwardSrcVideoType为ROLE时视频角色名称 */
	private String srcVideoRoleName;
	
	/** 当forwardSrcAudioType为ROLE时音频角色id */
	private Long srcAudioRoleId;

	/** 当forwardSrcAudioType为ROLE时音频角色名称 */
	private String srcAudioRoleName;	
	
	
	
	/** 转发目的类型：角色/用户/设备 */
	private ForwardDstType forwardDstType;
	
	/***********
	 * forwardDstType为ROLE时 *
	 **********/
	
	/** 当forwardDstType为ROLE时存角色id */
	private Long dstRoleId;

	/** 当forwardDstType为ROLE时存角色名称 */
	private String dstRoleName;
	
	/** 关联配置 */
	private CommandSystemConfigPO config;

	@Enumerated(value = EnumType.STRING)
	@Column(name = "FORWARD_SRC_VIDEO_TYPE")
	public ForwardSrcType getForwardSrcVideoType() {
		return forwardSrcVideoType;
	}

	public void setForwardSrcVideoType(ForwardSrcType forwardSrcVideoType) {
		this.forwardSrcVideoType = forwardSrcVideoType;
	}

	@Enumerated(value = EnumType.STRING)
	@Column(name = "FORWARD_SRC_AUDIO_TYPE")
	public ForwardSrcType getForwardSrcAudioType() {
		return forwardSrcAudioType;
	}

	public void setForwardSrcAudioType(ForwardSrcType forwardSrcAudioType) {
		this.forwardSrcAudioType = forwardSrcAudioType;
	}

	@Column(name = "SRC_VIDEO_ROLE_ID")
	public Long getSrcVideoRoleId() {
		return srcVideoRoleId;
	}

	public void setSrcVideoRoleId(Long srcVideoRoleId) {
		this.srcVideoRoleId = srcVideoRoleId;
	}

	@Column(name = "SRC_VIDEO_ROLE_NAME")
	public String getSrcVideoRoleName() {
		return srcVideoRoleName;
	}

	public void setSrcVideoRoleName(String srcVideoRoleName) {
		this.srcVideoRoleName = srcVideoRoleName;
	}

	@Column(name = "SRC_AUDIO_ROLE_ID")
	public Long getSrcAudioRoleId() {
		return srcAudioRoleId;
	}

	public void setSrcAudioRoleId(Long srcAudioRoleId) {
		this.srcAudioRoleId = srcAudioRoleId;
	}

	@Column(name = "SRC_AUDIO_ROLE_NAME")
	public String getSrcAudioRoleName() {
		return srcAudioRoleName;
	}

	public void setSrcAudioRoleName(String srcAudioRoleName) {
		this.srcAudioRoleName = srcAudioRoleName;
	}

	@Enumerated(value = EnumType.STRING)
	@Column(name = "FORWARD_DST_TYPE")
	public ForwardDstType getForwardDstType() {
		return forwardDstType;
	}

	public void setForwardDstType(ForwardDstType forwardDstType) {
		this.forwardDstType = forwardDstType;
	}

	@Column(name = "DST_ROLE_ID")
	public Long getDstRoleId() {
		return dstRoleId;
	}

	public void setDstRoleId(Long dstRoleId) {
		this.dstRoleId = dstRoleId;
	}

	@Column(name = "DST_ROLE_NAME")
	public String getDstRoleName() {
		return dstRoleName;
	}

	public void setDstRoleName(String dstRoleName) {
		this.dstRoleName = dstRoleName;
	}

	@ManyToOne
	@JoinColumn(name = "CONFIG_ID")
	public CommandSystemConfigPO getConfig() {
		return config;
	}

	public void setConfig(CommandSystemConfigPO config) {
		this.config = config;
	}

	
}
