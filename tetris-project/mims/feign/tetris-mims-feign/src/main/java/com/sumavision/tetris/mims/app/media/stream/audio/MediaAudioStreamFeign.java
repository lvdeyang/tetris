package com.sumavision.tetris.mims.app.media.stream.audio;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.config.feign.FeignConfiguration;

@FeignClient(name = "tetris-mims", configuration = FeignConfiguration.class)
public interface MediaAudioStreamFeign {

	/**
	 * 加载文件夹下的音频流媒资<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月6日 下午4:03:27
	 * @param folderId 文件夹id
	 * @return rows List<MediaAudioStreamVO> 音频流媒资列表
	 * @return breadCrumb FolderBreadCrumbVO 面包屑数据
	 */
	@RequestMapping(value = "/media/audio/stream/feign/load")
	public JSONObject load(@RequestParam("folderId") Long folderId) throws Exception;
	
	/**
	 * 根据预览地址查询音频流<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月5日 上午11:15:05
	 * @param String previewUrls 预览地址列表
	 * @return List<MediaAudioStreamVO> 音频流列表
	 */
	@RequestMapping(value = "/media/audio/stream/feign/find/by/preview/url/in")
	public JSONObject findByPreviewUrlIn(@RequestParam("previewUrls") String previewUrls) throws Exception;
	
	/**
	 * 根据id查询音频流<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月5日 上午11:15:05
	 * @param JSONString previewUrls 预览地址列表
	 * @return MediaAudioStreamVO 音频流列表
	 */
	@RequestMapping(value = "/media/audio/stream/feign/find/by/id")
	public JSONObject findById(@RequestParam("id") Long id) throws Exception;
}
