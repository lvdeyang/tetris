package com.sumavision.tetris.capacity.server;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.sumavision.tetris.capacity.vo.director.OutputsVO;
import com.sumavision.tetris.mvc.ext.response.parser.JsonBodyResponseParser;

@Service
@Transactional(rollbackFor = Exception.class)
public class TransformService {

	@Autowired
	private CapacityFeign capacityFeign;
	
	/**
	 * 添加输出<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月4日 上午8:39:22
	 * @param String taskId
	 * @param String outputParam
	 */
	public void addOutput(String taskId, String outputParam) throws Exception{
		JsonBodyResponseParser.parseObject(capacityFeign.addTransformOutput(taskId, outputParam), null);
	}
	
	/**
	 * 删除输出<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月4日 上午8:39:22
	 * @param String taskId
	 * @param String outputParam
	 */
	public void deleteOutput(String taskId, String outputParam) throws Exception{
		JsonBodyResponseParser.parseObject(capacityFeign.deleteTransformOutput(taskId, outputParam), null);
	}
	
	/**
	 * 删除任务全部输出<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月4日 上午8:40:46
	 * @param String taskId 任务标识
	 */
	public void deleteAllOutput(String taskId) throws Exception{
		JsonBodyResponseParser.parseObject(capacityFeign.deleteTransformAllOutput(taskId), null);
	}
	
}
