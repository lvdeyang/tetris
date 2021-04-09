package com.sumavision.tetris.bvc.business.po.info;

import java.util.List;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.MetBaseDAO;

@RepositoryDefinition(domainClass = GroupCommandInfoPO.class, idClass = Long.class)
public interface GroupCommandInfoDAO extends MetBaseDAO<GroupCommandInfoPO> {
	
	public GroupCommandInfoPO findByGroupId(Long groupId);
	
	public List<GroupCommandInfoPO> findByHasSecret(Boolean hasSecret);
	
	/**
	 * 通过groupId查找协同指挥<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年3月4日 上午10:28:06
	 * @param hasCooperate
	 * @param groupId
	 * @return
	 */
	public GroupCommandInfoPO findByHasCooperateAndGroupId(Boolean hasCooperate, Long groupId);
	
	/**
	 * 通过groupId查找越级指挥<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年3月4日 上午10:29:28
	 * @param hasCross
	 * @param groupId
	 * @return
	 */
	public GroupCommandInfoPO findByHasCrossAndGroupId(Boolean hasCross, Long groupId);
}
