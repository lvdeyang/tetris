package com.sumavision.tetris.omms.software.service.type;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sumavision.tetris.omms.software.service.deployment.ServiceDeploymentDAO;
import com.sumavision.tetris.omms.software.service.deployment.ServiceDeploymentPO;
import com.sumavision.tetris.omms.software.service.installation.InstallationPackageDAO;
import com.sumavision.tetris.omms.software.service.installation.InstallationPackagePO;
import com.sumavision.tetris.omms.software.service.installation.PropertiesDAO;
import com.sumavision.tetris.omms.software.service.installation.PropertiesPO;
import com.sumavision.tetris.omms.software.service.type.exception.DeploymentNotNullException;
import com.sumavision.tetris.omms.software.service.type.exception.NoChoiceServerTypeException;
import com.sumavision.tetris.omms.software.service.type.exception.NoGroupTyprOrNameException;
import com.sumavision.tetris.omms.software.service.type.exception.NoServiceTypesToAddException;

@Service
@Transactional(rollbackFor = Exception.class)
public class ServiceTypeService {

	@Autowired
	private ServiceTypeDAO serviceTypeDao;
	
	@Autowired
	private InstallationPackageDAO installationPackageDAO;
	
	@Autowired
	private PropertiesDAO propertiesDAO;
	
	@Autowired
	private ServiceDeploymentDAO serviceDeploymentDAO;
	
	/**
	 * 一键创建服务类型<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年1月20日 下午4:43:48
	 * @return List<ServiceTypeVO> 创建的服务类型列表
	 */
	public List<OmmsSoftwareServiceTypeTreeNodeVO> oneButtonCreate() throws Exception{
		ServiceType[] values = ServiceType.values();
		List<ServiceTypePO> entities = serviceTypeDao.findAll();
		List<ServiceTypePO> needAdd = new ArrayList<ServiceTypePO>();
		for(ServiceType value:values){
			boolean exist = false;
			for(ServiceTypePO entity:entities){
				if(value.equals(entity.getServiceType())){
					exist = true;
					break;
				}
			}
			if(!exist){
				ServiceTypePO serviceType = new ServiceTypePO();
				serviceType.setName(value.getName());  //前端传name
				serviceType.setServiceType(value);
				serviceType.setInstallationDirectory(value.getInstallationDirectory());
				serviceType.setInstallScript(value.getInstallScript());
				serviceType.setInstallScriptPath(value.getInstallScriptPath());
				serviceType.setStartupScript(value.getStartupScript());
				serviceType.setStartupScriptPath(value.getStartupScriptPath());
				serviceType.setShutdownScript(value.getShutdownScript());
				serviceType.setShutdownScriptPath(value.getShutdownScriptPath());
				serviceType.setLogFile(value.getLogFile());
				serviceType.setGroupType(value.getGroupType());
				needAdd.add(serviceType);
			}
		}
		
		if(needAdd.size() <= 0){
			throw new NoServiceTypesToAddException();
		}else{
			serviceTypeDao.save(needAdd);
			List<OmmsSoftwareServiceTypeTreeNodeVO> nodes = new ArrayList<OmmsSoftwareServiceTypeTreeNodeVO>();
			for(ServiceTypePO serviceType:needAdd){
				nodes.add(new OmmsSoftwareServiceTypeTreeNodeVO().set(serviceType));
			}
			Collections.sort(nodes, new Comparator<OmmsSoftwareServiceTypeTreeNodeVO>(){
				@Override
				public int compare(OmmsSoftwareServiceTypeTreeNodeVO o1, OmmsSoftwareServiceTypeTreeNodeVO o2) {
					return o1.getName().compareTo(o2.getName());
				}
			});
			return nodes;
		}
	}
	
	/**
	 * 保存服务类型字段<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月10日 上午11:19:14
	 * @param Long id 服务类型id
	 * @param String columnKey 字段key
	 * @param String columnValue 字段值
	 */
	public void saveColumn(Long id, String columnKey, String columnValue) throws Exception{
		ServiceTypePO entity = serviceTypeDao.findOne(id);
		if("name".equals(columnKey)){
			entity.setName(columnValue);
		}else if("installationDirectory".equals(columnKey)){
			entity.setInstallationDirectory(columnValue);
		}else if("installScript".equals(columnKey)){
			entity.setInstallScript(columnValue);
		}else if("installScriptPath".equals(columnKey)){
			entity.setInstallScriptPath(columnValue);
		}else if("startupScript".equals(columnKey)){
			entity.setStartupScript(columnValue);
		}else if("startupScriptPath".equals(columnKey)){
			entity.setStartupScriptPath(columnValue);
		}else if("shutdownScript".equals(columnKey)){
			entity.setShutdownScript(columnValue);
		}else if("shutdownScriptPath".equals(columnKey)){
			entity.setShutdownScriptPath(columnValue);
		}else if("logFile".equals(columnKey)){
			entity.setLogFile(columnValue);
		}
		serviceTypeDao.save(entity);
	}
	
	/**
	 * 删除服务类型(已部署服务情况下不可删除)<br/>
	 * <b>作者:</b>lqxuhv<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年9月4日 上午11:13:36
	 * @param id 服务类型id
	 */
	public void delete(Long id) throws Exception{
		List<ServiceDeploymentPO> serviceDeploymentPOs = serviceDeploymentDAO.findByServiceTypeId(id);
		if (serviceDeploymentPOs !=null && !serviceDeploymentPOs.isEmpty()) {
			throw new DeploymentNotNullException();
		} else {
			ServiceTypePO serviceTypePO = serviceTypeDao.findOne(id);
			List<InstallationPackagePO> installationPackagePOs = installationPackageDAO.findByServiceTypeId(id);
			for (InstallationPackagePO installationPackagePO : installationPackagePOs) {
				List<PropertiesPO> propertiesPOs  = propertiesDAO.findByInstallationPackageId(installationPackagePO.getId());
				for (PropertiesPO propertiesPO : propertiesPOs) {
					if (propertiesPO != null) {
						propertiesDAO.delete(propertiesPO);
					}
				}
				if(installationPackagePO != null){
					installationPackageDAO.delete(installationPackagePO);
				}
			}
			for (ServiceDeploymentPO serviceDeploymentPO : serviceDeploymentPOs) {
				if (serviceDeploymentPO != null) {
					serviceDeploymentDAO.delete(serviceDeploymentPO);
				}
			}
			if(serviceTypePO != null){
				serviceTypeDao.delete(serviceTypePO);
			}
		}
	}	
	
	/**
	 * 添加服务类型<br/>
	 * <b>作者:</b>lqxuhv<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年9月4日 上午11:14:18
	 * @param name 服务类型名称
	 * @param groupType 服务类型枚举
	 * @return List<OmmsSoftwareServiceTypeTreeNodeVO> 服务类型数据 
	 */
	public List<OmmsSoftwareServiceTypeTreeNodeVO> createServer(
			String name,
			String groupType) throws Exception{
		if(groupType == null || name.equals("")){
			throw new NoGroupTyprOrNameException();
		}else{
			GroupType[] values = GroupType.values();
			GroupType groupTypeValue = null;
			for(GroupType value:values){
				if(value.getName().equals(groupType)){
					groupTypeValue = value;
				}
			}
			ServiceTypePO serviceTypePO =new ServiceTypePO();
			serviceTypePO.setName(name);
			serviceTypePO.setGroupType(groupTypeValue);
			serviceTypeDao.save(serviceTypePO);
			List<OmmsSoftwareServiceTypeTreeNodeVO> nodes = new ArrayList<OmmsSoftwareServiceTypeTreeNodeVO>();
			nodes.add(new OmmsSoftwareServiceTypeTreeNodeVO().set(serviceTypePO));
			return nodes;
		    }
		}
	/**
	 * 更改服务类型<br/>
	 * <b>作者:</b>lqxuhv<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年9月4日 下午7:08:52
	 * @param id 服务id
	 * @param groupType 服务类型
	 * @return OmmsSoftwareServiceTypeTreeNodeVO 服务
	 */
	public OmmsSoftwareServiceTypeTreeNodeVO editServer(Long id,String groupType) throws Exception{
		if(groupType == null || groupType.equals("")){
			throw new NoChoiceServerTypeException();
		}else{
			ServiceTypePO serviceTypePO = serviceTypeDao.findOne(id);
			GroupType[] values = GroupType.values();
			GroupType groupTypeValue = null;
			for (GroupType value : values) {
				if(value.getName().equals(groupType)){
					groupTypeValue = value;
				}
			}
			serviceTypePO.setGroupType(groupTypeValue);
			serviceTypeDao.save(serviceTypePO);
			return new OmmsSoftwareServiceTypeTreeNodeVO().set(serviceTypePO);
		}
	}
		
}
