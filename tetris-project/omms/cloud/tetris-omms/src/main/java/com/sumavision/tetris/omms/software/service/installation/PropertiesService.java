package com.sumavision.tetris.omms.software.service.installation;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PropertiesService {

	@Autowired
	private PropertiesDAO propertiesDao;
	
	/**
	 * 添加服务属性<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年8月26日 下午4:50:32
	 * @param Long installationPackageId 安装包id
	 * @param String propertyKey 属性key
	 * @param String propertyName 属性名称
	 * @param String valueType 值类型
	 * @param String propertyDefaultValue 默认值
	 * @return ServicePropertiesVO 服务属性
	 */
	public PropertiesVO add(
			Long installationPackageId,
			String propertyKey,
			String propertyName,
			String valueType,
			String propertyDefaultValue) throws Exception{
		
		PropertiesPO entity = new PropertiesPO();
		entity.setUpdateTime(new Date());
		entity.setInstallationPackageId(installationPackageId);
		entity.setPropertyKey(propertyKey);
		entity.setPropertyName(propertyName);
		entity.setValueType(PropertyValueType.fromName(valueType));
		entity.setPropertyDefaultValue(propertyDefaultValue);
		propertiesDao.save(entity);
		
		return new PropertiesVO().set(entity);
	}
	
	/**
	 * 修改服务属性<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年8月26日 下午4:53:42
	 * @param Long id 属性id
	  * @param String propertyKey 属性key
	 * @param String propertyName 属性名称
	 * @param String valueType 值类型
	 * @param String propertyDefaultValue 默认值
	 * @return ServicePropertiesVO 服务属性
	 */
	public PropertiesVO edit(
			Long id,
			String propertyKey,
			String propertyName,
			String valueType,
			String propertyDefaultValue) throws Exception{
		
		PropertiesPO properties = propertiesDao.findOne(id);
		properties.setPropertyKey(propertyKey);
		properties.setPropertyName(propertyName);
		properties.setValueType(PropertyValueType.fromName(valueType));
		properties.setPropertyDefaultValue(propertyDefaultValue);
		propertiesDao.save(properties);
		
		return new PropertiesVO().set(properties);
	}
	
	/**
	 * 删除服务属性<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年8月26日 下午4:55:01
	 * @param Long id 属性id
	 */
	public void remove(Long id) throws Exception{
		PropertiesPO properties = propertiesDao.findOne(id);
		if(properties != null){
			propertiesDao.delete(properties);
		}
	}
	
	/**
	 *删除安装包版本参数<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>lqxuhv<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年9月1日 下午1:24:49
	 * @param Long 属性id
	 */
	public void delete(Long id) {
		PropertiesPO propertiesPO = propertiesDao.findOne(id);
		if (propertiesPO != null) {
			propertiesDao.delete(propertiesPO);
		}
	}
	
	/**
	 * 编辑安装包版本参数<br/>
	 * <b>作者:</b>lqxuhv<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年9月1日 下午6:29:14
	 * @param id 属性id
	 * @param String propertyKey 属性key
	 * @param String propertyName 属性名称
	 * @param String valueType 值类型
	 * @param String propertyDefaultValue 默认值
	 * @param valueSelect 枚举项
	 * @return PropertiesVO 版本参数
	 */
	public PropertiesVO editProperty(
			Long id,
			String propertyKey,
			String propertyName,
			String valueType,
			String propertyDefaultValue,
			String valueSelect) throws Exception{
		
		PropertiesPO properties = propertiesDao.findOne(id);
		properties.setPropertyKey(propertyKey);
		properties.setPropertyName(propertyName);
		properties.setValueType(PropertyValueType.fromName(valueType));
		properties.setPropertyDefaultValue(propertyDefaultValue);
		properties.setValueSelect(valueSelect);
		propertiesDao.save(properties);
		
		return new PropertiesVO().set(properties);
	}
	
	/**
	 * 添加安装包版本参数<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>lqxuhv<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年9月1日 下午6:32:44
	 * @param installationPackageId 安装包id
	 * @param String propertyKey 属性key
	 * @param String propertyName 属性名称
	 * @param String valueType 值类型
	 * @param String propertyDefaultValue 默认值
	 * @param valueSelect 枚举项
	 * @return PropertiesVO 版本参数
	 */
	public PropertiesVO addProperty(
			Long installationPackageId,
			String propertyKey,
			String propertyName,
			String valueType,
			String propertyDefaultValue,
			String valueSelect) throws Exception{
		
		PropertiesPO entity = new PropertiesPO();
		entity.setUpdateTime(new Date());
		entity.setInstallationPackageId(installationPackageId);
		entity.setPropertyKey(propertyKey);
		entity.setPropertyName(propertyName);
		entity.setValueType(PropertyValueType.fromName(valueType));
		entity.setPropertyDefaultValue(propertyDefaultValue);
		entity.setValueSelect(valueSelect);
		propertiesDao.save(entity);
		
		return new PropertiesVO().set(entity);
	}
	
}
