package com.sumavision.tetris.mims.app.media.stream.video;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 视频流媒资url操作（主增删改）<br/>
 * <b>作者:</b>lzp<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年6月14日 下午3:38:33
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class MediaVideoStreamUrlRelationService {
	@Autowired
	MediaVideoStreamUrlRelationDAO mediaVideoStreamUrlRelationDAO;

	@Autowired
	MediaVideoStreamUrlRelationQuery mediaVideoStreamUrlRelationQuery;

	/**
	 * 视频流媒资url整体添加操作<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月17日 上午10:38:08
	 * @param urlList 所有最新url
	 * @param streamId 视频流媒资id
	 * @return List<MediaVideoStreamUrlRelationPO> 该视频流媒资所有url
	 */
	public List<MediaVideoStreamUrlRelationPO> add(List<String> urlList, Long streamId) {
		if (urlList == null || urlList.size() <= 0 || streamId == null)
			return null;

		List<MediaVideoStreamUrlRelationPO> mediaVideoStreamUrlRelationPOs = new ArrayList<MediaVideoStreamUrlRelationPO>();
		for (String url : urlList) {
			if (!url.isEmpty()) {
				MediaVideoStreamUrlRelationPO urlRelationPO = new MediaVideoStreamUrlRelationPO();
				urlRelationPO.setUrl(url);
				urlRelationPO.setVideoStreamId(streamId);
				urlRelationPO.setVisitCount(0l);
				mediaVideoStreamUrlRelationPOs.add(urlRelationPO);
			}
		}

		mediaVideoStreamUrlRelationDAO.save(mediaVideoStreamUrlRelationPOs);

		return mediaVideoStreamUrlRelationPOs;
	}

	/**
	 * 视频流媒资url更新操作<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月17日 上午10:38:08
	 * @param urlList 所有最新url
	 * @param streamId 视频流媒资id
	 * @return List<MediaVideoStreamUrlRelationPO> 该视频流媒资所有url并按访问次数排序
	 */
	public List<MediaVideoStreamUrlRelationPO> update(List<String> urlList, Long streamId) {
		mediaVideoStreamUrlRelationDAO.removeByStreamIdWithExceptUrlIn(streamId, urlList);
		
		List<String> aliveUrl = mediaVideoStreamUrlRelationDAO.findUrlsByVideoStreamId(streamId);
		
		List<MediaVideoStreamUrlRelationPO> addUrl = new ArrayList<MediaVideoStreamUrlRelationPO>();
		for(String url:urlList){
			if ((aliveUrl == null || aliveUrl.isEmpty() || !aliveUrl.contains(url)) && !url.isEmpty()) {
				MediaVideoStreamUrlRelationPO media = new MediaVideoStreamUrlRelationPO();
				media.setUrl(url);
				media.setVideoStreamId(streamId);
				media.setVisitCount(0l);
				addUrl.add(media);
			}
		}
		
		mediaVideoStreamUrlRelationDAO.save(addUrl);
		
		return mediaVideoStreamUrlRelationDAO.findByVideoStreamIdOrderByVisitCountAsc(streamId);
	}
	
	/**
	 * 视频流媒资url整体删除操作<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月17日 上午10:38:08
	 * @param streamId 视频流媒资id
	 */
	public void remove(Long streamId){
		mediaVideoStreamUrlRelationDAO.removeByStreamId(streamId);
	}
	
	/**
	 * 视频流媒资url批量整体删除操作<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月17日 上午10:38:08
	 * @param streamId 视频流媒资id
	 */
	public void remove(Collection<Long> videoIds){
		mediaVideoStreamUrlRelationDAO.removeByStreamIdIn(videoIds);
	}
	
	public List<String> copy(Long streamId, Long newStreamId){
		List<MediaVideoStreamUrlRelationPO> medias = mediaVideoStreamUrlRelationDAO.findByVideoStreamIdOrderByVisitCountAsc(streamId);
		
		if (medias != null && medias.size() > 0) {
			List<MediaVideoStreamUrlRelationPO> copyMedias = new ArrayList<MediaVideoStreamUrlRelationPO>();
			List<String> mediaUrls = new ArrayList<String>();
			for(MediaVideoStreamUrlRelationPO videoStream:medias){
				MediaVideoStreamUrlRelationPO copyMedia = new MediaVideoStreamUrlRelationPO();
				copyMedia.setUrl(videoStream.getUrl());
				copyMedia.setVideoStreamId(newStreamId);
				copyMedia.setVisitCount(0l);
				copyMedias.add(copyMedia);
				mediaUrls.add(videoStream.getUrl());
			}
			mediaVideoStreamUrlRelationDAO.save(copyMedias);
			
			return mediaUrls;
		}else {
			return new ArrayList<String>();
		}
	}
}
