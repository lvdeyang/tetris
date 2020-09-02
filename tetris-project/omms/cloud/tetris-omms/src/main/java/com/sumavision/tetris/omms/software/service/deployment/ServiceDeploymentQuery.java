package com.sumavision.tetris.omms.software.service.deployment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ServiceDeploymentQuery {

	@Autowired
	private ServiceDeploymentDAO serviceDeploymentDao;
	
	/**
	 * 查询状态<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年9月1日 上午10:26:33
	 * @param Long serviceDeploymentId 部署id
	 * @return ServiceDeploymentVO 状态
	 */
	public ServiceDeploymentVO queryUploadStatus(Long serviceDeploymentId) throws Exception{
	
		ServiceDeploymentPO deploymentEntity = serviceDeploymentDao.findOne(serviceDeploymentId);
		return new ServiceDeploymentVO().set(deploymentEntity);
	}
	
}
