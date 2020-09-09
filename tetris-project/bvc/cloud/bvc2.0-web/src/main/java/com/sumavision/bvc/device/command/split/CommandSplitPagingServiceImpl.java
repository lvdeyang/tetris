package com.sumavision.bvc.device.command.split;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
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
import com.sumavision.bvc.system.po.AvtplGearsPO;
import com.sumavision.bvc.system.po.AvtplPO;
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
public class CommandSplitPagingServiceImpl {
	
	@Autowired
	private CommandGroupUserInfoDAO commandGroupUserInfoDao;
	
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
	
	public MessageSendCacheBO notifyUser(CommandGroupUserInfoPO userInfo, boolean doWebsocket) throws Exception{
		
		CommandGroupUserLayoutShemePO usingScheme = userInfo.obtainUsingScheme();
		CommandGroupUserLayoutShemeVO schemeVO = new CommandGroupUserLayoutShemeVO().set(usingScheme);
		
		JSONObject message = new JSONObject();
		message.put("businessType", "refreshSplit");
		message.put("layout", schemeVO);
		log.info("layout: " + JSON.toJSONString(schemeVO));
		
		MessageSendCacheBO cache = new MessageSendCacheBO(userInfo.getUserId(), message.toJSONString(), WebsocketMessageType.COMMAND);
	
		//发送消息
		if(doWebsocket){
			WebsocketMessageVO ws = websocketMessageService.send(userInfo.getUserId(), message.toJSONString(), WebsocketMessageType.COMMAND);
			websocketMessageService.consume(ws.getId());
		}
		
		return cache;
	}
	
	public void addAndRemoveTasks(CommandGroupUserInfoPO userInfo, List<CommandPlayerTaskBO> newTasks, List<CommandPlayerTaskPO> removeTasks) throws Exception{
		
		if(newTasks == null) newTasks = new ArrayList<CommandPlayerTaskBO>();
		if(removeTasks == null) removeTasks = new ArrayList<CommandPlayerTaskPO>();
		
//		//查找该用户配置信息
//		CommandGroupUserInfoPO userInfo = commandGroupUserInfoDao.findByUserId(userId);
//		if(null == userInfo){
//			//如果没有则建立默认
////			userInfo = commandUserServiceImpl.generateDefaultUserInfo(user.getId(), user.getName(), true);
//		}
		
		Integer currentPage = userInfo.getCurrentPage();
		if(currentPage == null){
			currentPage = 1;
			userInfo.setCurrentPage(1);
		}
		
//		removeTask
		removeTasks(userInfo, removeTasks);
		
//		addTask
		List<CommandPlayerTaskPO> newTaskPOs = new ArrayList<CommandPlayerTaskPO>();
		for(CommandPlayerTaskBO newTaskBO : newTasks){
			newTaskPOs.add(new CommandPlayerTaskPO().set(newTaskBO));
		}
		CommandPlayerTaskPO jumpTo = addTask(userInfo, newTaskPOs);
		
		//总页数
		int newPageCount = userInfo.obtainTotalPageCount();
		
		//要跳转到的页数
		int newCurrentPage = 1;
		if(jumpTo != null){
			newCurrentPage = getPageNumberByIndex(userInfo, jumpTo.getTaskIndex());
		}
		else if(currentPage > newPageCount){
			newCurrentPage = newPageCount;
		}
		else{
			newCurrentPage = currentPage;
		}
		
		userInfo.setCurrentPage(newCurrentPage);
		
		//持久化
		commandGroupUserInfoDao.save(userInfo);
		
		jumpToPage(userInfo, newCurrentPage);
	}
	
	public void jumpToPage(CommandGroupUserInfoPO userInfo, int toPage) throws Exception{
		//对比新老页
		//筛选出正在使用的播放器
		List<CommandGroupUserPlayerPO> oldPageUsingPlayers = new ArrayList<CommandGroupUserPlayerPO>();
		List<CommandGroupUserPlayerPO> players = userInfo.obtainUsingSchemePlayers();
		for(CommandGroupUserPlayerPO player : players){
			if(!player.getPlayerBusinessType().equals(PlayerBusinessType.NONE)){
				oldPageUsingPlayers.add(player);
			}
		}
		List<CommandPlayerTaskPO> newPageTasks = getPageTasks(userInfo, toPage, true);
		comparePage(userInfo, newPageTasks);
	}
	
	/** 计算第index个任务在第几页 */
	private int getPageNumberByIndex(CommandGroupUserInfoPO userInfo, int index){
		int playerCount = userInfo.obtainUsingScheme().getPlayerSplitLayout().getPlayerCount();
		return calculatePageNumber(playerCount, index);
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
	private List<CommandPlayerTaskPO> getPageTasks(CommandGroupUserInfoPO userInfo, int pageNumber, boolean throwException) throws BaseException{
		List<CommandPlayerTaskPO> tasks = userInfo.getTasks();//new ArrayList<CommandPlayerTaskPO>();//userInfo.getxxx
		Collections.sort(tasks, new CommandPlayerTaskPO.TaskComparatorFromIndex());
		int playerCount = userInfo.obtainUsingScheme().getPlayerSplitLayout().getPlayerCount();
		int totalPageCount = tasks.size()/playerCount;
		int m = tasks.size()%playerCount;
		if(m > 0) totalPageCount++;
		
		if(pageNumber==1 && totalPageCount==0){
			return new ArrayList<CommandPlayerTaskPO>();
		}
		
		//TODO:考虑总共只有0页的情况
		if(pageNumber > totalPageCount){
			if(throwException){
				throw new BaseException(StatusCode.FORBIDDEN, "总页数为" + totalPageCount + "，无法找到第" + pageNumber + "页");
			}else{
				return null;
			}
		}
		
		if(pageNumber < totalPageCount){
			return tasks.subList((pageNumber-1)*playerCount, pageNumber*playerCount);
		}else{
			return tasks.subList((pageNumber-1)*playerCount, tasks.size());
		}		
	}
	
	private CommandPlayerTaskPO addTask(CommandGroupUserInfoPO userInfo, List<CommandPlayerTaskPO> newTasks){
		
		CommandPlayerTaskPO jumpTo = null;
		
		List<CommandPlayerTaskPO> tasks = userInfo.getTasks();//new ArrayList<CommandPlayerTaskPO>();//userInfo.getxxx
		Collections.sort(tasks, new CommandPlayerTaskPO.TaskComparatorFromIndex());
		
		for(CommandPlayerTaskPO newTask : newTasks){
			newTask.setUserInfo(userInfo);
			PlayerBusinessType type = newTask.getPlayerBusinessType();
			
			if(type==null || PlayerBusinessType.NONE.equals(type)){
				//不处理
			}else if(type.isCommandOrMeeting()){
				//TODO:查找同一个指挥/会议的，加在后边
				String commandId = newTask.getBusinessId().split("-")[0];
				int index = -1;
				for(int i=tasks.size()-1; i>=0; i--){
					CommandPlayerTaskPO task = tasks.get(i);
					if(task.getPlayerBusinessType().isCommandOrMeeting()){
						String taskCommandId = task.getBusinessId().split("-")[0];
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
		commandGroupUserInfoDao.save(userInfo);
		
		return jumpTo;
	}
	
	private void removeTasks(CommandGroupUserInfoPO userInfo, List<CommandPlayerTaskPO> _removeTasks){
		
		List<CommandPlayerTaskPO> removeTasks = new ArrayList<CommandPlayerTaskPO>(_removeTasks);
		
		List<CommandPlayerTaskPO> tasks = userInfo.getTasks();//new ArrayList<CommandPlayerTaskPO>();//userInfo.getxxx
		Collections.sort(tasks, new CommandPlayerTaskPO.TaskComparatorFromIndex());
		
		for(int i=tasks.size()-1; i>=0; i--){
			CommandPlayerTaskPO task = tasks.get(i);
			for(CommandPlayerTaskPO removeTask : removeTasks){
				if(task.equalsTask(removeTask)){
					tasks.remove(i);
					removeTasks.remove(removeTask);
					break;
				}
			}
		}
		
		markIndex(tasks);
		
		//持久化
		commandGroupUserInfoDao.save(userInfo);
	}
	
	/** 对比 */
	private LogicBO comparePage(
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
				//TODO:使用newTaskPlayer进行newTask的任务
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
			LogicBO logic = openAndClosePlayer(newTaskPlayers, needClosePlayers, codec);
//			log.info("对比分页 logic: " + JSON.toJSON(logic));
			executeBusiness.execute(logic, "刷新播放器分页");
			
			
			for(CommandGroupUserPlayerPO needClosePlayer : needClosePlayers){
				needClosePlayer.setFree();
			}
			
			//持久化
			commandGroupUserInfoDao.save(userInfo);
			
			return logic;
		}
	}
	
	private void markIndex(List<CommandPlayerTaskPO> tasks){
		int i = 0;
		for(CommandPlayerTaskPO task : tasks){
			task.setTaskIndex(i++);
		}
	}
	
	//TODO:切分屏，参考 CommandSplitServiceImpl.changeLayoutScheme
	
	//TODO:特殊跳转，调用jumpToPage
	
	private LogicBO openAndClosePlayer(
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
	}
	
}
