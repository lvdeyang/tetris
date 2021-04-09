package com.sumavision.tetris.bvc.model.agenda;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = CustomAudioPO.class, idClass = Long.class)
public interface CustomAudioDAO extends BaseDAO<CustomAudioPO>{
	
	/**
	 * 根据关联查询自定义音频<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月20日 上午10:04:51
	 * @param Collection<Long> permissionIds 关联id列表
	 * @param CustomAudioPermissionType permissionType 关联类型
	 * @return List<CustomAudioPO> 音频列表
	 */
	public List<CustomAudioPO> findByPermissionIdInAndPermissionType(Collection<Long> permissionIds, CustomAudioPermissionType permissionType);
	public List<CustomAudioPO> findByPermissionIdAndPermissionType(Long permissionId, CustomAudioPermissionType permissionType);
	
	/**
	 * 根据源查询转发源<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月20日 下午3:17:07
	 * @param Long permissionId 关联id
	 * @param CustomAudioPermissionType permissionType 关联类型
	 * @param Collection<Long> sourceIds 源id列表
 	 * @param SourceType sourceType 源类型
	 * @return List<CustomAudioPO> 音频列表
	 */
	public List<CustomAudioPO> findByPermissionIdAndPermissionTypeAndSourceIdInAndSourceType(Long permissionId, CustomAudioPermissionType permissionType, Collection<Long> sourceIds, SourceType sourceType);
	
	/**
	 * 根据源查询转发源<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月20日 下午3:17:07
	 * @param Collection<Long> permissionIds 关联id列表
	 * @param CustomAudioPermissionType permissionType 关联类型
	 * @param Collection<Long> sourceIds 源id列表
 	 * @param SourceType sourceType 源类型
	 * @return List<CustomAudioPO> 音频列表
	 */
	public List<CustomAudioPO> findByPermissionIdInAndPermissionTypeAndSourceIdInAndSourceType(Collection<Long> permissionIds, CustomAudioPermissionType permissionType, Collection<Long> sourceIds, SourceType sourceType);
	
	/**
	 * 根据成员id列表查询音频源<br/>
	 * <p>只查询根据“成员通道”配置的源，不包含根据“角色通道”配置的</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年12月28日 下午6:37:45
	 * @param memberIds
	 * @return
	 */
	@Query(value="SELECT * FROM TETRIS_BVC_MODEL_CUSTOM_AUDIO source INNER JOIN BVC_BUSINESS_GROUP_MEMBER_CHANNEL channel ON channel.id=source.source_id WHERE source.source_type='GROUP_MEMBER_CHANNEL' and channel.member_id in ?1", nativeQuery=true)
	public List<CustomAudioPO> findCustomAudiosByMemberIds(List<Long> memberIds);
	
}
