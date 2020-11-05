package com.sumavision.tetris.mims.app.media.stream.audio;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 音频流媒资url查询操作<br/>
 * <b>作者:</b>lzp<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年6月14日 上午10:38:08
 */
@Component
public class MediaAudioStreamUrlRelationQuery {
	@Autowired
	MediaAudioStreamUrlRelationDAO mediaAudioStreamUrlRelationDAO;

	/**
	 * 获取音频流媒资被访问最少的url<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月17日 上午10:38:08
	 * @param streamId 音频流媒资id
	 * @return List<String> 该音频流媒资所有url
	 */
	public String getUrlFromStreamId(Long streamId) {
		List<MediaAudioStreamUrlRelationPO> mediaAudioStreamUrlRelationPOs = mediaAudioStreamUrlRelationDAO
				.findByVideoStreamIdOrderByVisitCountAsc(streamId);
		if (mediaAudioStreamUrlRelationPOs != null && mediaAudioStreamUrlRelationPOs.size() > 0) {
			MediaAudioStreamUrlRelationPO selectPo = mediaAudioStreamUrlRelationPOs.get(0);
			selectPo.setVisitCount(selectPo.getVisitCount()+1);
			mediaAudioStreamUrlRelationDAO.save(selectPo);
			return selectPo.getUrl();
		}else {
			return "";
		}
	}
	
	/**
	 * 获取音频流媒资所有url<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月17日 上午10:38:08
	 * @param streamId 音频流媒资id
	 * @return List<String> 该音频流媒资所有url
	 */
	public List<String> getAllUrlFromStreamId(Long streamId){
		return mediaAudioStreamUrlRelationDAO.findUrlsByVideoStreamId(streamId);
	}
}
