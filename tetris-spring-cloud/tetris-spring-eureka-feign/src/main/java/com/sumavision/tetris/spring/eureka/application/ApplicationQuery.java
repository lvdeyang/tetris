package com.sumavision.tetris.spring.eureka.application;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sumavision.tetris.mvc.ext.response.parser.JsonBodyResponseParser;

@Component
public class ApplicationQuery {

	@Autowired
	private EurekaFeign.SqlQuery eurekaSqlQuery;
	
	/**
	 * 查询所有微服务实例<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月26日 上午9:29:56
	 * @return List<ApplicationVO> 微服务实例
	 */
	public List<ApplicationVO> findAll() throws Exception{
		return JsonBodyResponseParser.parseArray(eurekaSqlQuery.findAll(), ApplicationVO.class);
	}
	
	/**
	 * 根据实例id查询微服务节点信息<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月30日 下午2:34:20
	 * @param String instanceId 实例id
	 * @return ApplicationVO 微服务节点信息
	 */
	public ApplicationVO findByInstanceId(String instanceId) throws Exception{
		return JsonBodyResponseParser.parseObject(eurekaSqlQuery.findByInstanceId(instanceId), ApplicationVO.class);
	}
	
}
