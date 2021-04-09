package com.sumavision.tetris.bvc.model.terminal;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = TerminalBundlePO.class, idClass = Long.class)
public interface TerminalBundleDAO extends BaseDAO<TerminalBundlePO>{

	/**
	 * 查询终端设备<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月9日 下午5:58:47
	 * @param Long terminalId 终端id
	 * @return List<TerminalBundlePO> 设备列表
	 */
	public List<TerminalBundlePO> findByTerminalId(Long terminalId);
	
	/**
	 * 批量查询终端设备<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月9日 下午5:58:47
	 * @param Collection<Long> terminalIds 终端id
	 * @return List<TerminalBundlePO> 设备列表
	 */
	public List<TerminalBundlePO> findByTerminalIdIn(Collection<Long> terminalIds);
	
	/**
	 * 根据终端查询设备模板<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月17日 上午9:07:00
	 * @param Long terminalId 终端id
	 * @return List<TerminalBundlePO> 设备模板列表
	 */
	public Page<TerminalBundlePO> findByTerminalIdOrderByBundleTypeAscNameAsc(Long terminalId, Pageable page);
	
	/**
	 * 统计终端绑定的设备模板数量<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月17日 上午9:20:02
	 * @param Long terminalId 终端id
	 * @return Long 模板数量
	 */
	public Long countByTerminalId(Long terminalId);
	
	/**
	 * 根据终端和类型查询设备<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月19日 上午8:45:53
	 * @param Long terminalId 终端id
	 * @param Collection<TerminalBundleType> types 设备类型
	 * @return List<TerminalBundlePO> 设备类型
	 */
	public List<TerminalBundlePO> findByTerminalIdAndTypeIn(Long terminalId, Collection<TerminalBundleType> types);
	
	/**
	 * 根据终端和类型查询设备<br/>
	 * <b>作者:</b>lixin<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年8月18日 下午2:50:34
	 * @param terminalId 终端id
	 * @param bundleType 设备类型
	 * @return TerminalBundlePO 设备
	 */
	public List<TerminalBundlePO> findByTerminalIdAndBundleType(Long terminalId , String bundleType);
	
	public List<TerminalBundlePO> findByIdIn(Collection<Long> ids);
}
