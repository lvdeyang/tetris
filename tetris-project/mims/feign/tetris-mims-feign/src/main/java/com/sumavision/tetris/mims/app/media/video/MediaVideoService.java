package com.sumavision.tetris.mims.app.media.video;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.mvc.ext.response.parser.JsonBodyResponseParser;

@Service
@Transactional(rollbackFor = Exception.class)
public class MediaVideoService {

	@Autowired
	private MediaVideoFeign mediaVideoFeign;
	/**
	 * 添加远程媒资(供收录系统使用)<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月2日 下午4:23:53
	 * @param String name 媒资名称
	 * @param Long tagId 标签id(可不传)
	 * @param String httpUrl 预览地址
	 * @param String ftpUrl ftp下载地址(可不传)
	 */
	public MediaVideoVO addRemote(String name, Long tagId, String httpUrl, String ftpUrl) throws Exception {
		return JsonBodyResponseParser.parseObject(mediaVideoFeign.addRemote(name, tagId, httpUrl, ftpUrl), MediaVideoVO.class);
	}
	
	/**
	 * 根据id数组删除媒资<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月4日 上午9:12:35
	 * @param String ids 预删除媒资id数组的JSON字符串
	 */
	public JSONObject remove(String ids) throws Exception {
		return JsonBodyResponseParser.parseObject(mediaVideoFeign.remove(ids), JSONObject.class);
	}
	
	/**
	 * 根据id数组删除媒资<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年5月8日 下午3:39:22
	 * @param List<Long> ids 预删除媒资id数组
	 */
	public List<MediaVideoVO> remove(List<Long> ids) throws Exception {
		return JsonBodyResponseParser.parseArray(mediaVideoFeign.remove(JSONArray.toJSONString(ids)), MediaVideoVO.class);
	}
}
