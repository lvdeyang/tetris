/**
 * 
 */
package com.sumavision.tetris.loginpage;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 类型概述<br/>
 * <b>作者:</b>zhouaining<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2021年1月27日 上午11:33:37
 */
@Service
public class PageVariableService {
	@Autowired PageVariableDAO pageVariableDAO;
	
	public Object setPageVariable(List<PageVariablePO> list){
		
		return pageVariableDAO.saveAll(list);
	}
}
