package com.sumavision.tetris.loginpage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class VariableTypeQuery {

	@Autowired
	private VariableTypeDAO variableTypeDAO; 
	
	/**
	 * 查询变量类型<br/>
	 * <b>作者:</b>lqxuhv<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年1月25日 下午3:58:57
	 * @return List<VariableTypeVO> 变量类型列表
	 */
	public List<VariableTypeVO> queryVariableType() throws Exception{
		List<VariableTypePO> variableTypePOs = variableTypeDAO.findAll();
		List<VariableTypeVO> variableTypeVOs = new ArrayList<VariableTypeVO>();
		if (variableTypePOs != null && variableTypePOs.size()>0) {
			for (VariableTypePO variableTypePO : variableTypePOs) {
				VariableTypeVO variableTypeVO = new VariableTypeVO().set(variableTypePO);
				variableTypeVOs.add(variableTypeVO);
			}
		}
		return variableTypeVOs;
	}

	/**
	 * 查询变量类型类型名<br/>
	 * <b>作者:</b>lqxuhv<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年1月25日 下午4:08:22
	 * @return Set<String> 类型名
	 */
	public Set<String> findType() {
		Set<String> values = new HashSet<String>();
		Type[] types = Type.values();
		for(Type type:types){
			values.add(type.getName());
		}
		return values;
	}

}
