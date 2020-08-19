package com.sumavision.bvc.command.group.user.layout.scheme;

import java.util.Collections;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.sumavision.bvc.command.group.enumeration.SchemeType;
import com.sumavision.bvc.command.group.user.CommandGroupUserInfoPO;
import com.sumavision.bvc.command.group.user.layout.player.CommandGroupUserPlayerPO;
import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * @ClassName: 用户的播放器布局方案<br/>
 * @Description: 包括布局（几分屏），每个分屏绑定的上屏设备<br/>
 * @author zsy 
 * @date 2019年9月20日 下午1:06:00
 */
@Entity
@Table(name="BVC_COMMAND_GROUP_USER_LAYOUT_SCHEME")
public class CommandGroupUserLayoutShemePO extends AbstractBasePO {

	private static final long serialVersionUID = 1L;

	/** 名称 */
	private String name;
	
	/** 记录哪个方案正在使用 */
	private Boolean isUsing;
	
	/** 方案类型：默认/手动创建 */
	private SchemeType type = SchemeType.DEFAULT;
	
	/** 播放器分屏布局 */
	private PlayerSplitLayout playerSplitLayout;

	/** 关联用户信息 */
	private CommandGroupUserInfoPO userInfo;
	
	@Column(name = "NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "IS_USING")
	public Boolean getIsUsing() {
		return isUsing;
	}

	public void setIsUsing(Boolean isUsing) {
		this.isUsing = isUsing;
	}

	@Enumerated(value = EnumType.STRING)
	@Column(name = "SCHEME_TYPE")
	public SchemeType getType() {
		return type;
	}

	public void setType(SchemeType type) {
		this.type = type;
	}

	@Enumerated(value = EnumType.STRING)
	@Column(name = "PLAYER_SPLIT_LAYOUT")
	public PlayerSplitLayout getPlayerSplitLayout() {
		return playerSplitLayout;
	}

	public void setPlayerSplitLayout(PlayerSplitLayout playerSplitLayout) {
		this.playerSplitLayout = playerSplitLayout;
	}

	@ManyToOne
	@JoinColumn(name = "USER_INFO_ID")
	public CommandGroupUserInfoPO getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(CommandGroupUserInfoPO userInfo) {
		this.userInfo = userInfo;
	}

	/**
	 * 获取该方案下的播放器<br/>
	 * <p>取出该用户的所有播放器，按index排序，按分屏数量取出相应个数的播放器</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月19日 上午9:26:16
	 * @return
	 */
	public List<CommandGroupUserPlayerPO> obtainPlayers() {
		
		int playerCount = this.playerSplitLayout.getPlayerCount();
		List<CommandGroupUserPlayerPO> allPlayers = this.getUserInfo().getPlayers();
		Collections.sort(allPlayers, new CommandGroupUserPlayerPO.PlayerComparatorFromIndex());
		
		List<CommandGroupUserPlayerPO> usingSchemePlayers = allPlayers.subList(0, playerCount);
		
		return usingSchemePlayers;
	}
	
}
