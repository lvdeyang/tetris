package com.sumavision.tetris.sts.device;

/**
 * Created by Lost on 2017/8/10.
 */

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.sts.common.CommonDao;

import java.util.List;

@RepositoryDefinition(domainClass = StreamAccConfigPO.class, idClass = Long.class)
public interface StreamAccConfigDao extends CommonDao<StreamAccConfigPO> {

    List<StreamAccConfigPO> findByStreamMediaId(Long streamMediaId);
}
