package com.sumavision.tetris.websocket.message;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = WebsocketMessagePO.class, idClass = Long.class)
public interface WebsocketMessageDAO extends BaseDAO<WebsocketMessagePO>{

	/**
	 * 根据用户id和消费情况查询消息列表<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年9月11日 下午1:15:02
	 * @param Long userId 用户id
	 * @param boolean consumed 消息是否被消费
	 * @return List<MessagePO> 消息列表
	 */
	public List<WebsocketMessagePO> findByUserIdAndConsumed(Long userId, boolean consumed);
	
	/**
	 * 根据用户id和消息类型和消费情况查询消息列表<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月14日 下午1:07:54
	 * @param Long userId 用户id
	 * @param WebsocketMessageType messageType 消息类型
	 * @param boolean consumed 消息是否被消费
	 * @return List<MessagePO> 消息列表
	 */
	public List<WebsocketMessagePO> findByUserIdAndMessageTypeAndConsumed(Long userId, WebsocketMessageType messageType, boolean consumed);
	
	/**
	 * 根据用户id和消息类型和消费情况查询消息数量<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月14日 下午1:07:54
	 * @param Long userId 用户id
	 * @param WebsocketMessageType messageType 消息类型
	 * @param boolean consumed 消息是否被消费
	 * @return Long 消息数量
	 */
	public Long countByUserIdAndMessageTypeAndConsumed(Long userId, WebsocketMessageType messageType, boolean consumed);
	
	/**
	 * 根据用户id和发送用户id和消息类型和消费情况查询消息列表<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月14日 下午3:11:54
	 * @param Long userId 用户id
	 * @param Long fromUserId 发送用户id
	 * @param WebsocketMessageType messageType 消息类型
	 * @param boolean consumed 消息是否被消费
	 * @return List<MessagePO> 消息列表
	 */
	public List<WebsocketMessagePO> findByUserIdAndFromUserIdAndMessageTypeAndConsumed(Long userId, Long fromUserId, WebsocketMessageType messageType, boolean consumed);
	
	/**
	 * 分用户统计当前用户的离线即时消息数量<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月14日 下午3:16:27
	 * @param Long userId 用户id
	 */
	@Query(value = "select new com.sumavision.tetris.websocket.message.StatisticsInstantMessageResultDTO(fromUserId, fromUsername, count(fromUserId)) from WebsocketMessagePO where userId=?1 and consumed=0 group by fromUserId")
	public List<StatisticsInstantMessageResultDTO> statisticsUnconsumedInstantMessageNumber(Long userId);
	
	/**
	 * 统计用户收到的特定类型消息数量<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月18日 下午10:25:02
	 * @param Long userId 用户id
	 * @param WebsocketMessageType messageType 消息类型
	 * @return long 消息数量
	 */
	public long countByUserIdAndMessageType(Long userId,  WebsocketMessageType messageType);
	
	/**
	 * 分页用户收到的特定类型消息<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月18日 下午10:27:16
	 * @param Long userId 用户id
	 * @param WebsocketMessageType messageType 消息类型
	 * @param Pageable page 分页信息
	 * @return Page<WebsocketMessagePO> 消息列表
	 */
	public Page<WebsocketMessagePO> findByUserIdAndMessageTypeOrderByUpdateTimeDesc(Long userId, WebsocketMessageType messageType, Pageable page);
	
}
