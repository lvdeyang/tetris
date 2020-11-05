package com.sumavision.tetris.mims.app.media.encode;

import java.util.Collection;
import java.util.List;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = AudioFileEncodePO.class, idClass = Long.class)
public interface AudioFileEncodeDAO extends BaseDAO<AudioFileEncodePO>{

	/**
	 * 根据音频媒资id查询音频媒资加密信息<br/>
	 * <b>作者:</b>ldy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月8日 上午9:20:06
	 * @param Long mediaId 音频媒资id
	 */
	public AudioFileEncodePO findByMediaId(Long mediaId);
	
	/**
	 * 根据音频媒资id列表查询音频媒资加密信息<br/>
	 * <b>作者:</b>ldy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年9月27日 下午2:23:04
	 * @param Collection<Long> mediaIds 音频媒资id列表
	 */
	public List<AudioFileEncodePO> findByMediaIdIn(Collection<Long> mediaIds);
	
	/**
	 * 判断文件是否需要被删<br/>
	 * <b>作者:</b>ldy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年9月27日 下午2:25:00
	 * @param String filePath 文件路径
	 * @param Collection<Long> mediaIds 音频媒资id列表
	 */
	public List<AudioFileEncodePO> findByFilePathAndMediaIdNotIn(String filePath, Collection<Long> mediaIds);
}
