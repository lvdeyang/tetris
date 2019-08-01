package com.sumavision.tetris.mims.app.media.picture.api.process;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sumavision.tetris.mims.app.media.picture.MediaPictureService;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

@Controller
@RequestMapping(value = "/api/process/picture")
public class ApiProcessPictureController {

	@Autowired
	private MediaPictureService mediaPictureService;
	
	/**
	 * 上传图片媒资审核通过<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年7月18日 下午2:16:01
	 * @param Long id 媒资id
	 * @return MediaPictureVO 媒资
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/upload/review/passed")
	public Object uploadReviewPassed(
			Long id,
			HttpServletRequest request) throws Exception{
		mediaPictureService.uploadReviewPassed(id);
		return null;
	}
	
	/**
	 * 图片媒资上传审核拒绝<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年7月18日 下午2:16:01
	 * @param Long id 媒资id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/upload/review/refuse")
	public Object uploadReviewRefuse(
			Long id,
			HttpServletRequest request) throws Exception{
		mediaPictureService.uploadReviewRefuse(id);
		return null;
	}
	
}
