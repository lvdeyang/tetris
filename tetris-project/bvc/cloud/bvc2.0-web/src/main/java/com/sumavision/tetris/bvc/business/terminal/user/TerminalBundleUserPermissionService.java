package com.sumavision.tetris.bvc.business.terminal.user;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.suma.venus.resource.dao.BundleDao;
import com.suma.venus.resource.pojo.BundlePO;
import com.sumavision.tetris.bvc.business.terminal.user.exception.TerminalBundleUserPermissionNotFoundException;
import com.sumavision.tetris.bvc.model.terminal.TerminalBundleDAO;
import com.sumavision.tetris.bvc.model.terminal.TerminalBundlePO;
import com.sumavision.tetris.bvc.model.terminal.TerminalBundleType;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;

@Service
public class TerminalBundleUserPermissionService {

	@Autowired
	private TerminalBundleUserPermissionDAO terminalBundleUserPermissionDao;
	
	@Autowired
	private TerminalBundleDAO terminalBundleDao;
	
	@Autowired
	private BundleDao bundleDao;
	
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
	
	/**
	 * 为用户设备自动绑定<br/>
	 * <b>作者:</b>lixin<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年8月17日 上午11:15:25
	 * @param userId 用户id
	 * @param terminalId 设备id
	 * @throws BaseException 
	 */
	public void addAll(
			String userId,
			Long terminalId) throws BaseException {
		
		//TerminalBundleType a = TerminalBundleType.DECODER.equals(other);
		if(1!=terminalId){
			throw new BaseException(StatusCode.FORBIDDEN,"QT终端才能一键绑定");
		}
		//1拿到QT对应的所有终端
		List<TerminalBundlePO> terminalBundleEntities = terminalBundleDao.findByTerminalIdAndBundleType(terminalId,"player");
		//2拿到用户对应的设备
		//通过device_model以及user_id拿到设备
		List<BundlePO>  bundleEntities= bundleDao.findByDeviceModelAndUserId("player",userId);
		//3将对应的设备与终端进行关联
		//for循环为TETRIS_BVC_BUSINESS_TERMINAL_BUNDLE_USER_PERMISSION添加数据  如果原来有数据就将数据更新
		if(terminalBundleEntities.size()<=bundleEntities.size()){
			for(int index=0;index<terminalBundleEntities.size();index++){
				BundlePO bundlePOTemp =bundleEntities.get(index);
				TerminalBundlePO terminalBundlePOTemp =terminalBundleEntities.get(index);
				TerminalBundleUserPermissionPO entity=terminalBundleUserPermissionDao.findByUserIdAndTerminalIdAndTerminalBundleId(
						bundlePOTemp.getUserId().toString(),
						terminalBundlePOTemp.getTerminalId(),
						terminalBundlePOTemp.getId());
				if(entity!=null){
					entity.setUserId(userId);
					entity.setTerminalId(terminalId);
					entity.setTerminalBundleId(terminalBundlePOTemp.getId());
					entity.setBundleType(terminalBundlePOTemp.getBundleType());
					entity.setBundleId(bundlePOTemp.getBundleId());
					entity.setBundleName(bundlePOTemp.getBundleName());
					entity.setUpdateTime(new Date());
					terminalBundleUserPermissionDao.save(entity);
				}else{
					entity= new TerminalBundleUserPermissionPO();
					entity.setUserId(userId);
					entity.setTerminalId(terminalId);
					entity.setTerminalBundleId(terminalBundlePOTemp.getId());
					entity.setBundleType(terminalBundlePOTemp.getBundleType());
					entity.setBundleId(bundlePOTemp.getBundleId());
					entity.setBundleName(bundlePOTemp.getBundleName());
					entity.setUpdateTime(new Date());
					terminalBundleUserPermissionDao.save(entity);
				}
			}
		}
		else{
			for(int index=0;index<bundleEntities.size();index++){
				BundlePO bundlePOTemp =bundleEntities.get(index);
				TerminalBundlePO terminalBundlePOTemp =terminalBundleEntities.get(index);
				TerminalBundleUserPermissionPO entity=terminalBundleUserPermissionDao.findByUserIdAndTerminalIdAndTerminalBundleId(
						bundlePOTemp.getUserId().toString(),
						terminalBundlePOTemp.getTerminalId(),
						terminalBundlePOTemp.getId());
				if(entity!=null){
					entity.setUserId(userId);
					entity.setTerminalId(terminalId);
					entity.setTerminalBundleId(terminalBundlePOTemp.getId());
					entity.setBundleType(terminalBundlePOTemp.getBundleType());
					entity.setBundleId(bundlePOTemp.getBundleId());
					entity.setBundleName(bundlePOTemp.getBundleName());
					entity.setUpdateTime(new Date());
					terminalBundleUserPermissionDao.save(entity);
				}else{
					entity= new TerminalBundleUserPermissionPO();
					entity.setUserId(userId);
					entity.setTerminalId(terminalId);
					entity.setTerminalBundleId(terminalBundlePOTemp.getId());
					entity.setBundleType(terminalBundlePOTemp.getBundleType());
					entity.setBundleId(bundlePOTemp.getBundleId());
					entity.setBundleName(bundlePOTemp.getBundleName());
					entity.setUpdateTime(new Date());
					terminalBundleUserPermissionDao.save(entity);
				}
			}
		}
	}
}
