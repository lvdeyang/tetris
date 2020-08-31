package com.sumavision.tetris.omms.software.service.installation;

import java.util.List;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = PropertiesPO.class, idClass = Long.class)
public interface PropertiesDAO extends BaseDAO<PropertiesPO>{

	/**
	 * 查询安装包属性<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年8月28日 下午1:23:19
	 * @param Long installationPackageId 安装包id
	 * @return List<PropertiesPO> 属性列表
	 */
	public List<PropertiesPO> findByInstallationPackageId(Long installationPackageId);
	
}
