package com.sumavision.bvc.command.group.dao;

import java.util.Collection;
import java.util.List;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.bvc.command.group.vod.CommandVodPO;
import com.sumavision.tetris.orm.dao.MetBaseDAO;

@RepositoryDefinition(domainClass = CommandVodPO.class, idClass = Long.class)
public interface CommandVodDAO extends MetBaseDAO<CommandVodPO>{

	public CommandVodPO findByDstBundleId(String dstBundleId);

	public List<CommandVodPO> findByDstBundleIdIn(Collection<String> dstBundleIds);
}
