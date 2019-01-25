package com.sumavision.tetris.mims.app;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.sumavision.tetris.menu.MenuQuery;
import com.sumavision.tetris.menu.MenuVO;
import com.sumavision.tetris.mims.app.folder.FolderDAO;
import com.sumavision.tetris.mims.app.folder.FolderPO;
import com.sumavision.tetris.mims.app.group.ChatQuery;
import com.sumavision.tetris.mims.app.group.GroupVO;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.user.UserClassify;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;

@Controller
@RequestMapping(value = "")
public class WelcomeController {
	
	@Autowired
	private MenuQuery menuTool;
	
	//@Autowired
	private ChatQuery chatTool;
	
	@Autowired
	private UserQuery userTool;
	
	@Autowired
	private FolderDAO folderDao;

	@RequestMapping(value = "/index")
	public ModelAndView index() throws Exception{
		ModelAndView mv = null;
		mv = new ModelAndView("web/mims/index");
		return mv;
	}
	
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/prepare/app")
	public Object prepareApp(HttpServletRequest request) throws Exception{
		
		Map<String, Object> appInfo = new HashMap<String, Object>();
		
		//用户信息
		UserVO user = userTool.current();
		
		//TODO 权限校验
		
		//根目录
		FolderPO folder = folderDao.findMaterialRootByUserId(user.getUuid());
		user.setRootFolderId(folder.getId());
		user.setRootFolderName(folder.getName());
		
		appInfo.put("user", user);
		
		//菜单信息
		List<MenuVO> menus = menuTool.permissionMenus(user);
		
		appInfo.put("menus", menus);
		
		/*List<GroupVO> groups = chatTool.generateOrganization(user.getGroupId(), user.getUuid());
		appInfo.put("groups", groups);*/
		
		return appInfo;
	}
	
}
