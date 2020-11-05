package com.sumavision.tetris.sts.task.tasklink;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.sts.common.CommonDao;

@RepositoryDefinition(domainClass = StreamMediaPO.class, idClass = Long.class)
public interface StreamMediaDao extends CommonDao<StreamMediaPO> {
    StreamMediaPO findTopBySocketAddress(String socketAddress);

    List<StreamMediaPO> findByGroupId(Long groupId);

    @Query(value = "select socketAddress from StreamMediaPO")
    List<String> findAllSocketAddress();

    StreamMediaPO findTopBySocketAddressContaining(String ip);
}
