package com.sumavision.tetris.mims.app.media.compress.feign;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sumavision.tetris.mims.app.media.compress.MediaCompressQuery;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

@Controller
@RequestMapping(value = "/media/compress/feign")
public class MediaCompressFeignController {
	@Autowired
	private MediaCompressQuery mediaCompressQuery;
	
	/**
	 * 加载文件夹下的播发媒资<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月6日 下午4:03:27
	 * @param folderId 文件夹id
	 * @return rows List<MediaCompressVO> 音频播发列表
	 * @return breadCrumb FolderBreadCrumbVO 面包屑数据
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/load")
	public Object load(
			Long folderId,
			HttpServletRequest request) throws Exception{
		
		return mediaCompressQuery.load(folderId);
	}
}
