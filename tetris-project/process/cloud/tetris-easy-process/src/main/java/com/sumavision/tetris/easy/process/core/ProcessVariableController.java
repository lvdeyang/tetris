package com.sumavision.tetris.easy.process.core;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sumavision.tetris.commons.constant.DataType;
import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.easy.process.core.exception.ProcessNotExistException;
import com.sumavision.tetris.easy.process.core.exception.ProcessVariableNotExistException;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;

@Controller
@RequestMapping(value = "/process/variable")
public class ProcessVariableController {

	@Autowired
	private UserQuery userTool;
	
	@Autowired
	private ProcessVariableQuery processVariableTool;
	
	@Autowired
	private ProcessVariableDAO processVariableDao;
	
	@Autowired
	private ProcessDAO processDao;
	
	/**
	 * 分页查询流程变量<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月24日 下午5:31:49
	 * @param int currentPage 当前页码
	 * @param int pageSize 每页数据量
	 * @return long total 总数据量
	 * @return total 总数据量
	 * @return List<ProcessVariableVO> rows 变量数据
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/list")
	public Object list(
			Long processId,
			int currentPage,
			int pageSize,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userTool.current();
		
		ProcessPO process = processDao.findOne(processId);
		
		if(process == null){
			throw new ProcessNotExistException(processId);
		}
		
		long total = processVariableDao.countByProcessId(processId);
		
		List<ProcessVariablePO> entities = processVariableTool.findByProcessId(processId, currentPage, pageSize);
		
		List<ProcessVariableVO> rows = ProcessVariableVO.getConverter(ProcessVariableVO.class).convert(entities, ProcessVariableVO.class);
		
		Map<String, Object> result = new HashMapWrapper<String, Object>().put("total", total)
																		 .put("rows", rows)
																		 .getMap();
		
		return result;
	}
	
	/**
	 * 查询流程下的所有变量<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月3日 下午1:40:38
	 * @param Long processId 流程id
	 * @return List<ProcessVariableVO> 变量列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/list/all")
	public Object listAll(
			Long processId, 
			HttpServletRequest request) throws Exception{
		
		UserVO user = userTool.current();
		
		ProcessPO process = processDao.findOne(processId);
		
		if(process == null){
			throw new ProcessNotExistException(processId);
		}
		
		List<ProcessVariablePO> entities = processVariableDao.findByProcessId(processId);
		
		List<ProcessVariableVO> rows = ProcessVariableVO.getConverter(ProcessVariableVO.class).convert(entities, ProcessVariableVO.class);
		
		return rows;
	}
	
	/**
	 * 添加一个变量<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月3日 上午11:31:08
	 * @param Long processId 流程id
	 * @param String primaryKey 变量主键
	 * @param String name 变量名称
	 * @param String defaultValue 变量默认值
	 * @return ProcessVariableVO 变量数据
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add")
	public Object add(
			Long processId,
			String primaryKey,
			String name,
			String dataType,
			String defaultValue,
			String expressionValue,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userTool.current();
		
		ProcessPO process = processDao.findOne(processId);
		
		if(process == null){
			throw new ProcessNotExistException(processId);
		}
		
		ProcessVariablePO variable = new ProcessVariablePO();
		variable.setPrimaryKey(primaryKey);
		variable.setName(name);
		variable.setDataType(DataType.fromName(dataType));
		variable.setDefaultValue(defaultValue);
		variable.setExpressionValue(expressionValue);
		variable.setProcessId(processId);
		processVariableDao.save(variable);
		
		return new ProcessVariableVO().set(variable);
	}
	
	/**
	 * 修改变量<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月3日 上午11:39:21
	 * @param @PathVariable Long id 变量id
	 * @param String primaryKey 变量主键
	 * @param String name 变量名称
	 * @param String defaultValue 变量默认值
	 * @return ProcessVariableVO 变量数据
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/edit/{id}")
	public Object edit(
			@PathVariable Long id,
			String primaryKey,
			String name,
			String defaultValue,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userTool.current();
		
		ProcessVariablePO variable = processVariableDao.findOne(id);
		
		if(variable == null){
			throw new ProcessVariableNotExistException(id);
		}
		
		variable.setPrimaryKey(primaryKey);
		variable.setName(name);
		variable.setDefaultValue(defaultValue);
		processVariableDao.save(variable);
		
		return new ProcessVariableVO().set(variable);
	}
	
	/**
	 * 删除变量<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月3日 上午11:43:52
	 * @param @PathVariable Long id 变量id
	 * @return
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/delete/{id}")
	public Object delete(
			@PathVariable Long id,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userTool.current();
		
		ProcessVariablePO variable = processVariableDao.findOne(id);
		
		if(variable != null){
			processVariableDao.delete(variable);
		}
		
		return null;
	}
	
}
