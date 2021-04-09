package com.sumavision.tetris.bvc.model.agenda;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sumavision.tetris.bvc.business.BusinessInfoType;
import com.sumavision.tetris.bvc.business.dao.BusinessCombineVideoDAO;
import com.sumavision.tetris.bvc.business.po.combine.video.BusinessCombineVideoPO;
import com.sumavision.tetris.bvc.model.agenda.exception.AgendaNotFoundException;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;

/**
 * AgendaService<br/>
 * <p>详细描述</p>
 * <b>作者:</b>zsy<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年5月22日 上午10:03:43
 */
@Transactional(rollbackFor = Exception.class)
@Service
public class AgendaService {
	
	@Autowired
	private AgendaDAO agendaDao;
	
	@Autowired
	private BusinessCombineVideoDAO combineVideoDao;
	
	@Autowired
	private RoleAgendaPermissionDAO roleAgendaPermissionDao;

	@Autowired
	private CustomAudioDAO customAudioDao;
	
	@Autowired
	private AgendaForwardDAO agendaForwardDao;
	
	@Autowired
	private AgendaForwardSourceDAO agendaForwardSourceDao;
	
	@Autowired
	private AgendaForwardDestinationDAO agendaForwardDestinationDao;
	
	@Autowired
	private LayoutScopeDAO layoutScopeDao;
	
	/**
	 * 添加议程<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月30日 下午4:38:25
	 * @param String name 议程名称
	 * @param String remark 备注
	 * @param Long businessId 关联业务id
	 * @param String businessInfoTypeName 业务类型
	 * @return AgendaVO 议程
	 */
	@Transactional(rollbackFor = Exception.class)
	public AgendaVO add(
			String name,
			String remark,
			Long businessId,
			String businessInfoTypeName) throws Exception{
		AgendaPO agenda = new AgendaPO();
		agenda.setName(name);
		agenda.setRemark(remark);
		agenda.setVolume(50);
		if(businessId != null) agenda.setBusinessId(businessId);
		if(businessInfoTypeName!=null && !"".equals(businessInfoTypeName)){
			agenda.setBusinessInfoType(BusinessInfoType.fromName(businessInfoTypeName));
		}
		agenda.setAudioPriority(AudioPriority.GLOBAL_FIRST);
		agenda.setGlobalCustomAudio(false);
		agenda.setUpdateTime(new Date());
		agendaDao.save(agenda);
		return new AgendaVO().set(agenda);
	}
	
	/**
	 * 修改议程信息<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月30日 下午4:42:26
	 * @param Long id 议程id
	 * @param String name 议程名称
	 * @param String remark 备注
	 * @param String businessInfoTypeName 业务类型
	 * @return AgendaVO 议程
	 */
	@Transactional(rollbackFor = Exception.class)
	public AgendaVO edit(
			Long id,
			String name,
			String remark,
			String businessInfoTypeName) throws Exception{
		AgendaPO agenda = agendaDao.findOne(id);
		if(agenda == null){
			throw new AgendaNotFoundException(id);
		}
		agenda.setName(name);
		agenda.setRemark(remark);
		if(businessInfoTypeName!=null && !"".equals(businessInfoTypeName)){
			agenda.setBusinessInfoType(BusinessInfoType.fromName(businessInfoTypeName));
		}
		agenda.setUpdateTime(new Date());
		agendaDao.save(agenda);
		return new AgendaVO().set(agenda);
	}
	
	/**
	 * 是否启动全局音频修改<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月20日 下午1:52:44
	 * @param Long id 议程id
	 * @param Boolean globalCustomAudio 是否启动全局音频
	 * @return AgendaVO 议程
	 */
	@Transactional(rollbackFor = Exception.class)
	public AgendaVO globalCustomAudioChange(
			Long id,
			Boolean globalCustomAudio) throws Exception{
		AgendaPO agenda = agendaDao.findOne(id);
		if(agenda == null){
			throw new AgendaNotFoundException(id);
		}
		agenda.setGlobalCustomAudio(globalCustomAudio);
		agenda.setAudioType(globalCustomAudio?AudioType.SILENCE:AudioType.CUSTOM);
		agendaDao.save(agenda);
		
		List<AgendaForwardPO> agendaForwards = agendaForwardDao.findByAgendaId(id);
		if(agendaForwards!=null && agendaForwards.size()>0){
			if(globalCustomAudio){
				List<Long> agendaForwardIds = new ArrayList<Long>();
				for(AgendaForwardPO agendaForward:agendaForwards){
					agendaForwardIds.add(agendaForward.getId());
					agendaForward.setAudioType(null);
					agendaForward.setVolume(null);
				}
				List<CustomAudioPO> customAudios = customAudioDao.findByPermissionIdInAndPermissionType(agendaForwardIds, CustomAudioPermissionType.AGENDA_FORWARD);
				if(customAudios!=null && customAudios.size()>0){
					customAudioDao.deleteInBatch(customAudios);
				}
			}else{
				for(AgendaForwardPO agendaForward:agendaForwards){
					agendaForward.setAudioType(AudioType.CUSTOM);
					agendaForward.setVolume(50);
				}
				List<CustomAudioPO> customAudios = customAudioDao.findByPermissionIdInAndPermissionType(new ArrayListWrapper<Long>().add(id).getList(), CustomAudioPermissionType.AGENDA);
				if(customAudios!=null && customAudios.size()>0){
					customAudioDao.deleteInBatch(customAudios);
				}
			}
			agendaForwardDao.save(agendaForwards);
		}
		
		return new AgendaVO().set(agenda);
	}
	
	/**
	 * 议程音频类型修改<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月27日 上午9:16:14
	 * @param Long id 议程id
	 * @param String audioType 音频类型
	 * @return AgendaVO 议程
	 */
	@Transactional(rollbackFor = Exception.class)
	public AgendaVO audioTypeChange(
			Long id,
			String audioType) throws Exception{
		AgendaPO agenda = agendaDao.findOne(id);
		if(agenda == null){
			throw new AgendaNotFoundException(id);
		}
		agenda.setAudioType(AudioType.valueOf(audioType));
		agendaDao.save(agenda);
		
		if(agenda.getAudioType().equals(AudioType.SILENCE)){
			List<CustomAudioPO> customAudioEntities = customAudioDao.findByPermissionIdInAndPermissionType(new ArrayListWrapper<Long>().add(id).getList(), CustomAudioPermissionType.AGENDA);
			if(customAudioEntities != null){
				customAudioDao.deleteInBatch(customAudioEntities);
			}
		}
		
		return new AgendaVO().set(agenda);
	}
	
	/**
	 * 修改议程音频优先级设置<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月27日 上午11:13:08
	 * @param Long id 议程id
	 * @param String audioPriority 音频优先级
	 * @return AgendaVO 议程
	 */
	public AgendaVO audioPriorityChange(
			Long id,
			String audioPriority) throws Exception{
		AgendaPO agenda = agendaDao.findOne(id);
		if(agenda == null){
			throw new AgendaNotFoundException(id);
		}
		agenda.setAudioPriority(AudioPriority.valueOf(audioPriority));
		agendaDao.save(agenda);
		return new AgendaVO().set(agenda);
	}
	
	/**
	 * 议程音量修改<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月20日 下午4:05:54
	 * @param Long id 议程id
	 * @param Integer volume 音量
	 */
	@Transactional(rollbackFor = Exception.class)
	public void volumeChange(
			Long id,
			Integer volume) throws Exception{
		AgendaPO agenda = agendaDao.findOne(id);
		if(agenda == null){
			throw new AgendaNotFoundException(id);
		}
		agenda.setVolume(volume);
		agendaDao.save(agenda);
	}
	
	/**
	 * 删除议程<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月30日 下午4:43:30
	 * @param Long id 议程id
	 */
	@Transactional(rollbackFor = Exception.class)
	public void delete(Long id) throws Exception{
		AgendaPO agenda = agendaDao.findOne(id);
				
		//如果删掉的议程是一个虚拟源
		if(BusinessInfoType.COMBINE_VIDEO_VIRTUAL_SOURCE.equals(agenda.getBusinessInfoType())){
			List<AgendaForwardSourcePO> agendaForwardSources = agendaForwardSourceDao.findBySourceIdAndSourceType(id, SourceType.COMBINE_VIDEO_VIRTUAL_SOURCE);
			//校验这些源是否正在合屏中使用
			BusinessCombineVideoPO combineVideo = combineVideoDao.findByUuid(agenda.getUuid());
			if(combineVideo != null){
				throw new BaseException(StatusCode.FORBIDDEN, "虚拟源正在使用，不能删除");
			}
			agendaForwardSourceDao.deleteInBatch(agendaForwardSources);
		}
		
		if(agenda != null){
			agendaDao.delete(agenda);
		}
		List<RoleAgendaPermissionPO>  roleAgendaPermissions = roleAgendaPermissionDao.findByAgendaId(id);
		if(roleAgendaPermissions!=null && roleAgendaPermissions.size()>0){
			roleAgendaPermissionDao.deleteInBatch(roleAgendaPermissions);
		}
		List<CustomAudioPO> customAudios = customAudioDao.findByPermissionIdInAndPermissionType(new ArrayListWrapper<Long>().add(id).getList(), CustomAudioPermissionType.AGENDA);
		if(customAudios!=null && customAudios.size()>0){
			customAudioDao.deleteInBatch(customAudios);
		}
		
		List<AgendaForwardPO> agendaForwards = agendaForwardDao.findByAgendaId(id);
		if(agendaForwards!=null && agendaForwards.size()>0){
			agendaForwardDao.deleteInBatch(agendaForwards);
			List<Long> agendaForwardIds = new ArrayList<Long>();
			for(AgendaForwardPO agendaForward:agendaForwards){
				agendaForwardIds.add(agendaForward.getId());
			}
			List<AgendaForwardSourcePO> agendaForwardSources = agendaForwardSourceDao.findByAgendaForwardIdIn(agendaForwardIds);
			if(agendaForwardSources!=null && agendaForwardSources.size()>0){
				agendaForwardSourceDao.deleteInBatch(agendaForwardSources);
			}
			List<AgendaForwardDestinationPO> agendaFrowardDestinations = agendaForwardDestinationDao.findByAgendaForwardIdIn(agendaForwardIds);
			if(agendaFrowardDestinations!=null && agendaFrowardDestinations.size()>0){
				agendaForwardDestinationDao.deleteInBatch(agendaFrowardDestinations);
			}
			List<CustomAudioPO> forwardCustomAudios = customAudioDao.findByPermissionIdInAndPermissionType(agendaForwardIds, CustomAudioPermissionType.AGENDA_FORWARD);
			if(forwardCustomAudios!=null && forwardCustomAudios.size()>0){
				customAudioDao.deleteInBatch(forwardCustomAudios);
			}
			List<LayoutScopePO> layoutScopes = layoutScopeDao.findByAgendaForwardIdIn(agendaForwardIds);
			if(layoutScopes!=null && layoutScopes.size()>0){
				layoutScopeDao.deleteInBatch(layoutScopes);
			}
		}
	}
		
}
