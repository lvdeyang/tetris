package com.sumavision.tetris.bvc.business.terminal.hall;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;

import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = TerminalBundleConferenceHallPermissionPO.class, idClass = Long.class)
public interface TerminalBundleConferenceHallPermissionDAO extends BaseDAO<TerminalBundleConferenceHallPermissionPO>{

	/**
	 * 查询会场设备绑定<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月10日 下午5:07:23
	 * @param Long conferenceHallId 会场id
	 * @return List<TerminalBundleConferenceHallPermissionPO> 设备信息列表
	 */
	public List<TerminalBundleConferenceHallPermissionPO> findByConferenceHallId(Long conferenceHallId);
	
	/**
	 * 查询会场设备绑定<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月10日 下午5:07:23
	 * @param Collection<Long> conferenceHallIds 会场id列表
	 * @return List<TerminalBundleConferenceHallPermissionPO> 设备信息列表
	 */
	public List<TerminalBundleConferenceHallPermissionPO> findByConferenceHallIdIn(Collection<Long> conferenceHallIds);
	
	/**
	 * 查询会场终端的设备绑定<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月13日 下午2:23:02
	 * @param Long conferenceHallId 会场id
	 * @param Long terminalBundleId 终端设备id
	 * @return TerminalBundleConferenceHallPermissionPO 设备绑定信息
	 */
	public TerminalBundleConferenceHallPermissionPO findByConferenceHallIdAndTerminalBundleId(Long conferenceHallId, Long terminalBundleId);
	
	public List<TerminalBundleConferenceHallPermissionPO> findByBundleIdIn(List<String> bundleIds);
	
	public List<TerminalBundleConferenceHallPermissionPO> findByTerminalBundleIdAndBundleId(Long terminalBundleId,String bundleId);
	
	public List<TerminalBundleConferenceHallPermissionPO> findByBundleTypeAndBundleId(String bundleType, String bundleId);
	
	@Query(value = "SELECT * FROM TETRIS_BVC_BUSINESS_TERMINAL_BUNDLE_CONFERENCE_HALL_PERMISSION permission LEFT JOIN (select * from BundlePO where id in ?) as bundle on"
			+ "permission.bundleId = bundle.bundleId AND permission.bundleType = bundle.deviceModel",nativeQuery = true)
	public List<TerminalBundleConferenceHallPermissionPO> findByBundleTypeInAndBundleIdIn(Collection<Long> bundleIds);
}
