package com.sumavision.tetris.bvc.listener;

import java.util.List;
import java.util.Map;

import javax.jdo.annotations.Transactional;
import javax.persistence.EntityManagerFactory;
import javax.servlet.ServletContextEvent;
import javax.servlet.annotation.WebListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.sumavision.bvc.device.monitor.record.MonitorRecordPO;
import com.sumavision.bvc.device.monitor.record.MonitorRecordService;
import com.sumavision.tetris.bvc.business.group.GroupPO;
import com.sumavision.tetris.bvc.business.group.GroupService;
import com.sumavision.tetris.mvc.listener.ServletContextListener;

@WebListener
public class MainListener extends ServletContextListener {

	private static final Logger LOG = LoggerFactory.getLogger(MainListener.class);

	@Override
	public void contextInitialized(ServletContextEvent event) {

		ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(event.getServletContext());

		//会议的调度
		final GroupService groupService = ctx.getBean(GroupService.class);

		Thread groupSchedulingThread = new Thread(new Runnable() {
			@Override
			public void run() {
				EntityManagerFactory entityManagerFactory = (EntityManagerFactory)ctx.getBean("baseEntityManagerFactory");
				
				while (true) {
					
					
					try {
						Thread.sleep(GroupPO.SCHEDULING_INTERVAL);
					} catch (Exception e) {
						LOG.error("预约会议执行线程被打断！", e);
					}
					
					Map<String, List<GroupPO>> needDisposeRecords = groupService.saveVersion();
					
					boolean participate = HibernateUtil.bindSessionForThread(entityManagerFactory);
					
					try {
						groupService.orderGroupScheduling(needDisposeRecords);
					} catch (Exception e) {
						LOG.error("预约会议执行异常！", e);
					}finally {
						HibernateUtil.closeSession(participate, entityManagerFactory);
					}
				}
				
			}
		});

		groupSchedulingThread.start();
		LOG.info("预约会议线程启动！");

		//录制的调度
		final MonitorRecordService recordService = ctx.getBean(MonitorRecordService.class);

		Thread schedulingRecordThread = new Thread(new Runnable() {
			@Override
			public void run() {
				EntityManagerFactory entityManagerFactory = (EntityManagerFactory)ctx.getBean("baseEntityManagerFactory");
				
				while (true) {
					try {
						Thread.sleep(MonitorRecordPO.SCHEDULING_INTERVAL);
					} catch (Exception e) {
						LOG.error("排期录制线程被打断！", e);
					}
					
					Map<String, List<MonitorRecordPO>> needDisposeRecords = recordService.saveVersion();
					
					boolean participate = HibernateUtil.bindSessionForThread(entityManagerFactory);
					
					try {
						recordService.doScheduling(needDisposeRecords);
						//recordService.scheduling();
						//LOG.info("排期录制被暂时关闭！");
					} catch (Exception e) {
						LOG.error("排期录制执行异常！", e);
					}finally {
						HibernateUtil.closeSession(participate, entityManagerFactory);
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
