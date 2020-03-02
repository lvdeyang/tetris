package com.sumavision.tetris.cs.menu;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sumavision.tetris.mims.app.media.avideo.MediaAVideoVO;

@Service
@Transactional(rollbackFor = Exception.class)
public class CsMenuService {
	@Autowired
	private CsMenuDAO menuDao;

	@Autowired
	private CsMenuQuery menuQuery;

	@Autowired
	private CsResourceService csResourceService;

	/**
	 * 添加cs媒资根目录<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:06:57
	 * @param Long channelId 频道id
	 * @param String name 根目录名称
	 * @return CsMenuVO cs媒资目录
	 */
	public CsMenuPO addRoot(Long channelId, String name) {
		if (name.equals("yjgbPush")) {
			CsMenuPO menu = menuDao.findByChannelIdAndParentIdAndName(channelId, -1l, name);
			if (menu != null) return menu;
		}
		CsMenuPO menuPO = new CsMenuPO();
		menuPO.setName(name);
		menuPO.setUpdateTime(new Date());
		menuPO.setChannelId(channelId);
		menuPO.setParentId(-1l);
		menuPO.setParentPath(".");

		menuDao.save(menuPO);

		return menuPO;
	}
	
	/**
	 * 自动添加根目录和目录下资源<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月28日 下午3:39:33
	 * @param Long channelId 频道id
	 * @param String name 目录名称
	 * @param List<MediaAVideoVO> medias 媒资列表
	 */
	public CsMenuPO autoAddMenuAndSource(Long channelId, String name, List<MediaAVideoVO> medias) throws Exception {
		CsMenuPO menuPO = menuDao.findByChannelIdAndParentIdAndName(channelId, -1l, name);
		if (menuPO == null) menuPO = addRoot(channelId, name);
		
		csResourceService.addResources(medias, menuPO.getId(), channelId);
		return null;
	}

	/**
	 * 上移一层cs媒资目录<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:06:57
	 * @param Long id 目录id
	 * @param Long newParentId 新父目录id
	 * @return CsMenuVO cs媒资目录
	 */
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

	/**
	 * 编辑cs媒资目录<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:06:57
	 * @param Long id 目录id
	 * @param String name 目录新名称
	 * @param String remark 目录新备注
	 * @return CsMenuPO cs媒资目录
	 */
	public CsMenuPO editPO(Long id, String name, String remark) {
		CsMenuPO menuPO = menuDao.findOne(id);
		menuPO.setName(name);
		menuPO.setRemark(remark);

		menuDao.save(menuPO);

		return menuPO;
	}

	/**
	 * 添加cs下属媒资目录<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:06:57
	 * @param Long id 父目录id
	 * @param Long channelId 频道id
	 * @param String name 新添加的标签名
	 * @return CsMenuPO 添加的cs媒资目录
	 */
	public CsMenuPO appendPO(Long id, Long channelId, String name) {
		CsMenuPO parentPO = menuDao.findOne(id);
		String parentPath = parentPO.getParentPath() + "/" + parentPO.getName();
		
		CsMenuPO menuPO = new CsMenuPO();
		menuPO.setName(name);
		menuPO.setParentId(id);
		menuPO.setParentPath(parentPath);
		menuPO.setChannelId(channelId);
		menuPO.setUpdateTime(new Date());

		menuDao.save(menuPO);

		return menuPO;
	}

	/**
	 * 删除cs媒资目录(包括目录下的媒资)<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:06:57
	 * @param Long id 目录id
	 * @return CsMenuPO cs媒资目录
	 */
	public CsMenuPO removePO(Long menuId) throws Exception {
		CsMenuPO menuPO = menuDao.findOne(menuId);

		if(menuPO != null){
			menuDao.delete(menuPO);
			csResourceService.removeResourcesByMenuId(menuId, menuPO.getChannelId());
			this.deleteMenuTree(menuId);
		}else {
			this.deleteMenuTree(menuId);
		}

		return menuPO;
	}

	/**
	 * 删除cs媒资目录树<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:06:57
	 * @param Long menuId 目录id
	 */
	public void deleteMenuTree(Long menuId) throws Exception {
		
		List<CsMenuPO> menus = menuDao.findByParentId(menuId);
		
		if (menus == null || menus.isEmpty()) return;
		
		for (CsMenuPO menu : menus) {
			this.removePO(menu.getId());
		}
	}

	/**
	 * 根据频道id删除cs媒资目录树<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:06:57
	 * @param Long channelId 频道id
	 */
	public void removeMenuByChannelId(Long channelId) throws Exception {
		List<CsMenuPO> menuList = menuDao.findByChannelIdAndParentId(channelId, -1l);
		
		if (menuList == null || menuList.isEmpty()) return;
		
		for (CsMenuPO csMenuPO : menuList) {
			removePO(csMenuPO.getId());
		}
	}
}
