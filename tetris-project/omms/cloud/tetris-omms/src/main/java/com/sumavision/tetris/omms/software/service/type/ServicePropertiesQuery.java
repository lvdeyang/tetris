package com.sumavision.tetris.omms.software.service.type;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Component;

@Component
public class ServicePropertiesQuery {

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
	
}
