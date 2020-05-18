package com.sumavision.bvc.command.group.user;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.sumavision.bvc.command.group.user.decoder.CommandGroupDecoderSchemePO;
import com.sumavision.bvc.command.group.user.layout.player.CommandGroupUserPlayerPO;
import com.sumavision.bvc.command.group.user.layout.scheme.CommandGroupUserLayoutShemePO;
import com.sumavision.bvc.command.group.user.setting.CommandGroupUserSettingPO;
import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * @ClassName: 用户信息<br/>
 * @Description: 普通、专向；会议<br/>
 * @author zsy 
 * @date 2019年9月20日 下午1:06:00
 */
@Entity
@Table(name="BVC_COMMAND_GROUP_USER_INFO")
public class CommandGroupUserInfoPO extends AbstractBasePO {

	private static final long serialVersionUID = 1L;
	
	/** 用户id */
	private Long userId;

	/** 用户名称（便于查看） */
	private String userName;
	
	/** 关联设置 */
	private CommandGroupUserSettingPO setting = new CommandGroupUserSettingPO();
	
	/** 存储的方案 */
	private List<CommandGroupUserLayoutShemePO> layoutSchemes;
	
	/** 各个播放器的配置信息 */
	private List<CommandGroupUserPlayerPO> players;
	
	/** 该用户配置的上屏方案 */
	private List<CommandGroupDecoderSchemePO> decoderSchemes;

	@Column(name = "USER_ID")
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	@Column(name = "USER_NAME")
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@OneToOne(mappedBy = "userInfo", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	public CommandGroupUserSettingPO getSetting() {
		if(setting == null){
			setting = new CommandGroupUserSettingPO();
			setting.setUserInfo(this);
		}
		return setting;
	}

	public void setSetting(CommandGroupUserSettingPO setting) {
		this.setting = setting;
	}

	@OneToMany(mappedBy = "userInfo", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	public List<CommandGroupUserLayoutShemePO> getLayoutSchemes() {
		return layoutSchemes;
	}

	public void setLayoutSchemes(List<CommandGroupUserLayoutShemePO> layoutSchemes) {
		this.layoutSchemes = layoutSchemes;
	}
	
	@OneToMany(mappedBy = "userInfo", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	public List<CommandGroupUserPlayerPO> getPlayers() {
		return players;
	}

	public void setPlayers(List<CommandGroupUserPlayerPO> players) {
		this.players = players;
	}
	
	@OneToMany(mappedBy = "userInfo", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	public List<CommandGroupDecoderSchemePO> getDecoderSchemes() {
		return decoderSchemes;
	}

	public void setDecoderSchemes(List<CommandGroupDecoderSchemePO> decoderSchemes) {
		this.decoderSchemes = decoderSchemes;
	}

	/**
	 * 获取该用户正在使用的分屏方案<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月21日 上午11:56:02
	 * @return
	 */
	public CommandGroupUserLayoutShemePO obtainUsingScheme(){
		for(CommandGroupUserLayoutShemePO scheme : this.getLayoutSchemes()){
			if(scheme.getIsUsing()){
				return scheme;
			}
		}
		return null;
	}
	
	/**
	 * 获取该用户正在使用的分屏方案下的播放器<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月19日 上午9:28:39
	 * @return
	 */
	public List<CommandGroupUserPlayerPO> obtainUsingSchemePlayers(){
		CommandGroupUserLayoutShemePO usingScheme = this.obtainUsingScheme();		
		List<CommandGroupUserPlayerPO> usingSchemePlayers = usingScheme.obtainPlayers();
		return usingSchemePlayers;
	}
	
}
