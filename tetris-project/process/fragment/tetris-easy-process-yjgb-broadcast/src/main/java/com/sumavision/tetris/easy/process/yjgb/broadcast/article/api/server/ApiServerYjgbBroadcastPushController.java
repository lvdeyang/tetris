package com.sumavision.tetris.easy.process.yjgb.broadcast.article.api.server;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.easy.process.core.ProcessService;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

@Controller
@RequestMapping(value = "/api/server/yjgb/broadcast/push")
public class ApiServerYjgbBroadcastPushController {

	@Autowired
	private ProcessService processService;
	
	/**
	 * 启动应急广播根据播发媒资生成广播流程<br/>
	 * <b>作者:</b>ldy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年4月4日 下午2:43:55
	 * @param Long id 播发媒资id
	 * @param String param 额外传的参数 {"freq":"频点", "vpid":"视频pid", "apid":"音频pid"}
	 * @return String 流程实例id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add")
	public Object add(
			Long id, 
			String param,
			HttpServletRequest request) throws Exception{
		
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("_pa1_id", id);
		jsonObject.put("_pa1_param", param);

		String processId = processService.startByKey("_yjgb_generate_push_by_compress_", jsonObject.toJSONString(), null, null);
	
		return new HashMapWrapper<String, Object>().put("uuid", processId).getMap();
	
	}
}
