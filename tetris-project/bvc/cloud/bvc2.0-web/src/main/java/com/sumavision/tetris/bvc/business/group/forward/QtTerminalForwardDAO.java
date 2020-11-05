package com.sumavision.tetris.bvc.business.group.forward;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = QtTerminalForwardPO.class, idClass = Long.class)
public interface QtTerminalForwardDAO extends BaseDAO<QtTerminalForwardPO>{

	/**
	 * 查询终端非230设备混音转发<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月28日 下午2:34:45
	 * @param String userId 用户id
	 * @param Long terminalId 终端id
	 * @param ForwardBusinessType businessType 业务类型
	 * @param ForwardSourceType sourceType 源类型
	 * @return List<QtTerminalForwardPO> 转发列表
	 */
	public List<QtTerminalForwardPO> findByUserIdAndTerminalIdAndBusinessTypeAndSourceType(String userId, Long terminalId, ForwardBusinessType businessType, ForwardSourceType sourceType);
	
	/**
	 * 查询用户的非230设备上屏转发<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月28日 下午3:13:32
	 * @param String userId 用户id
	 * @param Long terminalId 终端id
	 * @param String bundleId 设备id
	 * @return List<QtTerminalForwardPO> 转发列表
	 */
	public List<QtTerminalForwardPO> findByUserIdAndTerminalIdAndBundleId(String userId, Long terminalId, String bundleId);
	
	/**
	 * 查询用户的非230设备上屏转发<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月28日 下午3:13:32
	 * @param String userId 用户id
	 * @param Long terminalId 终端id
	 * @param String bundleId 设备id
	 * @return List<QtTerminalForwardPO> 转发列表
	 */
	public List<QtTerminalForwardPO> findByUserIdAndTerminalIdAndBundleIdNot(String userId, Long terminalId, String bundleId);
	
	/**
	 * 查询终端非230设备上屏设备id<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月28日 下午4:51:07
	 * @param String userId 用户id
	 * @param Long terminalId 终端id
	 * @param ForwardBusinessType businessType 业务类型
	 * @return List<String> 设备id列表
	 */
	@Query("select distinct f.bundleId from com.sumavision.tetris.bvc.business.group.forward.QtTerminalForwardPO f where f.userId=? and f.terminalId=? and f.businessType=?")
	public List<String> findDistinctBundleIdByUserIdAndTerminalIdAndBusinessType(String userId, Long terminalId, ForwardBusinessType businessType);
	
	/**
	 * 查询qt终端非230设备上屏转发列表<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月28日 下午3:50:10
	 * @param String userId 用户id
	 * @param Long terminalId 终端id
	 * @param ForwardBusinessType businessType 业务类型
	 * @return List<QtTerminalForwardPO> 转发列表
	 */
	public List<QtTerminalForwardPO> findByUserIdAndTerminalIdAndBusinessType(String userId, Long terminalId, ForwardBusinessType businessType);
	
}
