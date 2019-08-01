package com.sumavision.tetris.easy.process.yjgb.broadcast.article.api.server;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.easy.process.core.ProcessService;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
@RequestMapping(value = "/api/server/yjgb/broadcast/article")
public class ApiServerYjgbBroadcastArticleController {

	@Autowired
	private ProcessService processService;
	
	/**
	 * 应急广播根据播发媒资生成文章<br/>
	 * <b>作者:</b>ldy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年4月4日 下午2:43:55
	 * @param Long id 播发媒资id
	 * @return String 流程实例id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add")
	public Object add(
			Long id, 
			HttpServletRequest request) throws Exception{
		
		JSONObject variables = new JSONObject();
		variables.put("_pa1_id", id);
		
		return processService.startByKey("_yjgb_generate_article_by_compress_", variables.toJSONString(), null, null);
	}
	
}
