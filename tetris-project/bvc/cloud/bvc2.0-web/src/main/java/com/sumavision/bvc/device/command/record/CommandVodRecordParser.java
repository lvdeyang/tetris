package com.sumavision.bvc.device.command.record;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.suma.venus.resource.base.bo.AccessNodeBO;
import com.suma.venus.resource.base.bo.UserBO;
import com.suma.venus.resource.dao.BundleDao;
import com.suma.venus.resource.dao.FolderUserMapDAO;
import com.suma.venus.resource.pojo.BundlePO;
import com.suma.venus.resource.pojo.FolderUserMap;
import com.suma.venus.resource.service.ResourceService;
import com.sumavision.bvc.command.group.basic.CommandGroupAvtplGearsPO;
import com.sumavision.bvc.command.group.basic.CommandGroupMemberPO;
import com.sumavision.bvc.command.group.basic.CommandGroupPO;
import com.sumavision.bvc.command.group.dao.CommandGroupDAO;
import com.sumavision.bvc.command.group.dao.CommandGroupRecordDAO;
import com.sumavision.bvc.command.group.dao.CommandGroupRecordFragmentDAO;
import com.sumavision.bvc.command.group.dao.CommandGroupUserInfoDAO;
import com.sumavision.bvc.command.group.dao.CommandGroupUserPlayerDAO;
import com.sumavision.bvc.command.group.enumeration.ExecuteStatus;
import com.sumavision.bvc.command.group.enumeration.GroupStatus;
import com.sumavision.bvc.command.group.forward.CommandGroupForwardPO;
import com.sumavision.bvc.command.group.record.CommandGroupRecordFragmentPO;
import com.sumavision.bvc.command.group.record.CommandGroupRecordPO;
import com.sumavision.bvc.command.group.user.CommandGroupUserInfoPO;
import com.sumavision.bvc.command.group.user.layout.player.CommandGroupUserPlayerPO;
import com.sumavision.bvc.command.group.user.layout.player.PlayerBusinessType;
import com.sumavision.bvc.control.device.monitor.device.ChannelVO;
import com.sumavision.bvc.control.device.monitor.record.MonitorRecordTaskVO;
import com.sumavision.bvc.control.utils.UserUtils;
import com.sumavision.bvc.control.welcome.UserVO;
import com.sumavision.bvc.device.command.bo.PlayerInfoBO;
import com.sumavision.bvc.device.command.cast.CommandCastServiceImpl;
import com.sumavision.bvc.device.command.common.CommandCommonServiceImpl;
import com.sumavision.bvc.device.command.common.CommandCommonUtil;
import com.sumavision.bvc.device.group.bo.CodecParamBO;
import com.sumavision.bvc.device.group.bo.LogicBO;
import com.sumavision.bvc.device.group.bo.RecordSetBO;
import com.sumavision.bvc.device.group.bo.RecordSourceBO;
import com.sumavision.bvc.device.group.service.test.ExecuteBusinessProxy;
import com.sumavision.bvc.device.group.service.util.QueryUtil;
import com.sumavision.bvc.device.group.service.util.ResourceQueryUtil;
import com.sumavision.bvc.device.monitor.playback.exception.AccessNodeIpMissingException;
import com.sumavision.bvc.device.monitor.playback.exception.AccessNodeNotExistException;
import com.sumavision.bvc.device.monitor.playback.exception.AccessNodePortMissionException;
import com.sumavision.bvc.device.monitor.record.MonitorRecordPO;
import com.sumavision.bvc.device.monitor.record.MonitorRecordService;
import com.sumavision.bvc.meeting.logic.ExecuteBusinessReturnBO;
import com.sumavision.bvc.meeting.logic.ExecuteBusinessReturnBO.ResultDstBO;
import com.sumavision.bvc.resource.dto.ChannelSchemeDTO;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

import lombok.extern.slf4j.Slf4j;

/**
 * 
* @ClassName: CommandVodRecordParser 
* @Description: 录制设备、用户。不加事物
* @author zsy
* @date 2020年4月29日 上午10:56:48 
*
 */
@Slf4j
@Service
public class CommandVodRecordParser {
		
	@Autowired
	private FolderUserMapDAO folderUserMapDao;
		
	@Autowired
	private BundleDao bundleDao;
		
	@Autowired
	private CommandGroupDAO commandGroupDao;
	
	@Autowired
	private CommandGroupRecordDAO commandGroupRecordDao;
	
	@Autowired
	private CommandGroupRecordFragmentDAO commandGroupRecordFragmentDao;
	
	@Autowired
	private CommandGroupUserInfoDAO commandGroupUserInfoDao;
	
	@Autowired
	private CommandGroupUserPlayerDAO commandGroupUserPlayerDao;
	
	@Autowired
	private CommandCommonUtil commandCommonUtil;
	
	@Autowired
	private ResourceQueryUtil resourceQueryUtil;
	
	@Autowired
	private UserUtils userUtils;
	
	@Autowired
	private QueryUtil queryUtil;
	
	@Autowired
	private CommandCommonServiceImpl commandCommonServiceImpl;
	
	@Autowired
	private CommandCastServiceImpl commandCastServiceImpl;
	
	@Autowired
	private MonitorRecordService monitorRecordService;

	@Autowired
	private ResourceService resourceService;

	@Autowired
	private ExecuteBusinessProxy executeBusiness;
	
	public MonitorRecordPO playerStartRecord(
//			Long userId,
			UserVO user,
			int locationIndex,
			String mode, 
			String fileName, 
			String startTime, 
			String endTime) throws Exception{
		
		CommandGroupUserInfoPO userInfo = commandGroupUserInfoDao.findByUserId(user.getId());
		CommandGroupUserPlayerPO player = commandCommonUtil.queryPlayerByLocationIndex(userInfo.getPlayers(), locationIndex);
		
		//从播放器找当前业务
		PlayerInfoBO playerInfoBO = commandCastServiceImpl.changeCastDevices2(player, null, null, false, true);
		String code = playerInfoBO.getSrcCode();
		if(!playerInfoBO.isHasBusiness() || code==null || code.equals("")){
			throw new BaseException(StatusCode.FORBIDDEN, "没有可以录制的内容");
		}
		UserBO srcUser = userUtils.queryUserByUserno(code);
		if(srcUser != null){
			//录制用户
			FolderUserMap srcUserMap = folderUserMapDao.findByUserId(srcUser.getId());
			boolean bSrcUserLdap = queryUtil.isLdapUser(srcUser, srcUserMap);
			if(!bSrcUserLdap){
				MonitorRecordPO task = monitorRecordService.addLocalUser(mode, fileName, startTime, endTime, srcUser, user.getId(), user.getUserno());
				return task;
			}else{
				//录制本地用户
				MonitorRecordPO task = monitorRecordService.addXtUser(mode, fileName, startTime, endTime, srcUser, user.getId(), user.getUserno());
				return task;
			}
			
		}else{
			BundlePO bundle = bundleDao.findByUsername(code);
			String bundleId = bundle.getBundleId();
			//录制设备 TODO:鉴权
			if(!queryUtil.isLdapBundle(bundle)){
				List<ChannelSchemeDTO> queryChannels = resourceQueryUtil.findByBundleIdsAndChannelType(new ArrayListWrapper<String>().add(bundleId).getList(), 0);
				ChannelVO encodeVideo = null;
				ChannelVO encodeAudio = null;
				if(queryChannels!=null && queryChannels.size()>0){
					for(ChannelSchemeDTO channel:queryChannels){
						if("VenusVideoIn".equals(channel.getBaseType())){
							encodeVideo = new ChannelVO().set(channel);
						}else if("VenusAudioIn".equals(channel.getBaseType())){
							encodeAudio = new ChannelVO().set(channel);
						}
					}
				}
				String videoBundleId = bundle.getBundleId();
				String videoBundleName = bundle.getBundleName();
				String videoBundleType = bundle.getBundleType();
				String videoLayerId = bundle.getAccessNodeUid();
				String videoChannelId = encodeVideo.getChannelId();
				String videoBaseType = encodeVideo.getBaseType();
				String videoChannelName = encodeVideo.getName();
				String audioBundleId = bundle.getBundleId();
				String audioBundleName = bundle.getBundleName();
				String audioBundleType = bundle.getBundleType();
				String audioLayerId = bundle.getAccessNodeUid();
				String audioChannelId = encodeAudio.getChannelId();
				String audioBaseType = encodeAudio.getBaseType();
				String audioChannelName = encodeAudio.getName();
				
				MonitorRecordPO task = monitorRecordService.addLocalDevice(
						mode, fileName, startTime, endTime, 
						videoBundleId, videoBundleName, videoBundleType, videoLayerId, videoChannelId, videoBaseType, videoChannelName, 
						audioBundleId, audioBundleName, audioBundleType, audioLayerId, audioChannelId, audioBaseType, audioChannelName, 
						user.getId(), user.getUserno());

				return task;
			}else{
				MonitorRecordPO task = monitorRecordService.addXtDevice(mode, fileName, startTime, endTime, bundleId, user.getId(), user.getUserno());

				return task;
			}
		}
	}
}
