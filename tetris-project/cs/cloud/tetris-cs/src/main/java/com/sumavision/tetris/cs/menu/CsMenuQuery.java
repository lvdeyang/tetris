package com.sumavision.tetris.cs.menu;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CsMenuQuery {
	@Autowired
	private CsMenuDAO menuDao;
	
	public List<CsMenuVO> queryMenuTree(Long channelId) throws Exception{
		List<CsMenuPO> menus = menuDao.findAll();

		List<CsMenuVO> rootmenus = generateRootMenus(menus,channelId);

		packMenuTree(rootmenus, menus);

		return rootmenus;
	}
	
	private List<CsMenuVO> generateRootMenus(Collection<CsMenuPO> columns,Long channelId) throws Exception {
		if (columns == null || columns.size() <= 0)
			return null;
		List<CsMenuVO> rootcolumns = new ArrayList<CsMenuVO>();
		for (CsMenuPO column : columns) {
			if (column.getParentId() == -1 && column.getChannelId() == channelId) {
				rootcolumns.add(new CsMenuVO().set(column));
			}
		}
		return rootcolumns;
	}
	
	public void packMenuTree(List<CsMenuVO> rootcolumns, List<CsMenuPO> totalcolumns) throws Exception {
		if (rootcolumns == null || rootcolumns.size() <= 0)
			return;
		for (int i = 0; i < rootcolumns.size(); i++) {
			CsMenuVO rootcolumn = rootcolumns.get(i);
			for (int j = 0; j < totalcolumns.size(); j++) {
				CsMenuPO column = totalcolumns.get(j);
				if (column.getParentId() != null && column.getParentId() == rootcolumn.getId()) {
					if (rootcolumn.getSubColumns() == null)
						rootcolumn.setSubColumns(new ArrayList<CsMenuVO>());
					rootcolumn.getSubColumns().add(new CsMenuVO().set(column));
				}
			}
			if (rootcolumn.getSubColumns() != null && rootcolumn.getSubColumns().size() > 0) {
				packMenuTree(rootcolumn.getSubColumns(), totalcolumns);
			}
		}
	}
	
	public CsMenuVO getMenuByMenuId(Long menuId) throws Exception{
		CsMenuPO menu = menuDao.findOne(menuId);
		return new CsMenuVO().set(menu);
	}
}
