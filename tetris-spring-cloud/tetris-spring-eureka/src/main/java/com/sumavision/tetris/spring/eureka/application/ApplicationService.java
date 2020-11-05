package com.sumavision.tetris.spring.eureka.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class ApplicationService {

	@Autowired
	private ApplicationDAO applicationDao;
	
	/**
	 * 创建新的服务实例<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月23日 上午11:51:50
	 */
	public void create(
			String name, 
			String instanceId, 
			String ip, 
			String port, 
			String securePort){
		ApplicationPO application = new ApplicationPO();
		application.setName(name);
		application.setInstanceId(instanceId);
		application.setIp(ip);
		application.setPort(port);
		application.setSecurePort(securePort);
		application.setStatus(ApplicationStatus.UP);
		application.setGadgetPort("8910");
		applicationDao.save(application);
	}
	
	/**
	 * 微服务实例上线<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月23日 上午11:41:38
	 * @param String instanceId 实例id
	 */
	public void up(String instanceId){
		ApplicationPO application = applicationDao.findByInstanceId(instanceId);
		if(application != null){
			application.setStatus(ApplicationStatus.UP);
			applicationDao.save(application);
		}
	}
	
	/**
	 * 微服务实例离线<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月23日 上午11:43:12
	 * @param String instanceId 实例id
	 */
	public void down(String instanceId){
		ApplicationPO application = applicationDao.findByInstanceId(instanceId);
		if(application != null){
			application.setStatus(ApplicationStatus.DOWN);
			applicationDao.save(application);
		}
	}
	
}
