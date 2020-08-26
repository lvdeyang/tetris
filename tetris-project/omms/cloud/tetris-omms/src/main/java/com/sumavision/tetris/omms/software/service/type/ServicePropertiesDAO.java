package com.sumavision.tetris.omms.software.service.type;

import java.util.Collection;
import java.util.List;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = ServicePropertiesPO.class, idClass = Long.class)
public interface ServicePropertiesDAO extends BaseDAO<ServicePropertiesPO>{

	/**
	 * 查询服务属性<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年8月25日 下午6:16:34
	 * @param Collection<Long> serviceTypeIds 服务id列表
	 * @return List<ServicePropertiesPO> 属性列表
	 */
	public List<ServicePropertiesPO> findByServiceTypeIdIn(Collection<Long> serviceTypeIds);
	
}
