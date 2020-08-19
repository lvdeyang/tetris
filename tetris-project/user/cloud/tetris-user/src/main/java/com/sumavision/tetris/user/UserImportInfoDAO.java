package com.sumavision.tetris.user;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = UserImportInfoPO.class, idClass = Long.class)
public interface UserImportInfoDAO extends BaseDAO<UserImportInfoPO>{

	/**
	 * 查询企业用户导入信息<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月9日 下午12:03:23
	 * @param String companyId 企业id
	 * @return UserImportInfoPO 用户导入信息
	 */
	public UserImportInfoPO findByCompanyId(String companyId);
	
}
