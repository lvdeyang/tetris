package com.sumavision.tetris.omms.software.service.installation;

import java.util.List;
import java.util.Set;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = InstallationPackagePO.class, idClass = Long.class)
public interface InstallationPackageDAO extends BaseDAO<InstallationPackagePO>{

	/**
	 * 查询服务安装包<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年8月27日 下午1:33:41
	 * @param Long serviceTypeId 服务类型id
	 * @return List<InstallationPackagePO> 安装包列表
	 */
	public List<InstallationPackagePO> findByServiceTypeId(Long serviceTypeId);
	
	public List<InstallationPackagePO> findByIdIn(Set<Long> id);
	
}
