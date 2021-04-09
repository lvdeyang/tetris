package com.sumavision.tetris.bvc.business.po.member;

import java.util.Comparator;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.alibaba.fastjson.JSONObject;
import com.sumavision.bvc.resource.dto.ChannelSchemeDTO;
import com.sumavision.tetris.bvc.model.terminal.channel.TerminalChannelType;
import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * 
 * 类型概述<br/>
 * <p>对应 TerminalChannelPO</p>
 * <b>作者:</b>zsy<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年10月27日 上午11:45:42
 */
@Entity
@Table(name="BVC_BUSINESS_GROUP_MEMBER_CHANNEL")
public class BusinessGroupMemberTerminalChannelPO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;
	
	/** 关联成员 */
	private BusinessGroupMemberPO member;
	
	/** 所作用的屏幕 */
	private BusinessGroupMemberTerminalScreenPO screen;
	
	/** 所拥有的的真实通道 */
	private List<BusinessGroupMemberTerminalBundleChannelPO> memberTerminalBundleChannels;
	
	/** 一个音频通道关联多个视频通道：视频编码所附带的音频编码；音频解码需要接哪几个视频解码中出现的音频 */
	/** 仅音频编码通道有效：该音频通道关联的视频通道 */
	private List<BusinessGroupMemberTerminalChannelPO> videoEncodeChannels;
	
	/** 仅视频编码通道有效：该视频通道关联的音频通道 */
	private BusinessGroupMemberTerminalChannelPO audioEncodeChannel;
	
	/** 仅视音解码通道有效：所属的音频输出 */
	private BusinessGroupMemberTerminalAudioOutputPO audioOutput;
	
	/**------- TerminalChannelPO 信息 ---------*/
	
	/** id */
	private Long terminalChannelId;

	/** 起一个别名 */
	private String terminalChannelname;
	
	/** 通道编解码类型 */
	private TerminalChannelType terminalChannelType;
	
	/** 隶属终端id */
	private Long terminalId;
	
	@ManyToOne
	@JoinColumn(name = "MEMBER_ID")
	public BusinessGroupMemberPO getMember() {
		return member;
	}
	
	public void setMember(BusinessGroupMemberPO member) {
		this.member = member;
	}
	
	@ManyToOne
	@JoinColumn(name = "MEMBER_SCREEN_ID")
	public BusinessGroupMemberTerminalScreenPO getScreen() {
		return screen;
	}

	public void setScreen(BusinessGroupMemberTerminalScreenPO screen) {
		this.screen = screen;
	}

	@OneToMany(mappedBy = "memberTerminalChannel", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	public List<BusinessGroupMemberTerminalBundleChannelPO> getMemberTerminalBundleChannels() {
		return memberTerminalBundleChannels;
	}

	public void setMemberTerminalBundleChannels(
			List<BusinessGroupMemberTerminalBundleChannelPO> memberTerminalBundleChannels) {
		this.memberTerminalBundleChannels = memberTerminalBundleChannels;
	}
	
	@OneToMany(mappedBy = "audioEncodeChannel", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	public List<BusinessGroupMemberTerminalChannelPO> getVideoEncodeChannels() {
		return videoEncodeChannels;
	}

	public void setVideoEncodeChannels(List<BusinessGroupMemberTerminalChannelPO> videoEncodeChannels) {
		this.videoEncodeChannels = videoEncodeChannels;
	}

	@ManyToOne
	@JoinColumn(name = "AUDIO_CHANNEL_ID")
	public BusinessGroupMemberTerminalChannelPO getAudioEncodeChannel() {
		return audioEncodeChannel;
	}

	public void setAudioEncodeChannel(BusinessGroupMemberTerminalChannelPO audioEncodeChannel) {
		this.audioEncodeChannel = audioEncodeChannel;
	}

	@ManyToOne
	@JoinColumn(name = "AUDIO_OUTPUT_ID")
	public BusinessGroupMemberTerminalAudioOutputPO getAudioOutput() {
		return audioOutput;
	}

	public void setAudioOutput(BusinessGroupMemberTerminalAudioOutputPO audioOutput) {
		this.audioOutput = audioOutput;
	}

	public Long getTerminalChannelId() {
		return terminalChannelId;
	}

	public void setTerminalChannelId(Long terminalChannelId) {
		this.terminalChannelId = terminalChannelId;
	}

	public String getTerminalChannelname() {
		return terminalChannelname;
	}

	public void setTerminalChannelname(String terminalChannelname) {
		this.terminalChannelname = terminalChannelname;
	}

	@Enumerated(value = EnumType.STRING)
	public TerminalChannelType getTerminalChannelType() {
		return terminalChannelType;
	}

	public void setTerminalChannelType(TerminalChannelType terminalChannelType) {
		this.terminalChannelType = terminalChannelType;
	}

	public Long getTerminalId() {
		return terminalId;
	}

	public void setTerminalId(Long terminalId) {
		this.terminalId = terminalId;
	}

	/**
	 * @ClassName: 通道排序器，id为 类型-编号， 按照编号从小到大排列<br/> 
	 * @author lvdeyang
	 * @date 2018年11月12日 上午8:36:10 
	 */
	public static final class ChannelComparatorFromPO implements Comparator<BusinessGroupMemberTerminalChannelPO>{
		@Override
		public int compare(BusinessGroupMemberTerminalChannelPO o1, BusinessGroupMemberTerminalChannelPO o2) {
			
			/*long id1 = Long.parseLong(o1.getChannelId().split("_")[1]);
			long id2 = Long.parseLong(o2.getChannelId().split("_")[1]);
			
			if(id1 > id2){
				return 1;
			}
			if(id1 == id2){
				return 0;
			}*/
			return -1;
		}
	}
	
	/**
	 * @ClassName: 通道排序器，id为 类型-编号， 按照编号从小到大排列<br/> 
	 * @author lvdeyang
	 * @date 2018年8月27日 上午8:36:10 
	 */
	public static final class ChannelComparatorFromJSON implements Comparator<JSONObject>{
		@Override
		public int compare(JSONObject o1, JSONObject o2) {
			
			long id1 = Long.parseLong(o1.getString("id").split("_")[1]);
			long id2 = Long.parseLong(o2.getString("id").split("_")[1]);
			
			if(id1 > id2){
				return 1;
			}
			if(id1 == id2){
				return 0;
			}
			return -1;
		}
	}
	
	/**
	 * @ClassName: 通道排序器，id为 类型-编号， 按照编号从小到大排列<br/> 
	 * @author lvdeyang
	 * @date 2018年10月23日 上午8:36:10 
	 */
	public static final class ChannelComparatorFromDTO implements Comparator<ChannelSchemeDTO>{
		@Override
		public int compare(ChannelSchemeDTO o1, ChannelSchemeDTO o2) {
			
			long id1 = Long.parseLong(o1.getChannelId().split("_")[1]);
			long id2 = Long.parseLong(o2.getChannelId().split("_")[1]);
			
			if(id1 > id2){
				return 1;
			}
			if(id1 == id2){
				return 0;
			}
			return -1;
		}
	}
	
}
