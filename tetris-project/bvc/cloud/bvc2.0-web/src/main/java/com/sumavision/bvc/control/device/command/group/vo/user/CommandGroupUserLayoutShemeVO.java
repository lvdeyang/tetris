package com.sumavision.bvc.control.device.command.group.vo.user;

import java.util.ArrayList;
import java.util.List;
import com.sumavision.bvc.command.group.user.layout.player.CommandGroupUserPlayerPO;
import com.sumavision.bvc.command.group.user.layout.scheme.CommandGroupUserLayoutShemePO;
import com.sumavision.tetris.bvc.page.PageInfoPO;
import com.sumavision.tetris.bvc.page.PageTaskPO;

public class CommandGroupUserLayoutShemeVO {

	/** 名称 */
	private String name;
	
	/** 播放器分屏布局 */
	private String playerSplitLayout;
	
	//-------分页新增
	
	/** 总页数 */
	private int total = 1;

	/** 当前所在页数 */
	private int currentPage = 1;

	/** 每页个数 */
	private int pageSize = 1;

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

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
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

	public CommandGroupUserLayoutShemeVO setPlayersByPageTasks(List<PageTaskPO> pageTasks) {
		this.setPlayers(new ArrayList<CommandGroupUserPlayerSettingVO>()); 
		if(pageTasks == null) return this;
		for(PageTaskPO pageTask : pageTasks){
			this.getPlayers().add(new CommandGroupUserPlayerSettingVO().set(pageTask));
		}
		//还缺少空闲的播放器信息，需要额外补全
		return this;
	}
	
}
