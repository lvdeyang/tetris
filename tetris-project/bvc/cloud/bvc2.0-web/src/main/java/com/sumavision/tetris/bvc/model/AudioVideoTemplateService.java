package com.sumavision.tetris.bvc.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.sumavision.tetris.bvc.model.Resolution;
import com.sumavision.tetris.bvc.model.exception.AudioVideoTemplateNotFoundException;

@Service
public class AudioVideoTemplateService {

	@Autowired
	private AudioVideoTemplateDAO audioVideoTemplateDao;
	
	/**
	 * 添加参数模板<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月30日 下午1:59:21
	 * @param String name 名称
	 * @param String videoFormat 视频格式
	 * @param String videoFormatSpare 备用视频格式
	 * @param String audioFormat 音频格式
	 * @param Boolean mux 是否端口复用
	 * @param String videoBitRate 视频码率
	 * @param String videoBitRateSpare 备用视频码率
	 * @param String videoResolution 视频分辨率
	 * @param String videoResolutionSpare 备用视频分辨率
	 * @param String fps 帧率
	 * @param String audioBitRate 音频码率
	 * @param String usageType 模板用途
	 * @param Boolean isTemplate 是否是模板
	 * @return AudioVideoTemplateVO 参数模板
	 */
	public AudioVideoTemplateVO add(
			String name,
			String videoFormat,
			String videoFormatSpare,
			String audioFormat,
			Boolean mux,
			String videoBitRate,
			String videoBitRateSpare,
			String videoResolution,
			String videoResolutionSpare,
			String fps,
			String audioBitRate,
			String usageType,
			Boolean isTemplate) throws Exception{
		AudioVideoTemplatePO template = new AudioVideoTemplatePO();
		template.setName(name);
		template.setVideoFormat(VideoFormat.valueOf(videoFormat));
		template.setVideoFormatSpare(VideoFormat.valueOf(videoFormatSpare));
		template.setAudioFormat(AudioFormat.valueOf(audioFormat));
		template.setMux(mux);
		template.setVideoBitRate(videoBitRate);
		template.setVideoBitRateSpare(videoBitRateSpare);
		template.setVideoResolution(Resolution.valueOf(videoResolution));
		template.setVideoResolutionSpare(Resolution.valueOf(videoResolutionSpare));
		template.setFps(fps);
		template.setAudioBitRate(audioBitRate);
		template.setUsageType(UsageType.valueOf(usageType));
		template.setIsTemplate(isTemplate);
		audioVideoTemplateDao.save(template);
		return new AudioVideoTemplateVO().set(template);
	}
	
	/**
	 * 修改参数模板<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月30日 下午1:59:21
	 * @param Long id 模板id
	 * @param String name 名称
	 * @param String videoFormat 视频格式
	 * @param String videoFormatSpare 备用视频格式
	 * @param String audioFormat 音频格式
	 * @param Boolean mux 是否端口复用
	 * @param String videoBitRate 视频码率
	 * @param String videoBitRateSpare 备用视频码率
	 * @param String videoResolution 视频分辨率
	 * @param String videoResolutionSpare 备用视频分辨率
	 * @param String fps 帧率
	 * @param String audioBitRate 音频码率
	 * @param String usageType 模板用途
	 * @param Boolean isTemplate 是否是模板
	 * @return AudioVideoTemplateVO 参数模板
	 */
	public AudioVideoTemplateVO edit(
			Long id,
			String name,
			String videoFormat,
			String videoFormatSpare,
			String audioFormat,
			Boolean mux,
			String videoBitRate,
			String videoBitRateSpare,
			String videoResolution,
			String videoResolutionSpare,
			String fps,
			String audioBitRate,
			String usageType,
			Boolean isTemplate) throws Exception{
		AudioVideoTemplatePO template = audioVideoTemplateDao.findOne(id);
		if(template == null){
			throw new AudioVideoTemplateNotFoundException(id);
		}
		template.setName(name);
		template.setVideoFormat(VideoFormat.valueOf(videoFormat));
		template.setVideoFormatSpare(VideoFormat.valueOf(videoFormatSpare));
		template.setAudioFormat(AudioFormat.valueOf(audioFormat));
		template.setMux(mux);
		template.setVideoBitRate(videoBitRate);
		template.setVideoBitRateSpare(videoBitRateSpare);
		template.setVideoResolution(Resolution.valueOf(videoResolution));
		template.setVideoResolutionSpare(Resolution.valueOf(videoResolutionSpare));
		template.setFps(fps);
		template.setAudioBitRate(audioBitRate);
		template.setUsageType(UsageType.valueOf(usageType));
		template.setIsTemplate(isTemplate);
		audioVideoTemplateDao.save(template);
		return new AudioVideoTemplateVO().set(template);
	}
	
	/**
	 * 删除音视频参数模板<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月30日 下午2:08:57
	 * @param Long id 模板id
	 */
	public void delete(Long id) throws Exception{
		AudioVideoTemplatePO template = audioVideoTemplateDao.findOne(id);
		if(template != null){
			audioVideoTemplateDao.delete(template);
		}
	}
	
}
