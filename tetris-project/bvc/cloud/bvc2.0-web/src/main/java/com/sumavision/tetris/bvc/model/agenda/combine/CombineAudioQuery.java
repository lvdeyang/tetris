package com.sumavision.tetris.bvc.model.agenda.combine;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.sumavision.tetris.bvc.model.role.RoleChannelDAO;
import com.sumavision.tetris.bvc.model.role.RoleChannelPO;
import com.sumavision.tetris.bvc.model.role.RoleDAO;
import com.sumavision.tetris.bvc.model.role.RolePO;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

@Component
public class CombineAudioQuery {

	@Autowired
	@Qualifier("com.sumavision.tetris.bvc.model.agenda.combine.CombineAudioDAO")
	private CombineAudioDAO combineAudioDao;
	
	@Autowired
	@Qualifier("com.sumavision.tetris.bvc.model.agenda.combine.CombineAudioSrcDAO")
	private CombineAudioSrcDAO combineAudioSrcDao;
	
	@Autowired
	private RoleChannelDAO roleChannelDao;
	
	@Autowired
	private RoleDAO roleDao;
	
	/**
	 * 查询业务下的混音<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月7日 下午3:52:48
	 * @param Long businessId 业务id
	 * @param String businessType 业务类型
	 * @return List<CombineAudioVO> 混音列表
	 */
	public List<CombineAudioVO> load(
			Long businessId,
			String businessType) throws Exception{
		
		List<CombineAudioPO> entities = combineAudioDao.findByBusinessIdAndBusinessType(businessId, CombineBusinessType.valueOf(businessType));
		return CombineAudioVO.getConverter(CombineAudioVO.class).convert(entities, CombineAudioVO.class);
	}
	 
	/**
	 * 查询混音源<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月8日 下午1:56:13
	 * @param Long combineAudioId 混音id
	 * @return List<CombineAudioSrcVO> 混音源列表 
	 */
	public List<CombineAudioSrcVO> loadSrcs(
			Long combineAudioId) throws Exception{
		List<CombineAudioSrcVO> srcs = new ArrayList<CombineAudioSrcVO>();
		List<CombineAudioSrcPO> srcEntities =  combineAudioSrcDao.findByCombineAudioId(combineAudioId);
		if(srcEntities!=null && srcEntities.size()>0){
			List<Long> roleChannelSrcIds = new ArrayList<Long>();
			for(CombineAudioSrcPO srcEntity:srcEntities){
				if(CombineAudioSrcType.ROLE_CHANNEL.equals(srcEntity.getCombineAudioSrcType())){
					roleChannelSrcIds.add(Long.valueOf(srcEntity.getSrcId()));
				}
			}
			List<RoleChannelPO> roleChannelEntities = null;
			List<RolePO> roles = null;
			if(roleChannelSrcIds.size() > 0){
				roleChannelEntities = roleChannelDao.findAll(roleChannelSrcIds);
				Set<Long> roleIds = new HashSet<Long>();
				for(RoleChannelPO roleChannelEntity:roleChannelEntities){
					roleIds.add(roleChannelEntity.getRoleId());
				}
				roles = roleDao.findAll(roleIds);
			}
			for(CombineAudioSrcPO srcEntity:srcEntities){
				CombineAudioSrcVO src = new CombineAudioSrcVO().set(srcEntity);
				if(srcEntity.getCombineAudioSrcType().equals(CombineAudioSrcType.ROLE_CHANNEL)){
					RoleChannelPO targetRoleChannelEntity = null;
					RolePO targetRoleEntity = null;
					if(roleChannelEntities!=null && roleChannelEntities.size()>0){
						for(RoleChannelPO roleChannelEntity:roleChannelEntities){
							if(roleChannelEntity.getId().toString().equals(srcEntity.getSrcId())){
								targetRoleChannelEntity = roleChannelEntity;
								break;
							}
						}
					}
					if(targetRoleChannelEntity!=null && roles!=null && roles.size()>0){
						for(RolePO role:roles){
							if(role.getId().equals(targetRoleChannelEntity.getRoleId())){
								targetRoleEntity = role;
								break;
							}
						}
					}
					StringBufferWrapper name = new StringBufferWrapper();
					if(targetRoleEntity != null){
						name.append(targetRoleEntity.getName());
					}
					name.append("-");
					if(targetRoleChannelEntity != null){
						name.append(targetRoleChannelEntity.getName());
					}
					src.setName(name.toString());
				}
				srcs.add(src);
			}
		}
		return srcs;
	}
	
	
}
