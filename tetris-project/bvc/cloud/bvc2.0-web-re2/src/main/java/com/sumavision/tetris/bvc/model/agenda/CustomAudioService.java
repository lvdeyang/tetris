package com.sumavision.tetris.bvc.model.agenda;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.sumavision.tetris.bvc.business.dao.BusinessGroupMemberDAO;
import com.sumavision.tetris.bvc.business.po.member.BusinessGroupMemberPO;
import com.sumavision.tetris.bvc.business.po.member.BusinessGroupMemberTerminalChannelPO;
import com.sumavision.tetris.bvc.business.po.member.dao.BusinessGroupMemberTerminalChannelDAO;
import com.sumavision.tetris.bvc.model.role.RoleChannelDAO;
import com.sumavision.tetris.bvc.model.role.RoleChannelPO;
import com.sumavision.tetris.bvc.model.role.RoleDAO;
import com.sumavision.tetris.bvc.model.role.RolePO;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

@Service
public class CustomAudioService {

	@Autowired
	private CustomAudioDAO customAudioDao;
	
	@Autowired
	private RoleDAO roleDao;

	@Autowired
	private RoleChannelDAO roleChannelDao;
	
	@Autowired
	private BusinessGroupMemberTerminalChannelDAO businessGroupMemberTerminalChannelDao;
	
	@Autowired
	private BusinessGroupMemberDAO businessGroupMemberDao;
	
	/**
	 * 添加议程音频<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月20日 下午5:25:12
	 * @param Long agendaId 议程id
	 * @param JSONString roleChannelIds 角色通道id列表
	 * @param JSONString groupMemberChannelIds 会议成员通道id列表
	 * @return ist<CustomAudioVO> 音频列表
	 */
	@Transactional(rollbackFor = Exception.class)
	public List<CustomAudioVO> addAgendaAudio(
			Long agendaId,
			String roleChannelIds,
			String groupMemberChannelIds) throws Exception{
		
		List<Long> parsedRoleChannelIds = null;
		if(roleChannelIds!=null && !"".equals(roleChannelIds)) parsedRoleChannelIds = JSON.parseArray(roleChannelIds, Long.class);
		List<Long> parsedGroupMemberChannelIds = null;
		if(groupMemberChannelIds!=null && !"".equals(groupMemberChannelIds)) parsedGroupMemberChannelIds = JSON.parseArray(groupMemberChannelIds, Long.class);
		
		List<CustomAudioPO> customAudios = new ArrayList<CustomAudioPO>();
		if(parsedRoleChannelIds!=null && parsedRoleChannelIds.size()>0){
			for(Long roleChannelId:parsedRoleChannelIds){
				CustomAudioPO customAudio = new CustomAudioPO();
				customAudio.setSourceId(roleChannelId);
				customAudio.setSourceType(SourceType.ROLE_CHANNEL);
				customAudio.setPermissionId(agendaId);
				customAudio.setPermissionType(CustomAudioPermissionType.AGENDA);
				customAudio.setUpdateTime(new Date());
				customAudios.add(customAudio);
			}
		}
		
		if(parsedGroupMemberChannelIds!=null && parsedGroupMemberChannelIds.size()>0){
			for(Long groupMemberChannelId:parsedGroupMemberChannelIds){
				CustomAudioPO customAudio = new CustomAudioPO();
				customAudio.setSourceId(groupMemberChannelId);
				customAudio.setSourceType(SourceType.GROUP_MEMBER_CHANNEL);
				customAudio.setPermissionId(agendaId);
				customAudio.setPermissionType(CustomAudioPermissionType.AGENDA);
				customAudio.setUpdateTime(new Date());
				customAudios.add(customAudio);
			}
		}
		
		customAudioDao.save(customAudios);
		
		List<RoleChannelPO> roleChannels = (parsedRoleChannelIds==null||parsedRoleChannelIds.size()<=0?null:roleChannelDao.findAll(parsedRoleChannelIds));
		Set<Long> roleIds = new HashSet<Long>();
		if(roleChannels!=null && roleChannels.size()>0){
			for(RoleChannelPO roleChannel:roleChannels){
				roleIds.add(roleChannel.getRoleId());
			}
		}
		List<RolePO> roles = null;
		if(roleIds.size() > 0){
			roles = roleDao.findAll(roleIds);
		}
		
		List<BusinessGroupMemberTerminalChannelPO> memberChannels = (parsedGroupMemberChannelIds==null||parsedGroupMemberChannelIds.size()<=0?null:businessGroupMemberTerminalChannelDao.findAll(parsedGroupMemberChannelIds));
		List<Object[]> originMemberIdMaps = (parsedGroupMemberChannelIds==null||parsedGroupMemberChannelIds.size()<=0?null:businessGroupMemberTerminalChannelDao.findGroupMemberIdByIdIn(parsedGroupMemberChannelIds));
		Map<Long, Long> memberIdMaps = null;
		if(originMemberIdMaps!=null && originMemberIdMaps.size()>0){
			memberIdMaps = new HashMap<Long, Long>();
			for(Object[] originMemberIdMap:originMemberIdMaps){
				memberIdMaps.put(Long.valueOf(originMemberIdMap[0].toString()), Long.valueOf(originMemberIdMap[1].toString()));
			}
		}
		List<BusinessGroupMemberPO> members = (memberIdMaps==null||memberIdMaps.size()<=0?null:businessGroupMemberDao.findAll(memberIdMaps.values()));
		
		List<CustomAudioVO> audios = CustomAudioVO.getConverter(CustomAudioVO.class).convert(customAudios, CustomAudioVO.class);
		for(CustomAudioVO audio:audios){
			if(audio.getSourceType().equals(SourceType.ROLE_CHANNEL.toString())){
				if(roleChannels!=null && roleChannels.size()>0){
					for(RoleChannelPO roleChannel:roleChannels){
						if(roleChannel.getId().equals(audio.getSourceId())){
							if(roles!=null && roles.size()>0){
								for(RolePO role:roles){
									if(role.getId().equals(roleChannel.getRoleId())){
										audio.setSourceName(new StringBufferWrapper().append(role.getName()).append(" - ").append(roleChannel.getName()).toString());
										break;
									}
								}
							}
							break;
						}
						
					}
				}
			}else if(audio.getSourceType().equals(SourceType.GROUP_MEMBER_CHANNEL.toString())){
				if(memberChannels!=null && memberChannels.size()>0){
					
					for(BusinessGroupMemberTerminalChannelPO memberChannel:memberChannels){
						if(memberChannel.getId().equals(audio.getSourceId())){
							if(members!=null && members.size()>0){
								for(BusinessGroupMemberPO member:members){
									if(member.getId().equals(memberIdMaps.get(memberChannel.getId()))){
										audio.setSourceName(new StringBufferWrapper().append(member.getName()).append(" - ").append(memberChannel.getTerminalChannelname()).toString());
										break;
									}
								}
							}
							break;
						}
						
					}
					
				}
			}
		}
		return audios;
	}
	
	/**
	 * 删除议程自定义音频<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月22日 上午10:37:30
	 * @param Long id 自定义音频id
	 */
	@Transactional(rollbackFor = Exception.class)
	public void remove(Long id) throws Exception{
		 CustomAudioPO customAudio = customAudioDao.findOne(id);
		 if(customAudio != null){
			 customAudioDao.delete(customAudio);
		 }
	}
	
}
