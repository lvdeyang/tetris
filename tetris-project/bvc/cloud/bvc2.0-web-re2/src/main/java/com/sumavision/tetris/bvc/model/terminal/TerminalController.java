package com.sumavision.tetris.bvc.model.terminal;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

@Controller
@RequestMapping(value = "/tetris/bvc/model/terminal")
public class TerminalController {

	@Autowired
	private TerminalQuery terminalQuery;
	
	@Autowired
	private TerminalService terminalService;
	
	/**
	 * 查询全部终端<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月29日 下午3:07:03
	 * @return List<TerminalVO> 终端列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/load/all")
	public Object loadAll(HttpServletRequest request) throws Exception{
		
		return terminalQuery.loadAll();
	}
	
	/**
	 * 查询终端类型<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月29日 下午2:52:52
	 * @return Set<String> 终端类型列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/query/types")
	public Object queryTypes(HttpServletRequest request) throws Exception{
		List<TerminalType> values = TerminalType.findBySignleBundle(false);
		Set<String> terminalTypes = new HashSet<String>();
		for(TerminalType value:values){
			terminalTypes.add(value.getName());
		}
		return terminalTypes;
	}
	
	/**
	 * 分页查询终端<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月5日 下午2:59:49
	 * @param int currentPage 当前页
	 * @param int pageSize 每页数据量
	 * @return Long total 总数据量
	 * @return List<TerminalVO> rows 终端列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/load")
	public Object load(
			int currentPage,
			int pageSize,
			HttpServletRequest request) throws Exception{
		
		return terminalQuery.load(currentPage, pageSize);
	}
	
	/**
	 * 添加终端<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月5日 下午3:14:38
	 * @param String name 终端名称
	 * @param String typeName 类型名称
	 * @return TerminalVO 终端
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/create")
	public Object create(
			String name,
			String typeName,
			HttpServletRequest request) throws Exception{
		
		return terminalService.create(name, typeName);
	}
	
	/**
	 * 一键添加单设备类型终端<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年1月20日 上午9:09:25
	 * @return List<TerminalVO> 终端列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/create/single/bundle/terminal/batch")
	public Object createSingleBundleTerminalBatch(HttpServletRequest request) throws Exception{
		
		return terminalService.createSingleBundleTerminalBatch();
	}
	
	/**
	 * 修改终端名称<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月8日 上午9:37:28
	 * @param Long id 终端id
	 * @param String name 名称
	 * @param String typeName 类型名称
	 * @return TerminalVO 终端
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/edit")
	public Object edit(
			Long id,
			String name,
			String typeName,
			HttpServletRequest request) throws Exception{
		
		return terminalService.edit(id, name, typeName);
	}
	
	/**
	 * 删除终端<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月8日 上午9:43:50
	 * @param Long id 终端id
 	 * @return TerminalVO 终端
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/delete")
	public Object delete(
			Long id, 
			HttpServletRequest request) throws Exception{
		
		return terminalService.delete(id);
	}
	
	/**
	 * 根据id查询终端<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月15日 下午6:09:19
	 * @param Long id 终端id
	 * @return TerminalVO 终端信息
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/find/by/id")
	public Object findById(
			Long id,
			HttpServletRequest request) throws Exception{
		
		return terminalQuery.findById(id);
	}
	
	/**
	 * 修改终端是否开启屏幕布局<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年10月30日 下午4:15:35
	 * @param Long terminalId 终端id
	 * @param Boolean enableLayout 是否开启屏幕布局
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/layout/enable/change")
	public Object layoutEnableChange(
			Long terminalId, 
			Boolean enableLayout,
			HttpServletRequest request)throws Exception{
		
		terminalService.layoutEnableChange(terminalId, enableLayout);
		return null;
	}
	
	/**
	 * 修改布局列数<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月16日 上午10:18:59
	 * @param Long terminalId 终端id
	 * @param Integer columns 列数
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/layout/columns/change")
	public Object layoutColumnsChange(
			Long terminalId,
			Integer columns,
			HttpServletRequest request) throws Exception{
		
		terminalService.layoutColumnsChange(terminalId, columns);
		return null;
	}
	
	/**
	 * 修改布局行数<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月16日 上午10:18:59
	 * @param Long terminalId 终端id
	 * @param Integer rows 行数
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/layout/rows/change")
	public Object layoutRowsChange(
			Long terminalId,
			Integer rows,
			HttpServletRequest request) throws Exception{
		
		terminalService.layoutRowsChange(terminalId, rows);
		return null;
	}
	
}
