package com.sumavision.tetris.bvc.model.layout;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;

@Component
public class LayoutPositionQuery {

	@Autowired
	private LayoutPositionDAO layoutPositionDao;
	
	/**
	 * 查询布局类型列表<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年10月30日 下午5:08:49
	 * @return List<Map<String, String>> 类型列表<br/>
	 * @return label String 名称
	 * @return value String key 
	 */
	public List<Map<String, String>> loadTypes() throws Exception{
		List<Map<String, String>> types = new ArrayList<Map<String,String>>();
		LayoutPositionType[] values = LayoutPositionType.values();
		for(LayoutPositionType value:values){
			types.add(new HashMapWrapper<String, String>().put("label", value.getName())
														  .put("value", value.toString())
														  .getMap());
		}
		return types;
	}
	
	/**
	 * 查询虚拟源下的布局<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年10月27日 上午9:42:10
	 * @param Long layoutId 虚拟源id
	 * @return List<LayoutPositionVO> 布局列表
	 */
	public List<LayoutPositionVO> load(Long layoutId) throws Exception{
		List<LayoutPositionPO> entities = layoutPositionDao.findByLayoutIdOrderBySerialNum(layoutId);
		return LayoutPositionVO.getConverter(LayoutPositionVO.class).convert(entities, LayoutPositionVO.class);
	}
	
}
