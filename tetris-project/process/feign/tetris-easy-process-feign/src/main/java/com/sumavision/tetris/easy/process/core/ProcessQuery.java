package com.sumavision.tetris.easy.process.core;

import java.util.Collection;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.sumavision.tetris.mvc.ext.response.parser.JsonBodyResponseParser;

@Component
public class ProcessQuery {

	@Autowired
	private ProcessFeign processFeign;
	
	/**
	 * 根据流程id查询流程<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年7月18日 上午9:33:53
	 * @param Long id 流程id
	 * @return ProcessVO 流程
	 */
	public ProcessVO findById(Long id) throws Exception{
		return JsonBodyResponseParser.parseObject(processFeign.findById(id), ProcessVO.class);
	}
	
	/**
	 * 根据id查询流程bpmn文件<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年7月18日 上午9:39:00
	 * @param Long id 流程id
	 * @return String bpmn文件
	 */
	public String findBpmnById(Long id) throws Exception{
		return JsonBodyResponseParser.parseObject(processFeign.findBpmnById(id), String.class);
	}
	
	/**
	 * 分页查询流程<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月24日 下午5:31:49
	 * @param int currentPage 当前页码
	 * @param int pageSize 每页数据量
	 * @return int total 总数据量
	 * @return List<ProcessVO> rows 流程列表
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> findByCompanyId(int currentPage, int pageSize) throws Exception{
		return JsonBodyResponseParser.parseObject(processFeign.list(currentPage, pageSize), Map.class);
	}
	
	/**
	 * 分页查询流程（带例外）<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月24日 下午5:31:49
	 * @param int currentPage 当前页码
	 * @param int pageSize 每页数据量
	 * @param Collection<Long> except 例外流程id列表
	 * @return int total 总数据量
	 * @return List<ProcessVO> 流程列表
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> findByCompanyIdWithExcept(int currentPage, int pageSize, Collection<Long> except) throws Exception{
		return JsonBodyResponseParser.parseObject(processFeign.listWithExcept(currentPage, pageSize, JSON.toJSONString(except)), Map.class);
	}
	
}
