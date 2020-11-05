package com.sumavision.tetris.mims.app.media.api.qt;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.mims.app.folder.FolderDAO;
import com.sumavision.tetris.mims.app.media.audio.MediaAudioItemType;
import com.sumavision.tetris.mims.app.media.audio.MediaAudioQuery;
import com.sumavision.tetris.mims.app.media.audio.MediaAudioService;
import com.sumavision.tetris.mims.app.media.audio.MediaAudioVO;
import com.sumavision.tetris.mims.app.media.encode.FileEncodeService;
import com.sumavision.tetris.mims.app.media.video.MediaVideoQuery;
import com.sumavision.tetris.mims.app.media.video.MediaVideoService;
import com.sumavision.tetris.mims.app.media.video.MediaVideoVO;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;

/**
 * qt素材库接口<br/>
 * <b>作者:</b>lzp<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年3月11日 下午1:12:05
 */
@Controller
@RequestMapping(value = "/api/qt/media")
public class ApiQtMediaController {
	
	@Autowired
	private UserQuery userQuery;
	
	@Autowired
	private MediaVideoQuery mediaVideoQuery;
	
	@Autowired
	private MediaAudioQuery mediaAudioQuery;
	
	/**
	 * 加载所有的视频和音频媒资<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年6月4日 下午4:03:27
	 * @return List<MediaVideoVO> videos 视频媒资列表
	 * @return List<MediaAudioVO> audios 音频媒资列表
	 * @return List<MediaAudioVO> recommend 根据标签分类
	 * @return List<MediaAudioVO> hot 根据下载数量获取
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/va/load/all")
	public Object loadAllTest(HttpServletRequest request) throws Exception{
		UserVO user = userQuery.current();
		
		List<MediaVideoVO> videoVOs = mediaVideoQuery.loadAll();
		List<MediaAudioVO> audioVOs = mediaAudioQuery.loadAll();
		List<MediaAudioVO> recommend = mediaAudioQuery.loadRecommendWithWeight(user);
		mediaAudioQuery.queryEncodeUrl(recommend);
		mediaAudioQuery.queryEncodeUrl(audioVOs);
		
		MediaAudioVO recommendRoot = new MediaAudioVO();
		recommendRoot.setName("推荐");
		recommendRoot.setType(MediaAudioItemType.FOLDER.toString());
		recommendRoot.setChildren(recommend);
		
		return new HashMapWrapper<String, Object>().put("videos", videoVOs)
				   .put("audios", audioVOs)
				   .put("recommend", new ArrayListWrapper<MediaAudioVO>().add(recommendRoot).getList())
				   .getMap();
		
	}
	
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/encode/key")
	public Object queryEncodeKey(HttpServletRequest request) throws Exception {
		return FileEncodeService.AES_KEY;
	}
}
