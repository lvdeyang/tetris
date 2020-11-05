package com.sumavision.tetris.record;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.sumavision.tetris.record.storage.StorageService;


@WebListener
public class InitListener implements ServletContextListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(InitListener.class);



	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		try {

			ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(arg0.getServletContext());

			LOGGER.info("==============record initListener start=================");
			
			// TODO 仓库信息 初始化
			StorageService storageService = ctx.getBean(StorageService.class);
			storageService.init();
			
			// TODO 录制设备，状态查询
			
			// TODO 录制任务 重启后的处理
			
			
			LOGGER.info("==============record initListener finish=================");

		} catch (Exception e) {
			LOGGER.error("==============initListener fail=================", e);
		}
	}
}
