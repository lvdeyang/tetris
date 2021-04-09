package com.sumavision.tetris.bvc.model.layout.forward;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sumavision.tetris.bvc.model.layout.LayoutPositionDAO;
import com.sumavision.tetris.bvc.model.layout.LayoutPositionPO;
import com.sumavision.tetris.bvc.model.layout.forward.exception.CombineTemplatePositionNotFoundException;
import com.sumavision.tetris.bvc.model.layout.forward.exception.LayoutForwardNotFoundException;
import com.sumavision.tetris.bvc.model.terminal.channel.TerminalChannelDAO;
import com.sumavision.tetris.bvc.model.terminal.channel.TerminalChannelPO;
import com.sumavision.tetris.bvc.model.terminal.channel.TerminalChannelVO;
import com.sumavision.tetris.bvc.model.terminal.physical.screen.TerminalPhysicalScreenChannelPermissionDAO;
import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;

@Component
public class LayoutForwardQuery {

	@Autowired
	private LayoutForwardDAO layoutForwardDao;
	
	@Autowired
	private LayoutPositionDAO layoutPositionDao;
	
	@Autowired
	private CombineTemplateDAO combineTemplateDao;
	
	@Autowired
	private CombineTemplatePositionDAO combineTemplatePositionDao;
	
	@Autowired
	private TerminalChannelDAO terminalChannelDao;
	
	@Autowired
	private TerminalPhysicalScreenChannelPermissionDAO terminalPhysicalScreenChannelPermissionDao;
	
	/**
	 * 根据虚拟源和物理屏幕查询转发配置信息<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年10月29日 下午3:27:36
	 * @param Long layoutId 虚拟源id
	 * @param Long terminalPhysicalScreenId 物理屏幕id
	 * @return List<LayoutForwardTreeNodeVO> 转发配置树
	 */
	public List<LayoutForwardTreeNodeVO> findByLayoutIdAndTerminalPhysicalScreenId(
			Long layoutId,
			Long terminalPhysicalScreenId) throws Exception{
		
		List<LayoutForwardPO> layoutForwardEntities = layoutForwardDao.findByLayoutIdAndTerminalPhysicalScreenId(layoutId, terminalPhysicalScreenId);
		if(layoutForwardEntities==null || layoutForwardEntities.size()<=0) return null;
		List<LayoutForwardTreeNodeVO> roots = new ArrayList<LayoutForwardTreeNodeVO>();
		List<Long> combineTemplateIds = new ArrayList<Long>();
		List<Long> terminalDecodeChannelIds = new ArrayList<Long>();
		for(LayoutForwardPO layoutForwardEntity:layoutForwardEntities){
			terminalDecodeChannelIds.add(layoutForwardEntity.getTerminalDecodeChannelId());
			if(LayoutForwardSourceType.COMBINE_TEMPLATE.equals(layoutForwardEntity.getSourceType())){
				combineTemplateIds.add(layoutForwardEntity.getSourceId());
			}
		}
		
		List<LayoutPositionPO> layoutPositionEntities = layoutPositionDao.findByLayoutIdOrderBySerialNum(layoutId);
		
		List<CombineTemplatePO> combineTemplateEntities = null;
		List<CombineTemplatePositionPO> combineTemplatePositionEntities = null;
		if(combineTemplateIds.size() > 0){
			combineTemplateEntities = combineTemplateDao.findAll(combineTemplateIds);
			combineTemplatePositionEntities = combineTemplatePositionDao.findByCombineTemplateIdIn(combineTemplateIds);
		}
		
		List<TerminalChannelPO> terminalChannels = null;
		if(terminalDecodeChannelIds.size() > 0){
			terminalChannels = terminalChannelDao.findAll(terminalDecodeChannelIds);
		}
		
		for(LayoutForwardPO layoutForwardEntity:layoutForwardEntities){
			
			LayoutForwardTreeNodeVO root = new LayoutForwardTreeNodeVO().setDecodeChannelNode(layoutForwardEntity);
			roots.add(root);
			if(layoutForwardEntity.getTerminalDecodeChannelId().intValue() == -1){
				root.setName("随机解码");
			}else {
				if(terminalChannels!=null && terminalChannels.size()>0){
					for(TerminalChannelPO terminalChannel:terminalChannels){
						if(terminalChannel.getId().equals(layoutForwardEntity.getTerminalDecodeChannelId())){
							root.setName(terminalChannel.getName());
							break;
						}
					}
				}
			}
			
			root.setChildren(new ArrayList<LayoutForwardTreeNodeVO>());
			
			if(layoutForwardEntity.getSourceType().equals(LayoutForwardSourceType.LAYOUT_POSITION)){
				
				LayoutForwardTreeNodeVO forwardNode = new LayoutForwardTreeNodeVO().setForwardNode(layoutForwardEntity);
				root.getChildren().add(forwardNode);
				if(layoutForwardEntity.getSourceId() != null){
					for(LayoutPositionPO layoutPositinEntity:layoutPositionEntities){
						if(Long.valueOf(layoutPositinEntity.getSerialNum()).equals(layoutForwardEntity.getSourceId())){
							forwardNode.getChildren().add(new LayoutForwardTreeNodeVO().setLayoutPositionNode(layoutForwardEntity, layoutPositinEntity));
							break;
						}
					}
				}
			}else if(layoutForwardEntity.getSourceType().equals(LayoutForwardSourceType.COMBINE_TEMPLATE)){
				
				if(combineTemplateEntities!=null && combineTemplateEntities.size()>0){
					
					CombineTemplatePO targetCombineTemplateEntity = null;
					for(CombineTemplatePO combineTemplateEntity:combineTemplateEntities){
						if(combineTemplateEntity.getId().equals(layoutForwardEntity.getSourceId())){
							targetCombineTemplateEntity = combineTemplateEntity;
							break;
						}
					}
					
					if(targetCombineTemplateEntity != null){
						LayoutForwardTreeNodeVO combineTemplateNode = new LayoutForwardTreeNodeVO().setCombineTemplateNode(layoutForwardEntity, targetCombineTemplateEntity);
						root.getChildren().add(combineTemplateNode);
						if(combineTemplatePositionEntities!=null && combineTemplatePositionEntities.size()>0){
							for(CombineTemplatePositionPO combineTemplatePositionEntity:combineTemplatePositionEntities){
								if(targetCombineTemplateEntity.getId().equals(combineTemplatePositionEntity.getCombineTemplateId())){
									for(LayoutPositionPO layoutPositionEntity:layoutPositionEntities){
										if(layoutPositionEntity.getSerialNum().equals(combineTemplatePositionEntity.getLayoutPositionSerialNum())){
											combineTemplateNode.getChildren().add(new LayoutForwardTreeNodeVO().setCombineTemplatePositionNode(layoutForwardEntity, combineTemplatePositionEntity, layoutPositionEntity));
											break;
										}
									}
								}
							}
						}
					}
					
				}
				
			}
			
		}
		
		return roots;
	}
	
	/**
	 * 查询解码<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年10月30日 下午1:20:39
	 * @param Long layoutId 虚拟源id
	 * @param Long terminalPhysicalScreenId 终端物理屏id
	 * @return mode String 模式：无模式，自选通道模式以及随机通道模式
	 * @return confirmChannels List<TerminalChannelVO> 可配置通道列表
	 * @return adaptableChannels List<TerminalChannelVO> adaptableChannels 随机通道列表
	 */
	public Map<String, Object> loadDecodes(
			Long layoutId,
			Long terminalPhysicalScreenId) throws Exception{
		String mode = "NONE";
		List<TerminalChannelVO> confirmChannels = null;
		List<TerminalChannelVO> adaptableChannels = null;
		List<LayoutForwardPO> forwardEntities =  layoutForwardDao.findByLayoutIdAndTerminalPhysicalScreenId(layoutId, terminalPhysicalScreenId);
		if(forwardEntities==null || forwardEntities.size()<=0){
			List<TerminalChannelPO> channelEntities = terminalChannelDao.findByTerminalPhysicalScreenId(terminalPhysicalScreenId);
			if(channelEntities!=null && channelEntities.size()>0){
				confirmChannels = TerminalChannelVO.getConverter(TerminalChannelVO.class).convert(channelEntities, TerminalChannelVO.class);
				adaptableChannels = new ArrayList<TerminalChannelVO>();
				for(TerminalChannelVO confirmChannel:confirmChannels){
					adaptableChannels.add(new TerminalChannelVO().setId(-1l).setName("随机解码"));
				}
			}
		}else{
			mode = "CONFIRM";
			List<Long> decodeChannelIds = new ArrayList<Long>();
			for(LayoutForwardPO forwardEntity:forwardEntities){
				if(forwardEntity.getTerminalDecodeChannelId().intValue() == -1l){
					mode = "ADAPTABLE";
					break;
				}else {
					decodeChannelIds.add(forwardEntity.getTerminalDecodeChannelId());
				}
			}
			if("CONFIRM".equals(mode)){
				List<TerminalChannelPO> channelEntities = terminalChannelDao.findByTerminalPhysicalScreenIdAndIdNotIn(terminalPhysicalScreenId, decodeChannelIds);
				confirmChannels = TerminalChannelVO.getConverter(TerminalChannelVO.class).convert(channelEntities, TerminalChannelVO.class);
			}else if("ADAPTABLE".equals(mode)){
				long total = terminalPhysicalScreenChannelPermissionDao.countByTerminalPhysicalScreenId(terminalPhysicalScreenId);
				adaptableChannels = new ArrayList<TerminalChannelVO>();
				for(int i=0; i<total-forwardEntities.size(); i++){
					adaptableChannels.add(new TerminalChannelVO().setId(-1l).setName("随机解码"));
				}
			}
			
		}
		return new HashMapWrapper<String, Object>().put("mode", mode)
												   .put("confirmChannels", confirmChannels)
												   .put("adaptableChannels", adaptableChannels)
												   .getMap();
	}
	
	/**
	 * 查询解码布局<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月9日 上午10:30:04
	 * @param Long layoutForwardId 虚拟源布局设置id
	 * @return String x 左偏移
	 * @return String y 上偏移
	 * @return String width 宽度
	 * @return String height 高度
	 * @return String zIndex 涂层
	 */
	public Map<String, String> queryPosition(Long layoutForwardId) throws Exception{
		LayoutForwardPO layoutForwardEntity =  layoutForwardDao.findOne(layoutForwardId);
		if(layoutForwardEntity == null){
			throw new LayoutForwardNotFoundException(layoutForwardId);
		}
		return new HashMapWrapper<String, String>().put("x", layoutForwardEntity.getX())
												   .put("y", layoutForwardEntity.getY())
												   .put("width", layoutForwardEntity.getWidth())
												   .put("height", layoutForwardEntity.getHeight())
												   .put("zIndex", layoutForwardEntity.getzIndex())
												   .getMap();
	}
	
	/**
	 * 查询合屏模板布局<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月9日 下午5:14:25
	 * @param Long combineTemplatePositionId 合屏模板布局id
	 * @return String x 左偏移
	 * @return String y 上偏移
	 * @return String width 宽度
	 * @return String height 高度
	 * @return String zIndex 涂层
	 */
	public Map<String, String> queryCombineTemplatePosition(Long combineTemplatePositionId) throws Exception{
		CombineTemplatePositionPO combineTemplatePositionEntity = combineTemplatePositionDao.findOne(combineTemplatePositionId);
		if(combineTemplatePositionEntity == null){
			throw new CombineTemplatePositionNotFoundException(combineTemplatePositionId);
		}
		return new HashMapWrapper<String, String>().put("x", combineTemplatePositionEntity.getX())
												   .put("y", combineTemplatePositionEntity.getY())
												   .put("width", combineTemplatePositionEntity.getWidth())
												   .put("height", combineTemplatePositionEntity.getHeight())
												   .put("zIndex", combineTemplatePositionEntity.getzIndex())
												   .getMap();
	}
	
	
}
