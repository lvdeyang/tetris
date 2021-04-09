package com.sumavision.tetris.bvc.page;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.sumavision.bvc.command.group.user.layout.scheme.PlayerSplitLayout;
import com.sumavision.tetris.bvc.business.group.GroupMemberType;
import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * 播放任务分页信息<br/>
 * <p>详细描述</p>
 * <b>作者:</b>zsy<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年5月27日 上午8:42:42
 */
@Entity
@Table(name="TETRIS_BVC_PAGE_INFO")
public class PageInfoPO extends AbstractBasePO {

	private static final long serialVersionUID = 1L;
	
	/** 类型：用户、会场、设备也要记录为会场（这个字段没什么用？） */
	private GroupMemberType groupMemberType;
	
	/** 隶属用户id/设备id/会场id:"hall-id" */
	private String originId;
	
	/** 隶属终端 */
	private Long terminalId;
	
	/** 隶属终端设备（暂时没用） */
	private Long terminalBundleId;
	
	/** 隶属屏幕(虚拟设备中的屏幕) */
	private Long terminalPhysicalScreenId;

	/** 当前所在页数，从1开始 */
	private Integer currentPage = 1;
	
	/** 当前每页个数，默认1 */
	private Integer pageSize = 1;
	
	/** 播放器分屏布局，主要针对QT客户端 */
	private PlayerSplitLayout playerSplitLayout;

	/** 关联分页任务 */
	private List<PageTaskPO> pageTasks;

	@Enumerated(value = EnumType.STRING)
	@Column(name = "GROUP_MEMBER_TYPE")
	public GroupMemberType getGroupMemberType() {
		return groupMemberType;
	}

	public void setGroupMemberType(GroupMemberType groupMemberType) {
		this.groupMemberType = groupMemberType;
	}

	@Column(name = "ORIGIN_ID")
	public String getOriginId() {
		return originId;
	}

	public void setOriginId(String originId) {
		this.originId = originId;
	}

	@Column(name = "TERMINAL_ID")
	public Long getTerminalId() {
		return terminalId;
	}

	public void setTerminalId(Long terminalId) {
		this.terminalId = terminalId;
	}

	@Column(name = "CURRENT_PAGE")
	public Integer getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}

	@Column(name = "PAGE_SIZE")
	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	@Enumerated(value = EnumType.STRING)
	@Column(name = "PLAYER_SPLIT_LAYOUT")
	public PlayerSplitLayout getPlayerSplitLayout() {
		return playerSplitLayout;
	}

	public void setPlayerSplitLayout(PlayerSplitLayout playerSplitLayout) {
		this.playerSplitLayout = playerSplitLayout;
	}

	@OneToMany(mappedBy = "pageInfo", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	public List<PageTaskPO> getPageTasks() {
		return pageTasks;
	}

	public void setPageTasks(List<PageTaskPO> pageTasks) {
		this.pageTasks = pageTasks;
	}
	
	public PageInfoPO() {}
			
	public PageInfoPO(String originId, Long terminalId, GroupMemberType groupMemberType) {
		this.originId = originId;
		this.terminalId = terminalId;
		this.groupMemberType = groupMemberType;
	}
	
	/**
	 * 获取总页数。总数为0时，总页数为1<br/>
	 * <p>考虑优化this.getTasks()</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月3日 下午3:16:31
	 * @return
	 */
	public int obtainTotalPageCount(){
		if(pageTasks==null || pageTasks.size()==0) return 1;
		int totalPageCount = pageTasks.size()/pageSize;
		int m = pageTasks.size()%pageSize;
		if(m > 0) totalPageCount++;
		return totalPageCount;
	}	
}
