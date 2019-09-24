package com.sumavision.tetris.mims.app;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.menu.MenuQuery;
import com.sumavision.tetris.menu.MenuVO;
import com.sumavision.tetris.mims.app.folder.FolderDAO;
import com.sumavision.tetris.mims.app.folder.FolderPO;
import com.sumavision.tetris.mims.app.folder.FolderType;
import com.sumavision.tetris.mims.app.group.ChatQuery;
import com.sumavision.tetris.mvc.constant.HttpConstant;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;

@Controller
@RequestMapping(value = "")
public class WelcomeController {
	
	@Autowired
	private MenuQuery menuQuery;
	
	//@Autowired
	private ChatQuery chatQuery;
	
	@Autowired
	private UserQuery userQuery;
	
	@Autowired
	private FolderDAO folderDao;

	/**
	 * 需要登录后访问<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月5日 下午4:51:11
	 * @param String token 登录后的token
	 */
	@RequestMapping(value = "/index/{token}")
	public ModelAndView index(
			@PathVariable String token, 
			HttpServletRequest request) throws Exception{
		ModelAndView mv = null;
		//初始化一个session
		HttpSession session = request.getSession(false);
		if(session == null){
			session = request.getSession();
			session.setMaxInactiveInterval(HttpConstant.SESSION_TIMEOUT);
		}
		mv = new ModelAndView("web/mims/index");
		mv.addObject(HttpConstant.MODEL_TOKEN, token);
		mv.addObject(HttpConstant.MODEL_SESSION_ID, session.getId());
		return mv;
	}
	
	/**
	 * 页面框架初始化数据查询<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月5日 下午4:51:49
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/prepare/app")
	public Object prepareApp(HttpServletRequest request) throws Exception{
		
		Map<String, Object> appInfo = new HashMap<String, Object>();
		
		//用户信息
		UserVO user = userQuery.current();
		
		//TODO 权限校验
		
		//根目录
		FolderPO folder = folderDao.findMaterialRootByUserId(user.getUuid());
		user.setRootFolderId(folder.getId());
		user.setRootFolderName(folder.getName());
		
		appInfo.put("user", user);
		
		//菜单信息
		List<MenuVO> menus = menuQuery.permissionMenus(user);
		
		appInfo.put("menus", menus);
		
		/*List<GroupVO> groups = chatTool.generateOrganization(user.getGroupId(), user.getUuid());
		appInfo.put("groups", groups);*/
		
		return appInfo;
	}
	
	/**
	 * 重定向到素材库<br/>
	 * <p>
	 * 	解决url变量在其他微服务中获取不到值的问题<br/>
	 * </p>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月26日 上午9:40:54
	 */
	@RequestMapping(value = "/index/material/{token}")
	public void indexMaterial(
			@PathVariable String token,
			HttpServletRequest request, 
			HttpServletResponse response) throws Exception{
		
		UserVO user = userQuery.findByToken(token);
		
		//TODO 权限校验
		
		FolderPO folder = folderDao.findMaterialRootByUserId(user.getUuid());
		
		StringBufferWrapper redirectUrl = new StringBufferWrapper().append("http://")
																   .append(request.getServerName())
																   .append(":")
																   .append(request.getServerPort())
																   .append("/")
																   .append("index/")
																   .append(token);
		if(folder == null){
			redirectUrl.append("#/page-error/403/").append(URLEncoder.encode("数据异常，网盘根目录丢失，请联系管理员！"));
		}else{
			redirectUrl.append("#/page-material/")
					   .append(folder.getId());
		}
		response.sendRedirect(redirectUrl.toString());
	}
	
	/**
	 * 重定向到媒资库<br/>
	 * <p>
	 * 	解决url变量在其他微服务中获取不到值的问题<br/>
	 * </p>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月26日 下午2:54:48
	 * @param @PathVariable String folderType FolderType.prim
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "/index/media/{folderType}/{token}")
	public void indexMedia(
			@PathVariable String token,
			@PathVariable String folderType, 
			HttpServletRequest request, 
			HttpServletResponse response) throws Exception{
		
		UserVO user = userQuery.findByToken(token);
		
		//权限校验
		
		FolderType type = FolderType.fromPrimaryKey(folderType);
		
		FolderPO folder = folderDao.findCompanyRootFolderByType(user.getGroupId(), type.toString());
		
		StringBufferWrapper redirectUrl = new StringBufferWrapper().append("http://")
																   .append(request.getServerName())
																   .append(":")
																   .append(request.getServerPort())
																   .append("/")
																   .append("index/")
																   .append(token);
		if(folder == null){
			redirectUrl.append("#/page-error/403/").append(URLEncoder.encode(new StringBufferWrapper().append("数据异常，").append(type.getName()).append("根目录丢失，请联系管理员！").toString()));
		}else{
			redirectUrl.append("#/page-media-")
					   .append(type.getWebSuffix())
					   .append("/")
					   .append(folder.getId());
		}
		response.sendRedirect(redirectUrl.toString());
	}
	
}
