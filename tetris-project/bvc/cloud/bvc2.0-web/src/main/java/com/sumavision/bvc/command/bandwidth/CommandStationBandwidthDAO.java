package com.sumavision.bvc.command.bandwidth;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.MetBaseDAO;

@RepositoryDefinition(domainClass=CommandStationBandwidthPO.class,idClass=Long.class)
public interface CommandStationBandwidthDAO extends MetBaseDAO<CommandStationBandwidthPO>{

}
