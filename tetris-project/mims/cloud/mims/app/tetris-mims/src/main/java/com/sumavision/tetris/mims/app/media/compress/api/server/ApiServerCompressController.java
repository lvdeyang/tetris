package com.sumavision.tetris.mims.app.media.compress.api.server;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sumavision.tetris.mims.app.media.compress.MediaCompressDAO;
import com.sumavision.tetris.mims.app.media.compress.MediaCompressPO;
import com.sumavision.tetris.mims.app.media.compress.MediaCompressService;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

@Controller
@RequestMapping(value = "/api/server/compress")
public class ApiServerCompressController {
	
	@Autowired
	private MediaCompressDAO mediaCompressDao;
	
	@Autowired
	private MediaCompressService mediaCompressService;

	/**
	 * 压缩包处理<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>sm<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年4月3日 下午2:11:43
	 * @param Long id 播发媒资id
	 * @return String type 文章类型
	 * @return String name 文章名称
	 * @return String publishTime 文章发布时间
	 * @return String remark 备注
	 * @return String author 作者
	 * @return String keywords 关键字
	 * @return String column 栏目
	 * @return String region 地区
	 * @return String content 媒体内容
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/parse")
	public Object parse(
			Long id,
			String param,
			HttpServletRequest request) throws Exception{
		
		MediaCompressPO mediaCompress = mediaCompressDao.findById(id);
		
		return mediaCompressService.parse(mediaCompress.getUploadTmpPath(), param);
	}
}
