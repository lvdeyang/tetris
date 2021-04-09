package com.sumavision.tetris.bvc.business.dao;

import java.util.Collection;
import java.util.List;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.bvc.business.po.combine.audio.CombineAudioPermissionPO;
import com.sumavision.tetris.bvc.model.agenda.CustomAudioPermissionType;
import com.sumavision.tetris.orm.dao.MetBaseDAO;

@RepositoryDefinition(domainClass = CombineAudioPermissionPO.class, idClass = long.class)
public interface CombineAudioPermissionDAO extends MetBaseDAO<CombineAudioPermissionPO>{

	public List<CombineAudioPermissionPO> findByGroupId(Long groupId);

	/**
	 * 通过组id以及议程id查询<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月24日 上午9:00:29
	 * @param groupId
	 * @param agendaId
	 * @return
	 */
	public List<CombineAudioPermissionPO> findByGroupIdAndAgendaId(Long groupId, Long agendaId);
	
	/**
	 * 通过组id以及permissionId（议程id或者议程转发id）以及议程转发类型<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月24日 上午9:00:52
	 * @param groupId
	 * @param permissionId
	 * @return
	 */
	public CombineAudioPermissionPO findByGroupIdAndPermissionIdAndPermissionType(Long groupId, Long permissionId, CustomAudioPermissionType permissionType);
	
	public List<CombineAudioPermissionPO> findByGroupIdAndPermissionIdInAndPermissionType(Long groupId, Collection<Long> permissionId, CustomAudioPermissionType permissionType);
	
	public List<CombineAudioPermissionPO> findByGroupIdAndPermissionIdIn(Long groupId, Collection<Long> permissionId);
}
