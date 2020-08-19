package com.sumavision.tetris.mims.app.media.stream.video;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 视频流媒资url查询操作<br/>
 * <b>作者:</b>lzp<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年6月14日 上午10:38:08
 */
@Component
public class MediaVideoStreamUrlRelationQuery {
	@Autowired
	MediaVideoStreamUrlRelationDAO mediaVideoStreamUrlRelationDAO;

	/**
	 * 获取视频流媒资被访问最少的url<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月17日 上午10:38:08
	 * @param streamId 视频流媒资id
	 * @return List<String> 该视频流媒资所有url
	 */
	public String getUrlFromStreamId(Long streamId) {
		List<MediaVideoStreamUrlRelationPO> mediaVideoStreamUrlRelationPOs = mediaVideoStreamUrlRelationDAO
				.findByVideoStreamIdOrderByVisitCountAsc(streamId);
		if (mediaVideoStreamUrlRelationPOs != null && mediaVideoStreamUrlRelationPOs.size() > 0) {
			MediaVideoStreamUrlRelationPO selectPo = mediaVideoStreamUrlRelationPOs.get(0);
			selectPo.setVisitCount(selectPo.getVisitCount()+1);
			mediaVideoStreamUrlRelationDAO.save(selectPo);
			return selectPo.getUrl();
		}else {
			return "";
		}
	}
	
	/**
	 * 获取视频流媒资所有url<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月17日 上午10:38:08
	 * @param streamId 视频流媒资id
	 * @return List<String> 该视频流媒资所有url
	 */
	public List<String> getAllUrlFromStreamId(Long streamId){
		return mediaVideoStreamUrlRelationDAO.findUrlsByVideoStreamId(streamId);
	}
}
