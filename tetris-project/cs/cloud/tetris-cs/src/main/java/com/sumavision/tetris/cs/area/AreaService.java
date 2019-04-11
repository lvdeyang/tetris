package com.sumavision.tetris.cs.area;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class AreaService {
	@Autowired
	AreaDAO areaDao;
	
	@Autowired
	AreaQuery areaQuery;
	
	public List<AreaVO> setCheckArea(Long channelId,List<AreaVO> list) throws Exception{
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
}
