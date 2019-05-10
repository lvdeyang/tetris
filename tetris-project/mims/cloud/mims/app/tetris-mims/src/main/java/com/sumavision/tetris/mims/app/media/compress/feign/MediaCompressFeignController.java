package com.sumavision.tetris.mims.app.media.compress.feign;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.sumavision.tetris.mims.app.media.compress.FileCompressVO;
import com.sumavision.tetris.mims.app.media.compress.MediaCompressDAO;
import com.sumavision.tetris.mims.app.media.compress.MediaCompressService;
import com.sumavision.tetris.mims.app.media.compress.MediaCompressVO;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

@Controller
@RequestMapping(value = "/media/compress/feign")
public class MediaCompressFeignController {
	
	@Autowired
	private MediaCompressService mediaCompressService;
	
	@Autowired
	private MediaCompressDAO mediaCompressDao;
	
	/**
	 * 根据播发媒资id查询<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年4月15日 上午9:49:53
	 * @param Long id 播发媒资id
	 * @return MediaCompressVO 播发媒资
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/query")
	public Object query(Long id) throws Exception{
		
		return new MediaCompressVO().set(mediaCompressDao.findOne(id));
	}
	
	/**
	 * 压缩播发媒资<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年4月3日 上午9:49:53
	 * @param String jsonString 播发媒资json描述
	 * @param JSONArray mimsUuidList 打包媒资列表
	 * @return MediaCompressVO 生成的播发媒资
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/package/tar")
	public Object packageTar(String jsonString, String mimsUuidList, HttpServletRequest httpServletRequest) throws Exception{
		
		return mediaCompressService.packageTar(jsonString, JSON.parseArray(mimsUuidList, FileCompressVO.class));
	}
	
}
