package com.sumavision.tetris.mims.app.media.compress;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.config.feign.FeignConfiguration;

@FeignClient(name = "tetris-mims", configuration = FeignConfiguration.class)
public interface MediaCompressFeign {

	/**
	 * 根据媒资id查询媒资<br/>
	 * <b>作者:</b>ldy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年4月1日 下午5:46:46
	 * @param Long id 媒资id
	 * @return MediaCompressVO 播发媒资
	 */
	@RequestMapping(value = "/media/feign/compress/query")
	public JSONObject query(@RequestParam("id") Long id) throws Exception;
}
