package com.sumavision.tetris.system.storage;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sumavision.tetris.system.storage.exception.SystemStorageNotExistException;

@Service
@Transactional(rollbackFor = Exception.class)
public class SystemStorageService {

	@Autowired
	private SystemStorageDAO systemStorageDao;
	
	@Autowired
	private SystemStoragePartitionDAO systemStoragePartitionDao;
	
	/**
	 * 添加系统存储<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月7日 上午8:48:37
	 * @param String name 存储名称
	 * @param String rootPath 存储根路径
	 * @param String baseFtpPath ftp根路径
	 * @param String basePreviewPath 预览http根路径
	 * @param String gadgetBasePath 小工具访问http根路径 
	 * @param String serverGadgetType 服务小工具类型
	 * @param String remark 备注
	 * @return SystemStoragePO 存储
	 */
	public SystemStoragePO add(
			String name,
			String rootPath,
			String baseFtpPath,
			String basePreviewPath,
			String gadgetBasePath,
			String serverGadgetType,
			String remark) throws Exception{
		
		SystemStoragePO entity = new SystemStoragePO();
		entity.setName(name);
		entity.setRootPath(rootPath);
		entity.setBaseFtpPath(baseFtpPath);
		entity.setBasePreviewPath(basePreviewPath);
		entity.setGadgetBasePath(gadgetBasePath);
		entity.setServerGadgetType(ServerGadgetType.valueOf(serverGadgetType));
		entity.setRemark(remark);
		entity.setStatus(0);
		entity.setUpdateTime(new Date());
		systemStorageDao.save(entity);
		
		return entity;
	}
	
	/**
	 * 删除存储，并删除分区<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月7日 上午11:44:44
	 * @param Long id 存储id
	 */
	public void remove(Long id) throws Exception{
		SystemStoragePO entity = systemStorageDao.findById(id);
		if(entity != null){
			systemStorageDao.delete(entity);
		}
		List<SystemStoragePartitionPO> partitions = systemStoragePartitionDao.findByStorageId(id);
		if(partitions!=null && partitions.size()>0){
			systemStoragePartitionDao.deleteInBatch(partitions);
		}
	}
	
	/**
	 * 修改系统存储参数<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月7日 上午8:48:37
	 * @param Long id 存储id
	 * @param String name 存储名称
	 * @param String rootPath 存储根路径
	 * @param String baseFtpPath ftp根路径
	 * @param String basePreviewPath 预览http根路径
	 * @param String gadgetBasePath 小工具访问http根路径 
	 * @param String serverGadgetType 服务小工具类型
	 * @param String remark 备注
	 * @return SystemStoragePO 存储
	 */
	public SystemStoragePO edit(
			Long id,
			String name,
			String rootPath,
			String baseFtpPath,
			String basePreviewPath,
			String gadgetBasePath,
			String serverGadgetType,
			String remark) throws Exception{
		SystemStoragePO entity = systemStorageDao.findById(id);
		if(entity == null){
			throw new SystemStorageNotExistException(id, name);
		}
		entity.setName(name);
		entity.setRootPath(rootPath);
		entity.setBaseFtpPath(baseFtpPath);
		entity.setBasePreviewPath(basePreviewPath);
		entity.setGadgetBasePath(gadgetBasePath);
		entity.setServerGadgetType(ServerGadgetType.valueOf(serverGadgetType));
		entity.setRemark(remark);
		systemStorageDao.save(entity);
		
		return entity;
	}
	
}
