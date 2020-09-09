package com.sumavision.tetris.bvc.model.terminal.channel;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

@Controller
@RequestMapping(value = "/tetris/bvc/model/terminal/channel")
public class TerminalChannelController {

	@Autowired
	private TerminalChannelQuery terminalChannelQuery;
	
	@Autowired
	private TerminalChannelService terminalChannelService;
	
	/**
	 * 查询终端通道类型<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月18日 下午4:08:07
	 * @return Map<String, String> 通道类型列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/query/types")
	public Object queryTypes(HttpServletRequest request) throws Exception{
		TerminalChannelType[] values = TerminalChannelType.values();
		Map<String, String> types = new HashMap<String, String>();
		for(TerminalChannelType value:values){
			types.put(value.toString(), value.getName());
		}
		return types;
	}
	
	/**
	 * 查询终端下的通道<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月18日 下午4:01:59
	 * @param Long terminalId 终端id
	 * @return List<TerminalChannelVO> 通道列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/load")
	public Object load(
			Long terminalId, 
			HttpServletRequest request) throws Exception{
		
		return terminalChannelQuery.load(terminalId);
	}
	
	/**
	 * 查询终端下的视频解码通道<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月18日 下午4:01:59
	 * @param Long terminalId 终端id
	 * @return List<TerminalChannelVO> 视频解码通道列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/load/video/decode")
	public Object loadVideoDecode(
			Long terminalId,
			HttpServletRequest request) throws Exception{
		
		return terminalChannelQuery.loadVideoDecode(terminalId);
	}
	
	/**
	 * 添加终端通道<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月18日 下午4:33:08
	 * @param String name 通道名称
	 * @param String type 通道类型
	 * @param Long terminalId 终端id
	 * @param Long terminalBundleId 终端设备id
	 * @param String realChannelId 终端设备通道id
	 * @return TerminalChannelVO 通道
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add")
	public Object add(
			String name, 
			String type, 
			Long terminalId, 
			Long terminalBundleId, 
			String realChannelId,
			HttpServletRequest request) throws Exception{
		
		return terminalChannelService.add(name, type, terminalId, terminalBundleId, realChannelId);
	}
	
	/**
	 * 修改终端通道<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月18日 下午4:33:08
	 * @param Long id 终端通道id
	 * @param String name 通道名称
	 * @param String type 通道类型
	 * @param Long terminalId 终端id
	 * @param Long terminalBundleId 终端设备id
	 * @param String realChannelId 终端设备通道id
	 * @return TerminalChannelVO 通道
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/edit")
	public Object edit(
			Long id,
			String name,
			String type,
			Long terminalId,
			Long terminalBundleId,
			String realChannelId,
			HttpServletRequest request) throws Exception{
		
		return terminalChannelService.edit(id, name, type, terminalId, terminalBundleId, realChannelId);
	}
	
	/**
	 * 删除终端通道<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月18日 下午4:42:03
	 * @param Long id 通道id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/delete")
	public Object delete(Long id, HttpServletRequest request) throws Exception{
		
		terminalChannelService.delete(id);
		return null;
	}
	
}
