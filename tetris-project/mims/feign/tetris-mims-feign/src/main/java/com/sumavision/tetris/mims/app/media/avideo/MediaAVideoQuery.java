package com.sumavision.tetris.mims.app.media.avideo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.mims.app.media.audio.MediaAudioFeign;
import com.sumavision.tetris.mims.app.media.video.MediaVideoFeign;
import com.sumavision.tetris.mvc.ext.response.parser.JsonBodyResponseParser;

@Component
public class MediaAVideoQuery {
	@Autowired
	MediaVideoFeign mediaVideoFeign;
	
	@Autowired
	MediaAudioFeign mediaAudioFeign;
	
	public List<MediaAVideoVO> loadAll() throws Exception{
		List<MediaAVideoVO> videos = JsonBodyResponseParser.parseArray(mediaVideoFeign.loadAll(), MediaAVideoVO.class);
		List<MediaAVideoVO> audios = JsonBodyResponseParser.parseArray(mediaAudioFeign.loadAll(), MediaAVideoVO.class);
		
		return dealMediaList(new ArrayListWrapper<MediaAVideoVO>().addAll(videos).addAll(audios).getList());
	}
	
	private List<MediaAVideoVO> dealMediaList(List<MediaAVideoVO> medias){
		if (medias == null || medias.isEmpty()) return null;
		
		List<MediaAVideoVO> removeList = new ArrayListWrapper<MediaAVideoVO>().getList();
		
		for (MediaAVideoVO media : medias) {
			if(media.getChildren().isEmpty()) removeList.add(media);
			removeEmptyFold(media, media.getChildren());
		}
		
		medias.removeAll(removeList);
		
		return medias;
	}
	
	private void removeEmptyFold(MediaAVideoVO parentMedia, List<MediaAVideoVO> medias){
		if (medias == null || medias.isEmpty()) return;
		
		List<MediaAVideoVO> removeList = new ArrayListWrapper<MediaAVideoVO>().getList();
		
		for (MediaAVideoVO media : medias) {
			if (media.getType().equals("FOLDER")) {
				if (media.getChildren().isEmpty()) {
					removeList.add(media);
				} else {
					removeEmptyFold(media, media.getChildren());
				}
			}
		}
		
		parentMedia.getChildren().removeAll(removeList);
	}
}
