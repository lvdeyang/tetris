package com.sumavision.bvc.device.command.record;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.suma.venus.resource.base.bo.UserBO;
import com.suma.venus.resource.constant.BusinessConstants.BUSINESS_OPR_TYPE;
import com.suma.venus.resource.dao.BundleDao;
import com.suma.venus.resource.dao.FolderUserMapDAO;
import com.suma.venus.resource.pojo.BundlePO;
import com.suma.venus.resource.pojo.FolderUserMap;
import com.sumavision.bvc.command.group.dao.CommandGroupUserInfoDAO;
import com.sumavision.bvc.command.group.user.CommandGroupUserInfoPO;
import com.sumavision.bvc.command.group.user.layout.player.CommandGroupUserPlayerPO;
import com.sumavision.bvc.control.device.monitor.device.ChannelVO;
import com.sumavision.bvc.control.utils.UserUtils;
import com.sumavision.bvc.control.welcome.UserVO;
import com.sumavision.bvc.device.command.bo.PlayerInfoBO;
import com.sumavision.bvc.device.command.cast.CommandCastServiceImpl;
import com.sumavision.bvc.device.command.common.CommandCommonServiceImpl;
import com.sumavision.bvc.device.command.common.CommandCommonUtil;
import com.sumavision.bvc.device.group.service.util.QueryUtil;
import com.sumavision.bvc.device.group.service.util.ResourceQueryUtil;
import com.sumavision.bvc.device.monitor.record.MonitorRecordPO;
import com.sumavision.bvc.device.monitor.record.MonitorRecordService;
import com.sumavision.bvc.resource.dto.ChannelSchemeDTO;
import com.sumavision.tetris.bvc.business.group.GroupMemberType;
import com.sumavision.tetris.bvc.model.terminal.TerminalDAO;
import com.sumavision.tetris.bvc.model.terminal.TerminalPO;
import com.sumavision.tetris.bvc.page.PageInfoDAO;
import com.sumavision.tetris.bvc.page.PageInfoPO;
import com.sumavision.tetris.bvc.page.PageTaskPO;
import com.sumavision.tetris.bvc.page.PageTaskQueryService;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
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
	private TerminalDAO terminalDao;
		
	@Autowired
	private PageInfoDAO pageInfoDao;
		
	@Autowired
	private FolderUserMapDAO folderUserMapDao;
		
	@Autowired
	private BundleDao bundleDao;
		
	@Autowired
	private CommandGroupUserInfoDAO commandGroupUserInfoDao;
	
	@Autowired
	private CommandCommonUtil commandCommonUtil;
	
	@Autowired
	private ResourceQueryUtil resourceQueryUtil;
	
	@Autowired
	private UserUtils userUtils;
	
	@Autowired
	private QueryUtil queryUtil;
	
	@Autowired
	private PageTaskQueryService pageTaskQueryService;
	
	@Autowired
	private CommandCommonServiceImpl commandCommonServiceImpl;
	
	@Autowired
	private CommandCastServiceImpl commandCastServiceImpl;
	
	@Autowired
	private MonitorRecordService monitorRecordService;

	public MonitorRecordPO playerStartRecord(
//			Long userId,
			UserVO user,
			int locationIndex,
			String mode, 
			String fileName, 
			String startTime, 
			String endTime
			) throws Exception{
		
		TerminalPO terminal = terminalDao.findByType(com.sumavision.tetris.bvc.model.terminal.TerminalType.QT_ZK);		
		PageTaskPO pageTask = pageTaskQueryService.queryPageTask(user.getId().toString(), terminal.getId(), locationIndex);
		
		if(pageTask == null){
			throw new BaseException(StatusCode.FORBIDDEN, "没有可以录制的内容");
		}
		
		String code = pageTask.getSrcVideoCode();
		if(code==null || code.equals("")){
			throw new BaseException(StatusCode.FORBIDDEN, "没有可以录制的内容");
		}
		UserBO srcUser = userUtils.queryUserByUserno(code);
		if(srcUser != null){
			//录制用户
			commandCommonServiceImpl.authorizeUser(srcUser.getId(), user.getId(), BUSINESS_OPR_TYPE.RECORD);
			FolderUserMap srcUserMap = folderUserMapDao.findByUserId(srcUser.getId());
			boolean bSrcUserLdap = queryUtil.isLdapUser(srcUser, srcUserMap);
			if(!bSrcUserLdap){
				MonitorRecordPO task = monitorRecordService.addLocalUser(mode, fileName, startTime, endTime, srcUser, user.getId(), user.getUserno(), user.getName());
				return task;
			}else{
				//录制本地用户
				MonitorRecordPO task = monitorRecordService.addXtUser(mode, fileName, startTime, endTime, srcUser, user.getId(), user.getUserno(), user.getName());
				return task;
			}
			
		}else{
			BundlePO bundle = bundleDao.findByUsername(code);
			String bundleId = bundle.getBundleId();
			commandCommonServiceImpl.authorizeBundle(bundleId, user.getId(), BUSINESS_OPR_TYPE.RECORD);
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
				
				//还没有区分录制模式
				MonitorRecordPO task = monitorRecordService.addDevice(
						mode, fileName, startTime, endTime, 
						videoBundleId, videoBundleName, videoBundleType, videoLayerId, videoChannelId, videoBaseType, videoChannelName, 
						audioBundleId, audioBundleName, audioBundleType, audioLayerId, audioChannelId, audioBaseType, audioChannelName, 
						user.getId(), user.getUserno(), user.getName());

				return task;
			}else{
				MonitorRecordPO task = monitorRecordService.addXtDevice(mode, fileName, startTime, endTime, bundleId, user.getId(), user.getUserno(), user.getName());

				return task;
			}
		}
	}
}
