package com.sumavision.tetris.loginpage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class VariableQuery {

	@Autowired
	private VariableDAO variableDAO;
	
	@Autowired
	private VariableTypeDAO variableTypeDAO;
	
	/**
	 * 查询变量信息<br/>
	 * <b>作者:</b>lqxuhv<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年1月25日 下午3:37:45
	 * @return List<VariableVO> 变量信息列表
	 */
	public List<VariableVO> queryVariable() throws Exception {
		List<VariablePO> variablePOs = variableDAO.findAll();
		List<VariableVO> variableVOs = new ArrayList<VariableVO>();
		Set<Long> variableTypeIds = new HashSet<Long>(); 
		if (variablePOs != null && variablePOs.size()>0) {
			for (VariablePO variablePO : variablePOs) {
				VariableVO variableVO = new VariableVO().set(variablePO);
				variableVOs.add(variableVO);
				variableTypeIds.add(Long.valueOf(variablePO.getVariableTypeId()));
			}
			List<VariableTypePO> variableTypePOs = variableTypeDAO.findByIdIn(variableTypeIds);
			if (variableTypePOs != null&&variableTypePOs.size()>0) {
				for (VariableVO variableVO : variableVOs) {
					for (VariableTypePO variableTypePO : variableTypePOs) {
						if (variableVO.getVariableTypeId().equals(variableTypePO.getId().toString())) {
							variableVO.setType(variableTypePO.getType()==null?null:variableTypePO.getType().getName());
							variableVO.setVariableKey(variableTypePO.getVariableKey());
							variableVO.setName(variableTypePO.getName());
						}
					}
				}
			}
		}
		return variableVOs;
	}

	/**
	 * 变量类型分类变量<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>lqxuhv<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年1月25日 下午3:56:19
	 * @return List<VariableTypeVO> 变量类型分组
	 */
	public List<VariableTypeVO> variableByType() throws Exception{
		List<VariableTypePO> variableTypePOs = variableTypeDAO.findAll();
		List<VariableTypeVO> variableTypeVOs = new ArrayList<VariableTypeVO>();
		List<VariablePO> variablePOs = variableDAO.findAll();
		List<VariableVO> variableVOs = new ArrayList<VariableVO>();
		if (variableTypePOs != null && variableTypePOs.size()>0) {
			for (VariableTypePO variableTypePO : variableTypePOs) {
				VariableTypeVO variableTypeVO = new VariableTypeVO().set(variableTypePO);
				variableTypeVOs.add(variableTypeVO);
			}
		}
		if (variablePOs != null && variablePOs.size()>0) {
			for (VariablePO variablePO : variablePOs) {
				VariableVO variableVO = new VariableVO().set(variablePO);
				variableVOs.add(variableVO);
			}
		}
		if (variableTypeVOs != null&& variableTypeVOs.size()>0) {
			for (VariableTypeVO variableTypeVO : variableTypeVOs) {
				List<VariableVO> vos = new ArrayList<VariableVO>();
				if (variableVOs != null && variableVOs.size()>0) {
					for (VariableVO variableVO : variableVOs) {
						if (variableVO.getVariableTypeId().equals(variableTypeVO.getId().toString())) {
							vos.add(variableVO);
						}
					}
				}
				variableTypeVO.setVariable(vos);
			}
		}
		return variableTypeVOs;
	}

}
