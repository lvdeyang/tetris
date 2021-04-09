package com.sumavision.tetris.bvc.model.terminal.channel;

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
	 * 根据终端和类型查询终端通道<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月13日 上午11:13:42
	 * @param Long terminalId 终端id
	 * @param String type 终端通道类型
	 * @return List<TerminalChannelVO> 通道列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/load/by/type")
	public Object loadByType(
			Long terminalId, 
			String type, 
			HttpServletRequest request) throws Exception{
		
		return terminalChannelQuery.loadByType(terminalId, type);
	}
	
	/**
	 * 添加视频编码通道<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月13日 上午8:59:46
	 * @param Long terminalId 终端id
	 * @param String name 通道名称
	 * @param Long terminalAudioEncodeChannelId 关联终端音频编码通道
	 * @param JSONArray paramsPermissions [{channelParams:"参数", terminalBundleId:"终端设备id", terminalBundleChannelId:"终端设备通道id"}]
	 * @return TerminalGraphNodeVO 拓补图节点
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add/video/encode")
	public Object addVideoEncode(
			Long terminalId,
			String name,
			Long terminalAudioEncodeChannelId,
			String paramsPermissions,
			HttpServletRequest request) throws Exception{
		
		return terminalChannelService.addVideoEncode(terminalId, name, terminalAudioEncodeChannelId, paramsPermissions);
	}
	
	/**
	 * 修改视频编码通道<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月13日 上午8:59:46
	 * @param Long id 视频编码通道id
	 * @param String name 通道名称
	 * @param JSONArray paramsPermissions [{channelParams:"参数", terminalBundleId:"终端设备id", terminalBundleChannelId:"终端设备通道id"}]
	 * @return TerminalGraphNodeVO 拓补图节点
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/edit/video/encode")
	public Object editVideoEncode(
			Long id,
			String name,
			String paramsPermissions,
			HttpServletRequest request) throws Exception{
		
		return terminalChannelService.editVideoEncode(id, name, paramsPermissions);
	}
	
	/**
	 * 移动视频编码通道<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月17日 下午1:40:04
	 * @param Long id 视频编码通道
	 * @param Long terminalAudioEncodeChannelId 终端音频编码通道id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/move/video/encode")
	public Object moveVideoEncode(
			Long id,
			Long terminalAudioEncodeChannelId,
			HttpServletRequest request) throws Exception{
		
		terminalChannelService.moveVideoEncode(id, terminalAudioEncodeChannelId);
		return null;
	}
	
	/**
	 * 删除视频编码通道<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月17日 下午1:40:04
	 * @param Long id 视频编码通道id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/remove/video/encode")
	public Object removeVideoEncode(
			Long id,
			HttpServletRequest request) throws Exception{
		
		terminalChannelService.removeVideoEncode(id);
		return null;
	}
	
	/**
	 * 添加视频解码通道<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月13日 下午3:41:00
	 * @param Long terminalId 终端id
	 * @param String name 解码通道名称
	 * @param Long terminalPhysicalScreenId 关联物理屏幕id
	 * @param JSONArray paramsPermissions [{channelParams:"参数", terminalBundleId:"终端设备id", terminalBundleChannelId:"终端设备通道id"}]
	 * @return TerminalGraphNodeVO 拓补图节点
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add/video/decode")
	public Object addVideoDecode(
			Long terminalId,
			String name,
			Long terminalPhysicalScreenId,
			String paramsPermissions,
			HttpServletRequest request) throws Exception{
		
		return terminalChannelService.addVideoDecode(terminalId, name, terminalPhysicalScreenId, paramsPermissions);
	}
	
	/**
	 * 修改视频解码通道 <br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月17日 下午3:34:41
	 * @param Long id 视频解码通道id
	 * @param String name 名称
	 * @param JSONArray paramsPermissions [{channelParams:"参数", terminalBundleId:"终端设备id", terminalBundleChannelId:"终端设备通道id"}]
	 * @return TerminalGraphNodeVO 拓补图节点
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/edit/video/decode")
	public Object editVideoDecode(
			Long id,
			String name,
			String paramsPermissions,
			HttpServletRequest request) throws Exception{
		
		return terminalChannelService.editVideoDecode(id, name, paramsPermissions);
	}
	
	/**
	 * 移动视频解码通道<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月17日 下午4:15:03
	 * @param Long id 视频解码通道id
	 * @param Long terminalPhysicalScreenId 物理屏幕id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/move/video/decode")
	public Object moveVideoDecode(
			Long id,
			Long terminalPhysicalScreenId,
			HttpServletRequest request) throws Exception{
		
		terminalChannelService.moveVideoDecode(id, terminalPhysicalScreenId);
		return null;
	}
	
	
	/**
	 * 删除视频解码通道<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月17日 下午4:18:07
	 * @param Long id 视频解码通道id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/remove/video/decode")
	public Object removeVideoDecode(
			Long id,
			HttpServletRequest reuqHttpServletRequest) throws Exception{
		
		terminalChannelService.removeVideoDecode(id);
		return null;
	}
	
	/**
	 * 添加音频编码通道<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月13日 下午3:41:00
	 * @param Long terminalId 终端id
	 * @param String name 音频编码通道名称
	 * @param Long terminalBundleChannelId 终端设备通道id
	 * @return TerminalGraphNodeVO 拓补图节点
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add/audio/encode")
	public Object addAudioEncode(
			Long terminalId,
			String name,
			Long terminalBundleChannelId,
			HttpServletRequest request) throws Exception{
		
		return terminalChannelService.addAudioEncode(terminalId, name, terminalBundleChannelId);
	}
	
	/**
	 * 修改音频编码<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月17日 下午7:14:17
	 * @param Long id 音频编码通道id
	 * @param String name 通道名称
	 * @param Long terminalBundleChannelId 终端设备通道id
	 * @return TerminalGraphNodeVO 终端拓补图
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/edit/audio/encode")
	public Object editAudioEncode(
			Long id,
			String name,
			Long terminalBundleChannelId,
			HttpServletRequest request) throws Exception{
		
		return terminalChannelService.editAudioEncode(id, name, terminalBundleChannelId);
	}
	
	/**
	 * 删除音频编码通道<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月18日 上午9:54:33
	 * @param Long id 音频编码通道id
	 * @param Boolean removeChildren 是否删除子节点
	 * 	1.音频编码通道
	 *  2.音频编码通道视频编码通道关联
	 * 子节点包括：
	 *  3.视频编码通道
	 *  4.视频编码通道设备通道关联
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/remove/audio/encode")
	public Object removeAudioEncode(
			Long id,
			Boolean removeChildren,
			HttpServletRequest request) throws Exception{
		
		terminalChannelService.removeAudioEncode(id, removeChildren);
		return null;
	}
	
	/**
	 * 添加音频解码通道<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月13日 下午3:41:00
	 * @param Long terminalId 终端id
	 * @param String name 音频编码通道名称
	 * @param Long terminalAudioOutputId 音频输出id
	 * @param Long terminalBundleChannelId 终端设备通道id
	 * @return TerminalGraphNodeVO 拓补图节点
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add/audio/decode")
	public Object addAudioDecode(
			Long terminalId,
			String name,
			Long terminalAudioOutputId,
			Long terminalBundleChannelId,
			HttpServletRequest request) throws Exception{
		
		return terminalChannelService.addAudioDecode(terminalId, name, terminalAudioOutputId, terminalBundleChannelId);
	}
	
	/**
	 * 修改音频解码通道<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月18日 上午11:18:39
	 * @param Long id 音频解码通道id
	 * @param String name 音频解码通道名称
	 * @param Long terminalBundleChannelId 终端设备通道id
	 * @return TerminalGraphNodeVO 拓补图音频解码通道节点
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/edit/audio/decode")
	public Object editAudioDecode(
			Long id,
			String name,
			Long terminalBundleChannelId,
			HttpServletRequest request) throws Exception{
		
		return terminalChannelService.editAudioDecode(id, name, terminalBundleChannelId);
	}
	
	/**
	 * 移动音频解码通道<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月18日 上午11:27:59
	 * @param Long id 音频解码通道id
	 * @param Long terminalAudioOutputId 音频输出id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/move/audio/decode")
	public Object moveAudioDecode(
			Long id,
			Long terminalAudioOutputId,
			HttpServletRequest request) throws Exception{
		
		terminalChannelService.moveAudioDecode(id, terminalAudioOutputId);
		return null;
	}
	
	/**
	 * 删除音频解码<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月18日 上午11:37:23
	 * @param Long id 音频解码通道id
	 * 1.音频解码通道
	 * 2.音频输出与音频解码通道关联
	 * 3.音频解码通道与终端设备通道关联
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/remove/audio/decode")
	public Object removeAudioDecode(
			Long id,
			HttpServletRequest request) throws Exception{
		
		terminalChannelService.removeAudioDecode(id);
		return null;
	}
	
	/**
	 * 查询终端通道类型<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月18日 下午4:08:07
	 * @return Map<String, String> 通道类型列表
	 */
	/*@JsonBody
	@ResponseBody
	@RequestMapping(value = "/query/types")
	public Object queryTypes(HttpServletRequest request) throws Exception{
		TerminalChannelType[] values = TerminalChannelType.values();
		Map<String, String> types = new HashMap<String, String>();
		for(TerminalChannelType value:values){
			types.put(value.toString(), value.getName());
		}
		return types;
	}*/
	
	/**
	 * 查询终端下的视频解码通道<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月18日 下午4:01:59
	 * @param Long terminalId 终端id
	 * @return List<TerminalChannelVO> 视频解码通道列表
	 */
	/*@JsonBody
	@ResponseBody
	@RequestMapping(value = "/load/video/decode")
	public Object loadVideoDecode(
			Long terminalId,
			HttpServletRequest request) throws Exception{
		
		return terminalChannelQuery.loadVideoDecode(terminalId);
	}*/
	
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
	/*@JsonBody
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
	}*/
	
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
	/*@JsonBody
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
	}*/
	
	/**
	 * 删除终端通道<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月18日 下午4:42:03
	 * @param Long id 通道id
	 */
	/*@JsonBody
	@ResponseBody
	@RequestMapping(value = "/delete")
	public Object delete(Long id, HttpServletRequest request) throws Exception{
		
		terminalChannelService.delete(id);
		return null;
	}*/
	
	/**
	 * 查询通道以及所关联的设备类型通道（不关联屏幕）<br/>
	 * <b>作者:</b>lqxuhv<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年10月28日 下午8:41:18
	 * @param terminalId 终端id
	 * @return List<TerminalChannelVO> 视频解码通道列表
	 */
	/*@JsonBody
	@ResponseBody
	@RequestMapping(value = "/load/channel/permission")
	public Object loadChannelPermission(Long terminalId)throws Exception{
		return terminalChannelQuery.loadChannelPermission(terminalId);
	}
	
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add/channel/permission")
	public Object addChannelPermission(Long terminalId,String name,String type)throws Exception{
		return terminalChannelService.addChannelPermission();
	}*/
	
}
