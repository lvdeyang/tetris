package com.sumavision.bvc.feign;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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
@FeignClient(name="suma-venus-resource")
@RequestMapping(value="/suma-venus-resource/api")
public interface ResourceServiceClient {
	
	/**
	 * Feign调用资源层http接口完成channel锁定
	 * @param lockChannelRequest
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/lockChannel")
	public LockChannelRespParam lockChannel(@RequestBody LockChannelRequest lockChannelRequest);
	
	/**
	 * Feign调用资源层http接口完成channel释放
	 * @param releaseChannelRequest
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/releaseChannel")
	public ReleaseChannelRespParam releaseChannel(@RequestBody ReleaseChannelRequest releaseChannelRequest);
	
	/**
	 * Feign调用资源层http接口完成bundle锁定
	 * @param lockParam
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/lockBundle")
	public LockBundleRespParam lockBundle(@RequestBody LockBundleParam lockParam);
	
	/**
	 * Feign调用资源层http接口完成bundle释放
	 * @param releaseParam
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/releaseBundle")
	public LockBundleRespParam releaseBundle(@RequestBody LockBundleParam releaseParam);
	
	/**
	 * Feign调用资源层http接口完成bundle批量锁定
	 * @param batchLockBundleParam
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/batchLockBundleNew")
	public BatchLockBundleRespParam batchLockBundle(@RequestBody BatchLockBundleParam batchLockBundleParam);
	
	/**
	 * Feign调用资源层http接口完成bundle批量释放
	 * @param batchReleaseParam
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/batchReleaseBundleNew")
	public BatchLockBundleRespParam batchReleaseBundle(@RequestBody BatchLockBundleParam batchReleaseParam);
	
	/**
	 * Feign调用资源层http接口创建资源
	 * @param createResource
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/createResource")
	public Object createResource(@RequestBody CreateResourceBO createResourceBO);
	
	@RequestMapping(method = RequestMethod.POST, value = "/deleteLiveChannel")
	public Object deleteLiveChannel(@RequestBody LiveChannelResourceBO liveChannelResourceBO);
}
