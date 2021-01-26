package com.sumavision.tetris.mims.app.media.stream.video;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

		mediaVideoStreamUrlRelationDAO.saveAll(mediaVideoStreamUrlRelationPOs);

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
		
		mediaVideoStreamUrlRelationDAO.saveAll(addUrl);
		
		return mediaVideoStreamUrlRelationDAO.findByVideoStreamIdOrderByVisitCountAsc(streamId);
	}
	
	/**
	 * 复制视频流信息<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月25日 下午5:02:16
	 * @param Long streamId 视频流id
	 * @param Long newStreamId 新的视频流id
	 */
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
			mediaVideoStreamUrlRelationDAO.saveAll(copyMedias);
			
			return mediaUrls;
		}else {
			return new ArrayList<String>();
		}
	}
	
	/**
	 * 根据url地址删除数据<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月25日 下午5:28:20
	 * @param List<String> urls 地址列表
	 * @return List<Long> url对应的视频流列表id
	 */
	public List<Long> remove(List<String> urls) throws Exception {
		if (urls == null || urls.isEmpty()) return null;
		
		List<MediaVideoStreamUrlRelationPO> medias = mediaVideoStreamUrlRelationDAO.findByUrlIn(urls);
		
		if (medias == null || medias.isEmpty()) return null;
		
		mediaVideoStreamUrlRelationDAO.deleteInBatch(medias);
		
		return medias.stream().map(MediaVideoStreamUrlRelationPO::getVideoStreamId).collect(Collectors.toList());
	}
}
