package com.sumavision.bvc.device.monitor.live.device;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.config.feign.FeignConfiguration;

@FeignClient(name = "tetris-bvc-business", configuration = FeignConfiguration.class)
public interface MonitorLiveDeviceFeign {

	/**
	 * 停止设备转发<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月10日 上午9:51:56
	 * @param ids 设备的id集合
	 */
	@RequestMapping(value="/monitor/live/stop/live/device", method = RequestMethod.POST)
	public JSONObject stopLiveDevice(
			@RequestParam(value="ids") String ids) throws Exception;
	
	/**
	 * 失去权限后停止转发<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月11日 上午9:08:41
	 * @param userBundleBo 
	 */
	@RequestMapping(value="/monitor/live/stop/live/by/lose/privilege", method = RequestMethod.POST)
	public JSONObject stopLiveByLosePrivilege(
			@RequestParam(value="userBundleBoList") List<UserBundleBO> userBundleBoList) throws Exception;
	
	/**
	 * 重置设备<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月12日 下午7:10:07
	 * @return
	 */
	@RequestMapping(value="/monitor/live/reset/bundles", method = RequestMethod.POST)
	public JSONObject resetBundles(
			@RequestParam(value="bundleIds")List<Long> bundleIds) throws Exception;
}
