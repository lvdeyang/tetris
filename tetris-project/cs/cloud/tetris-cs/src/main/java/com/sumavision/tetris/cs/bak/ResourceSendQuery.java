package com.sumavision.tetris.cs.bak;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sumavision.tetris.cs.menu.CsResourceQuery;
import com.sumavision.tetris.cs.menu.CsResourceVO;

import scala.annotation.elidable;

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
			List<String> mimsUuidList = new ArrayList<String>();
			Iterator<CsResourceVO> it = resources.iterator();
			while (it.hasNext()) {
				CsResourceVO item = it.next();
				if (mimsUuidList.contains(item.getMimsUuid())) {
					it.remove();
				} else {
					mimsUuidList.add(item.getMimsUuid());
				}
			}

			for (CsResourceVO item : resources) {
				ResourceSendPO sourceSend = new ResourceSendPO();
				sourceSend.setChannelId(channelId);
				sourceSend.setMimsUuid(item.getMimsUuid());
				sourceSend.setName(item.getName());
				sourceSend.setParentId(item.getParentId());
				sourceSend.setPreviewUrl(item.getPreviewUrl());
				sourceSend.setTime(item.getTime());
				saveResource.add(sourceSend);

				if (previewResource != null && previewResource.size() > 0) {
					for (int i = 0; i < previewResource.size(); i++) {
						if (previewResource.get(i).getMimsUuid() == item.getMimsUuid()) {
							break;
						}
						if (i == previewResource.size() - 1) {
							returnList.add(item);
						}
					}
				}else{
					returnList.add(item);
				}
			}
			resourceSendDao.save(saveResource);
		}

		return returnList;
	}
}
