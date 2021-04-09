package com.sumavision.tetris.bvc.model.layout;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sumavision.tetris.bvc.model.layout.forward.CombineTemplateDAO;
import com.sumavision.tetris.bvc.model.layout.forward.CombineTemplatePO;
import com.sumavision.tetris.bvc.model.layout.forward.CombineTemplatePositionDAO;
import com.sumavision.tetris.bvc.model.layout.forward.CombineTemplatePositionPO;
import com.sumavision.tetris.bvc.model.layout.forward.LayoutForwardDAO;
import com.sumavision.tetris.bvc.model.layout.forward.LayoutForwardPO;
import com.sumavision.tetris.bvc.model.layout.forward.LayoutForwardSourceType;

@Service
public class LayoutService {

	@Autowired
	private LayoutDAO layoutDao;
	
	@Autowired
	private LayoutPositionDAO layoutPositionDao;
	
	@Autowired
	private LayoutForwardDAO layoutForwardDao;
	
	@Autowired
	private CombineTemplateDAO combineTemplateDao;
	
	@Autowired
	private CombineTemplatePositionDAO combineTemplatePositionDao;
	
	/**
	 * 添加虚拟源<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年10月26日 下午4:12:29
	 * @param String name 虚拟源名称
	 * @return LayoutVO 虚拟源
	 */
	@Transactional(rollbackFor = Exception.class)
	public LayoutVO add(String name) throws Exception{
		LayoutPO layout = new LayoutPO();
		layout.setUpdateTime(new Date());
		layout.setName(name);
		layoutDao.save(layout);
		return new LayoutVO().set(layout);
	}
	
	/**
	 * 修改虚拟源名称<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年10月27日 上午9:02:49
	 * @param Long id 虚拟源id
	 * @param String name 虚拟源名称
	 * @return LayoutVO 虚拟源
	 */
	@Transactional(rollbackFor = Exception.class)
	public LayoutVO edit(
			Long id, 
			String name) throws Exception{
		LayoutPO layout = layoutDao.findOne(id);
		if(layout != null){
			layout.setName(name);
			layoutDao.save(layout);
			return new LayoutVO().set(layout);
		}
		return null;
	}
	
	/**
	 * 删除虚拟源<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年10月27日 上午10:13:17
	 * @param Long id 虚拟源id
	 */
	@Transactional(rollbackFor = Exception.class)
	public void remove(Long id) throws Exception{
		LayoutPO layout = layoutDao.findOne(id);
		if(layout != null){
			layoutDao.delete(layout);
		}
		List<LayoutPositionPO> positions = layoutPositionDao.findByLayoutIdOrderBySerialNum(id);
		if(positions!=null && positions.size()>0){
			layoutPositionDao.deleteInBatch(positions);
		}
		List<LayoutForwardPO> layoutForwardEntities = layoutForwardDao.findByLayoutId(id);
		if(layoutForwardEntities!=null && layoutForwardEntities.size()>0){
			Set<Long> combineTemplateIds = new HashSet<Long>();
			for(LayoutForwardPO layoutForwardEntity:layoutForwardEntities){
				if(LayoutForwardSourceType.COMBINE_TEMPLATE.equals(layoutForwardEntity.getSourceType())){
					combineTemplateIds.add(layoutForwardEntity.getSourceId());
				}
			}
			layoutForwardDao.deleteInBatch(layoutForwardEntities);
			List<CombineTemplatePO> combineTemplateEntities = combineTemplateDao.findAll(combineTemplateIds);
			if(combineTemplateEntities!=null && combineTemplateEntities.size()>0){
				combineTemplateDao.deleteInBatch(combineTemplateEntities);
			}
			List<CombineTemplatePositionPO> combineTemplatePositionEntities = combineTemplatePositionDao.findByCombineTemplateIdIn(combineTemplateIds);
			if(combineTemplatePositionEntities!=null && combineTemplatePositionEntities.size()>0){
				combineTemplatePositionDao.deleteInBatch(combineTemplatePositionEntities);
			}
		}
	}
	
	/**
	 * 更新虚拟源positionNum<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年2月19日 下午4:40:45
	 * @param layoutId
	 */
	public void updatePositionNum(Long layoutId){
		LayoutPO layout = layoutDao.findOne(layoutId);
		List<LayoutPositionPO> layoutPositions = layoutPositionDao.findByLayoutId(layoutId);
		layout.setPositionNum(Long.valueOf(layoutPositions.size()));
		layoutDao.save(layout);
	}
}
