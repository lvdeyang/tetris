package com.sumavision.signal.bvc.feign;

import java.util.List;
import java.util.Map;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.suma.venus.resource.base.bo.BundleOfflineRequest;
import com.suma.venus.resource.base.bo.BundleOfflineResponse;
import com.suma.venus.resource.base.bo.BundleOnlineRequest;
import com.suma.venus.resource.base.bo.BundleOnlineResp;
import com.suma.venus.resource.base.bo.GetBundleInfoRequest;
import com.suma.venus.resource.base.bo.LayerHeartBeatRequest;
import com.suma.venus.resource.base.bo.LayerHeartBeatResponse;
import com.sumavision.tetris.config.feign.FeignConfiguration;

/**
 * 资源层调用feign接口<br/>
 * <b>作者:</b>wjw<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年8月5日 上午9:32:11
 */
@FeignClient(name="tetris-resource", configuration = FeignConfiguration.class)
@RequestMapping(value="/api")
public interface ResourceServiceClient {

	/**
	 * 资源在线状态上报feign<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年8月5日 上午9:50:46
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/access/bundleOnline")
    public BundleOnlineResp bundleOnline(@RequestBody BundleOnlineRequest request);
	
	/**
	 * <p>资源不在线状态上报feign</p>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年8月5日 上午9:51:20
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/access/bundleOffline")
    public BundleOfflineResponse bundleOffline(@RequestBody BundleOfflineRequest request);
	
	/**
	 * 资源获取bundle整体信息<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年8月7日 下午2:44:33
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/access/getBundleInfo")
    public Map<String, Object> getBundleInfo(@RequestBody GetBundleInfoRequest request);
	
	/**
	 * 批量获取bundleInf信息<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年9月6日 上午8:51:57
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/access/getBatchBundleInfos")
	public Map<String, Object> getBatchBundleInfos(@RequestBody List<String> bundleIdList);
	
	/**
	 * 接入层心跳跳<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年9月23日 上午10:26:36
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/access/layerHeartbeat")
	public LayerHeartBeatResponse layerHeartBeat(@RequestBody LayerHeartBeatRequest request);
}
