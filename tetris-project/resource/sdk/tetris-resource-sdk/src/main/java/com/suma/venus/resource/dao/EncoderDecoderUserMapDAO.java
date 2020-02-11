
package com.suma.venus.resource.dao;

import java.util.Collection;
import java.util.List;

import org.springframework.data.repository.RepositoryDefinition;

import com.suma.venus.resource.pojo.EncoderDecoderUserMap;

@RepositoryDefinition(domainClass = EncoderDecoderUserMap.class, idClass = Long.class)
public interface EncoderDecoderUserMapDAO extends CommonDao<EncoderDecoderUserMap>{

	public EncoderDecoderUserMap findByUserId(Long userId);
	
	public List<EncoderDecoderUserMap> findByUserIdIn(Collection<Long> userIds);
	
	public EncoderDecoderUserMap findByUserName(String userName);
	
}
