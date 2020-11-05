package com.sumavision.bvc.device.monitor.live.call;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;
import com.sumavision.tetris.orm.dao.MetBaseDAO;

@RepositoryDefinition(domainClass = MonitorLiveCallPO.class, idClass = Long.class)
public interface MonitorLiveCallDAO extends MetBaseDAO<MonitorLiveCallPO>{

	/**
	 * 获取用户的通话<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月21日 下午4:30:33
	 * @param String user 用户号码
	 * @return List<MonitorLiveCallPO> 通话列表
	 */
	@Query(value = "from com.sumavision.bvc.device.monitor.live.call.MonitorLiveCallPO where callUserId=?1 or calledUserId=?1")
	public List<MonitorLiveCallPO> findByCallUserIdOrCalledUserId(Long userId);
	
	/**
	 * 根据解码设备id查询呼叫用户任务<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月26日 下午2:32:18
	 * @param Collection<String> bundleIds 设备id列表
	 * @return List<MonitorLiveCallPO> 呼叫用户业务列表
	 */
	@Query(value = "from com.sumavision.bvc.device.monitor.live.call.MonitorLiveCallPO where calledDecoderBundleId in ?1 or callDecoderBundleId in ?1")
	public List<MonitorLiveCallPO> findByDecoderBundleIdIn(Collection<String> bundleIds);
	
}
