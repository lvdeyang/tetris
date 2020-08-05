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
import com.sumavision.tetris.bvc.model.agenda.AgendaForwardDAO;
import com.sumavision.tetris.bvc.model.agenda.AgendaForwardType;
import com.sumavision.tetris.bvc.model.agenda.exception.AgendaNotFoundException;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.netflix.infix.lang.infix.antlr.EventFilterParser.null_predicate_return;
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
import com.sumavision.bvc.device.group.bo.ConnectBO;
import com.sumavision.bvc.device.group.bo.ConnectBundleBO;
import com.sumavision.bvc.device.group.bo.DisconnectBundleBO;
import com.sumavision.bvc.device.group.bo.ForwardSetSrcBO;
import com.sumavision.bvc.device.group.bo.LogicBO;
import com.sumavision.bvc.device.group.bo.PassByBO;
import com.sumavision.bvc.device.group.enumeration.ChannelType;
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
import com.sumavision.tetris.bvc.page.PageTaskBO;
import com.sumavision.tetris.bvc.page.PageTaskDAO;
import com.sumavision.tetris.bvc.page.PageTaskPO;
import com.sumavision.tetris.bvc.page.PageTaskService;
import com.sumavision.tetris.bvc.util.TetrisBvcQueryUtil;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.orm.po.AbstractBasePO;
import com.sumavision.tetris.websocket.message.WebsocketMessageService;
import com.sumavision.tetris.websocket.message.WebsocketMessageType;
import com.sumavision.tetris.websocket.message.WebsocketMessageVO;

import lombok.extern.slf4j.Slf4j;

/**
 * AutoCombineService<br/>
 * <p>自动创建合屏混音</p>
 * <b>作者:</b>zsy<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年5月22日 上午10:03:43
 */
@Slf4j
//@Transactional(rollbackFor = Exception.class)
@Service
public class AutoCombineService {

	@Autowired
	private CombineVideoDAO combineVideoDao;
	
	@Autowired
	private CombineVideoPositionDAO combineVideoPositionDao;
	
	@Autowired
	@Qualifier("com.sumavision.tetris.bvc.model.agenda.combine.CombineVideoSrcDAO")
	private CombineVideoSrcDAO combineVideoSrcDao;
	
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
	private PageTaskService pageTaskService;
	
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
	private ExecuteBusinessProxy executeBusiness;
	
	@Autowired
	private AgendaDAO agendaDao;
	
	public List<CommonForwardPO> combine(List<GroupMemberPO> dstMembers, List<SourceBO> sourceBOs) throws Exception{
		
		String name = "自动合屏";		
		int sourceSize = sourceBOs.size();
		
		CombineVideoPO entity = new CombineVideoPO();
		entity.setName(name);
//		entity.setBusinessId(businessId);
		entity.setBusinessType(CombineBusinessType.AGENDA);
		entity.setUpdateTime(new Date());
		combineVideoDao.save(entity);
		
		List<CombineVideoPositionPO> existPositionEntities = combineVideoPositionDao.findByCombineVideoId(entity.getId());
		if(existPositionEntities!=null && existPositionEntities.size()>0){
			List<Long> existPositionIds = new ArrayList<Long>();
			for(CombineVideoPositionPO existPositionEntity:existPositionEntities){
				existPositionIds.add(existPositionEntity.getId());
			}
			combineVideoPositionDao.deleteInBatch(existPositionEntities);
			List<CombineVideoSrcPO> existSrcEntities = combineVideoSrcDao.findByCombineVideoPositionIdIn(existPositionIds);
			if(existSrcEntities!=null && existSrcEntities.size()>0){
				combineVideoSrcDao.deleteInBatch(existSrcEntities);
			}
		}
//		entity.setWebsiteDraw(websiteDraw.toJSONString());
		combineVideoDao.save(entity);
		
		//自动生成布局，关联到CombineVideoPO
		List<CombineVideoPositionPO> positionEntities = autoLayout(sourceBOs.size());
		for(CombineVideoPositionPO positionEntity : positionEntities){
			positionEntity.setCombineVideoId(entity.getId());
		}		
		combineVideoPositionDao.save(positionEntities);
		
		
		List<CombineVideoSrcPO> srcEntities = new ArrayList<CombineVideoSrcPO>();
		Iterator<SourceBO> iterator = sourceBOs.iterator();
		for(CombineVideoPositionPO positionEntity : positionEntities){
//		for(int i=0; i<positions.size(); i++){
			if(!iterator.hasNext()) break;
			SourceBO sourceBO = iterator.next();
			AgendaForwardType agendaforwardType = sourceBO.getAgendaForwardType();
			
			
			String srcId = sourceBO.getVideoSource().getChannelId()
					+ "@@" + sourceBO.getVideoSource().getBundleId()
					+ "@@" + sourceBO.getVideoBundle().getAccessNodeUid();
			
			CombineVideoSrcPO srcEntity = new CombineVideoSrcPO();
			srcEntity.setSrcId(srcId);
			srcEntity.setCombineVideoSrcType(CombineVideoSrcType.CHANNEL);
			srcEntity.setSerial(1);
			srcEntity.setUpdateTime(new Date());
			srcEntity.setCombineVideoPositionId(positionEntity.getId());
			srcEntities.add(srcEntity);
//				}
//			}
		}
		combineVideoSrcDao.save(srcEntities);
		
		
		
		return null;
	}
	
	private List<CombineVideoPositionPO> autoLayout(int size) throws Exception{
		int combineSize = 0;
		int combineBase = 1;
		if(size <= 0){
			return null;
		}else if(size == 1){
			combineBase = 1;
			combineSize = 1;
		}else if(size <= 4){
			combineBase = 2;
			combineSize = 4;
		}else if(size <= 9){
			combineBase = 3;
			combineSize = 9;
		}else{
			combineBase = 4;
			combineSize = 16;
		}
		
		List<CombineVideoPositionPO> positionEntities = new ArrayList<CombineVideoPositionPO>();
		int serialNum = 1;
		String w = "1/" + combineBase;
		String h = "1/" + combineBase;
		for(int i=0; i<combineBase; i++){
			String y = i + "/" + combineBase;
			for(int j=0; j<combineBase; j++){
				
				String x = j + "/" + combineBase;
				CombineVideoPositionPO positionEntity = new CombineVideoPositionPO();
				positionEntity.setSerialnum(serialNum++);
				positionEntity.setX(tenThousand(x));
				positionEntity.setY(tenThousand(y));
				positionEntity.setW(tenThousand(w));
				positionEntity.setH(tenThousand(h));
				positionEntity.setPictureType(PictureType.STATIC);
//				positionEntity.setPollingTime(data.getString("pollingTime"));
				positionEntity.setUpdateTime(new Date());
//				positionEntity.setCombineVideoId(entity.getId());
				positionEntities.add(positionEntity);
			}
		}
//		combineVideoPositionDao.save(positionEntities);
		
		return positionEntities;
	}
	
	/**
	 * 分数转换万分比分子<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月7日 上午10:36:38
	 * @param String fraction 分数
	 * @return 万分比分子
	 */
	private String tenThousand(String fraction) throws Exception{
		String[] values = fraction.split("/");
		//分子
		int molecule = Integer.parseInt(values[0]);
		//分母
		int denominator = Integer.parseInt(values[1]);
		return String.valueOf(molecule*10000/denominator);
	}
	
}
