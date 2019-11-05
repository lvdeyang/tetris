package com.sumavision.tetris.mims.app.media.picture.feign;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.sumavision.tetris.mims.app.media.picture.MediaPictureQuery;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

@Controller
@RequestMapping(value = "/media/picture/feign")
public class MediaPictureFeignController {

	@Autowired
	private MediaPictureQuery mediaPictureQuery;
	
	/**
	 * 查询媒资库图片feign接口<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年2月26日 下午5:30:44
	 * @param Long folderId 当前文件夹id
	 * @return rows List<MediaPictureVO> 媒资项目列表
	 * @return breadCrumb FolderBreadCrumbVO 面包屑数据
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/load")
	public Object load(
			Long folderId, 
			HttpServletRequest request) throws Exception{
		
		
		return mediaPictureQuery.load(folderId);
	}
	
	/**
	 * 根据预览地址查询图片列表<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月5日 上午10:34:58
	 * @param JSONString previewUrls 预览地址列表
	 * @return List<MediaPictureVO> 图片列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/find/by/preview/url/in")
	public Object findByPreviewUrlIn(
			String previewUrls,
			HttpServletRequest request) throws Exception{
		return mediaPictureQuery.findByPreviewUrlIn(JSON.parseArray(previewUrls, String.class));
	}
	
}
