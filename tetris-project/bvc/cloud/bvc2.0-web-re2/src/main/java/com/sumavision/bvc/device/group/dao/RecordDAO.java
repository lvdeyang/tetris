package com.sumavision.bvc.device.group.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.bvc.device.group.enumeration.RecordType;
import com.sumavision.bvc.device.group.po.RecordPO;
import com.sumavision.tetris.orm.dao.MetBaseDAO;

@RepositoryDefinition(domainClass = RecordPO.class, idClass = long.class)
public interface RecordDAO extends MetBaseDAO<RecordPO>{

	/**
	 * @Title: 查找设备组所有设备录制 
	 * @param GroupId
	 * @return List<RecordPO>    返回类型 
	 */
	@Query("select record from RecordPO record where record.group.id=?1 and record.type=?2")
	public List<RecordPO> findBundleRecordByGroupId(Long GroupId, RecordType type);
	
	//@Query("select record from RecordPO record where record.group.id=?1")
	public List<RecordPO> findByGroupId(Long groupId);
}
