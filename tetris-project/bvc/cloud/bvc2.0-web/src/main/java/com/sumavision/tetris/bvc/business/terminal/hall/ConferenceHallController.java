package com.sumavision.tetris.bvc.business.terminal.hall;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

@Controller
@RequestMapping(value = "/tetris/bvc/business/conference/hall")
public class ConferenceHallController {

	@Autowired
	private ConferenceHallQuery conferenceHallQuery;
	
	@Autowired
	private ConferenceHallService conferenceHallService;
	
	/**
	 * 查询但设备终端<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年8月3日 下午6:09:24
	 * @return List<TerminalVO> 终端列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/load/single/bundle/terminal")
	public Object loadSingleBundleTerminal(HttpServletRequest request) throws Exception{
		
		return conferenceHallQuery.loadSingleBundleTerminal();
	}
	
	/**
	 * 分页查询会场<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月10日 下午4:42:00
	 * @param String name 会场名称模糊查询
	 * @param int currentPage 当前页
	 * @param int pageSize 每页数据量
	 * @return total long 总数据量
	 * @return rows List<ConferenceHallVO> 会场列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/load")
	public Object load(
			String name,
			int currentPage, 
			int pageSize,
			HttpServletRequest request) throws Exception{
		
		return conferenceHallQuery.load(name, currentPage, pageSize);
	}
	
	/**
	 * 批量添加会场（直接绑定设备）<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年8月4日 上午10:29:52
	 * @param Long terminalId 终端id
	 * @param JSONString bundles 设备列表
	 * @return List<ConferenceHallVO> 会场列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add/batch")
	public Object addBatch(
			Long terminalId,
			String bundles,
			HttpServletRequest request) throws Exception{
		
		return conferenceHallService.addBatch(terminalId, JSON.parseArray(bundles));
	}
	
	/**
	 * 添加会场<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月10日 下午4:45:20
	 * @param String name 会场名称
	 * @param Long terminalId 终端类型id
	 * @return ConferenceHallVO 会场
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add")
	public Object add(
			String name,
			Long terminalId,
			HttpServletRequest request) throws Exception{
		
		return conferenceHallService.add(name, terminalId);
	}
	
	/**
	 * 修改会场名称<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月13日 上午11:14:43
	 * @param Long id 会场id
	 * @param String name 会场名称
	 * @return ConferenceHallVO 会场
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/edit/name")
	public Object editName(
			Long id,
			String name,
			HttpServletRequest request) throws Exception{
		
		return conferenceHallService.editName(id, name);
	}
	 
	/**
	 * 删除会场<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月10日 下午4:47:04
	 * @param Long id 会场id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/delete")
	public Object delete(
			Long id, 
			HttpServletRequest request) throws Exception{
	
		conferenceHallService.delete(id);
		return null;
	}
	
}
