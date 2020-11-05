/**
 * 
 */
package com.sumavision.tetris.guide.control;



import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

/**
 * 类型概述<br/>
 * <p>详细描述</p>
 * <b>作者:</b>Administrator<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年9月4日 上午10:09:04
 */
@Controller
@RequestMapping(value = "/tetris/guide/control/output/setting/po")
public class OutputSettingController {
	
	@Autowired
	private OutputSettingQuery outputSettingQuery;
	
	@Autowired
	private OutputSettingService outputSettingService;
	
	/**
	 * 
	 * 查询输出<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>Administrator<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年9月30日 下午4:12:03
	 * @param groupId 输出组id
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/query")
	public Object queryOutputSetting(Long groupId, HttpServletRequest request) throws Exception{
		
		return outputSettingQuery.query(groupId);
	}
	
	/**
	 * 
	 * 方法概述<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>Administrator<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年9月30日 下午4:13:19
	 * @param groupId
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/queryVideo")
	public Object queryVideoParameter(Long groupId, HttpServletRequest request) throws Exception{
		
		return outputSettingQuery.queryVideo(groupId);
	}
	
	/**
	 * 
	 * 方法概述<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>Administrator<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年9月30日 下午4:13:47
	 * @param groupId
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/queryAudio")
	public Object queryAudioParameter(Long groupId, HttpServletRequest request) throws Exception{
		 
		return outputSettingQuery.queryAudio(groupId);
	}
	
	/**
	 * 
	 * 新建输出<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>Administrator<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年9月30日 下午4:14:03
	 * @param groupId 输出组id
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add")
	public Object add(Long groupId, HttpServletRequest request) throws Exception{
		
		return outputSettingService.addOutput(groupId);
	
	}

	/**
	 * 
	 * 修改输出<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>Administrator<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年9月30日 下午4:14:48
	 * @param outputs
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/edit")
	public Object edit(
			String outputs,
			HttpServletRequest request) throws Exception{
		return outputSettingService.edit(outputs);
	}

	/**
	 * 
	 * 方法概述<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>Administrator<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年9月30日 下午4:15:28
	 * @param id
	 * @param codingObject
	 * @param fps
	 * @param bitrate
	 * @param resolution
	 * @param ratio
	 * @param rcMode
	 * @param maxBitrate
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/editVideo")
	public Object editVideo(
			Long id,
			String codingObject,
			String fps,
			Long bitrate,
			String resolution,
			String ratio,
			String rcMode,
			Long maxBitrate,
			HttpServletRequest request) throws Exception{
		return outputSettingService.editVideo(
				id,
				codingObject,
				fps,
				bitrate,
				resolution,
				ratio,
				rcMode,
				maxBitrate);
		
	}
	
	/**
	 * 
	 * 方法概述<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>Administrator<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年9月30日 下午4:15:39
	 * @param id
	 * @param codingFormat
	 * @param channelLayout
	 * @param bitrate
	 * @param sampleRate
	 * @param codingType
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/editAudio")
	public Object editAudio(
			Long id,
			String codingFormat,
			String channelLayout,
			String bitrate,
			String sampleRate,
			String codingType,
			HttpServletRequest request) throws Exception{
		return outputSettingService.editAudio(
				id,
				codingFormat,
				channelLayout,
				bitrate,
				sampleRate,
				codingType);
		
	}

	/**
	 * 
	 * 删除输出<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>Administrator<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年9月30日 下午4:15:58
	 * @param ids
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/delete")
	public Object delete(
			String ids,
			HttpServletRequest request) throws Exception{
		outputSettingService.deleteOutput(ids);
		return null;
	}
}
