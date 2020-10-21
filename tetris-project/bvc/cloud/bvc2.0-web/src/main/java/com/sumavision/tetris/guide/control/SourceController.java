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
 * <b>日期：</b>2020年9月3日 下午2:51:38
 */

@Controller
@RequestMapping(value = "/tetris/guide/control/source/po")
public class SourceController {

	@Autowired
	private SourceQuery sourceQuery;
	
	@Autowired
	private SourceService sourceService;
	
	/**
	 * 
	 * 方法概述<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>Administrator<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年9月3日 下午8:24:16
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/query")
	public Object querySource(Long id, HttpServletRequest request) throws Exception{
		
		return sourceQuery.query(id);
	}
	
	/**
	 * 
	 * 方法概述<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>Administrator<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年9月21日 下午3:42:43
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/queryDevice")
	public Object querySourceDevice() throws Exception{
		
		return sourceQuery.queryDevice();
	}
	
	/**
	 * 
	 * 方法概述<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>Administrator<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年9月3日 下午8:24:24
	 * @param sourceNumber
	 * @param sourceType
	 * @param sourceName
	 * @param sourceAddress
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
			String sourceType,
			String sourceName,
			String source,
			String previewOut,
			Boolean isPreviewOut,
			String sourceProtocol,
			HttpServletRequest request) throws Exception{
		return sourceService.edit(
				id,
				sourceType,
				sourceName,
				source,
				previewOut,
				isPreviewOut,
				sourceProtocol);
	}
	
	/**
	 * 
	 * 方法概述<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>Administrator<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年9月3日 下午8:24:33
	 * @param sourceNumber
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
		sourceService.delete(id);
		return null;
	}
	
	/**
	 * 
	 * 方法概述<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>Administrator<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年9月7日 下午5:17:10
	 * @param id
	 * @param sourceNumber
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/cut")
	public Object cut(
			Long id,
			HttpServletRequest request) throws Exception{
		return sourceService.cut(id,OutType.SWITCH);
	}
	
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/pvmcut")
	public Object pvmcut(
			Long id,
			HttpServletRequest request) throws Exception{
		return sourceService.cut(id,OutType.MONITOR);
	}
	
}
