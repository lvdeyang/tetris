package com.sumavision.tetris.mims.app.media.audio.feign;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mims.app.media.audio.MediaAudioDAO;
import com.sumavision.tetris.mims.app.media.audio.MediaAudioPO;
import com.sumavision.tetris.mims.app.media.audio.MediaAudioQuery;
import com.sumavision.tetris.mims.app.media.audio.MediaAudioService;
import com.sumavision.tetris.mims.app.media.audio.MediaAudioVO;
import com.sumavision.tetris.mims.app.media.audio.exception.MediaAudioNotExistException;
import com.sumavision.tetris.mims.app.media.recommend.statistics.MediaRecommendStatisticsDAO;
import com.sumavision.tetris.mims.app.media.recommend.statistics.MediaRecommendStatisticsPO;
import com.sumavision.tetris.mims.app.media.tag.TagQuery;
import com.sumavision.tetris.mims.app.media.tag.TagVO;
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
	
	@Autowired
	private MediaAudioService mediaAudioService;
	
	@Autowired
	private TagQuery tagQuery;
	
	@Autowired
	private MediaRecommendStatisticsDAO mediaRecommendStatisticsDAO;
	
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
		
		String date = DateUtil.format(new Date(), DateUtil.defaultDatePattern);
		String groupId = user.getGroupId();
		Long userId = user.getId();
		MediaRecommendStatisticsPO recommendStatisticsPO = 
				mediaRecommendStatisticsDAO.findByDateAndUserId(date, userId);
		if (recommendStatisticsPO == null) {
			recommendStatisticsPO = new MediaRecommendStatisticsPO();
			recommendStatisticsPO.setDate(date);
			recommendStatisticsPO.setUserId(userId);
			recommendStatisticsPO.setGroupId(groupId);
			recommendStatisticsPO.setCount(1l);
		} else {
			recommendStatisticsPO.setCount(recommendStatisticsPO.getCount() + 1);
		}
		mediaRecommendStatisticsDAO.save(recommendStatisticsPO);
		
		return audioVOs;
	}
	
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/load/recommendnew")
	public Object loadRecommendnew(HttpServletRequest request) throws Exception{
		UserVO user = userQuery.current();
		List<MediaAudioVO> audioVOs = mediaAudioQuery.loadRecommendWithNew(user);
		mediaAudioQuery.queryEncodeUrl(audioVOs);
		
		String date = DateUtil.format(new Date(), DateUtil.defaultDatePattern);
		String groupId = user.getGroupId();
		Long userId = user.getId();
		MediaRecommendStatisticsPO recommendStatisticsPO = 
				mediaRecommendStatisticsDAO.findByDateAndUserId(date, userId);
		if (recommendStatisticsPO == null) {
			recommendStatisticsPO = new MediaRecommendStatisticsPO();
			recommendStatisticsPO.setDate(date);
			recommendStatisticsPO.setUserId(userId);
			recommendStatisticsPO.setGroupId(groupId);
			recommendStatisticsPO.setCount(1l);
		} else {
			recommendStatisticsPO.setCount(recommendStatisticsPO.getCount() + 1);
		}
		mediaRecommendStatisticsDAO.save(recommendStatisticsPO);
		
		return audioVOs;
	}
	
	
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/load/tag/recommend")
	public Object loadTagRecommend(HttpServletRequest request,String tags) throws Exception{
		UserVO user = userQuery.current();
		List<MediaAudioVO> audioVOs = mediaAudioQuery.loadRecommendWithtags(user, tags);
		mediaAudioQuery.queryEncodeUrl(audioVOs);
		
		String date = DateUtil.format(new Date(), DateUtil.defaultDatePattern);
		String groupId = user.getGroupId();
		Long userId = user.getId();
		MediaRecommendStatisticsPO recommendStatisticsPO = 
				mediaRecommendStatisticsDAO.findByDateAndUserId(date, userId);
		if (recommendStatisticsPO == null) {
			recommendStatisticsPO = new MediaRecommendStatisticsPO();
			recommendStatisticsPO.setDate(date);
			recommendStatisticsPO.setUserId(userId);
			recommendStatisticsPO.setGroupId(groupId);
			recommendStatisticsPO.setCount(1l);
		} else {
			recommendStatisticsPO.setCount(recommendStatisticsPO.getCount() + 1);
		}
		mediaRecommendStatisticsDAO.save(recommendStatisticsPO);
		
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
	
	/**
	 * 增加下载数<br/>
	 * <b>作者:</b>Mr<br/>
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
	 * 添加远程媒资(收录系统使用)<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年9月3日 下午5:24:19
	 * @param String name 媒资名称
	 * @param Long tagId 标签id 
	 * @param String httpUrl 媒资预览地址
	 * @param String ftpUrl 媒资ftp地址
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add/remote")
	public Object addRemote(String name, Long tagId, String httpUrl, String ftpUrl, HttpServletRequest request) throws Exception {
		UserVO user = userQuery.current();
		
		TagVO tag = tagQuery.queryById(tagId);
		
		return mediaAudioService.addTask(user, name, tag == null ? "" : tag.getName(), httpUrl, ftpUrl == null ? "" : ftpUrl);
	}
	
	/**
	 * 根据id数组删除媒资<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月4日 上午9:12:35
	 * @param List<Long> ids 预删除媒资id数组
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/remove")
	public void remove(String ids, HttpServletRequest request) throws Exception {
		if (ids == null || ids.isEmpty()) return;
		
		List<Long> idList = JSONArray.parseArray(ids, Long.class);
		
		mediaAudioService.remove(idList);
	}
}
