package com.sumavision.bvc.device.monitor.live;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;
import com.sumavision.tetris.orm.dao.MetBaseDAO;

@Deprecated
@RepositoryDefinition(domainClass = MonitorUserCallPO.class, idClass = Long.class)
public interface MonitorUserCallDAO extends MetBaseDAO<MonitorUserCallPO>{

	/**
	 * 获取用户的通话<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月21日 下午4:30:33
	 * @param String user 用户号码
	 * @return List<MonitorUserCallPO> 通话列表
	 */
	@Query(value = "from com.sumavision.bvc.device.monitor.live.MonitorUserCallPO where (srcUser=?1 or dstUser=?1) and type=?2")
	public List<MonitorUserCallPO> findBySrcUserOrDstUserAndType(String user, String type);
	
	/**
	 * 查询两个用户之间的通话<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月21日 下午4:42:18
	 * @param String srcUser 主叫用户
	 * @param String dstUser 被叫用户
	 * @return List<MonitorUserCallPO> 通话列表
	 */
	public List<MonitorUserCallPO> findBySrcUserAndDstUserAndType(String srcUser, String dstUser, String type);
	
	/**
	 * 根据uuid查询两个用户之间的通话<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月21日 下午4:42:18
	 * @param String uuid 通话uuid
	 * @return MonitorUserCallPO 通话列表
	 */
	public MonitorUserCallPO findByUuidAndType(String uuid, String type);
}
