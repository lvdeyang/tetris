package com.sumavision.tetris.bvc.model.layout.forward;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.bvc.model.layout.LayoutPositionDAO;
import com.sumavision.tetris.bvc.model.layout.LayoutPositionPO;
import com.sumavision.tetris.bvc.model.layout.forward.exception.CombineTemplatePositionNotFoundException;
import com.sumavision.tetris.bvc.model.layout.forward.exception.LayoutForwardNotFoundException;
import com.sumavision.tetris.bvc.model.terminal.channel.TerminalChannelDAO;
import com.sumavision.tetris.bvc.model.terminal.channel.TerminalChannelPO;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;

@Service
public class LayoutForwardService {

	@Autowired
	private LayoutPositionDAO layoutPositionDao;
	
	@Autowired
	private LayoutForwardDAO layoutForwardDao;
	
	@Autowired
	private CombineTemplateDAO combineTemplateDao;
	
	@Autowired
	private CombineTemplatePositionDAO combineTemplatePositionDao;
	
	@Autowired
	private TerminalChannelDAO terminalChannelDao;
	
	/**
	 * 虚拟源终端显示设置中--添加解码操作<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月2日 下午1:41:26
	 * @param Long layoutId 虚拟源
	 * @param Long terminalId 终端id
	 * @param Long terminalPhysicalScreenId 终端物理屏id
	 * @param JSONArray decodeIds 解码通道id列表
	 * @return List<LayoutForwardTreeNodeVO> 转发配置树
	 */
	@Transactional(rollbackFor = Exception.class)
	public List<LayoutForwardTreeNodeVO> add(
			Long layoutId,
			Long terminalId,
			Long terminalPhysicalScreenId,
			String decodeIds) throws Exception{
		
		List<Long> parsedDecodeIds = JSON.parseArray(decodeIds, Long.class);
		List<LayoutForwardPO> forwardEntities = new ArrayList<LayoutForwardPO>();
		for(Long parsedDecodeId:parsedDecodeIds){
			LayoutForwardPO forwardEntity = new LayoutForwardPO();
			forwardEntity.setLayoutId(layoutId);
			forwardEntity.setSourceType(LayoutForwardSourceType.LAYOUT_POSITION);
			forwardEntity.setTerminalId(terminalId);
			forwardEntity.setTerminalPhysicalScreenId(terminalPhysicalScreenId);
			forwardEntity.setTerminalDecodeChannelId(parsedDecodeId);
			forwardEntity.setEnablePosition(true);
			forwardEntity.setX("0");
			forwardEntity.setY("0");
			forwardEntity.setWidth("10000");
			forwardEntity.setHeight("10000");
			forwardEntity.setzIndex("0");
			forwardEntities.add(forwardEntity);
		}
		layoutForwardDao.save(forwardEntities);
		
		List<LayoutForwardTreeNodeVO> roots = new ArrayList<LayoutForwardTreeNodeVO>();
		
		List<TerminalChannelPO> terminalChannels = null;
		if(parsedDecodeIds.size() > 0){
			terminalChannels = terminalChannelDao.findAll(parsedDecodeIds);
		}
		
		for(LayoutForwardPO forwardEntity:forwardEntities){
			LayoutForwardTreeNodeVO root = new LayoutForwardTreeNodeVO().setDecodeChannelNode(forwardEntity);
			roots.add(root);
			if(forwardEntity.getTerminalDecodeChannelId().intValue() == -1){
				root.setName("随机解码");
			}else {
				if(terminalChannels!=null && terminalChannels.size()>0){
					for(TerminalChannelPO terminalChannel:terminalChannels){
						if(terminalChannel.getId().equals(forwardEntity.getTerminalDecodeChannelId())){
							root.setName(terminalChannel.getName());
							break;
						}
					}
				}
			}
			
			LayoutForwardTreeNodeVO forwardNode = new LayoutForwardTreeNodeVO().setForwardNode(forwardEntity);
			root.getChildren().add(forwardNode);
		}
		
		return roots;
	}
	
	/**
	 * 修改源类型（转发，合屏）<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月4日 下午5:15:53
	 * @param Long layoutForwardId 转发配置id
	 * @param String treeNodeType 树节点类型
	 * @return LayoutForwardTreeNodeVO 源类型节点
	 */
	@Transactional(rollbackFor = Exception.class)
	public LayoutForwardTreeNodeVO changeSourceType(
			Long layoutForwardId,
			String treeNodeType) throws Exception{
		
		LayoutForwardPO layoutForwardEntity =  layoutForwardDao.findOne(layoutForwardId);
		if(layoutForwardEntity == null){
			throw new LayoutForwardNotFoundException(layoutForwardId);
		}
		
		LayoutForwardTreeNodeType layoutForwardTreeNodeType = LayoutForwardTreeNodeType.valueOf(treeNodeType);
		if(layoutForwardTreeNodeType.equals(LayoutForwardTreeNodeType.FORWARD)){
			CombineTemplatePO combineTemplate = combineTemplateDao.findOne(layoutForwardEntity.getSourceId());
			if(combineTemplate != null){
				combineTemplateDao.delete(combineTemplate);
			}
			List<CombineTemplatePositionPO> combineTemplatePositions = combineTemplatePositionDao.findByCombineTemplateIdIn(new ArrayListWrapper<Long>().add(layoutForwardEntity.getSourceId()).getList());
			if(combineTemplatePositions!=null && combineTemplatePositions.size()>0){
				combineTemplatePositionDao.deleteInBatch(combineTemplatePositions);
			}
			layoutForwardEntity.setSourceId(null);
			layoutForwardEntity.setSourceType(LayoutForwardSourceType.LAYOUT_POSITION);
			layoutForwardDao.save(layoutForwardEntity);
			return new LayoutForwardTreeNodeVO().setForwardNode(layoutForwardEntity);
		}else if(layoutForwardTreeNodeType.equals(LayoutForwardTreeNodeType.COMBINE_TEMPLATE)){
			CombineTemplatePO combineTemplate = new CombineTemplatePO();
			combineTemplateDao.save(combineTemplate);
			layoutForwardEntity.setSourceId(combineTemplate.getId());
			layoutForwardEntity.setSourceType(LayoutForwardSourceType.COMBINE_TEMPLATE);
			layoutForwardDao.save(layoutForwardEntity);
			return new LayoutForwardTreeNodeVO().setCombineTemplateNode(layoutForwardEntity, combineTemplate);
		}
		return null;
	}
	
	/**
	 * 修改是否启动解码布局<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月4日 下午4:26:19
	 * @param Long layoutForwardId 转发配置id
	 * @param Boolean enablePosition 是否启动解码布局
	 */
	@Transactional(rollbackFor = Exception.class)
	public void changeEnablePosition(
			Long layoutForwardId,
			Boolean enablePosition) throws Exception{
		
		LayoutForwardPO layoutForwardEntity =  layoutForwardDao.findOne(layoutForwardId);
		if(layoutForwardEntity == null){
			throw new LayoutForwardNotFoundException(layoutForwardId);
		}
		layoutForwardEntity.setEnablePosition(enablePosition);
		layoutForwardDao.save(layoutForwardEntity);
	}
	
	/**
	 * 保存布局<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月9日 上午10:17:10
	 * @param Long layoutForwardId 虚拟源布局设置id
	 * @param String x 左偏移
	 * @param String y 上偏移
	 * @param String width 宽度
	 * @param String height 高度
	 * @param String zIndex 涂层
	 */
	@Transactional(rollbackFor = Exception.class)
	public void savePosition(
			Long layoutForwardId,
			String x,
			String y,
			String width,
			String height,
			String zIndex) throws Exception{
		LayoutForwardPO layoutForwardEntity =  layoutForwardDao.findOne(layoutForwardId);
		if(layoutForwardEntity == null){
			throw new LayoutForwardNotFoundException(layoutForwardId);
		}
		layoutForwardEntity.setX(x);
		layoutForwardEntity.setY(y);
		layoutForwardEntity.setWidth(width);
		layoutForwardEntity.setHeight(height);
		layoutForwardEntity.setzIndex(zIndex);
		layoutForwardDao.save(layoutForwardEntity);
	}
	
	/**
	 * 保存合屏模板布局<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月9日 下午5:04:19
	 * @param Long combineTemplatePositionId 合屏模板布局id
	 * @param String x 左偏移
	 * @param String y 上偏移
	 * @param String width 宽度
	 * @param String height 高度
	 * @param String zIndex 涂层
	 */
	@Transactional(rollbackFor = Exception.class)
	public void saveCombineTemplatePosition(
			Long combineTemplatePositionId,
			String x,
			String y,
			String width,
			String height,
			String zIndex) throws Exception{
		CombineTemplatePositionPO combineTemplatePositionEntity = combineTemplatePositionDao.findOne(combineTemplatePositionId);
		if(combineTemplatePositionEntity == null){
			throw new CombineTemplatePositionNotFoundException(combineTemplatePositionId);
		}
		combineTemplatePositionEntity.setX(x);
		combineTemplatePositionEntity.setY(y);
		combineTemplatePositionEntity.setWidth(width);
		combineTemplatePositionEntity.setHeight(height);
		combineTemplatePositionEntity.setzIndex(zIndex);
		combineTemplatePositionDao.save(combineTemplatePositionEntity);
	}
	
	/**
	 * 添加转发虚拟源布局<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月9日 下午1:27:35
	 * @param Long layoutForwardId 虚拟源布局设置id
	 * @param Long layoutPositionSerialNum 虚拟源布局序号
	 * @return LayoutForwardTreeNodeVO 转发源节点
	 */
	@Transactional(rollbackFor = Exception.class)
	public LayoutForwardTreeNodeVO addForwardPosition(
			Long layoutForwardId,
			Long layoutPositionSerialNum) throws Exception{
		LayoutForwardPO layoutForwardEntity =  layoutForwardDao.findOne(layoutForwardId);
		if(layoutForwardEntity == null){
			throw new LayoutForwardNotFoundException(layoutForwardId);
		}
		layoutForwardEntity.setSourceId(layoutPositionSerialNum);
		layoutForwardDao.save(layoutForwardEntity);
		LayoutPositionPO layoutPositionEntity = layoutPositionDao.findTopByLayoutIdAndSerialNum(layoutForwardEntity.getLayoutId(), Integer.valueOf(layoutPositionSerialNum.toString()));
		return new LayoutForwardTreeNodeVO().setLayoutPositionNode(layoutForwardEntity, layoutPositionEntity);
	}
	
	/**
	 * 为和平模板添加布局<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月9日 下午3:47:42
	 * @param  Long layoutForwardId 虚拟源布局设置id
	 * @param String layoutPositions 虚拟源布局列表（vo列表）
	 * @return List<LayoutForwardTreeNodeVO> 合屏模板布局节点列表
	 */
	@Transactional(rollbackFor = Exception.class)
	public List<LayoutForwardTreeNodeVO> addCombinePositions(
			Long layoutForwardId,
			String layoutPositions) throws Exception{
		
		LayoutForwardPO layoutForwardEntity =  layoutForwardDao.findOne(layoutForwardId);
		if(layoutForwardEntity == null){
			throw new LayoutForwardNotFoundException(layoutForwardId);
		}
		
		List<LayoutPositionPO> layoutPositionEntities = layoutPositionDao.findByLayoutIdOrderBySerialNum(layoutForwardEntity.getLayoutId());
		
		List<CombineTemplatePositionPO> combineTemplatePositions = new ArrayList<CombineTemplatePositionPO>();
		JSONArray parsedLayoutPositions = JSON.parseArray(layoutPositions);
		for(int i=0; i<parsedLayoutPositions.size(); i++){
			JSONObject parsedLayoutPosition = parsedLayoutPositions.getJSONObject(i);
			CombineTemplatePositionPO combineTemplatePosition = new CombineTemplatePositionPO();
			combineTemplatePosition.setLayoutPositionId(parsedLayoutPosition.getLong("id"));
			combineTemplatePosition.setLayoutPositionSerialNum(parsedLayoutPosition.getInteger("serialNum"));
			combineTemplatePosition.setX(parsedLayoutPosition.getString("x"));
			combineTemplatePosition.setY(parsedLayoutPosition.getString("y"));
			combineTemplatePosition.setWidth(parsedLayoutPosition.getString("width"));
			combineTemplatePosition.setHeight(parsedLayoutPosition.getString("height"));
			combineTemplatePosition.setzIndex(parsedLayoutPosition.getString("zIndex"));
			combineTemplatePosition.setCombineTemplateId(layoutForwardEntity.getSourceId());
			combineTemplatePositions.add(combineTemplatePosition);
		}
		combineTemplatePositionDao.save(combineTemplatePositions);
		
		List<LayoutForwardTreeNodeVO> combineTemplatePositionNodes = new ArrayList<LayoutForwardTreeNodeVO>();
		for(CombineTemplatePositionPO combineTemplatePosition:combineTemplatePositions){
			for(LayoutPositionPO layoutPositionEntity:layoutPositionEntities){
				if(combineTemplatePosition.getLayoutPositionSerialNum().equals(layoutPositionEntity.getSerialNum())){
					combineTemplatePositionNodes.add(new LayoutForwardTreeNodeVO().setCombineTemplatePositionNode(layoutForwardEntity, combineTemplatePosition, layoutPositionEntity));
					break;
				}
			}
		}
		
		Collections.sort(combineTemplatePositionNodes, new LayoutForwardTreeNodeVO.CombineTemplatePositionNodeComparator());
		return combineTemplatePositionNodes;
	}
	
	/**
	 * 删除转发节点布局信息<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月9日 下午4:25:10
	 * @param Long layoutForwardId 虚拟源布局设置id
	 */
	@Transactional(rollbackFor = Exception.class)
	public void removeForwardPosition(Long layoutForwardId) throws Exception{
		LayoutForwardPO layoutForwardEntity =  layoutForwardDao.findOne(layoutForwardId);
		if(layoutForwardEntity == null){
			throw new LayoutForwardNotFoundException(layoutForwardId);
		}
		layoutForwardEntity.setSourceId(null);
	}
	
	/**
	 * 删除合屏节点布局信息<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月9日 下午4:30:06
	 * @param Long combineTemplatePositionId 合屏模板布局id
	 */
	@Transactional(rollbackFor = Exception.class)
	public void removeCombineTemplatePosition(Long combineTemplatePositionId) throws Exception{
		CombineTemplatePositionPO combineTemplatePositionEntity = combineTemplatePositionDao.findOne(combineTemplatePositionId);
		if(combineTemplatePositionEntity != null){
			combineTemplatePositionDao.delete(combineTemplatePositionEntity);
		}
	}
	
	/**
	 * 删除虚拟源转发<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年1月8日 下午2:16:55
	 * @param Long layoutForwardId 虚拟源转发id
	 */
	@Transactional(rollbackFor = Exception.class)
	public void remove(Long layoutForwardId) throws Exception{
		LayoutForwardPO layoutForwardEntity =  layoutForwardDao.findOne(layoutForwardId);
		if(layoutForwardEntity == null) return;
		if(LayoutForwardSourceType.COMBINE_TEMPLATE.equals(layoutForwardEntity.getSourceType())){
			CombineTemplatePO combineTemplateEntity = combineTemplateDao.findOne(layoutForwardEntity.getSourceId());
			if(combineTemplateEntity != null){
				List<CombineTemplatePositionPO> combineTemplatePositionEntities = combineTemplatePositionDao.findByCombineTemplateIdIn(new ArrayListWrapper<Long>().add(combineTemplateEntity.getId()).getList());
				if(combineTemplatePositionEntities!=null && combineTemplatePositionEntities.size()>0){
					combineTemplatePositionDao.deleteInBatch(combineTemplatePositionEntities);
				}
				combineTemplateDao.delete(combineTemplateEntity);
			}
		}
		layoutForwardDao.delete(layoutForwardEntity);
	}
	
}
