package com.sumavision.tetris.bvc.page;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.suma.venus.resource.pojo.BundlePO;
import com.sumavision.bvc.command.group.basic.CommandGroupPO;
import com.sumavision.bvc.command.group.dao.CommandGroupUserInfoDAO;
import com.sumavision.bvc.command.group.user.CommandGroupUserInfoPO;
import com.sumavision.bvc.command.group.user.layout.page.CommandPlayerTaskBO;
import com.sumavision.bvc.command.group.user.layout.page.CommandPlayerTaskPO;
import com.sumavision.bvc.command.group.user.layout.player.CommandGroupUserPlayerCastDevicePO;
import com.sumavision.bvc.command.group.user.layout.player.CommandGroupUserPlayerPO;
import com.sumavision.bvc.command.group.user.layout.player.PlayerBusinessType;
import com.sumavision.bvc.command.group.user.layout.scheme.CommandGroupUserLayoutShemePO;
import com.sumavision.bvc.command.group.user.layout.scheme.PlayerSplitLayout;
import com.sumavision.bvc.control.device.command.group.vo.user.CommandGroupUserLayoutShemeVO;
import com.sumavision.bvc.device.command.bo.MessageSendCacheBO;
import com.sumavision.bvc.device.command.bo.PlayerInfoBO;
import com.sumavision.bvc.device.command.cast.CommandCastServiceImpl;
import com.sumavision.bvc.device.command.common.CommandCommonConstant;
import com.sumavision.bvc.device.command.common.CommandCommonServiceImpl;
import com.sumavision.bvc.device.command.common.CommandCommonUtil;
import com.sumavision.bvc.device.command.user.CommandUserServiceImpl;
import com.sumavision.bvc.device.group.bo.CodecParamBO;
import com.sumavision.bvc.device.group.bo.ConnectBO;
import com.sumavision.bvc.device.group.bo.ConnectBundleBO;
import com.sumavision.bvc.device.group.bo.DisconnectBundleBO;
import com.sumavision.bvc.device.group.bo.ForwardSetSrcBO;
import com.sumavision.bvc.device.group.bo.LogicBO;
import com.sumavision.bvc.device.group.bo.PassByBO;
import com.sumavision.bvc.device.group.service.test.ExecuteBusinessProxy;
import com.sumavision.bvc.resource.dao.ResourceBundleDAO;
import com.sumavision.bvc.system.po.AvtplGearsPO;
import com.sumavision.bvc.system.po.AvtplPO;
import com.sumavision.tetris.bvc.business.BusinessInfoType;
import com.sumavision.tetris.bvc.business.ExecuteStatus;
import com.sumavision.tetris.bvc.business.dao.TerminalBundleUserPermissionDAO;
import com.sumavision.tetris.bvc.business.terminal.user.TerminalBundleUserPermissionPO;
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
	private PageInfoDAO pageInfoDao;
	
	@Autowired
	private TerminalBundleUserPermissionDAO terminalBundleUserPermissionDao;
	
	@Autowired
	private ResourceBundleDAO resourceBundleDao;
	
	@Autowired
	private CommandCommonServiceImpl commandCommonServiceImpl;
	
	@Autowired
	private WebsocketMessageService websocketMessageService;
	
	@Autowired
	private CommandUserServiceImpl commandUserServiceImpl;
	
	@Autowired
	private CommandCastServiceImpl commandCastServiceImpl;

	@Autowired
	private CommandCommonUtil commandCommonUtil;
	
	@Autowired
	private ExecuteBusinessProxy executeBusiness;
	
	//待重构
	public MessageSendCacheBO notifyUser(CommandGroupUserInfoPO userInfo, PageInfoPO pageInfo, boolean doWebsocket) throws Exception{
		
		List<PageTaskPO> pageTasks = getPageTasks(pageInfo, pageInfo.getCurrentPage(), true);
		
		CommandGroupUserLayoutShemePO usingScheme = userInfo.obtainUsingScheme();
		CommandGroupUserLayoutShemeVO schemeVO = new CommandGroupUserLayoutShemeVO().set(usingScheme, pageTasks);
		
		JSONObject message = new JSONObject();
		message.put("businessType", "refreshSplit");
		message.put("pageInfo", schemeVO);
		log.info("layout: " + JSON.toJSONString(schemeVO));
		
		MessageSendCacheBO cache = new MessageSendCacheBO(userInfo.getUserId(), message.toJSONString(), WebsocketMessageType.COMMAND);
	
		//发送消息
		if(doWebsocket){
			WebsocketMessageVO ws = websocketMessageService.send(userInfo.getUserId(), message.toJSONString(), WebsocketMessageType.COMMAND);
			websocketMessageService.consume(ws.getId());
		}
		
		return cache;
	}
	
	public void addAndRemoveTasks(PageInfoPO pageInfo, List<PageTaskPO> newTasks, List<PageTaskPO> removeTasks) throws Exception{
		
		if(newTasks == null) newTasks = new ArrayList<PageTaskPO>();
		if(removeTasks == null) removeTasks = new ArrayList<PageTaskPO>();
		
//		//查找该用户配置信息
//		CommandGroupUserInfoPO userInfo = commandGroupUserInfoDao.findByUserId(userId);
//		if(null == userInfo){
//			//如果没有则建立默认
////			userInfo = commandUserServiceImpl.generateDefaultUserInfo(user.getId(), user.getName(), true);
//		}
		
		Integer currentPage = pageInfo.getCurrentPage();
		
		//获取当前分页下的任务
		List<PageTaskPO> oldTaskPOs = getPageTasks(pageInfo, currentPage, true);
		
//		removeTask
		removeTasks(pageInfo, removeTasks);
		
//		addTask
//		List<PageTaskPO> newTaskPOs = new ArrayList<PageTaskPO>();
//		for(PageTaskBO newTaskBO : newTasks){
//			newTaskPOs.add(new PageTaskPO().set(newTaskBO));
//		}
		PageTaskPO jumpTo = addTask(pageInfo, newTasks);
		
		//总页数
		int newPageCount = pageInfo.obtainTotalPageCount();
		
		//要跳转到的页数
		int newCurrentPage = 1;
		if(jumpTo != null){
			newCurrentPage = getPageNumberByIndex(pageInfo, jumpTo.getTaskIndex());
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
		List<PageTaskPO> newPageTasks = getPageTasks(pageInfo, newCurrentPage, true);
		comparePage(pageInfo, oldTaskPOs, newPageTasks, null, null, null);
		//notifyUser
	}
	
	public void jumpToPage(PageInfoPO pageInfo, int toPage) throws Exception{
		//对比新老页
		//筛选出正在使用的播放器
		/*List<CommandGroupUserPlayerPO> oldPageUsingPlayers = new ArrayList<CommandGroupUserPlayerPO>();
		List<CommandGroupUserPlayerPO> players = pageInfo.obtainUsingSchemePlayers();
		for(CommandGroupUserPlayerPO player : players){
			if(!player.getPlayerBusinessType().equals(PlayerBusinessType.NONE)){
				oldPageUsingPlayers.add(player);
			}
		}
		List<CommandPlayerTaskPO> newPageTasks = getPageTasks(pageInfo, toPage, true);
		comparePage(pageInfo, newPageTasks);*/
	}
	
	/** 计算第index个任务在第几页 */
	private int getPageNumberByIndex(PageInfoPO userInfo, int index){
		int pageSize = userInfo.getPageSize();
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
		List<PageTaskPO> tasks = pageInfo.getPageTasks();//new ArrayList<CommandPlayerTaskPO>();//userInfo.getxxx
		Collections.sort(tasks, new PageTaskPO.TaskComparatorFromIndex());
		int pageSize = pageInfo.getPageSize();
		int totalPageCount = tasks.size()/pageSize;
		int m = tasks.size()%pageSize;
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
		
		if(pageNumber < totalPageCount){
			return tasks.subList((pageNumber-1)*pageSize, pageNumber*pageSize);
		}else{
			return tasks.subList((pageNumber-1)*pageSize, tasks.size());
		}		
	}
	
	private PageTaskPO addTask(PageInfoPO pageInfo, List<PageTaskPO> newTasks){
		
		PageTaskPO jumpTo = null;
		
		List<PageTaskPO> tasks = pageInfo.getPageTasks();//new ArrayList<CommandPlayerTaskPO>();//userInfo.getxxx
		Collections.sort(tasks, new PageTaskPO.TaskComparatorFromIndex());
		
		for(PageTaskPO newTask : newTasks){
			newTask.setPageInfo(pageInfo);
			BusinessInfoType type = newTask.getBusinessInfoType();
			
			if(type==null || PlayerBusinessType.NONE.equals(type)){
				//不处理
			}else if(type.isCommandOrMeeting()){
				//TODO:查找同一个指挥/会议的，加在后边
				String commandId = "";//newTask.getBusinessId().split("-")[0];
				int index = -1;
				for(int i=tasks.size()-1; i>=0; i--){
					PageTaskPO task = tasks.get(i);
					if(task.getBusinessInfoType().isCommandOrMeeting()){
						String taskCommandId = "";//task.getBusinessId().split("-")[0];
						if(taskCommandId.equals(commandId)){
							index = i;
							break;
						}
					}
					
				}
				if(index < 0){
					index = tasks.size();
				}
				tasks.add(index, newTask);
			}else{
				//直接加在最后
				tasks.add(newTask);
				if(jumpTo == null) jumpTo = newTask;
			}
		}
		
		markIndex(tasks);
		
		//持久化
		pageInfoDao.save(pageInfo);
		
		return jumpTo;
	}
	
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
		
		markIndex(tasks);
		
		//持久化
		pageInfoDao.save(pageInfo);
	}
	
	/** 对比 */
	/*private LogicBO comparePage(
			CommandGroupUserInfoPO userInfo,
//			List<CommandGroupUserPlayerPO> _oldPageUsingPlayers,
			List<CommandPlayerTaskPO> newPageTasks) throws Exception{
		
		synchronized (userInfo.getUserId()) {
//			for(CommandGroupUserPlayerPO _oldPlayer : _oldPageUsingPlayers){
//				oldPlayers.add(_oldPlayer);
//			}
			
			//把所有的task的LocationIndex置为null
			List<CommandPlayerTaskPO> tasks = userInfo.getTasks();
			for(CommandPlayerTaskPO task : tasks){
				task.setLocationIndex(null);
			}
			
			List<CommandGroupUserPlayerPO> allPlayers = userInfo.getPlayers();
			Collections.sort(allPlayers, new CommandGroupUserPlayerPO.PlayerComparatorFromIndex());
			
			List<CommandGroupUserPlayerPO> oldPlayers = new ArrayList<CommandGroupUserPlayerPO>();
			List<CommandGroupUserPlayerPO> idlePlayers = new ArrayList<CommandGroupUserPlayerPO>();
			for(CommandGroupUserPlayerPO player : allPlayers){
				if(!player.getPlayerBusinessType().equals(PlayerBusinessType.NONE)){
					oldPlayers.add(player);
				}else{
					idlePlayers.add(player);
				}
			}
			
			//保留转发关系不变的播放器（index可能改变）（暂时无用）
			List<CommandGroupUserPlayerPO> remainPlayers = new ArrayList<CommandGroupUserPlayerPO>();
			
			//可能需要关闭的播放器
			List<CommandGroupUserPlayerPO> closePlayers = new ArrayList<CommandGroupUserPlayerPO>();
			//本页上的新任务
			List<CommandPlayerTaskPO> newTasks = new ArrayList<CommandPlayerTaskPO>();
			
			//循环，筛选出新的任务，剩下oldPlayers就是需要关闭的老任务
			int newTaskIndex = 0;
			for( ; newTaskIndex<newPageTasks.size(); newTaskIndex++){
//			for(CommandPlayerTaskPO newPageTask : newPageTasks){
				CommandPlayerTaskPO newPageTask = newPageTasks.get(newTaskIndex);
				newPageTask.setLocationIndex(newTaskIndex);
				boolean has = false;
				for(CommandGroupUserPlayerPO oldPlayer : oldPlayers){
					if(newPageTask.equalsPlayer(oldPlayer)){//对比2个播放器业务是否相同
						has = true;
						oldPlayer.setLocationIndex(newTaskIndex);
						remainPlayers.add(oldPlayer);
						oldPlayers.remove(oldPlayer);//remove，下次不再遍历
						break;//删除了元素，必须跳出
					}
				}
				if(!has){
					newTasks.add(newPageTask);
				}
			}
			
			closePlayers.addAll(oldPlayers);
			
			
			
			//用来做新业务的所有播放器，可能包含老播放器，以及未使用的播放器
			List<CommandGroupUserPlayerPO> newTaskPlayers = new ArrayList<CommandGroupUserPlayerPO>();
			
			//需要挂断的老播放器
			List<CommandGroupUserPlayerPO> needClosePlayers = new ArrayList<CommandGroupUserPlayerPO>();
							
			if(newTasks.size() <= closePlayers.size()){
				//直接用来做新业务的老播放器
				List<CommandGroupUserPlayerPO> old4newPlayers = closePlayers.subList(0, newTasks.size());
				newTaskPlayers.addAll(old4newPlayers);
				
				//需要挂断的老播放器
				needClosePlayers = closePlayers.subList(newTasks.size(), closePlayers.size());
			}else{
				//所有老播放器都做新业务
	//			List<CommandGroupUserPlayerPO> old4newPlayers = new ArrayList<CommandGroupUserPlayerPO>(closePlayers);
				newTaskPlayers.addAll(closePlayers);
				
				//再找空闲播放器进行补充（数量一定足够吗？）
				int needCount = newTasks.size() - closePlayers.size();
//				List<CommandGroupUserPlayerPO> players = commandCommonServiceImpl.userChoseUsefulPlayers(userId, PlayerBusinessType.COOPERATE_COMMAND, needCount, false);
				for(int i=0; i<needCount; i++){
					newTaskPlayers.add(idlePlayers.get(0));
					idlePlayers.remove(0);
				}
//				newTaskPlayers.addAll(players);
			}
			
			for(int i=0; i<newTasks.size(); i++){
				CommandPlayerTaskPO newTask = newTasks.get(i);
				CommandGroupUserPlayerPO newTaskPlayer = newTaskPlayers.get(i);
				//使用newTaskPlayer进行newTask的任务
				newTaskPlayer.set(newTask);
//				newTask.set(newTaskPlayer);
				//newTaskPlayer与newTask互相关联
			}
			
			//给挂断的老播放器、剩下的空闲播放器重新标locationIndex			
			for(CommandGroupUserPlayerPO needClosePlayer : needClosePlayers){
				needClosePlayer.setLocationIndex(newTaskIndex++);
			}
			for(CommandGroupUserPlayerPO idlePlayer : idlePlayers){
				idlePlayer.setLocationIndex(newTaskIndex++);
			}
			
			//呼叫所有的newTaskPlayers，挂断所有的NONE的needClosePlayers；进行相应的上屏处理；字幕下发
			CodecParamBO codec = commandCommonServiceImpl.queryDefaultAvCodecParamBO();			
			LogicBO logic = openAndCloseDecoder(newTaskPlayers, needClosePlayers, codec);
//			log.info("对比分页 logic: " + JSON.toJSON(logic));
			executeBusiness.execute(logic, "刷新播放器分页");
			
			
			for(CommandGroupUserPlayerPO needClosePlayer : needClosePlayers){
				needClosePlayer.setFree();
			}
			
			//持久化
			pageInfoDao.save(userInfo);
			
			return logic;
		}
	}*/
	
	/** 对比 */
	private LogicBO comparePage(
			PageInfoPO pageInfo,
			List<PageTaskPO> _oldPageTasks,
			List<PageTaskPO> newPageTasks,
			List<PageTaskPO> remainTasks,
			List<PageTaskPO> addTasks,
			List<PageTaskPO> closeTasks) throws Exception{
		
		synchronized (pageInfo.getOriginId()) {//TODO:这个锁需要再考虑
			
			if(remainTasks == null) remainTasks = new ArrayList<PageTaskPO>();
			if(addTasks == null) addTasks = new ArrayList<PageTaskPO>();
			if(closeTasks == null) closeTasks = new ArrayList<PageTaskPO>();
			
			List<PageTaskPO> oldPageTasks = new ArrayList<PageTaskPO>(_oldPageTasks);
			for(PageTaskPO oldPageTask : oldPageTasks){
				oldPageTask.setShowing(false);
				oldPageTask.clearDst();
			}
			int index=0;
			for(PageTaskPO newPageTask : newPageTasks){
				newPageTask.setShowing(true);
				newPageTask.setLocationIndex(index++);
			}
//			for(CommandGroupUserPlayerPO _oldPlayer : _oldPageUsingPlayers){
//				oldPlayers.add(_oldPlayer);
//			}
			
			//把所有的task的LocationIndex置为null
//			List<CommandPlayerTaskPO> tasks = pageInfo.getTasks();
//			for(PageTaskPO task : oldPageTasks){
//				task.setLocationIndex(null);
//			}
			
//			List<CommandGroupUserPlayerPO> allPlayers = userInfo.getPlayers();
//			Collections.sort(allPlayers, new CommandGroupUserPlayerPO.PlayerComparatorFromIndex());
			
//			List<CommandGroupUserPlayerPO> oldPlayers = new ArrayList<CommandGroupUserPlayerPO>();
//			List<CommandGroupUserPlayerPO> idlePlayers = new ArrayList<CommandGroupUserPlayerPO>();
//			for(CommandGroupUserPlayerPO player : allPlayers){
//				if(!player.getPlayerBusinessType().equals(PlayerBusinessType.NONE)){
//					oldPlayers.add(player);
//				}else{
//					idlePlayers.add(player);
//				}
//			}
			
			//保留转发关系不变的播放器（index可能改变）（暂时无用）
//			List<CommandGroupUserPlayerPO> remainPlayers = new ArrayList<CommandGroupUserPlayerPO>();
			
			//可能需要关闭的播放器
//			List<CommandGroupUserPlayerPO> closePlayers = new ArrayList<CommandGroupUserPlayerPO>();
			//本页上的新任务
//			List<CommandPlayerTaskPO> newTasks = new ArrayList<CommandPlayerTaskPO>();
			
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
					addTasks.add(newPageTask);
				}
			}
			
			closeTasks.addAll(oldPageTasks);
			
			//老播放器，一部分用来做新业务，剩余的挂断
			List<String> bundleIds = new ArrayList<String>();
			for(PageTaskPO closeTask : closeTasks){
				bundleIds.add(closeTask.getDstBundleId());
			}
			//TODO:这个查询再优化一下条件
			List<TerminalBundleUserPermissionPO> closePlayers = terminalBundleUserPermissionDao.findByBundleIdIn(bundleIds);
			
			//TODO: 空闲的播放器
			List<TerminalBundleUserPermissionPO> idlePlayers = new ArrayList<TerminalBundleUserPermissionPO>();
			
			//用来做新业务的所有播放器，可能包含老播放器，以及未使用的播放器
			List<TerminalBundleUserPermissionPO> newTaskPlayers = new ArrayList<TerminalBundleUserPermissionPO>();
			
			//需要挂断的老播放器
			List<TerminalBundleUserPermissionPO> needClosePlayers = new ArrayList<TerminalBundleUserPermissionPO>();
										
			if(addTasks.size() <= closeTasks.size()){
				//直接用来做新业务的老播放器
				List<TerminalBundleUserPermissionPO> old4newPlayers = closePlayers.subList(0, addTasks.size());
				newTaskPlayers.addAll(old4newPlayers);
				
				//需要挂断的老播放器
				needClosePlayers = closePlayers.subList(addTasks.size(), closePlayers.size());
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
				//TODO:使用newTaskPlayer进行addTask的任务
//				newTaskPlayer.set(newTask);
				addTask.setDst(newTaskPlayer, bundlePOs.get(0));
//				newTask.set(newTaskPlayer);
				//newTaskPlayer与newTask互相关联
			}
			
			//给挂断的老播放器、剩下的空闲播放器重新标locationIndex（上边已经标过）
//			for(CommandGroupUserPlayerPO needClosePlayer : needClosePlayers){
//				needClosePlayer.setLocationIndex(newTaskIndex++);
//			}
//			for(CommandGroupUserPlayerPO idlePlayer : idlePlayers){
//				idlePlayer.setLocationIndex(newTaskIndex++);
//			}
			
			//呼叫所有的newTaskPlayers，挂断所有的NONE的needClosePlayers；进行相应的上屏处理；字幕下发
			CodecParamBO codec = commandCommonServiceImpl.queryDefaultAvCodecParamBO();			
			LogicBO logic = openAndCloseDecoder(addTasks, closeTasks, codec);
//			log.info("对比分页 logic: " + JSON.toJSON(logic));
			executeBusiness.execute(logic, "刷新播放器分页");
			
			
//			for(CommandGroupUserPlayerPO needClosePlayer : needClosePlayers){
//				needClosePlayer.setFree();
//			}
			
			//持久化
			pageInfoDao.save(pageInfo);
			
			return logic;
		}
	}
	
	/** 改变执行状态后，重呼解码器。并不会推送分页信息。只对showing = true的下发 */
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
		LogicBO logic = openAndCloseDecoder(pageTasks, null, codec);
		
		if(doProtocol){
			executeBusiness.execute(logic, "改变执行状态后，重呼解码器");
		}
		
		return logic;
	}

	private void markIndex(List<PageTaskPO> tasks){
		int i = 0;
		for(PageTaskPO task : tasks){
			task.setTaskIndex(i++);
		}
	}
	
	//TODO:切分屏，参考 CommandSplitServiceImpl.changeLayoutScheme
	
	//TODO:特殊跳转，调用jumpToPage
	
	/*private LogicBO openAndCloseDecoder(
			List<CommandGroupUserPlayerPO> openPlayers,
			List<CommandGroupUserPlayerPO> closePlayers,
			CodecParamBO codec){
		
		if(openPlayers == null) openPlayers = new ArrayList<CommandGroupUserPlayerPO>();
		if(closePlayers == null) closePlayers = new ArrayList<CommandGroupUserPlayerPO>();
		
		LogicBO logic = new LogicBO().setUserId("-1")
		 		 .setConnectBundle(new ArrayList<ConnectBundleBO>())
		 		 .setDisconnectBundle(new ArrayList<DisconnectBundleBO>())
		 		 .setPass_by(new ArrayList<PassByBO>());
		
		for(CommandGroupUserPlayerPO openPlayer : openPlayers){
			ConnectBundleBO connectDecoderBundle = new ConnectBundleBO().setBusinessType(ConnectBundleBO.BUSINESS_TYPE_VOD)
//			  													        .setOperateType(ConnectBundleBO.OPERATE_TYPE)
			  													        .setLock_type("write")
			  													        .setBundleId(openPlayer.getBundleId())
			  													        .setLayerId(openPlayer.getLayerId())
			  													        .setBundle_type(openPlayer.getBundleType());
			ForwardSetSrcBO decoderVideoForwardSet = null;
			if(openPlayer.getSrcVideoBundleId() != null){
				decoderVideoForwardSet = new ForwardSetSrcBO().setType("channel")
						.setBundleId(openPlayer.getSrcVideoBundleId())
						.setLayerId(openPlayer.getSrcVideoLayerId())
						.setChannelId(openPlayer.getSrcVideoChannelId());
			}
			ConnectBO connectDecoderVideoChannel = new ConnectBO().setChannelId(openPlayer.getVideoChannelId())
														          .setChannel_status("Open")
														          .setBase_type(openPlayer.getVideoBaseType())
														          .setCodec_param(codec)
														          .setSource_param(decoderVideoForwardSet);
			ForwardSetSrcBO decoderAudioForwardSet = null;
			if(openPlayer.getSrcAudioBundleId() != null){
				decoderAudioForwardSet = new ForwardSetSrcBO().setType("channel")
						.setBundleId(openPlayer.getSrcAudioBundleId())
						.setLayerId(openPlayer.getSrcAudioLayerId())
						.setChannelId(openPlayer.getSrcAudioChannelId());
			}
			ConnectBO connectDecoderAudioChannel = new ConnectBO().setChannelId(openPlayer.getAudioChannelId())
																  .setChannel_status("Open")
																  .setBase_type(openPlayer.getAudioBaseType())
																  .setCodec_param(codec)
																  .setSource_param(decoderAudioForwardSet);
			
			connectDecoderBundle.setChannels(new ArrayListWrapper<ConnectBO>().add(connectDecoderVideoChannel).add(connectDecoderAudioChannel).getList());
			logic.getConnectBundle().add(connectDecoderBundle);
		}
		
		for(CommandGroupUserPlayerPO closePlayer : closePlayers){
			DisconnectBundleBO disconnectDecoderBundle = new DisconnectBundleBO().setBusinessType(DisconnectBundleBO.BUSINESS_TYPE_VOD)
//																           	 .setOperateType(DisconnectBundleBO.OPERATE_TYPE)
																           	 .setBundleId(closePlayer.getBundleId())
																           	 .setBundle_type(closePlayer.getBundleType())
																           	 .setLayerId(closePlayer.getLayerId());
			logic.getDisconnectBundle().add(disconnectDecoderBundle);
		}
		
		return logic;
	}*/
	
	
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
			//TODO:播放文件不需要呼叫
			ConnectBundleBO connectDecoderBundle = new ConnectBundleBO().setBusinessType(ConnectBundleBO.BUSINESS_TYPE_VOD)
//			  													        .setOperateType(ConnectBundleBO.OPERATE_TYPE)
			  													        .setLock_type("write")
			  													        .setBundleId(openTask.getDstBundleId())
			  													        .setLayerId(openTask.getDstLayerId())
			  													        .setBundle_type(openTask.getDstBundleType());
			ForwardSetSrcBO decoderVideoForwardSet = null;
			if(openTask.getSrcVideoBundleId() != null && ExecuteStatus.DONE.equals(openTask.getVideoStatus())){
				decoderVideoForwardSet = new ForwardSetSrcBO().setType("channel")
						.setBundleId(openTask.getSrcVideoBundleId())
						.setLayerId(openTask.getSrcVideoLayerId())
						.setChannelId(openTask.getSrcVideoChannelId());
			}
			ConnectBO connectDecoderVideoChannel = new ConnectBO().setChannelId(openTask.getDstVideoChannelId())
														          .setChannel_status("Open")
														          .setBase_type(openTask.getDstVideoBaseType())
														          .setCodec_param(codec)
														          .setSource_param(decoderVideoForwardSet);
			ForwardSetSrcBO decoderAudioForwardSet = null;
			if(openTask.getSrcAudioBundleId() != null && ExecuteStatus.DONE.equals(openTask.getAudioStatus())){
				decoderAudioForwardSet = new ForwardSetSrcBO().setType("channel")
						.setBundleId(openTask.getSrcAudioBundleId())
						.setLayerId(openTask.getSrcAudioLayerId())
						.setChannelId(openTask.getSrcAudioChannelId());
			}
			ConnectBO connectDecoderAudioChannel = new ConnectBO().setChannelId(openTask.getDstAudioChannelId())
																  .setChannel_status("Open")
																  .setBase_type(openTask.getDstAudioBaseType())
																  .setCodec_param(codec)
																  .setSource_param(decoderAudioForwardSet);
			
			connectDecoderBundle.setChannels(new ArrayListWrapper<ConnectBO>().add(connectDecoderVideoChannel).add(connectDecoderAudioChannel).getList());
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
	
}
