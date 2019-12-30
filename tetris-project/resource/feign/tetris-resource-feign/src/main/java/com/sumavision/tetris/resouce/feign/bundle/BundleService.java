package com.sumavision.tetris.resouce.feign.bundle;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

@Service
@Transactional(rollbackFor = Exception.class)
public class BundleService {

	@Autowired
	private BundleFeign bundleFeign;

	public List<BundleFeignVO> queryTranscodeDevice() throws Exception {

		return JSON.parseArray(bundleFeign.queryTranscodeDevice(), BundleFeignVO.class);

	}
	
	public BundleFeignVO queryDeviceByBundleId(String bundleId) throws Exception{
		return JSON.parseObject(bundleFeign.queryDeviceByBundleId(bundleId), BundleFeignVO.class);

		
	}

	public JSONObject queryAuth(String bundle_id) throws Exception {
		return bundleFeign.queryAuth(bundle_id);
	}
	


}
