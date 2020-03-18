package com.sumavision.tetris.resouce.feign.resource;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.config.feign.FeignConfiguration;

@FeignClient(name = "tetris-resource", configuration = FeignConfiguration.class)
public interface ResourceFeign {

	/**
	 * 查询用户同一权限下所有用户信息<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月17日 下午4:17:12
	 * @param Long userId 用户id
	 * @param String terminalType 终端类型
	 */
	@RequestMapping(value = "/feign/resource/query/users/by/userId", method = RequestMethod.POST)
	public JSONObject queryUsersByUserId(
			@RequestParam(value = "userId") Long userId,
			@RequestParam(value = "terminalType") String terminalType) throws Exception;
	
	/**
	 * 查询webrtc服务节点<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月17日 下午4:22:01
	 */
	@RequestMapping(value = "/feign/resource/query/webrtc", method = RequestMethod.POST)
	public JSONObject queryWebRtc() throws Exception;
	
	/**
	 * 查询用户下对应资源类型的资源信息<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月17日 下午5:10:58
	 * @param String userIds 用户列表
	 * @param String type 资源类型
	 */
	@RequestMapping(value = "/feign/resource/query/resource")
	public JSONObject queryResource(
			@RequestParam(value = "userIds") String userIds,
			@RequestParam(value = "type") String type) throws Exception;

}
