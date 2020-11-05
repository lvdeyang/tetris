package com.sumavision.tetris.mims.app.media.audio.api.qt;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sumavision.tetris.mims.app.boss.MediaType;
import com.sumavision.tetris.mims.app.boss.QdBossService;
import com.sumavision.tetris.mims.app.media.audio.MediaAudioQuery;
import com.sumavision.tetris.mims.app.media.audio.MediaAudioService;
import com.sumavision.tetris.mims.app.media.audio.MediaAudioVO;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;

@Controller
@RequestMapping(value = "/api/qt/media/audio")
public class ApiQtMediaAudioController {

	@Autowired
	private MediaAudioQuery mediaAudioQuery;
	
	@Autowired
	private MediaAudioService mediaAudioService;
	
	@Autowired
	private UserQuery userQuery;
	
	@Autowired
	private QdBossService bossService;
	
	/**
	 * 加载所有的音频媒资<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月6日 下午4:03:27
	 * @return List<MediaAudioVO> 视频媒资列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/load/all")
	public Object loadAll(HttpServletRequest request) throws Exception{
		
		List<MediaAudioVO> audioVOs = mediaAudioQuery.loadAll();
		mediaAudioQuery.queryEncodeUrl(audioVOs);
		
		return audioVOs;
	}
	
	/**
	 * 加载所有的音频媒资目录<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月6日 下午4:03:27
	 * @return List<MediaAudioVO> 音频媒资列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/load/folder/all")
	public Object loadAllFolder(HttpServletRequest request) throws Exception{
		
		List<MediaAudioVO> audioVOs = mediaAudioQuery.loadAllFolder();
		mediaAudioQuery.queryEncodeUrl(audioVOs);
		
		return audioVOs;
	}
	
	/**
	 * 加载所有的音频媒资(根据标签分类)<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年8月11日 下午4:03:27
	 * @return List<MediaAudioVO> 视频媒资列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/load/all/by/tags")
	public Object loadAllByTags(HttpServletRequest request) throws Exception{
		UserVO user = userQuery.current();
		List<MediaAudioVO> audioVOs = mediaAudioQuery.loadAllByUserTags(user);
		mediaAudioQuery.queryEncodeUrl(audioVOs);
		return audioVOs;
	}
	
	/**
	 * 增加下载数<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年8月15日 下午5:11:49
	 * @param Long id 下载的音频id
	 * @return MediaAudioVO 音频
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/download")
	public Object downloadAdd(Long id, HttpServletRequest request) throws Exception{
		UserVO user = userQuery.current();
		return mediaAudioService.downloadAdd(user, id);
	}
	
	/**
	 * qt终端点播时调用
	 * 方法概述<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>Mr.h<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年8月19日 下午2:07:01
	 * @param id 音频媒资id
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/play")
	public Object play(Long id, HttpServletRequest request) throws Exception{
		UserVO user = userQuery.current();
		bossService.playMedia(id, user.getUuid(), MediaType.AUDIO);
		return "success";
	}
	
}
