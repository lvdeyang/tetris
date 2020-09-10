/**
 * 
 */
package com.sumavision.tetris.guide.control;

import org.bouncycastle.asn1.x509.qualified.TypeOfBiometricData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
	
	public SourcePO edit(
			Long id,
			String sourceType,
			String sourceName,
			String source,
			String previewOut) throws Exception{
		SourcePO sourcePO = sourceDAO.findOne(id);
		SourceType type = SourceType.fromName(sourceType);
		sourcePO.setSourceType(type);
		sourcePO.setSourceName(sourceName);
		sourcePO.setSource(source);
		sourcePO.setPreviewOut(previewOut);
		return sourceDAO.save(sourcePO);
	}
	
	public void delete(Long id){
		sourceDAO.delete(id);
	}
	
	public Object cut(
			Long id, 
			Long sourceNumber){
		return null;
	}
}

