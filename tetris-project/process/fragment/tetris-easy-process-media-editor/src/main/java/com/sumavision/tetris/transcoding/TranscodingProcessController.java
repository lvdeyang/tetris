package com.sumavision.tetris.transcoding;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

@Controller
@RequestMapping(value = "/api/process/transcoding/test")
public class TranscodingProcessController {
	
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/first")
	public Object first(String firstFirst, String firstSecond, HttpServletRequest request) throws Exception {
		System.out.println("firstFirst:" + firstFirst + ";firstSecond:" + firstSecond);
		return null;
	}
	
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/second")
	public Object second(String secondFirst, String secondSecond, HttpServletRequest request) throws Exception {
		System.out.println("secondFirst:" + secondFirst + ";secondSecond:" + secondSecond);
		return null;
	}
}
