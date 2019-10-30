package com.sumavision.tetris.mims.app.media.stream.video;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.mvc.ext.response.parser.JsonBodyResponseParser;

@Service
@Transactional(rollbackFor = Exception.class)
public class MediaVideoStreamService {
	@Autowired
	private MediaVideoStreamFeign mediaVideoStreamFeign;

	/**
	 * 添加视频流媒资上传任务<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年11月29日 下午3:21:49
	 * 
	 * @param String
	 *            name 媒资名称
	 * @param String
	 *            previewUrl 视频流地址
	 * @return MediaVideoStreamPO 视频流媒资
	 */
	public MediaVideoStreamVO addVideoStreamTask(String previewUrl, String name) throws Exception {
		return JsonBodyResponseParser.parseObject(mediaVideoStreamFeign.addVideoStreamTask(previewUrl, name),
				MediaVideoStreamVO.class);
	}

	/**
	 * 删除媒资 <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年11月29日 下午3:21:49
	 * 
	 * @param Long
	 *            mediaId 媒资名称
	 */
	public Object remove(List<Long> mediaIdList) throws Exception {
		String mediaIds = JSONArray.toJSONString(mediaIdList);
		return JsonBodyResponseParser.parseObject(mediaVideoStreamFeign.remove(mediaIds), JSONObject.class);
	}

	/**
	 * 修改媒资 <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年11月29日 下午3:21:49
	 * 
	 * @param Long
	 *            mediaId 媒资名称
	 * @param String
	 *            previewUrl 媒资地址
	 * @param String
	 *            name 媒资名称
	 * @return MediaVideoStreamVO
	 */
	public MediaVideoStreamVO edit(Long mediaId, String previewUrl, String name) throws Exception {
		return JsonBodyResponseParser.parseObject(mediaVideoStreamFeign.edit(mediaId, previewUrl, name),
				MediaVideoStreamVO.class);
	}
}
