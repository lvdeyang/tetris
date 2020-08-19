package com.suma.venus.resource.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.suma.venus.resource.dao.BundleDao;
import com.suma.venus.resource.pojo.BundlePO;
import com.suma.venus.resource.pojo.BundlePO.ONLINE_STATUS;
import com.suma.venus.resource.vo.G01BundleVO;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

@Service
@Transactional(rollbackFor = Exception.class)
public class ApiResourceService {
	
	@Autowired
	private BundleService bundleService;
	
	@Autowired
	private BundleDao bundleDao;

	/**
	 * 添加g01设备<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月19日 下午4:07:29
	 * @param String bundleIp 设备ip
	 * @param String daId DA系统id
	 * @return G01BundleVO g01设备信息
	 */
	public G01BundleVO addG01Bundle(String bundleIp, String daId) throws Exception{
		
		String identify = new StringBufferWrapper().append(daId)
				 								   .append("_")
				 								   .append(bundleIp)
				 								   .toString();
		
		BundlePO bundle = bundleDao.findByDeviceModelAndUsername("g01", identify);
		if(bundle != null){
			throw new BaseException(StatusCode.ERROR, "bundleIp为：" + bundleIp + "，daId为：" + daId + " 的设备已经存在！");
		}
		
		//创建G01设备
		BundlePO g01 = new BundlePO();
		g01.setBundleName(identify);
		g01.setUsername(identify);
		g01.setOnlinePassword(identify);
		g01.setBundleId(BundlePO.createBundleId());
		g01.setDeviceModel("g01");
		g01.setBundleType("VenusTerminal");
		g01.setDeviceIp(bundleIp);
		g01.setOnlineStatus(ONLINE_STATUS.OFFLINE);
		
		bundleService.configDefaultAbility(g01);
		
		bundleDao.save(g01);
		
		return new G01BundleVO().setBundleId(g01.getBundleId());
	}
	
	/**
	 * g01设备认证上线<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月19日 下午4:09:32
	 * @param String bundleIp 设备ip
	 * @param String daId DA系统id
	 * @return G01BundleVO g01设备信息
	 */
	public G01BundleVO certifyG01Bundle(String bundleIp, String daId) throws Exception{
		
		String identify = new StringBufferWrapper().append(daId)
				 								   .append("_")
				 								   .append(bundleIp)
				 								   .toString();
		
		BundlePO bundle = bundleDao.findByDeviceModelAndUsername("g01", identify);
		if(bundle == null){
			throw new BaseException(StatusCode.ERROR, "bundleIp为：" + bundleIp + "，daId为：" + daId + " 的设备不存在！");
		}
		
		bundle.setOnlineStatus(ONLINE_STATUS.ONLINE);
		bundleDao.save(bundle);
		
		return new G01BundleVO().setBundleId(bundle.getBundleId());
	}
	
	/**
	 * 删除g01设备<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月19日 下午4:17:35
	 * @param String bundleIp 设备ip
	 * @param String daId DA系统id
	 */
	public void deleteG01Bundle(String bundleId) throws Exception{
		
		BundlePO bundle = bundleDao.findByBundleId(bundleId);
		if(bundle == null){
			throw new BaseException(StatusCode.ERROR, "bundleId为：" + " 的设备不存在！");
		}
		
		bundleDao.delete(bundle);
	}
}
