package com.sumavision.tetris.bvc.business.bo;

import java.util.ArrayList;
import java.util.List;
import com.sumavision.tetris.bvc.page.PageTaskPO;

/**
 * 
 * 类型概述<br/>
 * <p>详细描述</p>
 * <b>作者:</b>zsy<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年7月1日 上午10:03:28
 */
public class MemberChangedTaskBO {

	//	Map<CommonForwardPO, CommonForwardPO> addVideoAudioMap
//	List<CommonForwardPO> addForwards = new ArrayList<CommonForwardPO>();
	
	private List<PageTaskPO> addTasks = new ArrayList<PageTaskPO>();
	
	private List<PageTaskPO> removeTasks = new ArrayList<PageTaskPO>();
	
//	public List<CommonForwardPO> getAddForwards() {
//		return addForwards;
//	}
//
//	public MemberChangedTaskBO setAddForwards(List<CommonForwardPO> addForwards) {
//		this.addForwards = addForwards;
//		return this;
//	}

	public List<PageTaskPO> getAddTasks() {
		return addTasks;
	}

	public MemberChangedTaskBO setAddTasks(List<PageTaskPO> addTasks) {
		this.addTasks = addTasks;
		return this;
	}

	public List<PageTaskPO> getRemoveTasks() {
		return removeTasks;
	}

	public MemberChangedTaskBO setRemoveTasks(List<PageTaskPO> removeTasks) {
		this.removeTasks = removeTasks;
		return this;
	}
	
}
