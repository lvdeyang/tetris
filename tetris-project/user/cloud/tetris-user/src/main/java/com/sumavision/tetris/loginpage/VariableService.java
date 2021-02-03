package com.sumavision.tetris.loginpage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class VariableService {
	
	@Autowired
	private VariableDAO variableDAO;
	
	@Autowired
	private VariableTypeDAO variableTypeDAO; 

	/**
	 * 添加变量<br/>
	 * <b>作者:</b>lqxuhv<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年1月25日 下午3:36:13
	 * @param variableTypeId 变量类型id
	 * @param value 变量值 
	 * @return VariableVO 变量信息
	 */
	public VariableVO addVariable(String variableTypeId, String value) throws Exception {
		VariablePO variablePO = new VariablePO();
		variablePO.setVariableTypeId(variableTypeId);
		variablePO.setValue(value);
		variableDAO.save(variablePO);
		VariableTypePO  variableTypePO = variableTypeDAO.findOne(Long.valueOf(variableTypeId));
		VariableVO variableVO = new VariableVO().set(variablePO);
		variableVO.setType(variableTypePO.getType()==null?null:variableTypePO.getType());
		variableVO.setTypeName(variableTypePO.getType().getName());
		variableVO.setVariableKey(variableTypePO.getVariableKey());
		variableVO.setName(variableTypePO.getName());
		return variableVO;
	}

	/**
	 * 删除变量<br/>
	 * <b>作者:</b>lqxuhv<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年1月25日 下午3:38:47
	 * @param id 变量id
 	 */
	public Object deleteVariable(Long id) {
		VariablePO variablePO = variableDAO.findOne(id);
		if (null != variablePO) {
			variableDAO.delete(variablePO);
		}
		return null;
	}

	/**
	 * 修改变量<br/>
	 * <b>作者:</b>lqxuhv<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年1月25日 下午3:40:51
	 * @param id 变量id
	 * @param variableTypeId 变量类型id
	 * @param value 变量value
	 * @return VariableVO 修改后的变量信息
	 */
	public VariableVO editVariable(String id, String variableTypeId, String value) throws Exception {
		VariablePO variablePO = variableDAO.findOne(Long.valueOf(id));
		variablePO.setVariableTypeId(variableTypeId);
		variablePO.setValue(value);
		variableDAO.save(variablePO);
		VariableTypePO  variableTypePO = variableTypeDAO.findOne(Long.valueOf(variableTypeId));
		VariableVO variableVO = new VariableVO().set(variablePO);
		variableVO.setType(variableTypePO.getType()==null?null:variableTypePO.getType());
		variableVO.setTypeName(variableTypePO.getType().getName());
		variableVO.setVariableKey(variableTypePO.getVariableKey());
		variableVO.setName(variableTypePO.getName());
		return variableVO;
	}

}
