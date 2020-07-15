package com.sumavision.bvc.control.device.command.group.vo.user;

import java.util.ArrayList;
import java.util.List;
import com.sumavision.bvc.command.group.user.layout.player.CommandGroupUserPlayerPO;
import com.sumavision.bvc.command.group.user.layout.scheme.CommandGroupUserLayoutShemePO;
import com.sumavision.tetris.bvc.page.PageTaskPO;

public class CommandGroupUserLayoutShemeVO {

	/** 名称 */
	private String name;
	
	/** 播放器分屏布局 */
	private String playerSplitLayout;

	/** 各个播放器的配置信息 */
	private List<CommandGroupUserPlayerSettingVO> players;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPlayerSplitLayout() {
		return playerSplitLayout;
	}

	public void setPlayerSplitLayout(String playerSplitLayout) {
		this.playerSplitLayout = playerSplitLayout;
	}

	public List<CommandGroupUserPlayerSettingVO> getPlayers() {
		return players;
	}

	public void setPlayers(List<CommandGroupUserPlayerSettingVO> players) {
		this.players = players;
	}

	@Deprecated
	private CommandGroupUserLayoutShemeVO setPlayersByPO(List<CommandGroupUserPlayerPO> players) {
		this.setPlayers(new ArrayList<CommandGroupUserPlayerSettingVO>()); 
		if(players == null) return this;
		for(CommandGroupUserPlayerPO player : players){
			this.getPlayers().add(new CommandGroupUserPlayerSettingVO().set(player));
		}
		return this;
	}

	@Deprecated
	public CommandGroupUserLayoutShemeVO set(CommandGroupUserLayoutShemePO schemePO){
		this.setName(schemePO.getName());
		this.setPlayerSplitLayout(String.valueOf(schemePO.getPlayerSplitLayout().getId()));
		this.setPlayersByPO(schemePO.obtainPlayers());
		
		return this;
	}

	private CommandGroupUserLayoutShemeVO setPlayersByPageTasks(List<PageTaskPO> pageTasks) {
		this.setPlayers(new ArrayList<CommandGroupUserPlayerSettingVO>()); 
		if(pageTasks == null) return this;
		for(PageTaskPO pageTask : pageTasks){
			this.getPlayers().add(new CommandGroupUserPlayerSettingVO().set(pageTask));
		}
		//TODO:把剩下的补全，最好改成不用补全
		return this;
	}
	
	public CommandGroupUserLayoutShemeVO set(CommandGroupUserLayoutShemePO schemePO, List<PageTaskPO> pageTasks){
		this.setName(schemePO.getName());
		this.setPlayerSplitLayout(String.valueOf(schemePO.getPlayerSplitLayout().getId()));
		this.setPlayersByPageTasks(pageTasks);
		
		return this;
	}
	
}
