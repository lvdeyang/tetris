package com.sumavision.tetris.easy.process.config.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.sumavision.tetris.mvc.ext.response.parser.JsonBodyResponseParser;

@Component
public class EasyProcessServerPropsQuery {

	@Autowired
	private EasyProcessServerPropsFeign easyProcessServerPropsFeign;
	
	/**
	 * 查询流程服务属性<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月4日 下午3:11:36
	 * @return ServerProps 服务属性
	 */
	public ServerProps queryProps() throws Exception{
		return JsonBodyResponseParser.parseObject(easyProcessServerPropsFeign.queryProps(), ServerProps.class);
	}
	
}
