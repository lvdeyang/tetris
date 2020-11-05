package com.sumavision.tetris.zoom;

import java.util.List;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = ZoomPO.class, idClass = Long.class)
public interface ZoomDAO extends BaseDAO<ZoomPO>{

	/**
	 * 根据号码查询会议<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月2日 下午3:25:25
	 * @param String code 会议号码
	 * @return ZoomPO 会议
	 */
	public ZoomPO findByCode(String code);
	
	/**
	 * 根据号码批量查询会议<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月9日 上午9:35:34
	 * @param List<String> codeList 会议号码列表
	 * @return List<ZoomPO> 会议列表
	 */
	public List<ZoomPO> findByCodeIn(List<String> codeList);
	
	/**
	 * 查询用户创建的回忆<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月9日 上午9:37:04
	 * @param Long creatorUserId 创建用户id
	 * @return List<ZoomPO> 会议列表
	 */
	public List<ZoomPO> findByCreatorUserId(Long creatorUserId);
	
}
