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
	 * 方法概述<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>Administrator<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年9月4日 下午1:59:45
	 * @param taskNumber
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/query")
	public Object queryOutputSetting(Long taskNumber, HttpServletRequest request) throws Exception{
		
		return outputSettingQuery.query(taskNumber);
	}
	
	/**
	 * 
	 * 方法概述<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>Administrator<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年9月4日 下午1:59:53
	 * @param id
	 * @param outputProtocol
	 * @param outputAddress
	 * @param taskNumber
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/edit")
	public Object edit(
			Long id,
			String outputProtocol,
			String outputAddress,
			String rateCtrl,
			Long bitrate,
			HttpServletRequest request) throws Exception{
		return outputSettingService.edit(
				id,
				outputProtocol,
				outputAddress,
				rateCtrl,
				bitrate
				);
	}

	/**
	 * 
	 * 方法概述<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>Administrator<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年9月15日 下午2:09:00
	 * @param id
	 * @param codingObject
	 * @param profile
	 * @param fps
	 * @param bitrate
	 * @param resolution
	 * @param maxBitrate
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/editVideo")
	public Object editVideo(
			Long id,
			String codingObject,
			String profile,
			Long fps,
			Long bitrate,
			String resolution,
			Long maxBitrate,
			HttpServletRequest request) throws Exception{
		return outputSettingService.editVideo(
				id,
				codingObject,
				profile,
				fps,
				bitrate,
				resolution,
				maxBitrate);
		
	}
	
	/**
	 * 
	 * 方法概述<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>Administrator<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年9月15日 下午2:19:32
	 * @param id
	 * @param codingFormat
	 * @param sampleFmt
	 * @param bitrate
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
			String sampleFmt,
			String bitrate,
			String codingType,
			HttpServletRequest request) throws Exception{
		return outputSettingService.editAudio(
				id,
				codingFormat,
				sampleFmt,
				bitrate,
				codingType);
		
	}
	/**
	 * 
	 * 方法概述<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>Administrator<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年9月4日 下午2:01:16
	 * @param id
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/delete")
	public Object delete(
			Long id,
			HttpServletRequest request) throws Exception{
		outputSettingService.delete(id);
		return null;
	}
}
