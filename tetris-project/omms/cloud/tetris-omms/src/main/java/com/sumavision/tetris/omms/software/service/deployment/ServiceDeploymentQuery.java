package com.sumavision.tetris.omms.software.service.deployment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.omms.software.service.installation.InstallationPackageDAO;
import com.sumavision.tetris.omms.software.service.installation.InstallationPackagePO;
import com.sumavision.tetris.omms.software.service.type.ServiceTypeDAO;
import com.sumavision.tetris.omms.software.service.type.ServiceTypePO;

@Component
public class ServiceDeploymentQuery {

	@Autowired
	private ServiceDeploymentDAO serviceDeploymentDao;
	
	@Autowired
	public InstallationPackageDAO installationPackageDao;
	
	@Autowired
	public ServiceTypeDAO serviceTypeDao;
	
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
		InstallationPackagePO installPackageEntity = installationPackageDao.findOne(deploymentEntity.getInstallationPackageId());
		ServiceTypePO serviceTypeEntity = serviceTypeDao.findOne(deploymentEntity.getServiceTypeId());
		return new ServiceDeploymentVO().set(deploymentEntity)
										.setVersion(installPackageEntity.getVersion())
										.setName(serviceTypeEntity.getName());
	}
	
	/**
	 * 根据服务器查询部署服务<br/>
	 * <b>作者:</b>lqxuhv<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年9月3日 上午10:12:06
	 * @param serverId 服务器id
	 * @return Map<String, Object> 部署服务参数
	 */
	public Map<String, Object> load(
			Long serverId,
			int currentPage,
			int pageSize)throws Exception{
		Pageable pageable = new PageRequest(currentPage-1, pageSize);
		Page<ServiceDeploymentPO> paged = serviceDeploymentDao.findByServerId(serverId, pageable);
		List<ServiceDeploymentPO> entities = paged.getContent();
		List<InstallationPackagePO> installationPackagePOs = new ArrayList<InstallationPackagePO>();
		List<ServiceTypePO> serviceTypePOs = new ArrayList<ServiceTypePO>();
		List<ServiceDeploymentVO> rows = new ArrayList<ServiceDeploymentVO>();
		for (ServiceDeploymentPO serviceDeploymentPO : entities) {
			ServiceDeploymentVO row = new ServiceDeploymentVO(); 
			InstallationPackagePO installationPackagePO = installationPackageDao.findOne(serviceDeploymentPO.getInstallationPackageId());
			ServiceTypePO serviceTypePO = serviceTypeDao.findOne(serviceDeploymentPO.getServiceTypeId());
			installationPackagePOs.add(installationPackagePO);
			serviceTypePOs.add(serviceTypePO);
			row.setId(serviceDeploymentPO.getId())
			.setServerId(serviceDeploymentPO.getServerId())
			.setServiceTypeId(serviceDeploymentPO.getServiceTypeId())
			.setInstallationPackageId(serviceDeploymentPO.getInstallationPackageId())
			.setInstallFullPath(serviceDeploymentPO.getInstallFullPath())
			.setCreator(serviceDeploymentPO.getCreator())
			.setCreateTime(serviceDeploymentPO.getCreateTime().toString())//问题可能
			.setName(serviceTypePO.getName())
			.setInstallationDirectory(serviceTypePO.getInstallationDirectory())
			.setInstallScriptPath(serviceTypePO.getInstallScriptPath())
			.setLogFile(serviceTypePO.getLogFile())
			.setFileName(installationPackagePO.getFilePath())
			.setVersion(installationPackagePO.getVersion());
			rows.add(row);
		}
//		List<ServiceDeploymentVO> rows = ServiceDeploymentVO.getConverter(ServiceDeploymentVO.class).convert(entities, ServiceDeploymentVO.class);
		int total = serviceDeploymentDao.countByServerId(serverId);
		
		return new HashMapWrapper<String,Object>().put("total", total)
												.put("rows", rows)
												.getMap();
	}
}
