package com.sumavision.tetris.bvc.page;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
import com.sumavision.tetris.bvc.business.BusinessInfoType;
import com.sumavision.tetris.bvc.business.ExecuteStatus;
import com.sumavision.tetris.bvc.business.common.BusinessCommonService;
import com.sumavision.tetris.bvc.business.terminal.user.TerminalBundleUserPermissionDAO;
import com.sumavision.tetris.bvc.business.terminal.user.TerminalBundleUserPermissionPO;
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
	private CommandGroupUserInfoDAO commandGroupUserInfoDao;	
	
	@Autowired
	private PageInfoDAO pageInfoDao;
	
	@Autowired
	private PageTaskDAO pageTaskDao;	
	
	@Autowired
	private TerminalBundleUserPermissionDAO terminalBundleUserPermissionDao;
	
	@Autowired
	private ResourceBundleDAO resourceBundleDao;
	
	@Autowired
	private BusinessCommonService businessCommonService;
	
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
	private TetrisBvcQueryUtil tetrisBvcQueryUtil;
	
	@Autowired
	private ExecuteBusinessProxy executeBusiness;
	
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
		comparePage(pageInfo, oldTaskPOs, newPageTasks, true);
		
		//notifyUser，后续要改
		Long userId = Long.parseLong(pageInfo.getOriginId());
		CommandGroupUserInfoPO userInfo = commandGroupUserInfoDao.findByUserId(userId);
		notifyUser(userInfo, pageInfo, true);
		
	}
	
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
		
		//notifyUser，后续要改
		Long userId = Long.parseLong(pageInfo.getOriginId());
		CommandGroupUserInfoPO userInfo = commandGroupUserInfoDao.findByUserId(userId);
		notifyUser(userInfo, pageInfo, true);
		
	}
	
	/** 总页数 */
	public int getTotalPageNumber(PageInfoPO pageInfo){
		int lastIndex = pageInfo.getPageTasks().size() - 1;
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
//			return tasks.subList((pageNumber-1)*pageSize, pageNumber*pageSize);
			List<PageTaskPO> subTasks = tasks.subList((pageNumber-1)*pageSize, pageNumber*pageSize);
			List<PageTaskPO> result = new ArrayList<PageTaskPO>(subTasks);
			return result;
		}else{
//			return tasks.subList((pageNumber-1)*pageSize, tasks.size());
			List<PageTaskPO> subTasks = tasks.subList((pageNumber-1)*pageSize, tasks.size());
			List<PageTaskPO> result = new ArrayList<PageTaskPO>(subTasks);
			return result;
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
//				tasks.add(newTask);
			}else{
				//直接加在最后
				tasks.add(newTask);
				if(jumpTo == null) jumpTo = newTask;
			}
		}
		
		markIndex(tasks);
		
		//持久化
		pageTaskDao.save(tasks);
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
			
			List<PageTaskPO> remainTasks = new ArrayList<PageTaskPO>();
			List<PageTaskPO> addTasks = new ArrayList<PageTaskPO>();
			List<PageTaskPO> closeTasks = new ArrayList<PageTaskPO>();
			List<PageTaskPO> needCloseTasks = new ArrayList<PageTaskPO>();//需要挂断的播放器
			
			List<PageTaskPO> oldPageTasks = new ArrayList<PageTaskPO>();//(_oldPageTasks);
			List<PageTaskPO> showingPageTasks = new ArrayList<PageTaskPO>();
			for(PageTaskPO _oldPageTask : _oldPageTasks){
				oldPageTasks.add(_oldPageTask);
				showingPageTasks.add(_oldPageTask);
			}
			
			int index=0;
			for(PageTaskPO newPageTask : newPageTasks){
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
					addTasks.add(newPageTask);
				}
			}
			
			closeTasks.addAll(oldPageTasks);
			
			//老播放器，一部分用来做新业务，剩余的挂断
			List<String> oldBundleIds = new ArrayList<String>();
			for(PageTaskPO closeTask : closeTasks){
				oldBundleIds.add(closeTask.getDstBundleId());
			}
			//TODO:这个查询再优化一下条件
			List<TerminalBundleUserPermissionPO> closePlayers = terminalBundleUserPermissionDao.findByBundleIdIn(oldBundleIds);
			
			//TODO: 空闲的播放器
			List<TerminalBundleUserPermissionPO> idlePlayers = new ArrayList<TerminalBundleUserPermissionPO>();
			List<TerminalBundleUserPermissionPO> allPlayers = terminalBundleUserPermissionDao.findByUserIdAndTerminalId(pageInfo.getOriginId(), pageInfo.getTerminalId());
//			List<PageTaskPO> tasks = pageInfo.getPageTasks();
			for(TerminalBundleUserPermissionPO aPlayer : allPlayers){
				if(aPlayer.getBundleType().equals("player")){//后续改成从TerminalBundle里边判断type:DECODER/ENCODER
					PageTaskPO task = tetrisBvcQueryUtil.queryPageTaskById(showingPageTasks, aPlayer.getId().toString());
					if(task == null){
						idlePlayers.add(aPlayer);
					}
				}
			}
			
			//用来做新业务的所有播放器，可能包含老播放器，以及未使用的播放器
			List<TerminalBundleUserPermissionPO> newTaskPlayers = new ArrayList<TerminalBundleUserPermissionPO>();
			
			//需要挂断的老播放器
			List<TerminalBundleUserPermissionPO> needClosePlayers = new ArrayList<TerminalBundleUserPermissionPO>();
			
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
				
				//需要挂断的老播放器
				needClosePlayers = closePlayers.subList(addTasks.size(), closePlayers.size());//这个变量没有用
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
			LogicBO logic = openAndCloseDecoder(addTasks, needCloseTasks, codec);
						
			for(PageTaskPO closeTask : closeTasks){
				closeTask.setShowing(false);
				closeTask.clearDst();
			}
			
			//持久化
			pageInfoDao.save(pageInfo);
			
			if(doProtocal) executeBusiness.execute(logic, "刷新播放器分页");
			
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
		LogicBO logic = openAndCloseDecoder(pageTasks, null, codec);
		
		if(doProtocol){
			executeBusiness.execute(logic, "改变执行状态后，重呼解码器");
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
		int i = pageTasks.size();
		for(BundlePO bundle : bundles){
			vo.getPlayers().add(new CommandGroupUserPlayerSettingVO().setIdlePlayer(i++, bundle));
		}
		
		return vo;
	}

	private void markIndex(List<PageTaskPO> tasks){
		int i = 0;
		for(PageTaskPO task : tasks){
			task.setTaskIndex(i++);
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
