package com.sumavision.tetris.bvc.business.dispatch;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.config.feign.FeignConfiguration;

@FeignClient(name = "tetris-bvc-business", configuration = FeignConfiguration.class)
public interface DispatchFeign {
	
	/**
	 * 执行调度<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月12日 下午5:01:33
	 * @param dispatch
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/tetris/dispatch/execute", method = RequestMethod.POST)
	public JSONObject dispatch(
			@RequestParam(value = "dispatch") String dispatch) throws Exception;
	
	/**
	 * 批量透传<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月12日 下午5:01:43
	 * @param passbyArray
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/tetris/dispatch/passby", method = RequestMethod.POST)
	public JSONObject passby(
			@RequestBody String passbyArray) throws Exception;

}
