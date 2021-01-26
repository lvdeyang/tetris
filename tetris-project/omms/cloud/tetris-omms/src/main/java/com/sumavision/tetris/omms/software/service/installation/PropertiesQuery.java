package com.sumavision.tetris.omms.software.service.installation;

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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.omms.software.service.deployment.ServiceDeploymentDAO;
import com.sumavision.tetris.omms.software.service.deployment.ServiceDeploymentPO;

@Component
public class PropertiesQuery {
	
	@Autowired
	private PropertiesDAO propertiesDAO;
	
	@Autowired
	private ServiceDeploymentDAO serviceDeploymentDAO;
	
	@Autowired
	private InstallationPackageDAO installationPackageDAO; 

	/**
	 * 查询服务属性值类型<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年8月26日 上午11:06:44
	 * @return Set<String> 值类型列表
	 */
	public Set<String> findValueTypes(){
		Set<String> values = new HashSet<String>();
		PropertyValueType[] types = PropertyValueType.values();
		for(PropertyValueType type:types){
			values.add(type.getName());
		}
		return values;
	}
	
	/**
	 * 根据安装包id查询参数<br/>
	 * <b>作者:</b>lqxuhv<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年9月1日 下午6:36:14
	 * @param installationPackageId 安装包id
	 * @return Map<String, Object> 安装包参数
	 */
	public Map<String, Object> load(
			Long installationPackageId,
			int currentPage, 
			int pageSize) throws Exception {
		Pageable page = PageRequest.of(currentPage-1, pageSize);
		Page<PropertiesPO> paged = propertiesDAO.findByInstallationPackageId(installationPackageId, page);
		List<PropertiesPO> entities = paged.getContent();
		List<PropertiesVO> rows = PropertiesVO.getConverter(PropertiesVO.class).convert(entities, PropertiesVO.class);
		int total = propertiesDAO.countByInstallationPackageId(installationPackageId);
		
		return new HashMapWrapper<String,Object>().put("total", total)
												  .put("rows", rows)
												  .getMap();
	}
	
	/**
	 * 根据安装包查询参数<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年9月3日 下午7:18:38
	 * @param Long installationPackageId 安装包id
	 * @return List<PropertiesVO> 参数列表
	 */
	public List<PropertiesVO> findByInstallationPackageId(Long installationPackageId) throws Exception{
		List<PropertiesPO> propertyEntities =  propertiesDAO.findByInstallationPackageId(installationPackageId);
		List<PropertiesPO> list = new ArrayList<PropertiesPO>();
		for (PropertiesPO propertiesPO : propertyEntities) {
			if(propertiesPO.getValueType().equals(PropertyValueType.DBIP)){
				continue;
			}else{
				if(propertiesPO.getValueType().equals(PropertyValueType.DBPORT)){
					for (PropertiesPO propertiesPOip : propertyEntities) {
						if(propertiesPO.getRef().equals(propertiesPOip.getPropertyKey())){
							StringBufferWrapper valueBufferWrapper = new StringBufferWrapper().append(propertiesPOip.getPropertyDefaultValue())
                                    .append(":")
                                    .append(propertiesPO.getPropertyDefaultValue());
							propertiesPO.setPropertyDefaultValue(valueBufferWrapper.toString());
						}
					}
				}	
				list.add(propertiesPO);
			}
		}
		return PropertiesVO.getConverter(PropertiesVO.class).convert(list, PropertiesVO.class);
	}
	
	/**
	 * 根据部署id查询参数<br/>
	 * <b>作者:</b>jiajun<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月2日 下午2:12:26
	 * @param deploymentId 部署id
	 * @return
	 * @throws Exception
	 */
	public List<PropertiesVO> findByDeploymentId(Long deploymentId) throws Exception{
		ServiceDeploymentPO deployment = serviceDeploymentDAO.findById(deploymentId);
		InstallationPackagePO installationPackage = installationPackageDAO.findById(deployment.getInstallationPackageId());
		List<PropertiesPO> packagePropertiesList = propertiesDAO.findByInstallationPackageId(installationPackage.getId());
		List<PropertiesVO> list = PropertiesVO.getConverter(PropertiesVO.class).convert(packagePropertiesList, PropertiesVO.class);
		
		String config = deployment.getConfig();
		JSONObject jsonObject = JSON.parseObject(config);
		Set<String> propertyKeys = jsonObject.keySet();
		
		for (PropertiesVO propertiesVO : list) {
			String propertyKey = propertiesVO.getPropertyKey();
			for (String key : propertyKeys) {
				if(propertyKey.equals(key)){
					propertiesVO.setPropertyValue(jsonObject.getString(key));
					break;
				}
			}
		}
		
		List<PropertiesVO> resList = new ArrayList<PropertiesVO>();
		for (PropertiesVO propertiesVO : list) {
			String propertyKey = propertiesVO.getPropertyKey();
			if("databaseAddr".equals(propertyKey) || "databaseport".equals(propertyKey)){
				continue;
			}else{
				resList.add(propertiesVO);
			}
		}
		return resList;
		
		/*ServiceDeploymentPO deployment = serviceDeploymentDAO.findOne(deploymentId);
		String config = deployment.getConfig();
		JSONObject jsonObject = JSON.parseObject(config);
		List<PropertiesVO> list = new ArrayList<PropertiesVO>();
		Set<String> propertyKeys = jsonObject.keySet();
		for (String propertyKey : propertyKeys) {
			if("databaseAddr".equals(propertyKey) || "databaseport".equals(propertyKey)){
				continue;
			}
			String propertyValue = jsonObject.getString(propertyKey);
			PropertiesVO properties = new PropertiesVO();
			properties.setPropertyKey(propertyKey);
			properties.setPropertyValue(propertyValue);
			List<PropertiesPO> propertiesPOList = propertiesDAO.findByPropertyKey(propertyKey);
			properties.setPropertyName(propertiesPOList.get(0).getPropertyName());
			list.add(properties);
		}
		return list;*/
	}
	
	/**
	 * 
	 * 查询升级参数<br/>
	 * <b>作者:</b>jiajun<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月4日 下午2:00:12
	 * @param installationPackageId 安装包id
	 * @param deploymentId 部署id
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public List<PropertiesVO> findUpdateParameters(Long installationPackageId, Long deploymentId) throws Exception{
		List<PropertiesPO> packagePropertiesList = propertiesDAO.findByInstallationPackageId(installationPackageId);
		List<PropertiesVO> list = PropertiesVO.getConverter(PropertiesVO.class).convert(packagePropertiesList, PropertiesVO.class);
		
		ServiceDeploymentPO deployment = serviceDeploymentDAO.findById(deploymentId);
		String config = deployment.getConfig();
		JSONObject jsonObject = JSON.parseObject(config);
		Set<String> propertyKeys = jsonObject.keySet();
		
		for (PropertiesVO propertiesVO : list) {
			String propertyKey = propertiesVO.getPropertyKey();
			for (String key : propertyKeys) {
				if(propertyKey.equals(key)){
					propertiesVO.setPropertyValue(jsonObject.getString(key));
					break;
				}
			}
		}
		
		List<PropertiesVO> updatePropertiesList = new ArrayList<PropertiesVO>();
		for (PropertiesVO propertiesVO : list) {
			if(propertiesVO.getValueType().equals(PropertyValueType.DBIP.toString())){
				continue;
			}else{
				if(propertiesVO.getValueType().equals(PropertyValueType.DBPORT.toString())){
					for (PropertiesVO propertiesPOip : list) {
						if(propertiesVO.getRef().equals(propertiesPOip.getPropertyKey())){
							StringBufferWrapper valueBufferWrapper = new StringBufferWrapper().append(propertiesPOip.getPropertyDefaultValue())
                                    .append(":")
                                    .append(propertiesVO.getPropertyDefaultValue());
							propertiesVO.setPropertyDefaultValue(valueBufferWrapper.toString());
						}
					}
				}	
				updatePropertiesList.add(propertiesVO);
			}
		}
		return updatePropertiesList;
	}
	
}
