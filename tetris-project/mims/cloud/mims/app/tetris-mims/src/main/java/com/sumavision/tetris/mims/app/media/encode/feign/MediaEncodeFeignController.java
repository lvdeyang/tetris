package com.sumavision.tetris.mims.app.media.encode.feign;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sumavision.tetris.mims.app.media.encode.FileEncodeService;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

@Controller
@RequestMapping(value = "/media/encode/feign")
public class MediaEncodeFeignController {
	@Autowired
	private FileEncodeService fileEncodeService;

	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/quest/key")
	public Object questKey(HttpServletRequest request) throws Exception{
		return fileEncodeService.AES_KEY;
	}
}
