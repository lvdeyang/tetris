package com.sumavision.tetris.mims.app.media.avideo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.mims.app.folder.FolderType;
import com.sumavision.tetris.mims.app.folder.FolderVO;
import com.sumavision.tetris.mims.app.media.audio.MediaAudioFeign;
import com.sumavision.tetris.mims.app.media.video.MediaVideoFeign;
import com.sumavision.tetris.mvc.ext.response.parser.JsonBodyResponseParser;
import com.sumavision.tetris.orm.exception.ErrorTypeException;

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
	
	public HashMapWrapper<String, MediaAVideoVO> getByUuids(List<String> uuids) throws Exception{
		if (uuids == null || uuids.isEmpty()) return null;
		
		String uuidString = JSONArray.toJSONString(uuids);
		
		List<MediaAVideoVO> videos = JsonBodyResponseParser.parseArray(mediaVideoFeign.getByUuids(uuidString), MediaAVideoVO.class);
		List<MediaAVideoVO> audios = JsonBodyResponseParser.parseArray(mediaAudioFeign.getByUuids(uuidString), MediaAVideoVO.class);
		
		List<MediaAVideoVO> videoVOs = new ArrayListWrapper<MediaAVideoVO>().addAll(videos).addAll(audios).getList();
		
		HashMapWrapper<String, MediaAVideoVO> map =	new HashMapWrapper<String, MediaAVideoVO>();
		for (MediaAVideoVO mediaAVideoVO : videoVOs) {
			map.put(mediaAVideoVO.getUuid(), mediaAVideoVO);
		}
		
		return map;
	}
	
	public String buildUrl(FolderVO folder, String name) throws Exception{
		if (folder == null) {
			return null;
		}
		if (folder.getType() == FolderType.COMPANY_VIDEO) {
			return JsonBodyResponseParser.parseObject(mediaVideoFeign.buildUrl(name, folder.getUuid()), String.class);
		}else {
			return JsonBodyResponseParser.parseObject(mediaAudioFeign.buildUrl(name, folder.getUuid()), String.class);
		}
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
	
	public MediaAVideoVO loadByIdAndType(Long id, String type) throws Exception{
		switch (type.toLowerCase()) {
		case "video":
			return JsonBodyResponseParser.parseObject(mediaVideoFeign.getById(id), MediaAVideoVO.class);
		case "audio":
			return JsonBodyResponseParser.parseObject(mediaAudioFeign.getById(id), MediaAVideoVO.class);
		default:
			throw new ErrorTypeException("mediaType", type);
		}
	}
	
	public List<MediaAVideoVO> findByPreviewUrlIn(List<String> previewUrls, String type) throws Exception {
		switch (type.toLowerCase()) {
		case "video":
			return JsonBodyResponseParser.parseArray(mediaVideoFeign.findByPreviewUrlIn(JSON.toJSONString(previewUrls)), MediaAVideoVO.class);
		case "audio":
			return JsonBodyResponseParser.parseArray(mediaAudioFeign.findByPreviewUrlIn(JSON.toJSONString(previewUrls)), MediaAVideoVO.class);
		default:
			throw new ErrorTypeException("mediaType", type);
		}
	}
}
