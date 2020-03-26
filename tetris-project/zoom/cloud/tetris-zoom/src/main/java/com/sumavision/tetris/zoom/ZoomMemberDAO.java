package com.sumavision.tetris.zoom;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = ZoomMemberPO.class, idClass = Long.class)
public interface ZoomMemberDAO extends BaseDAO<ZoomMemberPO>{

	/**
	 * 查询会议下的所有成员<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月2日 下午3:38:36
	 * @param Long zoomId 会议id
	 * @return List<ZoomMemberPO> 会议成员列表
	 */
	public List<ZoomMemberPO> findByZoomId(Long zoomId);
	
	/**
	 * 查询会议成员id带例外<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月19日 下午3:16:20
	 * @param Long zoomId 会议室id
	 * @param Collection<Long> exceptId 例外成员id
	 * @param boolean join 是否入会
	 * @return List<Long> 会议成员id列表
	 */
	@Query(value = "select member.id from com.sumavision.tetris.zoom.ZoomMemberPO member where member.zoomId=?1 and member.id not in and join=?3")
	public List<Long> findIdByZoomIdAndIdNotIn(Long zoomId, Collection<Long> exceptId, boolean join);
	
	/**
	 * 查询会议中的成员<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月3日 上午10:52:21
	 * @param Long zoomId 会议id
	 * @param String userId 用户id
	 * @return ZoomMemberPO 会议成员
	 */
	public ZoomMemberPO findByZoomIdAndUserId(Long zoomId, String userId);
	
	/**
	 * 查询会议中的成员，带例外用户<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月3日 下午4:58:25
	 * @param Long zoomId 会议id
	 * @param Long exceptId 例外成员id
	 * @return List<ZoomMemberPO> 会议成员列表
	 */
	public List<ZoomMemberPO> findByZoomIdAndIdNot(Long zoomId, Long exceptId);
	
	/**
	 * 查询会议中的成员，带例外用户列表<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月4日 上午9:13:44
	 * @param Long zoomId 会议id
	 * @param Collection<Long> exceptIds 例外成员id列表
	 * @return List<ZoomMemberPO> 会议成员列表
	 */
	public List<ZoomMemberPO> findByZoomIdAndIdNotIn(Long zoomId, Collection<Long> exceptIds);
	
	/**
	 * 查询会议中的主席<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月4日 上午11:16:55
	 * @param Long zoomId 会议id
	 * @param Boolean chairman 主席标识
	 * @return ZoomMemberPO 主席成员
	 */
	public ZoomMemberPO findByZoomIdAndChairman(Long zoomId, Boolean chairman);
	
	/**
	 * 查询用户参加的会<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月18日 下午4:12:00
	 * @param String userno 用户号码
	 * @return List<ZoomMemberPO> 会议成员
	 */
	public List<ZoomMemberPO> findByUserno(String userno);
	
}
