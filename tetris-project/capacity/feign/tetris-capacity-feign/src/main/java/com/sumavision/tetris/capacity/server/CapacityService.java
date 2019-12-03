package com.sumavision.tetris.capacity.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sumavision.tetris.mvc.ext.response.parser.JsonBodyResponseParser;

@Service
@Transactional(rollbackFor = Exception.class)
public class CapacityService {

	@Autowired
	private CapacityFeign capacityFeign;
	
	/**
	 * 添加收录<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月3日 上午10:46:43
	 * @param String recordInfo 收录信息
	 * @return String 收录标识
	 */
	public String add(String recordInfo) throws Exception{
		return JsonBodyResponseParser.parseObject(capacityFeign.add(recordInfo), String.class);
	}
	
	/**
	 * 停止收录<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月3日 上午10:47:20
	 * @param String id 收录标识
	 */
	public void delete(String id) throws Exception{
		capacityFeign.delete(id);
	}
	
}
