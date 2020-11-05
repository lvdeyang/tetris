package com.sumavision.tetris.cs.menu;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CsMenuQuery {
	@Autowired
	private CsMenuDAO menuDao;
	
	@Autowired
	private CsResourceQuery resourceQuery;
	
	/**
	 * 获取cs媒资目录树<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:06:57
	 * @param Long channelId 频道id
	 * @return List<CsMenuVO> cs媒资目录树
	 */
	public List<CsMenuVO> queryMenuTree(Long channelId) throws Exception{
		List<CsMenuPO> menus = menuDao.findByChannelId(channelId);

		List<CsMenuVO> rootMenus = generateRootMenus(menus);

		packMenuTree(rootMenus, menus);
		
		return rootMenus;
	}
	
	/**
	 * 获取cs媒资目录树(带媒资)<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:06:57
	 * @param Long channelId 频道id
	 * @return List<CsMenuVO> cs媒资目录树
	 */
	public List<MenuResourceTreeNodeVO> queryMenuTreeWithResource(Long channelId) throws Exception {
		List<CsMenuPO> menus = menuDao.findByChannelId(channelId);

		List<CsMenuVO> rootMenus = generateRootMenus(menus);
		
		List<MenuResourceTreeNodeVO> rootColumnNodes = MenuResourceTreeNodeVO.setAllMenu(rootMenus);
		List<CsMenuVO> menuVOs = CsMenuVO.getConverter(CsMenuVO.class).convert(menus, CsMenuVO.class);
		List<MenuResourceTreeNodeVO> totleColumnNodes = MenuResourceTreeNodeVO.setAllMenu(menuVOs);

		packMenuTreeNode(rootColumnNodes, totleColumnNodes);
		
		return rootColumnNodes;
	}
	
	/**
	 * 根据频道id获取cs媒资根目录<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:06:57
	 * @param List<CsMenuPO> columns 频道所有cs目录
	 * @return List<CsMenuVO> cs媒资根目录
	 */
	private List<CsMenuVO> generateRootMenus(List<CsMenuPO> columns) throws Exception {
		if (columns == null || columns.size() <= 0)
			return null;
		List<CsMenuVO> rootcolumns = new ArrayList<CsMenuVO>();
		for (CsMenuPO column : columns) {
			if (column.getParentId() == -1) {
				rootcolumns.add(new CsMenuVO().set(column));
			}
		}
		return rootcolumns;
	}
	
	/**
	 * 递归获取树结构<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:06:57
	 * @param List<CsMenuVO> rootColumns 根目录列表
	 * @param List<CsMenuPO> totalColumns 所有目录数据
	 * @return List<CsMenuVO> cs媒资根目录
	 */
	public void packMenuTree(List<CsMenuVO> rootColumns, List<CsMenuPO> totalColumns) throws Exception {
		if (rootColumns == null || rootColumns.size() <= 0)
			return;
		for (int i = 0; i < rootColumns.size(); i++) {
			CsMenuVO rootcolumn = rootColumns.get(i);
			if (rootcolumn.getSubColumns() == null) rootcolumn.setSubColumns(new ArrayList<CsMenuVO>());
			List<CsMenuPO> pickColumn = new ArrayList<CsMenuPO>();
			for (CsMenuPO column : totalColumns) {
				if (column.getParentId() != null && column.getParentId() == rootcolumn.getId()) {
					pickColumn.add(column);
					rootcolumn.getSubColumns().add(new CsMenuVO().set(column));
				}
			}
			totalColumns.removeAll(pickColumn);
			if (!rootcolumn.getSubColumns().isEmpty()) {
				packMenuTree(rootcolumn.getSubColumns(), totalColumns);
			}
		}
	}
	
	/**
	 * 递归获取树结构(封装格式)<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:06:57
	 * @param List<CsMenuVO> rootColumns 根目录列表
	 * @param List<CsMenuPO> totalColumns 所有目录数据
	 * @return List<CsMenuVO> cs媒资根目录
	 */
	public void packMenuTreeNode(List<MenuResourceTreeNodeVO> rootColumns, List<MenuResourceTreeNodeVO> totalColumns) throws Exception {
		if (rootColumns == null || rootColumns.size() <= 0)
			return;
		for (int i = 0; i < rootColumns.size(); i++) {
			MenuResourceTreeNodeVO rootcolumn = rootColumns.get(i);
			if (rootcolumn.getSubColumns() == null) rootcolumn.setSubColumns(new ArrayList<MenuResourceTreeNodeVO>());
			List<MenuResourceTreeNodeVO> pickColumn = new ArrayList<MenuResourceTreeNodeVO>();
			for (MenuResourceTreeNodeVO column : totalColumns) {
				if (column.getParentId() != null && column.getParentId() == rootcolumn.getId()) {
					pickColumn.add(column);
					rootcolumn.getSubColumns().add(column);
				}
			}
			totalColumns.removeAll(pickColumn);
			if (rootcolumn.getSubColumns().isEmpty()) {
				List<CsResourceVO> resources = resourceQuery.queryMenuResources(rootcolumn.getId());
				List<MenuResourceTreeNodeVO> resourceNodes = MenuResourceTreeNodeVO.setAllResource(resources);
				rootcolumn.setSubColumns(resourceNodes);
			} else {
				packMenuTreeNode(rootcolumn.getSubColumns(), totalColumns);
			}
		}
	}
	
	/**
	 * 获取cs媒资目录<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:06:57
	 * @param Long menuId 目录id
	 * @return CsMenuVO cs媒资
	 */
	public CsMenuVO getMenuByMenuId(Long menuId) throws Exception{
		CsMenuPO menu = menuDao.findOne(menuId);
		return new CsMenuVO().set(menu);
	}
}
