package com.sumavision.bvc.control.device.monitor.record;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.sumavision.bvc.device.monitor.record.MonitorRecordPO;
import com.sumavision.bvc.device.monitor.record.MonitorRecordService;

@WebListener
public class MonitorRecordSchedulingListener implements ServletContextListener{

	private static final Logger LOG = LoggerFactory.getLogger(MonitorRecordSchedulingListener.class);
	
	@Override
	public void contextInitialized(ServletContextEvent event) {
		
		ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(event.getServletContext());
		
		final MonitorRecordService recordService = ctx.getBean(MonitorRecordService.class);
		
		Thread schedulingRecordThread = new Thread(new Runnable() {
			@Override
			public void run() {
				while (true){
					try{
						Thread.sleep(MonitorRecordPO.SCHEDULING_INTERVAL);
					}catch(Exception e){
						LOG.error("排期录制线程被打断！", e);
					}
					try{
						//recordService.doScheduling();
//						LOG.info("排期录制被暂时关闭！");
					}catch(Exception e){
						LOG.error("排期录制执行异常！", e);
					}
				}
				
			}
		});
		
		schedulingRecordThread.start();
		LOG.info("排期录制线程启动！");
		
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		
	}

}
