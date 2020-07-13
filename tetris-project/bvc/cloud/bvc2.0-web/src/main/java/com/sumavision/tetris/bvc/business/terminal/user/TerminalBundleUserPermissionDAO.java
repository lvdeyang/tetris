package com.sumavision.tetris.bvc.business.terminal.user;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = TerminalBundleUserPermissionPO.class, idClass = Long.class)
public interface TerminalBundleUserPermissionDAO extends BaseDAO<TerminalBundleUserPermissionPO>{

	/**
	 * 查询用户为终端设备绑定的真实设备<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月10日 上午11:04:21
	 * @param String userId 用户id
	 * @param Long terminalId 终端id
	 * @param Long terminalBundleId 终端设备id
	 * @return TerminalBundleUserPermissionPO 真实设备信息
	 */
	public TerminalBundleUserPermissionPO findByUserIdAndTerminalIdAndTerminalBundleId(String userId, Long terminalId, Long terminalBundleId);
	
}
