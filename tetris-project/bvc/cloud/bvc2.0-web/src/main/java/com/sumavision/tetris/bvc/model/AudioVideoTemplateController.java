package com.sumavision.tetris.bvc.model;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

@Controller
@RequestMapping(value = "/tetris/bvc/model/audio/video/template")
public class AudioVideoTemplateController {

	@Autowired
	private AudioVideoTemplateQuery audioVideoTemplateQuery;
	
	@Autowired
	private AudioVideoTemplateService audioVideoTemplateService;
	
	/**
	 * 查询枚举类型<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月30日 下午1:23:08
	 * @return audioFormats Map<String, String> 音频编码格式
	 * @return resolutions Map<String, String> 分辨率
	 * @return usageTypes Map<String, String> 模板用途
	 * @return videoFormats Map<String, String> 视频编码格式
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/query/types")
	public Object queryTypes(HttpServletRequest request) throws Exception{
		
		return audioVideoTemplateQuery.queryTypes();
	}
	
	/**
	 * 分页查询参数模板<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月30日 下午1:44:01
	 * @param int currentPage 当前页
	 * @param int pageSize 每页数据量
	 * @return total long 总数据量
	 * @return rows List<AudioVideoTemplateVO> 模板列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/load")
	public Object load(
			int currentPage, 
			int pageSize,
			HttpServletRequest request) throws Exception{
		
		return audioVideoTemplateQuery.load(currentPage, pageSize);
	}
	
	/**
	 * 添加参数模板<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月30日 下午1:59:21
	 * @param String name 名称
	 * @param String videoFormat 视频格式
	 * @param String videoFormatSpare 备用视频格式
	 * @param String audioFormat 音频格式
	 * @param Boolean mux 是否端口复用
	 * @param String videoBitRate 视频码率
	 * @param String videoBitRateSpare 备用视频码率
	 * @param String videoResolution 视频分辨率
	 * @param String videoResolutionSpare 备用视频分辨率
	 * @param String fps 帧率
	 * @param String audioBitRate 音频码率
	 * @param String usageType 模板用途
	 * @param Boolean isTemplate 是否是模板
	 * @return AudioVideoTemplateVO 参数模板
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add")
	public Object add(
			String name,
			String videoFormat,
			String videoFormatSpare,
			String audioFormat,
			Boolean mux,
			String videoBitRate,
			String videoBitRateSpare,
			String videoResolution,
			String videoResolutionSpare,
			String fps,
			String audioBitRate,
			String usageType,
			Boolean isTemplate,
			HttpServletRequest request) throws Exception{
		
		return audioVideoTemplateService.add(
				name, 
				videoFormat, 
				videoFormatSpare, 
				audioFormat, 
				mux, 
				videoBitRate, 
				videoBitRateSpare, 
				videoResolution, 
				videoResolutionSpare, 
				fps, 
				audioBitRate, 
				usageType, 
				isTemplate);
	}
	
	/**
	 * 修改参数模板<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月30日 下午1:59:21
	 * @param Long id 模板id
	 * @param String name 名称
	 * @param String videoFormat 视频格式
	 * @param String videoFormatSpare 备用视频格式
	 * @param String audioFormat 音频格式
	 * @param Boolean mux 是否端口复用
	 * @param String videoBitRate 视频码率
	 * @param String videoBitRateSpare 备用视频码率
	 * @param String videoResolution 视频分辨率
	 * @param String videoResolutionSpare 备用视频分辨率
	 * @param String fps 帧率
	 * @param String audioBitRate 音频码率
	 * @param String usageType 模板用途
	 * @param Boolean isTemplate 是否是模板
	 * @return AudioVideoTemplateVO 参数模板
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/edit")
	public Object edit(
			Long id,
			String name,
			String videoFormat,
			String videoFormatSpare,
			String audioFormat,
			Boolean mux,
			String videoBitRate,
			String videoBitRateSpare,
			String videoResolution,
			String videoResolutionSpare,
			String fps,
			String audioBitRate,
			String usageType,
			Boolean isTemplate,
			HttpServletRequest request) throws Exception{
		
		return audioVideoTemplateService.edit(
				id, 
				name, 
				videoFormat, 
				videoFormatSpare, 
				audioFormat, 
				mux, 
				videoBitRate, 
				videoBitRateSpare, 
				videoResolution, 
				videoResolutionSpare, 
				fps, 
				audioBitRate, 
				usageType, 
				isTemplate);
	}
	
	/**
	 * 删除音视频参数模板<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月30日 下午2:08:57
	 * @param Long id 模板id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/delete")
	public Object delete(
			Long id, 
			HttpServletRequest request) throws Exception{
		
		audioVideoTemplateService.delete(id);
		return null;
	}
	
}
