package com.sumavision.tetris.menu.init;

import org.springframework.stereotype.Component;
import com.sumavision.tetris.commons.context.SystemInitialization;

@Component
public class MenuInitialization implements SystemInitialization{

	@Override
	public int index() {
		return 0;
	}

	@Override
	public void init() {
		System.out.println("初始化菜单服务！");
	}

}
