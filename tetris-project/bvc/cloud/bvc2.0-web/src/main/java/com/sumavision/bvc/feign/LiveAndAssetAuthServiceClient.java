package com.sumavision.bvc.feign;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.suma.venus.resource.base.bo.LockChannelRespParam;
import com.sumavision.tetris.config.feign.FeignConfiguration;


/**
 * 
 * @ClassName:  setUserAuthByUsernames   
 * @Description资源层feignclient  
 * @author: 
 * @date:   2018年8月30日 19:55:57   
 *     
 * @Copyright: 2018 Sumavision. All rights reserved. 
 * 注意：本内容仅限于北京数码视讯科技股份有限公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@FeignClient(name="tetris-resource", configuration = FeignConfiguration.class)
public interface LiveAndAssetAuthServiceClient {
	
	/**
	 * Feign调用资源层http接口完设置直播/点播权限
	 * @param setUserAuthByUsernames
	 * @return
	 */
	@RequestMapping(value = "/api/setUserAuthByUsernames")
	public LockChannelRespParam setUserAuthByUsernames(@RequestBody Object bo);	
	
}
