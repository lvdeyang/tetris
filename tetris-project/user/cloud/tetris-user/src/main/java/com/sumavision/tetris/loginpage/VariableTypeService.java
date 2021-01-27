package com.sumavision.tetris.loginpage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class VariableTypeService {
	
	@Autowired
	private VariableTypeDAO variableTypeDAO;

	/**
	 * 添加变量类型<br/>
	 * <b>作者:</b>lqxuhv<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年1月25日 下午4:00:29
	 * @param name 变量类型名称
	 * @param variableKey 变量key
	 * @param type 变量类型
	 * @return VariableTypeVO 变量类型
	 */
	public VariableTypeVO addVariableType(String name, String variableKey, String type) throws Exception{
		VariableTypePO variableTypePO = new VariableTypePO();
		variableTypePO.setName(name);
		variableTypePO.setVariableKey(variableKey);
		variableTypePO.setType(Type.fromName(type));
		variableTypeDAO.save(variableTypePO);
		VariableTypeVO variableTypeVO = new VariableTypeVO().set(variableTypePO);
		return variableTypeVO;
	}

	/**
	 * 修改变量类型<br/>
	 * <b>作者:</b>lqxuhv<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年1月25日 下午4:03:04
	 * @param id 变量类型id
	 * @param name 变量类型名称
	 * @param variableKey 变量key
	 * @param type 变量类型类型
	 * @return VariableTypeVO 变量类型信息
	 */
	public VariableTypeVO editVariableType(Long id, String name, String variableKey, String type) throws Exception{
		VariableTypePO variableTypePO = variableTypeDAO.findOne(id);
		variableTypePO.setName(name);
		variableTypePO.setVariableKey(variableKey);
		variableTypePO.setType(Type.fromName(type));
		variableTypeDAO.save(variableTypePO);
		VariableTypeVO variableTypeVO = new VariableTypeVO().set(variableTypePO);
		return variableTypeVO;
	}

	/**
	 * 删除变量类型<br/>
	 * <b>作者:</b>lqxuhv<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年1月25日 下午4:07:21
	 * @param id 变量类型id
	 * @return null
	 */
	public Object deleteVariableType(Long id) throws Exception{
		VariableTypePO variableTypePO = variableTypeDAO.findOne(id);
		if (null != variableTypePO) {
			variableTypeDAO.delete(id);
		}
		return null;
	}

}
