package com.sumavision.tetris.bvc.model.terminal;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.suma.venus.resource.dao.ChannelTemplateDao;
import com.suma.venus.resource.pojo.ChannelTemplatePO;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;


@Controller
@RequestMapping(value = "/tetris/bvc/model/terminal/bundle")
public class TerminalBundleController {

	@Autowired
	private ChannelTemplateDao channelTemplateDao;
	
	@Autowired
	private TerminalBundleQuery terminalBundleQuery;
	
	@Autowired
	private TerminalBundleService terminalBundleService;
	
	/**
	 * 查询设备类型<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月16日 下午4:56:59
	 * @return bundleTypes Set<String> 设备模板列表
	 * @return types Map<String, String> 编解码类型
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/query/types")
	public Object queryTypes(HttpServletRequest request) throws Exception{
		List<ChannelTemplatePO> templates = channelTemplateDao.findByBundleTypeIn(new ArrayListWrapper<String>().add("VenusTerminal")
																												.add("VenusVideoMatrix")
																												.getList());
		Set<String> bundleTypes = new HashSet<String>();
		if(templates!=null && templates.size()>0){
			for(ChannelTemplatePO template:templates){
				bundleTypes.add(template.getDeviceModel());
			}
		}
		TerminalBundleType[] values = TerminalBundleType.values();
		Map<String, String> types = new HashMap<String, String>();
		for(TerminalBundleType value:values){
			types.put(value.toString(), value.getName());
		}
		return new HashMapWrapper<String, Object>().put("bundleTypes", bundleTypes)
												   .put("types", types)
												   .getMap();
	}
	
	/**
	 * 分页查询终端下的设备模板<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月17日 上午9:13:02
	 * @param Long terminalId 终端类型id
	 * @param int currentPage 当前页
	 * @param int pageSize 每页数据量
	 * @return long total 总数据量
	 * @return rows List<TerminalBundleVO> 设备模板列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/load")
	public Object load(
			Long terminalId, 
			int currentPage, 
			int pageSize, 
			HttpServletRequest request) throws Exception{
		
		return terminalBundleQuery.load(terminalId, currentPage, pageSize);
	}
	
	/**
	 * 根据终端和类型查询设备<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月19日 上午8:50:24
	 * @param Long terminalId 终端id
	 * @param String type 编解码类型
	 * @return List<TerminalBundleVO> 设备列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/load/by/type")
	public Object loadByType(
			Long terminalId,
			String type,
			HttpServletRequest request) throws Exception{
		
		return terminalBundleQuery.loadByType(terminalId, type);
	}
	
	/**
	 * 为终端绑定设备模板<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月17日 上午10:05:19
	 * @param Long terminalId 终端id
	 * @param String name 名称前缀
	 * @param String bundleType 设备模板类型
	 * @param String type 编解码类型
	 * @param Integer number 设备数量
	 * @return List<TerminalBundleVO> 设备列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add")
	public Object add(
			Long terminalId,
			String name,
			String bundleType,
			String type,
			Integer number,
			HttpServletRequest request) throws Exception{
		
		return terminalBundleService.add(terminalId, name, bundleType, type, number);
	}
	
	/**
	 * 修改终端设备名称<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月17日 下午1:18:21
	 * @param Long id 终端设备id
	 * @param String name 名称
	 * @return TerminalBundleVO 终端设备
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/edit/name")
	public Object editName(
			Long id,
			String name,
			HttpServletRequest request) throws Exception{
		
		return terminalBundleService.editName(id, name);
	}
	
	/**
	 * 删除终端设备<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月17日 下午4:38:06
	 * @param Long id 终端设备id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/delete")
	public Object delete(
			Long id,
			HttpServletRequest request) throws Exception{
		
		terminalBundleService.delete(id);
		return null;
	}
	
}
