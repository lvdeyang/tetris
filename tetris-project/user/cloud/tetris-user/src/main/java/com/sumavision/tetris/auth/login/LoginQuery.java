package com.sumavision.tetris.auth.login;

import java.io.InputStream;
import java.util.List;
import java.util.Properties;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Node;

import com.sumavision.tetris.auth.token.TokenDAO;
import com.sumavision.tetris.auth.token.TokenPO;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.commons.util.xml.XMLReader;
import com.sumavision.tetris.config.server.ServerProps;
import com.sumavision.tetris.config.server.UserServerPropsQuery;
/*import com.sumavision.tetris.menu.MenuPO;*/
import com.sumavision.tetris.menu.MenuQuery;
import com.sumavision.tetris.menu.MenuVO;
import com.sumavision.tetris.mims.config.server.MimsServerPropsQuery;
import com.sumavision.tetris.spring.eureka.application.EurekaFeign.MemoryQuery;
import com.sumavision.tetris.system.role.SystemRoleType;
import com.sumavision.tetris.system.role.UserSystemRolePermissionDAO;
import com.sumavision.tetris.system.role.UserSystemRolePermissionPO;
import com.sumavision.tetris.user.UserDAO;
import com.sumavision.tetris.user.UserPO;

@Component
public class LoginQuery {

	@Autowired
	private UserDAO userDao;
	
	@Autowired
	private TokenDAO tokenDao;
	
	@Autowired
	private UserServerPropsQuery userServerPropsQuery;
	
	@Autowired
	private MimsServerPropsQuery mimsServerPropsQuery;
	
	@Autowired
	private MenuQuery menuQuery;
	
	@Autowired
	private UserSystemRolePermissionDAO userSystemRolePermissionDao;
	
	@Autowired
	private MemoryQuery memoryQuery;
	
	/**
	 * 登录成功后重定向的页面<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月7日 下午4:19:00
	 * @param String token 登录token
	 * @return String 重定向的url
	 */
	public String queryRedirectUrl(String token) throws Exception{
		TokenPO tokenEntity = tokenDao.findByToken(token);
		UserPO user = userDao.findOne(tokenEntity.getUserId());
		String redirectUrl = null;
		MenuVO menu = null;
		List<UserSystemRolePermissionPO> permissions = userSystemRolePermissionDao.findByUserIdAndRoleType(user.getId(), SystemRoleType.SYSTEM);
		if(permissions!=null && permissions.size()>0){
			UserSystemRolePermissionPO permission = permissions.get(0);
			menu = menuQuery.queryHomePage(permission.getRoleId());
		}
		if(menu == null){
			ServerProps props = userServerPropsQuery.queryProps();
			Properties properties = new Properties();
			InputStream in = null;
			try{
				in = LoginQuery.class.getClassLoader().getResourceAsStream("serverIp.properties");
				properties.load(in);
				String ip = properties.getProperty("ip");
				redirectUrl = new StringBufferWrapper().append("http://").append(ip).append(":").append(props.getPort()).append("/index/").append(token).append("#/page-home").toString();
				/*if(UserClassify.INTERNAL.equals(user.getClassify())){
					ServerProps props = userServerPropsQuery.queryProps();
					redirectUrl = new StringBufferWrapper().append("http://").append(props.getIp()).append(":").append(props.getPort()).append("/index/").append(token).append("#/page-user").toString();
				}else if(UserClassify.COMPANY.equals(user.getClassify())){
					ServerProps props = userServerPropsQuery.queryProps();
					redirectUrl = new StringBufferWrapper().append("http://").append(props.getIp()).append(":").append(props.getPort()).append("/index/").append(token).append("#/page-business-user").toString();
					//com.sumavision.tetris.mims.config.server.ServerProps props = mimsServerPropsQuery.queryProps();
					//redirectUrl = new StringBufferWrapper().append("http://").append(props.getFtpIp()).append(":").append(props.getPort()).append("/index/material/").append(token).toString();
				}else if(UserClassify.NORMAL.equals(user.getClassify())){
					com.sumavision.tetris.mims.config.server.ServerProps props = mimsServerPropsQuery.queryProps();
					redirectUrl = new StringBufferWrapper().append("http://").append(props.getFtpIp()).append(":").append(props.getPort()).append("/index/media/picture/").append(token).toString();
				}*/
			}finally{
				if(in != null) in.close();
			}
		}else{
			String xmlString = memoryQuery.findAll();
			XMLReader reader = new XMLReader(xmlString);
			List<Node> nodes = reader.readNodeList("applications.application");
			String trueName = menu.getLink().split("//")[1].split("/")[0].toLowerCase().toString();
			boolean replaced = false;
			for (Node node : nodes) {
				List<Node> nodesto = reader.readNodeList("application.instance",node);
				Random r = new Random();
				int i = r.nextInt(nodesto.size());
				Node instanceNode = nodesto.get(i);
				String xmlIp = reader.readString("instance.hostName",node);
				String port = reader.readString("instance.port",instanceNode);
				String nodePort = new StringBufferWrapper().append(xmlIp).append(":").append(port).toString();
				String name = reader.readString("application.name", node).toLowerCase();	
				if(trueName.equals(name)){
					redirectUrl = menu.getLink().replace(trueName, nodePort);
					redirectUrl = redirectUrl.replace("{context:token}", token)
											 .replace("{context:user;prop:token}", token);
					replaced = true;
					break;
				}
			}
			if(!replaced){
				redirectUrl = menu.getLink();			
				redirectUrl = redirectUrl.replace("{context:token}", token)
						   				 .replace("{context:user;prop:token}", token);
			}
		}	
		return redirectUrl;
	}
	
}
