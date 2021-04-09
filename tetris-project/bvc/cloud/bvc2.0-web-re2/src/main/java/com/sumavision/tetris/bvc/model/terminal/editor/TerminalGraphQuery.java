package com.sumavision.tetris.bvc.model.terminal.editor;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sumavision.tetris.bvc.model.terminal.TerminalBundleChannelDAO;
import com.sumavision.tetris.bvc.model.terminal.TerminalBundleChannelWithTerminalBundleAndParamsPermissionDTO;
import com.sumavision.tetris.bvc.model.terminal.TerminalDAO;
import com.sumavision.tetris.bvc.model.terminal.TerminalPO;
import com.sumavision.tetris.bvc.model.terminal.audio.TerminalAudioOutputDAO;
import com.sumavision.tetris.bvc.model.terminal.audio.TerminalAudioOutputPO;
import com.sumavision.tetris.bvc.model.terminal.channel.TerminalChannelDAO;
import com.sumavision.tetris.bvc.model.terminal.channel.TerminalChannelPO;
import com.sumavision.tetris.bvc.model.terminal.channel.TerminalChannelType;
import com.sumavision.tetris.bvc.model.terminal.channel.TerminalDecodeChannelWithTerminalAudioOutputPermissionDTO;
import com.sumavision.tetris.bvc.model.terminal.channel.TerminalDecodeChannelWithTerminalPhysicalScreenPermissionDTO;
import com.sumavision.tetris.bvc.model.terminal.channel.TerminalEncodeVideoChannelWithAudioChannelPermissionDTO;
import com.sumavision.tetris.bvc.model.terminal.exception.TerminalNotFoundException;
import com.sumavision.tetris.bvc.model.terminal.physical.screen.TerminalPhysicalScreenDAO;
import com.sumavision.tetris.bvc.model.terminal.physical.screen.TerminalPhysicalScreenPO;

@Component
public class TerminalGraphQuery {

	@Autowired
	private TerminalDAO terminalDao;
	
	@Autowired
	private TerminalPhysicalScreenDAO terminalPhysicalScreenDao;
	
	@Autowired
	private TerminalChannelDAO terminalChannelDao;
	
	@Autowired
	private TerminalAudioOutputDAO terminalAudioOutputDao;
	
	@Autowired
	private TerminalBundleChannelDAO terminalBundleChannelDao;
	
	/**
	 * 查询终端拓补图<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月11日 上午10:55:39
	 * @param Long terminalId 终端id
	 * @return TerminalGraphNodeVO 终端拓补图
	 */
	public TerminalGraphNodeVO load(Long terminalId) throws Exception{
		
		TerminalPO terminalEntity = terminalDao.findOne(terminalId);
		if(terminalEntity == null){
			throw new TerminalNotFoundException(terminalId);
		}
		
		//当前终端
		TerminalGraphNodeVO graph = new TerminalGraphNodeVO().set(terminalEntity);
		List<TerminalGraphNodeVO> roots = new ArrayList<TerminalGraphNodeVO>();
		graph.setChildren(roots);
		
		//查询屏幕
		List<TerminalPhysicalScreenPO> terminalPhysicalScreenEntities = terminalPhysicalScreenDao.findByTerminalIdOrderByName(terminalId);
		if(terminalPhysicalScreenEntities!=null && terminalPhysicalScreenEntities.size()>0){
			List<Long> terminalPhysicalScreenIds = new ArrayList<Long>();
			List<Long> terminalAudioOutputIds = new ArrayList<Long>();
			for(TerminalPhysicalScreenPO terminalPhysicalScreenEntity:terminalPhysicalScreenEntities){
				terminalPhysicalScreenIds.add(terminalPhysicalScreenEntity.getId());
				if(terminalPhysicalScreenEntity.getTerminalAudioOutputId() != null) 
					terminalAudioOutputIds.add(terminalPhysicalScreenEntity.getTerminalAudioOutputId());
				roots.add(new TerminalGraphNodeVO().set(terminalPhysicalScreenEntity));
			}
			
			//屏幕下的视频解码
			List<TerminalDecodeChannelWithTerminalPhysicalScreenPermissionDTO> videoDecodeChannelEntities = terminalChannelDao.findVideoDecodeChannelByTerminalPhysicalScreenIdIn(terminalPhysicalScreenIds);
			if(videoDecodeChannelEntities!=null && videoDecodeChannelEntities.size()>0){
				List<Long> videoDecodeChannelIds = new ArrayList<Long>();
				List<TerminalGraphNodeVO> videoDecodeChannelNodes = new ArrayList<TerminalGraphNodeVO>();
				for(TerminalDecodeChannelWithTerminalPhysicalScreenPermissionDTO videoDecodeChannelEntity:videoDecodeChannelEntities){
					videoDecodeChannelIds.add(videoDecodeChannelEntity.getId());
					TerminalGraphNodeVO videoDecodeChannelNode = new TerminalGraphNodeVO().set(videoDecodeChannelEntity);
					videoDecodeChannelNodes.add(videoDecodeChannelNode);
					for(TerminalGraphNodeVO root:roots){
						if(root.getType().equals(TerminalGraphNodeType.PHYSICAL_SCREEN.toString()) && 
								root.getId().endsWith(videoDecodeChannelEntity.getTerminalPhysicalScreenId().toString())){
							root.getChildren().add(videoDecodeChannelNode);
							break;
						}
					}
				}
				List<TerminalBundleChannelWithTerminalBundleAndParamsPermissionDTO> terminalBundleChannelEntities = terminalBundleChannelDao.findByTerminalChannelIdIn(videoDecodeChannelIds);
				if(terminalBundleChannelEntities!=null && terminalBundleChannelEntities.size()>0){
					for(TerminalBundleChannelWithTerminalBundleAndParamsPermissionDTO terminalBundleChannelEntity:terminalBundleChannelEntities){
						for(TerminalGraphNodeVO videoDecodeChannelNode:videoDecodeChannelNodes){
							if(videoDecodeChannelNode.getType().equals(TerminalGraphNodeType.VIDEO_DECODE_CHANNEL.toString()) && 
									videoDecodeChannelNode.getId().endsWith(terminalBundleChannelEntity.getTerminalChannelId().toString())){
								videoDecodeChannelNode.getChildren().add(new TerminalGraphNodeVO().set(terminalBundleChannelEntity));
								break;
							}
						}
					}
				}
			}
			
			if(terminalAudioOutputIds.size() > 0){
				//屏幕下的音频输出
				List<TerminalAudioOutputPO> terminalAudioOutputEntities = terminalAudioOutputDao.findAll(terminalAudioOutputIds);
				List<TerminalGraphNodeVO> terminalAudioOutputNodes = new ArrayList<TerminalGraphNodeVO>();
				for(TerminalAudioOutputPO terminalAudioOutputEntity:terminalAudioOutputEntities){
					TerminalGraphNodeVO terminalAudioOutputNode = new TerminalGraphNodeVO().set(terminalAudioOutputEntity);
					terminalAudioOutputNodes.add(terminalAudioOutputNode);
					for(TerminalGraphNodeVO root:roots){
						if(root.getType().equals(TerminalGraphNodeType.PHYSICAL_SCREEN.toString()) && 
								terminalAudioOutputEntity.getId().equals(root.getParams().getLong("terminalAudioOutputId"))){
							root.getChildren().add(terminalAudioOutputNode);
							break;
						}
					}
				}
				//音频输出关联的音频解码通道
				List<TerminalDecodeChannelWithTerminalAudioOutputPermissionDTO> audioDecodeChannelEntities = terminalChannelDao.findAudioDecodeChannelByTerminalAudioOutputIdIn(terminalAudioOutputIds);
				if(audioDecodeChannelEntities!=null && audioDecodeChannelEntities.size()>0){
					List<Long> audioDecodeChannelIds = new ArrayList<Long>();
					List<TerminalGraphNodeVO> audioDecodeChannelNodes = new ArrayList<TerminalGraphNodeVO>();
					for(TerminalDecodeChannelWithTerminalAudioOutputPermissionDTO audioDecodeChannelEntity:audioDecodeChannelEntities){
						audioDecodeChannelIds.add(audioDecodeChannelEntity.getId());
						TerminalGraphNodeVO audioDecodeChannelNode = new TerminalGraphNodeVO().set(audioDecodeChannelEntity);
						audioDecodeChannelNodes.add(audioDecodeChannelNode);
						for(TerminalGraphNodeVO terminalAudioOutputNode:terminalAudioOutputNodes){
							if(terminalAudioOutputNode.getType().equals(TerminalGraphNodeType.AUDIO_OUTPUT.toString()) &&
									terminalAudioOutputNode.getId().endsWith(audioDecodeChannelEntity.getTerminalAudioOutputId().toString())){
								terminalAudioOutputNode.getChildren().add(audioDecodeChannelNode);
								break;
							}
						}
					}
					List<TerminalBundleChannelWithTerminalBundleAndParamsPermissionDTO> terminalBundleChannelEntities = terminalBundleChannelDao.findByTerminalChannelIdIn(audioDecodeChannelIds);
					if(terminalBundleChannelEntities!=null && terminalBundleChannelEntities.size()>0){
						for(TerminalBundleChannelWithTerminalBundleAndParamsPermissionDTO terminalBundleChannelEntity:terminalBundleChannelEntities){
							for(TerminalGraphNodeVO audioDecodeChannelNode:audioDecodeChannelNodes){
								if(audioDecodeChannelNode.getType().equals(TerminalGraphNodeType.AUDIO_DECODE_CHANNEL.toString()) &&
										audioDecodeChannelNode.getId().endsWith(terminalBundleChannelEntity.getTerminalChannelId().toString())){
									audioDecodeChannelNode.getChildren().add(new TerminalGraphNodeVO().set(terminalBundleChannelEntity));
									break;
								}
							}
						}
					}
				}
			}
		}
		
		//查询视频编码
		List<TerminalChannelPO> terminalVideoEncodeChannelEntities = terminalChannelDao.findFreeVideoEncodeChannelByTerminalId(terminalId);
		if(terminalVideoEncodeChannelEntities!=null && terminalVideoEncodeChannelEntities.size()>0){
			List<Long> terminalVideoEncodeChannelIds = new ArrayList<Long>();
			List<TerminalGraphNodeVO> terminalVideoEncodeChannelNodes = new ArrayList<TerminalGraphNodeVO>();
			for(TerminalChannelPO terminalVideoEncodeChannelEntitiy:terminalVideoEncodeChannelEntities){
				terminalVideoEncodeChannelIds.add(terminalVideoEncodeChannelEntitiy.getId());
				terminalVideoEncodeChannelNodes.add(new TerminalGraphNodeVO().set(terminalVideoEncodeChannelEntitiy));
			}
			List<TerminalBundleChannelWithTerminalBundleAndParamsPermissionDTO> terminalBundleChannelEntities = terminalBundleChannelDao.findByTerminalChannelIdIn(terminalVideoEncodeChannelIds);
			if(terminalBundleChannelEntities!=null && terminalBundleChannelEntities.size()>0){
				for(TerminalBundleChannelWithTerminalBundleAndParamsPermissionDTO terminalBundleChannelEntity:terminalBundleChannelEntities){
					for(TerminalGraphNodeVO terminalVideoEncodeChannelNode:terminalVideoEncodeChannelNodes){
						if(terminalVideoEncodeChannelNode.getId().endsWith(terminalBundleChannelEntity.getTerminalChannelId().toString())){
							terminalVideoEncodeChannelNode.getChildren().add(new TerminalGraphNodeVO().set(terminalBundleChannelEntity));
							break;
						}
					}
				}
			}
			roots.addAll(terminalVideoEncodeChannelNodes);
		}
		
		//查询视频解码
		List<TerminalChannelPO> terminalVideoDecodeChannelEntities = terminalChannelDao.findFreeVideoDecodeChannelByTerminalId(terminalId);
		if(terminalVideoDecodeChannelEntities!=null && terminalVideoDecodeChannelEntities.size()>0){
			List<Long> terminalVideoDecodeChannelIds = new ArrayList<Long>();
			List<TerminalGraphNodeVO> terminalVideoDecodeChannelNodes = new ArrayList<TerminalGraphNodeVO>();
			for(TerminalChannelPO terminalVideoDecodeChannelEntity:terminalVideoDecodeChannelEntities){
				terminalVideoDecodeChannelIds.add(terminalVideoDecodeChannelEntity.getId());
				terminalVideoDecodeChannelNodes.add(new TerminalGraphNodeVO().set(terminalVideoDecodeChannelEntity));
			}
			List<TerminalBundleChannelWithTerminalBundleAndParamsPermissionDTO> terminalBundleChannelEntities = terminalBundleChannelDao.findByTerminalChannelIdIn(terminalVideoDecodeChannelIds);
			if(terminalBundleChannelEntities!=null && terminalBundleChannelEntities.size()>0){
				for(TerminalBundleChannelWithTerminalBundleAndParamsPermissionDTO terminalBundleChannelEntity:terminalBundleChannelEntities){
					for(TerminalGraphNodeVO terminalVideoDecodeChannelNode:terminalVideoDecodeChannelNodes){
						if(terminalVideoDecodeChannelNode.getId().endsWith(terminalBundleChannelEntity.getTerminalChannelId().toString())){
							terminalVideoDecodeChannelNode.getChildren().add(new TerminalGraphNodeVO().set(terminalBundleChannelEntity));
							break;
						}
					}
				}
			}
			roots.addAll(terminalVideoDecodeChannelNodes);
		}
		
		//查询音频编码
		List<TerminalChannelPO> terminalAudioEncodeChannelEntities = terminalChannelDao.findByTerminalIdAndType(terminalId, TerminalChannelType.AUDIO_ENCODE);
		if(terminalAudioEncodeChannelEntities!=null && terminalAudioEncodeChannelEntities.size()>0){
			List<Long> terminalAudioEncodeChannelIds = new ArrayList<Long>();
			List<TerminalGraphNodeVO> terminalAudioEncodeChannelNodes = new ArrayList<TerminalGraphNodeVO>();
			for(TerminalChannelPO terminalAudioEncodeChannelEntity:terminalAudioEncodeChannelEntities){
				terminalAudioEncodeChannelIds.add(terminalAudioEncodeChannelEntity.getId());
				terminalAudioEncodeChannelNodes.add(new TerminalGraphNodeVO().set(terminalAudioEncodeChannelEntity));
			}
			List<TerminalBundleChannelWithTerminalBundleAndParamsPermissionDTO> terminalBundleChannelEntities = terminalBundleChannelDao.findByTerminalChannelIdIn(terminalAudioEncodeChannelIds);
			if(terminalBundleChannelEntities!=null && terminalBundleChannelEntities.size()>0){
				for(TerminalBundleChannelWithTerminalBundleAndParamsPermissionDTO terminalBundleChannelEntity:terminalBundleChannelEntities){
					for(TerminalGraphNodeVO terminalAudioEncodeChannelNode:terminalAudioEncodeChannelNodes){
						if(terminalAudioEncodeChannelNode.getId().endsWith(terminalBundleChannelEntity.getTerminalChannelId().toString())){
							terminalAudioEncodeChannelNode.getChildren().add(new TerminalGraphNodeVO().set(terminalBundleChannelEntity));
							break;
						}
					}
				}
			}
			
			//关联视频编码
			List<TerminalEncodeVideoChannelWithAudioChannelPermissionDTO> terminalVideoEncodeChannelPermissions = terminalChannelDao.findVideoEncodeChannelByTerminalAudioEncodeChannelIdIn(terminalAudioEncodeChannelIds);
			if(terminalVideoEncodeChannelPermissions!=null && terminalVideoEncodeChannelPermissions.size()>0){
				List<Long> terminalVideoEncodeChannelIds = new ArrayList<Long>();
				List<TerminalGraphNodeVO> terminalVideoEncodeChannelNodes = new ArrayList<TerminalGraphNodeVO>();
				for(TerminalEncodeVideoChannelWithAudioChannelPermissionDTO terminalVideoEncodeChannelPermission:terminalVideoEncodeChannelPermissions){
					terminalVideoEncodeChannelIds.add(terminalVideoEncodeChannelPermission.getId());
					TerminalGraphNodeVO terminalVideoEncodeChannelNode = new TerminalGraphNodeVO().set(terminalVideoEncodeChannelPermission);
					terminalVideoEncodeChannelNodes.add(terminalVideoEncodeChannelNode);
					for(TerminalGraphNodeVO terminalAudioEncodeChannelNode:terminalAudioEncodeChannelNodes){
						if(terminalAudioEncodeChannelNode.getId().endsWith(terminalVideoEncodeChannelPermission.getTerminalAudioChannelId().toString())){
							terminalAudioEncodeChannelNode.getChildren().add(terminalVideoEncodeChannelNode);
							break;
						}
					}
				}
				List<TerminalBundleChannelWithTerminalBundleAndParamsPermissionDTO> terminalBundleVideoEncodeChannelEntities = terminalBundleChannelDao.findByTerminalChannelIdIn(terminalVideoEncodeChannelIds);
				if(terminalBundleVideoEncodeChannelEntities!=null && terminalBundleVideoEncodeChannelEntities.size()>0){
					for(TerminalBundleChannelWithTerminalBundleAndParamsPermissionDTO terminalBundleVideoEncodeChannelEntity:terminalBundleVideoEncodeChannelEntities){
						for(TerminalGraphNodeVO terminalVideoEncodeChannelNode:terminalVideoEncodeChannelNodes){
							if(terminalVideoEncodeChannelNode.getId().endsWith(terminalBundleVideoEncodeChannelEntity.getTerminalChannelId().toString())){
								terminalVideoEncodeChannelNode.getChildren().add(new TerminalGraphNodeVO().set(terminalBundleVideoEncodeChannelEntity));
								break;
							}
						}
					}
				}
			}
			roots.addAll(terminalAudioEncodeChannelNodes);
		}
		
		//查询音频解码
		List<TerminalChannelPO> terminalAudioDecodeChannelEntities = terminalChannelDao.findFreeAudioDecodeChannelByTerminalId(terminalId);
		if(terminalAudioDecodeChannelEntities!=null && terminalAudioDecodeChannelEntities.size()>0){
			List<Long> terminalAudioDecodeChannelIds = new ArrayList<Long>();
			List<TerminalGraphNodeVO> terminalAudioDecodeChannelNodes = new ArrayList<TerminalGraphNodeVO>();
			for(TerminalChannelPO terminalAudioDecodeChannelEntity:terminalAudioDecodeChannelEntities){
				terminalAudioDecodeChannelIds.add(terminalAudioDecodeChannelEntity.getId());
				terminalAudioDecodeChannelNodes.add(new TerminalGraphNodeVO().set(terminalAudioDecodeChannelEntity));
			}
			List<TerminalBundleChannelWithTerminalBundleAndParamsPermissionDTO> terminalBundleChannelEntities = terminalBundleChannelDao.findByTerminalChannelIdIn(terminalAudioDecodeChannelIds);
			if(terminalBundleChannelEntities!=null && terminalBundleChannelEntities.size()>0){
				for(TerminalBundleChannelWithTerminalBundleAndParamsPermissionDTO terminalBundleChannelEntity:terminalBundleChannelEntities){
					for(TerminalGraphNodeVO terminalAudioDecodeChannelNode:terminalAudioDecodeChannelNodes){
						if(terminalAudioDecodeChannelNode.getId().endsWith(terminalBundleChannelEntity.getTerminalChannelId().toString())){
							terminalAudioDecodeChannelNode.getChildren().add(new TerminalGraphNodeVO().set(terminalBundleChannelEntity));
							break;
						}
					}
				}
			}
			roots.addAll(terminalAudioDecodeChannelNodes);
		}
		
		//查询音频输出
		List<TerminalAudioOutputPO> terminalAudioOutputEntities = terminalAudioOutputDao.findFreeAudioOutputByTerminalId(terminalId);
		if(terminalAudioOutputEntities!=null && terminalAudioOutputEntities.size()>0){
			List<Long> terminalAudioOutputIds = new ArrayList<Long>();
			List<TerminalGraphNodeVO> terminalAudioOutputNodes = new ArrayList<TerminalGraphNodeVO>();
			for(TerminalAudioOutputPO terminalAudioOutputEntity:terminalAudioOutputEntities){
				terminalAudioOutputIds.add(terminalAudioOutputEntity.getId());
				terminalAudioOutputNodes.add(new TerminalGraphNodeVO().set(terminalAudioOutputEntity));
			}
			//音频输出关联的音频解码通道
			List<TerminalDecodeChannelWithTerminalAudioOutputPermissionDTO> audioDecodeChannelEntities = terminalChannelDao.findAudioDecodeChannelByTerminalAudioOutputIdIn(terminalAudioOutputIds);
			if(audioDecodeChannelEntities!=null && audioDecodeChannelEntities.size()>0){
				List<Long> audioDecodeChannelIds = new ArrayList<Long>();
				List<TerminalGraphNodeVO> audioDecodeChannelNodes = new ArrayList<TerminalGraphNodeVO>();
				for(TerminalDecodeChannelWithTerminalAudioOutputPermissionDTO audioDecodeChannelEntity:audioDecodeChannelEntities){
					audioDecodeChannelIds.add(audioDecodeChannelEntity.getId());
					TerminalGraphNodeVO audioDecodeChannelNode = new TerminalGraphNodeVO().set(audioDecodeChannelEntity);
					audioDecodeChannelNodes.add(audioDecodeChannelNode);
					for(TerminalGraphNodeVO terminalAudioOutputNode:terminalAudioOutputNodes){
						if(terminalAudioOutputNode.getType().equals(TerminalGraphNodeType.AUDIO_OUTPUT.toString()) &&
								terminalAudioOutputNode.getId().endsWith(audioDecodeChannelEntity.getTerminalAudioOutputId().toString())){
							terminalAudioOutputNode.getChildren().add(audioDecodeChannelNode);
							break;
						}
					}
				}
				List<TerminalBundleChannelWithTerminalBundleAndParamsPermissionDTO> terminalBundleChannelEntities = terminalBundleChannelDao.findByTerminalChannelIdIn(audioDecodeChannelIds);
				if(terminalBundleChannelEntities!=null && terminalBundleChannelEntities.size()>0){
					for(TerminalBundleChannelWithTerminalBundleAndParamsPermissionDTO terminalBundleChannelEntity:terminalBundleChannelEntities){
						for(TerminalGraphNodeVO audioDecodeChannelNode:audioDecodeChannelNodes){
							if(audioDecodeChannelNode.getType().equals(TerminalGraphNodeType.AUDIO_DECODE_CHANNEL.toString()) &&
									audioDecodeChannelNode.getId().endsWith(terminalBundleChannelEntity.getTerminalChannelId().toString())){
								audioDecodeChannelNode.getChildren().add(new TerminalGraphNodeVO().set(terminalBundleChannelEntity));
								break;
							}
						}
					}
				}
			}
			roots.addAll(terminalAudioOutputNodes);
		}
		
		return graph;
	}
	
}
