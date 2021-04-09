package com.sumavision.tetris.bvc.model.agenda.combine;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sumavision.tetris.bvc.business.BusinessInfoType;
import com.sumavision.tetris.bvc.business.bo.BusinessDeliverBO;
import com.sumavision.tetris.bvc.business.dao.BusinessCombineVideoDAO;
import com.sumavision.tetris.bvc.business.dao.CombineTemplateGroupAgendeForwardPermissionDAO;
import com.sumavision.tetris.bvc.business.group.GroupPO;
import com.sumavision.tetris.bvc.business.group.combine.video.CombineVideoService;
import com.sumavision.tetris.bvc.business.po.combine.video.BusinessCombineVideoPO;
import com.sumavision.tetris.bvc.business.po.combine.video.CombineTemplateGroupAgendeForwardPermissionPO;
import com.sumavision.tetris.bvc.model.agenda.AgendaDAO;
import com.sumavision.tetris.bvc.model.agenda.AgendaForwardDAO;
import com.sumavision.tetris.bvc.model.agenda.AgendaForwardPO;
import com.sumavision.tetris.bvc.model.agenda.AgendaForwardSourceDAO;
import com.sumavision.tetris.bvc.model.agenda.AgendaForwardSourcePO;
import com.sumavision.tetris.bvc.model.agenda.AgendaPO;
import com.sumavision.tetris.bvc.model.agenda.AgendaVO;
import com.sumavision.tetris.bvc.model.agenda.AudioPriority;
import com.sumavision.tetris.bvc.model.agenda.AudioType;
import com.sumavision.tetris.bvc.model.agenda.LayoutSelectionType;
import com.sumavision.tetris.bvc.model.layout.LayoutDAO;
import com.sumavision.tetris.bvc.model.layout.LayoutPO;
import com.sumavision.tetris.bvc.model.layout.forward.CombineTemplatePositionDAO;
import com.sumavision.tetris.bvc.model.layout.forward.CombineTemplatePositionPO;
import com.sumavision.tetris.bvc.model.layout.forward.LayoutForwardPO;
import com.sumavision.tetris.bvc.model.terminal.TerminalDAO;
import com.sumavision.tetris.bvc.model.terminal.TerminalPO;
import com.sumavision.tetris.bvc.model.terminal.TerminalType;
import com.sumavision.tetris.bvc.util.AgendaRoleMemberUtil;
import com.sumavision.tetris.bvc.util.TetrisBvcQueryUtil;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;

import lombok.extern.slf4j.Slf4j;

/**
 * 合屏虚拟源处理<br/>
 * <p>合屏虚拟源使用AgendaPO和AgendaForwardPO为载体，AgendaPO的businessId为groupId，businessInfoType为COMBINE_VIDEO_VIRTUAL_SOURCE，以此标记这不是自定义议程；</p>
 * <p>约定BusinessCombineVideoPO必须使用AgendaPO的uuid。约定使用AgendaPO的id作为虚拟源id。BusinessCombineVideoPO存在即表示合屏已经建立，反之亦然</p>
 * <p>部分方法直接使用了 AgendaService 和 AgendaForwardService 的</p>
 * <p>合屏虚拟源在被使用时才会创建，在 AgendaExecuteService.runAgenda()实现；不使用时立即删除，是由 CombineVideoService.update()的businessDeliverBO.getUnusefulVideoPermissions().addAll(videoPers)将所有的合屏先标记为不使用，最后由 DeliverExecuteService 删除</p>
 * <b>作者:</b>zsy<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年5月22日 上午10:03:43
 */
@Slf4j
@Transactional(rollbackFor = Exception.class)
@Service
public class VirtualSourceService {
	
	@Autowired
	private TerminalDAO terminalDao;
	
	@Autowired
	private AgendaDAO agendaDao;
	
	@Autowired
	private LayoutDAO layoutDao;
	
	@Autowired
	private AgendaForwardDAO agendaForwardDao;
	
	@Autowired
	private AgendaForwardSourceDAO agendaForwardSourceDao;
	
	@Autowired
	private BusinessCombineVideoDAO businessCombineVideoDao;
	
	@Autowired
	private CombineTemplatePositionDAO combineTemplatePositionDao;
	
	@Autowired
	private CombineTemplateGroupAgendeForwardPermissionDAO combineTemplateGroupAgendeForwardPermissionDao;
	
	@Autowired
	private TetrisBvcQueryUtil tetrisBvcQueryUtil;
	
	@Autowired
	private AgendaRoleMemberUtil agendaRoleMemberUtil;
	
	@Autowired
	private CombineVideoService combineVideoService;
	
	/**
	 * 添加议程<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月30日 下午4:38:25
	 * @param String name 议程名称
	 * @param String remark 备注
	 * @param Long businessId 关联业务id
	 * @return AgendaVO 议程
	 */
	@Transactional(rollbackFor = Exception.class)
	public AgendaVO add(
			String name,
			String remark,
			Long businessId) throws Exception{
		
		AgendaPO agenda = new AgendaPO();
		agenda.setName(name);
		agenda.setRemark(remark);
		agenda.setVolume(50);
		if(businessId != null) agenda.setBusinessId(businessId);
		agenda.setBusinessInfoType(BusinessInfoType.COMBINE_VIDEO_VIRTUAL_SOURCE);
		agenda.setAudioPriority(AudioPriority.GLOBAL_FIRST);
		agenda.setGlobalCustomAudio(false);
		agenda.setUpdateTime(new Date());
		agendaDao.save(agenda);
		
		AgendaForwardPO agendaForward = new AgendaForwardPO();
		agendaForward.setName(name);
		agendaForward.setAgendaId(agenda.getId());
		agendaForward.setLayoutSelectionType(LayoutSelectionType.CONFIRM);
		if(!agenda.getGlobalCustomAudio()){
			agendaForward.setAudioType(AudioType.CUSTOM);
			agendaForward.setVolume(50);
		}
		List<LayoutPO> layoutEntities = layoutDao.findAll();
		if(layoutEntities!=null && layoutEntities.size()>0){
			agendaForward.setLayoutId(layoutEntities.get(0).getId());
		}
		agendaForwardDao.save(agendaForward);
		
		//虚拟源在使用时才被创建合屏，所以这段不需要了
		/*GroupPO group = groupDao.findOne(businessId);
		if(group.getStatus().equals(GroupStatus.START)){
			BusinessDeliverBO businessDeliverBO = new BusinessDeliverBO().setGroup(group).setUserId(group.getUserId().toString());
			combineVideo(group, agenda, agendaForward, businessDeliverBO);
			deliverExecuteService.execute(businessDeliverBO, "新建合屏虚拟源：" + name, true);
		}*/
		
		return new AgendaVO().set(agenda);
	}
	
	/**
	 * 开会时调用，给虚拟源创建合屏<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年3月18日 下午4:35:31
	 * @param group
	 * @param businessDeliverBO
	 * @throws Exception
	 */
	@Deprecated
	public void groupStart(GroupPO group, BusinessDeliverBO businessDeliverBO) throws Exception{
		List<AgendaPO> agendas = agendaDao.findByBusinessIdAndBusinessInfoType(group.getId(), BusinessInfoType.COMBINE_VIDEO_VIRTUAL_SOURCE);
		List<Long> agendaIds = agendas.stream().map(AgendaPO::getId).collect(Collectors.toList());
		List<AgendaForwardPO> agendaForwards = agendaForwardDao.findByAgendaIdIn(agendaIds);
		for(AgendaForwardPO agendaForward : agendaForwards){
			AgendaPO agenda = tetrisBvcQueryUtil.queryAgendaById(agendas, agendaForward.getAgendaId());
			combineVideo(group, agenda, agendaForward, businessDeliverBO);
		}
	}

	/**
	 * 处理虚拟源转发（给某种终端类型）<br/>
	 * <p>生成转发关系PageTask，生成合屏</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月26日 下午2:19:19
	 * @param layoutForward 虚拟源转发
	 * @param layoutForwardDstMembers 目的成员，可能是该虚拟源转发的所有目的成员，也有可能是某个角色新增的该虚拟源转发的目的成员
	 * @param group
	 * @param agendaForward
	 * @param roleChannelIds 角色通道id
	 * @param businessDeliverBO
	 */
	public BusinessCombineVideoPO combineVideo(
			GroupPO group,
			AgendaPO agenda,
			AgendaForwardPO agendaForward,
			BusinessDeliverBO businessDeliverBO
			) throws Exception{
		
		List<LayoutForwardPO> layoutForwards = agendaRoleMemberUtil.obtainLayoutForwardsFromAgendaForward(agendaForward);
		TerminalPO tvosTerminal = terminalDao.findByType(TerminalType.ANDROID_TVOS);
		LayoutForwardPO layoutForward = tetrisBvcQueryUtil.queryLayoutForwardByTerminalId(layoutForwards, tvosTerminal.getId());
		
		List<AgendaForwardSourcePO> agendaForwardSources = agendaForwardSourceDao.findByAgendaForwardId(agendaForward.getId());		
				
		Long combineTemplateId = layoutForward.getSourceId();
		List<CombineTemplatePositionPO> combineTemplatePositions = combineTemplatePositionDao.findByCombineTemplateIdIn(
				new ArrayListWrapper<Long>().add(combineTemplateId).getList());
		
		if(combineTemplatePositions.size() == 0){
			throw new BaseException(StatusCode.FORBIDDEN, "无法创建虚拟源合屏，因为合屏模板没有找到分屏，配置有误！id: " + combineTemplateId);
		}
		
		/*if(combineTemplatePositions.size() == 1){			
			Integer serialNum = combineTemplatePositions.get(0).getLayoutPositionSerialNum();			
			List<AgendaForwardSourcePO> ss = tetrisBvcQueryUtil.queryAgendaForwardSourcesBySerialNum(agendaForwardSources, serialNum, null);			
			if(ss.size() == 0){
				log.info("在议程转发 " + agendaForward.getName() + "layoutPosition的 serialNum = " + serialNum + " 上没有找到配置的源，不需要转发");
				return;
			}
		}*/
		
		//找出关联关系
		CombineTemplateGroupAgendeForwardPermissionPO p = combineTemplateGroupAgendeForwardPermissionDao.
				findByGroupIdAndCombineTemplateIdAndAgendaForwardId(group.getId(), combineTemplateId, agendaForward.getId());
		if(p == null){
			p = new CombineTemplateGroupAgendeForwardPermissionPO();
			p.setGroupId(group.getId());
			p.setCombineTemplateId(combineTemplateId);
			p.setTerminalId(layoutForward.getTerminalId());
			p.setAgendaId(agendaForward.getAgendaId());
			p.setAgendaForwardId(agendaForward.getId());
			p.setLayoutId(layoutForward.getLayoutId());
			combineTemplateGroupAgendeForwardPermissionDao.save(p);
		}
		businessDeliverBO.getUnusefulVideoPermissions().remove(p);
		businessDeliverBO.getUsefulVideoPermissions().add(p);
				
		//新建合屏
		BusinessCombineVideoPO combineVideo = combineVideoService.templateCombineVideo(group, null, combineTemplatePositions, agendaForwardSources, null, 3, businessDeliverBO);
		combineVideo.setUuid(agenda.getUuid());//【重要，两者uuid需要一样】
		//与合屏模板互相关联
		combineVideo.setCombineTemplates(new HashSet<CombineTemplateGroupAgendeForwardPermissionPO>());
		combineVideo.getCombineTemplates().add(p);
		p.setCombineVideo(combineVideo);					
		businessCombineVideoDao.save(combineVideo);
		
		businessDeliverBO.getStartCombineVideos().add(combineVideo);
		
		return combineVideo;
	}
}
