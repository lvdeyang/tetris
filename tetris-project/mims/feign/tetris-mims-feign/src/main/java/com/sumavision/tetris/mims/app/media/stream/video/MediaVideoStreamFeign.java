package com.sumavision.tetris.mims.app.media.stream.video;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.config.feign.FeignConfiguration;

@FeignClient(name = "tetris-mims", configuration = FeignConfiguration.class)
public interface MediaVideoStreamFeign {

	/**
	 * 加载文件夹下的视频流媒资<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月6日 下午4:03:27
	 * @param folderId 文件夹id
	 * @return rows List<MediaVideoStreamVO> 视频流媒资列表
	 * @return breadCrumb FolderBreadCrumbVO 面包屑数据
	 */
	@RequestMapping(value = "/media/video/stream/feign/load")
	public JSONObject load(@RequestParam("folderId") Long folderId) throws Exception;
	
	/**
	 * 添加视频流媒资上传任务<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年11月29日 下午3:21:49
	 * @param UserVO user 用户
	 * @param String name 媒资名称
	 * @param List<String> tags 标签列表
	 * @param List<String> keyWords 关键字列表
	 * @param String remark 备注
	 * @param String previewUrl 视频流地址
	 * @param FolderPO folder 文件夹
	 * @return MediaVideoStreamPO 视频流媒资
	 */
	@RequestMapping(value = "/media/video/stream/feign/task/add")
	public JSONObject addVideoStreamTask(@RequestParam("previewUrl") String previewUrl,@RequestParam("name") String name) throws Exception;
	
	/**
	 * 视频流媒资删除<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年7月17日 下午3:43:03
	 * @param MediaVideoStreamPO videos 视频流媒资列表
	 */
	@RequestMapping(value = "/media/video/stream/feign/remove")
	public JSONObject remove(@RequestParam("mediaIds") String mediaIds) throws Exception;
	
	/** 
	 * 修改媒资
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年11月29日 下午3:21:49
	 * @param Long mediaId 媒资名称
	 * @param String previewUrl 媒资地址
	 * @param String name 媒资名称
	 * @return MediaVideoStreamVO
	 */
	@RequestMapping(value = "/media/video/stream/feign/edit")
	public JSONObject edit(@RequestParam("mediaId") Long mediaId, @RequestParam("previewUrl") String previewUrl,@RequestParam("name") String name) throws Exception;
}
