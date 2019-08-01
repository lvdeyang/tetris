package com.sumavision.tetris.mims.app;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.sumavision.tetris.commons.util.encoder.MessageEncoder.Sha256Encoder;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
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
		
		Long folderId = folder.getId();
		if(FolderType.COMPANY_PICTURE.equals(type) || FolderType.COMPANY_AUDIO.equals(type)){
			folderId = 0l;
		}
		
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
					   .append(folderId);
		}
		response.sendRedirect(redirectUrl.toString());
	}
	
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/api/test")
	public void apiTest() throws Exception{
		String filePath = "E:\\111.txt";
		InputStream fis = new FileInputStream(new File(filePath));
		byte[] bytes = FileCopyUtils.copyToByteArray(fis);
		System.out.println(bytes.length);
		
		CloseableHttpClient httpclient = HttpClients.createDefault();
		
		String appId = "327a89ed0afd0042880af8b0b98b6218ee64";
		String timestamp = "123";
		String appSecret = "123456";
		
		List<String> resources = new ArrayListWrapper<String>().add(appId)
															   .add(timestamp)
															   .add(appSecret)
															   .getList();
		Collections.sort(resources, new Comparator<String>(){
			@Override
			public int compare(String o1, String o2) {
				return o1.compareTo(o2);
			}
		});
		String unSigned = new StringBufferWrapper().append(resources.get(0))
												   .append(resources.get(1))
												   .append(resources.get(2))
												   .toString();
		
		String sign = new Sha256Encoder().encode(unSigned);
		
		String url = new StringBufferWrapper().append("http://192.165.56.71:8082/tetris-mims/api/server/media/upload?")
											  .append("appId=").append(appId).append("&")
											  .append("timestamp=").append(timestamp).append("&")
											  .append("sign=").append(sign)
											  .toString();
		HttpPost httppost =new HttpPost(url);
		
		MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
		ContentType contentType = ContentType.create(ContentType.TEXT_PLAIN.getMimeType(), Charset.forName("UTF-8"));
		
		
		entityBuilder.addPart("uuid", new StringBody("5c59be34781044499c394de4d30fa756", contentType));
		entityBuilder.addPart("folderType", new StringBody("video", contentType));
		entityBuilder.addPart("name", new StringBody("111.txt", contentType));
		entityBuilder.addPart("blockSize", new StringBody(String.valueOf(12315227), contentType));
		entityBuilder.addPart("lastModified", new StringBody(String.valueOf(1455792902312l), contentType));
		entityBuilder.addPart("size", new StringBody(String.valueOf(12315227), contentType));
		entityBuilder.addPart("type", new StringBody("video/mp4", contentType));
		entityBuilder.addPart("beginOffset", new StringBody("0", contentType));
		entityBuilder.addPart("endOffset", new StringBody(String.valueOf(12315227), contentType));
		entityBuilder.addPart("block", new ByteArrayBody(bytes, "111.txt"));
		entityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
		
		httppost.setEntity(entityBuilder.build());
		CloseableHttpResponse response = httpclient.execute(httppost);
		System.out.println(response);
	}
	
}
