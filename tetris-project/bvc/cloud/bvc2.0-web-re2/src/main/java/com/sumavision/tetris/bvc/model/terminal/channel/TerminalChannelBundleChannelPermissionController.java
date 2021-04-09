package com.sumavision.tetris.bvc.model.terminal.channel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

@Controller
@RequestMapping(value = "/tetris/bvc/model/terminal/channel/bundle/channel/permission")
public class TerminalChannelBundleChannelPermissionController {

	/**
	 * 查询通道映射参数类型<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月12日 下午4:59:07
	 * @return Map<String, String> 通道映射参数类型
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/query/param/types")
	public Object queryParamTypes(HttpServletRequest request) throws Exception{
		List<Map<String, String>> paramTypes = new ArrayList<Map<String, String>>();
		ChannelParamsType[] values = ChannelParamsType.values();
		for(ChannelParamsType value:values){
			if(value.equals(ChannelParamsType.DEFAULT)) continue;
			paramTypes.add(new HashMapWrapper<String, String>().put("key", value.toString())
															   .put("value", value.getName())
															   .getMap());
		}
		return paramTypes;
	}
	
}
