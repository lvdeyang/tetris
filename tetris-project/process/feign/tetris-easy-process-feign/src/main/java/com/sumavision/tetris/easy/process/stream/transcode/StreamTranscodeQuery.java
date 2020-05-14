package com.sumavision.tetris.easy.process.stream.transcode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sumavision.tetris.mvc.ext.response.parser.JsonBodyResponseParser;

@Component
public class StreamTranscodeQuery {
	@Autowired
	private StreamTranscodeFeign streamTranscodeFeign;
	
	/**
	 * 获取配置文件信息<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月27日 下午3:12:07
	 * @return StreamTranscodeProfileVO 配置信息
	 */
	public StreamTranscodeProfileVO getProfile() throws Exception {
		return JsonBodyResponseParser.parseObject(streamTranscodeFeign.getProfile(), StreamTranscodeProfileVO.class);
	}
	
	/**
	 * 流转码<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月3日 上午10:01:59
	 * @param Long assetId 资源仓库中流id
	 * @param Long assetPath 直接使用流url
	 * @param Boolean record 是否录制
	 * @param Integer bePCM 音频源pcm
	 * @param String mediaType 资源仓库中流类型
	 * @param String recordCallback 录制回调
	 * @param Integer progNum 节目号
	 * @param String task 输出信息
	 * @param String inputParam 输入信息
	 */
	public String addTask(Long assetId,
			String assetPath,
			boolean record,
			Integer bePCM,
			String mediaType,
			String recordCallback,
			Integer progNum,
			String deviceIp,
			String task,
			String inputParam) throws Exception{
		return JsonBodyResponseParser.parseObject(streamTranscodeFeign.addTask(assetId, assetPath, record, bePCM, mediaType, recordCallback, progNum, deviceIp, task, inputParam), String.class);
	}
	
	/**
	 * 添加使用流转码能力推流转码任务<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月30日 下午2:23:20
	 * @param String fileUrl 文件地址(m3u8为http，其他文件为ftp地址)
	 * @param Integer playTime 循环次数
	 * @param Long seek 首个文件seek时间
	 * @param Boolean record 是否录制
	 * @param Integer bePCM 音频源pcm
	 * @param String mediaType 资源仓库中流类型
	 * @param String recordCallback 录制回调
	 * @param Integer progNum 节目号
	 * @param String task 输出信息
	 * @param String inputParam 输入信息
	 * @return String 流程号
	 */
	public String addFileTask(
			String fileUrl,
			Integer playTime,
			Long seek,
			boolean record,
			Integer bePCM,
			String mediaType,
			String recordCallback,
			Integer progNum,
			String deviceIp,
			String task,
			String inputParam) throws Exception {
		return JsonBodyResponseParser.parseObject(streamTranscodeFeign.addFileTask(fileUrl, playTime, seek, record, bePCM, mediaType, recordCallback, progNum, deviceIp, task, inputParam), String.class);
	}
	
	/**
	 * 删除转码任务<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月3日 上午10:08:19
	 * @param Long id 任务id
	 */
	public String deleteTask(String id) throws Exception {
		return JsonBodyResponseParser.parseObject(streamTranscodeFeign.deleteTask(id), String.class);
	};
	
	/**
	 * 根据任务id添加输出<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月3日 下午2:23:41
	 * @param Long id 任务id
	 * @param String outputParam 输出信息
	 */
	public void addOutput(String id, String outputParam) throws Exception {
		streamTranscodeFeign.addOutput(id, outputParam);
	};
	
	/**
	 * 根据任务id删除输出<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月3日 下午2:23:41
	 * @param Long id 任务id
	 * @param String outputParam 输出信息
	 */
	public void deleteOutput(String id, String outputParam) throws Exception {
		streamTranscodeFeign.deleteOutput(id, outputParam);
	};
	
	/**
	 * 根据任务id删除全部输出<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月3日 下午2:23:41
	 * @param Long id 任务id
	 */
	public void deleteAllOutput(String id) throws Exception {
		streamTranscodeFeign.deleteAllOutput(id);
	};
	
	/**
	 * 校验文件是否需要云转码<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月11日 下午5:55:26
	 * @param Long mediaId 资源id
	 * @param String mediaType 资源类型(audio/video)
	 * @return FileDealVO 校验返回
	 */
	public FileDealVO checkEdit(Long mediaId, String mediaType) throws Exception {
		return JsonBodyResponseParser.parseObject(streamTranscodeFeign.checkEdit(mediaId, mediaType), FileDealVO.class);
	}
	
	/**
	 * 校验文件是否需要云转码<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月11日 下午5:55:26
	 * @param Long mediaId 资源id
	 * @param String mediaType 资源类型(audio/video)
	 * @return FileDealVO 校验返回
	 */
	public FileDealVO checkEdit(String uuid) throws Exception {
		return JsonBodyResponseParser.parseObject(streamTranscodeFeign.checkEditByUuid(uuid), FileDealVO.class);
	}
}
