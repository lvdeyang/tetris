package com.sumavision.tetris.omms.init;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sumavision.tetris.commons.context.AsynchronizedSystemInitialization;
import com.sumavision.tetris.omms.hardware.server.ServerService;
import com.sumavision.tetris.omms.software.service.deployment.ServiceDeploymentService;

@Service
@Transactional(rollbackFor = Exception.class)
public class OmmsInitialization implements AsynchronizedSystemInitialization{

	@Autowired
	private ServerService serverService;
	
	@Autowired
	private ServiceDeploymentService serviceDeploymentService;
	
	@Override
	public int index() {
		return 0;
	}

	@Override
	public void init() {
		Thread timer = new Thread(new Runnable() {
			@Override
			public void run(){
				while(true){
					try{
						serverService.queryStatus();
						Thread.sleep(60 * 1000);
					}catch(Exception e){
						e.printStackTrace();
					}
					
				}
			}
		});
		timer.start();
		
		Thread processStatus = new Thread(new Runnable() {
			@Override
			public void run() {
				while(true){
					try {
						serviceDeploymentService.queryStatus();
						Thread.sleep(60 * 1000);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
		processStatus.start();
	}

}
