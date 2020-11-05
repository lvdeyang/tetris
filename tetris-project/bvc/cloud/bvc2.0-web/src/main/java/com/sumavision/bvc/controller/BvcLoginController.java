package com.sumavision.bvc.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sumavision.bvc.common.RestResult;
import com.sumavision.bvc.common.RestResultGenerator;
import com.sumavision.bvc.vo.UserVO;

import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;

@Controller
@RequestMapping("/login")
@Slf4j
public class BvcLoginController {
	@RequestMapping(value = "", method = RequestMethod.POST)
	@ResponseBody
	public RestResult<String> login(@RequestParam("username") String userName,
			@RequestParam("password") String password) {
		List<String> res = null;
		log.info("login");
		UserVO userVO = UserVO.builder()
				.name("admin")
				.password("sumavisonrd")
				.build();
		return RestResultGenerator.genResult(true, JSONObject.fromObject(userVO).toString(), "login ok");
	}
}
