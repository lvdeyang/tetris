package com.sumavision.bvc.command.bandwidth;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.MetBaseDAO;

@RepositoryDefinition(domainClass=CommandStationBandwidthPO.class,idClass=Long.class)
public interface CommandStationBandwidthDAO extends MetBaseDAO<CommandStationBandwidthPO>{
	
	/**
	 * 通过站点名称以及标识符查找<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年1月29日 下午4:59:13
	 * @param stationName 站点名称
	 * @param identity 标识符
	 * @return CommandStationBandwidthPO
	 */
	public CommandStationBandwidthPO findByStationNameOrIdentity(String stationName, String identity);
}
