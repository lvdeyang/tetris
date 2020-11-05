package com.suma.venus.resource.bundle._5g;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

@Controller
@RequestMapping(value = "/fifth/generation/knapsack")
public class FifthGenerationKnapsackController {

	@Autowired
	private FifthGenerationKnapsackService fifthGenerationKnapsackService;
	
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/do/register")
	public Object doRegister(
			String bundleId,
			HttpServletRequest request) throws Exception{
		
		return fifthGenerationKnapsackService.doRegister(bundleId);
	}
	
	
}
