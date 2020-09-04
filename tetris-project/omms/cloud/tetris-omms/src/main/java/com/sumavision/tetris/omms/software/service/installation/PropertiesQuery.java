package com.sumavision.tetris.omms.software.service.installation;

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
import com.sumavision.tetris.omms.software.service.installation.history.InstallationPackageHistoryPO;
import com.sumavision.tetris.omms.software.service.installation.history.InstallationPackageHistoryVO;

@Component
public class PropertiesQuery {
	
	@Autowired
	public PropertiesDAO propertiesDAO;

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
		Pageable page = new PageRequest(currentPage-1, pageSize);
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
		return PropertiesVO.getConverter(PropertiesVO.class).convert(propertyEntities, PropertiesVO.class);
	}
	
}
