package com.sumavision.tetris.mims.app.store;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.stereotype.Component;

import com.sumavision.tetris.mims.app.material.MaterialFilePO;
import com.sumavision.tetris.mims.app.media.audio.MediaAudioPO;
import com.sumavision.tetris.mims.app.media.compress.MediaCompressPO;
import com.sumavision.tetris.mims.app.media.picture.MediaPicturePO;
import com.sumavision.tetris.mims.app.media.video.MediaVideoPO;

/**
 * 存储相关工具（主查询以及数据转换）<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2018年11月23日 下午3:51:18
 */
@Component
public class StoreQuery {

	/**
	 * 根据素材（批量）生成预删除存储文件<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年11月23日 下午3:56:32
	 * @param Collection<MaterialFilePO> materials 素材列表
	 * @return List<PreRemoveStoreFilePO> 预删除存储文件列表
	 */
	public List<PreRemoveFilePO> preRemoveMaterials(Collection<MaterialFilePO> materials) throws Exception{
		if(materials==null || materials.size()<=0) return null;
		List<PreRemoveFilePO> preRemoveFiles = new ArrayList<PreRemoveFilePO>();
		for(MaterialFilePO material:materials){
			preRemoveFiles.add(preRemoveMaterial(material));
		}
		return preRemoveFiles;
	}
	
	/**
	 * 根据素材生成预删除存储文件<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年11月23日 下午3:53:08
	 * @param MaterialFilePO material 素材文件
	 * @return PreRemoveStoreFilePO 预删除存储文件
	 */
	public PreRemoveFilePO preRemoveMaterial(MaterialFilePO material) throws Exception{
		PreRemoveFilePO preRemoveFile = new PreRemoveFilePO();
		return preRemoveFile;
	}
	
	/**
	 * 根据图片媒资（批量）生成预删除存储文件<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年11月23日 下午3:56:32
	 * @param Collection<MediaPicturePO> pictures 图片媒资列表
	 * @return List<PreRemoveStoreFilePO> 预删除存储文件列表
	 */
	public List<PreRemoveFilePO> preRemoveMediaPictures(Collection<MediaPicturePO> pictures) throws Exception{
		if(pictures==null || pictures.size()<=0) return null;
		List<PreRemoveFilePO> preRemoveFiles = new ArrayList<PreRemoveFilePO>();
		for(MediaPicturePO picture:pictures){
			preRemoveFiles.add(preRemoveMediaPicture(picture));
		}
		return preRemoveFiles;
	}
	
	/**
	 * 根据播发媒资（批量）生成预删除存储文件<br/>
	 * <b>作者:</b>ldy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月31日 下午3:56:32
	 * @param Collection<MediaCompressPO> compresses 播发媒资列表
	 * @return List<PreRemoveStoreFilePO> 预删除存储文件列表
	 */
	public List<PreRemoveFilePO> preRemoveMediaCompresses(Collection<MediaCompressPO> compresses) throws Exception{
		if(compresses==null || compresses.size()<=0) return null;
		List<PreRemoveFilePO> preRemoveFiles = new ArrayList<PreRemoveFilePO>();
		for(MediaCompressPO compress:compresses){
			preRemoveFiles.add(preRemoveMediaPicture(compress));
		}
		return preRemoveFiles;
	}
	
	/**
	 * 根据视频媒资（批量）生成预删除存储文件<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年11月23日 下午3:56:32
	 * @param Collection<MediaVideoPO> videos 视频媒资列表
	 * @return List<PreRemoveStoreFilePO> 预删除存储文件列表
	 */
	public List<PreRemoveFilePO> preRemoveMediaVideos(Collection<MediaVideoPO> videos) throws Exception{
		if(videos==null || videos.size()<=0) return null;
		List<PreRemoveFilePO> preRemoveFiles = new ArrayList<PreRemoveFilePO>();
		for(MediaVideoPO video:videos){
			preRemoveFiles.add(preRemoveMediaVideo(video));
		}
		return preRemoveFiles;
	}
	
	/**
	 * 根据音频媒资（批量）生成预删除存储文件<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年11月23日 下午3:56:32
	 * @param Collection<MediaAudioPO> videos 音频媒资列表
	 * @return List<PreRemoveStoreFilePO> 预删除存储文件列表
	 */
	public List<PreRemoveFilePO> preRemoveMediaAudios(Collection<MediaAudioPO> audios) throws Exception{
		if(audios==null || audios.size()<=0) return null;
		List<PreRemoveFilePO> preRemoveFiles = new ArrayList<PreRemoveFilePO>();
		for(MediaAudioPO audio:audios){
			preRemoveFiles.add(preRemoveMediaAudio(audio));
		}
		return preRemoveFiles;
	}
	
	/**
	 * 根据图片媒资生成预删除存储文件<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年11月23日 下午3:53:08
	 * @param MediaPicturePO picture 图片媒资
	 * @return PreRemoveStoreFilePO 预删除存储文件
	 */
	public PreRemoveFilePO preRemoveMediaPicture(MediaPicturePO picture) throws Exception{
		PreRemoveFilePO preRemoveFile = new PreRemoveFilePO();
		return preRemoveFile;
	}
	
	/**
	 * 根据播发媒资生成预删除存储文件<br/>
	 * <b>作者:</b>ldy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年3月31日 下午3:53:08
	 * @param MediaCompressPO compress 播发媒资
	 * @return PreRemoveStoreFilePO 预删除存储文件
	 */
	public PreRemoveFilePO preRemoveMediaPicture(MediaCompressPO compress) throws Exception{
		PreRemoveFilePO preRemoveFile = new PreRemoveFilePO();
		return preRemoveFile;
	}
	
	/**
	 * 根据视频媒资生成预删除存储文件<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年11月23日 下午3:53:08
	 * @param MediaAudioPO video 视频媒资
	 * @return PreRemoveStoreFilePO 预删除存储文件
	 */
	public PreRemoveFilePO preRemoveMediaVideo(MediaVideoPO video) throws Exception{
		PreRemoveFilePO preRemoveFile = new PreRemoveFilePO();
		return preRemoveFile;
	}
	
	/**
	 * 根据音频媒资生成预删除存储文件<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年11月23日 下午3:53:08
	 * @param MediaAudioPO audio 音频媒资
	 * @return PreRemoveStoreFilePO 预删除存储文件
	 */
	public PreRemoveFilePO preRemoveMediaAudio(MediaAudioPO audio) throws Exception{
		PreRemoveFilePO preRemoveFile = new PreRemoveFilePO();
		return preRemoveFile;
	}
	
	/**
	 * 将预删除存储文件押入队列<br/>
	 * <p>
	 * 	这里要启动线程去做，线程要延迟5秒（可变）再加入队列，加入前要<br/>
	 *  检验预删除文件数据是否存在，如果不存在认为程序发生异常回滚了<br/>
	 * </p>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年11月23日 下午4:20:49
	 * @throws Exception
	 */
	public void pushPreRemoveFileToQueue(Collection<PreRemoveFilePO> preRemoveFiles) throws Exception{
		//TODO 这里还没有实现
	}
	
}
