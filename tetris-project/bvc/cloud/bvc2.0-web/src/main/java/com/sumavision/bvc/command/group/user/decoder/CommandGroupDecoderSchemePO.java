package com.sumavision.bvc.command.group.user.decoder;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.sumavision.bvc.command.group.user.CommandGroupUserInfoPO;
import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * @ClassName: 上屏方案<br/>
 * @Description: <br/>
 * @author zsy
 * @date 2020年5月13日 下午1:06:00
 */
@Entity
@Table(name="BVC_COMMAND_GROUP_DECODER_SCHEME")
public class CommandGroupDecoderSchemePO extends AbstractBasePO {

	private static final long serialVersionUID = 1L;

	/** 名称 */
	private String name;
	
	/** 绑定的上屏设备 */
	private List<CommandGroupDecoderScreenPO> screens;
	
	/** 关联用户信息 */
	private CommandGroupUserInfoPO userInfo;

	@Column(name = "NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@OneToMany(mappedBy = "scheme", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	public List<CommandGroupDecoderScreenPO> getScreens() {
		return screens;
	}

	public void setScreens(List<CommandGroupDecoderScreenPO> screens) {
		this.screens = screens;
	}
	
	@ManyToOne
	@JoinColumn(name = "USER_INFO_ID")
	public CommandGroupUserInfoPO getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(CommandGroupUserInfoPO userInfo) {
		this.userInfo = userInfo;
	}

//	public CommandGroupDecoderSchemePO set(PlayerBundleBO entity){
//		this.setCode(entity.getUsername());
//		this.setUsername(entity.getUsername());
//		this.setPassword(entity.getPassword());
//		this.setBundleId(entity.getBundleId());
//		this.setBundleName(entity.getBundleName());
//		this.setBundleType(entity.getBundleType());
//		this.setLayerId(entity.getAccessLayerId());
//		this.setRegisterLayerIp(entity.getAccessLayerIp()==null?"0.0.0.0":entity.getAccessLayerIp());
//		this.setRegisterLayerPort(entity.getAccessLayerPort()==null?"0":entity.getAccessLayerPort().toString());
//	
//		List<String> channelIds = entity.getChannelIds();
//		for(String channelId:channelIds){
//			if(channelId.startsWith("VenusVideoOut")){
//				this.setVideoChannelId(channelId);
//				this.setVideoBaseType("VenusVideoOut");
//			}else if(channelId.startsWith("VenusAudioOut")){
//				this.setAudioChannelId(channelId);
//				this.setAudioBaseType("VenusAudioOut");
//			}
//		}
//		return this;
//	}
//	
//	/**
//	 * 释放播放器<br/>
//	 * <p>仅设置值，还需要额外从member解除对该player的关联，并save操作</p>
//	 * <b>作者:</b>zsy<br/>
//	 * <b>版本：</b>1.0<br/>
//	 * <b>日期：</b>2019年11月11日 下午3:34:05
//	 * @return
//	 */
//	public CommandGroupDecoderSchemePO setFree(){
//		this.setPlayerBusinessType(SchemeBusinessType.NONE);
//		this.setBusinessId(null);
//		this.setBusinessName(null);
//		this.setPlayUrl(null);
//		this.setOsdId(null);
//		this.setOsdName(null);
//		this.setMember(null);
//		return this;
//	}
//	
//	/** 该播放器是否正在播放文件 */
//	public boolean playingFile(){
//		SchemeBusinessType businessType = this.playerBusinessType;
//		if(SchemeBusinessType.PLAY_FILE.equals(businessType)
//				|| SchemeBusinessType.PLAY_RECORD.equals(businessType)
//				|| SchemeBusinessType.PLAY_COMMAND_RECORD.equals(businessType)
//				|| SchemeBusinessType.COMMAND_FORWARD_FILE.equals(businessType)){
//			return true;
//		}
//		return false;
//	}
	
}
