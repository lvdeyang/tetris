package com.sumavision.tetris.resouce.feign.bundle;

import java.util.Map;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.config.feign.FeignConfiguration;

@FeignClient(name = "tetris-resource", configuration = FeignConfiguration.class)
public interface BundleFeign {

	@RequestMapping(value = "/feign/bundle/addTranscodeDevice", method = RequestMethod.POST)
	public String addTranscodeDevice(@RequestParam(value = "name") String name, @RequestParam(value = "ip") String ip,
			@RequestParam(value = "port") Integer port) throws Exception;

	@RequestMapping(value = "/feign/bundle/delTranscodeDevice", method = RequestMethod.POST)
	public String delTranscodeDevice(@RequestParam(value = "bundle_id") String bundle_id) throws Exception;

	@RequestMapping(value = "/feign/bundle/resetHeartBeatAndAlarm", method = RequestMethod.POST)
	public String resetHeartBeatAndAlarm(@RequestParam(value = "bundle_id") String bundle_id) throws Exception;

	/**
	 * 查询转码设备<br/>
	 */
	@RequestMapping(value = "/feign/bundle/queryTranscodeDevice", method = RequestMethod.POST)
	public String queryTranscodeDevice() throws Exception;

	/**
	 * 查询转码设备<br/>
	 */
	@RequestMapping(value = "/feign/bundle/queryDeviceByBundleId", method = RequestMethod.POST)
	public String queryDeviceByBundleId(@RequestParam(value = "bundle_id") String bundle_id) throws Exception;

	/**
	 * 查询设备的授权<br/>
	 */
	@RequestMapping(value = "/feign/bundle/queryAuth", method = RequestMethod.POST)
	public JSONObject queryAuth(@RequestParam(value = "bundle_id") String bundle_id) throws Exception;

	/**
	 * 查询经纬度范围内的设备<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月6日 下午5:13:02
	 * 
	 * @param Long longitude 经度
	 * @param Long latitude 纬度
	 * @param Long raidus 半径范围
	 */
	@RequestMapping(value = "/feign/bundle/query/visible/bundle", method = RequestMethod.POST)
	public JSONObject queryVisibleBundle(@RequestParam(value = "longitude") String longitude,
			@RequestParam(value = "latitude") String latitude, @RequestParam(value = "raidus") Long raidus)
			throws Exception;

	@RequestMapping(value = "/feign/bundle/input/add", method = RequestMethod.POST)
	public String inputAdd(@RequestBody JSONObject bundleJson)throws Exception;
	
}
