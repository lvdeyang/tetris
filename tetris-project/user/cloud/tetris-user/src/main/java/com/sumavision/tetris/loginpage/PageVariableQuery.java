/**
 * 
 */
package com.sumavision.tetris.loginpage;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 类型概述<br/>
 * <b>作者:</b>zhouaining<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2021年1月27日 上午11:33:21
 */
@Component
public class PageVariableQuery {
	
	@Autowired
	private PageVariableDAO pageVariableDAO;

	@Autowired
	private VariableDAO variableDAO;
	
	@Autowired
	private VariableTypeDAO variableTypeDAO;
	
	
	
	/**
	 * 返回页面配置好的变量<br/>
	 * <b>作者:</b>zhouaining<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年1月29日 下午5:25:53
	 * @param loginPageId
	 * @return
	 */
	public List<VariableTypeVO> getVariable(Long loginPageId){
		
		List<VariableTypeVO> variableTypeVOs = new ArrayList<VariableTypeVO>();
		//获取该页面配置好的变量
		List<PageVariablePO> pageVariablePOs = pageVariableDAO.findByLoginPageId(loginPageId);
		
		for(PageVariablePO pageVariablePO:pageVariablePOs){
			List<VariableVO> variableVOs = new ArrayList<VariableVO>();
		
			Long variableId=Long.parseLong(pageVariablePO.getVariableId());
			//获取变量对应的variableTypeId
			VariablePO variablePO = variableDAO.findById(variableId);
			String variableTypeId = variablePO.getVariableTypeId();
			//根据variableTypeId取得variableKey
			VariableTypePO variableTypePO = variableTypeDAO.findById(Long.parseLong(variableTypeId));
			String variableKey = variableTypePO.getVariableKey();	
			
			//返回需要信息，变量的Id和变量对应的variableKey
			variableVOs.add(new VariableVO().setId(variableId));
			VariableTypeVO variableTypeVO = new VariableTypeVO();
			variableTypeVO.setVariable(variableVOs);
			variableTypeVO.setVariableKey(variableKey);
			variableTypeVOs.add(variableTypeVO);
		}
		return variableTypeVOs;
	}
	
	public List<PageVariablePO> getPageVariable(Long longinPageId){
		return pageVariableDAO.findByLoginPageId(longinPageId);
	}
}
