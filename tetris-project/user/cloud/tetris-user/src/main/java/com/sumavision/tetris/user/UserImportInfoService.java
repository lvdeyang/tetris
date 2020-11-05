package com.sumavision.tetris.user;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class UserImportInfoService {

	@Autowired
	private UserImportInfoDAO userImportInfoDao;
	
	/**
	 * 增加用户导入次数<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月9日 下午1:14:19
	 * @param String companyId 企业id
	 */
	public void addTimes(String companyId) throws Exception{
		UserImportInfoPO userImportInfo = userImportInfoDao.findByCompanyId(companyId);
		if(userImportInfo == null){
			userImportInfo = new UserImportInfoPO();
			userImportInfo.setCompanyId(companyId);
			userImportInfo.setTimes(0l);
			userImportInfo.setUpdateTime(new Date());
		}
		userImportInfo.setTimes(userImportInfo.getTimes() + 1);
		userImportInfoDao.save(userImportInfo);
	}
	
}
