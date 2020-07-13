package com.sumavision.tetris.bvc.model.agenda;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.suma.venus.resource.dao.BundleDao;
import com.suma.venus.resource.pojo.BundlePO;
import com.sumavision.tetris.bvc.model.agenda.combine.CombineAudioDAO;
import com.sumavision.tetris.bvc.model.agenda.combine.CombineAudioPO;
import com.sumavision.tetris.bvc.model.agenda.combine.CombineVideoDAO;
import com.sumavision.tetris.bvc.model.agenda.combine.CombineVideoPO;
import com.sumavision.tetris.bvc.model.role.RoleChannelDAO;
import com.sumavision.tetris.bvc.model.role.RoleChannelPO;
import com.sumavision.tetris.bvc.model.role.RoleDAO;
import com.sumavision.tetris.bvc.model.role.RolePO;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

@Component
public class AgendaForwardQuery {

	@Autowired
	private AgendaForwardDAO agendaForwardDao;
	
	@Autowired
	private RoleDAO roleDao;
	
	@Autowired
	private RoleChannelDAO roleChannelDao;
	
	@Autowired
	private CombineVideoDAO combineVideoDao;
	
	@Autowired
	private CombineAudioDAO combineAudioDao;
	
	@Autowired
	private BundleDao bundleDao;
	
	/**
	 * 查询议程转发<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月9日 上午10:04:39
	 * @param Long agendaId 议程id
	 * @return List<AgendaForwardVO> 转发列表
	 */
	public List<AgendaForwardVO> load(Long agendaId) throws Exception{
		List<AgendaForwardPO> forwardEntities =  agendaForwardDao.findByAgendaId(agendaId);
		return packForwards(forwardEntities);
	}
	
	/**
	 * 议程转发数据转换<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月9日 上午10:05:37
	 * @param List<AgendaForwardPO> forwardEntities 转发数据（持久化）
	 * @return List<AgendaForwardVO> 转发数据（视图）
	 */
	public List<AgendaForwardVO> packForwards(List<AgendaForwardPO> forwardEntities) throws Exception{
		List<AgendaForwardVO> forwards = null;
		if(forwardEntities!=null && forwardEntities.size()>0){
			Set<Long> roleIds = new HashSet<Long>();
			Set<Long> roleChannelIds = new HashSet<Long>();
			Set<Long> combineVideoIds = new HashSet<Long>();
			Set<Long> combineAudioIds = new HashSet<Long>();
			Set<String> bundleIds = new HashSet<String>();
			for(AgendaForwardPO forwardEntity:forwardEntities){
				if(forwardEntity.getSourceType().equals(AgendaSourceType.ROLE)){
					roleIds.add(Long.valueOf(forwardEntity.getSourceId()));
				}else if(forwardEntity.getSourceType().equals(AgendaSourceType.ROLE_CHANNEL)){
					roleChannelIds.add(Long.valueOf(forwardEntity.getSourceId()));
				}else if(forwardEntity.getSourceType().equals(AgendaSourceType.COMBINE_VIDEO)){
					combineVideoIds.add(Long.valueOf(forwardEntity.getSourceId()));
				}else if(forwardEntity.getSourceType().equals(AgendaSourceType.COMBINE_AUDIO)){
					combineAudioIds.add(Long.valueOf(forwardEntity.getSourceId()));
				}else if(forwardEntity.getSourceType().equals(AgendaSourceType.BUNDLE)){
					bundleIds.add(forwardEntity.getSourceId());
				}else if(forwardEntity.getSourceType().equals(AgendaSourceType.CHANNEL)){
					bundleIds.add(forwardEntity.getSourceId().split("-")[0]);
				}
				if(forwardEntity.getDestinationType().equals(AgendaDestinationType.ROLE)){
					roleIds.add(Long.valueOf(forwardEntity.getDestinationId()));
				}else if(forwardEntity.getDestinationType().equals(AgendaDestinationType.ROLE_CHANNEL)){
					roleChannelIds.add(Long.valueOf(forwardEntity.getDestinationId()));
				}else if(forwardEntity.getDestinationType().equals(AgendaDestinationType.BUNDLE)){
					bundleIds.add(forwardEntity.getDestinationId());
				}else if(forwardEntity.getDestinationType().equals(AgendaDestinationType.CHANNEL)){
					bundleIds.add(forwardEntity.getDestinationId().split("-")[0]);
				}
			}
			List<RolePO> roles = null;
			List<RoleChannelPO> roleChannels = null;
			List<CombineVideoPO> combineVideos = null;
			List<CombineAudioPO> combineAudios = null;
			List<BundlePO> bundles = null;
			if(roleChannelIds.size() > 0) roleChannels = roleChannelDao.findAll(roleChannelIds);
			if(roleChannels!=null && roleChannels.size()>0){
				for(RoleChannelPO roleChannel:roleChannels){
					roleIds.add(roleChannel.getRoleId());
				}
			}
			if(roleIds.size() > 0) roles = roleDao.findAll(roleIds);
			if(combineVideoIds.size() > 0) combineVideos = combineVideoDao.findAll(combineVideoIds);
			if(combineAudioIds.size() > 0) combineAudios = combineAudioDao.findAll(combineAudioIds);
			if(bundleIds.size() > 0) bundles = bundleDao.findByBundleIdIn(bundleIds);
			forwards = AgendaForwardVO.getConverter(AgendaForwardVO.class).convert(forwardEntities, AgendaForwardVO.class);
			for(AgendaForwardVO forward:forwards){
				if(forward.getSourceType().equals(AgendaSourceType.ROLE.toString())){
					if(roles!=null && roles.size()>0){
						for(RolePO role:roles){
							if(role.getId().toString().equals(forward.getSourceId())){
								forward.setSourceName(role.getName());
								break;
							}
						}
					}
				}else if(forward.getSourceType().equals(AgendaSourceType.ROLE_CHANNEL.toString())){
					if(roleChannels!=null && roleChannels.size()>0){
						for(RoleChannelPO roleChannel:roleChannels){
							if(roleChannel.getId().toString().equals(forward.getSourceId())){
								RolePO targetRole = null;
								for(RolePO role:roles){
									if(role.getId().equals(roleChannel.getRoleId())){
										targetRole = role;
										break;
									}
								}
								forward.setSourceName(new StringBufferWrapper().append(targetRole.getName()).append("-").append(roleChannel.getName()).toString());
								break;
							}
						}
					}
				}else if(forward.getSourceType().equals(AgendaSourceType.COMBINE_VIDEO.toString())){
					if(combineVideos!=null && combineVideos.size()>0){
						for(CombineVideoPO combineVideo:combineVideos){
							if(combineVideo.getId().toString().equals(forward.getSourceId())){
								forward.setSourceName(combineVideo.getName());
								break;
							}
						}
					}
				}else if(forward.getSourceType().equals(AgendaSourceType.COMBINE_AUDIO.toString())){
					if(combineAudios!=null && combineAudios.size()>0){
						for(CombineAudioPO combineAudio:combineAudios){
							if(combineAudio.getId().toString().equals(forward.getSourceId())){
								forward.setSourceName(combineAudio.getName());
								break;
							}
						}
					}
				}else if(forward.getSourceType().equals(AgendaSourceType.BUNDLE.toString())){
					if(bundles!=null && bundles.size()>0){
						for(BundlePO bundle:bundles){
							if(bundle.getBundleId().equals(forward.getSourceId())){
								forward.setSourceName(bundle.getBundleName());
								break;
							}
						}
					}
				}else if(forward.getSourceType().equals(AgendaSourceType.CHANNEL.toString())){
					if(bundleIds!=null && bundles.size()>0){
						for(BundlePO bundle:bundles){
							if(forward.getSourceId().startsWith(bundle.getBundleId())){
								forward.setSourceName(forward.getSourceId().replace(bundle.getBundleId(), bundle.getBundleName()));
								break;
							}
						}
					}
				}
				if(forward.getDestinationType().equals(AgendaDestinationType.ROLE.toString())){
					if(roles!=null && roles.size()>0){
						for(RolePO role:roles){
							if(role.getId().toString().equals(forward.getDestinationId())){
								forward.setDestinationName(role.getName());
								break;
							}
						}
					}
				}else if(forward.getDestinationType().equals(AgendaDestinationType.ROLE_CHANNEL.toString())){
					if(roleChannels!=null && roleChannels.size()>0){
						for(RoleChannelPO roleChannel:roleChannels){
							if(roleChannel.getId().toString().equals(forward.getDestinationId())){
								RolePO targetRole = null;
								for(RolePO role:roles){
									if(role.getId().equals(roleChannel.getRoleId())){
										targetRole = role;
										break;
									}
								}
								forward.setDestinationName(new StringBufferWrapper().append(targetRole.getName()).append("-").append(roleChannel.getName()).toString());
								break;
							}
						}
					}
				}else if(forward.getDestinationType().equals(AgendaDestinationType.BUNDLE.toString())){
					if(bundles!=null && bundles.size()>0){
						for(BundlePO bundle:bundles){
							if(bundle.getBundleId().equals(forward.getDestinationId())){
								forward.setDestinationName(bundle.getBundleName());
								break;
							}
						}
					}
				}else if(forward.getDestinationType().equals(AgendaDestinationType.CHANNEL.toString())){
					if(bundleIds!=null && bundles.size()>0){
						for(BundlePO bundle:bundles){
							if(forward.getDestinationId().startsWith(bundle.getBundleId())){
								forward.setDestinationName(forward.getDestinationId().replace(bundle.getBundleId(), bundle.getBundleName()));
								break;
							}
						}
					}
				}
			}
		}
		return forwards;
	}
	
}
