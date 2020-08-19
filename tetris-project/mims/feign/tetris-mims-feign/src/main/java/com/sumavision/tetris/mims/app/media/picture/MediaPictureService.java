package com.sumavision.tetris.mims.app.media.picture;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.mims.app.media.video.MediaVideoTaskVO;
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
	
	/**
	 * 添加上传图片媒资任务<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年11月29日 下午1:44:06
	 * @param MediaPictureTaskVO task{name:文件名称, size:文件大小, mimetype:文件mime类型, lastModified:最后更新时间}
	 * @param String name 媒资名称
	 * @param JSONString tags 标签数组
	 * @param JSONString keyWords 关键字数组
	 * @param String remark 备注
	 * @param Long folerId 文件夹id		
	 * @return MediaPictureVO 图片媒资
	 */
	public MediaPictureVO addTask(
			MediaPictureTaskVO task, 
			String name,
            List<String> tags,
            String keyWords,
            String remark,
			Long folderId, 
            String addition) throws Exception {
		if(tags == null) tags = new ArrayList<String>();
		return JsonBodyResponseParser.parseObject(mediaPictureFeign.addTask(JSONObject.toJSONString(task), name, String.join(",", tags), keyWords, remark, folderId, addition), MediaPictureVO.class);
	}
	
	/**
	 * 素材分片上传<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月2日 下午3:32:40
	 * @param String uuid 任务uuid
	 * @param String name 文件名称
	 * @param long lastModified 最后修改日期
	 * @param long beginOffset 文件分片的起始位置
	 * @param long  endOffset 文件分片的结束位置
	 * @param long blockSize 文件分片大小
	 * @param long size 文件大小
	 * @param String type 文件的mimetype
	 * @return MediaPictureVO 图片媒资
	 */
	public MediaPictureVO upload(
			MultipartFile file,
			String uuid,
			String name,
			Long blockSize,
			Long lastModified,
			Long size,
			String type,
			Long beginOffset,
			Long endOffset) throws Exception {
		return JsonBodyResponseParser.parseObject(mediaPictureFeign.upload(file, uuid, name, blockSize, lastModified, size, type, beginOffset, endOffset), MediaPictureVO.class);
	}
}
