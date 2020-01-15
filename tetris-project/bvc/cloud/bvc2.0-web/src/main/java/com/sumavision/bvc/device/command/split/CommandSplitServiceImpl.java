package com.sumavision.bvc.device.command.split;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sumavision.bvc.command.group.dao.CommandGroupUserInfoDAO;
import com.sumavision.bvc.command.group.user.CommandGroupUserInfoPO;
import com.sumavision.bvc.command.group.user.layout.player.CommandGroupUserPlayerPO;
import com.sumavision.bvc.command.group.user.layout.player.PlayerBusinessType;
import com.sumavision.bvc.command.group.user.layout.scheme.CommandGroupUserLayoutShemePO;
import com.sumavision.bvc.command.group.user.layout.scheme.PlayerSplitLayout;
import com.sumavision.bvc.device.command.common.CommandCommonUtil;
import com.sumavision.bvc.device.command.user.CommandUserServiceImpl;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
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
@Transactional(rollbackFor = Exception.class)
@Service
public class CommandSplitServiceImpl {
	
	@Autowired
	private CommandGroupUserInfoDAO commandGroupUserInfoDao;
	
	@Autowired
	private CommandUserServiceImpl commandUserServiceImpl;

	@Autowired
	private CommandCommonUtil commandCommonUtil;
	
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
	public void exchangeTwoSplit(Long userId, int oldIndex, int newIndex) throws Exception{

		synchronized (userId) {
			
			CommandGroupUserInfoPO userInfo = commandGroupUserInfoDao.findByUserId(userId);
			//当前方案的所有播放器
			List<CommandGroupUserPlayerPO> players = userInfo.obtainUsingSchemePlayers();
			for(CommandGroupUserPlayerPO player : players){
				if(player.getLocationIndex() == oldIndex){
					player.setLocationIndex(newIndex);
				}else if(player.getLocationIndex() == newIndex){
					player.setLocationIndex(oldIndex);
				}
			}
			
			commandGroupUserInfoDao.save(userInfo);
			
		}
	}
}
