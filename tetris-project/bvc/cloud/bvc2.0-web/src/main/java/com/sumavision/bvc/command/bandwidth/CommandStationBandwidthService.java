package com.sumavision.bvc.command.bandwidth;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.suma.venus.resource.dao.SerNodeDao;
import com.suma.venus.resource.pojo.SerNodePO;
import com.suma.venus.resource.pojo.BundlePO.SOURCE_TYPE;
import com.sumavision.tetris.bvc.business.OriginType;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CommandStationBandwidthService {
	
	@Autowired
	private SerNodeDao serNodeDao;
	
	@Autowired
	private CommandStationBandwidthDAO commandStationBandwidthDao;
	
	public void syncSerNodeToStation(){
		
		List<SerNodePO> nodes = serNodeDao.findBySourceType(SOURCE_TYPE.EXTERNAL);
		List<CommandStationBandwidthPO> stations = commandStationBandwidthDao.findAll();
		Set<String> stationNodeUuids = new HashSet<String>();
		for(CommandStationBandwidthPO station : stations){
			stationNodeUuids.add(station.getSerNodeUuid());
		}
		Set<String> nodeUuids = new HashSet<String>();
		for(SerNodePO node : nodes){
			nodeUuids.add(node.getNodeUuid());
		}
		
		//新建
		List<CommandStationBandwidthPO> newStations = new ArrayList<CommandStationBandwidthPO>();
		for(SerNodePO node : nodes){
			if(!stationNodeUuids.contains(node.getNodeUuid())){
				CommandStationBandwidthPO newStation = new CommandStationBandwidthPO();
				newStation.setOriginType(OriginType.OUTER);
				newStation.setSerNodeUuid(node.getNodeUuid());
				newStation.setIdentity(node.getNodeUuid());
				newStation.setTotalWidth(0);
				newStation.setSingleWidth(0);
				newStation.setStationName(node.getNodeName());
				newStations.add(newStation);
			}
		}
		commandStationBandwidthDao.save(newStations);
		if(newStations.size() > 1)  log.info("从级联服务节点同步" + newStations.size() + "个站点");
		
		//删除
		List<CommandStationBandwidthPO> deleteStations = new ArrayList<CommandStationBandwidthPO>();
		for(CommandStationBandwidthPO station : stations){
			if(!OriginType.OUTER.equals(station.getOriginType())) continue;
			if(!nodeUuids.contains(station.getSerNodeUuid())){
				deleteStations.add(station);
				commandStationBandwidthDao.delete(station);
			}
		}
		if(deleteStations.size() > 0) log.info("根据级联服务节点删除了" + deleteStations.size() + "个站点");
		
	}
	
}
