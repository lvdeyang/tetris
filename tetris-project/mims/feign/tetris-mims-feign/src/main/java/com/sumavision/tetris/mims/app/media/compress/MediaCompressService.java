package com.sumavision.tetris.mims.app.media.compress;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.alibaba.fastjson.JSON;
import com.sumavision.tetris.mvc.ext.response.parser.JsonBodyResponseParser;

/**
 * 播发媒资操作（主增删改）<br/>
 * <b>作者:</b>ldy<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年3月31日 下午5:38:33
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class MediaCompressService {

	@Autowired
	private MediaCompressFeign mediaCompressFeign;
	
	/**
	 * 压缩播发媒资<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年4月3日 上午9:49:53
	 * @param String jsonString 播发媒资json描述
	 * @param JSONArray mimsUuidList 打包媒资列表
	 * @return MediaCompressVO 生成的播发媒资
	 */
	public MediaCompressVO packageTar(String jsonString, List<FileCompressVO> mimsList) throws Exception {
		return JsonBodyResponseParser.parseObject(mediaCompressFeign.packageTar(jsonString, JSON.toJSONString(mimsList)), MediaCompressVO.class);
	}
}
