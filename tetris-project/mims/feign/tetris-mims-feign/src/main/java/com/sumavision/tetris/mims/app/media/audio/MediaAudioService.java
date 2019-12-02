package com.sumavision.tetris.mims.app.media.audio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.sumavision.tetris.mvc.ext.response.parser.JsonBodyResponseParser;
import com.sumavision.tetris.user.UserVO;

@Service
@Transactional(rollbackFor = Exception.class)
public class MediaAudioService {
	
	@Autowired
	private MediaAudioFeign mediaAudioFeign;

	public MediaAudioVO add(
			UserVO user,
			String name,
			String fileName,
			Long size,
			String folderType,
			String mimeType,
			String uploadTempPath) throws Exception{
		
		return JsonBodyResponseParser.parseObject(mediaAudioFeign.add(user.getUuid(), user.getNickname(), user.getGroupId(), name, fileName, size, folderType, mimeType, uploadTempPath), MediaAudioVO.class);
	}
	
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
	public MediaAudioVO addRemote(String name, Long tagId, String httpUrl, String ftpUrl) throws Exception {
		return JsonBodyResponseParser.parseObject(mediaAudioFeign.addRemote(name, tagId, httpUrl, ftpUrl), MediaAudioVO.class);
	}
}
