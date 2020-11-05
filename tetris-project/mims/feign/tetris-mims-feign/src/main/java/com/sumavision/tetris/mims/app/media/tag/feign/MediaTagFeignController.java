package com.sumavision.tetris.mims.app.media.tag.feign;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sumavision.tetris.mims.app.media.tag.MediaTagFeign;
import com.sumavision.tetris.mims.app.media.tag.TagVO;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.mvc.ext.response.parser.JsonBodyResponseParser;

@Controller
@RequestMapping(value = "/media/tag/feign")
public class MediaTagFeignController {
	@Autowired
	private MediaTagFeign mediaTagFeign;
	
	/**
	 * 获取标签树<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年8月11日 下午4:03:27
	 * @return List<TagVO> 标签树
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/list/get")
	public Object getTagList(HttpServletRequest request) throws Exception{
		return JsonBodyResponseParser.parseArray(mediaTagFeign.getTagList(), TagVO.class);
	}
}
