package com.sumavision.tetris.mims.app.media.picture;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.sumavision.tetris.mvc.ext.response.parser.JsonBodyResponseParser;

@Service
@Transactional(rollbackFor = Exception.class)
public class MediaPictureService {

	@Autowired
	private MediaPictureFeign mediaPictureFeign;
	
	/**
	 * 根据id数组删除媒资<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年5月8日 下午3:39:22
	 * @param List<Long> ids 预删除媒资id数组
	 */
	public List<MediaPictureVO> remove(List<Long> ids) throws Exception {
		return JsonBodyResponseParser.parseArray(mediaPictureFeign.remove(JSONArray.toJSONString(ids)), MediaPictureVO.class);
	}
}
