/**   

* @Title: MQListener.java 

* @Package com.sumavision.bvc.listener 

* @Description: TODO(用一句话描述该文件做什么) 

* @author （作者）  
* @date 2018年6月12日 下午7:09:29 

* @version V1.0   

*/

package com.sumavision.bvc.listener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.util.WebUtils;

import com.suma.venus.alarmoprlog.client.AlarmOprlogClientService;
import com.suma.venus.message.service.MessageService;
import com.sumavision.bvc.mq.MQCallBackService;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * 
 * 
 * 项目名称：bvc-monitor-ui
 * 
 * 类名称：MQListener
 * 
 * 类描述：
 * 
 * 创建人：cll
 * 
 * 创建时间：2018年6月12日 下午7:09:29
 * 
 * 修改人：cll
 * 
 * 修改时间：2018年6月12日 下午7:09:29
 * 
 * 修改备注：
 * 
 * @version
 *
 * 
 * 
 */
@Slf4j
@WebListener
public class MQListener implements ServletContextListener {
	
	private static ServletContext servletContext;

	@Getter
	@Setter
//	@Value("${spring.application.name}")
	private String name;
	
	@Getter
	@Setter
//	@Value("${spring.cloud.client.ipAddress}")
	private String localIp;

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
	public void contextInitialized(ServletContextEvent sce) {
		
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
		
		String sourceID = messageService.getRecoveryNodeId();
		Properties prop = new Properties();
		try {
			prop.load(MQListener.class.getClassLoader().getResourceAsStream("application.yml"));
			name = prop.getProperty("name");
			localIp = prop.getProperty("ipAddress");
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		log.info("name = " + name + ", sourceID = " + sourceID + ", localIp = " + localIp + ", messageService = " + messageService);
		
		try {
			AlarmOprlogClientService.initClient(name, sourceID, localIp, messageService);
			//订阅“设备离线”告警
			executesubscribeAlarm("11010001", "设备离线");
		} catch (Exception e) {
			e.printStackTrace();
		}		
		log.info("==============initListener alarm finish=================");
		log.info("alarmOprlogClientService.instance = " + AlarmOprlogClientService.getInstance());
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

	private void executesubscribeAlarm(String alarmCode, String alarmName){
		new Thread(null, null, "subscribeAlarm-" + alarmCode){
			@Override
			public void run() {				
				try {
					AlarmOprlogClientService.getInstance().subscribeAlarm(alarmCode);
				} catch (Exception e) {
					e.printStackTrace();
					log.error("告警代码为 " + alarmCode + " 的 " + alarmName + " 告警订阅失败");
				}				
			}
		}.start();
	}
}
