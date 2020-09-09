package com.sumavision.tetris.bvc.model.terminal.channel;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sumavision.tetris.bvc.model.terminal.TerminalBundleChannelDAO;
import com.sumavision.tetris.bvc.model.terminal.TerminalBundleChannelPO;
import com.sumavision.tetris.bvc.model.terminal.TerminalBundleDAO;
import com.sumavision.tetris.bvc.model.terminal.TerminalBundlePO;
import com.sumavision.tetris.bvc.model.terminal.TerminalDAO;
import com.sumavision.tetris.bvc.model.terminal.TerminalPO;
import com.sumavision.tetris.bvc.model.terminal.channel.exception.TerminalChannelNotFoundException;
import com.sumavision.tetris.bvc.model.terminal.exception.TerminalBundleChannelNotFoundException;
import com.sumavision.tetris.bvc.model.terminal.exception.TerminalBundleNotFoundException;
import com.sumavision.tetris.bvc.model.terminal.exception.TerminalNotFoundException;

@Service
public class TerminalChannelService {

	@Autowired
	private TerminalDAO terminalDao;
	
	@Autowired
	private TerminalBundleDAO terminalBundleDao;
	
	@Autowired
	private TerminalBundleChannelDAO terminalBundleChannelDao;
	
	@Autowired
	private TerminalChannelDAO terminalChannelDao;
	
	/**
	 * 添加终端通道<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月18日 下午4:33:08
	 * @param String name 通道名称
	 * @param String type 通道类型
	 * @param Long terminalId 终端id
	 * @param Long terminalBundleId 终端设备id
	 * @param String realChannelId 终端设备通道id
	 * @return TerminalChannelVO 通道
	 */
	public TerminalChannelVO add(
			String name, 
			String type, 
			Long terminalId, 
			Long terminalBundleId, 
			String realChannelId) throws Exception{
		
		TerminalPO terminal = terminalDao.findOne(terminalId);
		if(terminal == null){
			throw new TerminalNotFoundException(terminalId);
		}
		TerminalBundlePO terminalBundle = terminalBundleDao.findOne(terminalBundleId);
		if(terminalBundle == null){
			throw new TerminalBundleNotFoundException(terminalBundleId);
		}
		List<TerminalBundleChannelPO> terminalBundleChannel = terminalBundleChannelDao.findByTerminalBundleIdAndChannelId(terminalBundleId, realChannelId);
		if(terminalBundleChannel == null){
			throw new TerminalBundleChannelNotFoundException(terminalBundleId, realChannelId);
		}
		
		TerminalChannelPO channel = new TerminalChannelPO();
		channel.setName(name);
		channel.setType(TerminalChannelType.valueOf(type));
		channel.setTerminalId(terminalId);
		channel.setTerminalBundleId(terminalBundleId);
		channel.setRealChannelId(realChannelId);
		channel.setUpdateTime(new Date());
		terminalChannelDao.save(channel);
		
		return new TerminalChannelVO().set(channel)
									  .setTerminalBundleName(terminalBundle.getName());
	}
	
	/**
	 * 修改终端通道<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月18日 下午4:33:08
	 * @param Long id 终端通道id
	 * @param String name 通道名称
	 * @param String type 通道类型
	 * @param Long terminalId 终端id
	 * @param Long terminalBundleId 终端设备id
	 * @param String realChannelId 终端设备通道id
	 * @return TerminalChannelVO 通道
	 */
	public TerminalChannelVO edit(
			Long id,
			String name,
			String type,
			Long terminalId,
			Long terminalBundleId,
			String realChannelId) throws Exception{
		TerminalPO terminal = terminalDao.findOne(terminalId);
		if(terminal == null){
			throw new TerminalNotFoundException(terminalId);
		}
		TerminalBundlePO terminalBundle = terminalBundleDao.findOne(terminalBundleId);
		if(terminalBundle == null){
			throw new TerminalBundleNotFoundException(terminalBundleId);
		}
		List<TerminalBundleChannelPO> terminalBundleChannel = terminalBundleChannelDao.findByTerminalBundleIdAndChannelId(terminalBundleId, realChannelId);
		if(terminalBundleChannel == null){
			throw new TerminalBundleChannelNotFoundException(terminalBundleId, realChannelId);
		}
		TerminalChannelPO channel = terminalChannelDao.findOne(id);
		if(channel == null){
			throw new TerminalChannelNotFoundException(id);
		}
		channel.setName(name);
		channel.setType(TerminalChannelType.valueOf(type));
		channel.setTerminalId(terminalId);
		channel.setTerminalBundleId(terminalBundleId);
		channel.setRealChannelId(realChannelId);
		channel.setUpdateTime(new Date());
		terminalChannelDao.save(channel);
		return new TerminalChannelVO().set(channel)
									  .setTerminalBundleName(terminalBundle.getName());
	}
	
	/**
	 * 删除终端通道<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月18日 下午4:42:03
	 * @param Long id 通道id
	 */
	public void delete(Long id) throws Exception{
		TerminalChannelPO channel = terminalChannelDao.findOne(id);
		if(channel != null){
			terminalChannelDao.delete(channel);
		}
	}
	
}
