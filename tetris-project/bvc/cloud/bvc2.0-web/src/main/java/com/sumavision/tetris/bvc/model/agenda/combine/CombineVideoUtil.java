package com.sumavision.tetris.bvc.model.agenda.combine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import com.sumavision.tetris.bvc.model.agenda.AgendaDAO;
import com.sumavision.tetris.bvc.model.agenda.AgendaExecuteService;
import com.sumavision.tetris.bvc.model.agenda.AgendaForwardDAO;
import com.sumavision.tetris.bvc.model.agenda.AgendaForwardType;
import com.sumavision.tetris.bvc.model.agenda.AgendaSourceType;
import com.sumavision.tetris.bvc.model.agenda.exception.AgendaNotFoundException;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.suma.venus.resource.dao.BundleDao;
import com.suma.venus.resource.pojo.BundlePO;
import com.sumavision.bvc.command.group.basic.CommandGroupPO;
import com.sumavision.bvc.command.group.dao.CommandGroupUserInfoDAO;
import com.sumavision.bvc.command.group.enumeration.ForwardBusinessType;
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
import com.sumavision.bvc.device.group.bo.CombineAudioBO;
import com.sumavision.bvc.device.group.bo.CombineVideoBO;
import com.sumavision.bvc.device.group.bo.ConnectBO;
import com.sumavision.bvc.device.group.bo.ConnectBundleBO;
import com.sumavision.bvc.device.group.bo.DisconnectBundleBO;
import com.sumavision.bvc.device.group.bo.ForwardSetBO;
import com.sumavision.bvc.device.group.bo.ForwardSetDstBO;
import com.sumavision.bvc.device.group.bo.ForwardSetSrcBO;
import com.sumavision.bvc.device.group.bo.LogicBO;
import com.sumavision.bvc.device.group.bo.PassByBO;
import com.sumavision.bvc.device.group.bo.PositionSrcBO;
import com.sumavision.bvc.device.group.enumeration.ChannelType;
import com.sumavision.bvc.device.group.enumeration.ForwardSrcType;
import com.sumavision.bvc.device.group.po.DeviceGroupMemberChannelPO;
import com.sumavision.bvc.device.group.service.test.ExecuteBusinessProxy;
import com.sumavision.bvc.device.group.service.util.QueryUtil;
import com.sumavision.bvc.device.monitor.live.DstDeviceType;
import com.sumavision.bvc.resource.dao.ResourceBundleDAO;
import com.sumavision.bvc.resource.dao.ResourceChannelDAO;
import com.sumavision.bvc.resource.dto.ChannelSchemeDTO;
import com.sumavision.bvc.system.po.AvtplGearsPO;
import com.sumavision.bvc.system.po.AvtplPO;
import com.sumavision.tetris.bvc.business.BusinessInfoType;
import com.sumavision.tetris.bvc.business.ExecuteStatus;
import com.sumavision.tetris.bvc.business.bo.MemberChangedTaskBO;
import com.sumavision.tetris.bvc.business.bo.ModifyMemberRoleBO;
import com.sumavision.tetris.bvc.business.bo.SourceBO;
import com.sumavision.tetris.bvc.business.common.BusinessCommonService;
import com.sumavision.tetris.bvc.business.dao.CommonForwardDAO;
import com.sumavision.tetris.bvc.business.dao.GroupDAO;
import com.sumavision.tetris.bvc.business.dao.GroupMemberDAO;
import com.sumavision.tetris.bvc.business.dao.GroupMemberRolePermissionDAO;
import com.sumavision.tetris.bvc.business.dao.RunningAgendaDAO;
import com.sumavision.tetris.bvc.business.forward.CommonForwardPO;
import com.sumavision.tetris.bvc.business.group.GroupMemberPO;
import com.sumavision.tetris.bvc.business.group.GroupMemberRolePermissionPO;
import com.sumavision.tetris.bvc.business.group.GroupMemberStatus;
import com.sumavision.tetris.bvc.business.group.GroupMemberType;
import com.sumavision.tetris.bvc.business.group.GroupPO;
import com.sumavision.tetris.bvc.business.group.GroupStatus;
import com.sumavision.tetris.bvc.business.group.RunningAgendaPO;
import com.sumavision.tetris.bvc.business.group.forward.Jv230CombineAudioSrcPO;
import com.sumavision.tetris.bvc.business.group.forward.QtTerminalCombineVideoSrcPO;
import com.sumavision.tetris.bvc.business.terminal.hall.TerminalBundleConferenceHallPermissionDAO;
import com.sumavision.tetris.bvc.business.terminal.hall.TerminalBundleConferenceHallPermissionPO;
import com.sumavision.tetris.bvc.business.terminal.user.TerminalBundleUserPermissionDAO;
import com.sumavision.tetris.bvc.business.terminal.user.TerminalBundleUserPermissionPO;
import com.sumavision.tetris.bvc.model.role.InternalRoleType;
import com.sumavision.tetris.bvc.model.role.RoleChannelDAO;
import com.sumavision.tetris.bvc.model.role.RoleChannelPO;
import com.sumavision.tetris.bvc.model.role.RoleDAO;
import com.sumavision.tetris.bvc.model.role.RolePO;
import com.sumavision.tetris.bvc.model.terminal.TerminalBundleChannelDAO;
import com.sumavision.tetris.bvc.model.terminal.TerminalBundleChannelPO;
import com.sumavision.tetris.bvc.model.terminal.TerminalBundleDAO;
import com.sumavision.tetris.bvc.model.terminal.TerminalBundlePO;
import com.sumavision.tetris.bvc.model.terminal.TerminalBundleType;
import com.sumavision.tetris.bvc.model.terminal.TerminalPO;
import com.sumavision.tetris.bvc.model.terminal.channel.TerminalChannelDAO;
import com.sumavision.tetris.bvc.model.terminal.channel.TerminalChannelPO;
import com.sumavision.tetris.bvc.page.PageInfoDAO;
import com.sumavision.tetris.bvc.page.PageInfoPO;
import com.sumavision.tetris.bvc.page.PageTaskDAO;
import com.sumavision.tetris.bvc.page.PageTaskPO;
import com.sumavision.tetris.bvc.page.PageTaskService;
import com.sumavision.tetris.bvc.util.TetrisBvcQueryUtil;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.orm.po.AbstractBasePO;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;
import com.sumavision.tetris.websocket.message.WebsocketMessageService;
import com.sumavision.tetris.websocket.message.WebsocketMessageType;
import com.sumavision.tetris.websocket.message.WebsocketMessageVO;

import lombok.extern.slf4j.Slf4j;

/**
 * 合屏工具类<br/>
 * <p>详细描述</p>
 * <b>作者:</b>zsy<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年9月4日 上午10:29:14
 */
@Slf4j
//@Transactional(rollbackFor = Exception.class)
@Service
public class CombineVideoUtil {

	@Autowired
	private CombineVideoDAO combineVideoDao;
	
	@Autowired
	private CombineVideoPositionDAO combineVideoPositionDao;
	
	@Autowired
	@Qualifier("com.sumavision.tetris.bvc.model.agenda.combine.CombineVideoSrcDAO")
	private CombineVideoSrcDAO combineVideoSrcDao;

	@Autowired
	private CombineAudioDAO combineAudioDao;
		
	@Autowired
	@Qualifier("com.sumavision.tetris.bvc.model.agenda.combine.CombineAudioSrcDAO")
	private CombineAudioSrcDAO combineAudioSrcDao;
	
	@Autowired
	private BundleDao bundleDao;
	
	@Autowired
	private ResourceBundleDAO resourceBundleDao;
	
	@Autowired
	private ResourceChannelDAO resourceChannelDao;
	
	@Autowired
	private GroupDAO groupDao;
	
	@Autowired
	private RoleChannelDAO roleChannelDao;
	
//	@Autowired
//	private TerminalBundleChannelDAO terminalBundleChannelDao;
	
	@Autowired
	private TerminalChannelDAO terminalChannelDao;
	
	@Autowired
	private PageInfoDAO pageInfoDao;
	
	@Autowired
	private PageTaskDAO pageTaskDao;
	
	@Autowired
	private CommonForwardDAO commonForwardDao;
	
	@Autowired
	private TerminalBundleDAO terminalBundleDao;
	
	@Autowired
	private AgendaForwardDAO agendaForwardDao;
	
	@Autowired
	private GroupMemberRolePermissionDAO groupMemberRolePermissionDao;
	
	@Autowired
	private RoleDAO roleDao;
	
	@Autowired
	private GroupMemberDAO groupMemberDao;
	
	@Autowired
	private TerminalBundleUserPermissionDAO terminalBundleUserPermissionDao;
	
	@Autowired
	private TerminalBundleConferenceHallPermissionDAO terminalBundleConferenceHallPermissionDao;
	
	@Autowired
	private RunningAgendaDAO runningAgendaDao;
	
	@Autowired
	private CombineVideoService combineVideoService;
	
	@Autowired
	private CombineAudioService combineAudioService;
	
	@Autowired
	private PageTaskService pageTaskService;
	
	@Autowired
	private AgendaExecuteService agendaExecuteService;
	
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
	private QueryUtil queryUtil;

	@Autowired
	private TetrisBvcQueryUtil tetrisBvcQueryUtil;

	@Autowired
	private UserQuery userQuery;
	
	@Autowired
	private ExecuteBusinessProxy executeBusiness;
	
	@Autowired
	private AgendaDAO agendaDao;
	
	/**
	 * 是否需要合屏<br/>
	 * <p>判定条件：</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年9月14日 下午7:00:15
	 * @param combineVideoPO
	 * @return
	 */
	public boolean needCombineVideo(CombineVideoPO combineVideoPO){
		List<CombineVideoPositionPO> positions = combineVideoPositionDao.findByCombineVideoId(combineVideoPO.getId());
		if(positions == null) return false;
		List<Long> positionIds = new ArrayList<Long>();
		for(CombineVideoPositionPO position : positions){
			positionIds.add(position.getId());
		}
		List<CombineVideoSrcPO> allSrcs = combineVideoSrcDao.findByCombineVideoPositionIdIn(positionIds);
		/*for(CombineVideoPositionPO position : positions){
			List<CombineVideoSrcPO> srcs = tetrisBvcQueryUtil.queryCombineVideoSrcPOByPositionId(allSsrcs, position.getId());
			for(CombineVideoSrcPO src : srcs){				
			}
		}*/
		//判定条件
		if(positions.size() > 1 && allSrcs.size() >= 1) return true;
		if(allSrcs.size() >= 2) return true;
		
		return false;
	}
	
	/**
	 * 从单个合屏中找出单个源<br/>
	 * <p>必须先确认合屏是1*1单个源</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年9月15日 下午6:42:02
	 * @param groupId
	 * @param combineVideoPO
	 * @return
	 */
	public SourceBO getSingleSource(Long groupId, CombineVideoPO combineVideoPO){
		List<CombineVideoPositionPO> positions = combineVideoPositionDao.findByCombineVideoId(combineVideoPO.getId());
		if(positions == null) return null;
		List<Long> positionIds = new ArrayList<Long>();
		for(CombineVideoPositionPO position : positions){
			positionIds.add(position.getId());
		}
		List<CombineVideoSrcPO> allSrcs = combineVideoSrcDao.findByCombineVideoPositionIdIn(positionIds);
		//认为只要有源的配置，就需要合屏
		if(allSrcs.size() > 0){
			CombineVideoSrcPO src = allSrcs.get(0);
			//TODO:先只考虑角色通道，后续补充其他类型
			List<SourceBO> sourceBOs = agendaExecuteService.obtainVideoSourceFromRoleChannelId(groupId, Long.parseLong(src.getSrcId()), BusinessInfoType.COMMON, AgendaForwardType.VIDEO);
			if(sourceBOs!=null && sourceBOs.size()>0){
				return sourceBOs.get(0);
			}			
		}		
		return null;
	}
	
	/**
	 * 遍历合屏的源，将角色通道替换成设备通道<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年9月10日 下午3:40:38
	 * @param groupId
	 * @param combineVideo
	 */
	public void transferSrcs(Long groupId,
			com.sumavision.bvc.device.group.po.CombineVideoPO combineVideo){
		//将配置成角色的源转化成通道
		Set<com.sumavision.bvc.device.group.po.CombineVideoPositionPO> positions = combineVideo.getPositions();
		for(com.sumavision.bvc.device.group.po.CombineVideoPositionPO position:positions){
			List<com.sumavision.bvc.device.group.po.CombineVideoSrcPO> srcs = position.getSrcs();
			if(srcs!=null && srcs.size()>0){
				List<com.sumavision.bvc.device.group.po.CombineVideoSrcPO> deleteSrcs = new ArrayList<com.sumavision.bvc.device.group.po.CombineVideoSrcPO>();
				for(com.sumavision.bvc.device.group.po.CombineVideoSrcPO src:srcs){
					if(CombineVideoSrcType.ROLE_CHANNEL.toString().equals(src.getName())){
						Long roleChannelId = Long.parseLong(src.getBundleId());
						//TODO:AgendaForwardType.VIDEO从支持视音频AUDIO_VIDEO						
						List<SourceBO> sourceBOs = agendaExecuteService.obtainVideoSourceFromRoleChannelId(groupId, roleChannelId, BusinessInfoType.COMMON, AgendaForwardType.VIDEO);
//						List<DeviceGroupMemberChannelPO> channels = queryUtil.queryMemberChannel(group, Long.valueOf(src.getBundleId()), ChannelType.valueOf(src.getChannelId()));
						if(sourceBOs!=null && sourceBOs.size()>0){
							for(SourceBO sourceBO:sourceBOs){
								src.set(sourceBO);
							}
						}else{
							//没有配置角色的源要清除
							src.setPosition(null);
							deleteSrcs.add(src);
						}
					}
				}
				if(deleteSrcs.size() > 0) position.getSrcs().removeAll(deleteSrcs);
			}
		}
	}
	
}
