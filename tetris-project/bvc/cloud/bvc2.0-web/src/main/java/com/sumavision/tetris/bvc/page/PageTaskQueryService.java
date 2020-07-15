package com.sumavision.tetris.bvc.page;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 
 * PageTaskQueryService<br/>
 * <p>分页信息查询</p>
 * <b>作者:</b>zsy<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年7月15日 上午11:47:09
 */
@Service
public class PageTaskQueryService {
	
	@Autowired
	private PageInfoDAO pageInfoDao;
	
	/**
	 * 查询用户终端当前的每页显示个数<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月15日 上午11:51:24
	 * @param originId 用户id
	 * @param terminalId 终端id
	 * @return 每页个数
	 */
	public int queryCurrentPageSize(String originId, Long terminalId){
		PageInfoPO pageInfo = pageInfoDao.findByOriginIdAndTerminalId(originId, terminalId);
		return pageInfo.getPageSize();
	}
	
	/**
	 * 查询用户终端当前显示的任务列表<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月15日 上午11:54:20
	 * @param originId
	 * @param terminalId
	 * @return
	 */
	public List<PageTaskPO> queryCurrentPageTasks(String originId, Long terminalId){
		PageInfoPO pageInfo = pageInfoDao.findByOriginIdAndTerminalId(originId, terminalId);
		return queryCurrentPageTasks(pageInfo);
	}

	/**
	 * 查询当前显示的任务列表<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月15日 下午1:13:16
	 * @param pageInfo
	 * @return
	 */
	public List<PageTaskPO> queryCurrentPageTasks(PageInfoPO pageInfo){
		List<PageTaskPO> tasks = pageInfo.getPageTasks();
		List<PageTaskPO> currentPageTasks = new ArrayList<PageTaskPO>();
		for(PageTaskPO task : tasks){
			if(task.isShowing()) currentPageTasks.add(task);
		}
		return currentPageTasks;
	}
	
	/**
	 * 查询用户终端当前显示的某个任务<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月15日 下午1:10:24
	 * @param originId
	 * @param terminalId
	 * @param locationIndex 分屏序号
	 * @return
	 */
	public PageTaskPO queryPageTask(String originId, Long terminalId, int locationIndex) {
		List<PageTaskPO> tasks = queryCurrentPageTasks(originId, terminalId);
		return queryPageTask(tasks, locationIndex);
	}
	
	/**
	 * 查询任务列表中的某个任务<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月15日 下午1:11:08
	 * @param tasks
	 * @param locationIndex
	 * @return
	 */
	public PageTaskPO queryPageTask(List<PageTaskPO> tasks, int locationIndex) {
		for(PageTaskPO task : tasks){
			if(locationIndex == task.getLocationIndex()){
				return task;
			}
		}
		return null;
	}
	
}
