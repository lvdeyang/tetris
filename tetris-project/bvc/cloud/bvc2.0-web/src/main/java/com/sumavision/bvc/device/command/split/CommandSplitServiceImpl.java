package com.sumavision.bvc.device.command.split;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.suma.venus.resource.base.bo.UserBO;
import com.sumavision.bvc.command.group.basic.CommandGroupPO;
import com.sumavision.bvc.command.group.dao.CommandGroupDAO;
import com.sumavision.bvc.command.group.dao.CommandGroupUserInfoDAO;
import com.sumavision.bvc.command.group.user.CommandGroupUserInfoPO;
import com.sumavision.bvc.command.group.user.layout.player.CommandGroupUserPlayerCastDevicePO;
import com.sumavision.bvc.command.group.user.layout.player.CommandGroupUserPlayerPO;
import com.sumavision.bvc.command.group.user.layout.player.PlayerBusinessType;
import com.sumavision.bvc.command.group.user.layout.scheme.CommandGroupUserLayoutShemePO;
import com.sumavision.bvc.command.group.user.layout.scheme.PlayerSplitLayout;
import com.sumavision.bvc.control.utils.UserUtils;
import com.sumavision.bvc.device.command.basic.CommandBasicServiceImpl;
import com.sumavision.bvc.device.command.basic.forward.CommandForwardServiceImpl;
import com.sumavision.bvc.device.command.bo.PlayerInfoBO;
import com.sumavision.bvc.device.command.cast.CommandCastServiceImpl;
import com.sumavision.bvc.device.command.common.CommandCommonUtil;
import com.sumavision.bvc.device.command.record.CommandRecordServiceImpl;
import com.sumavision.bvc.device.command.user.CommandUserServiceImpl;
import com.sumavision.bvc.device.command.vod.CommandVodService;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;

import lombok.extern.slf4j.Slf4j;

/**
 * 
* @ClassName: CommandSplitServiceImpl 
* @Description: 分屏相关业务
* @author zsy
* @date 2019年11月15日 上午10:56:48 
*
 */
@Slf4j
@Service
public class CommandSplitServiceImpl {
	
	@Autowired
	private CommandGroupDAO commandGroupDao;
	
	@Autowired
	private CommandGroupUserInfoDAO commandGroupUserInfoDao;
	
	@Autowired
	private CommandVodService commandVodService;
	
	@Autowired
	private CommandUserServiceImpl commandUserServiceImpl;
	
	@Autowired
	private CommandRecordServiceImpl commandRecordServiceImpl;
	
	@Autowired
	private CommandBasicServiceImpl commandBasicServiceImpl;
	
	@Autowired
	private CommandForwardServiceImpl commandForwardServiceImpl;
	
	@Autowired
	private CommandCastServiceImpl commandCastServiceImpl;

	@Autowired
	private CommandCommonUtil commandCommonUtil;

	@Autowired
	private UserUtils userUtils;
	
	/**
	 * 切换分屏方案<br/>
	 * <p>如果当前有“观看本地视频”的播放器，则会把它放到新分屏中的最后一个播放器</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月19日 上午9:20:34
	 * @param userId
	 * @param split
	 * @return
	 * @throws Exception
	 */
	@Transactional(rollbackFor = Exception.class)
	public CommandGroupUserInfoPO changeLayoutScheme(Long userId, int split) throws Exception{
		
		synchronized (userId) {
			
			PlayerSplitLayout newSplitLayout = PlayerSplitLayout.fromId(split);
			
			CommandGroupUserInfoPO userInfo = commandGroupUserInfoDao.findByUserId(userId);
			List<CommandGroupUserPlayerPO> allPlayers = userInfo.getPlayers();
			CommandGroupUserLayoutShemePO oldScheme = null;
			for(CommandGroupUserLayoutShemePO scheme : userInfo.getLayoutSchemes()){
				if(scheme.getIsUsing()){
					oldScheme = scheme;
					break;
				}
			}
			
			//当前方案的所有播放器
			List<CommandGroupUserPlayerPO> oldPlayers = oldScheme.obtainPlayers();
			//当前正在使用的播放器
			List<CommandGroupUserPlayerPO> oldUsingPlayers = new ArrayList<CommandGroupUserPlayerPO>();
			List<CommandGroupUserPlayerPO> oldUnusedPlayers = new ArrayList<CommandGroupUserPlayerPO>();
			for(CommandGroupUserPlayerPO oldPlayer : oldPlayers){
				if(!oldPlayer.getPlayerBusinessType().equals(PlayerBusinessType.NONE)){
					oldUsingPlayers.add(oldPlayer);
				}else{
					oldUnusedPlayers.add(oldPlayer);
				}
			}
			
			//如果切换后播放器不够用，则抛错
			int oldUsingPlayersCount = oldUsingPlayers.size();
			if(oldUsingPlayersCount > newSplitLayout.getPlayerCount()){
				throw new BaseException(StatusCode.FORBIDDEN, oldUsingPlayersCount + "个播放器正在使用，请先停止一些业务");
			}
			
			//查找切换后的方案，如果没有则生成一个
			CommandGroupUserLayoutShemePO newScheme = commandCommonUtil.querySchemeByPlayerSplitLayout(userInfo, newSplitLayout);
			if(newScheme == null){
				CommandGroupUserLayoutShemePO ns =commandUserServiceImpl.generateUserLayoutScheme(userInfo, newSplitLayout);
				userInfo.getLayoutSchemes().add(ns);
				commandGroupUserInfoDao.save(userInfo);
				newScheme = commandCommonUtil.querySchemeByPlayerSplitLayout(userInfo, newSplitLayout);
			}
			
			//切分屏，如果当前有“观看本地视频”的播放器，则会把它放到新分屏中的最后一个播放器
			Collections.sort(oldUsingPlayers, new CommandGroupUserPlayerPO.PlayerComparatorFromIndex());
			int i = 0;
			CommandGroupUserPlayerPO selfPlayer = null;
			for(CommandGroupUserPlayerPO oldUsingPlayer : oldUsingPlayers){
				if(oldUsingPlayer.getPlayerBusinessType().equals(PlayerBusinessType.PLAY_USER_ONESELF)){
					selfPlayer = oldUsingPlayer;
				}else{
					oldUsingPlayer.setLocationIndex(i++);
				}
			}
			if(selfPlayer!=null){
				selfPlayer.setLocationIndex(i++);
			}
			for(CommandGroupUserPlayerPO oldUnusedPlayer : oldUnusedPlayers){
				oldUnusedPlayer.setLocationIndex(i++);
			}
			if(selfPlayer != null){
				int lastIndex = newScheme.getPlayerSplitLayout().getPlayerCount() - 1;
				CommandGroupUserPlayerPO player = commandCommonUtil.queryPlayerByLocationIndex(allPlayers, lastIndex);
				player.setLocationIndex(selfPlayer.getLocationIndex());
				selfPlayer.setLocationIndex(lastIndex);				
			}
			oldScheme.setIsUsing(false);
			newScheme.setIsUsing(true);
			commandGroupUserInfoDao.save(userInfo);
			
			return userInfo;
			
		}		
	}
		
	/**
	 * 交换2个分屏的位置<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月19日 上午9:20:01
	 * @param userId
	 * @param oldIndex
	 * @param newIndex
	 * @throws Exception
	 */
	@Transactional(rollbackFor = Exception.class)
	public void exchangeTwoSplit(Long userId, int oldIndex, int newIndex) throws Exception{

		synchronized (userId) {
			
			CommandGroupUserInfoPO userInfo = commandGroupUserInfoDao.findByUserId(userId);
			CommandGroupUserPlayerPO oldPlayer = null;
			CommandGroupUserPlayerPO newPlayer = null;
			//当前方案的所有播放器
			List<CommandGroupUserPlayerPO> players = userInfo.obtainUsingSchemePlayers();
			for(CommandGroupUserPlayerPO player : players){
				if(player.getLocationIndex() == oldIndex){
//					player.setLocationIndex(newIndex);
					newPlayer = player;
				}else if(player.getLocationIndex() == newIndex){
//					player.setLocationIndex(oldIndex);
					oldPlayer = player;
				}
			}
			
			List<CommandGroupUserPlayerCastDevicePO> oldDevices = oldPlayer.getCastDevices();
			if(oldDevices == null) oldDevices = new ArrayList<CommandGroupUserPlayerCastDevicePO>();
			List<CommandGroupUserPlayerCastDevicePO> newDevices = newPlayer.getCastDevices();
			if(newDevices == null) newDevices = new ArrayList<CommandGroupUserPlayerCastDevicePO>();
			
			List<CommandGroupUserPlayerCastDevicePO> oldDevicesCopy = new ArrayList<CommandGroupUserPlayerCastDevicePO>();
			List<String> oldBundleIds = new ArrayList<String>();
			for(CommandGroupUserPlayerCastDevicePO oldDevice : oldDevices){
				oldDevicesCopy.add(oldDevice);
				oldBundleIds.add(oldDevice.getDstBundleId());
			}
			List<CommandGroupUserPlayerCastDevicePO> newDevicesCopy = new ArrayList<CommandGroupUserPlayerCastDevicePO>();
			List<String> newBundleIds = new ArrayList<String>();
			for(CommandGroupUserPlayerCastDevicePO newDevice : newDevices){
				newDevicesCopy.add(newDevice);
				newBundleIds.add(newDevice.getDstBundleId());
			}
			
			PlayerInfoBO newPlayerInfoBO = commandCastServiceImpl.changeCastDevices2(newPlayer, null, null, false, true, true);
			PlayerInfoBO oldPlayerInfoBO = commandCastServiceImpl.changeCastDevices2(oldPlayer, null, null, false, true, true);
			
			//先解绑
			commandCastServiceImpl.changeCastDevices2(newPlayer, null, newDevicesCopy, true, !oldPlayerInfoBO.isHasBusiness(), false);
			commandCastServiceImpl.changeCastDevices2(oldPlayer, null, oldDevicesCopy, true, !newPlayerInfoBO.isHasBusiness(), false);
			
			//再绑新的
			commandCastServiceImpl.editCastDevices(newPlayer, oldBundleIds, null);
			commandCastServiceImpl.editCastDevices(oldPlayer, newBundleIds, null);
			
			newPlayer.setLocationIndex(newIndex);
			oldPlayer.setLocationIndex(oldIndex);
			
			commandGroupUserInfoDao.save(userInfo);
			
		}
	}
	
	/**
	 * 清空所有的播放器及其业务<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年5月25日 上午11:50:36
	 * @param userId
	 * @throws Exception
	 */
	public void clearAllPlayers(Long userId) throws Exception{
		UserBO user = userUtils.queryUserById(userId);
		UserBO admin = new UserBO(); admin.setId(-1L);
		log.info(user + "一键清屏");
		CommandGroupUserInfoPO userInfo = commandGroupUserInfoDao.findByUserId(userId);
		//当前方案的所有播放器
		List<CommandGroupUserPlayerPO> players = userInfo.obtainUsingSchemePlayers();
		for(CommandGroupUserPlayerPO player : players){
			String businessId = player.getBusinessId();
			
			switch(player.getPlayerBusinessType()){
			case PLAY_COMMAND_RECORD:
				commandRecordServiceImpl.stopPlayFragments(userId, new ArrayListWrapper<String>().add(businessId).getList());
				break;
			case PLAY_FILE:
				commandVodService.resourceVodStop(user, businessId);
				break;
			case PLAY_USER:
			case PLAY_USER_ONESELF:
				commandVodService.userStop(user, Long.parseLong(businessId), admin);
				break;
			case PLAY_DEVICE:
				commandVodService.deviceStop(user, Long.parseLong(businessId), admin);
				break;
			case PLAY_RECORD:
				commandVodService.recordVodStop(user, player.getLocationIndex());
				break;
			case USER_CALL:
				commandUserServiceImpl.stopCall_Cascade(user, Long.parseLong(businessId), null);
				break;
			case USER_VOICE:
				commandUserServiceImpl.stopVoice(user, Long.parseLong(businessId), admin);
				break;
			case BASIC_COMMAND:
				Long groupId = Long.parseLong(businessId);
				CommandGroupPO group = commandGroupDao.findOne(groupId);
				Long memberUserId = group.getUserId();//得到主席的userId
				commandBasicServiceImpl.vodMemberStop(userId, groupId, memberUserId);
				break;
			case CHAIRMAN_BASIC_COMMAND:
			case COOPERATE_COMMAND:
			case SPEAK_MEETING:
				Long groupId2 = Long.parseLong(businessId.split("-")[0]);
				Long memberUserId2 = Long.parseLong(businessId.split("-")[1]);
				commandBasicServiceImpl.vodMemberStop(userId, groupId2, memberUserId2);
				break;
			case SECRET_COMMAND:
				commandBasicServiceImpl.stop(userId, Long.parseLong(businessId), 0);
				break;
			case COMMAND_FORWARD_DEVICE:
			case COMMAND_FORWARD_FILE:
			case COMMAND_FORWARD_USER:
				Long demandId = Long.parseLong(businessId.split("-")[1]);
				commandForwardServiceImpl.stopByMember(userId, new ArrayListWrapper<Long>().add(demandId).getList());
				break;
			default:
				break;
			}
		}
	}
}
