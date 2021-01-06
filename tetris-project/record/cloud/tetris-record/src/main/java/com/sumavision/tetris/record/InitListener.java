package com.sumavision.tetris.record;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.sumavision.tetris.record.storage.CleanStorageThread;
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

			// TODO 仓库信息 初始化, 后面可以删除
			StorageService storageService = ctx.getBean(StorageService.class);
			storageService.init();

			// TODO 录制设备，状态同步

			// TODO 录制任务 重启后的处理

			// 设置清理定时器，每天凌晨2点执行一次
			long oneDay = 24 * 60 * 60 * 1000;
			long initDelay = getTimeMillis("02:00:00") - System.currentTimeMillis();

			initDelay = initDelay > 0 ? initDelay : oneDay + initDelay;

			CleanStorageThread cleanStorageThread = ctx.getBean(CleanStorageThread.class);

			ScheduledExecutorService timer = Executors.newSingleThreadScheduledExecutor();
			timer.scheduleAtFixedRate(cleanStorageThread, initDelay, oneDay, TimeUnit.MILLISECONDS);

			LOGGER.info("==============record initListener finish=================");

		} catch (Exception e) {
			LOGGER.error("==============record initListener fail=================", e);
		}
	}

	/**
	 * 获取指定时间对应的毫秒数
	 *
	 * @param time "HH:mm:ss"
	 * @return
	 */
	private static long getTimeMillis(String time) {
		try {
			DateFormat dateFormat = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
			DateFormat dayFormat = new SimpleDateFormat("yy-MM-dd");
			Date curDate = dateFormat.parse(dayFormat.format(new Date()) + " " + time);
			return curDate.getTime();
		} catch (ParseException e) {
			LOGGER.error("getTimeMillis catch exception.");
			LOGGER.error("", e);
		}
		return 0;
	}

}
