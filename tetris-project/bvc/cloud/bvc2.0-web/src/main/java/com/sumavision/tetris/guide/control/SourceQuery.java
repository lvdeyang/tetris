/**
 * 
 */
package com.sumavision.tetris.guide.control;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.suma.venus.resource.dao.BundleDao;
import com.suma.venus.resource.pojo.BundlePO;


@Component
public class SourceQuery {
	
	@Autowired
	private SourceDAO sourceDao;
	
	@Autowired
	private BundleDao bundleDao;
	
	public List<SourceVO> query(Long guideId) throws Exception{
		
		List<SourcePO> list = sourceDao.findByGuideIdOrderBySourceNumber(guideId);
		return SourceVO.getConverter(SourceVO.class).convert(list, SourceVO.class);
	}
	
	public List<Map<String, String>> queryDevice() throws Exception{
		List<BundlePO> list = bundleDao.findByDeviceModel("5G");
		List<Map<String, String>> list2 = new ArrayList<Map<String,String>>();
		for (BundlePO bundlePO : list) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("bundleId",bundlePO.getBundleId());
			map.put("bundleName", bundlePO.getBundleName());
			list2.add(map);
		}	
		return list2;
	}
}
