package com.sumavision.tetris.mims.app.media.api.process;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.commons.util.httprequest.HttpRequestUtil;
import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.commons.util.wrapper.StringBuilderWrapper;
import com.sumavision.tetris.mims.app.folder.FolderDAO;
import com.sumavision.tetris.mims.app.folder.FolderPO;
import com.sumavision.tetris.mims.app.folder.FolderType;
import com.sumavision.tetris.mims.app.media.api.process.exception.ProcessFileSizeOverCommitException;
import com.sumavision.tetris.mims.app.media.api.process.exception.ProcessQuestToOldMimsFailException;
import com.sumavision.tetris.mims.app.media.audio.MediaAudioPO;
import com.sumavision.tetris.mims.app.media.audio.MediaAudioService;
import com.sumavision.tetris.mims.app.media.audio.MediaAudioVO;
import com.sumavision.tetris.mims.app.media.encode.exception.FileNotExitException;
import com.sumavision.tetris.mims.app.media.video.MediaVideoPO;
import com.sumavision.tetris.mims.app.media.video.MediaVideoService;
import com.sumavision.tetris.mims.app.media.video.MediaVideoVO;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.orm.exception.ErrorTypeException;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;

@Controller
@RequestMapping(value = "/api/process/media")
public class ApiProcessMediaController {

	@Autowired
	private UserQuery userQuery;
	
	@Autowired
	private FolderDAO folderDao;
	
	@Autowired
	private MediaVideoService mediaVideoService;
	
	@Autowired
	private MediaAudioService mediaAudioService;
	
	/**
	 * 根据视音频媒资列表批量加载的视频媒资（给转码添加媒资提供）<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月27日 下午4:03:27
	 * @param String urlList 视音频媒资http地址由","连接
	 * @return null
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add/by/transcoding")
	public Object addByTranscoding(String urlList, Long parentFolderId,String mediaTags, HttpServletRequest request) throws Exception {
		UserVO user = userQuery.current();
		
		List<String> urls = Arrays.asList(urlList.split(","));
		
		FolderPO folder = folderDao.findOne(parentFolderId);
		
		if (folder == null) return null;
		
		HashMapWrapper<String, String> map = new HashMapWrapper<String, String>();
		if(folder.getType() == FolderType.COMPANY_VIDEO){
			List<MediaVideoPO> videos = mediaVideoService.addList(user, urls, parentFolderId, mediaTags);
			map.put("type", "video")
			.put("medias", videos == null || videos.isEmpty() ? null : JSONArray.toJSONString(MediaVideoVO.getConverter(MediaVideoVO.class).convert(videos, MediaVideoVO.class)));
		}else if (folder.getType() == FolderType.COMPANY_AUDIO) {
			List<MediaAudioPO> audios = mediaAudioService.addList(user, urls, parentFolderId, mediaTags);
			map.put("type", "audio")
			.put("medias", audios == null || audios.isEmpty() ? null : JSONArray.toJSONString(MediaAudioVO.getConverter(MediaAudioVO.class).convert(audios, MediaAudioVO.class)));
		}
		
		return map.getMap();
	}
	
	/**
	 * 向西研所旧媒资注入文件媒资<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月28日 下午4:45:33
	 * @param type 媒资类型(目前只有audio和video)
	 * @param medias 媒资列表
	 * @return
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/old/media/upload")
	public Object updoadToOldMims(String type, String medias, HttpServletRequest request) throws Exception {
		if (medias == null) {
			System.out.println("预上传媒资信息为空");
			return null;
		}
		File jsonFile = ResourceUtils.getFile("classpath:profile.json");
		String json = FileUtils.readFileToString(jsonFile);
		JSONObject jsonObject = JSONObject.parseObject(json);
		Boolean ifUpload = jsonObject.getBoolean("upload");
		if (ifUpload == null || !ifUpload) return null;
		
		String filePath;
		switch (type.toLowerCase()) {
		case "video":
			MediaVideoVO video = JSONArray.parseArray(medias, MediaVideoVO.class).get(0);
			filePath = video.getUploadTmpPath();
			break;
		case "audio":
			MediaAudioVO audio = JSONArray.parseArray(medias, MediaAudioVO.class).get(0);
			filePath = audio.getUploadTmpPath();
		default:
			throw new ErrorTypeException("type", type);
		}

		File file = new File(filePath);
		if (!file.exists()) throw new FileNotExitException("");
		String fileName = file.getName();
		if (file.length() > 4l*1024*1024*1024) throw new ProcessFileSizeOverCommitException(fileName, file.length());
		
		String ip = jsonObject.getString("oldMimsIp");
		String port = jsonObject.getString("oldMimsPort");
		String url = new StringBuilderWrapper()
				.append("http://")
				.append(ip)
				.append(":")
				.append(port)
				.append("/smartexpress-issue-ui/sarftPlat/mediaProcessUpload")
				.toString();
		
		Map<String, String> requestMap = new HashMapWrapper<String, String>()
				.put("fileName", fileName)
				.put("capType", type.toLowerCase() == "audio" ? "1" : "2")
				.put("fileCRC", CRC32.calculate(file) + "")
				.getMap();
		
		Thread thread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				Map<String,Object> map = HttpRequestUtil.uploadFileByHTTP(file, url, requestMap);
				
				JSONObject responseJSON = JSON.parseObject(map.get("data").toString());
				
				if (responseJSON != null && responseJSON.getString("errMsg").equals("success")) {
					System.out.println("文件注入成功:" + fileName);
				} else {
					try {
						throw new ProcessQuestToOldMimsFailException("注入媒资");
					} catch (ProcessQuestToOldMimsFailException e) {
						e.printStackTrace();
					}
				}
			}
		});
		thread.start();
		
		return null;
	}
}
