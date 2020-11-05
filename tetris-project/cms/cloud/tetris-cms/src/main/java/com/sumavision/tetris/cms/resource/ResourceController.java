package com.sumavision.tetris.cms.resource;

import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

@Controller
@RequestMapping(value = "/cms/resource")
public class ResourceController {

	/**
	 * 查询测试文本列表<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年2月20日 下午2:36:11
	 * @return List<TextVO> 测试文本列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/list/text")
	public Object listText(HttpServletRequest request) throws Exception{
		return Text.list();
	}
	
	/**
	 * 查询测试图片列表<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年2月20日 下午2:36:53
	 * @return List<ImageVO> 测试图片列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/list/image")
	public Object listImage(HttpServletRequest request) throws Exception{
		return Image.list();
	}
	
	/**
	 * 查询编辑器列表<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年2月21日 下午3:08:39
	 * @return List<EditorVO> 编辑器列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/list/editor")
	public Object listEditor(HttpServletRequest request) throws Exception{
		return Editor.list();
	}
	
}
