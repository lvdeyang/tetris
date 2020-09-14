package com.suma.venus.resource.bundle._5g;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.suma.venus.resource.bundle._5g.exception.BundleIdUsedByOtherDeviceModelException;
import com.suma.venus.resource.bundle._5g.exception.NoPortCanBeUsedException;
import com.suma.venus.resource.dao.BundleDao;
import com.suma.venus.resource.dao.ExtraInfoDao;
import com.suma.venus.resource.pojo.BundlePO;
import com.suma.venus.resource.pojo.ExtraInfoPO;
import com.suma.venus.resource.service.BundleService;

@Service
public class FifthGenerationKnapsackService {
	
	/** 设备模板类型 */
	private static final String DEVICE_MODEL = "5G";
	
	/** 5G代理服务端口 */
	private static final String EXTRA_INFO_NAME = "5gProxyPort";

	@Autowired
	private BundleDao bundleDao;
	
	@Autowired
	private BundleService bundleService;
	
	@Autowired
	private ExtraInfoDao extraInfoDao;
	
	/**
	 * 5G背包注册<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月22日 下午1:37:06
	 * @param String bundleId 5G背包序列号
	 * @return Integer 5G代理服务端口
	 */
	@Transactional(rollbackFor = Exception.class)
	public synchronized Integer doRegister(String bundleId) throws Exception{
		
		BundlePO existBundle = bundleDao.findByBundleId(bundleId);
		if(existBundle!=null && !DEVICE_MODEL.equals(existBundle.getDeviceModel())){
			throw new BundleIdUsedByOtherDeviceModelException(bundleId);
		}
		
		if(existBundle != null){
			ExtraInfoPO extraInfo = extraInfoDao.findByBundleIdAndName(bundleId, EXTRA_INFO_NAME);
			return Integer.parseInt(extraInfo.getValue());
		}
		
		//选一个端口
		Integer port = choosePort();
		
		//创建bundle
		BundlePO bundle = new BundlePO();
		bundle.setBundleId(bundleId);
		bundle.setDeviceModel(DEVICE_MODEL);
		bundle.setBundleType("VenusTerminal");
		bundleDao.save(bundle);
		
		//创建通道
		bundleService.configDefaultAbility(bundle);
		
		//创建端口占用
		ExtraInfoPO extraInfo = new ExtraInfoPO();
		extraInfo.setBundleId(bundle.getBundleId());
		extraInfo.setName(EXTRA_INFO_NAME);
		extraInfo.setValue(port.toString());
		extraInfoDao.save(extraInfo);
		
		return port;
	}
	
	/**
	 * 选一个5G代理服务端口<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月22日 下午1:42:55
	 * @return 5G代理服务端口
	 */
	private Integer choosePort() throws Exception{
		FifthGenerationProperties props = FifthGenerationProperties.getInstance();
		List<Integer> existPorts = new ArrayList<Integer>();
		List<BundlePO> bundles = bundleDao.findByDeviceModel(DEVICE_MODEL);
		if(bundles!=null && bundles.size()>0){
			List<String> bundleIds = new ArrayList<String>();
			for(BundlePO bundle:bundles){
				bundleIds.add(bundle.getBundleId());
			}
			List<ExtraInfoPO> variables = extraInfoDao.findByBundleIdInAndName(bundleIds, EXTRA_INFO_NAME);
			if(variables!=null && variables.size()>0){
				for(ExtraInfoPO variable:variables){
					existPorts.add(Integer.valueOf(variable.getValue()));
				}
			}
		}
		for(int i=props.getMinPort().intValue(); i<=props.getMaxPort().intValue(); i++){
			boolean exist = false;
			for(Integer existPort:existPorts){
				if(i == existPort.intValue()){
					exist = true;
					break;
				}
			}
			if(!exist){
				return i;
			}
		}
		throw new NoPortCanBeUsedException();
	}
	
}
