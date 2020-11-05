package com.sumavision.tetris.easy.process.media.editor;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sumavision.tetris.mvc.ext.response.parser.JsonBodyResponseParser;

@Component
public class MediaEditorQuery {
	@Autowired
	private MediaEditorFeign mediaEditorFeign;
	
	/**
	 * 获取云转码模板列表<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:06:57
	 * @return List<String>
	 */
	public List<String> getTemplates() throws Exception{
		return JsonBodyResponseParser.parseArray(mediaEditorFeign.getTemplates(), String.class);
	}
}
