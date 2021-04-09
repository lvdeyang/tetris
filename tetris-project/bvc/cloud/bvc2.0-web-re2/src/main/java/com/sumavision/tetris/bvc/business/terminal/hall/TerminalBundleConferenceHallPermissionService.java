package com.sumavision.tetris.bvc.business.terminal.hall;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sumavision.tetris.bvc.business.terminal.hall.exception.TerminalBundleConferenceHallPermissionNotFoundException;

@Service
public class TerminalBundleConferenceHallPermissionService {

	@Autowired
	private TerminalBundleConferenceHallPermissionDAO terminalBundleConferenceHallPermissionDao;
	
	/**
	 * 添加会场设备绑定<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月13日 下午2:33:43
	 * @param Long conferenceHallId 会场id
	 * @param Long terminalBundleId 终端设备id
	 * @param String bundleType 设备类型
	 * @param String bundleId 设备id
	 * @param String bundleName 设备名称
	 * @return TerminalBundleConferenceHallPermissionVO 会场设备绑定
	 */
	public TerminalBundleConferenceHallPermissionVO add(
			Long conferenceHallId, 
			Long terminalBundleId,
			String bundleType,
			String bundleId,
			String bundleName) throws Exception{
		TerminalBundleConferenceHallPermissionPO entity = new TerminalBundleConferenceHallPermissionPO();
		entity.setConferenceHallId(conferenceHallId);
		entity.setTerminalBundleId(terminalBundleId);
		entity.setBundleType(bundleType);
		entity.setBundleId(bundleId);
		entity.setBundleName(bundleName);
		entity.setUpdateTime(new Date());
		terminalBundleConferenceHallPermissionDao.save(entity);
		return new TerminalBundleConferenceHallPermissionVO().set(entity);
	}
	
	/**
	 * 修改会场设备绑定<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月13日 下午2:33:43
	 * @param Long id 会场设备绑定id
	 * @param String bundleType 设备类型
	 * @param String bundleId 设备id
	 * @param String bundleName 设备名称
	 * @return TerminalBundleConferenceHallPermissionVO 会场设备绑定
	 */
	public TerminalBundleConferenceHallPermissionVO edit(
			Long id,
			String bundleType,
			String bundleId,
			String bundleName) throws Exception{
		TerminalBundleConferenceHallPermissionPO entity = terminalBundleConferenceHallPermissionDao.findOne(id);
		if(entity == null){
			throw new TerminalBundleConferenceHallPermissionNotFoundException(id);
		}
		entity.setBundleType(bundleType);
		entity.setBundleId(bundleId);
		entity.setBundleName(bundleName);
		terminalBundleConferenceHallPermissionDao.save(entity);
		return new TerminalBundleConferenceHallPermissionVO().set(entity);
	}
	
	/**
	 * 删除会场设备绑定<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月13日 下午2:38:56
	 * @param Long id 会场设备绑定id
	 */
	public void delete(Long id) throws Exception{
		TerminalBundleConferenceHallPermissionPO entity = terminalBundleConferenceHallPermissionDao.findOne(id);
		if(entity != null){
			terminalBundleConferenceHallPermissionDao.delete(entity);
		}
	}
	
}
