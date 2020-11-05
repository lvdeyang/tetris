package com.sumavision.tetris.bvc.model.terminal.screen;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.sumavision.tetris.bvc.model.terminal.channel.TerminalChannelDAO;
import com.sumavision.tetris.bvc.model.terminal.channel.TerminalChannelPO;
import com.sumavision.tetris.bvc.model.terminal.channel.exception.TerminalChannelNotFoundException;
import com.sumavision.tetris.bvc.model.terminal.screen.exception.TerminalScreenNotFoundException;

@Service
public class TerminalScreenService {

	@Autowired
	private TerminalScreenDAO terminalScreenDao;
	
	@Autowired
	private TerminalChannelDAO terminalChannelDao;
	
	/**
	 * 添加终端屏幕<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月23日 下午3:00:53
	 * @param Long terminalId 终端id
	 * @param JSONArray screenParams 屏幕参数[{name:"",screenPrimaryKey:"",terminalChannelId:""}]
	 * @return List<TerminalScreenVO> 屏幕列表
	 */
	@Transactional(rollbackFor = Exception.class)
	public List<TerminalScreenVO> add(
			Long terminalId, 
			String screenParams) throws Exception{
		List<TerminalScreenPO> entities = JSON.parseArray(screenParams, TerminalScreenPO.class);
		for(TerminalScreenPO entity:entities){
			entity.setTerminalId(terminalId);
			entity.setUpdateTime(new Date());
		}
		terminalScreenDao.save(entities);
		List<Long> terminalChannelIds = new ArrayList<Long>();
		for(TerminalScreenPO entity:entities){
			if(entity.getTerminalChannelId() != null) terminalChannelIds.add(entity.getTerminalChannelId());
		}
		List<TerminalChannelPO> channels = null;
		if(terminalChannelIds!=null && terminalChannelIds.size()>0){
			channels = terminalChannelDao.findAll(terminalChannelIds);
		}
		List<TerminalScreenVO> screens = new ArrayList<TerminalScreenVO>();
		for(TerminalScreenPO entity:entities){
			TerminalChannelPO targetChannel = null;
			if(channels!=null && channels.size()>0){
				for(TerminalChannelPO channel:channels){
					if(channel.getId().equals(entity.getTerminalChannelId())){
						targetChannel = channel;
						break;
					}
				}
			}
			screens.add(new TerminalScreenVO().set(entity).setTerminalChannelName(targetChannel==null?null:targetChannel.getName()));
		}
		return screens;
	}
	
	/**
	 * 修改名称<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月23日 下午4:07:49
	 * @param Long id 屏幕id
	 * @param String name 名称
	 * @return TerminalScreenVO 屏幕
	 */
	@Transactional(rollbackFor = Exception.class)
	public TerminalScreenVO editName(
			Long id,
			String name) throws Exception{
		TerminalScreenPO entity = terminalScreenDao.findOne(id);
		if(entity == null){
			throw new TerminalScreenNotFoundException(id);
		}
		entity.setName(name);
		terminalScreenDao.save(entity);
		TerminalScreenVO screen = new TerminalScreenVO().set(entity);
		if(entity.getTerminalChannelId() != null){
			TerminalChannelPO channel = terminalChannelDao.findOne(entity.getTerminalChannelId());
			if(channel != null){
				screen.setTerminalChannelName(channel.getName());
			}
		}
		return screen;
	}
	
	/**
	 * 修改屏幕和通道绑定关系<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月23日 下午4:11:26
	 * @param Long id 屏幕id
	 * @param Long terminalChannelId 通道id
	 * @return TerminalScreenVO 屏幕
	 */
	@Transactional(rollbackFor = Exception.class)
	public TerminalScreenVO editChannel(
			Long id,
			Long terminalChannelId) throws Exception{
		TerminalScreenPO entity = terminalScreenDao.findOne(id);
		if(entity == null){
			throw new TerminalScreenNotFoundException(id);
		}
		TerminalChannelPO channel = terminalChannelDao.findOne(terminalChannelId);
		if(channel == null){
			throw new TerminalChannelNotFoundException(terminalChannelId);
		}
		entity.setTerminalChannelId(channel.getId());
		terminalScreenDao.save(entity);
		return new TerminalScreenVO().set(entity).setTerminalChannelName(channel.getName());
	}
	
	/**
	 * 删除屏幕<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月22日 下午5:13:16
	 * @param Long id 屏幕id
	 */
	@Transactional(rollbackFor = Exception.class)
	public void delete(Long id) throws Exception{
		TerminalScreenPO screen = terminalScreenDao.findOne(id);
		if(screen != null){
			terminalScreenDao.delete(screen);
		}
	}
	
}
