package com.sumavision.tetris.bvc.model.terminal.physical.screen;

import java.util.List;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = TerminalPhysicalScreenPO.class, idClass = Long.class)
public interface TerminalPhysicalScreenDAO extends BaseDAO<TerminalPhysicalScreenPO>{

	/**
	 * 查询终端下的物理屏<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年10月29日 下午1:40:18
	 * @param Long terminalId 终端id
	 * @return List<TerminalPhysicalScreenPO> 物理屏列表
	 */
	public List<TerminalPhysicalScreenPO> findByTerminalId(Long terminalId);
	
	/**
	 * 查询终端下的物理屏，并按照名称排序<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年10月29日 下午1:40:18
	 * @param Long terminalId 终端id
	 * @return List<TerminalPhysicalScreenPO> 物理屏列表
	 */
	public List<TerminalPhysicalScreenPO> findByTerminalIdOrderByName(Long terminalId);
	
	/**
	 * 根据布局序号查询终端下的屏幕<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月16日 上午11:48:23
	 * @param Long terminalId 终端id
	 * @param Integer col 布局列号
	 * @param Integer row 布局行号
	 * @return List<TerminalPhysicalScreenPO> 屏幕列表
	 */
	public List<TerminalPhysicalScreenPO> findByTerminalIdAndColAndRow(Long terminalId, Integer col, Integer row);
	
	/**
	 * 根据终端音频输出查询物理屏幕<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月16日 下午7:11:36
	 * @param Long terminalAudioOutputId 音频输出id
	 * @return List<TerminalPhysicalScreenPO> 物理屏幕列表
	 */
	public List<TerminalPhysicalScreenPO> findByTerminalAudioOutputId(Long terminalAudioOutputId);
	
}
