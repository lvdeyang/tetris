package com.sumavision.tetris.omms.software.service.installation.history;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class InstallationPackageHistoryService {
	@Autowired
	private InstallationPackageHistoryDAO installationPackageHistoryDAO;
		
	/**
	 * 
	 * 删除历史安装包<br/>
	 * <b>作者:</b>lqxuhv<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年8月28日 上午9:36:10
	 * @param id 安装包id
	 */
	public void deletePackage(Long id) {
		InstallationPackageHistoryPO installationPackageHistoryPO = installationPackageHistoryDAO.findOne(id);
		if (installationPackageHistoryPO != null) {
			installationPackageHistoryDAO.delete(installationPackageHistoryPO);
		}
	}

}
