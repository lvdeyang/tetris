package com.sumavision.tetris.zoom.vod.device;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = VodDevicePO.class, idClass = Long.class)
public interface VodDeviceDAO extends BaseDAO<VodDevicePO>{

	/**
	 * 获取用户点播设备任务<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月27日 上午8:48:35
	 * @param String userId 用户id
	 * @param String dstBundleId 目标设备id
	 * @return VodDevicePO 点播任务
	 */
	public VodDevicePO findByUserIdAndDstBundleId(String userId, String dstBundleId);
	
}
