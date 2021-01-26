package com.sumavision.tetris.loginpage;

import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sumavision.tetris.commons.util.image.ImageUtil;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.mvc.wrapper.MultipartHttpServletRequestWrapper;

@Controller
@RequestMapping(value = "/variable")
public class VariableController {
	
	@Autowired
	private VariableQuery variableQuery;
	
	@Autowired
	private VariableService variableService;
	
	@Autowired
	private ImageUtil imageUtil;
	
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
	@JsonBody
	@ResponseBody
	@RequestMapping(value ="/add")
	public Object addVariable(HttpServletRequest request)throws Exception{
		MultipartHttpServletRequestWrapper requestWrapper = new MultipartHttpServletRequestWrapper(request, -1);
		String variableTypeId = requestWrapper.getString("variableTypeId");
		String value = null;
		VariableTypePO variableTypePO = variableTypeDAO.findOne(Long.valueOf(variableTypeId));
		if (variableTypePO.getType().equals(Type.IMG)) {
			InputStream file = requestWrapper.getInputStream("file");
			byte[] bytes = IOUtils.toByteArray(file);
			value = imageUtil.getUrlSchema(bytes);
		}else if (variableTypePO.getType().equals(Type.TEXT)) {
			String text = requestWrapper.getString("text");
			value = text;
		}
		
		return variableService.addVariable( variableTypeId ,value);
	}
	
	/**
	 * 查询变量信息<br/>
	 * <b>作者:</b>lqxuhv<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年1月25日 下午3:37:45
	 * @return List<VariableVO> 变量信息列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value="/query")
	public Object queryVariable()throws Exception{
		return variableQuery.queryVariable();
	}
	
	/**
	 * 删除变量<br/>
	 * <b>作者:</b>lqxuhv<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年1月25日 下午3:38:47
	 * @param id 变量id
 	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value="/delete")
	public Object deleteVariable(Long id)throws Exception{
		return variableService.deleteVariable(id);
	}
	
	/**
	 * 变量类型分类变量<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>lqxuhv<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年1月25日 下午3:56:19
	 * @return List<VariableTypeVO> 变量类型分组
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value="/variable/ByType")
	public Object variableByType() throws Exception{
		return variableQuery.variableByType();
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
	@JsonBody
	@ResponseBody
	@RequestMapping(value="/edit")
	public Object editVariable(HttpServletRequest request)throws Exception{
		MultipartHttpServletRequestWrapper requestWrapper = new MultipartHttpServletRequestWrapper(request, -1);
		String variableTypeId = requestWrapper.getString("variableTypeId");
		String id = requestWrapper.getString("id");
		String value = null;
		VariableTypePO variableTypePO = variableTypeDAO.findOne(Long.valueOf(variableTypeId));
		if (variableTypePO.getType().equals(Type.IMG)) {
			InputStream file = requestWrapper.getInputStream("file");
			byte[] bytes = IOUtils.toByteArray(file);
			value = imageUtil.getUrlSchema(bytes);
		}else if (variableTypePO.getType().equals(Type.TEXT)) {
			String text = requestWrapper.getString("text");
			value = text;
		}
		return variableService.editVariable(id, variableTypeId, value);
	}
	
}
