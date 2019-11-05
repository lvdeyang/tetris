package com.sumavision.tetris.cs.menu;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sumavision.tetris.cs.program.ScreenQuery;
import com.sumavision.tetris.cs.program.ScreenService;
import com.sumavision.tetris.mims.app.media.avideo.MediaAVideoVO;

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

	public List<CsResourceVO> addResources(List<MediaAVideoVO> resourceList, Long parentId, Long channelId)
			throws Exception {
		List<CsResourceVO> returnList = new ArrayList<CsResourceVO>();

		List<CsResourcePO> aliveResource = resourceDao.findByParentId(parentId);

		CsMenuVO parentMenu = csMenuQuery.getMenuByMenuId(parentId);

		if (aliveResource != null && aliveResource.size() > 0) {
			for (int i = 0; i < aliveResource.size(); i++) {
				returnList.add(new CsResourceVO().set(aliveResource.get(i)));
			}
		}

		if (resourceList != null && !resourceList.isEmpty()) {
			for (MediaAVideoVO aVideoVO : resourceList) {
				if (aliveResource != null && aliveResource.size() > 0) {
					Boolean alive = false;
					for (int j = 0; j < aliveResource.size(); j++) {
						if (aliveResource.get(j).getMimsUuid().equals(aVideoVO.getUuid())) {
							alive = true;
							break;
						}
					}
					if (alive) {
						continue;
					}
				}
				CsResourcePO resource = new CsResourcePO();
				resource.setName(aVideoVO.getName());
				resource.setDuration(aVideoVO.getDuration());
				resource.setPreviewUrl(aVideoVO.getPreviewUrl());
				resource.setEncryption(aVideoVO.getEncryption() != null && aVideoVO.getEncryption() ? "true" : "false");
				resource.setEncryptionUrl(aVideoVO.getEncryptionUrl());
				resource.setDownloadCount(aVideoVO.getDownloadCount());
				resource.setMimsUuid(aVideoVO.getUuid());
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

	public void removeResourcesByMenuId(Long menuId, Long channelId) throws Exception {
		List<CsResourcePO> resourcePOs = resourceDao.findByParentId(menuId);
		if (resourcePOs != null && resourcePOs.size() > 0) {
			for(CsResourcePO item:resourcePOs){
				resourceDao.delete(item);
				screenService.dealWithResourceRemove(channelId, item.getId());
			}
		}
	}
	
	public CsResourceVO removeResource(Long resourceId) throws Exception{
		 CsResourcePO resource = resourceDao.findOne(resourceId);
		
		 resourceDao.delete(resourceId);
		
		 screenService.dealWithResourceRemove(resource.getChannelId(),resourceId);
		
		 return new CsResourceVO().set(resource);
	}

	// 根据MimsId来判断是否删除screen内的媒资
	// public void removeResourcesByMenuId(Long menuId,Long channelId) throws
	// Exception {
	// List<CsResourcePO> resourceList =
	// resourceDao.findResourceFromMenu(menuId);
	// if (resourceList != null && resourceList.size() > 0) {
	// for (int i = 0; i < resourceList.size(); i++) {
	// removeResource(resourceList.get(i).getId());
	// }
	// }
	// }
	//
	// public CsResourceVO removeResource(Long id) throws Exception {
	// CsResourcePO resource = resourceDao.findOne(id);
	//
	// resourceDao.delete(id);
	//
	// String mimsUuid = resource.getMimsUuid();
	// Long channelId = resource.getChannelId();
	// List<CsResourcePO> mimsList =
	// resourceDao.findResourceByChannelAndMims(channelId, mimsUuid);
	// if(mimsList == null || mimsList.size() <= 0){
	// screenService.dealWithResourceRemove(channelId,mimsUuid);
	// }
	//
	// return new CsResourceVO().set(resource);
	// }
}
