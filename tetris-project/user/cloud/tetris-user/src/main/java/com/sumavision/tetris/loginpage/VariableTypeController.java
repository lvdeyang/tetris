package com.sumavision.tetris.loginpage;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

@Controller
@RequestMapping(value = "/variableType")
public class VariableTypeController {
	
	@Autowired
	private VariableTypeQuery variableTypeQuery;
	
	@Autowired
	private VariableTypeService variableTypeService;
	
	/**
	 * 查询变量类型<br/>
	 * <b>作者:</b>lqxuhv<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年1月25日 下午3:58:57
	 * @return List<VariableTypeVO> 变量类型列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/query")
	public Object queryVariableType()throws Exception{
		return variableTypeQuery.queryVariableType();
	}
	
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
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add")
	public Object addVariableType(String name, String variableKey, String type)throws Exception{
		return variableTypeService.addVariableType(name, variableKey, type);
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
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/edit")
	public Object editVariableType(Long id, String name, String variableKey, String type)throws Exception{
		return variableTypeService.editVariableType(id, name, variableKey, type);
	}
	
	/**
	 * 删除变量类型<br/>
	 * <b>作者:</b>lqxuhv<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年1月25日 下午4:07:21
	 * @param id 变量类型id
	 * @return null
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/delete")
	public Object deleteVariableType(Long id)throws Exception{
		return variableTypeService.deleteVariableType(id);
	}
	
	/**
	 * 查询变量类型类型名<br/>
	 * <b>作者:</b>lqxuhv<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年1月25日 下午4:08:22
	 * @return Set<String> 类型名
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/find/type")
	public Object findType()throws Exception{
		return variableTypeQuery.findType();
	}
	
}
