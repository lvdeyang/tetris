package com.sumavision.tetris.bvc.business.terminal.user;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.sumavision.tetris.config.feign.FeignConfiguration;

@FeignClient(name = "tetris-bvc-business", configuration = FeignConfiguration.class)
public interface TerminalBundleUserPermissionFeign {

	@RequestMapping(value="/tetris/bvc/business/terminal/bundle/user/permission/add/all/bunch",method=RequestMethod.POST)
	public void addAll(
			@RequestParam(value = "userIds") String userIds) throws Exception;
}
