package com.sumavision.tetris.bvc.model.terminal.audio;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sumavision.tetris.bvc.model.terminal.channel.TerminalChannelBundleChannelPermissionDAO;
import com.sumavision.tetris.bvc.model.terminal.channel.TerminalChannelBundleChannelPermissionPO;
import com.sumavision.tetris.bvc.model.terminal.channel.TerminalChannelDAO;
import com.sumavision.tetris.bvc.model.terminal.channel.TerminalChannelPO;
import com.sumavision.tetris.bvc.model.terminal.editor.TerminalGraphNodeVO;
import com.sumavision.tetris.bvc.model.terminal.physical.screen.TerminalPhysicalScreenDAO;
import com.sumavision.tetris.bvc.model.terminal.physical.screen.TerminalPhysicalScreenPO;

@Service
public class TerminalAudioOutputService {

	@Autowired
	private TerminalAudioOutputDAO terminalAudioOutputDao;
	
	@Autowired
	private TerminalPhysicalScreenDAO terminalPhysicalScreenDao;
	
	@Autowired
	private TerminalAudioOutputChannelPermissionDAO terminalAudioOutputChannelPermissionDao;
	
	@Autowired
	private TerminalChannelDAO terminalChannelDao;
	
	@Autowired
	private TerminalChannelBundleChannelPermissionDAO terminalChannelBundleChannelPermissionDao;
	
	/**
	 * 添加音频输出<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月15日 下午12:32:52
	 * @param Long terminalId 终端id
	 * @param String name 名称
	 * @param Long terminalPhysicalScreenId 关联终端物理屏幕id
	 * @return TerminalGraphNodeVO 终端拓补图
	 */
	@Transactional(rollbackFor = Exception.class)
	public TerminalGraphNodeVO add(
			Long terminalId,
			String name,
			Long terminalPhysicalScreenId) throws Exception{
		
		TerminalAudioOutputPO terminalAudioOutputEntity = new TerminalAudioOutputPO();
		terminalAudioOutputEntity.setUpdateTime(new Date());
		terminalAudioOutputEntity.setName(name);
		terminalAudioOutputEntity.setTerminalId(terminalId);
		terminalAudioOutputDao.save(terminalAudioOutputEntity);
		
		if(terminalPhysicalScreenId != null){
			TerminalPhysicalScreenPO terminalPhysicalScreenEntity = terminalPhysicalScreenDao.findOne(terminalPhysicalScreenId);
			terminalPhysicalScreenEntity.setTerminalAudioOutputId(terminalAudioOutputEntity.getId());
			terminalPhysicalScreenDao.save(terminalPhysicalScreenEntity);
		}
		
		return new TerminalGraphNodeVO().set(terminalAudioOutputEntity);
	}
	
	/**
	 * 修改音频输出名称<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月16日 下午6:58:54
	 * @param Long id 音频输出id
	 * @param String name 名称
	 */
	@Transactional(rollbackFor = Exception.class)
	public void edit(
			Long id,
			String name) throws Exception{
		
		TerminalAudioOutputPO entity = terminalAudioOutputDao.findOne(id);
		entity.setName(name);
		terminalAudioOutputDao.save(entity);
		
	}
	
	/**
	 * 修改音频输出关联的物理屏幕<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月16日 下午8:01:58
	 * @param Long id 音频输出id
	 * @param Long physicalScreenId 物理屏幕id
	 */
	public void move(
			Long id,
			Long physicalScreenId) throws Exception{
		
		List<TerminalPhysicalScreenPO> terminalPhysicalScreenEntities = terminalPhysicalScreenDao.findByTerminalAudioOutputId(id);
		if(terminalPhysicalScreenEntities!=null && terminalPhysicalScreenEntities.size()>0){
			for(TerminalPhysicalScreenPO terminalPhysicalScreenEntity:terminalPhysicalScreenEntities){
				terminalPhysicalScreenEntity.setTerminalAudioOutputId(null);
			}
			terminalPhysicalScreenDao.save(terminalPhysicalScreenEntities);
		}
		
		if(physicalScreenId != null){
			TerminalPhysicalScreenPO targetPhysicalScreenEntity = terminalPhysicalScreenDao.findOne(physicalScreenId);
			targetPhysicalScreenEntity.setTerminalAudioOutputId(id);
			terminalPhysicalScreenDao.save(targetPhysicalScreenEntity);
		}
		
	}
	
	/**
	 * 删除音频输出<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月16日 下午7:03:25
	 * @param Long id 音频输出id
	 * @param Boolean removeChildren 是否删除子节点
	 *  1.音频输出
	 *  2.音频输出屏幕关联
	 * 	2.音频输出与音频解码通道关联
	 * 子节点包括：
	 * 	3.音频解码通道
	 * 	4.音频解码通道与参数关联
	 */
	@Transactional(rollbackFor = Exception.class)
	public void remove(
			Long id,
			Boolean removeChildren) throws Exception{
		
		TerminalAudioOutputPO terminalAudioOutputEntity = terminalAudioOutputDao.findOne(id);
		if(terminalAudioOutputEntity != null){
			terminalAudioOutputDao.delete(terminalAudioOutputEntity);
		}
		
		List<TerminalPhysicalScreenPO> terminalPhysicalScreenEntities = terminalPhysicalScreenDao.findByTerminalAudioOutputId(terminalAudioOutputEntity.getId());
		if(terminalPhysicalScreenEntities!=null && terminalPhysicalScreenEntities.size()>0){
			for(TerminalPhysicalScreenPO terminalPhysicalScreenEntity:terminalPhysicalScreenEntities){
				terminalPhysicalScreenEntity.setTerminalAudioOutputId(null);
			}
			terminalPhysicalScreenDao.save(terminalPhysicalScreenEntities);
		}
		
		List<Long> terminalChannelIds = new ArrayList<Long>();
		List<TerminalAudioOutputChannelPermissionPO> terminalAudioOutputChannelPermissionEntities = terminalAudioOutputChannelPermissionDao.findByTerminalAudioOutputId(terminalAudioOutputEntity.getId());
		if(terminalAudioOutputChannelPermissionEntities!=null && terminalAudioOutputChannelPermissionEntities.size()>0){
			for(TerminalAudioOutputChannelPermissionPO terminalAudioOutputChannelPermissionEntity:terminalAudioOutputChannelPermissionEntities){
				terminalChannelIds.add(terminalAudioOutputChannelPermissionEntity.getTerminalAudioChannelId());
			}
			terminalAudioOutputChannelPermissionDao.deleteInBatch(terminalAudioOutputChannelPermissionEntities);
		}
		
		if(removeChildren){
			if(terminalChannelIds.size() > 0){
				List<TerminalChannelPO> terminalChannelEntities = terminalChannelDao.findAll(terminalChannelIds);
				if(terminalChannelEntities!=null && terminalChannelEntities.size()>0){
					terminalChannelDao.deleteInBatch(terminalChannelEntities);
				}
				List<TerminalChannelBundleChannelPermissionPO> terminalChannelBundleChannelPermissionEntities = terminalChannelBundleChannelPermissionDao.findByTerminalChannelIdIn(terminalChannelIds);
				if(terminalChannelBundleChannelPermissionEntities!=null && terminalChannelBundleChannelPermissionEntities.size()>0){
					terminalChannelBundleChannelPermissionDao.deleteInBatch(terminalChannelBundleChannelPermissionEntities);
				}
			}
		}
		
	}
	
}
