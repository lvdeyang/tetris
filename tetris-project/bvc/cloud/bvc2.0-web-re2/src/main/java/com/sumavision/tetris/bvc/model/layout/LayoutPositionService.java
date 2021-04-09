package com.sumavision.tetris.bvc.model.layout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.bvc.model.layout.LayoutPositionVO.LayoutPositionComparator;

@Service
public class LayoutPositionService {

	@Autowired
	private LayoutPositionDAO layoutPositionDao;
	
	@Autowired
	private LayoutService layoutService;
	
	/**
	 * 添加布局<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年10月27日 上午10:08:34
	 * @param Long layoutId 虚拟源id
	 * @param Integer number 布局个数
	 * @param Integer beginIndex 起始布局序号
	 * @return List<LayoutPositionVO> 布局列表
	 */
	public List<LayoutPositionVO> add(
			Long layoutId,
			Integer number,
			Integer beginIndex) throws Exception{
		List<LayoutPositionPO> entities = new ArrayList<LayoutPositionPO>();
		for(int i=0; i<number; i++){
			LayoutPositionPO position = new LayoutPositionPO();
			position.setUpdateTime(new Date());
			position.setLayoutId(layoutId);
			position.setSerialNum(beginIndex + i);
			position.setzIndex("0");
			position.setX("3750");
			position.setY("3750");
			position.setWidth("2500");
			position.setHeight("2500");
			position.setType(LayoutPositionType.REMOTE);
			entities.add(position);
		}
		layoutPositionDao.save(entities);
		List<LayoutPositionVO> finalPositions = LayoutPositionVO.getConverter(LayoutPositionVO.class).convert(entities, LayoutPositionVO.class);
		Collections.sort(finalPositions, new LayoutPositionComparator());
		
		layoutService.updatePositionNum(layoutId);
		return finalPositions; 
	}
	
	/**
	 * 修改虚拟源布局<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年10月27日 下午2:06:56
	 * @param JSONString positions 修改后的布局列表
	 * @return List<LayoutPositionVO> 布局列表
	 */
	public List<LayoutPositionVO> edit(String positions) throws Exception{
		JSONArray positionArray = JSON.parseArray(positions);
		List<Long> ids = new ArrayList<Long>();
		for(int i=0; i<positionArray.size(); i++){
			ids.add(positionArray.getJSONObject(i).getLong("id"));
		}
		List<LayoutPositionPO> entities = layoutPositionDao.findAll(ids);
		if(entities!=null && entities.size()>0){
			for(LayoutPositionPO entity:entities){
				for(int i=0; i<positionArray.size(); i++){
					JSONObject positionObject = positionArray.getJSONObject(i);
					if(entity.getId().equals(positionObject.getLong("id"))){
						entity.setType(LayoutPositionType.valueOf(positionObject.getString("type")));
						entity.setX(positionObject.getString("x"));
						entity.setY(positionObject.getString("y"));
						entity.setWidth(positionObject.getString("width"));
						entity.setHeight(positionObject.getString("height"));
						entity.setzIndex(positionObject.getString("zIndex"));
						break;
					}
				}
			}
			layoutPositionDao.save(entities);
		}
		List<LayoutPositionVO> finalPositions = LayoutPositionVO.getConverter(LayoutPositionVO.class).convert(entities, LayoutPositionVO.class);
		Collections.sort(finalPositions, new LayoutPositionComparator());
		return finalPositions;
	}
	
	/**
	 * 删除虚拟源布局<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年10月27日 上午11:06:16
	 * @param Long id 虚拟源布局id
	 * @param List<LayoutPositionVO> 剩余布局列表（重新排序）
	 */
	public List<LayoutPositionVO> remove(Long id) throws Exception{
		LayoutPositionPO position = layoutPositionDao.findOne(id);
		Long layoutId = position.getLayoutId();
		List<LayoutPositionPO> otherPositions = null;
		if(position != null){
			layoutPositionDao.delete(position);
			otherPositions = layoutPositionDao.findByLayoutIdAndIdNotOrderBySerialNum(position.getLayoutId(), id);
			if(otherPositions!=null && otherPositions.size()>0){
				for(int i=0; i<otherPositions.size(); i++){
					otherPositions.get(i).setSerialNum(i+1);
				}
			}
			layoutPositionDao.save(otherPositions);
		}
		List<LayoutPositionVO> finalPositions = LayoutPositionVO.getConverter(LayoutPositionVO.class).convert(otherPositions, LayoutPositionVO.class);
		Collections.sort(finalPositions, new LayoutPositionComparator());
		
		layoutService.updatePositionNum(layoutId);
		return finalPositions;
	}
	
}
