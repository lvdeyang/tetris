package com.sumavision.tetris.resouce.feign.bundle;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.mvc.ext.response.parser.JsonBodyResponseParser;

@Service
@Transactional(rollbackFor = Exception.class)
public class BundleFeignService {

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
	
	/**
	 * 查询经纬度范围内的设备<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月6日 下午5:18:41
	 * @param Long longitude 经度
	 * @param Long latitude 纬度
	 * @param Long raidus 半径范围
	 */
	public List<BundleVO> queryVisibleBundle(Long longitude, Long latitude, Long raidus) throws Exception{
		return JsonBodyResponseParser.parseArray(bundleFeign.queryVisibleBundle(longitude, latitude, raidus), BundleVO.class);
	}

}
