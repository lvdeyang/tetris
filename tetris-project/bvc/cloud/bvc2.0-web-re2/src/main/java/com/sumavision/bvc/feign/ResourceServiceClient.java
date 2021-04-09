package com.sumavision.bvc.feign;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.suma.venus.resource.base.bo.BatchLockBundleParam;
import com.suma.venus.resource.base.bo.BatchLockBundleRespParam;
import com.suma.venus.resource.base.bo.LockBundleParam;
import com.suma.venus.resource.base.bo.LockBundleRespParam;
import com.suma.venus.resource.base.bo.LockChannelRespParam;
import com.suma.venus.resource.base.bo.ReleaseChannelRespParam;
import com.sumavision.bvc.BO.LockChannelRequest;
import com.sumavision.bvc.BO.ReleaseChannelRequest;
import com.sumavision.bvc.device.resource.bo.CreateResourceBO;
import com.sumavision.bvc.device.resource.bo.LiveChannelResourceBO;
import com.sumavision.tetris.config.feign.FeignConfiguration;


/**
 * 
 * @ClassName:  ResourceServiceClient   
 * @Description资源层feignclient  
 * @author: 
 * @date:   2018年7月18日 下午1:55:57   
 *     
 * @Copyright: 2018 Sumavision. All rights reserved. 
 * 注意：本内容仅限于北京数码视讯科技股份有限公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@FeignClient(name="tetris-resource", configuration = FeignConfiguration.class)
//@FeignClient(name="tetris-resource")
//@RequestMapping(value="/tetris-resource/api")
public interface ResourceServiceClient {
	
	/**
	 * Feign调用资源层http接口完成channel锁定
	 * @param lockChannelRequest
	 * @return
	 */
	@RequestMapping(value = "/api/lockChannel")
	public LockChannelRespParam lockChannel(@RequestBody LockChannelRequest lockChannelRequest);
	
	/**
	 * Feign调用资源层http接口完成channel释放
	 * @param releaseChannelRequest
	 * @return
	 */
	@RequestMapping(value = "/api/releaseChannel")
	public ReleaseChannelRespParam releaseChannel(@RequestBody ReleaseChannelRequest releaseChannelRequest);
	
	/**
	 * Feign调用资源层http接口完成bundle锁定
	 * @param lockParam
	 * @return
	 */
	@RequestMapping(value = "/api/lockBundle")
	public LockBundleRespParam lockBundle(@RequestBody LockBundleParam lockParam);
	
	/**
	 * Feign调用资源层http接口完成bundle释放
	 * @param releaseParam
	 * @return
	 */
	@RequestMapping(value = "/api/releaseBundle")
	public LockBundleRespParam releaseBundle(@RequestBody LockBundleParam releaseParam);
	
	/**
	 * Feign调用资源层http接口完成bundle批量锁定
	 * @param batchLockBundleParam
	 * @return
	 */
	@RequestMapping(value = "/api/batchLockBundleNew")
	public BatchLockBundleRespParam batchLockBundle(@RequestBody BatchLockBundleParam batchLockBundleParam);
	
	/**
	 * Feign调用资源层http接口完成bundle批量释放
	 * @param batchReleaseParam
	 * @return
	 */
	@RequestMapping(value = "/api/batchReleaseBundleNew")
	public BatchLockBundleRespParam batchReleaseBundle(@RequestBody BatchLockBundleParam batchReleaseParam);
	
	/**
	 * Feign调用资源层http接口创建资源
	 * @param createResource
	 * @return
	 */
	@RequestMapping(value = "/api/createResource")
	public Object createResource(@RequestBody CreateResourceBO createResourceBO);
	
	@RequestMapping(value = "/api/deleteLiveChannel")
	public Object deleteLiveChannel(@RequestBody LiveChannelResourceBO liveChannelResourceBO);
	
	/**
	 * 存联网passby<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月29日 上午9:26:13
	 * @param String uuid 唯一标识，业务uuid，或者bundleId
	 * @param String layerId 联网layerId
	 * @param String type 业务类型
	 * @param String protocol passby
	 */
	@RequestMapping(value = "/api/add/passby")
	public Object addLianwangPassby(
			@RequestParam("uuid") String uuid, 
			@RequestParam("layerId") String layerId, 
			@RequestParam("type") String type, 
			@RequestParam("protocol") String protocol);
	

	@RequestMapping(value = "/api/add/cover/passby")
	public Object coverLianwangPassby(
			@RequestParam("uuid") String uuid, 
			@RequestParam("layerId") String layerId, 
			@RequestParam("type") String type, 
			@RequestParam("protocol") String protocol);
	
	/**
	 * 删除联网passby<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月29日 上午9:48:59
	 * @param String uuid 业务uuid
	 */
	@RequestMapping(value = "/api/remove/passby")
	public Object removeLianwangPassby(@RequestParam("uuid") String uuid);
	
}
