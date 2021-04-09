package com.sumavision.tetris.bvc.model.terminal;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = TerminalBundleChannelPO.class, idClass = Long.class)
public interface TerminalBundleChannelDAO extends BaseDAO<TerminalBundleChannelPO>{

	/**
	 * 查询终端设备下的通道列表<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月17日 下午4:20:29
	 * @param Long terminalBundleId 终端设备id
	 * @return List<TerminalBundleChannelPO> 通道列表
	 */
	public List<TerminalBundleChannelPO> findByTerminalBundleIdOrderByChannelIdAsc(Long terminalBundleId);
	
	/**
	 * 根据终端设备id和通道id查询通道<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月18日 下午2:28:14
	 * @param Long terminalBundleId 终端设备id
	 * @param String channelId 通道id
	 * @return List<TerminalBundleChannelPO> 通道列表
	 */
	public List<TerminalBundleChannelPO> findByTerminalBundleIdAndChannelId(Long terminalBundleId, String channelId);
	
	/**
	 * 根据终端设备id查询通道<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月11日 下午3:25:15
	 * @param terminalBundleId 终端设备id
	 * @return List<TerminalBundleChannelPO> 通道列表
	 */
	public List<TerminalBundleChannelPO> findByTerminalBundleId(Long terminalBundleId);
	
	/**
	 * 根据终端设备id查询通道<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年1月20日 下午2:37:40
	 * @param Collection<Long> terminalBundleIds 终端设备id列表
	 * @return List<TerminalBundleChannelPO> 通道列表
	 */
	public List<TerminalBundleChannelPO> findByTerminalBundleIdIn(Collection<Long> terminalBundleIds);
	
	/**
	 * 根据类型查询终端设备通道<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月19日 上午9:01:29
	 * @param Long terminalBundleId 终端设备id
	 * @param TerminalBundleChannelType type 通道类型
	 * @return List<TerminalBundleChannelPO> 通道列表
	 */
	public List<TerminalBundleChannelPO> findByTerminalBundleIdAndType(Long terminalBundleId, TerminalBundleChannelType type);
	
	/**
	 * 根据id查询终端设备通道br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月11日 下午2:53:22
	 * @param Collection<Long> id 终端设备通道id列表
	 * @return List<TerminalBundleChannelPO> 终端设备通道列表
	 */
	public List<TerminalBundleChannelPO> findByIdIn(Collection<Long> id);
	
	/**
	 * 根据终端通道查询终端设备通道带终端设备信息以及参数信息<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月11日 下午2:55:32
	 * @param Collection<Long> terminalChannelIds 终端通道id列表
	 * @return List<TerminalBundleChannelWithTerminalBundleAndParamsPermissionDTO> 带终端设备信息以及参数信息的终端设备通道列表
	 */
	@Query(value = "select new com.sumavision.tetris.bvc.model.terminal.TerminalBundleChannelWithTerminalBundleAndParamsPermissionDTO("
					+ "terminalBundleChannel.id, "
					+ "terminalBundleChannel.channelId, "
					+ "terminalBundleChannel.type, "
					+ "terminalBundle.id, "
					+ "terminalBundle.name, "
					+ "terminalBundle.bundleType, "
					+ "terminalBundle.type, "
					+ "terminalBundle.terminalId, "
					+ "paramsPermission.terminalChannelId, "
					+ "paramsPermission.channelParamsType, "
					+ "paramsPermission.id"
				+ ") "
				+ "from com.sumavision.tetris.bvc.model.terminal.channel.TerminalChannelBundleChannelPermissionPO paramsPermission,"
				+ "com.sumavision.tetris.bvc.model.terminal.TerminalBundleChannelPO terminalBundleChannel,"
				+ "com.sumavision.tetris.bvc.model.terminal.TerminalBundlePO terminalBundle "
				+ "where paramsPermission.terminalChannelId in ?1 "
				+ "and paramsPermission.terminalBundleChannelId = terminalBundleChannel.id "
				+ "and terminalBundleChannel.terminalBundleId = terminalBundle.id")
	public List<TerminalBundleChannelWithTerminalBundleAndParamsPermissionDTO> findByTerminalChannelIdIn(Collection<Long> terminalChannelIds);
	
	/**
	 * 根据终端和类型查询带终端设备信息的终端通道<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月12日 下午7:42:10
	 * @param Long terminalId 终端id
	 * @param TerminalBundleChannelType type 中固定那设备通道类型
	 * @return List<TerminalBundleChannelWithTerminalBundleAndParamsPermissionDTO> 带终端设备信息的终端通道列表
	 */
	@Query(value = "select new com.sumavision.tetris.bvc.model.terminal.TerminalBundleChannelWithTerminalBundleAndParamsPermissionDTO("
					+ "terminalBundleChannel.id, "
					+ "terminalBundleChannel.channelId, "
					+ "terminalBundleChannel.type, "
					+ "terminalBundle.id, "
					+ "terminalBundle.name, "
					+ "terminalBundle.bundleType, "
					+ "terminalBundle.type"
				+ ") "
				+ "from com.sumavision.tetris.bvc.model.terminal.TerminalBundlePO terminalBundle, "
				+ "com.sumavision.tetris.bvc.model.terminal.TerminalBundleChannelPO terminalBundleChannel "
				+ "where terminalBundle.terminalId=?1 "
				+ "and terminalBundle.id=terminalBundleChannel.terminalBundleId "
				+ "and terminalBundleChannel.type=?2")
	public List<TerminalBundleChannelWithTerminalBundleAndParamsPermissionDTO> findByTerminalIdAndType(Long terminalId, TerminalBundleChannelType type);
	
}
