package com.sumavision.tetris.mims.event;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.config.feign.FeignConfiguration;

/**
 * 用户注册事件代理<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年1月25日 下午4:31:38
 */
@FeignClient(name = "tetris-mims", configuration = FeignConfiguration.class)
public interface UserRegisteredFeign {

	/**
	 * 用户注册事件代理<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月25日 下午4:31:31
	 * @param String userId 用户id
	 * @param String userName 用户昵称
	 * @param String companyId 公司id
	 * @param String companyName 公司名称
	 */
	@RequestMapping(value = "/event/publish/user/registered")
	public JSONObject userRegistered(
			@RequestParam("userId") String userId,
			@RequestParam("nickname") String nickname,
			@RequestParam("companyId") String companyId,
			@RequestParam("companyName") String companyName,
			@RequestParam("roleId") String roleId) throws Exception;
	
}
