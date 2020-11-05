package com.sumavision.tetris.bvc.business.terminal.hall;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TerminalBundleConferenceHallPermissionQuery {

	@Autowired
	private TerminalBundleConferenceHallPermissionDAO terminalBundleConferenceHallPermissionDao;
	
	/**
	 * 查询会场设备绑定<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月13日 下午2:28:53
	 * @param Long conferenceHallId 会场id
	 * @param Long terminalBundleId 终端设备id
	 * @return TerminalBundleConferenceHallPermissionVO 设备绑定信息
	 */
	public TerminalBundleConferenceHallPermissionVO load(
			Long conferenceHallId, 
			Long terminalBundleId) throws Exception{
		TerminalBundleConferenceHallPermissionPO entity = terminalBundleConferenceHallPermissionDao.findByConferenceHallIdAndTerminalBundleId(conferenceHallId, terminalBundleId);
		if(entity != null){
			return new TerminalBundleConferenceHallPermissionVO().set(entity);
		}
		return null;
	}
	
}
