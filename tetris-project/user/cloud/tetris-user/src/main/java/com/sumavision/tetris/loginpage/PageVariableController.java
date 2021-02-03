/**
 * 
 */
package com.sumavision.tetris.loginpage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

/**
 * 类型概述<br/>
 * <b>作者:</b>zhouaining<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2021年1月27日 上午11:32:29
 */

@Controller
@RequestMapping(value = "/pageVariable")
public class PageVariableController {
	@Autowired
	private PageVariableQuery pageVariableQuery;
	
	@Autowired
	private PageVariableService pageVariableService;
	
	@Autowired
	private PageVariableDAO pageVariableDAO;
	/**
	 * 获取页面配置的变量<br/>
	 * <b>作者:</b>zhouaining<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年1月27日 下午3:01:22
	 * @param loginPageId
	 * @return
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/get")
	public Object getPageVariable(Long loginPageId){
		if(loginPageId==null){
			return null;
		}
		return pageVariableQuery.getVariable(loginPageId);
	}
	
	
	/**
	 * 配置页面变量<br/>
	 * <b>作者:</b>zhouaining<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年1月28日 上午10:02:49
	 * @param loginPageId
	 * @return
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/set")
	public Object setPageVariable(Long loginPageId,String variableIds){
		//删除旧配置
		List<PageVariablePO> oldList = pageVariableQuery.getPageVariable(loginPageId);
		if(!oldList.isEmpty()){
			pageVariableDAO.deleteInBatch(oldList);
		}
		//保存新配置
		List<PageVariablePO> list = new ArrayList<>();
		JSONObject jsonObject = JSONObject.parseObject(variableIds);
		for(Map.Entry<String, Object> entry : jsonObject.entrySet()){
			System.out.println(entry.getValue());
			PageVariablePO pageVariablePO = new PageVariablePO();
			pageVariablePO.setLoginPageId(loginPageId);
			pageVariablePO.setVariableId(entry.getValue()+"");
			list.add(pageVariablePO);
				
			}
		pageVariableService.setPageVariable(list);
		return null;
	}
	
}
