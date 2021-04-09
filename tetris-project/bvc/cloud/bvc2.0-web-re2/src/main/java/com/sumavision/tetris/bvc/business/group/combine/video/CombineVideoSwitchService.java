package com.sumavision.tetris.bvc.business.group.combine.video;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.sumavision.tetris.bvc.model.role.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sumavision.bvc.device.command.common.CommandCommonUtil;
import com.sumavision.bvc.device.group.bo.CodecParamBO;
import com.sumavision.bvc.device.group.bo.CombineVideoBO;
import com.sumavision.bvc.device.group.bo.LogicBO;
import com.sumavision.bvc.device.group.bo.PositionSrcBO;
import com.sumavision.bvc.device.group.enumeration.PictureType;
import com.sumavision.bvc.device.group.po.CombineVideoPO;
import com.sumavision.bvc.device.group.po.DeviceGroupAvtplGearsPO;
import com.sumavision.bvc.device.group.po.DeviceGroupAvtplPO;
import com.sumavision.bvc.device.group.service.test.ExecuteBusinessProxy;
import com.sumavision.bvc.device.group.service.util.QueryUtil;
import com.sumavision.tetris.bvc.business.bo.BusinessDeliverBO;
import com.sumavision.tetris.bvc.business.dao.BusinessCombineVideoDAO;
import com.sumavision.tetris.bvc.business.dao.BusinessGroupMemberDAO;
import com.sumavision.tetris.bvc.business.dao.CombineTemplateGroupAgendeForwardPermissionDAO;
import com.sumavision.tetris.bvc.business.dao.GroupDAO;
import com.sumavision.tetris.bvc.business.dao.RunningAgendaDAO;
import com.sumavision.tetris.bvc.business.group.GroupPO;
import com.sumavision.tetris.bvc.business.group.GroupStatus;
import com.sumavision.tetris.bvc.business.group.RunningAgendaPO;
import com.sumavision.tetris.bvc.business.po.combine.video.BusinessCombineVideoPO;
import com.sumavision.tetris.bvc.business.po.combine.video.BusinessCombineVideoPositionPO;
import com.sumavision.tetris.bvc.business.po.combine.video.BusinessCombineVideoSrcPO;
import com.sumavision.tetris.bvc.business.po.combine.video.CombineTemplateGroupAgendeForwardPermissionPO;
import com.sumavision.tetris.bvc.business.po.member.BusinessGroupMemberPO;
import com.sumavision.tetris.bvc.business.po.member.BusinessGroupMemberTerminalChannelPO;
import com.sumavision.tetris.bvc.model.agenda.AgendaDAO;
import com.sumavision.tetris.bvc.model.agenda.AgendaForwardDAO;
import com.sumavision.tetris.bvc.model.agenda.AgendaForwardPO;
import com.sumavision.tetris.bvc.model.agenda.AgendaForwardSourceDAO;
import com.sumavision.tetris.bvc.model.agenda.AgendaForwardSourcePO;
import com.sumavision.tetris.bvc.model.agenda.AgendaPO;
import com.sumavision.tetris.bvc.model.agenda.SourceType;
import com.sumavision.tetris.bvc.model.layout.forward.CombineTemplatePositionDAO;
import com.sumavision.tetris.bvc.model.layout.forward.CombineTemplatePositionPO;
import com.sumavision.tetris.bvc.page.PageTaskDAO;
import com.sumavision.tetris.bvc.page.PageTaskPO;
import com.sumavision.tetris.bvc.util.AgendaRoleMemberUtil;
import com.sumavision.tetris.bvc.util.TetrisBvcQueryUtil;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.mvc.util.BaseUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * 合屏<br/>
 * <p>详细描述</p>
 * <b>作者:</b>zsy<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年11月24日 下午1:02:43
 */
@Slf4j
@Service
public class CombineVideoSwitchService {
	
	@Autowired
	private RoleChannelDAO roleChannelDao;
	
	@Autowired
	private PageTaskDAO pageTaskDao;
	
	@Autowired
	private AgendaForwardDAO agendaForwardDao;
	
	@Autowired
	private ExecuteBusinessProxy executeBusiness;
	
	@Autowired
	private BusinessCombineVideoDAO combineVideoDao;

	@Autowired
	private AgendaRoleMemberUtil agendaRoleMemberUtil;

	@Autowired
	private TetrisBvcQueryUtil tetrisBvcQueryUtil;

	@Autowired
	private BusinessGroupMemberDAO businessGroupMemberDao;
	
	@Autowired
	private AgendaForwardSourceDAO agendaForwardSourceDao;
	
	@Autowired
	private CombineTemplatePositionDAO combineTemplatePositionDao;
	
	@Autowired
	private BusinessCombineVideoDAO businessCombineVideoDao;
	
	@Autowired
	private RoleChannelTerminalChannelPermissionDAO roleChannelTerminalChannelPermissionDao;
	
	@Autowired
	private CombineTemplateGroupAgendeForwardPermissionDAO combineTemplateGroupAgendeForwardPermissionDao;
	
	@Autowired
	private AgendaDAO agendaDao;
	
	@Autowired
	private GroupDAO groupDao;
	
	@Autowired
	private RunningAgendaDAO runningAgendaDao;
	
	@Autowired
	private CommandCommonUtil commandCommonUtil;
	
	public void switchPollingIndex(
			Long agendaForwardId,
			Long sourceId,
			SourceType sourceType,
			Integer serialNum,
			int index) throws Exception{
		
		//校验部分不用看
		AgendaForwardPO agendaForward = agendaForwardDao.findOne(agendaForwardId);
		if(agendaForward == null){
			throw new BaseException(StatusCode.FORBIDDEN, "没有找到该议程转发");
		}
		
		AgendaPO  agenda = agendaDao.findOne(agendaForward.getAgendaId());
		if(agenda == null){
			throw new BaseException(StatusCode.FORBIDDEN, "没有找到该议程");
		}
		
		GroupPO group = groupDao.findOne(agenda.getBusinessId());
		if(group == null){
			throw new BaseException(StatusCode.FORBIDDEN, "没有找到该会议");
		}
		if(!GroupStatus.START.equals(group.getStatus())){
			throw new BaseException(StatusCode.FORBIDDEN, "还没有开始会议");
		}
		
		RunningAgendaPO runningAgenda = runningAgendaDao.findByGroupId(group.getId());
		if(!runningAgenda.getAgendaId().equals(agenda.getId())){
			throw new BaseException(StatusCode.FORBIDDEN, "还没有执行该议程转发");
		}
		
		CombineTemplateGroupAgendeForwardPermissionPO permission = combineTemplateGroupAgendeForwardPermissionDao.findByGroupIdAndAgendaIdAndAgendaForwardId(group.getId(), agenda.getId(), agendaForwardId);
		if(permission == null){
			throw new BaseException(StatusCode.FORBIDDEN, "该议程转发没有轮询");
		}
		List<BusinessCombineVideoPositionPO> combineVideoPositions = permission.getCombineVideo().getPositions();
		BusinessCombineVideoPositionPO pollingPositiion = null;
		for(BusinessCombineVideoPositionPO combineVideoPosition : combineVideoPositions){
			if(combineVideoPosition.getSerialnum() == serialNum && PictureType.POLLING.equals(combineVideoPosition.getPictureType())){
				pollingPositiion = combineVideoPosition;
				break;
			}
		}
		if(pollingPositiion == null){
			throw new BaseException(StatusCode.FORBIDDEN, "该议程转发的该布局画面没有配置轮询");
		}
		//校验结束
		
		List<BusinessCombineVideoSrcPO> pollingSrcs= pollingPositiion.getSrcs();
		
		//根据index找到AgendaForwardPO。
		if(pollingSrcs.size() < (index+1)){
			throw new BaseException(StatusCode.FORBIDDEN, "该轮询配置需要先保存");
		}
		
		//对比sourceId和sourceType校验。是否有源
		BusinessCombineVideoSrcPO src = pollingSrcs.get(index);
		if(!src.getSourceId().equals(sourceId) || !src.getSourceType().equals(sourceType)){
			throw new BaseException(StatusCode.FORBIDDEN, "该轮询配置需要先保存");
		}
		if(!src.isHasSource()){
			throw new BaseException(StatusCode.FORBIDDEN, "该角色或者成员因为没有源，所以不在轮询当中");
		}
		
		int offset = 0;
		for(int i = 0 ; i <= index ; i++){
			if(!pollingSrcs.get(i).isHasSource()){
				offset++;
			}
		}
		
		Integer mixerSrcIndex = index - offset;
		
		//协议数据
		LogicBO logic = new LogicBO();
		logic.setUserId(group.getUserId().toString());
		
		//处理参数模板
		DeviceGroupAvtplPO g_avtpl = group.getAvtpl();
		CodecParamBO codec = new CodecParamBO().set(g_avtpl);
		
		List<BusinessCombineVideoPO> combineVideoList = new ArrayList<BusinessCombineVideoPO>();
//				CombineVideoPO cvideo = combineVideoDao.findByUuid(combineVideoUuid);
		combineVideoList.add(pollingPositiion.getCombineVideo());
		
		logic.setCombineVideoUpdate_recon(combineVideoList, codec);
		List<PositionSrcBO> positions = logic.getCombineVideoUpdate().get(0).getPosition();
		for(PositionSrcBO position : positions){
			if(position.getUuid().equals(pollingPositiion.getUuid())){
				position.setPolling_index(mixerSrcIndex);
			}
		}
		
		//调用逻辑层
		executeBusiness.execute(logic, "设置轮询跳转到指定画面：");
		
		return;
	}
	
	/**
	 * 合屏：下一个画面<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年3月9日 下午5:09:33
	 * @param agendaForwardId 议程转发id。
	 * @param serialNum 分屏位置：从1开始计数。
	 */
	public void switchPollingNext(Long agendaForwardId, Integer serialNum) throws Exception{
		
		//校验
		BusinessCombineVideoPositionPO pollingPositiion = validate(agendaForwardId, serialNum);
		
		//生成协议
		PositionSrcBO aPosition = new PositionSrcBO();
		aPosition.set(pollingPositiion, 0, 0)
				.setSrc(null)
				.setOperation("next_mixer_video_loop_index");
		CombineVideoBO aCombineVideoSwitchToNextSource = new CombineVideoBO();
		aCombineVideoSwitchToNextSource.setCodec_param(null)
				.setUuid(pollingPositiion.getCombineVideo().getUuid())
				.setPosition(new ArrayList<PositionSrcBO>())
				.getPosition().add(aPosition);
		LogicBO logic = new LogicBO();
		logic.setCombineVideoOperation(new ArrayList<CombineVideoBO>())
				.getCombineVideoOperation().add(aCombineVideoSwitchToNextSource);
		
		//调用逻辑层
		executeBusiness.execute(logic, "切换轮询到下一画面");
		
		return;
	}
	
	/**
	 * 校验<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年3月9日 下午4:35:25
	 */
	private BusinessCombineVideoPositionPO validate(Long agendaForwardId, Integer serialNum) throws Exception{
		
		//校验是否开会
		//校验是否执行当前议程 或者 校验能否找到合屏
		//BusinessCombineVideoSrcPO hasSource
		AgendaForwardPO agendaForward = agendaForwardDao.findOne(agendaForwardId);
		if(agendaForward == null){
			throw new BaseException(StatusCode.FORBIDDEN, "没有找到该议程转发");
		}
		
		AgendaPO  agenda = agendaDao.findOne(agendaForward.getAgendaId());
		if(agenda == null){
			throw new BaseException(StatusCode.FORBIDDEN, "没有找到该议程");
		}
		
		RunningAgendaPO runningAgenda = runningAgendaDao.findByGroupId(agenda.getBusinessId());
		if(!runningAgenda.getAgendaId().equals(agenda.getId())){
			throw new BaseException(StatusCode.FORBIDDEN, "还没有执行该议程转发");
		}
		
		CombineTemplateGroupAgendeForwardPermissionPO permission = combineTemplateGroupAgendeForwardPermissionDao.findByGroupIdAndAgendaIdAndAgendaForwardId(agenda.getBusinessId(), agenda.getId(), agendaForwardId);
		if(permission == null){
			throw new BaseException(StatusCode.FORBIDDEN, "该议程转发没有轮询");
		}
		List<BusinessCombineVideoPositionPO> combineVideoPositions = permission.getCombineVideo().getPositions();
		BusinessCombineVideoPositionPO pollingPositiion = null;
		for(BusinessCombineVideoPositionPO combineVideoPosition : combineVideoPositions){
			if(combineVideoPosition.getSerialnum() == serialNum && PictureType.POLLING.equals(combineVideoPosition.getPictureType())){
				pollingPositiion = combineVideoPosition;
				break;
			}
		}
		if(pollingPositiion == null){
			throw new BaseException(StatusCode.FORBIDDEN, "该议程转发的该布局画面没有配置轮询");
		}
		
		return pollingPositiion;
	}
}
