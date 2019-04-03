package com.sumavision.tetris.mims.app.media.compress;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sumavision.tetris.mvc.ext.response.parser.JsonBodyResponseParser;

@Component
public class MediaCompressQuery {

	@Autowired
	private MediaCompressFeign mediaCompressFeign;
	
	/**
	 * 根据id查询播发媒资<br/>
	 * <b>作者:</b>ldy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年4月1日 下午5:46:46
	 * @param Long id 播发媒资id
	 * @return MediaCompressVO 播发媒资
	 */
	public MediaCompressVO query(Long id) throws Exception{
		return JsonBodyResponseParser.parseObject(mediaCompressFeign.query(id), MediaCompressVO.class);
	}
}
