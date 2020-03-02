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

		List<CsResourcePO> aliveResourcePOs = resourceDao.findByParentId(parentId);
		List<CsResourceVO> aliveResource = CsResourceVO.getConverter(CsResourceVO.class).convert(aliveResourcePOs, CsResourceVO.class);
		if (aliveResource != null && !aliveResource.isEmpty()) {
			returnList.addAll(aliveResource);
		}

		if (resourceList == null || resourceList.isEmpty()) return returnList;
		CsMenuVO parentMenu = csMenuQuery.getMenuByMenuId(parentId);
		List<CsResourcePO> saveResources = new ArrayList<CsResourcePO>();
		for (MediaAVideoVO aVideoVO : resourceList) {
			Boolean alive = false;
			if (aliveResource != null && !aliveResource.isEmpty()){
				for (CsResourceVO resource : aliveResource) {
					if (resource.getMimsUuid().equals(aVideoVO.getUuid())){
						alive = true;
						break;
					}
				}
			}
			if (alive) continue;
			CsResourcePO newresource = new CsResourcePO().getFromAVideoVO(aVideoVO);
			newresource.setChannelId(channelId);
			newresource.setParentId(parentId);
			newresource.setParentPath(parentMenu.getParentPath() + "/" + parentMenu.getName());
			newresource.setUpdateTime(new Date());
			saveResources.add(newresource);
		}
		if (!saveResources.isEmpty()) {
			resourceDao.save(saveResources);
			returnList.addAll(CsResourceVO.getConverter(CsResourceVO.class).convert(saveResources, CsResourceVO.class));
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
