package com.sumavision.tetris.cs.bak;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sumavision.tetris.cs.menu.CsResourceQuery;
import com.sumavision.tetris.cs.menu.CsResourceVO;

@Component
public class ResourceSendQuery {
	@Autowired
	ResourceSendDAO resourceSendDao;

	@Autowired
	CsResourceQuery csResourceQuery;

	public List<CsResourceVO> saveResource(Long channelId) throws Exception {
		List<CsResourceVO> resources = csResourceQuery.getResourcesFromChannelId(channelId);
		List<ResourceSendPO> previewResource = resourceSendDao.findByChannelId(channelId);
		List<ResourceSendPO> saveResource = new ArrayList<ResourceSendPO>();

		List<CsResourceVO> returnList = new ArrayList<CsResourceVO>();

		resourceSendDao.deleteInBatch(previewResource);
		if (resources != null && resources.size() > 0) {
			List<Long> mimsIdList = new ArrayList<Long>();
			Iterator<CsResourceVO> it = resources.iterator();
			while (it.hasNext()) {
				CsResourceVO item = it.next();
				if (mimsIdList.contains(item.getMimsId())) {
					it.remove();
				} else {
					mimsIdList.add(item.getMimsId());
				}
			}

			for (CsResourceVO item : resources) {
				ResourceSendPO sourceSend = new ResourceSendPO();
				sourceSend.setChannelId(channelId);
				sourceSend.setMimsId(item.getMimsId());
				sourceSend.setName(item.getName());
				sourceSend.setParentId(item.getParentId());
				sourceSend.setPreviewUrl(item.getPreviewUrl());
				sourceSend.setTime(item.getTime());
				saveResource.add(sourceSend);

				if (previewResource != null && previewResource.size() > 0) {
					for (int i = 0; i < previewResource.size(); i++) {
						if (previewResource.get(i).getMimsId() == item.getMimsId()) {
							break;
						}
						if (i == previewResource.size() - 1) {
							returnList.add(item);
						}
					}
				}
			}
			resourceSendDao.save(saveResource);
		}

		return returnList;
	}
}
