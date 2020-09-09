package com.sumavision.tetris.bvc.model.terminal;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sumavision.tetris.bvc.model.terminal.exception.TerminalBundleChannelNotFoundException;
import com.sumavision.tetris.bvc.model.terminal.exception.TerminalBundleNotFoundException;

@Service
public class TerminalBundleChannelService {

	@Autowired
	private TerminalBundleDAO terminalBundleDao;
	
	@Autowired
	private TerminalBundleChannelDAO terminalBundleChannelDao;
	
	/**
	 * 添加终端设备通道<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月17日 下午5:33:21
	 * @param Long terminalBundleId 终端设备id
	 * @param String channelId 通道id
	 * @param String type 通道类型
	 * @return TerminalBundleChannelVO 终端设备通道
	 */
	public TerminalBundleChannelVO add(
			Long terminalBundleId, 
			String channelId, 
			String type) throws Exception{
		TerminalBundlePO bundle = terminalBundleDao.findOne(terminalBundleId);
		if(bundle == null){
			throw new TerminalBundleNotFoundException(terminalBundleId);
		}
		List<TerminalBundleChannelPO> existChannels = terminalBundleChannelDao.findByTerminalBundleIdAndChannelId(terminalBundleId, channelId);
		if(existChannels!=null && existChannels.size()>0){
			
		}
		TerminalBundleChannelPO entity = new TerminalBundleChannelPO();
		entity.setChannelId(channelId);
		entity.setType(TerminalBundleChannelType.valueOf(type));
		entity.setTerminalBundleId(bundle.getId());
		entity.setUpdateTime(new Date());
		terminalBundleChannelDao.save(entity);
		return new TerminalBundleChannelVO().set(entity);
	}
	
	/**
	 * 修改终端设备通道<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月17日 下午5:49:47
	 * @param Long id 通道id
	 * @param String channelId 通道id
	 * @param String type 通道类型
	 * @return TerminalBundleChannelVO 通道数据
	 */
	public TerminalBundleChannelVO edit(
			Long id,
			String channelId,
			String type) throws Exception{
		TerminalBundleChannelPO entity = terminalBundleChannelDao.findOne(id);
		if(entity == null){
			throw new TerminalBundleChannelNotFoundException(id);
		}
		entity.setChannelId(channelId);
		entity.setType(TerminalBundleChannelType.valueOf(type));
		terminalBundleChannelDao.save(entity);
		return new TerminalBundleChannelVO().set(entity);
	}
	
	/**
	 * 删除终端设备通道<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月18日 下午1:16:16
	 * @param Long id 通道id
	 */
	public void delete(Long id) throws Exception{
		TerminalBundleChannelPO channel = terminalBundleChannelDao.findOne(id);
		if(channel != null){
			terminalBundleChannelDao.delete(channel);
		}
	}
	
}
