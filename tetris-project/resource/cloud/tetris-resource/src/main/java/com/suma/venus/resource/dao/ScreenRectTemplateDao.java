package com.suma.venus.resource.dao;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;

import com.suma.venus.resource.pojo.ScreenRectTemplatePO;

/**
 * 屏信息模板
 * @author lxw
 *
 */
@RepositoryDefinition(domainClass = ScreenRectTemplatePO.class,idClass = Long.class)
public interface ScreenRectTemplateDao extends CommonDao<ScreenRectTemplatePO>{

	public List<ScreenRectTemplatePO> findByDeviceModel(String deviceModel);
	
	@Query("select po.screenId from ScreenRectTemplatePO po where po.deviceModel=?1")
	public Set<String> findScreenIdsByDeviceModel(String deviceModel);
	
	public List<ScreenRectTemplatePO> findByScreenId(String screenId);
	
	public List<ScreenRectTemplatePO> findByDeviceModelAndScreenId(String deviceModel,String screenId);
	
	public ScreenRectTemplatePO findByDeviceModelAndScreenIdAndRectId(String deviceModel,String screenId,String rectId);
	
}
