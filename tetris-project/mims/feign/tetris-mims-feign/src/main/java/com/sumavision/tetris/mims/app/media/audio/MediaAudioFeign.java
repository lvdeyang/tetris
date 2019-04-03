package com.sumavision.tetris.mims.app.media.audio;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.config.feign.FeignConfiguration;
import com.sumavision.tetris.user.UserVO;

@FeignClient(name = "tetris-mims", configuration = FeignConfiguration.class)
public interface MediaAudioFeign {

	/**
	 * 加载文件夹下的音频媒资<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月6日 下午4:03:27
	 * @param folderId 文件夹id
	 * @return rows List<MediaAudioVO> 音频媒资列表
	 * @return breadCrumb FolderBreadCrumbVO 面包屑数据
	 */
	@RequestMapping(value = "/media/audio/feign/load")
	public JSONObject load(@RequestParam("folderId") Long folderId) throws Exception;
	
	/**
	 * 加载所有的音频媒资<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月6日 下午4:03:27
	 * @return List<MediaAudioVO> 视频媒资列表
	 */
	@RequestMapping(value = "/media/audio/feign/load/all")
	public JSONObject loadAll() throws Exception;
	
	@RequestMapping(value = "/media/audio/feign/add")
	public JSONObject add(
				@RequestParam("userId") String userId,
				@RequestParam("userName") String userName,
				@RequestParam("groupId") String groupId,
				@RequestParam("name") String name,
				@RequestParam("fileName") String fileName,
				@RequestParam("size") Long size,
				@RequestParam("folderType") String folderType,
				@RequestParam("mimeType") String mimeType,
				@RequestParam("uploadTempPath") String uploadTempPath) throws Exception;
}
