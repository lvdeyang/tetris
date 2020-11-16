package com.sumavision.tetris.bvc.business.terminal.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;

@Service
public class TerminalBundleUserPermissionService {
	
	@Autowired
	private TerminalBundleUserPermissionFeign terminalBundleUserPermissionFeign;
	
	public void addAll(List<String> userIds) throws Exception{
		terminalBundleUserPermissionFeign.addAll(JSON.toJSONString(userIds));
	}
}