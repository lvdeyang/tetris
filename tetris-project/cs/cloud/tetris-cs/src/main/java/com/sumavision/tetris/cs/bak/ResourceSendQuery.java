package com.sumavision.tetris.cs.bak;

import java.util.ArrayList;
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

	/**
	 * 根据频道id获取媒资增量列表<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:06:57
	 * @param channelId 频道id
	 * @param sava 获取后是否保存入数据库
	 * @return List<CsResourceVO> 媒资列表
	 */
	public List<CsResourceVO> getAddResource(Long channelId, Boolean save) throws Exception {
		List<CsResourceVO> resources = csResourceQuery.getResourcesFromChannelId(channelId);
		List<ResourceSendPO> previewResource = resourceSendDao.findByChannelId(channelId);
		List<ResourceSendPO> saveResource = new ArrayList<ResourceSendPO>();

		List<CsResourceVO> returnList = new ArrayList<CsResourceVO>();

		if (save) {
			resourceSendDao.deleteInBatch(previewResource);
		}

		if (resources != null && resources.size() > 0) {
			// List<String> mimsUuidList = new ArrayList<String>();
			// Iterator<CsResourceVO> it = resources.iterator();
			// while (it.hasNext()) {
			// CsResourceVO item = it.next();
			// if (mimsUuidList.contains(item.getMimsUuid())) {
			// it.remove();
			// } else {
			// mimsUuidList.add(item.getMimsUuid());
			// }
			// }

			for (CsResourceVO item : resources) {
				ResourceSendPO sourceSend = new ResourceSendPO();
				sourceSend.setChannelId(channelId);
				sourceSend.setMimsUuid(item.getMimsUuid());
				sourceSend.setName(item.getName());
				sourceSend.setParentId(item.getParentId());
				sourceSend.setPreviewUrl(item.getPreviewUrl());
				sourceSend.setTime(item.getDuration());
				sourceSend.setFreq(item.getFreq());
				sourceSend.setAudioPid(item.getAudioPid());
				sourceSend.setVideoPid(item.getVideoPid());
				saveResource.add(sourceSend);

				if (previewResource != null && previewResource.size() > 0) {
					for (int i = 0; i < previewResource.size(); i++) {
						if (previewResource.get(i).getMimsUuid().equals(item.getMimsUuid())
								&& previewResource.get(i).getParentId() == item.getParentId()) {
							break;
						}
						if (i == previewResource.size() - 1) {
							returnList.add(item);
						}
					}
				} else {
					returnList.add(item);
				}
			}
			if (save) {
				resourceSendDao.save(saveResource);
			}
		}

		return returnList;
	}
}
