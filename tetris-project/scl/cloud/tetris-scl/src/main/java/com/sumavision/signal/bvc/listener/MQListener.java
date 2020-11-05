/**   

* @Title: MQListener.java 

* @Package com.sumavision.bvc.listener 

* @Description: TODO(用一句话描述该文件做什么) 

* @author （作者）  
* @date 2018年6月12日 下午7:09:29 

* @version V1.0   

*/

package com.sumavision.signal.bvc.listener;

import java.io.File;
import java.io.FileNotFoundException;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.util.WebUtils;

import com.suma.venus.message.service.MessageService;
import com.sumavision.signal.bvc.mq.MQCallBackService;
import com.sumavision.signal.bvc.service.HeartBeatService;
import com.sumavision.signal.bvc.service.TaskCancelService;
import com.sumavision.signal.bvc.service.TaskThreadService;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

@WebListener
public class MQListener implements ServletContextListener {
	
	private static ServletContext servletContext;
	
	private static final Logger log = LoggerFactory.getLogger(MQListener.class);

	/*
	 * (非 Javadoc)
	 * 
	 * <p>Title: contextInitialized</p>
	 * 
	 * <p>listener创建 </p>
	 * 
	 * @param sce
	 * 
	 * @see
	 * javax.servlet.ServletContextListener#contextInitialized(javax.servlet.
	 * ServletContextEvent)
	 * 
	 */
	@Override
	public void contextInitialized(ServletContextEvent sce){
		
		servletContext = sce.getServletContext();
		
		//对吗timeOuttimeOut
//		MessageService messageService = new MessageService();
//		if (messageService.getRecoveryNodeId() != null) {
//			messageService.recoveryMessageService(new Callback() {
//				@Override
//				public void execute(Message msg) {
//					log.info("=====================[" + messageService.getRecoveryNodeId()
//							+ "] message callback=======================");
//				}
//			});
//		}
		
		ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(sce
                .getServletContext());
		MessageService messageService = ctx.getBean(MessageService.class);
		MQCallBackService callBackService = ctx.getBean(MQCallBackService.class);
		//默认就把node的名称写入到消息服务的配置里然后只要重启就重新注册
		messageService.recoveryMessageService(callBackService);
		log.info("==============initListener resource finish=================");
		
		HeartBeatService heartBeatService = ctx.getBean(HeartBeatService.class);
		try {
			//heartBeatService.initHeart();
			log.info("==============initListener heartbeat finish=================");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		TaskCancelService taskCancelService = ctx.getBean(TaskCancelService.class);
		try {
			//taskCancelService.initCancelTask();
			log.info("==============initListener taskCancel finish=================");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		TaskThreadService taskThreadService = ctx.getBean(TaskThreadService.class);
		try {
			//taskThreadService.init();
			log.info("==============initListener taskThread finish=================");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}	

	/**
	 * 项目路径<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年11月30日 下午3:33:05
	 */
	@Component
	public static class Path{
		
		/**
		 * 获取webapp路径<br/>
		 * <b>作者:</b>lvdeyang<br/>
		 * <b>版本：</b>1.0<br/>
		 * <b>日期：</b>2018年11月30日 下午2:37:47
		 * @return String webapp 路径
		 */
		public String webappPath() throws FileNotFoundException{
			 return WebUtils.getRealPath(servletContext, "");
		}
		
		/**
		 * WEB-INF路径<br/>
		 * <b>作者:</b>lvdeyang<br/>
		 * <b>版本：</b>1.0<br/>
		 * <b>日期：</b>2019年1月8日 上午11:45:06
		 * @return String WEB-INF路径
		 */
		public String web_inf() throws Exception{
			String webappPath = webappPath();
			return new StringBufferWrapper().append(webappPath)
										    .append(File.separator)
										    .append("WEB-INF")
										    .toString();
		}
		
		/**
		 * 获取webapp下的类路径<br/>
		 * <b>作者:</b>lvdeyang<br/>
		 * <b>版本：</b>1.0<br/>
		 * <b>日期：</b>2019年1月8日 上午11:44:26
		 * @return String webapp下类路径
		 */
		public String webappClassPath() throws Exception{
			String webInfPath = web_inf();
			return new StringBufferWrapper().append(webInfPath)
											.append(File.separator)
										    .append("classes")
										    .toString();
		}
		
		/**
		 * 获取类路径<br/>
		 * <b>作者:</b>lvdeyang<br/>
		 * <b>版本：</b>1.0<br/>
		 * <b>日期：</b>2019年1月8日 下午2:05:16
		 * @return String 类路径
		 */
		public String classPath() throws Exception{
			String path = Thread.currentThread().getContextClassLoader().getResource(File.separator).getPath();
			return new StringBufferWrapper().append(path.split("classes")[0])
											.append("classes")
											.toString();
		}
		
	}
	
	
	@Override
	public void contextDestroyed(ServletContextEvent sce) {

	}
}
