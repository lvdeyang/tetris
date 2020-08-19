package com.suma.venus.alarmoprlog;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.suma.venus.alarmoprlog.service.alarm.HandleReceiveAlarmThread;
// import com.suma.venus.alarmoprlog.service.receive.MsgReceiveCallBack;
import com.suma.venus.alarmoprlog.service.alarm.InitAlarmInfoService;
// import com.suma.venus.alarmoprlog.service.receive.MsgForwardThread;
// import com.suma.venus.message.service.MessageService;
import com.suma.venus.alarmoprlog.service.oprlog.HandleReceiveOprlogThread;

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

			LOGGER.info("==============initListener alarmInfo start=================");
			InitAlarmInfoService initAlarmInfoService = ctx.getBean(InitAlarmInfoService.class);
			initAlarmInfoService.initAlarmInfo();
			LOGGER.info("==============initListener alarmInfo finish=================");

			// MsgForwardThread msgForwardingThread = ctx.getBean(MsgForwardThread.class);
			// msgForwardingThread.start();
			// LOGGER.info("==============start msgForwardingThread
			// finish=================");

			// MessageService messageService = ctx.getBean(MessageService.class);
			// 默认就把node的名称写入到消息服务的配置里然后只要重启就重新注册
			// messageService.recoveryMessageService(new MsgReceiveCallBack());

			LOGGER.info("==============initListener HandleReceiveAlarmThread start=================");
			HandleReceiveAlarmThread handleReceiveAlarmThread = ctx.getBean(HandleReceiveAlarmThread.class);
			handleReceiveAlarmThread.start();
			LOGGER.info("==============initListener HandleReceiveAlarmThread finish=================");

			LOGGER.info("==============initListener HandleReceiveOprlogThread start=================");
			HandleReceiveOprlogThread handleReceiveOprlogThread = ctx.getBean(HandleReceiveOprlogThread.class);
			handleReceiveOprlogThread.start();
			LOGGER.info("==============initListener HandleReceiveOprlogThread finish=================");

		} catch (Exception e) {
			LOGGER.error("==============initListener fail=================", e);
		}
	}
}
