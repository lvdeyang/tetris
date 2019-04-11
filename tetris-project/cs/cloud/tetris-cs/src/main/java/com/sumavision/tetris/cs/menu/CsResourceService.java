package com.sumavision.tetris.cs.menu;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sumavision.tetris.cs.program.ScreenQuery;
import com.sumavision.tetris.cs.program.ScreenService;
import com.sumavision.tetris.mims.app.media.video.MediaVideoVO;

@Service
@Transactional(rollbackFor = Exception.class)
public class CsResourceService {
	@Autowired
	CsResourceDAO resourceDao;

	@Autowired
	CsResourceQuery csResourceQuery;
	
	@Autowired
	CsMenuQuery csMenuQuery;
	
	@Autowired
	ScreenService screenService;
	
	@Autowired
	ScreenQuery screenQuery;

	public List<CsResourceVO> addResources(List<MediaVideoVO> resourceList, Long parentId,Long channelId) throws Exception {
		List<CsResourceVO> returnList = new ArrayList<CsResourceVO>();

		List<CsResourcePO> aliveResource = resourceDao.findResourceFromMenu(parentId);
		
		CsMenuVO parentMenu = csMenuQuery.getMenuByMenuId(parentId);

		if (aliveResource != null && aliveResource.size() > 0) {
			for (int i = 0; i < aliveResource.size(); i++) {
				returnList.add(new CsResourceVO().set(aliveResource.get(i)));
			}
		}

		if (resourceList != null && resourceList.size() > 0) {
			for (int i = 0; i < resourceList.size(); i++) {
				if (aliveResource != null && aliveResource.size() > 0) {
					Boolean alive = false;
					for (int j = 0; j < aliveResource.size(); j++) {
						if (aliveResource.get(j).getMimsId() == resourceList.get(i).getId()) {
							alive = true;
							break;
						}
					}
					if (alive) {
						continue;
					}
				}
				CsResourcePO resource = new CsResourcePO();
				resource.setName(resourceList.get(i).getName());
				resource.setTime("");
				resource.setPreviewUrl(resourceList.get(i).getPreviewUrl());
				resource.setMimsId(resourceList.get(i).getId());
				resource.setChannelId(channelId);
				resource.setParentId(parentId);
				resource.setParentPath(parentMenu.getParentPath() + "/" + parentMenu.getName());
				resource.setUpdateTime(new Date());
				resourceDao.save(resource);
				returnList.add(new CsResourceVO().set(resource));
			}
		}

		return returnList;
	}

	public void removeResourcesByMenuId(Long menuId,Long channelId) throws Exception {
		List<CsResourcePO> resourceList = resourceDao.findResourceFromMenu(menuId);
		if (resourceList != null && resourceList.size() > 0) {
			for (int i = 0; i < resourceList.size(); i++) {
				removeResource(resourceList.get(i).getId());
			}
		}
	}

	public CsResourceVO removeResource(Long id) throws Exception {
		CsResourcePO resource = resourceDao.findOne(id);

		resourceDao.delete(id);
		
		Long mimsId = resource.getMimsId();
		Long channelId = resource.getChannelId();
		List<CsResourcePO> mimsList = resourceDao.findResourceByChannelAndMims(channelId, mimsId);
		if(mimsList == null || mimsList.size() <= 0){
			screenService.dealWithResourceRemove(channelId,mimsId);
		}

		return new CsResourceVO().set(resource);
	}
}
