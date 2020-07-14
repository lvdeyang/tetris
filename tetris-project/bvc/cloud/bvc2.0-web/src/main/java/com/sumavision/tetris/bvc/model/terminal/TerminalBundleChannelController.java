package com.sumavision.tetris.bvc.model.terminal;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

@Controller
@RequestMapping(value = "/tetris/bvc/model/terminal/bundle/channel")
public class TerminalBundleChannelController {
	
	@Autowired
	private TerminalBundleChannelQuery terminalBundleChannelQuery;
	
	@Autowired
	private TerminalBundleChannelService terminalBundleChannelService;

	/**
	 * 查询终端设备通道类型<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月17日 下午3:52:36
	 * @return Map<String, String> 类型列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/query/types")
	public Object queryTypes(HttpServletRequest request) throws Exception{
		TerminalBundleChannelType[] values = TerminalBundleChannelType.values();
		Map<String, String> types = new HashMap<String, String>();
		for(TerminalBundleChannelType value:values){
			types.put(value.toString(), value.getName());
		}
		return types;
	}
	
	/**
	 * 查询终端设备下的通道<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月17日 下午4:23:09
	 * @param Long terminalBundleId 终端设备id
	 * @return List<TerminalBundleChannelVO> 通道列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/load")
	public Object load(
			Long terminalBundleId, 
			HttpServletRequest request) throws Exception{
		
		return terminalBundleChannelQuery.load(terminalBundleId);
	}
	
	/**
	 * 根据终端设备和通道类型查询通道<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月19日 上午9:03:11
	 * @param Long terminalBundleId 终端设备id
	 * @param String type 通道类型
	 * @return List<TerminalBundleChannelVO> 通道列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/load/by/type")
	public Object loadByType(
			Long terminalBundleId,
			String type,
			HttpServletRequest request) throws Exception{
		
		return terminalBundleChannelQuery.loadByType(terminalBundleId, type);
	}
	
	/**
	 * 添加终端设备通道<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月17日 下午5:33:21
	 * @param Long terminalBundleId 终端设备id
	 * @param String channelId 通道id
	 * @param String type 通道类型
	 * @return TerminalBundleChannelVO 终端设备通道
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add")
	public Object add(
			Long terminalBundleId,
			String channelId,
			String type,
			HttpServletRequest request) throws Exception{
		
		return terminalBundleChannelService.add(terminalBundleId, channelId, type);
	}
	
	/**
	 * 修改终端设备通道<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月17日 下午5:49:47
	 * @param Long id 通道id
	 * @param String channelId 通道id
	 * @param String type 通道类型
	 * @return TerminalBundleChannelVO 通道数据
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/edit")
	public Object edit(
			Long id,
			String channelId,
			String type,
			HttpServletRequest request) throws Exception{
		
		return terminalBundleChannelService.edit(id, channelId, type);
	}
	
	/**
	 * 删除终端设备通道<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月18日 下午1:16:16
	 * @param Long id 通道id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/delete")
	public Object delete(
			Long id, 
			HttpServletRequest request) throws Exception{
		
		terminalBundleChannelService.delete(id);
		return null;
	}
	
}
