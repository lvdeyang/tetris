package com.sumavision.tetris.bvc.business.group.forward;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = Jv230ForwardPO.class, idClass = Long.class)
public interface Jv230ForwardDAO extends BaseDAO<Jv230ForwardPO>{

	/**
	 * 查询JV230在用户、终端和业务类型下的转发业务<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月15日 下午2:18:25
	 * @param String bundleId 设备id
	 * @param String userId 用户id
	 * @param Long terminalId 终端id
	 * @param ForwardBusinessType businessType 业务类型
	 * @return List<Jv230ForwardPO> JV230转发业务
	 */
	public List<Jv230ForwardPO> findByBundleIdAndUserIdAndTerminalIdAndBusinessType(String bundleId, String userId, Long terminalId, ForwardBusinessType businessType);
	
	/**
	 * 查询JV230在用户、终端和业务类型下的转发业务，按照分屏序号排序<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月15日 下午2:18:25
	 * @param String bundleId 设备id
	 * @param String userId 用户id
	 * @param Long terminalId 终端id
	 * @param ForwardBusinessType businessType 业务类型
	 * @return List<Jv230ForwardPO> JV230转发业务
	 */
	public List<Jv230ForwardPO> findByBundleIdAndUserIdAndTerminalIdAndBusinessTypeOrderBySerialNum(String bundleId, String userId, Long terminalId, ForwardBusinessType businessType);
	
	/**
	 * 查询JV230某分屏在用户、终端和业务类型下的转发业务<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月15日 下午2:18:25
	 * @param int serialNum 分屏序号
	 * @param String bundleId 设备id
	 * @param String userId 用户id
	 * @param Long terminalId 终端id
	 * @param ForwardBusinessType businessType 业务类型
	 * @return List<Jv230ForwardPO> JV230转发业务
	 */
	public Jv230ForwardPO findByBundleIdAndSerialNumAndUserIdAndTerminalIdAndBusinessType(String bundleId, int serialNum, String userId, Long terminalId, ForwardBusinessType businessType);
	
	/**
	 * 根据用户、终端和业务类型查询JV230转发业务br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月15日 下午2:25:36
	 * @param String userId 用户id
	 * @param Long terminalId 终端id
	 * @param ForwardBusinessType businessType 业务类型
	 * @return List<Jv230ForwardPO> JV230转发业务
	 */
	public List<Jv230ForwardPO> findByUserIdAndTerminalIdAndBusinessType(String userId, Long terminalId, ForwardBusinessType businessType);
	
	/**
	 * 根据用户、终端和业务类型查询JV230某个分屏转发业务br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月15日 下午2:25:36
	 * @param int serialNum 分屏序号
	 * @param String userId 用户id
	 * @param Long terminalId 终端id
	 * @param ForwardBusinessType businessType 业务类型
	 * @return List<Jv230ForwardPO> JV230转发业务
	 */
	public List<Jv230ForwardPO> findBySerialNumAndUserIdAndTerminalIdAndBusinessType(int serialNum, String userId, Long terminalId, ForwardBusinessType businessType);

	/**
	 * 根据源类型用户、终端和业务类型查询JV230转发业务br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月15日 下午2:25:36
	 * @param Collection<Jv230ForwardSourceType> sourceType 转发源类型
	 * @param String userId 用户id
	 * @param Long terminalId 终端id
	 * @param ForwardBusinessType businessType 业务类型
	 * @return List<Jv230ForwardPO> JV230转发业务
	 */
	public List<Jv230ForwardPO> findBySourceTypeInAndUserIdAndTerminalIdAndBusinessType(Collection<ForwardSourceType> sourceType, String userId, Long terminalId, ForwardBusinessType businessType);
	
	/**
	 * 根据用户、终端和业务类型查询JV230某个源类型的转发业务br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月15日 下午2:25:36
	 * @param String bundleId 设备id
	 * @param Collection<Jv230ForwardSourceType> sourceTypes 转发源类型
	 * @param String userId 用户id
	 * @param Long terminalId 终端id
	 * @param ForwardBusinessType businessType 业务类型
	 * @return List<Jv230ForwardPO> JV230转发业务
	 */
	public List<Jv230ForwardPO> findByBundleIdAndSourceTypeInAndUserIdAndTerminalIdAndBusinessType(String bundleId, Collection<ForwardSourceType> sourceTypes, String userId, Long terminalId, ForwardBusinessType businessType);

	/**
	 * 根据用户、终端和业务类型查询JV230某个源类型的转发业务br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月15日 下午2:25:36
	 * @param (Collection<String> bundleIds 设备id列表
	 * @param Collection<Jv230ForwardSourceType> sourceTypes 转发源类型
	 * @param String userId 用户id
	 * @param Long terminalId 终端id
	 * @param ForwardBusinessType businessType 业务类型
	 * @return List<Jv230ForwardPO> JV230转发业务
	 */
	public List<Jv230ForwardPO> findByBundleIdInAndSourceTypeInAndUserIdAndTerminalIdAndBusinessType(Collection<String> bundleIds, Collection<ForwardSourceType> sourceTypes, String userId, Long terminalId, ForwardBusinessType businessType);

	
	/**
	 * 查询用户终端上屏的jv230设备<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月16日 上午9:37:04
	 * @param String userId 用户id
	 * @param Long terminalId 终端id
	 * @param ForwardBusinessType businessType 业务类型
	 * @return List<String> 设备id列表
	 */
	@Query(value = "select distinct f.bundleId from com.sumavision.tetris.bvc.business.group.forward.Jv230ForwardPO f where f.userId=? and f.terminalId=? and f.businessType=?")
	public List<String> findDistinctBundleIdByUserIdAndTerminalIdAndBusinessType(String userId, Long terminalId, ForwardBusinessType businessType);
}
