package com.suma.venus.resource.task;

import org.apache.catalina.connector.Connector;
import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.RequestFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.suma.venus.resource.dao.FolderDao;
import com.suma.venus.resource.dao.SerNodeDao;
import com.suma.venus.resource.feign.UserQueryFeign;
import com.suma.venus.resource.util.DepartSyncLdapUtils;
import com.suma.venus.resource.util.EquipSyncLdapUtils;
import com.sumavision.tetris.auth.login.LoginService;
import com.sumavision.tetris.mvc.constant.HttpConstant;
import com.sumavision.tetris.mvc.wrapper.CopyHeaderHttpServletRequestWrapper;

@Component
public class ResourceSyncLdapTask {

	private static final Logger LOGGER = LoggerFactory.getLogger(ResourceSyncLdapTask.class);

	private final long delay = 10 * 60 * 1000;
	
	@Autowired
	EquipSyncLdapUtils equipSyncLdapUtils;
	
	@Autowired
	UserQueryFeign userFeign;
	
	@Autowired
	SerNodeDao serNodeDao;
	
	@Autowired
	FolderDao folderDao;
	
	@Autowired
	DepartSyncLdapUtils departSyncLdapUtils;
	
	@Autowired
	LoginService loginService;
	
	//@Scheduled(fixedDelay = delay)
	public void sync(){
		
		try {
			/*SerNodePO bvcSerNodePO = serNodeDao.findTopBySourceType(SOURCE_TYPE.SYSTEM);
			SerNodePO externalSerNodePO = serNodeDao.findTopBySourceType(SOURCE_TYPE.EXTERNAL);
			if(null == bvcSerNodePO || null == externalSerNodePO || !externalSerNodePO.getNodeUuid().equals(bvcSerNodePO.getNodeFather())){
				//如果bvc和外部系统的双方node还没有建立起父子关系，退出，不能往下进行同步
				LOGGER.info("ResourceSyncLdapTask， serNode don't meet all the conditions. return");
				return ;
			}
			
			List<FolderPO> bvcRootFolders = folderDao.findBvcRootFolders();
			FolderPO bvcParentfolderPO = folderDao.findOne(bvcRootFolders.get(0).getParentId());
			List<FolderPO> externalFolders = folderDao.findBySourceType(SOURCE_TYPE.EXTERNAL);
			if(null == bvcParentfolderPO || !externalFolders.contains(bvcParentfolderPO)){
				//如果bvc和外部系统的组织机构还没有建立起父子关系，退出，不能往下进行同步
				LOGGER.info("ResourceSyncLdapTask， folder don't meet all the conditions. return");
				return ;
			}*/
			
			//构造request,user里面要用
			Long userId = 2l;			
			String token = loginService.doUserIdLogin(userId);
			
			org.apache.coyote.Request _coyoteRequest = new org.apache.coyote.Request();
			Request _request = new Request(new Connector());
			_request.setCoyoteRequest(_coyoteRequest);
			RequestFacade _requFacade = new RequestFacade(_request);
			
			final CopyHeaderHttpServletRequestWrapper request = new CopyHeaderHttpServletRequestWrapper(_requFacade);
			request.addHeader(HttpConstant.HEADER_AUTH_TOKEN, token);
			RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
			
			LOGGER.info("+++++++++++++Resource sync ldap timer starts++++++++++++++++++++");
			
			//从ldap下载设备数据
			equipSyncLdapUtils.handleSyncFromLdap();
			
			//向ldap上传设备信息
			equipSyncLdapUtils.handleSyncToLdap("false");
			
			//从ldap下载用户数据
			departSyncLdapUtils.handleFolderUserSyncFromLdap();
			
			//向ldap上传设备信息
			departSyncLdapUtils.handleFolderUserSyncToLdap();
			
		} catch (Exception e) {
			LOGGER.error("" , e);
			e.printStackTrace();
		}
	}
}
