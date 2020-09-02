package com.sumavision.tetris.omms.software.service.deployment;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.Date;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.omms.hardware.server.ServerDAO;
import com.sumavision.tetris.omms.hardware.server.ServerPO;
import com.sumavision.tetris.omms.software.service.installation.InstallationPackageDAO;
import com.sumavision.tetris.omms.software.service.installation.InstallationPackagePO;
import com.sumavision.tetris.omms.software.service.type.ServiceTypeDAO;
import com.sumavision.tetris.omms.software.service.type.ServiceTypePO;

@Service
public class ServiceDeploymentService {

	@Autowired
	private ServerDAO serverDao;
	
	@Autowired
	private ServiceTypeDAO serviceTypeDao;
	
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
	public ServiceDeploymentVO upload(
			Long serverId,
			Long installationPackageId) throws Exception{
		
		ServerPO serverEntity = serverDao.findOne(serverId);
		InstallationPackagePO installationPackageEntity = installationPackageDao.findOne(installationPackageId);
		ServiceTypePO serviceTypeEntity = serviceTypeDao.findOne(installationPackageEntity.getServiceTypeId());
		
		ServiceDeploymentPO serviceDeploymentEntity = new ServiceDeploymentPO();
		serviceDeploymentEntity.setUpdateTime(new Date());
		serviceDeploymentEntity.setServiceTypeId(serviceDeploymentEntity.getServiceTypeId());
		serviceDeploymentEntity.setInstallationPackageId(serviceDeploymentEntity.getId());
		serviceDeploymentEntity.setInstallFullPath(new StringBufferWrapper().append(serviceTypeEntity.getInstallationDirectory()).append(installationPackageEntity.getFileName()).toString());
		serviceDeploymentEntity.setServerId(serverEntity.getId());
		serviceDeploymentEntity.setStep(DeploymentStep.UPLOAD);
		serviceDeploymentEntity.setProgress(0);
		serviceDeploymentDao.save(serviceDeploymentEntity);
		
		Thread uploadThread = new Thread(new Uploader());
		uploadThread.start();
		
		return new ServiceDeploymentVO().set(serviceDeploymentEntity);
	}
	
	public static class Uploader implements Runnable{

		private Long deploymentId;

		
		
		@Override
		public void run() {
			
			
		}
		
	}
	
	public static void main(String[] args) throws Exception{
		/*// 设置默认工厂类
		System.setProperty("org.apache.commons.logging.LogFactory", "org.apache.commons.logging.impl.LogFactoryImpl");
		// 设置日志打印类
		LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.SimpleLog");
		//设置默认日志级别
		LogFactory.getFactory().setAttribute("org.apache.commons.logging.simplelog.defaultlog", "error");*/
		//Sardine sardine = SardineFactory.begin("root", "sumavisionrd");
		//sardine.createDirectory("http://192.165.56.109:8914/dav/install/");
		
		//InputStream in = new FileInputStream(new File("D:\\web\\eclipseWorkSpace\\tetris\\tetris-project\\omms\\cloud\\tetris-omms\\src\\main\\webapp\\packages\\文件转码\\test-V111.zip"));
		//InputStream in = new FileInputStream(new File("E:\\新建文件夹.zip"));
		//sardine.put("http://192.165.56.109:8914/dav/install/新建文件夹.zip", in);
		
		/*CredentialsProvider credsProvider = new BasicCredentialsProvider();
		AuthScope authScope = new AuthScope("192.165.56.109", 8914);
        credsProvider.setCredentials(authScope, new UsernamePasswordCredentials("root", "sumavisionrd"));
        
        CloseableHttpClient client = HttpClients.custom().setDefaultCredentialsProvider(credsProvider).build();
		
		HttpPut httpPut = new HttpPut("http://192.165.56.109:8914/dav/install/新建文件夹.zip");
		httpPut.setEntity(MultipartEntityBuilder.create().addPart("file", new FileBodyWithProgress(new File("E:\\新建文件夹.zip"), new UpdateProgress(){

			@Override
			public void update(long contentLength, long progress) {
				System.out.println((long)(progress/contentLength)*100);
			}

			@Override
			public int getBlockSize() {
				return 1024*1024*50;
			}
			
		})).build());
		
		CloseableHttpResponse response = client.execute(httpPut);
		System.out.println(response.getStatusLine());*/
		
		FTPClient ftpClient = new FTPClient();
		ftpClient.connect("192.165.56.70", 21);
		boolean r = ftpClient.login("test", "123");
		System.out.println(r);
		boolean dr = ftpClient.makeDirectory(new String("新建文件夹".getBytes("utf-8"), "iso-8859-1"));
		System.out.println(dr);
		ftpClient.setFileType(ftpClient.BINARY_FILE_TYPE);
		ftpClient.enterLocalPassiveMode();
		ftpClient.setFileTransferMode(FTP.STREAM_TRANSFER_MODE);
		ftpClient.setControlEncoding("UTF-8");
		ftpClient.setBufferSize(1024*1024*50);
		boolean cr = ftpClient.changeWorkingDirectory(new String("新建文件夹".getBytes("utf-8"), "iso-8859-1"));
		System.out.println(cr);
		File file = new File("E:\\test-V111.zip");
		System.out.println(file.exists());
		BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));
		//ftpClient.changeWorkingDirectory("/usr/sbin/sumavision/xStreamNginxData/data/dav/install");
		boolean result = ftpClient.storeFile(new String("新建文件夹1.zip".getBytes("utf-8"), "iso-8859-1"), in);
		System.out.println(result);
	}
	
}
