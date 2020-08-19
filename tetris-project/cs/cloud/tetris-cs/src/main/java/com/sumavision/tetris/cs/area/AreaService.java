package com.sumavision.tetris.cs.area;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

@Service
@Transactional(rollbackFor = Exception.class)
public class AreaService {
	@Autowired
	AreaDAO areaDao;
	
	@Autowired
	AreaQuery areaQuery;
	
	/**
	 * 设置频道播发地区<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:06:57
	 * @param channelId 频道id
	 * @param list 地区列表
	 * @return List<AreaPO> 地区列表
	 */
	public List<AreaVO> setCheckArea(Long channelId,List<AreaVO> list, Boolean forceSet) throws Exception{
		areaQuery.checkAreaUsed(channelId, list, forceSet);
		List<AreaVO> returnList = new ArrayList<AreaVO>();
		List<AreaPO> saveList = new ArrayList<AreaPO>();
		
		List<AreaPO> previewList = areaDao.findByChannelId(channelId);
		areaDao.deleteInBatch(previewList);
		
		if(list != null && list.size() > 0){
			for(AreaVO item : list){
				AreaPO area = new AreaPO();
				area.setAreaId(item.getAreaId());
				area.setChannelId(item.getChannelId());
				area.setName(item.getName());
				area.setParentId(item.getParentId());
				saveList.add(area);
				returnList.add(new AreaVO().set(area));
			}
		}
		areaDao.save(saveList);
		return returnList;
	}
	
	/**
	 * 根据频道id删除地区信息<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年1月15日 上午10:04:19
	 * @param Long channelId 频道id
	 */
	public void removeByChannelId(Long channelId) throws Exception {
		List<AreaPO> areaPOs = areaDao.findByChannelId(channelId);
		
		if (areaPOs != null && !areaPOs.isEmpty()) areaDao.deleteInBatch(areaPOs);
	}
}
