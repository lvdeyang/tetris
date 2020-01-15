package com.sumavision.bvc.control.device.monitor.message;

import java.util.Collection;
import java.util.Map;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import com.sumavision.bvc.device.monitor.message.MessageService;

@WebListener
public class MonitorMessageCleaner implements ServletContextListener{

	public static final int INTERVAL = 2000;
	
	private static final Logger LOG = LoggerFactory.getLogger(MonitorMessageCleaner.class);
	
	@Override
	public void contextInitialized(ServletContextEvent event) {
		ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(event.getServletContext());
		
		Map<String, MessageService> beanMap = ctx.getBeansOfType(MessageService.class);
		final Collection<MessageService> messageServices = beanMap.values();
		
		Thread schedulingRecordThread = new Thread(new Runnable() {
			@Override
			public void run() {
				while (true){
					try{
						Thread.sleep(INTERVAL);
					}catch(Exception e){
						LOG.error("消息清理器线程被打断！", e);
					}
					try{
						if(messageServices!=null && messageServices.size()>0){
							for(MessageService messageService:messageServices){
								messageService.clear();
							}
						}
					}catch(Exception e){
						LOG.error("消息清理器执行异常！", e);
					}
				}
			}
		});
		
		schedulingRecordThread.start();
		LOG.info("消息清理器线程启动！");
		
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		
	}

}
