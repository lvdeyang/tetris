package com.sumavision.bvc.init;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.sumavision.bvc.device.system.AvtplService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@WebListener
public class BvcInitListener implements ServletContextListener{

	@Override
	public void contextInitialized(ServletContextEvent eve) {
		
		ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(eve.getServletContext());
		
		final AvtplService avtplService = ctx.getBean(AvtplService.class);
		
		Thread initThread = new Thread(new Runnable() {
			@Override
			public void run() {
				log.info("==============BvcInitListener initThread start=================");
				try{
					avtplService.generateDefaultAvtpls();
				}catch(Exception e){
					log.error("添加默认参数模板异常！", e);
				}
				log.info("==============BvcInitListener 完成模板=================");
				log.info("==============BvcInitListener initThread finish=================");
			}
		});
		
		initThread.start();
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		
	}

}
