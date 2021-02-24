package com.sumavision.tetris.menu;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Node;

import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.commons.util.wrapper.HashSetWrapper;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.commons.util.xml.XMLReader;
import com.sumavision.tetris.menu.MenuPO.MenuComparator;
import com.sumavision.tetris.organization.CompanyQuery;
import com.sumavision.tetris.organization.CompanyVO;
import com.sumavision.tetris.spring.eureka.application.EurekaFeign.MemoryQuery;
import com.sumavision.tetris.system.role.SystemRoleQuery;
import com.sumavision.tetris.system.role.SystemRoleVO;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;
/**
 * 菜单工具类<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2018年11月20日 下午1:47:17
 */
@Component
public class MenuQuery {

	@Autowired
	private SystemRoleQuery systemRoleQuery;
	
	@Autowired
	private MenuDAO menuDao;
	
	@Autowired
	private MenuComparator menuComparator;
	
	@Autowired
	private MemoryQuery memoryQuery;
	
	@Autowired
	private SystemRoleMenuPermissionDAO systemRoleMenuPermissionDao;
	
	@Autowired
	private UserQuery userQuery;
	
	@Autowired
	private CompanyQuery companyQuery;
	
	/**
	 * 查询菜单树<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月16日 下午4:01:03
	 * @return List<MenuVO> 菜单树列表
	 */
	public List<MenuVO> listTree() throws Exception{
		List<MenuPO> totalMenus = menuDao.findAll();
		
		//重新排序
		Collections.sort(totalMenus, menuComparator);
		
		List<MenuVO> rootMenus = generateRootMenus(totalMenus);
		packMenuTree(rootMenus, totalMenus);
		return rootMenus;
	}
	
	/**
	 * 查询系统角色绑定的所有菜单<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年2月23日 下午4:44:37
	 * @param Collection<String> systemRoleIds 系统角色id列表
	 * @return List<MenuVO> 菜单树
	 */
	public List<MenuVO> listTreeBySystemRoleIdIn(Collection<String> systemRoleIds) throws Exception{
		List<MenuPO> menuEntities = menuDao.findByRoleIdIn(systemRoleIds);
		Set<Long> parentMenuIds = new HashSetWrapper<Long>().add(-1l).getSet();
		if(menuEntities!=null && menuEntities.size()>0){
			for(MenuPO menuEntity:menuEntities){
				if(menuEntity.getMenuIdPath() != null){
					String[] idArr = menuEntity.getMenuIdPath().split("/");
					for(int i=1; i<idArr.length; i++){
						parentMenuIds.add(Long.valueOf(idArr[i]));
					}
				}
			}
		}
		List<MenuPO> parentMenuEntities = menuDao.findAllById(parentMenuIds);
		List<MenuPO> totalMenuEntities = new ArrayList<MenuPO>();
		if(menuEntities!=null && menuEntities.size()>0) totalMenuEntities.addAll(menuEntities);
		if(parentMenuEntities!=null && parentMenuEntities.size()>0) totalMenuEntities.addAll(parentMenuEntities);
		List<MenuVO> rootMenus = generateRootMenus(totalMenuEntities);
		packMenuTree(rootMenus, totalMenuEntities);
		return rootMenus;
	}
	
	/**
	 * 获取有权限的菜单<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月11日 下午3:44:52
	 * @param UserVO user 用户
	 * @return List<MenuVO> 菜单列表
	 */
	public List<MenuVO> permissionMenus(UserVO user) throws Exception{
		
		List<SystemRoleVO> roles = systemRoleQuery.queryUserRoles(user.getUuid());
		if(roles!=null && roles.size()>0){
			Set<String> roleIds = new HashSet<String>();
			for(SystemRoleVO role:roles){
				roleIds.add(role.getId());
			}
			
			//根据权限获取菜单
			List<MenuPO> menus = menuDao.findByRoleIdIn(roleIds);
			String xmlString = memoryQuery.findAll();
			for (MenuPO menuPO : menus) {
				if(menuPO.getLink() == null) continue;
				String[] applink = menuPO.getLink().split("//");
				if (applink.length < 2) {
					continue;
				}
				String[] applinkString = applink[1].split("/");
				if (applinkString.length<1) {
					continue;
				}
				String appName = applinkString[0];
				
				XMLReader reader = new XMLReader(xmlString);
				List<Node> nodes = reader.readNodeList("applications.application");
				for (Node node : nodes) {
					List<Node> nodesto = reader.readNodeList("application.instance",node);
					String ip = null;
					String port  = null;
					Random r = new Random();
					int i = r.nextInt(nodesto.size());
					Node instanceNode = nodesto.get(i);
					ip = reader.readString("instance.hostName",instanceNode);				
					String name = reader.readString("application.name", node).toLowerCase();
					if(name.equalsIgnoreCase(appName)){
						if(menuPO.getLink().startsWith("http://")){
							port = reader.readString("instance.port",instanceNode);
						}else {
							String instanceId=reader.readString("instance.instanceId",instanceNode);
							String[] securePortStrs=instanceId.split("\\*");
							if(securePortStrs!=null&&securePortStrs.length>0){
								port = securePortStrs[securePortStrs.length-1];
							}
							
						}
						String nodePort = new StringBufferWrapper().append(ip).append(":").append(port).toString();
						menuPO.setLink(menuPO.getLink().replaceAll(appName,nodePort ));
						break;
					}
				}
			} 
			
			List<MenuPO> parentMenus = null;
			if(menus!=null && menus.size()>0) parentMenus = queryParentMenus(menus);
			
			List<MenuPO> totalMenus = new ArrayList<MenuPO>();
			if(menus!=null && menus.size()>0) totalMenus.addAll(menus);
			if(parentMenus!=null && parentMenus.size()>0) totalMenus.addAll(parentMenus);
			
			//重新排序
			Collections.sort(totalMenus, menuComparator);
			
			List<MenuVO> rootMenus = generateRootMenus(totalMenus);
			packMenuTree(rootMenus, totalMenus);
			
			return rootMenus;
		}
		
		return null;
	}
	
	/**
	 * 获取给定菜单列表的所有父级菜单<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年11月20日 下午1:52:07
	 * @param List<MenuPO> menus 菜单列表
	 * @return List<MenuPO> 父级菜单
	 */
	public List<MenuPO> queryParentMenus(List<MenuPO> menus) throws Exception{
		Set<Long> groupMenuIds = new HashSet<Long>();
		for(int i=0; i<menus.size(); i++){
			MenuPO menuPO = menus.get(i);
			String menuIdPath = menuPO.getMenuIdPath();
			if(menuIdPath != null){
				String[] ids = menuIdPath.split(MenuPO.SEPARATOR);
				for(int j=1; j<ids.length; j++){
					groupMenuIds.add(Long.valueOf(ids[j]));
				}
			}
		}
		List<MenuPO> groupMenus = menuDao.findByIdInOrderBySerialAsc(groupMenuIds);
		return groupMenus;
	}
	
	/**
	 * 根据给定的菜单列表获取根菜单，并包装成VO<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年11月20日 下午1:54:31
	 * @param List<MenuPO> menus 菜单列表
	 * @return List<MenuVO> 根菜单
	 */
	public List<MenuVO> generateRootMenus(List<MenuPO> menus) throws Exception{
		List<MenuVO> viewMenus = new ArrayList<MenuVO>();
		for(int i=0; i<menus.size(); i++){
			MenuPO menu = menus.get(i);
			if(menu.getParentId() == null){
				viewMenus.add(new MenuVO().set(menu));
			}
		}
		return viewMenus;
	}
	
	/**
	 * 根据给定的根菜单组装菜单树，并且包装成VO<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年11月20日 下午2:02:38
	 * @param List<MenuVO> rootMenus 根菜单
	 * @param List<MenuPO> totalMenus 全部菜单
	 */
	public void packMenuTree(List<MenuVO> rootMenus, List<MenuPO> totalMenus) throws Exception{
		for(int i=0; i<rootMenus.size(); i++){
			MenuVO rootMenu = rootMenus.get(i);
			for(int j=0; j<totalMenus.size(); j++){
				MenuPO menu = totalMenus.get(j);
				if(menu.getParentId()!=null && menu.getParentId().equals(rootMenu.getId())){
					if(rootMenu.getSub() == null) rootMenu.setSub(new ArrayList<MenuVO>());
					rootMenu.getSub().add(new MenuVO().set(menu));
				}
			}
			if(rootMenu.getSub()!=null && rootMenu.getSub().size()>0){
				packMenuTree(rootMenu.getSub(), totalMenus);
			}
		}
	}
	
	/**
	 * 查询一个菜单下的所有子菜单<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月17日 上午9:34:32
	 * @param Long id 给定菜单id
	 * @return List<MenuPO> 所有的子菜单
	 */
	public List<MenuPO> findAllSubMenus(Long id){
		return menuDao.findAllSubMenus(new StringBufferWrapper().append("%/")
														        .append(id)
														        .toString(), 
									   new StringBufferWrapper().append("%/")
															    .append(id)
															    .append("/%")
															    .toString());
	}
	
	/**
	 * 查询系统角色配置的首页<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年5月12日 下午3:32:25
	 * @param Long roleId 系统角色id
	 * @return MenuVO 菜单
	 */
	public MenuVO queryHomePage(Long roleId) throws Exception{
		List<SystemRoleMenuPermissionPO> permissions = systemRoleMenuPermissionDao.findByRoleIdAndFlag(roleId.toString(), SystemRoleMenuPermissionFlag.HOME_PAGE);
		if(permissions==null || permissions.size()<=0){
			return null;
		}
		SystemRoleMenuPermissionPO permission = permissions.get(0);
		MenuPO menu = menuDao.findById(permission.getMenuId());
		if(menu == null) return null;
		return new MenuVO().set(menu);
	}
	
	/**
	 * 系统功能授权菜单查询<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年2月20日 下午5:05:41
	 * @param Long roleId 系统角色id
	 * @param Long companyId 企业id
	 * @param Boolean handleCompanyQuery 是否查询企业菜单
	 * @return menus List<MenuVO> 菜单树
	 * @return authorized List<Long> 已授权的菜单id列表
	 * @return homePage Long 首页菜单id
	 */
	public Map<String, Object> queryMenusByRoleId(
			Long roleId,
			Long companyId,
			Boolean handleCompanyQuery) throws Exception{
		List<MenuVO> menus = null;
		if(!handleCompanyQuery){
			menus = listTree();
		}else{
			if(companyId == null){
				UserVO user = userQuery.current();
				companyId = Long.valueOf(user.getGroupId());
			}
			CompanyVO company = companyQuery.findById(companyId);
			menus = listTreeBySystemRoleIdIn(new ArrayListWrapper<String>().add(company.getSystemRoleId().toString()).getList());
		}
		
		List<SystemRoleMenuPermissionPO> permissions = systemRoleMenuPermissionDao.findByRoleIdAndIdNotIn(roleId.toString(), new ArrayListWrapper<Long>().add(-1l).getList());
		List<Long> authorized = new ArrayList<Long>();
		List<Long> autogeneration = new ArrayList<Long>();
		Long homePage = null;
		if(permissions!=null && permissions.size()>0){
			for(SystemRoleMenuPermissionPO permission:permissions){
				authorized.add(permission.getMenuId());
				if(permission.isAutoGeneration()) autogeneration.add(permission.getMenuId());
				if(SystemRoleMenuPermissionFlag.HOME_PAGE.equals(permission.getFlag())){
					homePage = permission.getMenuId();
				}
			}
		}
		if(autogeneration.size() > 0){
			for(Long id:autogeneration){
				setDisableNode(menus, id);
			}
		}
		return new HashMapWrapper<String, Object>().put("menus", menus)
												   .put("authorized", authorized)
												   .put("homePage", homePage)
												   .getMap();
	}
	
	/**
	 * 设置不能选中的菜单节点<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年2月23日 下午1:55:17
	 * @param List<MenuVO> nodes 菜单树节点列表
	 * @param Long id 禁用的菜单id
	 * @return boolean 设置结果
	 */
	private boolean setDisableNode(List<MenuVO> nodes, Long id) throws Exception{
		if(nodes==null || nodes.size()<=0) return false;
		for(MenuVO node:nodes){
			if(node.getId().equals(id)){
				node.setDisabled(true);
				return true;
			}else{
				boolean result = setDisableNode(node.getSub(), id);
				if(result) return true;
			}
		}
		return false;
	}
	
}
