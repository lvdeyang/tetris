package com.sumavision.tetris.omms.software.service.deployment;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.commons.context.SpringContext;
import com.sumavision.tetris.commons.util.file.FileUtil;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.mvc.listener.ServletContextListener.Path;
import com.sumavision.tetris.omms.hardware.database.DatabaseDAO;
import com.sumavision.tetris.omms.hardware.database.DatabasePO;
import com.sumavision.tetris.omms.hardware.server.ServerDAO;
import com.sumavision.tetris.omms.hardware.server.ServerPO;
import com.sumavision.tetris.omms.software.service.deployment.exception.FtpChangeFolderFailException;
import com.sumavision.tetris.omms.software.service.deployment.exception.FtpCreateFolderFailException;
import com.sumavision.tetris.omms.software.service.deployment.exception.FtpLoginFailWhenUploadInstallationPackageException;
import com.sumavision.tetris.omms.software.service.deployment.exception.HttpGadgetBackupException;
import com.sumavision.tetris.omms.software.service.deployment.exception.HttpGadgetDisableFtpException;
import com.sumavision.tetris.omms.software.service.deployment.exception.HttpGadgetEnableFtpException;
import com.sumavision.tetris.omms.software.service.deployment.exception.HttpGadgetInstallException;
import com.sumavision.tetris.omms.software.service.deployment.exception.HttpGadgetRestartProcessException;
import com.sumavision.tetris.omms.software.service.deployment.exception.HttpGadgetRestoreException;
import com.sumavision.tetris.omms.software.service.deployment.exception.HttpGadgetUninstallException;
import com.sumavision.tetris.omms.software.service.deployment.exception.HttpGadgetUnzipException;
import com.sumavision.tetris.omms.software.service.deployment.exception.HttpGadgetUpdateException;
import com.sumavision.tetris.omms.software.service.installation.BackupInformationDAO;
import com.sumavision.tetris.omms.software.service.installation.BackupInformationPO;
import com.sumavision.tetris.omms.software.service.installation.BackupInformationVO;
import com.sumavision.tetris.omms.software.service.installation.InstallationPackageDAO;
import com.sumavision.tetris.omms.software.service.installation.InstallationPackagePO;
import com.sumavision.tetris.omms.software.service.installation.ProcessDAO;
import com.sumavision.tetris.omms.software.service.installation.ProcessPO;
import com.sumavision.tetris.omms.software.service.type.ServiceTypeDAO;
import com.sumavision.tetris.omms.software.service.type.ServiceTypePO;

@Service
public class ServiceDeploymentService {

	public static final String RELATIVE_FOLDER = "install";
	
	@Autowired
	private ServerDAO serverDao;
	
	@Autowired
	private InstallationPackageDAO installationPackageDao;
	
	@Autowired
	private ServiceTypeDAO serviceTypeDao;
	
	@Autowired
	private ServiceDeploymentDAO serviceDeploymentDao;
	
	@Autowired
	private ProcessDAO processDAO;
	
	@Autowired
	private ProcessDeploymentDAO processDeploymentDAO;
	
	@Autowired
	private BackupInformationDAO backupInformationDAO;
	
	@Autowired
	private DatabaseDAO databaseDAO;
	
	/**
	 * 上传安装包到服务器<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年9月1日 上午10:08:16
	 * @param Long serverId 目标服务器id
	 * @param Long installationPackageId 安装包id
	 * @return ServiceDeploymentVO 部署信息
	 */
	@Transactional(rollbackFor = Exception.class)
	public ServiceDeploymentVO upload(
			Long serverId,
			Long installationPackageId) throws Exception{
		
		ServerPO serverEntity = serverDao.findOne(serverId);
		InstallationPackagePO installationPackageEntity = installationPackageDao.findOne(installationPackageId);
		ServiceTypePO serviceTypeEntity = serviceTypeDao.findOne(installationPackageEntity.getServiceTypeId());
		
		ServiceDeploymentPO serviceDeploymentPO = serviceDeploymentDao.findByServerIdAndServiceTypeId(serverId, installationPackageEntity.getServiceTypeId());
		
		ServiceDeploymentPO serviceDeploymentEntity = new ServiceDeploymentPO();
		
		if(serviceDeploymentPO != null && !serviceDeploymentPO.equals(" ")){
			serviceDeploymentEntity = serviceDeploymentPO;
			serviceDeploymentEntity.setStep(DeploymentStep.UPLOAD);
			serviceDeploymentEntity.setProgress(0);
			
		}else{
			Date now = new Date();
			serviceDeploymentEntity.setUpdateTime(now);
			serviceDeploymentEntity.setServiceTypeId(installationPackageEntity.getServiceTypeId());
			serviceDeploymentEntity.setInstallationPackageId(installationPackageEntity.getId());
			serviceDeploymentEntity.setInstallFullPath(new StringBufferWrapper().append(RELATIVE_FOLDER).append("/").append(installationPackageEntity.getFileName()).toString());
			serviceDeploymentEntity.setServerId(serverEntity.getId());
			serviceDeploymentEntity.setStep(DeploymentStep.UPLOAD);
			serviceDeploymentEntity.setProgress(0);
			serviceDeploymentEntity.setCreateTime(now);
			serviceDeploymentDao.save(serviceDeploymentEntity);
		}		
		
		enableFtp(serviceDeploymentEntity.getId());
		
		Thread uploadThread = new Thread(new Uploader(serverEntity, installationPackageEntity, serviceDeploymentEntity));
		uploadThread.start();
		
//		disableFtp(serviceDeploymentEntity.getId());
		
		return new ServiceDeploymentVO().set(serviceDeploymentEntity)
										.setVersion(installationPackageEntity.getVersion())
										.setName(serviceTypeEntity.getName());
	}
	
	/**
	 * 安装包上传ftp失败<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年9月2日 下午6:58:16
	 * @param ServiceDeploymentPO deployment entity
	 * @param String errorMessage 失败信息
	 */
	@Transactional(rollbackFor = Exception.class)
	public void uploadFail(ServiceDeploymentPO deployment, String errorMessage) {
		deployment.setError(true);
		deployment.setErrorMessage(errorMessage);
		serviceDeploymentDao.save(deployment);
	}
	
	/**
	 * 安装包上传进度<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年9月2日 下午7:00:44
	 * @param ServiceDeploymentPO deployment entity
	 * @param long total 安装包总大小
	 * @param long upSize 安装包上传大小
	 * @param Date uploadBegin 上传开始时间
	 */
	@Transactional(rollbackFor = Exception.class)
	public void uploadProgress(ServiceDeploymentPO deployment, long total, long upSize, Date uploadBegin) {
		if(upSize == total){
			deployment.setProgress(100);
			deployment.setStep(DeploymentStep.CONFIG);
			serviceDeploymentDao.save(deployment);
		}else{
			if(upSize % (1024*1024*50) == 0){
				//50M一更新
				deployment.setProgress((int)(upSize*100/total));
				serviceDeploymentDao.save(deployment);
			}
		}
	}
	
	/**
	 * 安装包上传ftp，带上传进度<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年9月2日 下午7:01:34
	 */
	public static class Uploader implements Runnable{

		private ServerPO server;
		
		private InstallationPackagePO installationPackage;
		
		private ServiceDeploymentPO deployment;
		
		private FTPClient ftpClient;
		
		private Date uploadBegin;
		
		public Uploader(
				ServerPO server, 
				InstallationPackagePO installationPackage, 
				ServiceDeploymentPO deployment) throws Exception{
			this.server = server;
			this.installationPackage = installationPackage;
			this.deployment = deployment;
			this.uploadBegin = new Date();
			initFtpClient();
		}
		
		/**
		 * 初始化FTPClient<br/>
		 * <b>作者:</b>lvdeyang<br/>
		 * <b>版本：</b>1.0<br/>
		 * <b>日期：</b>2020年9月2日 下午3:08:13
		 */
		private void initFtpClient() throws Exception{
			ftpClient = new FTPClient();
			ftpClient.connect(server.getIp(), Integer.parseInt(server.getFtpPort()));
			boolean loginResult = ftpClient.login(server.getFtpUsername(), server.getFtpPassword());
			if(!loginResult){
				throw new FtpLoginFailWhenUploadInstallationPackageException(
						server.getIp(), 
						server.getFtpPort(), 
						server.getFtpUsername(), 
						server.getFtpPassword());
			}
			ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
			//ftpClient.enterLocalPassiveMode();
			ftpClient.enterLocalActiveMode();
			ftpClient.setFileTransferMode(FTP.STREAM_TRANSFER_MODE);
			ftpClient.setControlEncoding("utf-8");
			ftpClient.setBufferSize(1024*1024*10);
			try{
				boolean changeResult = ftpClient.changeWorkingDirectory(encodeFtpText(RELATIVE_FOLDER));
				if(!changeResult){
					boolean mdResult = ftpClient.makeDirectory(encodeFtpText(RELATIVE_FOLDER));
					if(!mdResult){
						throw new FtpCreateFolderFailException(server.getIp(), server.getFtpPort(), RELATIVE_FOLDER);
					}
					changeResult = ftpClient.changeWorkingDirectory(encodeFtpText(RELATIVE_FOLDER));
					if(!changeResult){
						throw new FtpChangeFolderFailException(server.getIp(), server.getFtpPort(), RELATIVE_FOLDER);
					}
				}
			}catch(Exception e){
				ftpClient.logout();
				ftpClient.disconnect();
				uploadFail(e);
				e.printStackTrace();
			}
		}
		
		/**
		 * ftp中文编码iso-8859-1<br/>
		 * <b>作者:</b>lvdeyang<br/>
		 * <b>版本：</b>1.0<br/>
		 * <b>日期：</b>2020年9月2日 下午3:06:54
		 * @param text 编码前文本
		 * @return String 编码后文本 iso-8859-1
		 */
		private String encodeFtpText(String text) throws Exception{
			return new String(text.getBytes("utf-8"), "iso-8859-1");
		}
		
		/**
		 * 上传进度<br/>
		 * <b>作者:</b>lvdeyang<br/>
		 * <b>版本：</b>1.0<br/>
		 * <b>日期：</b>2020年9月2日 下午5:21:25
		 * @param long total 文件总大小
		 * @param long upSize 已上传文件大小
		 */
		private void progress(long total, long upSize) {
			ServiceDeploymentService service = SpringContext.getBean(ServiceDeploymentService.class);
			service.uploadProgress(deployment, total, upSize, uploadBegin);
		}
		
		/**
		 * 上传失败<br/>
		 * <b>作者:</b>lvdeyang<br/>
		 * <b>版本：</b>1.0<br/>
		 * <b>日期：</b>2020年9月2日 下午6:36:48
		 */
		private void uploadFail(Exception e) {
			ServiceDeploymentService service = SpringContext.getBean(ServiceDeploymentService.class);
			service.uploadFail(deployment, e.getMessage());
		}
		
		@Override
		public void run() {
			FileInputStream fIn = null;
			BufferedInputStream in = null;
			OutputStream out = null;
			try{
				Path path = SpringContext.getBean(Path.class);
				File file = new File(new StringBufferWrapper().append(path.webappPath()).append(installationPackage.getFilePath()).toString());
				long total = file.length();
				int lastBuffSize = 0;
				long buffLoopTimes = 0;
				int buffSize = ftpClient.getBufferSize();
				long upSize = 0;
				if(buffSize >= total){
					lastBuffSize = (int)total;
				}else{
					buffLoopTimes = (long)(total/buffSize);
					lastBuffSize = (int)(total - buffSize * buffLoopTimes);
				}
				byte[] buff = new byte[buffSize];
				byte[] lastBuff = new byte[lastBuffSize];
				
				fIn = new FileInputStream(file);
				in = new BufferedInputStream(fIn);
				out = ftpClient.storeFileStream(new String(installationPackage.getFileName().getBytes("utf-8"), "iso-8859-1"));
				for(int i=0; i<buffLoopTimes; i++){
					in.read(buff);
					out.write(buff);
					upSize += buffSize;
					progress(total, upSize);
				}
				if(lastBuffSize > 0){
					in.read(lastBuff);
					out.write(lastBuff);
					upSize += lastBuffSize;
					progress(total, upSize);
				}
			}catch(Exception e){
				uploadFail(e);
				e.printStackTrace();
			}finally{
				try{if(fIn != null) fIn.close();}catch(Exception e){e.printStackTrace();}
				try{if(in != null) in.close();}catch(Exception e){e.printStackTrace();}
				try{if(out != null) out.close();}catch(Exception e){e.printStackTrace();}
				try{if(ftpClient != null){ftpClient.logout(); ftpClient.disconnect();}}catch(Exception e){e.printStackTrace();}
			}
		}
		
	}
	
	/**
	 * 执行安装操作<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年9月3日 下午8:53:07
	 * @param Long deploymentId 部署id
	 * @param JSONString config config.ini json形式
	 */
	@Transactional(rollbackFor = Exception.class)
	public void install(Long deploymentId, String config) throws Exception{
		CloseableHttpClient client = null;
		try{
			ServiceDeploymentPO deployment = serviceDeploymentDao.findOne(deploymentId);
			deployment.setStatus(ServiceDeploymentStatus.INSTALLED);
			deployment.setConfig(config);
			serviceDeploymentDao.save(deployment);
			ServerPO server = serverDao.findOne(deployment.getServerId());
			
			InstallationPackagePO installationPackage = installationPackageDao.findOne(deployment.getInstallationPackageId());
			List<ProcessPO> list = processDAO.findByInstallationPackageId(installationPackage.getId());
			List<ProcessDeploymentPO> list2 = new ArrayList<ProcessDeploymentPO>();
			for (ProcessPO processPO : list) {
				ProcessDeploymentPO processDeployment = new ProcessDeploymentPO();
				processDeployment.setId(processPO.getId());
				processDeployment.setProcessId(processPO.getProcessId());
				processDeployment.setProcessName(processPO.getProcessName());
				processDeployment.setServiceDeploymentId(deploymentId);
				processDeployment.setServerId(server.getId());
				processDeployment.setDb(processPO.getDb());
				list2.add(processDeployment);
			}
			processDeploymentDAO.save(list2);
			
			StringBufferWrapper configBuffer = new StringBufferWrapper();
			if(config!=null && !"".equals(config)){
				JSONObject configJson = JSON.parseObject(config);
				Set<String> keys = configJson.keySet();
				for(String key:keys){
					String value = configJson.getString(key);
					configBuffer.append(key).append("=").append(value).append("\n");
				}
			}
			
			CredentialsProvider credsProvider = new BasicCredentialsProvider();
			AuthScope authScope = new AuthScope(server.getIp(), Integer.parseInt(server.getGadgetPort()), "example.com", AuthScope.ANY_SCHEME);
	        credsProvider.setCredentials(authScope, new UsernamePasswordCredentials(server.getGadgetUsername(), server.getGadgetPassword()));
	        client = HttpClients.custom()
			        		    .setDefaultCredentialsProvider(credsProvider)
			        		    .setRetryHandler(new DefaultHttpRequestRetryHandler(1, true))
			        		    .build();

	        String url = new StringBufferWrapper().append("http://").append(server.getIp()).append(":").append(server.getGadgetPort()).append("/action/install_simple").toString();
	        System.out.println(url);
			HttpPost httpPost = new HttpPost(url);
			List<NameValuePair> formparams = new ArrayList<NameValuePair>();  
			formparams.add(new BasicNameValuePair("path",deployment.getInstallFullPath()));  
			formparams.add(new BasicNameValuePair("config", configBuffer.toString()));
		
			/*JSONObject params = new JSONObject();
			params.put("path", deployment.getInstallFullPath());
			params.put("config", configBuffer.toString());
			
			StringEntity entity = new StringEntity(params.toJSONString());
			httpPost.setEntity(entity);*/
			httpPost.setEntity(new UrlEncodedFormEntity(formparams, "utf-8"));
			
			RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(60*60*1000).setConnectTimeout(60*60*1000).build();
            httpPost.setConfig(requestConfig);
            
			CloseableHttpResponse response = client.execute(httpPost);
			
			// 解析小工具HTTP返回结果并提示异常信息
			HttpEntity httpEntity = response.getEntity();
			InputStream content = httpEntity.getContent();
			byte[] byteArr = new byte[content.available()];
			content.read(byteArr);
			String str = new String(byteArr);
			JSONObject jsonObject = JSON.parseObject(str);
			String result = jsonObject.getString("result");
			String errormsg = jsonObject.getString("errormsg");
			if(!"0".equals(result)){
				throw new HttpGadgetInstallException(server.getIp(), server.getGadgetPort(), errormsg);
			}
			
			int code = response.getStatusLine().getStatusCode();
			if(code != 200){
				throw new HttpGadgetInstallException(server.getIp(), server.getGadgetPort(), String.valueOf(code));
			}
		}finally{
			if(client != null) client.close();
		}
	}
	
	/**
	 * 执行升级操作<br/>
	 * <b>作者:</b>jiajun<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年10月22日 上午9:20:45
	 * @param deploymentId 部署id
	 * @param updatePackageId 升级包id
	 * @param config 
	 * @throws Exception
	 */
	@Transactional(rollbackFor = Exception.class)
	public void update(Long deploymentId, Long updatePackageId, String config, Boolean isBackup, String notes) throws Exception{
		
		ServiceDeploymentPO deployment = serviceDeploymentDao.findOne(deploymentId);
		ServiceTypePO serviceType = serviceTypeDao.findOne(deployment.getServiceTypeId());
		String deploymentName = serviceType.getName();
		
		InstallationPackagePO updateInstallationPackagePO = installationPackageDao.findOne(updatePackageId);
		
		if(isBackup){
			backup(deploymentId, deploymentName, notes);
		}
				
		CloseableHttpClient client = null;
		try{
			
			deployment.setInstallationPackageId(updatePackageId);
			Date now = new Date();
			deployment.setUpdateTime(now);
			deployment.setInstallFullPath(new StringBufferWrapper().append(RELATIVE_FOLDER).append("/").append(updateInstallationPackagePO.getFileName()).toString());
			deployment.setConfig(config);
			serviceDeploymentDao.save(deployment);
			ServerPO server = serverDao.findOne(deployment.getServerId());
			
			List<ProcessDeploymentPO> oldProcessList = processDeploymentDAO.findByServiceDeploymentId(deploymentId);
			if(oldProcessList.size() > 0){
				processDeploymentDAO.deleteInBatch(oldProcessList);
			}
			
			InstallationPackagePO installationPackage = installationPackageDao.findOne(deployment.getInstallationPackageId());
			List<ProcessPO> list = processDAO.findByInstallationPackageId(installationPackage.getId());
			List<ProcessDeploymentPO> processList = new ArrayList<ProcessDeploymentPO>();
			for (ProcessPO processPO : list) {
				ProcessDeploymentPO processDeployment = new ProcessDeploymentPO();
//				processDeployment.setId(processPO.getId());
				processDeployment.setProcessId(processPO.getProcessId());
				processDeployment.setProcessName(processPO.getProcessName());
				processDeployment.setServiceDeploymentId(deploymentId);
				processList.add(processDeployment);
			}
			processDeploymentDAO.save(processList);
			
			StringBufferWrapper configBuffer = new StringBufferWrapper();
			if(config!=null && !"".equals(config)){
				JSONObject configJson = JSON.parseObject(config);
				Set<String> keys = configJson.keySet();
				for(String key:keys){
					String value = configJson.getString(key);
					configBuffer.append(key).append("=").append(value).append("\n");
				}
			}
			
			CredentialsProvider credsProvider = new BasicCredentialsProvider();
			AuthScope authScope = new AuthScope(server.getIp(), Integer.parseInt(server.getGadgetPort()), "example.com", AuthScope.ANY_SCHEME);
	        credsProvider.setCredentials(authScope, new UsernamePasswordCredentials(server.getGadgetUsername(), server.getGadgetPassword()));
	        client = HttpClients.custom()
			        		    .setDefaultCredentialsProvider(credsProvider)
			        		    .setRetryHandler(new DefaultHttpRequestRetryHandler(1, true))
			        		    .build();

	        String url = new StringBufferWrapper().append("http://").append(server.getIp()).append(":").append(server.getGadgetPort()).append("/action/unpack_and_execute_script").toString();
	        System.out.println(url);
			HttpPost httpPost = new HttpPost(url);
			List<NameValuePair> formparams = new ArrayList<NameValuePair>();  
			formparams.add(new BasicNameValuePair("path",deployment.getInstallFullPath()));  
//			formparams.add(new BasicNameValuePair("config", configBuffer.toString()));
			formparams.add(new BasicNameValuePair("name", "update.sh"));
			
			httpPost.setEntity(new UrlEncodedFormEntity(formparams, "utf-8"));
			
			RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(60*60*1000).setConnectTimeout(60*60*1000).build();
            httpPost.setConfig(requestConfig);
            
			CloseableHttpResponse response = client.execute(httpPost);
			
			// 解析小工具HTTP返回结果并提示异常信息
			HttpEntity httpEntity = response.getEntity();
			InputStream content = httpEntity.getContent();
			byte[] byteArr = new byte[content.available()];
			content.read(byteArr);
			String str = new String(byteArr);
			JSONObject jsonObject = JSON.parseObject(str);
			String result = jsonObject.getString("result");
			String errormsg = jsonObject.getString("errormsg");
			if(!"0".equals(result)){
				throw new HttpGadgetUpdateException(server.getIp(), server.getGadgetPort(), errormsg);
			}
						
			int code = response.getStatusLine().getStatusCode();
			if(code != 200){
				throw new HttpGadgetUpdateException(server.getIp(), server.getGadgetPort(), String.valueOf(code));
			}
		}finally{
			if(client != null) client.close();
		}
	}
	
	/**
	 * 重启进程<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>jiajun<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年10月14日 上午9:54:22
	 * @param deploymentId 部署id processId 进程名称
	 * @param config
	 * @throws Exception
	 */
	public void restart(Long deploymentId, String processId) throws Exception{
		CloseableHttpClient client = null;
		try {
			ServiceDeploymentPO deployment = serviceDeploymentDao.findOne(deploymentId);
			ServerPO server = serverDao.findOne(deployment.getServerId());
			ProcessDeploymentPO processDeployment = processDeploymentDAO.findByProcessId(processId);
			processDeployment.setStatus(ProcessDeploymentStatus.OFFLINE);
			processDeploymentDAO.save(processDeployment);
			
			CredentialsProvider credsProvider = new BasicCredentialsProvider();
			AuthScope authScope = new AuthScope(server.getIp(), Integer.parseInt(server.getGadgetPort()), "example.com", AuthScope.ANY_SCHEME);
	        credsProvider.setCredentials(authScope, new UsernamePasswordCredentials(server.getGadgetUsername(), server.getGadgetPassword()));
	        client = HttpClients.custom()
			        		    .setDefaultCredentialsProvider(credsProvider)
			        		    .setRetryHandler(new DefaultHttpRequestRetryHandler(1, true))
			        		    .build();
	        
	        String url = new StringBufferWrapper().append("http://").append(server.getIp()).append(":").append(server.getGadgetPort()).append("/action/kill_pid").toString();
	        HttpPost httpPost = new HttpPost(url);
	        List<NameValuePair> formparams = new ArrayList<NameValuePair>();  
			formparams.add(new BasicNameValuePair("name", processId));  
	        
			httpPost.setEntity(new UrlEncodedFormEntity(formparams, "utf-8"));
	        
	        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(10000).setConnectTimeout(10000).build();
	        httpPost.setConfig(requestConfig);
	        
			CloseableHttpResponse response = client.execute(httpPost);
			
			// 解析小工具HTTP返回结果并提示异常信息
			HttpEntity httpEntity = response.getEntity();
			InputStream content = httpEntity.getContent();
			byte[] byteArr = new byte[content.available()];
			content.read(byteArr);
			String str = new String(byteArr);
			JSONObject jsonObject = JSON.parseObject(str);
			String result = jsonObject.getString("result");
			String errormsg = jsonObject.getString("errormsg");
			if(!"0".equals(result)){
				throw new HttpGadgetRestartProcessException(server.getIp(), server.getGadgetPort(), errormsg);
			}
			
			int code = response.getStatusLine().getStatusCode();
			if(code != 200){
				throw new HttpGadgetRestartProcessException(server.getIp(), server.getGadgetPort(), String.valueOf(code));
			}     
		}finally{
			if(client != null) client.close();
		} 	
	}
	
	/**
	 * 
	 * 数据库备份<br/>
	 * <b>作者:</b>jiajun<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月16日 下午5:25:52
	 * @param id 进程id
	 * @throws Exception
	 */
	public void databaseBackup(Long id) throws Exception{
		ProcessDeploymentPO processDeployment = processDeploymentDAO.findOne(id);
		ServiceDeploymentPO serviceDeployment = serviceDeploymentDao.findOne(processDeployment.getServiceDeploymentId());
		String config = serviceDeployment.getConfig();
		JSONObject jsonObject = JSON.parseObject(config);
		String databaseIP = jsonObject.getString("databaseAddr");// 数据库IP
		DatabasePO database = databaseDAO.findByDatabaseIP(databaseIP);
		String username = database.getUsername();// 用户名
		String password = database.getPassword();// 密码
		String db = processDeployment.getDb();
		String[] arr = db.split("/");
		String databaseName = arr[1];// 数据库名称
		
		ServiceTypePO serviceType = serviceTypeDao.findOne(serviceDeployment.getServiceTypeId());
		
		String serviceTypeName = serviceType.getName();
	}
	
	/**
	 * 执行卸载<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年9月4日 上午10:35:27
	 * @param Long deploymentId 部署id
	 */
	@Transactional(rollbackFor = Exception.class)
	public void uninstall(Long deploymentId, String type, String notes) throws Exception{
		CloseableHttpClient client = null;
		try{
			ServiceDeploymentPO deployment = serviceDeploymentDao.findOne(deploymentId);
			ServerPO server = serverDao.findOne(deployment.getServerId());
			ServiceTypePO serviceType = serviceTypeDao.findOne(deployment.getServiceTypeId());
			String deploymentName = serviceType.getName();
			
			
			if("uninstall".equals(type)){
				backup(deploymentId, deploymentName, notes);
				deployment.setStatus(ServiceDeploymentStatus.UNINSTALLED);
				serviceDeploymentDao.save(deployment);
			}
			if("delete".equals(type)){
				List<ProcessDeploymentPO> processList = processDeploymentDAO.findByServiceDeploymentId(deploymentId);
				if(processList.size() > 0){
					processDeploymentDAO.deleteInBatch(processList);
				}
				List<BackupInformationPO> backupList = backupInformationDAO.findByDeploymentId(deploymentId);
				if(backupList.size() > 0){
					for (BackupInformationPO backupInformation : backupList) {
						deleteBackup(backupInformation.getId());
					}
				}
				serviceDeploymentDao.delete(deployment);
			}
			
			CredentialsProvider credsProvider = new BasicCredentialsProvider();
			AuthScope authScope = new AuthScope(server.getIp(), Integer.parseInt(server.getGadgetPort()), "example.com", AuthScope.ANY_SCHEME);
	        credsProvider.setCredentials(authScope, new UsernamePasswordCredentials(server.getGadgetUsername(), server.getGadgetPassword()));
	        client = HttpClients.custom()
			        		    .setDefaultCredentialsProvider(credsProvider)
			        		    .setRetryHandler(new DefaultHttpRequestRetryHandler(1, true))
			        		    .build();
			String url = new StringBufferWrapper().append("http://").append(server.getIp()).append(":").append(server.getGadgetPort()).append("/action/bvc_uninstall_with_path").toString();
			HttpPost httpPost = new HttpPost(url);
			
			List<NameValuePair> formparams = new ArrayList<NameValuePair>(); 
			formparams.add(new BasicNameValuePair("path", deployment.getInstallFullPath()));  
			httpPost.setEntity(new UrlEncodedFormEntity(formparams, "utf-8"));
			
			RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(10000).setConnectTimeout(10000).build();
            httpPost.setConfig(requestConfig);
			
			CloseableHttpResponse response = client.execute(httpPost);
			
			// 解析小工具HTTP返回结果并提示异常信息
			HttpEntity httpEntity = response.getEntity();
			InputStream content = httpEntity.getContent();
			byte[] byteArr = new byte[content.available()];
			content.read(byteArr);
			String str = new String(byteArr);
			JSONObject jsonObject = JSON.parseObject(str);
			String result = jsonObject.getString("result");
			String errormsg = jsonObject.getString("errormsg");
			if(!"0".equals(result)){
				throw new HttpGadgetUninstallException(server.getIp(), server.getGadgetPort(), errormsg);
			}

			int code = response.getStatusLine().getStatusCode();
			if(code != 200){
				throw new HttpGadgetUninstallException(server.getIp(), server.getGadgetPort(), String.valueOf(code));
			}
		}finally{
			if(client != null) client.close();
		}
	}
	
	/**
	 * 删除部署数据<br/>
	 * <b>作者:</b>jiajun<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年10月30日 上午10:57:49
	 * @param deploymentId 部署id
	 * @throws Exception
	 */
	@Transactional(rollbackFor = Exception.class)
	public void deleteDeploymentData(Long deploymentId) throws Exception{
		ServiceDeploymentPO deployment = serviceDeploymentDao.findOne(deploymentId);

		List<ProcessDeploymentPO> processList = processDeploymentDAO.findByServiceDeploymentId(deploymentId);
		if(processList.size() > 0){
			processDeploymentDAO.deleteInBatch(processList);
		}
		List<BackupInformationPO> backupList = backupInformationDAO.findByDeploymentId(deploymentId);
		if(backupList.size() > 0){
			for (BackupInformationPO backupInformation : backupList) {
				deleteBackup(backupInformation.getId());
			}
		}
		serviceDeploymentDao.delete(deployment);
	}
	
	/**
	 * 安装包备份<br/>
	 * <b>作者:</b>jiajun<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年10月15日 下午2:57:45
	 * @param deploymentId 部署id
	 */
	@Transactional(rollbackFor = Exception.class)
	public BackupInformationVO backup(Long deploymentId, String deploymentName, String notes) throws Exception{
		CloseableHttpClient client = null;
		try {
			// 调小工具接口生成backfile.zip
			ServiceDeploymentPO deployment = serviceDeploymentDao.findOne(deploymentId);
			ServerPO server = serverDao.findOne(deployment.getServerId());
			InstallationPackagePO installationPackage = installationPackageDao.findOne(deployment.getInstallationPackageId());
			
			CredentialsProvider credsProvider = new BasicCredentialsProvider();
			AuthScope authScope = new AuthScope(server.getIp(), Integer.parseInt(server.getGadgetPort()), "example.com", AuthScope.ANY_SCHEME);
	        credsProvider.setCredentials(authScope, new UsernamePasswordCredentials(server.getGadgetUsername(), server.getGadgetPassword()));
	        client = HttpClients.custom()
			        		    .setDefaultCredentialsProvider(credsProvider)
			        		    .setRetryHandler(new DefaultHttpRequestRetryHandler(1, true))
			        		    .build();
	        
	        String url = new StringBufferWrapper().append("http://").append(server.getIp()).append(":").append(server.getGadgetPort()).append("/action/execute_script").toString();
	        HttpPost httpPost = new HttpPost(url);
	        List<NameValuePair> formparams = new ArrayList<NameValuePair>();  
	        String installFullPath = deployment.getInstallFullPath();
			formparams.add(new BasicNameValuePair("path", installFullPath.substring(0, installFullPath.length()-4)));
			formparams.add(new BasicNameValuePair("name", "backup.sh"));
	        
			httpPost.setEntity(new UrlEncodedFormEntity(formparams, "utf-8"));
	        
	        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(60*60*1000).setConnectTimeout(60*60*1000).build();
	        httpPost.setConfig(requestConfig);
	        
			CloseableHttpResponse response = client.execute(httpPost);
			
			// 解析小工具HTTP返回结果并提示异常信息
			HttpEntity httpEntity = response.getEntity();
			InputStream content = httpEntity.getContent();
			byte[] byteArr = new byte[content.available()];
			content.read(byteArr);
			String str = new String(byteArr);
			JSONObject jsonObject = JSON.parseObject(str);
			String result = jsonObject.getString("result");
			String errormsg = jsonObject.getString("errormsg");
			if(!"0".equals(result)){
				throw new HttpGadgetBackupException(server.getIp(), server.getGadgetPort(), errormsg);
			}
			
			int code = response.getStatusLine().getStatusCode();
			if(code != 200){
				throw new HttpGadgetBackupException(server.getIp(), server.getGadgetPort(), String.valueOf(code));
			}
			// 将backup文件夹中的xxx.zip文件下载到运维服务器上
			Date date = new Date();
			
			Path path = SpringContext.getBean(Path.class);
			String fileName = installationPackage.getFileName();
			String folderName = fileName.substring(0, fileName.length()-4);
			String downloadPath = new StringBufferWrapper().append(path.webappPath())
															.append("backup/")
															.append(deploymentName)
															.append("/")
															.append(server.getName())
															.append("/")
															.append(installationPackage.getVersion().trim())
															.append("/")
															.append(date.getTime()+"/").toString();
			String backupFullPath = downloadPath + "backfile.zip";
			File file = new File(downloadPath);
			if(!file.exists()){
				file.mkdirs();
			}
			
			enableFtp(deploymentId);// 打开ftp端口
			
			Boolean bool = downFile(
				server.getIp(), 
				Integer.parseInt(server.getFtpPort()), 
				server.getFtpUsername(), 
				server.getFtpPassword(), 
				new StringBufferWrapper().append("/install/").append(folderName).append("/backup").toString(), 
				"backfile.zip", 
				downloadPath);
			
			disableFtp(deploymentId);// 关闭ftp端口
			
			// 将备份的相关信息存入到数据库中
			if(bool){
				BackupInformationPO backupInformation = new BackupInformationPO();
				backupInformation.setServerId(server.getId());
				backupInformation.setServerName(server.getName());
				backupInformation.setServerIp(server.getIp());
				backupInformation.setDeploymentId(deployment.getId());
				backupInformation.setDeploymentName(deploymentName);
				backupInformation.setInstallPackageId(installationPackage.getId());
				backupInformation.setFileName(installationPackage.getFileName());
				backupInformation.setVersion(installationPackage.getVersion());
				backupInformation.setBackupTime(date);
				backupInformation.setNotes(notes);
				backupInformation.setBackupFullPath(backupFullPath);
				backupInformation.setConfig(deployment.getConfig());
				backupInformationDAO.save(backupInformation);
				return new BackupInformationVO().set(backupInformation);
			}
		}finally{
			if(client != null) client.close();
		}
		return null; 	
	}
	
	/**
	 * 从FTP服务器下载文件<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>jiajun<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年10月15日 下午4:04:23
	 * @param url FTP服务器hostname
	 * @param port FTP服务器端口
	 * @param username FTP登录账号
	 * @param password FTP登录密码
	 * @param remotePath FTP服务器上的相对路径
	 * @param fileName 要下载的文件名
	 * @param localPath 下载后保存到本地的路径
	 * @return
	 */
	@Transactional(rollbackFor = Exception.class)
	public boolean downFile(String url, int port,String username, String password, String remotePath,String fileName,String localPath) {
		boolean success = false;
		FTPClient ftp = new FTPClient();
		try {
			int reply;
			ftp.connect(url, port);
			//如果采用默认端口，可以使用ftp.connect(url)的方式直接连接FTP服务器
			ftp.login(username, password);//登录
			reply = ftp.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)) {
				ftp.disconnect();
				return success;
			}
			ftp.changeWorkingDirectory(remotePath);//转移到FTP服务器目录
			ftp.setFileType(FTP.BINARY_FILE_TYPE);
			FTPFile[] fs = ftp.listFiles();
			for(FTPFile ff:fs){
				if(ff.getName().equals(fileName)){
					File localFile = new File(localPath+"/"+ff.getName());
					
					OutputStream is = new FileOutputStream(localFile); 
					ftp.retrieveFile(ff.getName(), is);
					is.close();
				}
			}
			
			ftp.logout();
			success = true;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (ftp.isConnected()) {
				try {
					ftp.disconnect();
				} catch (IOException ioe) {
				}
			}
		}
		return success;
	}
	
	/**
	 * 删除备份<br/>
	 * <b>作者:</b>jiajun<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年10月19日 下午4:11:26
	 * @param backupId 备份id
	 */
	@Transactional(rollbackFor = Exception.class)
	public void deleteBackup(Long backupId) throws Exception{
		BackupInformationPO backupInformation = backupInformationDAO.findOne(backupId);
		String fullPath = backupInformation.getBackupFullPath();
		String dirPath = fullPath.substring(0, fullPath.length()-12);
		deleteDir(dirPath);
		
		backupInformationDAO.delete(backupId);
	}
	
	/**
	 * 迭代删除文件夹<br/>
	 * <b>作者:</b>jiajun<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年10月23日 下午3:47:43
	 * @param dirPath 文件夹目录
	 */
	public void deleteDir(String dirPath)
	{
		File file = new File(dirPath);
		if(file.isFile())
		{
			file.delete();
		}else
		{
			File[] files = file.listFiles();
			if(files == null)
			{
				file.delete();
			}else
			{
				for (int i = 0; i < files.length; i++) 
				{
					deleteDir(files[i].getAbsolutePath());
				}
				file.delete();
			}
		}
	}
	/**
	 * 系统恢复<br/>
	 * <b>作者:</b>jiajun<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年10月19日 下午3:52:29
	 * @param backupId 备份id
	 */
	@Transactional(rollbackFor = Exception.class)
	public void restore(Long backupId) throws Exception{
		
		BackupInformationPO backupInformation = backupInformationDAO.findOne(backupId);
		InstallationPackagePO installationPackage = installationPackageDao.findOne(backupInformation.getInstallPackageId());
		ServerPO server = serverDao.findOne(backupInformation.getServerId());
		ServiceDeploymentPO deployment = serviceDeploymentDao.findOne(backupInformation.getDeploymentId());
		
		String fileName = installationPackage.getFileName();
		String folderName = fileName.substring(0, fileName.length()-4);
		
		// 根据备份信息找到运维服务器上的安装包上传到ftp服务器（覆盖原有的安装包）
		Path path = SpringContext.getBean(Path.class);
		
		enableFtp(deployment.getId());// 打开ftp端口
		
		uploadFile(
				server.getIp(), 
				Integer.parseInt(server.getFtpPort()), 
				server.getFtpUsername(), 
				server.getFtpPassword(),
				"/install/", 
				fileName,
				new StringBufferWrapper().append(path.webappPath()).append("packages").append(File.separator).append(backupInformation.getDeploymentName()).append(File.separator).append(fileName).toString()
		);
		
		disableFtp(deployment.getId());// 关闭ftp端口
		
		// 解压安装包
		CloseableHttpClient decompressionClient = null;
		try {
			CredentialsProvider credsProvider = new BasicCredentialsProvider();
			AuthScope authScope = new AuthScope(server.getIp(), Integer.parseInt(server.getGadgetPort()), "example.com", AuthScope.ANY_SCHEME);
	        credsProvider.setCredentials(authScope, new UsernamePasswordCredentials(server.getGadgetUsername(), server.getGadgetPassword()));
	        decompressionClient = HttpClients.custom()
			        		    .setDefaultCredentialsProvider(credsProvider)
			        		    .setRetryHandler(new DefaultHttpRequestRetryHandler(1, true))
			        		    .build();
	        
	        String url = new StringBufferWrapper().append("http://").append(server.getIp()).append(":").append(server.getGadgetPort()).append("/action/unpack_and_execute_script").toString();
	        HttpPost httpPost = new HttpPost(url);
	        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
			formparams.add(new BasicNameValuePair("path", deployment.getInstallFullPath()));
			formparams.add(new BasicNameValuePair("name", "1"));
	        
			httpPost.setEntity(new UrlEncodedFormEntity(formparams, "utf-8"));
	        
	        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(60*60*1000).setConnectTimeout(60*60*1000).build();
	        httpPost.setConfig(requestConfig);
	        
			CloseableHttpResponse response = decompressionClient.execute(httpPost);
			
			// 解析小工具HTTP返回结果并提示异常信息
			HttpEntity httpEntity = response.getEntity();
			InputStream content = httpEntity.getContent();
			byte[] byteArr = new byte[content.available()];
			content.read(byteArr);
			String str = new String(byteArr);
			JSONObject jsonObject = JSON.parseObject(str);
			String result = jsonObject.getString("result");
			String errormsg = jsonObject.getString("errormsg");
			if(!"0".equals(result)){
				throw new HttpGadgetUnzipException(server.getIp(), server.getGadgetPort(), errormsg);
			}
			
			int code = response.getStatusLine().getStatusCode();
			if(code != 200){
				throw new HttpGadgetUnzipException(server.getIp(), server.getGadgetPort(), String.valueOf(code));
			}
		}finally{
			if(decompressionClient != null) decompressionClient.close();
		}
		
		// 根据备份信息找到之前备份下来的xxx.zip压缩包，将其上传到ftp服务器上对应安装包下的backup文件夹中
		
		enableFtp(deployment.getId());// 打开ftp端口
		
		uploadFile(
				server.getIp(), 
				Integer.parseInt(server.getFtpPort()), 
				server.getFtpUsername(), 
				server.getFtpPassword(),
				new StringBufferWrapper().append("/install/").append(folderName).append("/backup/").toString(), 
				"backfile.zip", 
				backupInformation.getBackupFullPath());
		
		disableFtp(deployment.getId());// 关闭ftp端口
		
		// 执行恢复脚本restore.sh
		CloseableHttpClient client = null;
		try {
			CredentialsProvider credsProvider = new BasicCredentialsProvider();
			AuthScope authScope = new AuthScope(server.getIp(), Integer.parseInt(server.getGadgetPort()), "example.com", AuthScope.ANY_SCHEME);
	        credsProvider.setCredentials(authScope, new UsernamePasswordCredentials(server.getGadgetUsername(), server.getGadgetPassword()));
	        client = HttpClients.custom()
			        		    .setDefaultCredentialsProvider(credsProvider)
			        		    .setRetryHandler(new DefaultHttpRequestRetryHandler(1, true))
			        		    .build();
	        
	        String url = new StringBufferWrapper().append("http://").append(server.getIp()).append(":").append(server.getGadgetPort()).append("/action/execute_script").toString();
	        HttpPost httpPost = new HttpPost(url);
	        List<NameValuePair> formparams = new ArrayList<NameValuePair>();  
	        String installFullPath = deployment.getInstallFullPath();
			formparams.add(new BasicNameValuePair("path", installFullPath.substring(0, installFullPath.length()-4)));
			formparams.add(new BasicNameValuePair("name", "restore.sh"));
	        
			httpPost.setEntity(new UrlEncodedFormEntity(formparams, "utf-8"));
	        
	        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(60*60*1000).setConnectTimeout(60*60*1000).build();
	        httpPost.setConfig(requestConfig);
	        
			CloseableHttpResponse response = client.execute(httpPost);
			
			// 解析小工具HTTP返回结果并提示异常信息
			HttpEntity httpEntity = response.getEntity();
			InputStream content = httpEntity.getContent();
			byte[] byteArr = new byte[content.available()];
			content.read(byteArr);
			String str = new String(byteArr);
			JSONObject jsonObject = JSON.parseObject(str);
			String result = jsonObject.getString("result");
			String errormsg = jsonObject.getString("errormsg");
			if(!"0".equals(result)){
				throw new HttpGadgetRestoreException(server.getIp(), server.getGadgetPort(), errormsg);
			}
			
			int code = response.getStatusLine().getStatusCode();
			if(code != 200){
				throw new HttpGadgetRestoreException(server.getIp(), server.getGadgetPort(), String.valueOf(code));
			}
		}finally{
			if(client != null) client.close();
		}
	}
	/**
	 * 向FTP服务器上传文件<br/>
	 * <b>作者:</b>jiajun<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年10月19日 下午6:35:07
	 * @param url FTP服务器hostname
	 * @param port FTP服务器端口
	 * @param username FTP登录账号
	 * @param password FTP登录密码
	 * @param path FTP服务器保存相对路径
	 * @param filename 上传到FTP服务器上的文件名
	 * @param inputPath 上传到FTP服务器上的文件的绝对路径
	 * @return 成功返回true，否则返回false
	 */
	@Transactional(rollbackFor = Exception.class)
	public boolean uploadFile(String url,int port,String username, String password, String path, String filename, String inputPath) throws Exception{
		boolean success = false;
		FTPClient ftp = new FTPClient();
		
		FileInputStream fin = null;
		BufferedInputStream in = null;
		OutputStream out = null;
		try {
			int reply;
			ftp.connect(url, port);//连接FTP服务器
			ftp.login(username, password);//登录
			ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
			ftp.enterLocalPassiveMode();
			ftp.setFileTransferMode(FTP.STREAM_TRANSFER_MODE);
			ftp.setControlEncoding("utf-8");
			ftp.setBufferSize(1024*1024*10);
			reply = ftp.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)) {
				ftp.disconnect();
				return success;
			}
			File file = new File(inputPath);
			long total = file.length();
			int lastBuffSize = 0;
			long buffLoopTimes = 0;
			int buffSize = ftp.getBufferSize();
			if(buffSize >= total){
				lastBuffSize = (int)total;
			}else{
				buffLoopTimes = (long)(total/buffSize);
				lastBuffSize = (int)(total - buffSize * buffLoopTimes);
			}
			byte[] buff = new byte[buffSize];
			byte[] lastBuff = new byte[lastBuffSize];
			
			fin = new FileInputStream(file);
			in = new BufferedInputStream(fin);
			String str = path + filename;
			out = ftp.storeFileStream(new String(str.getBytes("utf-8"), "iso-8859-1"));
			for(int i = 0; i < buffLoopTimes; i++){
				in.read(buff);
				out.write(buff);
			}
			if(lastBuffSize > 0){
				in.read(lastBuff);
				out.write(lastBuff);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {out.close();} catch (IOException e) {e.printStackTrace();}
			try {in.close();} catch (IOException e) {e.printStackTrace();}
			try {fin.close();} catch (IOException e) {e.printStackTrace();}
			if (ftp.isConnected()) {
				try {ftp.logout();ftp.disconnect();} catch (IOException ioe) {}
			}
		}
		return success;
	}
	
	/**
	 * 
	 * 打开ftp端口<br/>
	 * <b>作者:</b>jiajun<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月5日 上午9:21:55
	 * @param deploymentId 部署id
	 * @throws Exception
	 */
	public void enableFtp(Long deploymentId) throws Exception{
		CloseableHttpClient client = null;
		try {
			ServiceDeploymentPO deployment = serviceDeploymentDao.findOne(deploymentId);
			ServerPO server = serverDao.findOne(deployment.getServerId());
			
			CredentialsProvider credsProvider = new BasicCredentialsProvider();
			AuthScope authScope = new AuthScope(server.getIp(), Integer.parseInt(server.getGadgetPort()), "example.com", AuthScope.ANY_SCHEME);
	        credsProvider.setCredentials(authScope, new UsernamePasswordCredentials(server.getGadgetUsername(), server.getGadgetPassword()));
	        client = HttpClients.custom()
			        		    .setDefaultCredentialsProvider(credsProvider)
			        		    .setRetryHandler(new DefaultHttpRequestRetryHandler(1, true))
			        		    .build();
	        
	        String url = new StringBufferWrapper().append("http://").append(server.getIp()).append(":").append(server.getGadgetPort()).append("/action/enable_ftp").toString();
	        HttpPost httpPost = new HttpPost(url);
//	        List<NameValuePair> formparams = new ArrayList<NameValuePair>();  
//			formparams.add(new BasicNameValuePair("name", processId));  
//	        
//			httpPost.setEntity(new UrlEncodedFormEntity(formparams, "utf-8"));
	        
	        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(60*60*1000).setConnectTimeout(60*60*1000).build();
	        httpPost.setConfig(requestConfig);
	        
			CloseableHttpResponse response = client.execute(httpPost);
			
			// 解析小工具HTTP返回结果并提示异常信息
			HttpEntity httpEntity = response.getEntity();
			InputStream content = httpEntity.getContent();
			byte[] byteArr = new byte[content.available()];
			content.read(byteArr);
			String str = new String(byteArr);
			JSONObject jsonObject = JSON.parseObject(str);
			String result = jsonObject.getString("result");
			String errormsg = jsonObject.getString("errormsg");
			if(!"0".equals(result)){
				throw new HttpGadgetEnableFtpException(server.getIp(), server.getGadgetPort(), errormsg);
			}
			
			int code = response.getStatusLine().getStatusCode();
			if(code != 200){
				throw new HttpGadgetEnableFtpException(server.getIp(), server.getGadgetPort(), String.valueOf(code));
			}     
		}finally{
			if(client != null) client.close();
		} 	
	}
	
	/**
	 * 
	 * 关闭ftp端口<br/>
	 * <b>作者:</b>jiajun<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月5日 上午9:33:10
	 * @param deploymentId 部署id
	 * @throws Exception
	 */
	public void disableFtp(Long deploymentId) throws Exception{
		CloseableHttpClient client = null;
		try {
			ServiceDeploymentPO deployment = serviceDeploymentDao.findOne(deploymentId);
			ServerPO server = serverDao.findOne(deployment.getServerId());
			
			CredentialsProvider credsProvider = new BasicCredentialsProvider();
			AuthScope authScope = new AuthScope(server.getIp(), Integer.parseInt(server.getGadgetPort()), "example.com", AuthScope.ANY_SCHEME);
	        credsProvider.setCredentials(authScope, new UsernamePasswordCredentials(server.getGadgetUsername(), server.getGadgetPassword()));
	        client = HttpClients.custom()
			        		    .setDefaultCredentialsProvider(credsProvider)
			        		    .setRetryHandler(new DefaultHttpRequestRetryHandler(1, true))
			        		    .build();
	        
	        String url = new StringBufferWrapper().append("http://").append(server.getIp()).append(":").append(server.getGadgetPort()).append("/action/disable_ftp").toString();
	        HttpPost httpPost = new HttpPost(url);
	        
	        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(60*60*1000).setConnectTimeout(60*60*1000).build();
	        httpPost.setConfig(requestConfig);
	        
			CloseableHttpResponse response = client.execute(httpPost);
			
			// 解析小工具HTTP返回结果并提示异常信息
			HttpEntity httpEntity = response.getEntity();
			InputStream content = httpEntity.getContent();
			byte[] byteArr = new byte[content.available()];
			content.read(byteArr);
			String str = new String(byteArr);
			JSONObject jsonObject = JSON.parseObject(str);
			String result = jsonObject.getString("result");
			String errormsg = jsonObject.getString("errormsg");
			if(!"0".equals(result)){
				throw new HttpGadgetDisableFtpException(server.getIp(), server.getGadgetPort(), errormsg);
			}
			
			int code = response.getStatusLine().getStatusCode();
			if(code != 200){
				throw new HttpGadgetDisableFtpException(server.getIp(), server.getGadgetPort(), String.valueOf(code));
			}     
		}finally{
			if(client != null) client.close();
		} 	
	}
	
	/**
	 * 查询进程状态<br/>
	 * <b>作者:</b>jiajun<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年10月27日 上午11:38:50
	 */
	@Transactional(rollbackFor = Exception.class)
	public void queryStatus() throws Exception{
		List<ProcessDeploymentPO> processEntites = processDeploymentDAO.findAll();
		
		if(processEntites == null || processEntites.size() <= 0) return;
		List<ProcessDeploymentPO> needSaveProcess = new ArrayList<ProcessDeploymentPO>();
		List<ProcessDeploymentPO> needLoopProcess = new ArrayList<ProcessDeploymentPO>();
		for (ProcessDeploymentPO processEntity : processEntites) {
			if(processEntity.getProcessId() == null){
				processEntity.setStatus(ProcessDeploymentStatus.OFFLINE);
				needSaveProcess.add(processEntity);
			}else{
				needLoopProcess.add(processEntity);
			}
		}
		if(needSaveProcess.size() > 0){
			processDeploymentDAO.save(needSaveProcess);
		}
		if(needLoopProcess.size() > 0){
			List<ServerPO> serverList = serverDao.findAll();
			for (ServerPO server : serverList) {
				
				List<ServiceDeploymentPO> deploymentList = serviceDeploymentDao.findByServerIdAndStatus(server.getId(), ServiceDeploymentStatus.INSTALLED);
				List<ProcessDeploymentPO> processList = new ArrayList<ProcessDeploymentPO>();
				for (ServiceDeploymentPO deployment : deploymentList) {
					processList.addAll(processDeploymentDAO.findByServiceDeploymentId(deployment.getId()));
				}
				
				
				List<String> processIdList = new ArrayList<String>();
				for (ProcessDeploymentPO processDeploymentPO : processList) {
					processIdList.add(processDeploymentPO.getProcessId());
				}
				
				CredentialsProvider credsProvider = new BasicCredentialsProvider();
				AuthScope authScope = new AuthScope(server.getIp(), Integer.parseInt(server.getGadgetPort()), "example.com", AuthScope.ANY_SCHEME);
		        credsProvider.setCredentials(authScope, new UsernamePasswordCredentials(server.getGadgetUsername(), server.getGadgetPassword()));
				CloseableHttpAsyncClient client = HttpAsyncClients.custom()
																  .setDefaultCredentialsProvider(credsProvider)
																  .build();
				String url = new StringBufferWrapper().append("http://").append(server.getIp()).append(":").append(server.getGadgetPort()).append("/action/get_pid_state").toString();
				HttpPost httpPost = new HttpPost(url);
				List<NameValuePair> formparams = new ArrayList<NameValuePair>(); 
				
				for(int i = 0; i < processIdList.size(); i++){
					formparams.add(new BasicNameValuePair(new StringBufferWrapper().append("name[").append(i).append("]").toString(), processIdList.get(i)));
				}
				
				httpPost.setEntity(new UrlEncodedFormEntity(formparams, "utf-8"));
		        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(60*60*1000).setConnectTimeout(60*60*1000).build();
		        
		        httpPost.setConfig(requestConfig);
				client.start();
				client.execute(httpPost, new QueryStatusListener(server.getId(), client));
			}	
		}
	}
	
	/**
	 * 设置进程状态<br/>
	 * <b>作者:</b>jiajun<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年10月27日 下午2:15:59
	 * @param id
	 * @param processInfo
	 */
	@Transactional(rollbackFor = Exception.class)
	public void setStatus(Long serverId, JSONObject processInfo){
		JSONArray jsonArray = processInfo.getJSONArray("pid_stat");
		List<ProcessDeploymentPO> processList = new ArrayList<ProcessDeploymentPO>();
		for (Object object : jsonArray) {
			JSONObject jsonObject = (JSONObject)object;
			ProcessDeploymentPO process = processDeploymentDAO.findByProcessId(jsonObject.getString("name"));
			if(jsonObject.getString("status") != null){
				process.setStatus(ProcessDeploymentStatus.ONLINE);
			}else{
				process.setStatus(ProcessDeploymentStatus.OFFLINE);
			}
			processList.add(process);
		}
		processDeploymentDAO.save(processList);
	}
	
	/**
	 * 进程离线<br/>
	 * <b>作者:</b>jiajun<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年10月27日 下午2:11:58
	 * @param id 进程id
	 */
	@Transactional(rollbackFor = Exception.class)
	public void offlineStatus(Long serverId){
		
		List<ProcessDeploymentPO> processList = processDeploymentDAO.findByServerId(serverId);
		for (ProcessDeploymentPO process : processList) {
			process.setStatus(ProcessDeploymentStatus.OFFLINE);
		}
		processDeploymentDAO.save(processList);
	}
	
	/**
	 * 进程状态回调<br/>
	 * <b>作者:</b>jiajun<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年10月27日 下午1:35:42
	 */
	private class QueryStatusListener implements FutureCallback<HttpResponse>{

		private Long serverId;
		
		private CloseableHttpAsyncClient client;
		
		QueryStatusListener(
				Long serverId,
				CloseableHttpAsyncClient client){
			this.serverId = serverId;
			this.client = client;
		}
		
		@Override
		public void completed(HttpResponse response) {
			ServiceDeploymentService serviceDeploymentService = SpringContext.getBean(ServiceDeploymentService.class);
			try{
				if(response.getStatusLine().getStatusCode() == 200){
		        	HttpEntity entity = response.getEntity();
		        	String status = FileUtil.readAsString(entity.getContent());
		 	        EntityUtils.consume(entity);
		 	        JSONObject processInfo = JSON.parseObject(status);
		 	        serviceDeploymentService.setStatus(this.serverId, processInfo);
		        }else{
		        	serviceDeploymentService.offlineStatus(this.serverId);
		        }
			}catch(Exception e){
				e.printStackTrace();
				serviceDeploymentService.offlineStatus(this.serverId);
			}finally{
				try {
					if(client!=null) client.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		@Override
		public void failed(Exception ex) {
			ex.printStackTrace();
			try{
				ServiceDeploymentService serviceDeploymentService = SpringContext.getBean(ServiceDeploymentService.class);
				serviceDeploymentService.offlineStatus(this.serverId);
			}finally{
				try {
					if(client!=null) client.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		@Override
		public void cancelled() {
			try{
				ServiceDeploymentService serviceDeploymentService = SpringContext.getBean(ServiceDeploymentService.class);
				serviceDeploymentService.offlineStatus(this.serverId);
			}finally{
				try {
					if(client!=null) client.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
	}

}
