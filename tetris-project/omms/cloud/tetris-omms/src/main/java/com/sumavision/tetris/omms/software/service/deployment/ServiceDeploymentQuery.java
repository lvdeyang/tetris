package com.sumavision.tetris.omms.software.service.deployment;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.omms.software.service.installation.BackupInformationDAO;
import com.sumavision.tetris.omms.software.service.installation.BackupInformationPO;
import com.sumavision.tetris.omms.software.service.installation.BackupInformationVO;
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
	
	@Autowired
	public ProcessDeploymentDAO processDeploymentDAO;
	
	@Autowired
	public BackupInformationDAO backupInformationDAO;
	
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
		List<ServiceDeploymentVO> rows = new ArrayList<ServiceDeploymentVO>();
		Set<Long> installlationPackageIds = new HashSet<Long>();
		Set<Long> serviceTypeIds = new HashSet<Long>();
		
		List<Long> deploymentIds = new ArrayList<Long>();
		
 		for (ServiceDeploymentPO serviceDeploymentPO : entities) {
			ServiceDeploymentVO row = new ServiceDeploymentVO().set(serviceDeploymentPO); 
			rows.add(row);
			
			deploymentIds.add(serviceDeploymentPO.getId());
			
			Long installationId = serviceDeploymentPO.getInstallationPackageId();
			installlationPackageIds.add(installationId);
			Long serviceTypeId = serviceDeploymentPO.getServiceTypeId();
			serviceTypeIds.add(serviceTypeId);
		}
 		List<InstallationPackagePO> installationPackagePOs = installationPackageDao.findByIdIn(installlationPackageIds);
 		List<ServiceTypePO> serviceTypePOs = serviceTypeDao.findByIdIn(serviceTypeIds);
 		
 		List<ProcessDeploymentPO> processList = processDeploymentDAO.findByServiceDeploymentIdIn(deploymentIds);
 		for (ServiceDeploymentVO row : rows) {
 			InstallationPackagePO installationPackagePO = null;
			for (InstallationPackagePO installationPackagePO1 : installationPackagePOs) {
				if(row.getInstallationPackageId() == installationPackagePO1.getId()){
					installationPackagePO = installationPackagePO1;
					break;
				}
			}
			ServiceTypePO serviceTypePO = null;
			for (ServiceTypePO serviceTypePO1 : serviceTypePOs) {
				if(row.getServiceTypeId() == serviceTypePO1.getId()){
					serviceTypePO = serviceTypePO1;
					break;
				}
			}
			row.setName(serviceTypePO.getName())
			.setInstallationDirectory(serviceTypePO.getInstallationDirectory())
			.setInstallScriptPath(serviceTypePO.getInstallScriptPath())
			.setLogFile(serviceTypePO.getLogFile())
			.setFileName(installationPackagePO.getFilePath())
			.setVersion(installationPackagePO.getVersion());
			
			List<ProcessDeploymentVO> subprocessList = new ArrayList<ProcessDeploymentVO>();
			for (ProcessDeploymentPO processDeploymentPO : processList) {
				if(processDeploymentPO.getServiceDeploymentId().equals(row.getId())){
					subprocessList.add(new ProcessDeploymentVO().set(processDeploymentPO));
				}
			}
			row.setProcessDeployments(subprocessList);
		}
//		List<ServiceDeploymentVO> rows = ServiceDeploymentVO.getConverter(ServiceDeploymentVO.class).convert(entities, ServiceDeploymentVO.class);
		int total = serviceDeploymentDao.countByServerId(serverId);
		
		return new HashMapWrapper<String,Object>().put("total", total)
												.put("rows", rows)
												.getMap();
	}
	
	/**
	 * 根据部署id查询所有备份信息<br/>
	 * <b>作者:</b>jiajun<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年10月16日 下午4:35:00
	 * @param deploymentId 部署id
	 * @return BackupInformationVO的集合
	 */
	public List<BackupInformationVO> findBackup(Long deploymentId) throws Exception{
		List<BackupInformationPO> list = backupInformationDAO.findByDeploymentId(deploymentId);
		return BackupInformationVO.getConverter(BackupInformationVO.class).convert(list, BackupInformationVO.class);
	}

}
