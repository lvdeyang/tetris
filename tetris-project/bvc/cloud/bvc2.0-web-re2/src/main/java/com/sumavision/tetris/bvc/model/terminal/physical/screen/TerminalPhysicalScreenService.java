package com.sumavision.tetris.bvc.model.terminal.physical.screen;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sumavision.tetris.bvc.model.terminal.audio.TerminalAudioOutputChannelPermissionDAO;
import com.sumavision.tetris.bvc.model.terminal.audio.TerminalAudioOutputChannelPermissionPO;
import com.sumavision.tetris.bvc.model.terminal.audio.TerminalAudioOutputDAO;
import com.sumavision.tetris.bvc.model.terminal.audio.TerminalAudioOutputPO;
import com.sumavision.tetris.bvc.model.terminal.channel.TerminalChannelBundleChannelPermissionDAO;
import com.sumavision.tetris.bvc.model.terminal.channel.TerminalChannelBundleChannelPermissionPO;
import com.sumavision.tetris.bvc.model.terminal.channel.TerminalChannelDAO;
import com.sumavision.tetris.bvc.model.terminal.channel.TerminalChannelPO;
import com.sumavision.tetris.bvc.model.terminal.editor.TerminalGraphNodeVO;


@Service
public class TerminalPhysicalScreenService {
	
	@Autowired
	private TerminalPhysicalScreenDAO terminalPhysicalScreenDao;
	
	@Autowired
	private TerminalPhysicalScreenChannelPermissionDAO terminalPhysicalScreenChannelPermissionDao;
	
	@Autowired
	private TerminalAudioOutputDAO terminalAudioOutputDao;
	
	@Autowired
	private TerminalAudioOutputChannelPermissionDAO terminalAudioOutputChannelPermissionDao;
	
	@Autowired
	private TerminalChannelDAO terminalChannelDao;
	
	@Autowired
	private TerminalChannelBundleChannelPermissionDAO terminalChannelBundleChannelPermissionDao;
	
	/**
	 * 添加物理屏幕<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年10月29日 下午3:43:01
	 * @param Long terminalId 终端id
	 * @param String name 名称
	 * @return TerminalGraphNodeVO 拓补图物理屏幕节点
	 */
	@Transactional(rollbackFor = Exception.class)
	public TerminalGraphNodeVO add(
			Long terminalId, 
			String name)throws Exception{
		TerminalPhysicalScreenPO terminalPhysicalScreen = new TerminalPhysicalScreenPO();
		terminalPhysicalScreen.setUpdateTime(new Date());
		terminalPhysicalScreen.setTerminalId(terminalId);
		terminalPhysicalScreen.setName(name);
		terminalPhysicalScreenDao.save(terminalPhysicalScreen);
		return new TerminalGraphNodeVO().set(terminalPhysicalScreen);
	}
	
	/**
	 * 修改物理屏幕<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年10月29日 下午3:43:01
	 * @param Long id 物理屏幕id
	 * @param String name 名称
	 */
	@Transactional(rollbackFor = Exception.class)
	public void edit(
			Long id,
			String name) throws Exception{
		TerminalPhysicalScreenPO terminalPhysicalScreen = terminalPhysicalScreenDao.findOne(id);
		terminalPhysicalScreen.setName(name);
		terminalPhysicalScreenDao.save(terminalPhysicalScreen);
	}
	
	/**
	 * 删除物理屏幕以及物理屏幕通道关联和物理屏幕音频输出关联<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月16日 下午4:22:15
	 * @param Long id 物理屏幕id
 	 * @param Boolean removeChildren 是否删除子节点<br/>
 	 * 子节点包括：
 	 *  1.物理屏幕视频解码通道关联
	 * 	2.视频解码通道
	 * 	3.视频解码与参数关联
	 * 	4.物理屏幕音频输出关联
	 * 	5.音频输出
	 * 	6.音频输出与音频解码通道关联
	 * 	7.音频解码通道
	 * 	8.音频解码通道与参数关联
	 */
	@Transactional(rollbackFor = Exception.class)
	public void remove(
			Long id,
			Boolean removeChildren) throws Exception{
		
		TerminalPhysicalScreenPO terminalPhysicalScreen = terminalPhysicalScreenDao.findOne(id);
		if(terminalPhysicalScreen != null){
			terminalPhysicalScreenDao.delete(terminalPhysicalScreen);
		}
		
		List<Long> terminalChannelIds = new ArrayList<Long>();
		List<TerminalPhysicalScreenChannelPermissionPO> terminalPhysicalScreenChannelPermissionEntities = terminalPhysicalScreenChannelPermissionDao.findByTerminalPhysicalScreenId(id);
		if(terminalPhysicalScreenChannelPermissionEntities!=null && terminalPhysicalScreenChannelPermissionEntities.size()>0){
			for(TerminalPhysicalScreenChannelPermissionPO terminalPhysicalScreenChannelPermissionEntity:terminalPhysicalScreenChannelPermissionEntities){
				terminalChannelIds.add(terminalPhysicalScreenChannelPermissionEntity.getTerminalChannelId());
			}
			terminalPhysicalScreenChannelPermissionDao.deleteInBatch(terminalPhysicalScreenChannelPermissionEntities);
		}
		
		if(removeChildren){
			if(terminalPhysicalScreen.getTerminalAudioOutputId() != null){
				TerminalAudioOutputPO terminalAudioOutputEntity = terminalAudioOutputDao.findOne(terminalPhysicalScreen.getTerminalAudioOutputId());
				if(terminalAudioOutputEntity != null){
					terminalAudioOutputDao.delete(terminalAudioOutputEntity);
					List<TerminalAudioOutputChannelPermissionPO> terminalAudioOutputChannelPermissionEntities = terminalAudioOutputChannelPermissionDao.findByTerminalAudioOutputId(terminalAudioOutputEntity.getId());
					if(terminalAudioOutputChannelPermissionEntities!=null && terminalAudioOutputChannelPermissionEntities.size()>0){
						for(TerminalAudioOutputChannelPermissionPO terminalAudioOutputChannelPermissionEntity:terminalAudioOutputChannelPermissionEntities){
							terminalChannelIds.add(terminalAudioOutputChannelPermissionEntity.getTerminalAudioChannelId());
						}
						terminalAudioOutputChannelPermissionDao.deleteInBatch(terminalAudioOutputChannelPermissionEntities);
					}
				}
			}
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
	
	/**
	 * 设置物理屏幕布局<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月16日 上午9:59:11
	 * @param Long terminalPhysicalScreenId 物理屏幕id
	 * @param Integer column 列号
	 * @param Integer row 行号
	 * @param String x 左偏移
	 * @param String y 上偏移
	 * @param String width 宽
	 * @param String height 高
	 * @return List<TerminalPhysicalScreenVO> 布局改变的物理屏幕列表
	 */
	@Transactional(rollbackFor = Exception.class)
	public List<TerminalPhysicalScreenVO> setPhysicalScreenLayout(
			Long terminalPhysicalScreenId,
			Integer column,
			Integer row,
			String x,
			String y,
			String width,
			String height) throws Exception{
		
		TerminalPhysicalScreenPO selectedEntity = terminalPhysicalScreenDao.findOne(terminalPhysicalScreenId);
		List<TerminalPhysicalScreenPO> existEntites = terminalPhysicalScreenDao.findByTerminalIdAndColAndRow(selectedEntity.getTerminalId(), column, row);
		List<TerminalPhysicalScreenPO> needSaveEntities = new ArrayList<TerminalPhysicalScreenPO>();
		needSaveEntities.add(selectedEntity);
		if(existEntites!=null && existEntites.size()>0){
			for(TerminalPhysicalScreenPO existEntity:existEntites){
				needSaveEntities.add(existEntity);
				existEntity.setCol(0);
				existEntity.setRow(0);
				existEntity.setX("0");
				existEntity.setY("0");
				existEntity.setWidth("0");
				existEntity.setHeight("0");
			}
		}
		
		selectedEntity.setCol(column);
		selectedEntity.setRow(row);
		selectedEntity.setX(x);
		selectedEntity.setY(y);
		selectedEntity.setWidth(width);
		selectedEntity.setHeight(height);
		terminalPhysicalScreenDao.save(needSaveEntities);
		
		return TerminalPhysicalScreenVO.getConverter(TerminalPhysicalScreenVO.class).convert(needSaveEntities, TerminalPhysicalScreenVO.class);
	}
	
}
