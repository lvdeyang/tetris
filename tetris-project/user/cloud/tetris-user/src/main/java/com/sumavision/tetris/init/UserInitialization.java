package com.sumavision.tetris.init;

import org.springframework.stereotype.Component;
import com.sumavision.tetris.commons.context.SystemInitialization;

@Component
public class UserInitialization implements SystemInitialization{

	@Override
	public int index() {
		return 0;
	}

	@Override
	public void init() {
		System.out.println("初始化用户服务！");
	}

}
