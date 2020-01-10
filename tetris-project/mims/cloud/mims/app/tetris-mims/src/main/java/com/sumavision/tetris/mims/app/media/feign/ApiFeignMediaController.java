package com.sumavision.tetris.mims.app.media.feign;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.mims.app.media.audio.MediaAudioQuery;
import com.sumavision.tetris.mims.app.media.audio.MediaAudioVO;
import com.sumavision.tetris.mims.app.media.picture.MediaPictureQuery;
import com.sumavision.tetris.mims.app.media.picture.MediaPictureVO;
import com.sumavision.tetris.mims.app.media.stream.audio.MediaAudioStreamQuery;
import com.sumavision.tetris.mims.app.media.stream.audio.MediaAudioStreamVO;
import com.sumavision.tetris.mims.app.media.stream.video.MediaVideoStreamQuery;
import com.sumavision.tetris.mims.app.media.stream.video.MediaVideoStreamVO;
import com.sumavision.tetris.mims.app.media.txt.MediaTxtQuery;
import com.sumavision.tetris.mims.app.media.txt.MediaTxtVO;
import com.sumavision.tetris.mims.app.media.video.MediaVideoQuery;
import com.sumavision.tetris.mims.app.media.video.MediaVideoVO;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.mvc.listener.ServletContextListener.Path;
import com.sumavision.tetris.orm.exception.ErrorTypeException;

@Controller
@RequestMapping(value = "/media/feign")
public class ApiFeignMediaController {
	
	@Autowired
	private MediaVideoQuery mediaVideoQuery;
	
	@Autowired
	private MediaAudioQuery mediaAudioQuery;
	
	@Autowired
	private MediaPictureQuery mediaPictureQuery;
	
	@Autowired
	private MediaTxtQuery mediaTxtQuery;
	
	@Autowired
	private MediaVideoStreamQuery mediaVideoStreamQuery;
	
	@Autowired
	private MediaAudioStreamQuery mediaAudioStreamQuery;
	
	@Autowired
	private Path path;
	
	/**
	 * 根据条件查询媒资<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年9月19日 下午3:17:30
	 * @param Long id 媒资id
	 * @param String name 名称(模糊匹配)
	 * @param String startTime updateTime起始查询
	 * @param Stinrg endTime updateTime终止查询
	 * @param Long tagId 标签id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/query/by/condition")
	public Object queryByCondition(Long id, String name, String type, String startTime, String endTime, Long tagId, HttpServletRequest request) throws Exception{
		
		List<MediaVideoVO> videoVOs = new ArrayList<MediaVideoVO>();
		List<MediaAudioVO> audioVOs = new ArrayList<MediaAudioVO>();
		List<MediaPictureVO> pictureVOs = new ArrayList<MediaPictureVO>();
		List<MediaTxtVO> txtVOs = new ArrayList<MediaTxtVO>();
		List<MediaVideoStreamVO> videoStreamVOs = new ArrayList<MediaVideoStreamVO>();
		List<MediaAudioStreamVO> audioStreamVOs = new ArrayList<MediaAudioStreamVO>();
		
		if (type == null) {
			videoVOs = mediaVideoQuery.loadByCondition(id, name, startTime, endTime, tagId);
			audioVOs = mediaAudioQuery.loadByCondition(id, name, startTime, endTime, tagId);
			pictureVOs = mediaPictureQuery.loadByCondition(id, name, startTime, endTime, tagId);
			txtVOs = mediaTxtQuery.loadByCondition(id, name, startTime, endTime, tagId);
			videoStreamVOs = mediaVideoStreamQuery.loadByCondition(id, name, startTime, endTime, tagId);
			audioStreamVOs = mediaAudioStreamQuery.loadByCondition(id, name, startTime, endTime, tagId);
		} else {
			switch (type.toLowerCase()) {
			case "video":
				videoVOs = mediaVideoQuery.loadByCondition(id, name, startTime, endTime, tagId);
				break;
			case "audio":
				audioVOs = mediaAudioQuery.loadByCondition(id, name, startTime, endTime, tagId);
				break;
			case "picture":
				pictureVOs = mediaPictureQuery.loadByCondition(id, name, startTime, endTime, tagId);
				break;
			case "txt":
				txtVOs = mediaTxtQuery.loadByCondition(id, name, startTime, endTime, tagId);
				break;
			case "videostream":
				videoStreamVOs = mediaVideoStreamQuery.loadByCondition(id, name, startTime, endTime, tagId);
				break;
			case "audiostream":
				audioStreamVOs = mediaAudioStreamQuery.loadByCondition(id, name, startTime, endTime, tagId);
				break;
			default:
				throw new ErrorTypeException("type",type);
			}
		}
		
		return new ArrayListWrapper<Object>()
				.addAll(videoVOs)
				.addAll(audioVOs)
				.addAll(pictureVOs)
				.addAll(txtVOs)
				.addAll(videoStreamVOs)
				.addAll(audioStreamVOs)
				.getList();
	}
	
	/**
	 * 从http目录获取目录下资源http地址(云转码使用)<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年1月10日 上午9:04:08
	 * @param String url 目录地址
	 * @return String 文件http地址
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/query/by/url")
	public Object getFileFromDir(String url, HttpServletRequest request) throws Exception{
		String fileUrl = null;
		if (url != null && !url.isEmpty()){
			File file = new File(url);
			String folderPath = new StringBufferWrapper().append(path.webappPath()).append("upload").append(file.getPath().split("upload")[1]).toString();
			File localFile = new File(folderPath);
			File[] fileList = localFile.listFiles();
			for (File childFile : fileList) {
				String fileNameSuffix = childFile.getName().split("\\.")[1];
				if (fileNameSuffix.equals("xml")) {
					continue;
				}else {
					String fileName = childFile.getName();
					fileUrl = new StringBufferWrapper().append(url).append("/").append(fileName).toString();
					break;
				}
			}
		}
		return fileUrl;
	}
}
