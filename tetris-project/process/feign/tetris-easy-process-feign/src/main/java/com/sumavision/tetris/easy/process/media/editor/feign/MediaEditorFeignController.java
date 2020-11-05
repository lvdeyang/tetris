package com.sumavision.tetris.easy.process.media.editor.feign;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sumavision.tetris.easy.process.media.editor.MediaEditorQuery;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

@Controller
@RequestMapping(value = "/media/editor/feign")
public class MediaEditorFeignController {
	@Autowired
	private MediaEditorQuery mediaEditorQuery;
	
	/**
	 * 获取云转码模板列表<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:06:57
	 * @return List<String>
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/template/list")
	public Object getTemplates(HttpServletRequest request) throws Exception {
		return mediaEditorQuery.getTemplates();
	}
}
