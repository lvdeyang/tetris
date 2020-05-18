package com.sumavision.tetris.mims.app.media.video;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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
	
	/**
	 * 添加上传视频媒资任务<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年11月29日 下午1:44:06
	 * @param MediaVideoTaskVO task{name:文件名称, size:文件大小, mimetype:文件mime类型, lastModified:最后更新时间}
	 * @param String name 媒资名称
	 * @param List<String> tags 标签数组
	 * @param String keyWords 关键字数组
	 * @param String remark 备注
	 * @param Long folerId 文件夹id		
	 * @return MediaVideoVO 视频媒资 
	 */
	public MediaVideoVO addTask(
			MediaVideoTaskVO task, 
			String name,
            List<String> tags,
            String keyWords,
            String remark,
			Long folderId, 
            String thumbnail,
            String addition) throws Exception {
		if(tags == null) tags = new ArrayList<String>();
		return JsonBodyResponseParser.parseObject(mediaVideoFeign.addTask(JSONObject.toJSONString(task), name, String.join(",", tags), keyWords, remark, folderId, thumbnail, addition), MediaVideoVO.class);
	}
	
	/**
	 * 视频媒资上传<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月2日 下午3:32:40
	 * @param MultipartFile file 文件描述
	 * @param String uuid 任务uuid
	 * @param String name 文件名称
	 * @param long lastModified 最后修改日期
	 * @param long beginOffset 文件分片的起始位置
	 * @param long  endOffset 文件分片的结束位置
	 * @param long blockSize 文件分片大小
	 * @param long size 文件大小
	 * @param String type 文件的mimetype
	 * @param blob block 文件分片数据
	 * @return MediaVideoVO 视频媒资
	 */
	public MediaVideoVO upload(
			MultipartFile file,
			String uuid,
			String name,
			Long blockSize,
			Long lastModified,
			Long size,
			String type,
			Long beginOffset,
			Long endOffset) throws Exception {
		return JsonBodyResponseParser.parseObject(mediaVideoFeign.upload(file, uuid, name, blockSize, lastModified, size, type, beginOffset, endOffset), MediaVideoVO.class);
	}
}
