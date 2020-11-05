package com.sumavision.tetris.bvc.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;

@Component
public class AudioVideoTemplateQuery {

	@Autowired
	private AudioVideoTemplateDAO audioVideoTemplateDao;
	
	/**
	 * 查询枚举类型<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月30日 下午1:23:08
	 * @return audioFormats Map<String, String> 音频编码格式
	 * @return resolutions Map<String, String> 分辨率
	 * @return usageTypes Map<String, String> 模板用途
	 * @return videoFormats Map<String, String> 视频编码格式
	 */
	public Map<String, Object> queryTypes() throws Exception{
		Map<String, Object> types = new HashMap<String, Object>();
		Map<String, String> audioFormats = new HashMap<String, String>();
		AudioFormat[] audioFormatValues = AudioFormat.values();
		for(AudioFormat value:audioFormatValues){
			audioFormats.put(value.toString(), value.getName());
		}
		Map<String, String> resolutions = new HashMap<String, String>();
		Resolution[] resolutionValues = Resolution.values();
		for(Resolution value:resolutionValues){
			resolutions.put(value.toString(), value.getName());
		}
		Map<String, String> usageTypes = new HashMap<String, String>();
		UsageType[] usageTypeValues = UsageType.values();
		for(UsageType value:usageTypeValues){
			usageTypes.put(value.toString(), value.getName());
		}
		Map<String, String> videoFormats = new HashMap<String, String>();
		VideoFormat[] videoFormatValues = VideoFormat.values();
		for(VideoFormat value:videoFormatValues){
			videoFormats.put(value.toString(), value.getName());
		}
		types.put("audioFormats", audioFormats);
		types.put("resolutions", resolutions);
		types.put("usageTypes", usageTypes);
		types.put("videoFormats", videoFormats);
		return types;	 
	}
	
	/**
	 * 分页查询参数模板<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月30日 下午1:44:01
	 * @param int currentPage 当前页
	 * @param int pageSize 每页数据量
	 * @return total long 总数据量
	 * @return rows List<AudioVideoTemplateVO> 模板列表
	 */
	public Map<String, Object> load(
			int currentPage,
			int pageSize) throws Exception{
		long total = audioVideoTemplateDao.count();
		Pageable page = new PageRequest(currentPage-1, pageSize);
		Page<AudioVideoTemplatePO> pagedEntities = audioVideoTemplateDao.findAll(page);
		List<AudioVideoTemplateVO> templates = AudioVideoTemplateVO.getConverter(AudioVideoTemplateVO.class).convert(pagedEntities.getContent(), AudioVideoTemplateVO.class);
		return new HashMapWrapper<String, Object>().put("total", total)
												   .put("rows", templates)
												   .getMap();
	}
	
}
