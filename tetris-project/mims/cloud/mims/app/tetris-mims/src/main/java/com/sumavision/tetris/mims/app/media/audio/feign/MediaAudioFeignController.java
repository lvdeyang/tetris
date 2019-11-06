package com.sumavision.tetris.mims.app.media.audio.feign;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.sumavision.tetris.mims.app.media.audio.MediaAudioDAO;
import com.sumavision.tetris.mims.app.media.audio.MediaAudioPO;
import com.sumavision.tetris.mims.app.media.audio.MediaAudioQuery;
import com.sumavision.tetris.mims.app.media.audio.MediaAudioVO;
import com.sumavision.tetris.mims.app.media.audio.exception.MediaAudioNotExistException;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;

@Controller
@RequestMapping(value = "/media/audio/feign")
public class MediaAudioFeignController {

	@Autowired
	private MediaAudioQuery mediaAudioQuery;
	
	@Autowired
	private MediaAudioDAO mediaAudioDAO;
	
	@Autowired
	private UserQuery userQuery;
	
	/**
	 * 加载文件夹下的音频媒资<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月6日 下午4:03:27
	 * @param folderId 文件夹id
	 * @return rows List<MediaAudioVO> 音频媒资列表
	 * @return breadCrumb FolderBreadCrumbVO 面包屑数据
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/load")
	public Object load(
			Long folderId,
			HttpServletRequest request) throws Exception{
		
		return mediaAudioQuery.load(folderId);
	}
	
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
	 * 根据uuid获取媒资信息<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年8月6日 下午4:03:27
	 * @return List<MediaAudioVO> 音频媒资列表
	 * @throws Exception 
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/quest/by/uuid")
	public Object getByUuids(String uuids) throws Exception{
		List<String> uuidList = JSON.parseArray(uuids, String.class);
		
		return MediaAudioVO.getConverter(MediaAudioVO.class).convert(mediaAudioQuery.questByUuid(uuidList), MediaAudioVO.class);
	}
	
	/**
	 * 根据id获取媒资信息<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月9日 下午2:31:07
	 * @param id 媒资id
	 * @return MediaAudioVO 媒资信息 
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/quest/by/id")
	public Object getById(Long id) throws Exception{
		MediaAudioPO audio = mediaAudioDAO.findOne(id);
		if (audio == null) throw new MediaAudioNotExistException(id);
		
		return new MediaAudioVO().set(audio);
	}
	
	/**
	 * 生成文件存储预览路径(云转码使用)<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年8月7日 下午4:03:27
	 * @return String 预览路径
	 * @throws Exception 
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/build/url")
	public Object buildUrl(String name, String folderUuid, HttpServletRequest request) throws Exception {
		return mediaAudioQuery.buildUrl(name, folderUuid);
	};
	
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
	 * 根据预览地址查询音频列表<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月5日 上午10:17:18
	 * @param JSONArray previewUrls 预览地址列表
	 * @return List<MediaAudioVO> 音频列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/find/by/preview/url/in")
	public Object findByPreviewUrlIn(
			String previewUrls, 
			HttpServletRequest request) throws Exception{
		return mediaAudioQuery.findByPreviewUrlIn(JSON.parseArray(previewUrls, String.class));
	}
	
	/**
	 * 获取下载量排名列表<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月5日 下午4:34:26
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/load/hot")
	public Object loadHot(HttpServletRequest request) throws Exception {
		UserVO user = userQuery.current();
		List<MediaAudioVO> audioVOs = mediaAudioQuery.loadHotList();
		mediaAudioQuery.queryEncodeUrl(audioVOs);
		return audioVOs;
	}
	
}
