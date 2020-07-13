package com.sumavision.tetris.bvc.business.terminal.user;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sumavision.tetris.bvc.business.terminal.user.exception.TerminalBundleUserPermissionNotFoundException;

@Service
public class TerminalBundleUserPermissionService {

	@Autowired
	private TerminalBundleUserPermissionDAO terminalBundleUserPermissionDao;
	
	/**
	 * 添加用户设备绑定<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月10日 上午11:13:08
	 * @param String userId 用户id
	 * @param Long terminalId 终端id
	 * @param Long terminalBundleId 终端设备id
	 * @param String bundleType 设备类型
	 * @param String bundleId 设备id
	 * @param String bundleName 设备名称
	 * @return TerminalBundleUserPermissionVO 真实设备信息
	 */
	public TerminalBundleUserPermissionVO add(
			String userId,
			Long terminalId,
			Long terminalBundleId,
			String bundleType,
			String bundleId,
			String bundleName) throws Exception{
		TerminalBundleUserPermissionPO entity = new TerminalBundleUserPermissionPO();
		entity.setUserId(userId);
		entity.setTerminalId(terminalId);
		entity.setTerminalBundleId(terminalBundleId);
		entity.setBundleType(bundleType);
		entity.setBundleId(bundleId);
		entity.setBundleName(bundleName);
		entity.setUpdateTime(new Date());
		terminalBundleUserPermissionDao.save(entity);
		return new TerminalBundleUserPermissionVO().set(entity);
	}
	
	/**
	 * 修改用户设备绑定<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月10日 上午11:13:08
	 * @param Long id 用户绑定设备id
	 * @param String bundleType 设备类型
	 * @param String bundleId 设备id
	 * @param String bundleName 设备名称
	 * @return TerminalBundleUserPermissionVO 真实设备信息
	 */
	public TerminalBundleUserPermissionVO edit(
			Long id,
			String bundleType,
			String bundleId,
			String bundleName) throws Exception{
		TerminalBundleUserPermissionPO entity =  terminalBundleUserPermissionDao.findOne(id);
		if(entity == null){
			throw new TerminalBundleUserPermissionNotFoundException(id);
		}
		entity.setBundleType(bundleType);
		entity.setBundleId(bundleId);
		entity.setBundleName(bundleName);
		terminalBundleUserPermissionDao.save(entity);
		return new TerminalBundleUserPermissionVO().set(entity);
	}
	
	/**
	 * 删除用户设备绑定<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月10日 上午11:23:25
	 * @param Long id 用户绑定设备id
	 */
	public void delete(Long id) throws Exception{
		TerminalBundleUserPermissionPO entity =  terminalBundleUserPermissionDao.findOne(id);
		if(entity != null){
			terminalBundleUserPermissionDao.delete(entity);
		}
	}
}
