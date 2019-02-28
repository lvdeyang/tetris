package com.sumavision.tetris.mims.app.media.txt;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sumavision.tetris.mvc.ext.response.parser.JsonBodyResponseParser;

@Component
public class MediaTxtQuery {

	@Autowired
	private MediaTxtFeign mediaTxtFeign;
	
	/**
	 * 加载文件夹下的文本媒资<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月6日 下午4:03:27
	 * @param folderId 文件夹id
	 * @return rows List<MediaTxtVO> 文本媒资列表
	 * @return breadCrumb FolderBreadCrumbVO 面包屑数据
	 */
	public Map<String, Object> load(Long folderId) throws Exception{
		return JsonBodyResponseParser.parseObject(mediaTxtFeign.load(folderId), Map.class);
	}
	
	/**
	 * 查询文本内容<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年2月14日 上午9:25:28
	 * @param Long id 媒资id
	 * @return String 文本内容
	 */
	public String queryContent(Long id) throws Exception{
		return JsonBodyResponseParser.parseObject(mediaTxtFeign.queryContent(id), String.class);
	}
	
}
