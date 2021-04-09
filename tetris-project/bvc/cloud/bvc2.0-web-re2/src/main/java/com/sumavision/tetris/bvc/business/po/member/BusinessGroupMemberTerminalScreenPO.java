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
 * <p>对应 TerminalPhysicalScreenPO</p>
 * <b>作者:</b>zsy<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年10月27日 上午11:44:54
 */
@Entity
@Table(name="BVC_BUSINESS_GROUP_MEMBER_TERMINAL_SCREEN")
public class BusinessGroupMemberTerminalScreenPO extends AbstractBasePO{
	
	private static final long serialVersionUID = 1L;	
	
	/** 关联成员终端 */
	private BusinessGroupMemberPO member;
	
	/** 关联所使用的音频输出 */
	private BusinessGroupMemberTerminalAudioOutputPO audioOutput;
	
	/** 关联可用的解码通道 */
	private List<BusinessGroupMemberTerminalChannelPO> channels;
	
	/**--------- TerminalPhysicalScreenPO 信息 ------------*/
	
	/** id */
	private Long terminalPhysicalScreenId;
	
	/** 物理屏名称 */
	private String name;
	
	/** 终端id */
	private Long terminalId;
	
	/** 音频输出id */
	private Long terminalAudioOutputId;
	
	/** 左偏移 */
	private String x;
	
	/** 上偏移 */
	private String y;
	
	/** 宽 */
	private String width;
	
	/** 高 */
	private String height;

	@ManyToOne
	@JoinColumn(name = "MEMBER_ID")
	public BusinessGroupMemberPO getMember() {
		return member;
	}

	public void setMember(BusinessGroupMemberPO member) {
		this.member = member;
	}

	@ManyToOne
	@JoinColumn(name = "AUDIO_OUTPUT_ID")
	public BusinessGroupMemberTerminalAudioOutputPO getAudioOutput() {
		return audioOutput;
	}

	public void setAudioOutput(BusinessGroupMemberTerminalAudioOutputPO audioOutput) {
		this.audioOutput = audioOutput;
	}

	@OneToMany(mappedBy = "screen", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	public List<BusinessGroupMemberTerminalChannelPO> getChannels() {
		return channels;
	}

	public void setChannels(List<BusinessGroupMemberTerminalChannelPO> channels) {
		this.channels = channels;
	}

	@Column(name = "TERMINAL_ID")
	public Long getTerminalId() {
		return terminalId;
	}

	public void setTerminalId(Long terminalId) {
		this.terminalId = terminalId;
	}

	public Long getTerminalAudioOutputId() {
		return terminalAudioOutputId;
	}

	public void setTerminalAudioOutputId(Long terminalAudioOutputId) {
		this.terminalAudioOutputId = terminalAudioOutputId;
	}

	public Long getTerminalPhysicalScreenId() {
		return terminalPhysicalScreenId;
	}

	public void setTerminalPhysicalScreenId(Long terminalPhysicalScreenId) {
		this.terminalPhysicalScreenId = terminalPhysicalScreenId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getX() {
		return x;
	}

	public void setX(String x) {
		this.x = x;
	}

	public String getY() {
		return y;
	}

	public void setY(String y) {
		this.y = y;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}
}
