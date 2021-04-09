package com.sumavision.tetris.bvc.model.terminal.channel;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = TerminalChannelPO.class, idClass = Long.class)
public interface TerminalChannelDAO extends BaseDAO<TerminalChannelPO>{

	/**
	 * 根据通道类型查询终端通道<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年1月19日 上午10:36:22
	 * @param Collection<TerminalChannelType> types 通道类型列表
	 * @return List<TerminalChannelPO> 终端通道列表
	 */
	public List<TerminalChannelPO> findByTypeIn(Collection<TerminalChannelType> types);
	
	/**
	 * 查询终端通道<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月18日 下午3:05:08
	 * @param Long terminalId 终端id
	 * @return List<TerminalChannelPO> 通道列表
	 */
	public List<TerminalChannelPO> findByTerminalIdOrderByTypeAscNameAsc(Long terminalId);
	
	/**
	 * 根据类型查询终端通道<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月18日 下午3:05:08
	 * @param Long terminalId 终端id
	 * @param TerminalChannelType type 通道类型
	 * @return List<TerminalChannelPO> 通道列表
	 */
	public List<TerminalChannelPO> findByTerminalIdAndTypeOrderByTypeAscNameAsc(Long terminalId, TerminalChannelType type);
	
	/**
	 * 根据角色通道id查询终端通道<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月23日 上午10:41:49
	 * @param roleChannelIds
	 * @return
	 */
	@Query(value = "select * from TETRIS_BVC_MODEL_TERMINAL_CHANNEL channel left join TETRIS_BVC_MODEL_ROLE_CHANNEL_TERMINAL_BUNDLE_CHANNEL m on channel.id = m.terminal_channel_id where m.role_channel_id in ?1", nativeQuery = true)
	public List<TerminalChannelPO> findByRoleChannelIdIn(List<Long> roleChannelIds);
	
	/**
	 * 根据terminalId和screenPrimaryKey查询终端通道<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年9月8日 下午1:04:24
	 * @param terminalId
	 * @param screenPrimaryKey
	 * @return
	 */
	@Query(value = "select * from TETRIS_BVC_MODEL_TERMINAL_CHANNEL channel left join TETRIS_BVC_MODEL_TERMINAL_SCREEN s on channel.id = s.terminal_channel_id where s.terminal_id = ?1 and s.screen_primary_key = ?2", nativeQuery = true)	
	public List<TerminalChannelPO> findByTerminalIdAndScreenPrimaryKey(Long terminalId, String screenPrimaryKey);
	
	/**
	 * 查询终端通道<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月11日 上午11:14:42
	 * @param Long terminalId 终端id
	 * @return List<TerminalChannelPO> 通道列表
	 */
	public List<TerminalChannelPO> findByTerminalId(Long terminalId);
	
	/**
	 * 根据id查询终端通道<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月11日 上午11:15:11
	 * @param Collection<Long> id id列表
	 * @return List<TerminalChannelPO> 通道列表
	 */
	public List<TerminalChannelPO> findByIdIn(Collection<Long> id);
	
	/**
	 * 查询物理屏幕关联的解码通道，带例外<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年10月30日 上午11:18:50
	 * @param Long terminalPhysicalScreenId 终端物理屏幕id
	 * @param Collection<Long> ids 例外通道id列表
	 * @return List<TerminalChannelPO> 通道列表
	 */
	@Query(value = "SELECT CHANNEL.* FROM TETRIS_BVC_MODEL_TERMINAL_PHYSICAL_SCREEN_CHANNEL_PERMISSION PERMISSION LEFT JOIN TETRIS_BVC_MODEL_TERMINAL_CHANNEL CHANNEL ON PERMISSION.TERMINAL_CHANNEL_ID=CHANNEL.ID WHERE PERMISSION.TERMINAL_PHYSICAL_SCREEN_ID=?1 AND CHANNEL.ID NOT IN ?2", nativeQuery = true)
	public List<TerminalChannelPO> findByTerminalPhysicalScreenIdAndIdNotIn(Long terminalPhysicalScreenId, Collection<Long> ids);
	
	/**
	 * 查询物理屏幕关联的解码通道<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年10月30日 上午11:18:50
	 * @param Long terminalPhysicalScreenId 终端物理屏幕id
	 * @return List<TerminalChannelPO> 通道列表
	 */
	@Query(value = "SELECT CHANNEL.* FROM TETRIS_BVC_MODEL_TERMINAL_PHYSICAL_SCREEN_CHANNEL_PERMISSION PERMISSION LEFT JOIN TETRIS_BVC_MODEL_TERMINAL_CHANNEL CHANNEL ON PERMISSION.TERMINAL_CHANNEL_ID=CHANNEL.ID WHERE PERMISSION.TERMINAL_PHYSICAL_SCREEN_ID=?1", nativeQuery = true)
	public List<TerminalChannelPO> findByTerminalPhysicalScreenId(Long terminalPhysicalScreenId);
	
	/**
	 * 查询终端特定类型的通道<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月11日 上午11:18:50
	 * @param Long terminalId 终端id
	 * @param TerminalChannelType type 终端通道类型
	 * @return List<TerminalChannelPO> 终端通道列表
	 */
	public List<TerminalChannelPO> findByTerminalIdAndType(Long terminalId, TerminalChannelType type);
	
	/**
	 * 查询终端下没有关联音频编码的视频编码通道<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月11日 上午11:27:22
	 * @param Long terminalId 终端id
	 * @return List<TerminalChannelPO> 视频编码通道列表
	 */
	@Query(value = "SELECT * FROM TETRIS_BVC_MODEL_TERMINAL_CHANNEL WHERE TERMINAL_ID=?1 AND TYPE='VIDEO_ENCODE' AND ID NOT IN("
			+ "SELECT TERMINAL_VIDEO_CHANNEL_ID FROM TETRIS_BVC_MODEL_TERMINAL_ENCODE_AUDIO_VIDEO_CHANNEL_PERMISSION WHERE TERMINAL_ID=?1)", nativeQuery = true)
	public List<TerminalChannelPO> findFreeVideoEncodeChannelByTerminalId(Long terminalId);
	
	/**
	 * 查询终端下没有配置到屏幕上的视频解码通道<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月11日 上午11:27:22
	 * @param Long terminalId 终端id
	 * @return List<TerminalChannelPO> 视频解码通道列表
	 */
	@Query(value = "SELECT * FROM TETRIS_BVC_MODEL_TERMINAL_CHANNEL WHERE TERMINAL_ID=?1 AND TYPE='VIDEO_DECODE' AND ID NOT IN("
			+ "SELECT TERMINAL_CHANNEL_ID FROM TETRIS_BVC_MODEL_TERMINAL_PHYSICAL_SCREEN_CHANNEL_PERMISSION WHERE TERMINAL_ID=?1)", nativeQuery = true)
	public List<TerminalChannelPO> findFreeVideoDecodeChannelByTerminalId(Long terminalId);
	
	/**
	 * 查询终端下没有配置到音频输出上的音频解码通道<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月11日 上午11:46:12
	 * @param Long terminalId 终端id
	 * @return List<TerminalChannelPO> 视频解码通道列表
	 */
	@Query(value = "SELECT * FROM TETRIS_BVC_MODEL_TERMINAL_CHANNEL WHERE TERMINAL_ID=?1 AND TYPE='AUDIO_DECODE' AND ID NOT IN("
			+ "SELECT TERMINAL_AUDIO_CHANNEL_ID FROM TETRIS_BVC_MODEL_TERMINAL_AUDIO_OUTPUT_CHANNEL_PERMISSION WHERE TERMINAL_ID=?1)", nativeQuery = true)
	public List<TerminalChannelPO> findFreeAudioDecodeChannelByTerminalId(Long terminalId);
	
	/**
	 * 根据物理屏幕查询终端视频解码通道<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月11日 下午1:32:23
	 * @param Collection<Long> terminalPhysicalScreenIds 终端物理屏幕id列表
	 * @return List<TerminalDecodeChannelWithTerminalPhysicalScreenPermissionDTO> 带有物理屏幕关联信息的视频解码通道
	 */
	@Query(value = "select new com.sumavision.tetris.bvc.model.terminal.channel.TerminalDecodeChannelWithTerminalPhysicalScreenPermissionDTO("
					+ "channel.id, "
					+ "channel.name, "
					+ "channel.type, "
					+ "channel.terminalId, "
					+ "permission.terminalPhysicalScreenId, "
					+ "permission.id"
				+ ") "
				+ "from "
				+ "com.sumavision.tetris.bvc.model.terminal.physical.screen.TerminalPhysicalScreenChannelPermissionPO permission,"
				+ "com.sumavision.tetris.bvc.model.terminal.channel.TerminalChannelPO channel "
				+ "where permission.terminalPhysicalScreenId in ?1 "
				+ "and permission.terminalChannelId=channel.id")
	public List<TerminalDecodeChannelWithTerminalPhysicalScreenPermissionDTO> findVideoDecodeChannelByTerminalPhysicalScreenIdIn(Collection<Long> terminalPhysicalScreenIds);
	
	/**
	 * 根据音频输出查询终端音频解码通道<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月11日 下午2:23:36
	 * @param Collection<Long> terminalAudioOutputIds 音频输出id列表
	 * @return List<TerminalDecodeChannelWithTerminalAudioOutputPermissionDTO> 带有音频输出关联信息的音频解码通道
	 */
	@Query(value = "select new com.sumavision.tetris.bvc.model.terminal.channel.TerminalDecodeChannelWithTerminalAudioOutputPermissionDTO("
					+ "channel.id, "
					+ "channel.name, "
					+ "channel.type, "
					+ "channel.terminalId, "
					+ "permission.terminalAudioOutputId, "
					+ "permission.id"
				+ ") "
				+ "from "
				+ "com.sumavision.tetris.bvc.model.terminal.audio.TerminalAudioOutputChannelPermissionPO permission,"
				+ "com.sumavision.tetris.bvc.model.terminal.channel.TerminalChannelPO channel "
				+ "where permission.terminalAudioOutputId in ?1 "
				+ "and permission.terminalAudioChannelId=channel.id")
	public List<TerminalDecodeChannelWithTerminalAudioOutputPermissionDTO> findAudioDecodeChannelByTerminalAudioOutputIdIn(Collection<Long> terminalAudioOutputIds);
	
	/**
	 * 根据音频编码通道查询视频编码通道<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月12日 上午9:37:28
	 * @param Collection<Long> terminalAudioEncodeIds 终端音频编码通道id列表
	 * @return List<TerminalEncodeVideoChannelWithAudioChannelPermissionDTO> 带有音频编码通道信息的视频编码通道
	 */
	@Query(value = "select new com.sumavision.tetris.bvc.model.terminal.channel.TerminalEncodeVideoChannelWithAudioChannelPermissionDTO("
					+"channel.id, "
					+ "channel.name, "
					+ "channel.type, "
					+ "channel.terminalId, "
					+ "permission.terminalAudioChannelId, "
					+ "permission.id"
				+ ") "
				+ "from "
				+ "com.sumavision.tetris.bvc.model.terminal.audio.TerminalEncodeAudioVideoChannelPermissionPO permission, "
				+ "com.sumavision.tetris.bvc.model.terminal.channel.TerminalChannelPO channel "
				+ "where permission.terminalAudioChannelId in ?1 "
				+ "and permission.terminalVideoChannelId=channel.id")
	public List<TerminalEncodeVideoChannelWithAudioChannelPermissionDTO> findVideoEncodeChannelByTerminalAudioEncodeChannelIdIn(Collection<Long> terminalAudioEncodeChannelIds);
	
}
