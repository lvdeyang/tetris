package com.sumavision.tetris.bvc.business.po.member;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * 
 * 类型概述<br/>
 * <p>对应 TerminalAudioOutputPO</p>
 * <b>作者:</b>zsy<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年11月6日 上午10:29:03
 */
@Entity
@Table(name="BVC_BUSINESS_GROUP_MEMBER_TERMINAL_AUDIO_OUTPUT")
public class BusinessGroupMemberTerminalAudioOutputPO extends AbstractBasePO{
	
	private static final long serialVersionUID = 1L;	
	
	/** 关联成员终端 */
	private BusinessGroupMemberPO member;
	
	/** 关联可用的音频解码通道 */
	private List<BusinessGroupMemberTerminalChannelPO> channels;
	
	/** 关联对应的屏幕 */
	private List<BusinessGroupMemberTerminalScreenPO> screens;
	
	/**--------- TerminalAudioOutputPO 信息 ------------*/
	
	/** TerminalAudioOutputPO的id*/
	private Long terminalAudioOutputId;
	
	/** 终端id */
	private Long terminalId;
	
	/** 音频输出名称 */
	private String name;
	
	@ManyToOne
	@JoinColumn(name = "MEMBER_ID")
	public BusinessGroupMemberPO getMember() {
		return member;
	}

	public void setMember(BusinessGroupMemberPO member) {
		this.member = member;
	}

	@OneToMany(mappedBy = "audioOutput", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	public List<BusinessGroupMemberTerminalChannelPO> getChannels() {
		return channels;
	}

	public void setChannels(List<BusinessGroupMemberTerminalChannelPO> channels) {
		this.channels = channels;
	}

	@OneToMany(mappedBy = "audioOutput", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	public List<BusinessGroupMemberTerminalScreenPO> getScreens() {
		return screens;
	}

	public void setScreens(List<BusinessGroupMemberTerminalScreenPO> screens) {
		this.screens = screens;
	}
	
	@Column(name="TERMINAL_AUDIOOUTPUT_ID")
	public Long getTerminalAudioOutputId() {
		return terminalAudioOutputId;
	}

	public void setTerminalAudioOutputId(Long terminalAudioOutputId) {
		this.terminalAudioOutputId = terminalAudioOutputId;
	}

	@Column(name = "TERMINAL_ID")
	public Long getTerminalId() {
		return terminalId;
	}

	public void setTerminalId(Long terminalId) {
		this.terminalId = terminalId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
