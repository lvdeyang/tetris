package com.sumavision.tetris.mims.app.media.audio.api.screen.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sumavision.tetris.mims.app.media.audio.MediaAudioQuery;
import com.sumavision.tetris.mims.app.media.audio.MediaAudioVO;
import com.sumavision.tetris.mims.app.media.recommend.statistics.MediaRecommendStatisticsQuery;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;

@Controller
@RequestMapping(value = "/api/screen/web/media/audio")
public class ApiScreenWebMediaAudioController {
	@Autowired
	private UserQuery userQuery;
	
	@Autowired
	private MediaAudioQuery mediaAudioQuery;
	
	@Autowired
	private MediaRecommendStatisticsQuery mediaRecommendStatisticsQuery;
	
	/**
	 * 获取推荐列表<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月5日 下午4:31:41
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/load/recommend")
	public Object loadRecommend(HttpServletRequest request) throws Exception{
		UserVO user = userQuery.current();
		List<MediaAudioVO> audioVOs = mediaAudioQuery.loadRecommendWithWeight(user);
		mediaAudioQuery.queryEncodeUrl(audioVOs);
		return audioVOs;
	}
	
	/**
	 * 获取推荐列表<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月5日 下午4:31:41
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/load/recommend/statistics/year")
	public Object loadRecommendStatisticsByYear(HttpServletRequest request) throws Exception{
		return mediaRecommendStatisticsQuery.queryRecommendStatisticsByYear();
	}
	
	/**
	 * 获取推荐列表<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月5日 下午4:31:41
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/load/recommend/statistics/area")
	public Object loadRecommendStatisticsByArea(HttpServletRequest request) throws Exception{
		return mediaRecommendStatisticsQuery.queryRecommendStatisticsByArea();
	}
	
	/**
	 * 获取资源上传者的资源上传量<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月23日 下午5:10:15
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/load/author/permission")
	public Object loadAuthorPermission(HttpServletRequest request) throws Exception {
		return mediaAudioQuery.loadAuthorPermission();
	}
}
