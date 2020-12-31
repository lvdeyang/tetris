package com.sumavision.tetris.bvc.page;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.suma.venus.resource.pojo.BundlePO;
import com.sumavision.bvc.command.group.dao.CommandGroupUserInfoDAO;
import com.sumavision.bvc.command.group.user.CommandGroupUserInfoPO;
import com.sumavision.bvc.command.group.user.layout.player.PlayerBusinessType;
import com.sumavision.bvc.command.group.user.layout.scheme.CommandGroupUserLayoutShemePO;
import com.sumavision.bvc.control.device.command.group.vo.user.CommandGroupUserLayoutShemeVO;
import com.sumavision.bvc.control.device.command.group.vo.user.CommandGroupUserPlayerSettingVO;
import com.sumavision.bvc.device.command.bo.MessageSendCacheBO;
import com.sumavision.bvc.device.command.cast.CommandCastServiceImpl;
import com.sumavision.bvc.device.command.common.CommandCommonServiceImpl;
import com.sumavision.bvc.device.group.bo.CodecParamBO;
import com.sumavision.bvc.device.group.bo.ConnectBO;
import com.sumavision.bvc.device.group.bo.ConnectBundleBO;
import com.sumavision.bvc.device.group.bo.DisconnectBundleBO;
import com.sumavision.bvc.device.group.bo.ForwardSetSrcBO;
import com.sumavision.bvc.device.group.bo.LogicBO;
import com.sumavision.bvc.device.group.bo.PassByBO;
import com.sumavision.bvc.device.group.bo.RectBO;
import com.sumavision.bvc.device.group.bo.ScreenBO;
import com.sumavision.bvc.device.group.service.test.ExecuteBusinessProxy;
import com.sumavision.bvc.device.group.service.util.QueryUtil;
import com.sumavision.bvc.resource.dao.ResourceBundleDAO;
import com.sumavision.tetris.bvc.business.BusinessInfoType;
import com.sumavision.tetris.bvc.business.ExecuteStatus;
import com.sumavision.tetris.bvc.business.common.BusinessReturnService;
import com.sumavision.tetris.bvc.business.dao.GroupDAO;
import com.sumavision.tetris.bvc.business.group.GroupMemberType;
import com.sumavision.tetris.bvc.business.group.GroupPO;
import com.sumavision.tetris.bvc.business.terminal.hall.TerminalBundleConferenceHallPermissionDAO;
import com.sumavision.tetris.bvc.business.terminal.hall.TerminalBundleConferenceHallPermissionPO;
import com.sumavision.tetris.bvc.business.terminal.user.TerminalBundleUserPermissionDAO;
import com.sumavision.tetris.bvc.business.terminal.user.TerminalBundleUserPermissionPO;
import com.sumavision.tetris.bvc.model.agenda.AgendaSourceType;
import com.sumavision.tetris.bvc.model.terminal.TerminalBundleDAO;
import com.sumavision.tetris.bvc.model.terminal.TerminalBundlePO;
import com.sumavision.tetris.bvc.model.terminal.TerminalBundleType;
import com.sumavision.tetris.bvc.model.terminal.channel.TerminalChannelDAO;
import com.sumavision.tetris.bvc.model.terminal.channel.TerminalChannelPO;
import com.sumavision.tetris.bvc.model.terminal.layout.LayoutPositionDAO;
import com.sumavision.tetris.bvc.model.terminal.layout.LayoutPositionPO;
import com.sumavision.tetris.bvc.util.TetrisBvcQueryUtil;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.websocket.message.WebsocketMessageService;
import com.sumavision.tetris.websocket.message.WebsocketMessageType;
import com.sumavision.tetris.websocket.message.WebsocketMessageVO;

import lombok.extern.slf4j.Slf4j;

/**
 * CommandSplitPagingServiceImpl<br/>
 * <p>详细描述</p>
 * <b>作者:</b>zsy<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年5月22日 上午10:03:43
 */
@Slf4j
//@Transactional(rollbackFor = Exception.class)
@Service
public class PageTaskService {
	
	@Autowired
	private TerminalChannelDAO terminalChannelDao;
	
	@Autowired
	private CommandGroupUserInfoDAO commandGroupUserInfoDao;
	
	@Autowired
	private LayoutPositionDAO layoutPositionDao;
	
	@Autowired
	private PageInfoDAO pageInfoDao;
	
	@Autowired
	private TerminalBundleDAO terminalBundleDao;
	
	@Autowired
	private TerminalBundleUserPermissionDAO terminalBundleUserPermissionDao;
	
	@Autowired
	private TerminalBundleConferenceHallPermissionDAO terminalBundleConferenceHallPermissionDao;
	
	@Autowired
	private ResourceBundleDAO resourceBundleDao;
	
	@Autowired
	private CommandCommonServiceImpl commandCommonServiceImpl;
	
	@Autowired
	private WebsocketMessageService websocketMessageService;
	
	@Autowired
	private CommandCastServiceImpl commandCastServiceImpl;

	@Autowired
	private TetrisBvcQueryUtil tetrisBvcQueryUtil;

	@Autowired
	private QueryUtil queryUtil;

	@Autowired
	private PageTaskUtil pageTaskUtil;
	
	@Autowired
	private ExecuteBusinessProxy executeBusiness;
	
	@Autowired
	private GroupDAO groupDao;
	
	@Autowired
	private BusinessReturnService businessReturnService;
	
	//重构
	public MessageSendCacheBO notifyUser(CommandGroupUserInfoPO userInfo, PageInfoPO pageInfo, boolean doWebsocket) throws Exception{
		
		/*List<PageTaskPO> pageTasks = getPageTasks(pageInfo, pageInfo.getCurrentPage(), true);
		
		CommandGroupUserLayoutShemePO usingScheme = userInfo.obtainUsingScheme();
		int totalPageNumber = getTotalPageNumber(pageInfo);
		CommandGroupUserLayoutShemeVO schemeVO = generateSchemeVO(usingScheme, pageInfo, pageTasks, totalPageNumber);*/
		CommandGroupUserLayoutShemeVO schemeVO = generateSchemeVO(userInfo, pageInfo);
		
		JSONObject message = new JSONObject();
		message.put("businessType", "refreshPage");
		message.put("pageInfo", schemeVO);
//		log.info("推送" + userInfo.getUserName() + "用户的当前分页layout: " + JSON.toJSONString(schemeVO));
		
		MessageSendCacheBO cache = new MessageSendCacheBO(userInfo.getUserId(), message.toJSONString(), WebsocketMessageType.COMMAND);
	
		//发送消息
		if(doWebsocket){
			if(businessReturnService.getSegmentedExecute()){
				businessReturnService.add(null, cache, "推送" + userInfo.getUserName() + "用户的当前分页layout: " + JSON.toJSONString(schemeVO));
			}else{
				WebsocketMessageVO ws = websocketMessageService.send(userInfo.getUserId(), message.toJSONString(), WebsocketMessageType.COMMAND);
				websocketMessageService.consume(ws.getId());
			}
		}
		
		return cache;
	}
	
//	/**
//	 * 判断添加任务是否支持创建新分页<br/>
//	 * <b>作者:</b>lx<br/>
//	 * <b>版本：</b>1.0<br/>
//	 * <b>日期：</b>2020年11月26日 上午11:51:20
//	 * @param pageInfo
//	 * @param newTasks
//	 * @param removeTasks
//	 * @param allowNewPage
//	 * @throws Exception
//	 */
//	public void allowNewPageByAddAndRemoveTasks(PageInfoPO pageInfo, List<PageTaskPO> newTasks, List<PageTaskPO> removeTasks, Boolean allowNewPage) throws Exception{
//		
//		PageTaskPO jumpTo= newAddTask(pageInfo, newTasks);
//		
//		if(jumpTo != null){
//			if(getPageNumberByIndex(pageInfo, jumpTo.getTaskIndex()) != 1 && !allowNewPage){
//				businessReturnService.getAndRemove();
//				throw new BaseException(StatusCode.FORBIDDEN, "可用的播放器不足");
//			}else{
//				addAndRemoveTasks(pageInfo, newTasks, removeTasks);
//			}
//		}
//	}
	
	public void addAndRemoveTasks(PageInfoPO pageInfo, List<PageTaskPO> newTasks, List<PageTaskPO> removeTasks) throws Exception{
		
		if(newTasks == null) newTasks = new ArrayList<PageTaskPO>();
		
		if(removeTasks == null) removeTasks = new ArrayList<PageTaskPO>();
		
		Integer currentPage = pageInfo.getCurrentPage();
		
		Boolean allowNewPage = true;
		
		//新添加的是同一个groupPO下的任务。
		if(newTasks.size()>0){
			String businessId=newTasks.get(0).getBusinessId();
//			if(businessId.matches("\\d+")){
				Long groupId=Long.parseLong(businessId);
				GroupPO group=groupDao.findOne(groupId);
				if(group!=null&& group.getLocationIndex()!=null){
					int taskIndex=(pageInfo.getCurrentPage()-1)*pageInfo.getPageSize()+group.getLocationIndex();
					for(PageTaskPO newTask:newTasks){
						newTask.setTaskIndex(taskIndex++);
						newTask.setFixedAtPageAndLocation(true);
					}
				}
				//获取group中分页标识字段
				if(group != null && group.getAllowNewPage() != null) allowNewPage = group.getAllowNewPage();
//			}
		}
		
		//获取当前分页下的任务
		List<PageTaskPO> oldTaskPOs = getPageTasks(pageInfo, currentPage, true);
		
		//解绑解码器      //TODO 合并里边的命令 （已完成）
		for(PageTaskPO removeTask : removeTasks){
			commandCastServiceImpl.setCastDevices(removeTask, new ArrayList<String>());
		}
		
//		removeTask
		newRemoveTasks(pageInfo, removeTasks);
		
		
//		addTask
//		List<PageTaskPO> newTaskPOs = new ArrayList<PageTaskPO>();
//		for(PageTaskBO newTaskBO : newTasks){
//			newTaskPOs.add(new PageTaskPO().set(newTaskBO));
//		}
		PageTaskPO jumpTo= newAddTask(pageInfo, newTasks);
		
		//总页数
		int newPageCount = pageInfo.obtainTotalPageCount();
		
		//要跳转到的页数
		int newCurrentPage = 1;
		if(jumpTo != null){
			newCurrentPage = getPageNumberByIndex(pageInfo, jumpTo.getTaskIndex());
			if(newCurrentPage != 1 && !allowNewPage){
				businessReturnService.getAndRemove();
				throw new BaseException(StatusCode.FORBIDDEN, "可用的播放器不足");
			}
		}
		else if(currentPage > newPageCount){
			newCurrentPage = newPageCount;
		}
		else{
			newCurrentPage = currentPage;
		}
		
		pageInfo.setCurrentPage(newCurrentPage);
		
		//持久化
		pageInfoDao.save(pageInfo);
		
//		jumpToPage(pageInfo, newCurrentPage);
		//对于非用户，不分页，直接得到所有的task（210设备可能会有2个）
		List<PageTaskPO> newPageTasks = pageInfo.getPageTasks();
		if(pageInfo.getGroupMemberType().equals(GroupMemberType.MEMBER_USER)){
			newPageTasks = getPageTasks(pageInfo, newCurrentPage, true);
		}
		comparePage(pageInfo, oldTaskPOs, newPageTasks, true); //TODO 需要合并下发命令（已完成）
		
		//notifyUser，后续要改
		GroupMemberType groupMemberType = pageInfo.getGroupMemberType();
		if(GroupMemberType.MEMBER_USER.equals(groupMemberType)){
			Long userId = Long.parseLong(pageInfo.getOriginId());
			CommandGroupUserInfoPO userInfo = commandGroupUserInfoDao.findByUserId(userId);
			notifyUser(userInfo, pageInfo, true);  //TODO 需要合并命令下发（已完成）
		}
		
	}
	
//	/**
//	 * 切换分屏，跳转页数时是否创建新分页<br/>
//	 * <b>作者:</b>lx<br/>
//	 * <b>版本：</b>1.0<br/>
//	 * <b>日期：</b>2020年11月26日 下午2:55:52
//	 * @param pageInfo
//	 * @param toPage
//	 * @param pageSize
//	 * @param allowNewPage
//	 * @throws Exception
//	 */
//	public void allowNewPageByJumpToPageAndChangeSplit(PageInfoPO pageInfo, int toPage, int pageSize, Boolean allowNewPage)throws Exception{
//		
//		List<PageTaskPO> oldTaskPOs = getPageTasks(pageInfo, pageInfo.getCurrentPage(), true);
//		if(oldTaskPOs.size() > pageSize){
//			throw new BaseException(StatusCode.FORBIDDEN, "要切换的分屏的播放器数量不满足");
//		}else{
//			jumpToPageAndChangeSplit(pageInfo, toPage, pageSize);
//		}
//	}
	
	/**
	 * 切换分屏，跳转页数<br/>
	 * <p>跳转页数时，分页大小不需要变化；分页大小改变时，一般跳转到第1页</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月21日 上午10:07:49
	 * @param pageInfo
	 * @param toPage 跳到第几页，切换分屏时一般跳到第1页
	 * @param pageSize 切换分页大小
	 * @throws Exception
	 */
	public void jumpToPageAndChangeSplit(PageInfoPO pageInfo, int toPage, int pageSize) throws Exception{
		
		if(toPage < 1) throw new BaseException(StatusCode.FORBIDDEN, "当前已经在第1页");
		
		int totalPageCount = getTotalPageNumber(pageInfo);
		if(toPage > totalPageCount){
			throw new BaseException(StatusCode.FORBIDDEN, "总页数为" + totalPageCount + "，无法找到第" + toPage + "页");
		}
		
		//获取当前分页下的任务
		List<PageTaskPO> oldTaskPOs = getPageTasks(pageInfo, pageInfo.getCurrentPage(), true);
		
		//设置新的参数，持久化
		pageInfo.setCurrentPage(toPage);
		pageInfo.setPageSize(pageSize);
		pageInfoDao.save(pageInfo);
		
		//获取新分页下的任务
		List<PageTaskPO> newTaskPOs = getPageTasks(pageInfo, toPage, true);
		
		//对比新旧页，下发协议
		comparePage(pageInfo, oldTaskPOs, newTaskPOs, true);
		
		Long userId = Long.parseLong(pageInfo.getOriginId());
		CommandGroupUserInfoPO userInfo = commandGroupUserInfoDao.findByUserId(userId);
		notifyUser(userInfo, pageInfo, true);
		
	}
	
	/**
	 * 找到最后一个任务的索引号，如果没有任务则返回-1<br/>
	 * <p>最后一个任务的索引（任务索引号从0开始）</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年8月31日 下午3:25:47
	 * @param pageInfo
	 * @return
	 */
	private int getLastIndex(PageInfoPO pageInfo){
		int lastIndex = -1;
		List<PageTaskPO> tasks =  pageInfo.getPageTasks();
		if(tasks == null) return lastIndex;
		for(PageTaskPO task : tasks){
			if(task.getTaskIndex() > lastIndex){
				lastIndex = task.getTaskIndex();
			}
		}
		return lastIndex;
	}
	
	/** 总页数，如果没有任务，则总页数为1 */
	public int getTotalPageNumber(PageInfoPO pageInfo){
//		int lastIndex = pageInfo.getPageTasks().size() - 1;
		int lastIndex = getLastIndex(pageInfo);
		if(lastIndex < 0) return 1;
		return getPageNumberByIndex(pageInfo, lastIndex);
	}
	
	/** 计算第index个任务在第几页 */
	private int getPageNumberByIndex(PageInfoPO pageInfo, int index){
		int pageSize = pageInfo.getPageSize();
		return calculatePageNumber(pageSize, index);
	}
	
	private int calculatePageNumber(int pageSize, int index){
		int number = (index+1)/pageSize;
		int m = (index+1)%pageSize;
		if(m > 0) number++;
		return number;
	}
	
	/** 获取总页数。总数为0时，总页数为1 */
	/*public int getTotalPageCount(CommandGroupUserInfoPO userInfo){
		List<CommandPlayerTaskPO> tasks = userInfo.getTasks();
		if(tasks==null || tasks.size()==0) return 1;
		int playerCount = userInfo.obtainUsingScheme().getPlayerSplitLayout().getPlayerCount();
		int totalPageCount = tasks.size()/playerCount;
		int m = tasks.size()%playerCount;
		if(m > 0) totalPageCount++;
		return totalPageCount;
	}*/
	
	/** pageNumber从1开始 */
	private List<PageTaskPO> getPageTasks(PageInfoPO pageInfo, int pageNumber, boolean throwException) throws BaseException{
		List<PageTaskPO> result = new ArrayList<PageTaskPO>();
		List<PageTaskPO> tasks = pageInfo.getPageTasks();//new ArrayList<CommandPlayerTaskPO>();//userInfo.getxxx
		if(tasks == null) return result;
		Collections.sort(tasks, new PageTaskPO.TaskComparatorFromIndex());
		int pageSize = pageInfo.getPageSize();
		
		int lastIndex = getLastIndex(pageInfo);
		//这是实际上的总页数，可能为0
		int totalPageCount = (lastIndex+1)/pageSize;
//		int totalPageCount = tasks.size()/pageSize;
//		int m = tasks.size()%pageSize;
		int m = (lastIndex+1)%pageSize;
		if(m > 0) totalPageCount++;
		

		//当前在第一页，总页数为0，说明没有任务
		if(pageNumber==1 && totalPageCount==0){
			return new ArrayList<PageTaskPO>();
		}
		
		if(pageNumber > totalPageCount){
			if(throwException){
				throw new BaseException(StatusCode.FORBIDDEN, "总页数为" + totalPageCount + "，无法找到第" + pageNumber + "页");
			}else{
				return null;
			}
		}
		
		int startIndex = pageSize * (pageNumber - 1);
		int endIndex = pageSize * pageNumber - 1;
		for(PageTaskPO task : tasks){
			if(task.getTaskIndex() >= startIndex && task.getTaskIndex() <= endIndex){
				result.add(task);
			}
		}
		
		return result;
		
		/*if(pageNumber < totalPageCount){
//			return tasks.subList((pageNumber-1)*pageSize, pageNumber*pageSize);
			List<PageTaskPO> subTasks = tasks.subList((pageNumber-1)*pageSize, pageNumber*pageSize);
			List<PageTaskPO> result = new ArrayList<PageTaskPO>(subTasks);
			return result;
		}else{
//			return tasks.subList((pageNumber-1)*pageSize, tasks.size());
			List<PageTaskPO> subTasks = tasks.subList((pageNumber-1)*pageSize, tasks.size());
			List<PageTaskPO> result = new ArrayList<PageTaskPO>(subTasks);
			return result;
		}*/
	}
	
	/*可以优化，有问题*/
	@Deprecated
	private PageTaskPO addTask(PageInfoPO pageInfo, List<PageTaskPO> newTasks){
		
		PageTaskPO jumpTo = null;
		
		List<PageTaskPO> tasks = pageInfo.getPageTasks();//new ArrayList<CommandPlayerTaskPO>();//userInfo.getxxx
		Collections.sort(tasks, new PageTaskPO.TaskComparatorFromIndex());
		
		for(PageTaskPO newTask : newTasks){
			newTask.setPageInfo(pageInfo);
			
			//如果是固定位置的任务，则直接加在最后，这里不校验index是否有问题
			if(newTask.getFixedAtPageAndLocation()){
				tasks.add(newTask);
				continue;
			}
			
			BusinessInfoType type = newTask.getBusinessInfoType();
			
			if(type==null || PlayerBusinessType.NONE.equals(type)){
				//不处理
			}else if(type.isCommandOrMeeting()){
				//TODO:查找同一个指挥/会议的，加在后边
				String commandId = newTask.getBusinessId().split("-")[0];
				int index = -1;
				for(int i=tasks.size()-1; i>=0; i--){
					PageTaskPO task = tasks.get(i);
					if(task.getBusinessInfoType().isCommandOrMeeting()){
						String taskCommandId = task.getBusinessId().split("-")[0];
						if(taskCommandId.equals(commandId)){
							index = i;
							break;
						}
					}
					
				}
				if(index < 0){
					index = tasks.size();
				}else{
					boolean found = false;//
					for(int i=index; i<tasks.size(); i++){
						PageTaskPO task = tasks.get(i);
						if(!Boolean.TRUE.equals(task.getFixedAtPageAndLocation())){
							//找到了放新任务的位置
							index = i;
							found = true;
							break;
						}else{
							//这里是固定任务，新任务要往后放
						}
					}
					if(!found){
						//没找到放新任务的位置，放在最后
						index = tasks.size();
					}
				}
				tasks.add(index, newTask);
				markIndex(tasks);
//				tasks.add(newTask);
			}else{
				/*//直接加在最后
				tasks.add(newTask);
				if(jumpTo == null) jumpTo = newTask;*/
				//加在第一个空缺的taskIndex位置
				boolean done = false;
				int previousIndex = -1;
				for(int i=0; i<tasks.size(); i++){
					PageTaskPO task = tasks.get(i);
					if(task.getTaskIndex() - 1 != previousIndex){
						//说明这前边出现了空缺，把新任务插入到此处，标号
						tasks.add(i, newTask);
						markIndex(tasks);
						break;
					}else{
						previousIndex = task.getTaskIndex();
					}
				}
				if(!done){
					tasks.add(newTask);
					markIndex(tasks);
				}
				if(jumpTo == null) jumpTo = newTask;
			}
		}
		
//		markIndex(tasks);//去掉？
		
		//持久化
		//pageTaskDao.save(tasks);
		pageInfoDao.save(pageInfo);
		
		return jumpTo;
	}

	@Deprecated
	private void removeTasks(PageInfoPO pageInfo, List<PageTaskPO> _removeTasks){
		
		List<PageTaskPO> removeTasks = new ArrayList<PageTaskPO>(_removeTasks);
		
		List<PageTaskPO> tasks = pageInfo.getPageTasks();//new ArrayList<CommandPlayerTaskPO>();//userInfo.getxxx
		Collections.sort(tasks, new PageTaskPO.TaskComparatorFromIndex());
		
		for(int i=tasks.size()-1; i>=0; i--){
			PageTaskPO task = tasks.get(i);
			for(PageTaskPO removeTask : removeTasks){
				if(task.equalsTask(removeTask)){
					tasks.remove(i);
					removeTasks.remove(removeTask);
					break;
				}
			}
		}
//		tasks.removeAll(removeTasks);
		markIndex(tasks);
		
		//持久化
		pageInfoDao.save(pageInfo);
	}
	
	/**
	 * 对比前后分页的任务，呼叫或挂断解码器<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月21日 上午10:13:23
	 * @param pageInfo 仅提供id等信息，不能用来获取pageInfo.getPageTasks
	 * @param _oldPageTasks 需要提前提取之前分页中的任务
	 * @param newPageTasks 切换分页之后的任务
	 * @param doProtocal 是否下发协议
	 * @return
	 * @throws Exception
	 */
	private LogicBO comparePage(
			PageInfoPO pageInfo,
			List<PageTaskPO> _oldPageTasks,
			List<PageTaskPO> newPageTasks,
			boolean doProtocal) throws Exception{
		
		synchronized (pageInfo.getOriginId()) {//TODO:这个锁需要再考虑
			
			List<PageTaskPO> remainTasks = new ArrayList<PageTaskPO>();//新老一样的pageTask
			List<PageTaskPO> addTasks = new ArrayList<PageTaskPO>();
			List<PageTaskPO> closeTasks = new ArrayList<PageTaskPO>();//需要关闭的pageTask
			List<PageTaskPO> needCloseTasks = new ArrayList<PageTaskPO>();//需要挂断的播放器
			LogicBO logic = null;
			
			//将原来的pageTasks遍历到新的集合
			List<PageTaskPO> oldPageTasks = new ArrayList<PageTaskPO>();//(_oldPageTasks);
			List<PageTaskPO> showingPageTasks = new ArrayList<PageTaskPO>();
			for(PageTaskPO _oldPageTask : _oldPageTasks){
				oldPageTasks.add(_oldPageTask);
				showingPageTasks.add(_oldPageTask);
			}
			
			//对将要显示的设置参数
			int index=0;
			for(PageTaskPO newPageTask : newPageTasks){
				if(newPageTask.getFixedAtPageAndLocation()){
					index++;
					newPageTask.setLocationIndex(newPageTask.getTaskIndex()%pageInfo.getPageSize());
					newPageTask.setShowing(true);
					continue;
				}
				newPageTask.setShowing(true);
				newPageTask.setLocationIndex(index++);
			}
			
			//循环，筛选出新的任务，剩下oldPlayers就是需要关闭的老任务
			int newTaskIndex = 0;
			for( ; newTaskIndex<newPageTasks.size(); newTaskIndex++){
				PageTaskPO newPageTask = newPageTasks.get(newTaskIndex);
//				newPageTask.setLocationIndex(newTaskIndex);
				boolean has = false;
				for(PageTaskPO oldPageTask : oldPageTasks){
					if(newPageTask.equalsTask(oldPageTask)){//对比2个task是否相同 TODO:方法待改进
						has = true;
//						oldPageTask.setLocationIndex(newTaskIndex);
						remainTasks.add(oldPageTask);
						oldPageTasks.remove(oldPageTask);//remove，下次不再遍历
						break;//删除了元素，必须跳出
					}
				}
				if(!has){
					addTasks.add(newPageTask);//需要添加的
				}
			}
//			oldPageTasks.removeAll(newPageTasks);
			closeTasks.addAll(oldPageTasks);
			
			
			//老播放器，一部分用来做新业务，剩余的挂断
			List<String> oldBundleIds = new ArrayList<String>();
			for(PageTaskPO closeTask : closeTasks){
				oldBundleIds.add(closeTask.getDstBundleId());
			}
			
			if(GroupMemberType.MEMBER_HALL.equals(pageInfo.getGroupMemberType())){

				//TODO:这个查询再优化一下条件
//				List<TerminalBundleConferenceHallPermissionPO> closeDecoders = terminalBundleConferenceHallPermissionDao.findByBundleIdIn(oldBundleIds);
				
				//空闲的解码器
				List<TerminalBundleConferenceHallPermissionPO> decoderPs = new ArrayList<TerminalBundleConferenceHallPermissionPO>();
				List<TerminalBundleConferenceHallPermissionPO> allTerminalBundlePs = terminalBundleConferenceHallPermissionDao.findByConferenceHallId(Long.parseLong(pageInfo.getOriginId()));
				List<Long> allTerminalBundleIds = new ArrayList<Long>();//用于从TerminalBundle里边判断type:DECODER/ENCODER
				List<String> allBundleIds = new ArrayList<String>();//暂时没有用
				List<String> decoderBundleIds = new ArrayList<String>();//用于解码的bundle，绑了几个就有几个
				for(TerminalBundleConferenceHallPermissionPO aTerminalBundle : allTerminalBundlePs){
					allTerminalBundleIds.add(aTerminalBundle.getTerminalBundleId());
					allBundleIds.add(aTerminalBundle.getBundleId());	
				}
				
				List<TerminalBundlePO> allTerminalBundles = terminalBundleDao.findAll(allTerminalBundleIds);
				
				for(TerminalBundlePO terminalBundle : allTerminalBundles){
					TerminalBundleType terminalBundleType = terminalBundle.getType();
					if(TerminalBundleType.DECODER.equals(terminalBundleType)
							|| TerminalBundleType.ENCODER_DECODER.equals(terminalBundleType)){
						//找到对应的TerminalBundleConferenceHallPermissionPO，对应到bundleId
						for(TerminalBundleConferenceHallPermissionPO aTerminalBundle : allTerminalBundlePs){
							if(aTerminalBundle.getTerminalBundleId().equals(terminalBundle.getId())){
								decoderPs.add(aTerminalBundle);
								decoderBundleIds.add(aTerminalBundle.getBundleId());
							}
						}
					}
				}
				
				List<BundlePO> bundlePOs = resourceBundleDao.findByBundleIds(decoderBundleIds);
				
				//应该只有1个task
				for(int i=0; i<addTasks.size(); i++){
					for(TerminalBundleConferenceHallPermissionPO decoderP : decoderPs){
						PageTaskPO addTask = addTasks.get(i);
						
//						LayoutPositionPO position = layoutPositionDao.findOne(addTask.getPositionId());
						String videoChannelId = null;
						Long positionId = addTask.getPositionId();
						if(positionId != null){
							LayoutPositionPO position = layoutPositionDao.findOne(positionId);
							if(position != null){
								//应该只能查到1个
								Long terminalId = pageInfo.getTerminalId();
								String screenPrimaryKey = position.getScreenPrimaryKey();
								List<TerminalChannelPO> channels = terminalChannelDao.findByTerminalIdAndScreenPrimaryKey(terminalId, screenPrimaryKey);
								if(channels.size() == 0){
									log.warn("terminalChannelDao.findByTerminalIdAndScreenPrimaryKey 没有查到通道，terminalId = " + terminalId + ", screenPrimaryKey = " + screenPrimaryKey);
								}
								if(channels.size() > 1){
									log.warn("terminalChannelDao.findByTerminalIdAndScreenPrimaryKey 查到多个" + channels.size() + "个通道，terminalId = " + terminalId + ", screenPrimaryKey = " + screenPrimaryKey);
								}
								videoChannelId = channels.get(0).getRealChannelId();
							}
						}
						
						BundlePO bundle = queryUtil.queryBundlePOByBundleId(bundlePOs, decoderP.getBundleId());						
						addTask.setDstByHall(decoderP, bundle, videoChannelId);
					}
				}
				
				//呼叫所有的newTaskPlayers，挂断所有的NONE的needClosePlayers；进行相应的上屏处理；字幕下发   
				CodecParamBO codec = commandCommonServiceImpl.queryDefaultAvCodecParamBO();			
				logic = openAndCloseDecoder_Rect(addTasks, needCloseTasks, codec);
							
				for(PageTaskPO closeTask : closeTasks){
					closeTask.setShowing(false);
					closeTask.clearDst();
				}
								
				//持久化
				pageInfoDao.save(pageInfo);
				
				if(doProtocal) {
					if(businessReturnService.getSegmentedExecute()){
						businessReturnService.add(logic, null, null);
					}else{
						executeBusiness.execute(logic, "刷新会场分页上屏");
					}
				}
				
			}else if(GroupMemberType.MEMBER_USER.equals(pageInfo.getGroupMemberType())){
				
				//TODO:这个查询再优化一下条件
				List<TerminalBundleUserPermissionPO> closePlayers = terminalBundleUserPermissionDao.findByBundleIdIn(oldBundleIds);
				
				//空闲的播放器
				List<TerminalBundleUserPermissionPO> idlePlayers = new ArrayList<TerminalBundleUserPermissionPO>();
				List<TerminalBundleUserPermissionPO> allTerminalBundles = terminalBundleUserPermissionDao.findByUserIdAndTerminalId(pageInfo.getOriginId(), pageInfo.getTerminalId());
	//			List<PageTaskPO> tasks = pageInfo.getPageTasks();
				for(TerminalBundleUserPermissionPO aTerminalBundle : allTerminalBundles){
					if(aTerminalBundle.getBundleType().equals("player")){//后续改成从TerminalBundle里边判断type:DECODER/ENCODER
						PageTaskPO task = tetrisBvcQueryUtil.queryPageTaskById(showingPageTasks, aTerminalBundle.getId().toString());
						if(task == null){
							idlePlayers.add(aTerminalBundle);
						}
					}
				}
				
				//用来做新业务的所有播放器，可能包含老播放器，以及未使用的播放器
				List<TerminalBundleUserPermissionPO> newTaskPlayers = new ArrayList<TerminalBundleUserPermissionPO>();
				
				new ArrayList<TerminalBundleUserPermissionPO>();
				
				if(addTasks.size() <= closeTasks.size()){
					//所有的新业务都可以用老播放器来做
					//这两行不对，因为closePlayers和closeTasks的排序不同
	//				List<TerminalBundleUserPermissionPO> old4newPlayers = closePlayers.subList(0, addTasks.size());
	//				newTaskPlayers.addAll(old4newPlayers);
					
					List<PageTaskPO> old4newTasks = closeTasks.subList(0, addTasks.size());
					List<String> old4newBundleIds = new ArrayList<String>();
					for(PageTaskPO old4newTask : old4newTasks){
						old4newBundleIds.add(old4newTask.getDstBundleId());
					}
					for(TerminalBundleUserPermissionPO p : closePlayers){
						if(old4newBundleIds.contains(p.getBundleId())){
							newTaskPlayers.add(p);
						}
					}				
					
					closePlayers.subList(addTasks.size(), closePlayers.size());
					needCloseTasks = closeTasks.subList(addTasks.size(), closePlayers.size());
				}else{
					//所有老播放器都做新业务
		//			List<CommandGroupUserPlayerPO> old4newPlayers = new ArrayList<CommandGroupUserPlayerPO>(closePlayers);
					newTaskPlayers.addAll(closePlayers);
					
					//再找空闲播放器进行补充（数量一定足够吗？）
					int needCount = addTasks.size() - closePlayers.size();
	//				List<CommandGroupUserPlayerPO> players = commandCommonServiceImpl.userChoseUsefulPlayers(userId, PlayerBusinessType.COOPERATE_COMMAND, needCount, false);
					for(int i=0; i<needCount; i++){
						newTaskPlayers.add(idlePlayers.get(0));
						idlePlayers.remove(0);
					}
	//				newTaskPlayers.addAll(players);
				}
				
				for(int i=0; i<addTasks.size(); i++){
					PageTaskPO addTask = addTasks.get(i);
					TerminalBundleUserPermissionPO newTaskPlayer = newTaskPlayers.get(i);
					List<BundlePO> bundlePOs = resourceBundleDao.findByBundleIds(new ArrayListWrapper<String>().add(newTaskPlayer.getBundleId()).getList());
					//使用newTaskPlayer进行addTask的任务
					addTask.setDstByUser(newTaskPlayer, bundlePOs.get(0));
				}
				
				//呼叫所有的newTaskPlayers，挂断所有的NONE的needClosePlayers；进行相应的上屏处理；字幕下发
				CodecParamBO codec = commandCommonServiceImpl.queryDefaultAvCodecParamBO();			
				logic = openAndCloseDecoder(addTasks, needCloseTasks, codec);
							
				for(PageTaskPO closeTask : closeTasks){
					closeTask.setShowing(false);
					closeTask.clearDst();
				}
				
				//持久化
				pageInfoDao.save(pageInfo);
				
				if(doProtocal){
					if(businessReturnService.getSegmentedExecute()){
						businessReturnService.add(logic, null, null);
					}else{
						executeBusiness.execute(logic, "刷新用户播放器分页");
					}
				} 
			}
			
			return logic;
		}
	}
	
	/** 改变执行状态后，重呼解码器。并不会推送分页信息。只对showing = true的设置源，false的不带源 */
	public LogicBO reExecute(
			List<PageTaskPO> pageTasks,
			boolean doProtocol) throws Exception{
		
		List<PageTaskPO> showingPageTasks = new ArrayList<PageTaskPO>();
		for(PageTaskPO pageTask : pageTasks){
			if(pageTask.isShowing()){
				showingPageTasks.add(pageTask);
			}
		}
		
		CodecParamBO codec = commandCommonServiceImpl.queryDefaultAvCodecParamBO();			
		LogicBO logic = openAndCloseDecoder_Rect(pageTasks, null, codec);
		
		if(doProtocol){
			if(businessReturnService.getSegmentedExecute()){
				businessReturnService.add(logic, null, null);
			}else{
				executeBusiness.execute(logic, "改变执行状态后，重呼解码器");
			}
		}
		
		return logic;
	}
	
	public CommandGroupUserLayoutShemeVO generateSchemeVO(CommandGroupUserInfoPO userInfo, PageInfoPO pageInfo) throws BaseException{
		List<PageTaskPO> pageTasks = getPageTasks(pageInfo, pageInfo.getCurrentPage(), true);		
		CommandGroupUserLayoutShemePO usingScheme = userInfo.obtainUsingScheme();
		int totalPageNumber = getTotalPageNumber(pageInfo);
		CommandGroupUserLayoutShemeVO schemeVO = generateSchemeVO(usingScheme, pageInfo, pageTasks, totalPageNumber);
		return schemeVO;
	}

	private CommandGroupUserLayoutShemeVO generateSchemeVO(
			CommandGroupUserLayoutShemePO schemePO, PageInfoPO pageInfo, List<PageTaskPO> pageTasks, int totalPageNumber){
		CommandGroupUserLayoutShemeVO vo = new CommandGroupUserLayoutShemeVO();
		vo.setName(schemePO.getName());
		vo.setTotal(totalPageNumber);
		vo.setCurrentPage(pageInfo.getCurrentPage());
		vo.setPageSize(pageInfo.getPageSize());
		vo.setPlayerSplitLayout(String.valueOf(schemePO.getPlayerSplitLayout().getId()));
		vo.setPlayersByPageTasks(pageTasks);
		
		int allNumber = schemePO.getPlayerSplitLayout().getPlayerCount();
		if(allNumber == pageTasks.size()) return vo;
		int needNumber = allNumber - pageTasks.size();
		
		List<String> dstIds = new ArrayList<String>();
		for(PageTaskPO pageTask : pageTasks){
			dstIds.add(pageTask.getDstId());
		}
		
		List<TerminalBundleUserPermissionPO> ps = terminalBundleUserPermissionDao.findByUserIdAndTerminalId(pageInfo.getOriginId(), pageInfo.getTerminalId());
//		List<TerminalBundleUserPermissionPO> idlePs = new ArrayList<TerminalBundleUserPermissionPO>();
		List<String> idleBundleIds = new ArrayList<String>();
		for(TerminalBundleUserPermissionPO p : ps){
			if(!dstIds.contains(p.getId().toString())){
				idleBundleIds.add(p.getBundleId());
				if(idleBundleIds.size()==needNumber) break;
			}
		}
		
		List<BundlePO> bundles = resourceBundleDao.findByBundleIds(idleBundleIds);
		List<Integer> localIndexs=new ArrayList<Integer>();
		for(int i=0;i<pageInfo.getPageSize();i++){
			boolean notExit=true;
			for(PageTaskPO pageTask:pageTasks){
				if(pageTask.getLocationIndex()==i){
					notExit=false;
					break;
				}
				
			}
			if(notExit){
				localIndexs.add(i);
			}
		}
		for(int i= 0;i<bundles.size();i++){
			vo.getPlayers().add(new CommandGroupUserPlayerSettingVO().setIdlePlayer(localIndexs.get(i), bundles.get(i)));
		}
//		int i = pageTasks.size();
//		for(BundlePO bundle : bundles){
//			vo.getPlayers().add(new CommandGroupUserPlayerSettingVO().setIdlePlayer(i++, bundle));
//		}
		
		return vo;
	}

	private void markIndex(List<PageTaskPO> tasks){
		int i = 0;
		for(PageTaskPO task : tasks){
			if(Boolean.TRUE.equals(task.getFixedAtPageAndLocation())){
				i = task.getTaskIndex() + 1;
			}else{
				task.setTaskIndex(i++);
			}
		}
	}
	
	//TODO:切分屏，参考 CommandSplitServiceImpl.changeLayoutScheme
	
	//TODO:特殊跳转，调用jumpToPage	
	private LogicBO openAndCloseDecoder(
			List<PageTaskPO> openTasks,
			List<PageTaskPO> closeTasks,
			CodecParamBO codec){
		
		if(openTasks == null) openTasks = new ArrayList<PageTaskPO>();
		if(closeTasks == null) closeTasks = new ArrayList<PageTaskPO>();
		
		LogicBO logic = new LogicBO().setUserId("-1")
		 		 .setConnectBundle(new ArrayList<ConnectBundleBO>())
		 		 .setDisconnectBundle(new ArrayList<DisconnectBundleBO>())
		 		 .setPass_by(new ArrayList<PassByBO>());
		
		for(PageTaskPO openTask : openTasks){
			//播放文件不需要呼叫
			if(openTask.getBusinessInfoType().isPlayFile()) continue;
			ConnectBundleBO connectDecoderBundle = new ConnectBundleBO().setBusinessType(ConnectBundleBO.BUSINESS_TYPE_VOD)
//			  													        .setOperateType(ConnectBundleBO.OPERATE_TYPE)
			  													        .setLock_type("write")
			  													        .setBundleId(openTask.getDstBundleId())
			  													        .setLayerId(openTask.getDstLayerId())
			  													        .setBundle_type(openTask.getDstBundleType());
			ForwardSetSrcBO decoderVideoForwardSet = null;
			if(AgendaSourceType.COMBINE_VIDEO.equals(openTask.getVideoSourceType())){
				decoderVideoForwardSet = new ForwardSetSrcBO().setType("combineVideo")
						.setUuid(openTask.getSrcVideoId());
			}else if(openTask.getSrcVideoBundleId() != null && ExecuteStatus.DONE.equals(openTask.getVideoStatus())){
				decoderVideoForwardSet = new ForwardSetSrcBO().setType("channel")
						.setBundleId(openTask.getSrcVideoBundleId())
						.setLayerId(openTask.getSrcVideoLayerId())
						.setChannelId(openTask.getSrcVideoChannelId());
			}
			ConnectBO connectDecoderVideoChannel = new ConnectBO().setChannelId(openTask.getDstVideoChannelId())
														          .setChannel_status("Open")
														          .setBase_type(openTask.getDstVideoBaseType())
														          .setMode(openTask.getVideoTransmissionMode().getCode())
														          .setMulti_addr(openTask.getVideoMultiAddr())
														          .setSrc_multi_ip(openTask.getVideoMultiSrcAddr())
														          .setCodec_param(codec)
														          .setSource_param(decoderVideoForwardSet);
			ForwardSetSrcBO decoderAudioForwardSet = null;
			if(AgendaSourceType.COMBINE_AUDIO.equals(openTask.getAudioSourceType())){
				decoderAudioForwardSet = new ForwardSetSrcBO().setType("combineAudio")
						.setUuid(openTask.getSrcAudioId());
			}else if(openTask.getSrcAudioBundleId() != null && ExecuteStatus.DONE.equals(openTask.getAudioStatus())){
				decoderAudioForwardSet = new ForwardSetSrcBO().setType("channel")
						.setBundleId(openTask.getSrcAudioBundleId())
						.setLayerId(openTask.getSrcAudioLayerId())
						.setChannelId(openTask.getSrcAudioChannelId());
			}
			ConnectBO connectDecoderAudioChannel = new ConnectBO().setChannelId(openTask.getDstAudioChannelId())
																  .setChannel_status("Open")
																  .setBase_type(openTask.getDstAudioBaseType())
														          .setMode(openTask.getAudioTransmissionMode().getCode())
														          .setMulti_addr(openTask.getAudioMultiAddr())
														          .setSrc_multi_ip(openTask.getAudioMultiSrcAddr())
																  .setCodec_param(codec)
																  .setSource_param(decoderAudioForwardSet);
			
			connectDecoderBundle.setChannels(new ArrayListWrapper<ConnectBO>().add(connectDecoderVideoChannel).add(connectDecoderAudioChannel).getList());
			logic.getConnectBundle().add(connectDecoderBundle);
		}
		
		for(PageTaskPO closeTask : closeTasks){
			if(closeTask.getBusinessInfoType().isPlayFile()) continue;
			DisconnectBundleBO disconnectDecoderBundle = new DisconnectBundleBO().setBusinessType(DisconnectBundleBO.BUSINESS_TYPE_VOD)
//																           	 .setOperateType(DisconnectBundleBO.OPERATE_TYPE)
																           	 .setBundleId(closeTask.getDstBundleId())
																           	 .setBundle_type(closeTask.getDstBundleType())
																           	 .setLayerId(closeTask.getDstLayerId());
			logic.getDisconnectBundle().add(disconnectDecoderBundle);
		}
		
		return logic;
	}
	
	/**
	 * 对比前后的分页，打开新播放的，关掉不播放的，保留原有的<br/>
	 * <p>增加对多个rect的支持（支持虚拟源）</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年9月14日 上午9:32:12
	 * @param openTasks 刷新分页后的任务
	 * @param closeTasks 刷新分页前的任务
	 * @param codec
	 * @return
	 */
	private LogicBO openAndCloseDecoder_Rect(
			List<PageTaskPO> openTasks,
			List<PageTaskPO> closeTasks,
			CodecParamBO codec){
		
		if(openTasks == null) openTasks = new ArrayList<PageTaskPO>();
		if(closeTasks == null) closeTasks = new ArrayList<PageTaskPO>();
		
		Map<String, List<PageTaskPO>> map = pageTaskUtil.classifyBundleTasks(openTasks);
		
		LogicBO logic = new LogicBO().setUserId("-1")
		 		 .setConnectBundle(new ArrayList<ConnectBundleBO>())
		 		 .setDisconnectBundle(new ArrayList<DisconnectBundleBO>())
		 		 .setPass_by(new ArrayList<PassByBO>());
		
		for(String bundleId : map.keySet()){
			List<PageTaskPO> opens = map.get(bundleId);
			PageTaskPO open = opens.get(0);
			ConnectBundleBO connectDecoderBundle = new ConnectBundleBO().setBusinessType(ConnectBundleBO.BUSINESS_TYPE_VOD)
//				        .setOperateType(ConnectBundleBO.OPERATE_TYPE)
				        .setLock_type("write")
				        .setBundleId(open.getDstBundleId())
				        .setLayerId(open.getDstLayerId())
				        .setBundle_type(open.getDstBundleType());
			ScreenBO screen = null;
			for(PageTaskPO openTask : opens){
				//TODO:播放文件不需要呼叫
//				ConnectBundleBO connectDecoderBundle = new ConnectBundleBO().setBusinessType(ConnectBundleBO.BUSINESS_TYPE_VOD)
////				  													        .setOperateType(ConnectBundleBO.OPERATE_TYPE)
//				  													        .setLock_type("write")
//				  													        .setBundleId(openTask.getDstBundleId())
//				  													        .setLayerId(openTask.getDstLayerId())
//				  													        .setBundle_type(openTask.getDstBundleType());
				ForwardSetSrcBO decoderVideoForwardSet = null;
				if(AgendaSourceType.COMBINE_VIDEO.equals(openTask.getVideoSourceType())){
					decoderVideoForwardSet = new ForwardSetSrcBO().setType("combineVideo")
							.setUuid(openTask.getSrcVideoId());
				}else if(openTask.getSrcVideoBundleId() != null && ExecuteStatus.DONE.equals(openTask.getVideoStatus())){
					decoderVideoForwardSet = new ForwardSetSrcBO().setType("channel")
							.setBundleId(openTask.getSrcVideoBundleId())
							.setLayerId(openTask.getSrcVideoLayerId())
							.setChannelId(openTask.getSrcVideoChannelId());
				}
				ConnectBO connectDecoderVideoChannel = new ConnectBO().setChannelId(openTask.getDstVideoChannelId())
															          .setChannel_status("Open")
															          .setBase_type(openTask.getDstVideoBaseType())
															          .setMode(openTask.getVideoTransmissionMode().getCode())
															          .setMulti_addr(openTask.getVideoMultiAddr())
															          .setSrc_multi_ip(openTask.getVideoMultiSrcAddr())
															          .setCodec_param(codec)
															          .setSource_param(decoderVideoForwardSet);
				ForwardSetSrcBO decoderAudioForwardSet = null;
				if(AgendaSourceType.COMBINE_AUDIO.equals(openTask.getAudioSourceType())){
					decoderAudioForwardSet = new ForwardSetSrcBO().setType("combineAudio")
							.setUuid(openTask.getSrcAudioId());
				}else if(openTask.getSrcAudioBundleId() != null && ExecuteStatus.DONE.equals(openTask.getAudioStatus())){
					decoderAudioForwardSet = new ForwardSetSrcBO().setType("channel")
							.setBundleId(openTask.getSrcAudioBundleId())
							.setLayerId(openTask.getSrcAudioLayerId())
							.setChannelId(openTask.getSrcAudioChannelId());
				}
				ConnectBO connectDecoderAudioChannel = new ConnectBO().setChannelId(openTask.getDstAudioChannelId())
																	  .setChannel_status("Open")
																	  .setBase_type(openTask.getDstAudioBaseType())
															          .setMode(openTask.getAudioTransmissionMode().getCode())
															          .setMulti_addr(openTask.getAudioMultiAddr())
															          .setSrc_multi_ip(openTask.getAudioMultiSrcAddr())
																	  .setCodec_param(codec)
																	  .setSource_param(decoderAudioForwardSet);
				
//				connectDecoderBundle.setChannels(new ArrayListWrapper<ConnectBO>().add(connectDecoderVideoChannel).add(connectDecoderAudioChannel).getList());
				connectDecoderBundle.getChannels().add(connectDecoderVideoChannel);
				connectDecoderBundle.getChannels().add(connectDecoderAudioChannel);//TODO:虚拟源时可能产生重复的多个音频解码channel，需要去掉
//				logic.getConnectBundle().add(connectDecoderBundle);
				
				Long positionId = openTask.getPositionId();
				if(positionId != null){
					LayoutPositionPO position = layoutPositionDao.findOne(positionId);
					if(position != null){
						if(screen == null) screen = new ScreenBO().setScreen_id("screen_1").setRects(new ArrayList<RectBO>());
						RectBO rect = new RectBO().set(position, openTask.getDstVideoChannelId());
						screen.getRects().add(rect);
					}
				}
			}
			if(screen != null){
				connectDecoderBundle.getScreens().add(screen);
			}
			logic.getConnectBundle().add(connectDecoderBundle);
		}
		
		for(PageTaskPO closeTask : closeTasks){
			DisconnectBundleBO disconnectDecoderBundle = new DisconnectBundleBO().setBusinessType(DisconnectBundleBO.BUSINESS_TYPE_VOD)
//																           	 .setOperateType(DisconnectBundleBO.OPERATE_TYPE)
																           	 .setBundleId(closeTask.getDstBundleId())
																           	 .setBundle_type(closeTask.getDstBundleType())
																           	 .setLayerId(closeTask.getDstLayerId());
			logic.getDisconnectBundle().add(disconnectDecoderBundle);
		}
		
		return logic;
	}

	/**
	 * 获取固定的任务集合br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年9月3日 上午8:58:35
	 * @param pageTasks 
	 * @return
	 */
	public List<PageTaskPO> getFixedPageTasks(Collection<PageTaskPO> pageTasks){
		
		List<PageTaskPO> fixedPageTasks=new ArrayList<PageTaskPO>();
		for(PageTaskPO pageTask:pageTasks){
			if(pageTask.getFixedAtPageAndLocation()){
				fixedPageTasks.add(pageTask);
			}
		}
		return fixedPageTasks;
	}
	
	/**
	 * 添加新任务并重新排序task_index<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年9月3日 上午9:14:27
	 * @param pageInfo
	 * @param newTasks
	 * @return
	 */
	private PageTaskPO newAddTask(PageInfoPO pageInfo, List<PageTaskPO> newTasks ){

		PageTaskPO jumpTo = null;
		List<PageTaskPO> pageTasks=pageInfo.getPageTasks();
		if(pageTasks==null) pageTasks=new ArrayList<PageTaskPO>();
		List<PageTaskPO> fixedPageTasks = getFixedPageTasks(pageTasks);
		if(fixedPageTasks==null) fixedPageTasks=new ArrayList<PageTaskPO>();
		List<PageTaskPO>  ordinaryPageTasks= new ArrayList<PageTaskPO>(pageTasks);
		ordinaryPageTasks.removeAll(fixedPageTasks);
		Collections.sort(fixedPageTasks, new PageTaskPO.TaskComparatorFromIndex());
		Collections.sort(ordinaryPageTasks, new PageTaskPO.TaskComparatorFromIndex());
		
//		List<PageTaskPO> tasks = pageInfo.getPageTasks();//new ArrayList<CommandPlayerTaskPO>();//userInfo.getxxx
//		Collections.sort(tasks, new PageTaskPO.TaskComparatorFromIndex());
		
		for(PageTaskPO newTask : newTasks){
			newTask.setPageInfo(pageInfo);
			
			//将固定任务加入fixedPageTask中
			if(newTask.getFixedAtPageAndLocation()){
				
				boolean addFixed=true;
				
				for(int i=0;i<fixedPageTasks.size();i++){
					//固定任务的task_index冲突的解决
					if(newTask.getTaskIndex()==fixedPageTasks.get(i).getTaskIndex()){
						PageTaskPO oldTask=fixedPageTasks.get(i);
						resolveConflict(oldTask,fixedPageTasks,i);
						fixedPageTasks.set(i, newTask);
						addFixed=false;
						break;
					}
					if(newTask.getTaskIndex()<fixedPageTasks.get(i).getTaskIndex()){
						fixedPageTasks.add(i, newTask);
						addFixed=false;
						break;
					}
				}
				if(addFixed) fixedPageTasks.add(newTask);
				continue;
			}
			
			BusinessInfoType type = newTask.getBusinessInfoType();
			
			if(type==null || PlayerBusinessType.NONE.equals(type)){
				//不处理
			}else if(type.isCommandOrMeeting()){
				//查找同一个指挥/会议的，加在后边
				String commandId = newTask.getBusinessId().split("-")[0];
				int index = -1;
				for(int i=ordinaryPageTasks.size()-1; i>=0; i--){
					PageTaskPO task = ordinaryPageTasks.get(i);
					if(task.getBusinessInfoType().isCommandOrMeeting()){
						String taskCommandId = task.getBusinessId().split("-")[0];
						if(taskCommandId.equals(commandId)){
							index = i+1;
							break;
						}
					}
				}
				if(index<0){
					index=ordinaryPageTasks.size();
				}
				ordinaryPageTasks.add(index, newTask);
			}else{
				//直接加在末尾
				ordinaryPageTasks.add(ordinaryPageTasks.size(), newTask);
				if(jumpTo == null) jumpTo = newTask;
			}
		}
		
		//对比两个集合合并为一个集合并进行赋值task_index再全部传给pageInfo
		pageTasks.clear();
		pageTasks.addAll(newmarkIndex(ordinaryPageTasks,fixedPageTasks));
		
		fixedAllTasks(pageTasks);
		
		//持久化
		pageInfoDao.save(pageInfo);
		
		return jumpTo;
	}
	
	/**
	 * 重新排序task_index<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年9月3日 上午10:28:10
	 * @param ordinaryPageTasks 非固定
	 * @param fixedPageTasks 固定
	 * @param pageTasks 真正的pageTask集合
	 */
	private List<PageTaskPO> newmarkIndex(List<PageTaskPO> ordinaryPageTasks,List<PageTaskPO> fixedPageTasks){
		
		int i = 0;
		List<PageTaskPO> pageTasks=new ArrayList<PageTaskPO>();
		if(fixedPageTasks.size()<=0){
			for(PageTaskPO pageTask:ordinaryPageTasks){
				pageTask.setTaskIndex(i);
				i++;
				pageTasks.add(pageTask);
			}
			return pageTasks;
		}else if(ordinaryPageTasks.size()<=0){
			return fixedPageTasks;
		}else{
			for(PageTaskPO ordinaeyPageTask:ordinaryPageTasks){
				for(PageTaskPO fixedPageTask:fixedPageTasks){
					if(fixedPageTask.getTaskIndex()==i){
						i++;
					}
				}
				ordinaeyPageTask.setTaskIndex(i);
				i++;
				pageTasks.add(ordinaeyPageTask);
			}
			for(PageTaskPO fixedPageTask:fixedPageTasks){
				pageTasks.add(fixedPageTask);
			}
			return pageTasks;
		}
	}
	
	/**
	 * 去除要删除的pageTask<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年9月3日 下午5:15:31
	 * @param pageInfo
	 * @param _removeTasks
	 */
	private void newRemoveTasks(PageInfoPO pageInfo, List<PageTaskPO> _removeTasks){
		
		List<PageTaskPO> removeTasks = new ArrayList<PageTaskPO>(_removeTasks);
		List<PageTaskPO> tasks = pageInfo.getPageTasks();

		tasks.removeAll(removeTasks);
		
		//持久化
		pageInfoDao.save(pageInfo);
	}
	
	/**
	 * 冲突解决：查找后边是否有空位，添加进空位，否则添加尾部<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年9月4日 上午9:37:14
	 * @param oldTask 发生冲突的原pageTask
	 * @param fixedPageTasks 
	 * @param index 发生冲突的集合中的位置
	 */
	public void resolveConflict(PageTaskPO oldTask,List<PageTaskPO> fixedPageTasks,int index){
		
		boolean addSuccess=false;
		for(int i=index;i<fixedPageTasks.size()-1;i++){
			int taskIndex=fixedPageTasks.get(i).getTaskIndex();
			int nextTaskIndex=fixedPageTasks.get(i+1).getTaskIndex();
			if(taskIndex+1<nextTaskIndex){
				oldTask.setTaskIndex(taskIndex+1);
				fixedPageTasks.add(i+1,oldTask);
				addSuccess=true;
				break;
			}
		}
		
		if(!addSuccess){
			int taskIndex=fixedPageTasks.get(fixedPageTasks.size()-1).getTaskIndex()+1;
			oldTask.setTaskIndex(taskIndex);;
			fixedPageTasks.add(oldTask);
		}
	}
	
	/**
	 * 将所有的都设置为固定的<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年9月10日 上午11:17:10
	 */
	public void fixedAllTasks(Collection <PageTaskPO> pageTasks){
		for(PageTaskPO pageTask:pageTasks){
			pageTask.setFixedAtPageAndLocation(true);
		}
	}
//	//-------------------去除空白页-----------------
//	//解决:不移动视频在屏幕中位置去除空白页
//	//先找所有固定,再找空白页页码集合,当前页,删除空白页后应该看哪一页,删除空白页后每页对应页码
//	/**
//	 * 去除空白页<br/>
//	 * <b>作者:</b>lx<br/>
//	 * <b>版本：</b>1.0<br/>
//	 * <b>日期：</b>2020年11月26日 下午4:46:51
//	 * @param allTasks 要除去空白页的任务集合
//	 * @param pageSize 每页的大小
//	 * @param currentPage 当前的页面
//	 */
//	public void removeBlankPage(
//			List<PageTaskPO> allTasks, 
//			Integer pageSize,
//			Integer currentPage){
//		//作为核心方法，应该操作简单，逻辑复杂，上层判空，反复调用。
//		
//		//每页中的集合,1是第一页
//		Map<Integer, List<PageTaskPO>> pageIndexAndTask = allTasks.stream().collect(Collectors.groupingBy(task->{return (task.getTaskIndex()/pageSize)+1;}));
//		//非空白页号码
//		List<Integer> pageNumberList = pageIndexAndTask.keySet().stream().sorted().collect(Collectors.toList());
////		//删除空白页需要改变标号的页面
////		Map<Integer, List<Integer>> cascadeExchangeMap =  new HashMap<Integer, List<Integer>>();
////		
////		for(Integer pageNumber :pageNumberList){
////			cascadeExchangeMap.put(pageNumber, new ArrayList<Integer>(pageNumberList));
////			pageNumberList.remove(pageNumber);
////		}
//		
//		Map<Integer, Integer> cascadeExchangeMap =  new HashMap<Integer, Integer>();
//		Integer pageNumber = 1;
//		for(Integer pageNumber: pageNumberList){
//			cascadeExchangeMap.put(pageNumber, 0);
//		}
//		
//		for(int i = 0 ; i<pageNumberList.size() ; i++){
//			Integer distance = pageNumberList.get(i) - pageNumberList.get(i);
//			if(distance > 1){
//				for(){
//					
//				}
//			}
//		}
//		
//	}
//	
//	//-------------------去除空白页结束--------------
}
