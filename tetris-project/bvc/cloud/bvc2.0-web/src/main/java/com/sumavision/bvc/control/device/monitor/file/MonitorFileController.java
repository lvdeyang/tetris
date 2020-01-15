package com.sumavision.bvc.control.device.monitor.file;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/monitor/file")
public class MonitorFileController {

	@RequestMapping(value = "/index")
	public ModelAndView index(String token){
		
		ModelAndView mv = new ModelAndView("web/bvc/monitor/file/file");
		mv.addObject("token", token);
		
		return mv;
	}
	
}
