package com.sumavision.tetris.mims.app.media.txt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sumavision.tetris.mvc.ext.response.parser.JsonBodyResponseParser;

@Service
@Transactional(rollbackFor = Exception.class)
public class MediaTxtService {
	@Autowired
	private MediaTxtFeign mediaTxtFeign;
	
	/**
	 * 数据库添加json文件<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月26日 上午10:26:53
	 * @param String jsonContent json内容
	 * @param Long folderId 目录id
	 * @param String name 文件名
	 * @return MediaTxtVO
	 */
	public MediaTxtVO addJson(String json, Long folderId, String name) throws Exception{
		return JsonBodyResponseParser.parseObject(mediaTxtFeign.addJson(json, folderId, name), MediaTxtVO.class);
	}
}
