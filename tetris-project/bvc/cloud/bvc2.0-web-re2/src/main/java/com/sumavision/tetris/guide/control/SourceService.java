/**
 * 
 */
package com.sumavision.tetris.guide.control;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sumavision.tetris.guide.business.GuidePlayService;

/**
 * 类型概述<br/>
 * <p>详细描述</p>
 * <b>作者:</b>Administrator<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年9月3日 下午7:21:20
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SourceService {

	@Autowired
	SourceDAO sourceDAO;
	
	@Autowired
	private GuidePlayService guidePlayService;
	
	public SourceVO edit(
			Long id,
			String sourceType,
			String sourceName,
			String source,
			String previewOut,
			Boolean isPreviewOut,
			String sourceProtocol) throws Exception{
		SourcePO sourcePO = sourceDAO.findOne(id);
		SourceType type = SourceType.fromName(sourceType);
		sourcePO.setSourceType(type);
		sourcePO.setSourceName(sourceName);
		sourcePO.setSource(source);
		sourcePO.setPreviewOut(previewOut);
		sourcePO.setIsPreviewOut(isPreviewOut);
		SourceProtocol protocol = SourceProtocol.fromName(sourceProtocol);
		sourcePO.setSourceProtocol(protocol);
		sourceDAO.save(sourcePO);
		return new SourceVO().set(sourcePO); 
	}
	
	public void delete(Long id){
		sourceDAO.delete(id);
	}
	
	public SourceVO cut(Long id,OutType type) throws Exception{
		
		SourcePO sourcePO = sourceDAO.findOne(id);
		List<SourcePO> list = sourceDAO.findByGuideIdOrderBySourceNumber(sourcePO.getGuideId());
		if(OutType.MONITOR.equals(type)){
			for (SourcePO sourcePO2 : list) {
				sourcePO2.setPvmCurrent(false);
			}
			sourcePO.setPvmCurrent(true);
		}else{
			for (SourcePO sourcePO2 : list) {
				sourcePO2.setCurrent(false);
			}
			sourcePO.setCurrent(true);
		}
		
		sourceDAO.save(list);
		sourceDAO.save(sourcePO);
		guidePlayService.exchange(sourcePO.getGuideId(), id, type);	
		return new SourceVO().set(sourcePO);
	}
}
