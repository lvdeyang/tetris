package com.sumavision.bvc.control.device.group.contacts;

import java.util.Collection;
import java.util.List;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.MetBaseDAO;

@RepositoryDefinition(domainClass = DeviceGroupContactsPO.class, idClass = Long.class)
public interface DeviceGroupContactsDAO extends MetBaseDAO<DeviceGroupContactsPO>{

	/**
	 * 根据用户id和bundleId查询用户联系人<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月6日 下午2:45:32
	 * @param Long userId 用户id
	 * @param Collection<String> bundleIdList 设备id列表
	 * @return List<DeviceGroupContactsPO> 联系人列表
	 */
	public List<DeviceGroupContactsPO> findByUserIdAndBundleIdIn(Long userId, Collection<String> bundleIdList);
	
	public List<DeviceGroupContactsPO> findByUserId(Long userId);
}
