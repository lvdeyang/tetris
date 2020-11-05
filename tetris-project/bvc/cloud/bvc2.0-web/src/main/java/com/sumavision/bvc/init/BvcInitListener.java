package com.sumavision.bvc.init;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.sumavision.bvc.basic.service.BasicRoleService;
import com.sumavision.bvc.device.system.AvtplService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@WebListener
public class BvcInitListener implements ServletContextListener{

	@Override
	public void contextInitialized(ServletContextEvent eve) {
		
		ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(eve.getServletContext());
		
		final BasicRoleService basicRoleService = ctx.getBean(BasicRoleService.class);
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
				try{
					basicRoleService.generateDefaultRoles();
				}catch(Exception e){
					log.error("添加默认系统角色异常！", e);
				}
				log.info("==============BvcInitListener initThread finish=================");
			}
		});
		
		initThread.start();
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		
	}

}
