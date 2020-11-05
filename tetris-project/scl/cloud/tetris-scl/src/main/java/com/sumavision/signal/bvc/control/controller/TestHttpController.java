package com.sumavision.signal.bvc.control.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sumavision.signal.bvc.terminal.TerminalParam;

@Controller
@RequestMapping(value = "")
public class TestHttpController {
	
	private static final Logger LOG = LoggerFactory.getLogger(TestHttpController.class);

	/**
	 * 获取Param<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年7月16日 上午9:12:49
	 */
	@ResponseBody
	@RequestMapping(value = "/goform/form1068", method = RequestMethod.GET)
	public Object getParam(
			Long type,
			Long cmd,
			HttpServletRequest request) throws Exception{
		
		String param = "";
		if(type.equals(10l) && cmd.equals(1l)){
			param = TerminalParam.array2Data(TerminalParam.TerminalCallingParam); 
			LOG.info("获取通话参数：" + param);
		}else if(type.equals(16l) && cmd.equals(1l)){
			param = TerminalParam.array2Data(TerminalParam.TerminalEncodeParam); 
			LOG.info("获取编码参数：" + param);
		}else if(type.equals(17l) && cmd.equals(1l)){
			param = TerminalParam.array2Data(TerminalParam.TerminalDecodeParam); 
			LOG.info("获取解码参数：" + param);
		}
		
		return param;
	}
	
	/**
	 * 设置Param<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年7月16日 下午1:48:17
	 */
	@ResponseBody
	@RequestMapping(value = "/goform/form1068", method = RequestMethod.POST)
	public Object postParam(
			Long type,
			Long cmd,
			Long funType,
			String setString,
			HttpServletRequest request) throws Exception{

		if(type.equals(10l) && cmd.equals(1l)){
			LOG.info("设置通话参数：" + setString);
		}else if(type.equals(16l) && cmd.equals(1l)){
			LOG.info("设置编码参数：" + setString);
		}else if(type.equals(17l) && cmd.equals(1l)){
			LOG.info("设置解码参数：" + setString);
		}
		
		return "yes";
	}
	
}
