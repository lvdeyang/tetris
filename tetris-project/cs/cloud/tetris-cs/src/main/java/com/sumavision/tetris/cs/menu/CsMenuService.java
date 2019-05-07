package com.sumavision.tetris.cs.menu;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class CsMenuService {
	@Autowired
	private CsMenuDAO menuDao;

	@Autowired
	private CsMenuQuery menuQuery;

	@Autowired
	private CsResourceService csResourceService;

	public CsMenuPO addRoot(Long channelId, String name) {
		CsMenuPO menuPO = new CsMenuPO();
		menuPO.setName(name);
		menuPO.setUpdateTime(new Date());
		menuPO.setChannelId(channelId);
		menuPO.setParentId(-1l);
		menuPO.setParentPath(".");

		menuDao.save(menuPO);

		return menuPO;
	}

	public CsMenuPO topPO(Long id, Long newParentId) {
		CsMenuPO menuPO = menuDao.findOne(id);
		if (newParentId == -1) {
			menuPO.setParentId(-1l);
			menuPO.setParentPath(".");
		} else {
			CsMenuPO parentPO = menuDao.findOne(newParentId);
			String parentPath = parentPO.getParentPath() + "/" + parentPO.getName();
			menuPO.setParentId(newParentId);
			menuPO.setParentPath(parentPath);
		}

		menuDao.save(menuPO);

		return menuPO;
	}

	public CsMenuPO editPO(Long id, String name, String remark) {
		CsMenuPO menuPO = menuDao.findOne(id);
		menuPO.setName(name);
		menuPO.setRemark(remark);

		menuDao.save(menuPO);

		return menuPO;
	}

	public CsMenuPO appendPO(Long id, Long channelId, String name) {
		CsMenuPO menuPO = new CsMenuPO();
		CsMenuPO parentPO = menuDao.findOne(id);
		String parentPath = parentPO.getParentPath() + "/" + parentPO.getName();
		menuPO.setName(name);
		menuPO.setParentId(id);
		menuPO.setParentPath(parentPath);
		menuPO.setChannelId(channelId);
		menuPO.setUpdateTime(new Date());

		menuDao.save(menuPO);

		return menuPO;
	}

	public CsMenuPO removePO(Long menuId) throws Exception {
		CsMenuPO menuPO = menuDao.findOne(menuId);

		menuDao.delete(menuPO);
		this.deleteMenuTree(menuId);
		csResourceService.removeResourcesByMenuId(menuId, menuPO.getChannelId());

		return menuPO;
	}

	public void deleteMenuTree(Long menuId) throws Exception {
		List<CsMenuPO> menus = menuDao.findByParentId(menuId);
		deleteChidrenMenu(menus, menuId);
	}

	private void deleteChidrenMenu(List<CsMenuPO> menus, Long menuId) throws Exception {
		if (menus != null && menus.size() > 0) {
			for (CsMenuPO menu : menus) {
				menuDao.delete(menu);
				this.removePO(menu.getId());
				csResourceService.removeResourcesByMenuId(menuId, menu.getChannelId());
			}
		}
	}

	public void removeMenuByChannelId(Long channelId) throws Exception {
		List<CsMenuPO> menuList = menuDao.findByChannelId(channelId);
		if (menuList != null && menuList.size() > 0) {
			for (int i = 0; i < menuList.size(); i++) {
				removePO(menuList.get(i).getId());
			}
		}
	}
}
