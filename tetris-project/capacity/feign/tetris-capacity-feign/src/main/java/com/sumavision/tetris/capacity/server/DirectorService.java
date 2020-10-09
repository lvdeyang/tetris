package com.sumavision.tetris.capacity.server;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.sumavision.tetris.capacity.vo.director.DirectorTaskVO;
import com.sumavision.tetris.capacity.vo.director.OutputsVO;
import com.sumavision.tetris.mvc.ext.response.parser.JsonBodyResponseParser;

@Service
@Transactional(rollbackFor = Exception.class)
public class DirectorService {

	@Autowired
	private CapacityFeign capacityFeign;
	
	/**
	 * 添加导播任务<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月26日 上午11:44:38
	 * @param List<DirectorTaskVO> tasks
	 */
	public void addDirector(List<DirectorTaskVO> tasks) throws Exception{
		JsonBodyResponseParser.parseObject(capacityFeign.addDirector(JSON.toJSONString(tasks)), null);
	}
	
	/**
	 * 删除导播任务<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月26日 上午11:44:38
	 * @param List<DirectorTaskVO> tasks
	 */
	public void deleteDirector(List<String> tasks) throws Exception{
		JsonBodyResponseParser.parseObject(capacityFeign.deleteDirector(JSON.toJSONString(tasks)), null);
	}
	
	/**
	 * 添加导播输出<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月26日 上午11:44:38
	 * @param List<OutputsVO> outputs
	 */
	public void addOutput(List<OutputsVO> outputs) throws Exception{
		JsonBodyResponseParser.parseObject(capacityFeign.addOutput(JSON.toJSONString(outputs)), null);
	}
	
	/**
	 * 删除导播输出<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月26日 上午11:44:38
	 * @param List<OutputsVO> outputs
	 */
	public void deleteOutput(List<OutputsVO> outputs) throws Exception{
		JsonBodyResponseParser.parseObject(capacityFeign.deleteOutput(JSON.toJSONString(outputs)), null);
	}


	public String getEncodeTemplate(String encodeType) throws Exception{
		return JsonBodyResponseParser.parseObject(capacityFeign.getEncodeTemplate(encodeType), String.class);
	}


}
