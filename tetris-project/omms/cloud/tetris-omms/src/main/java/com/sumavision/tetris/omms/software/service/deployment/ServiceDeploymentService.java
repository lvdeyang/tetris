package com.sumavision.tetris.omms.software.service.deployment;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.commons.context.SpringContext;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.mvc.listener.ServletContextListener.Path;
import com.sumavision.tetris.omms.hardware.server.ServerDAO;
import com.sumavision.tetris.omms.hardware.server.ServerPO;
import com.sumavision.tetris.omms.software.service.deployment.exception.FtpChangeFolderFailException;
import com.sumavision.tetris.omms.software.service.deployment.exception.FtpCreateFolderFailException;
import com.sumavision.tetris.omms.software.service.deployment.exception.FtpLoginFailWhenUploadInstallationPackageException;
import com.sumavision.tetris.omms.software.service.deployment.exception.HttpGadgetInstallException;
import com.sumavision.tetris.omms.software.service.deployment.exception.HttpGadgetUninstallException;
import com.sumavision.tetris.omms.software.service.installation.InstallationPackageDAO;
import com.sumavision.tetris.omms.software.service.installation.InstallationPackagePO;
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
		
		ServiceDeploymentPO serviceDeploymentEntity = new ServiceDeploymentPO();
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
		
		Thread uploadThread = new Thread(new Uploader(serverEntity, installationPackageEntity, serviceDeploymentEntity));
		uploadThread.start();
		
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
			ftpClient.enterLocalPassiveMode();
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
			deployment.setConfig(config);
			serviceDeploymentDao.save(deployment);
			ServerPO server = serverDao.findOne(deployment.getServerId());
			
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
			        		    .build();
	        String url = new StringBufferWrapper().append("http://").append(server.getIp()).append(":").append(server.getGadgetPort()).append("/action/get_bvc_install").toString();
			HttpPost httpPost = new HttpPost(url);
			List<NameValuePair> formparams = new ArrayList<NameValuePair>();  
			formparams.add(new BasicNameValuePair("relative_path", deployment.getInstallFullPath()));  
			formparams.add(new BasicNameValuePair("config", configBuffer.toString()));  
			httpPost.setEntity(new UrlEncodedFormEntity(formparams, "utf-8"));
			
			CloseableHttpResponse response = client.execute(httpPost);
			int code = response.getStatusLine().getStatusCode();
			if(code != 200){
				throw new HttpGadgetInstallException(server.getIp(), server.getGadgetPort(), String.valueOf(code));
			}
		}finally{
			if(client != null) client.close();
		}
	}
	
	/**
	 * 执行卸载<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年9月4日 上午10:35:27
	 * @param Long deploymentId 部署id
	 */
	@Transactional(rollbackFor = Exception.class)
	public void uninstall(Long deploymentId) throws Exception{
		CloseableHttpClient client = null;
		try{
			ServiceDeploymentPO deployment = serviceDeploymentDao.findOne(deploymentId);
			ServerPO server = serverDao.findOne(deployment.getServerId());
			serviceDeploymentDao.delete(deployment);
			
			CredentialsProvider credsProvider = new BasicCredentialsProvider();
			AuthScope authScope = new AuthScope(server.getIp(), Integer.parseInt(server.getGadgetPort()), "example.com", AuthScope.ANY_SCHEME);
	        credsProvider.setCredentials(authScope, new UsernamePasswordCredentials(server.getGadgetUsername(), server.getGadgetPassword()));
	        client = HttpClients.custom()
			        		    .setDefaultCredentialsProvider(credsProvider)
			        		    .build();
			String url = new StringBufferWrapper().append("http://").append(server.getIp()).append(":").append(server.getGadgetPort()).append("/action/bvc_uninstall").toString();
			HttpPost httpPost = new HttpPost(url);
			
			List<NameValuePair> formparams = new ArrayList<NameValuePair>();  
			formparams.add(new BasicNameValuePair("relative_path", deployment.getInstallFullPath()));  
			httpPost.setEntity(new UrlEncodedFormEntity(formparams, "utf-8"));
			
			CloseableHttpResponse response = client.execute(httpPost);
			int code = response.getStatusLine().getStatusCode();
			if(code != 200){
				throw new HttpGadgetUninstallException(server.getIp(), server.getGadgetPort(), String.valueOf(code));
			}
		}finally{
			if(client != null) client.close();
		}
	}
	
}
