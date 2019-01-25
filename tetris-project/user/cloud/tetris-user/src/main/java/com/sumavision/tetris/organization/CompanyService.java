package com.sumavision.tetris.organization;

import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.sumavision.tetris.user.UserPO;

/**
 * 公司操作（主增删改）<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年1月24日 上午9:58:25
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class CompanyService {

	@Autowired
	private CompanyDAO companyDao;
	
	@Autowired
	private CompanyUserPermissionService companyUserPermissionService;
	
	/**
	 * 添加一个公司<br/>
	 * <b>作者:</b>Administrator<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月24日 上午10:09:02
	 * @param String companyName 公司名
	 * @param UserPO user 创建用户
	 */
	public CompanyVO add(String companyName, UserPO user) throws Exception{
		CompanyPO company = new CompanyPO();
		company.setName(companyName);
		company.setUserId(user.getId().toString());
		company.setUpdateTime(new Date());
		companyDao.save(company);
		//加关联
		companyUserPermissionService.add(company, user);
		return new CompanyVO().set(company);
	}
	
}
