package com.sumavision.tetris.mims.app.media.video.api.qt;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.sumavision.tetris.mims.app.media.video.MediaVideoQuery;
import com.sumavision.tetris.mims.app.media.video.MediaVideoService;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;

@Controller
@RequestMapping(value = "/api/qt/media/video")
public class ApiQtMediaVideoController {
	@Autowired
	private UserQuery userQuery;

	@Autowired
	private MediaVideoQuery mediaVideoQuery;
	
	@Autowired
	private MediaVideoService mediaVideoService;
	
	/**
	 * 加载所有的视频媒资<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月6日 下午4:03:27
	 * @return List<MediaVideoVO> 视频媒资列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/load/all")
	public Object loadAll(HttpServletRequest request) throws Exception{
		
		return mediaVideoQuery.loadAll();
		
	}
	
	/**
	 * 加载所有的视频媒资目录<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月6日 下午4:03:27
	 * @return List<MediaVideoVO> 视频媒资列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/load/folder/all")
	public Object loadAllFolder(HttpServletRequest request) throws Exception{
		
		return mediaVideoQuery.loadAllFolder();
		
	}
	
	
	/**
	 * 根据视频媒资列表批量加载的视频媒资<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月27日 下午4:03:27
	 * @param String urlList 视频媒资http地址由","连接
	 * @return List<MediaVideoVO> 视频媒资列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add/by/transcoding")
	public Object addByTranscoding(String urlList, HttpServletRequest request) throws Exception {
		UserVO userVO = userQuery.current();
		
		List<String> urls = Arrays.asList(urlList.split(","));
		
		return mediaVideoService.addList(userVO, urls);
	}
	
}
