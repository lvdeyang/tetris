package com.sumavision.tetris.mims.app.media.stream.video.process;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.sumavision.tetris.mims.app.media.stream.video.MediaVideoStreamService;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

@Controller
@RequestMapping(value = "/api/process/video/stream")
public class ApiProcessVideoStreamController {

	@Autowired
	private MediaVideoStreamService mediaVideoStreamService;
	
	/**
	 * 上传视频流媒资审核通过<br/>
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
		mediaVideoStreamService.uploadReviewPassed(id);
		return null;
	}
	
	/**
	 * 视频流媒资上传审核拒绝<br/>
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
		mediaVideoStreamService.uploadReviewRefuse(id);
		return null;
	}
	
	/**
	 * 视频流媒资修改审核通过<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年8月1日 上午9:01:51
	 * @param Long id 视频流媒资id
	 * @param String name 媒资名称
	 * @param String tags 贴标签，以“,”分隔
	 * @param String keyWords 关键字，以“,”分隔
	 * @param String remarks 备注
	 * @param JSONString previewUrls 预览url列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/edit/review/passed")
	public Object editReviewPassed(
			Long id,
			String name,
			String tags,
			String keyWords,
			String remarks,
			String previewUrls,
			HttpServletRequest request) throws Exception{
		List<String> urls = JSON.parseArray(previewUrls, String.class);
		mediaVideoStreamService.editReviewPassed(id, name, tags, keyWords, remarks, urls);
		return null;
	}
	
	/**
	 * 视频流媒资修改审核拒绝<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年8月1日 上午9:19:16
	 * @param Long id 视频流媒资id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/edit/review/refuse")
	public Object editReviewRefuse(
			Long id,
			HttpServletRequest request) throws Exception{
		mediaVideoStreamService.editReviewRefuse(id);
		return null;
	}
	
	/**
	 * 视频流媒资删除审核通过<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年8月1日 上午9:01:51
	 * @param Long id 视频流媒资id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/delete/review/passed")
	public Object deleteReviewPassed(
			Long id,
			HttpServletRequest request) throws Exception{
		mediaVideoStreamService.deleteReviewPassed(id);
		return null;
	}
	
	/**
	 * 视频流媒资删除审核拒绝<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年8月1日 上午9:22:22
	 * @param Long id 视频流媒资id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/delete/review/refuse")
	public Object deleteReviewRefuse(
			Long id,
			HttpServletRequest request) throws Exception{
		mediaVideoStreamService.deleteReviewRefuse(id);
		return null;
	}
	
}
