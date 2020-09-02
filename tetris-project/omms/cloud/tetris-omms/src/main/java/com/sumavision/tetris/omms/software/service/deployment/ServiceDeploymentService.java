package com.sumavision.tetris.omms.software.service.deployment;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.util.Date;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sumavision.tetris.commons.context.SpringContext;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.mvc.listener.ServletContextListener.Path;
import com.sumavision.tetris.omms.hardware.server.ServerDAO;
import com.sumavision.tetris.omms.hardware.server.ServerPO;
import com.sumavision.tetris.omms.software.service.deployment.exception.FtpChangeFolderFailException;
import com.sumavision.tetris.omms.software.service.deployment.exception.FtpCreateFolderFailException;
import com.sumavision.tetris.omms.software.service.deployment.exception.FtpLoginFailWhenUploadInstallationPackageException;
import com.sumavision.tetris.omms.software.service.installation.InstallationPackageDAO;
import com.sumavision.tetris.omms.software.service.installation.InstallationPackagePO;

@Service
public class ServiceDeploymentService {

	public static final String RELATIVE_FOLDER = "install";
	
	@Autowired
	private ServerDAO serverDao;
	
	@Autowired
	private InstallationPackageDAO installationPackageDao;
	
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
		
		ServiceDeploymentPO serviceDeploymentEntity = new ServiceDeploymentPO();
		serviceDeploymentEntity.setUpdateTime(new Date());
		serviceDeploymentEntity.setServiceTypeId(serviceDeploymentEntity.getServiceTypeId());
		serviceDeploymentEntity.setInstallationPackageId(serviceDeploymentEntity.getId());
		serviceDeploymentEntity.setInstallFullPath(new StringBufferWrapper().append(RELATIVE_FOLDER).append("/").append(installationPackageEntity.getFileName()).toString());
		serviceDeploymentEntity.setServerId(serverEntity.getId());
		serviceDeploymentEntity.setStep(DeploymentStep.UPLOAD);
		serviceDeploymentEntity.setProgress(0);
		serviceDeploymentDao.save(serviceDeploymentEntity);
		
		Thread uploadThread = new Thread(new Uploader(serverEntity, installationPackageEntity, serviceDeploymentEntity));
		uploadThread.start();
		
		return new ServiceDeploymentVO().set(serviceDeploymentEntity);
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
	
}
