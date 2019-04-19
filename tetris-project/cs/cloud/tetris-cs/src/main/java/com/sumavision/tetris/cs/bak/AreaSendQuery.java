package com.sumavision.tetris.cs.bak;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sumavision.tetris.cs.area.AreaQuery;
import com.sumavision.tetris.cs.area.AreaVO;

@Component
public class AreaSendQuery {
	@Autowired
	AreaSendDAO areaSendDao;
	
	@Autowired
	AreaQuery areaQuery;

	public void saveArea(Long channelId) throws Exception {
		List<AreaSendPO> previewList = areaSendDao.findByChannelId(channelId);
		if (previewList != null && previewList.size() > 0) {
			for(AreaSendPO item:previewList){
				areaSendDao.delete(item);
			}
//			areaSendDao.deleteInBatch(previewList);
		}
		List<AreaVO> list = areaQuery.getCheckArea(channelId);
		if (list != null && list.size() > 0) {
			List<AreaSendPO> saveList = new ArrayList<AreaSendPO>();
			for (int i = 0; i < list.size(); i++) {
				AreaSendPO areaSend = new AreaSendPO();
				AreaVO item = list.get(i);
				areaSend.setId(item.getId());
				areaSend.setName(item.getName());
				areaSend.setAreaId(item.getAreaId());
				areaSend.setChannelId(item.getChannelId());
				areaSend.setParentId(item.getParentId());
				saveList.add(areaSend);
			}
			areaSendDao.save(saveList);
		}
	}
	
	public List<AreaSendPO> getArea(Long channelId){
		List<AreaSendPO> returnList = areaSendDao.findByChannelId(channelId);
		return returnList;
	}
}
