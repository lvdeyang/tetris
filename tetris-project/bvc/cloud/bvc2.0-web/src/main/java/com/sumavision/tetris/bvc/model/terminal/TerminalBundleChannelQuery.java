package com.sumavision.tetris.bvc.model.terminal;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TerminalBundleChannelQuery {

	@Autowired
	private TerminalBundleChannelDAO terminalBundleChannelDao;
	
	/**
	 * 查询终端设备下的通道<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月17日 下午4:23:09
	 * @param Long terminalBundleId 终端设备id
	 * @return List<TerminalBundleChannelVO> 通道列表
	 */
	public List<TerminalBundleChannelVO> load(Long terminalBundleId) throws Exception{
		List<TerminalBundleChannelPO> entities = terminalBundleChannelDao.findByTerminalBundleIdOrderByChannelIdAsc(terminalBundleId);
		return TerminalBundleChannelVO.getConverter(TerminalBundleChannelVO.class).convert(entities, TerminalBundleChannelVO.class);
	}
	
	/**
	 * 根据终端设备和通道类型查询通道<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月19日 上午9:03:11
	 * @param Long terminalBundleId 终端设备id
	 * @param String type 通道类型
	 * @return List<TerminalBundleChannelVO> 通道列表
	 */
	public List<TerminalBundleChannelVO> loadByType(
			Long terminalBundleId, 
			String type) throws Exception{
		List<TerminalBundleChannelPO> entities = terminalBundleChannelDao.findByTerminalBundleIdAndType(terminalBundleId, TerminalBundleChannelType.valueOf(type));
		return TerminalBundleChannelVO.getConverter(TerminalBundleChannelVO.class).convert(entities, TerminalBundleChannelVO.class);
	}
	
}
