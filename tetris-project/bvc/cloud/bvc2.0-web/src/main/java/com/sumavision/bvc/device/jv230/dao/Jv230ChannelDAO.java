package com.sumavision.bvc.device.jv230.dao;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.bvc.device.jv230.dto.Jv230ChannelDTO;
import com.sumavision.bvc.device.jv230.po.Jv230ChannelPO;
import com.sumavision.tetris.orm.dao.MetBaseDAO;

@RepositoryDefinition(domainClass = Jv230ChannelPO.class, idClass = long.class)
public interface Jv230ChannelDAO extends MetBaseDAO<Jv230ChannelPO>{

	/**
	 * @Title: 根据大屏后去jv230通道 <br/>
	 * @param combineJv230Ids 大屏id
	 * @return List<Jv230ChannelDTO> jv230通道
	 */
	@Query("select new com.sumavision.bvc.device.jv230.dto.Jv230ChannelDTO(channel.bundleId, channel.layerId, channel.channelId) from Jv230ChannelPO channel left join channel.jv230 jv230 left join jv230.combineJv230 combineJv230 where combineJv230.id in ?1")
	public List<Jv230ChannelDTO> findByCombineJv230Ids(Collection<Long> combineJv230Ids);
	
}
