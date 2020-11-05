package com.sumavision.tetris.mims.app.media.encode;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.config.feign.FeignConfiguration;

@FeignClient(name = "tetris-mims", configuration = FeignConfiguration.class)
public interface MediaEncodeFeign {

	/**
	 * 查询加密密钥<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月10日 下午5:46:46
	 */
	@RequestMapping(value = "/media/encode/feign/quest/key")
	public JSONObject queryKey() throws Exception;
}
