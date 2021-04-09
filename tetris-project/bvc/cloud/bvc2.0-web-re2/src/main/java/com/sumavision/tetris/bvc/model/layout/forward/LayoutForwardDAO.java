package com.sumavision.tetris.bvc.model.layout.forward;

import java.util.Collection;
import java.util.List;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = LayoutForwardPO.class, idClass = Long.class)
public interface LayoutForwardDAO extends BaseDAO<LayoutForwardPO>{

	/**
	 * 根据布局id和终端物理屏id查询转发配置列表<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年10月29日 下午2:29:14
	 * @param Long layoutId 布局id
	 * @param Long terminalPhysicalScreenId 终端物理屏id
	 * @return List<LayoutForwardPO> 转发配置列表
	 */
	public List<LayoutForwardPO> findByLayoutIdAndTerminalPhysicalScreenId(Long layoutId, Long terminalPhysicalScreenId);
	
	/**
	 * 查询虚拟源的转发配置<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月9日 下午5:29:24
	 * @param Long layoutId 虚拟源id
	 * @return List<LayoutForwardPO> 虚拟源转发配置列表
	 */
	public List<LayoutForwardPO> findByLayoutId(Long layoutId);
	
	/**
	 * 根据终端解码通道查询画面转发<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年1月20日 下午3:52:36
	 * @param Collection<Long> terminalDecodeChannelIds 终端解码通道id列表
	 * @return List<LayoutForwardPO> 画面转发列表
	 */
	public List<LayoutForwardPO> findByTerminalDecodeChannelIdIn(Collection<Long> terminalDecodeChannelIds);
	
}
