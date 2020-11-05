package com.sumavision.tetris.mims.app.media.txt.feign;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sumavision.tetris.mims.app.media.txt.MediaTxtQuery;
import com.sumavision.tetris.mims.app.media.txt.MediaTxtService;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

@Controller
@RequestMapping(value = "/media/txt/feign")
public class MediaTxtFeignController {

	@Autowired
	private MediaTxtQuery mediaTxtQuery;
	
	@Autowired
	private MediaTxtService mediaTxtService;
	
	/**
	 * 加载文件夹下的文本媒资<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月6日 下午4:03:27
	 * @param folderId 文件夹id
	 * @return rows List<MediaTxtVO> 文本媒资列表
	 * @return breadCrumb FolderBreadCrumbVO 面包屑数据
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/load")
	public Object load(
			Long folderId,
			HttpServletRequest request) throws Exception{
		
		return mediaTxtQuery.load(folderId);
	}
	
	/**
	 * 查询文本内容<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年2月14日 上午9:25:28
	 * @param Long id 媒资id
	 * @return String 文本内容
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/query/content")
	public Object queryContent(
			Long id,
			HttpServletRequest request) throws Exception{
		
		return mediaTxtQuery.queryContent(id);
	}
	
	/**
	 * 数据库添加json文件<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月26日 上午10:26:53
	 * @param String jsonContent json内容
	 * @param Long folderId 目录id
	 * @param String name 文件名
	 * @return MediaTxtVO
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add/json")
	public Object addJson(String json, Long folderId, String name, HttpServletRequest request) throws Exception {
		return mediaTxtService.addPushJson(json, folderId, name);
	}
}
