package com.sumavision.tetris.spring.eureka.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.eureka.server.event.EurekaInstanceCanceledEvent;
import org.springframework.cloud.netflix.eureka.server.event.EurekaInstanceRegisteredEvent;
import org.springframework.cloud.netflix.eureka.server.event.EurekaInstanceRenewedEvent;
import org.springframework.cloud.netflix.eureka.server.event.EurekaRegistryAvailableEvent;
import org.springframework.cloud.netflix.eureka.server.event.EurekaServerStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.netflix.appinfo.InstanceInfo;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.spring.eureka.config.server.ServerProps;

@Component
public class ApplicationStatusChangeListener {

	@Autowired
	private ApplicationService applicationService;
	
	@Autowired
	private ServerProps serverProps;
	
	/**
	 * 服务实例离线<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月23日 上午11:15:22
	 */
	@EventListener
    public void listen(EurekaInstanceCanceledEvent eurekaInstanceCanceledEvent){
        String instanceId = eurekaInstanceCanceledEvent.getServerId();
        applicationService.down(instanceId);
    }
 
	/**
	 * 微服务实例注册<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月23日 上午11:15:43
	 */
    @EventListener
    public void listen(EurekaInstanceRegisteredEvent event){
        InstanceInfo instanceInfo = event.getInstanceInfo();
        try{
        	applicationService.create(
            		instanceInfo.getAppName(), 
            		instanceInfo.getInstanceId(), 
            		instanceInfo.getIPAddr(), 
            		String.valueOf(instanceInfo.getPort()), 
            		String.valueOf(instanceInfo.getSecurePort()));
        }catch(Exception e){
        	applicationService.up(instanceInfo.getInstanceId());
        }
    }
 
    /**
     * 微服务实例心跳<br/>
     * <b>作者:</b>lvdeyang<br/>
     * <b>版本：</b>1.0<br/>
     * <b>日期：</b>2019年12月23日 上午11:16:10
     */
    @EventListener
    public void listen(EurekaInstanceRenewedEvent event){}
 
    /**
     * eureka启动在start event之前,表示eureka注册可用<br/>
     * <b>作者:</b>lvdeyang<br/>
     * <b>版本：</b>1.0<br/>
     * <b>日期：</b>2019年12月23日 上午11:16:35
     */
    @EventListener
    public void listen(EurekaRegistryAvailableEvent event){}
 
    /**
     * eureka启动完成<br/>
     * <b>作者:</b>lvdeyang<br/>
     * <b>版本：</b>1.0<br/>
     * <b>日期：</b>2019年12月23日 上午11:17:37
     */
    @EventListener
    public void listen(EurekaServerStartedEvent event){
    	try{
        	applicationService.create(
    			serverProps.getId().toUpperCase(), 
        		new StringBufferWrapper().append(serverProps.getIp()).append(":").append(serverProps.getId().toLowerCase()).append(":").append(serverProps.getPort()).toString(), 
        		serverProps.getIp(), 
        		serverProps.getPort(), 
        		"443");
        }catch(Exception e){}
    }
	
}
