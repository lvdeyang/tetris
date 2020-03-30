package com.sumavision.tetris.zoom.vod.device;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sumavision.tetris.resouce.feign.bundle.BundleFeignService;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;

@Service
@Transactional(rollbackFor = Exception.class)
public class VodDeviceService {

	@Autowired
	private UserQuery userQuery;
	
	@Autowired
	private VodDeviceDAO vodDeviceDao;
	
	@Autowired
	private BundleFeignService bundleFeignService;
	
	/**
	 * 开始点播设备<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月27日 上午8:58:57
	 * @param String bundleId 设备id
	 * @return VodDeviceVO 点播设备任务
	 */
	public VodDeviceVO start(String bundleId) throws Exception{
		UserVO user = userQuery.current();
		VodDevicePO vodDevice = new VodDevicePO();
		vodDevice.setUserId(user.getId().toString());
		vodDevice.setUserno(user.getUserno());
		vodDevice.setNickname(user.getNickname());
		vodDevice.setDstBundleId(bundleId);
		//TODO 查询设备信息
		vodDeviceDao.save(vodDevice);
		return new VodDeviceVO().set(vodDevice);
	}

	/*public VodDeviceVO stop(Long id) throws Exception{
		
	}*/
	
}
