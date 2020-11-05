package com.sumavision.tetris.spring.eureka.application;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ApplicationQuery {

	@Autowired
	private ApplicationDAO applicationDao;
	
	/**
	 * 查询所有微服务实例<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月23日 下午4:37:37
	 * @return List<ApplicationVO> 实例列表
	 */
	public List<ApplicationVO> findAll() throws Exception{
		List<ApplicationPO> entities = applicationDao.findAll();
		List<ApplicationVO> applications = ApplicationVO.getConverter(ApplicationVO.class).convert(entities, ApplicationVO.class);
		return applications;
	}
	
	/**
	 * 根据微服务实例查询服务节点<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月30日 下午1:45:40
	 * @param String instanceId 微服务实例id
	 * @return ApplicationVO 微服务节点信息
	 */
	public ApplicationVO findByInstanceId(String instanceId) throws Exception{
		ApplicationPO entity = applicationDao.findByInstanceId(instanceId);
		return new ApplicationVO().set(entity);
	}
	
}
