package com.sumavision.tetris.omms.hardware.database;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.commons.context.SpringContext;
import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.mvc.listener.ServletContextListener.Path;
import com.sumavision.tetris.omms.hardware.database.databaseBackup.DatabaseBackupDAO;
import com.sumavision.tetris.omms.hardware.database.databaseBackup.DatabaseBackupPO;
import com.sumavision.tetris.omms.hardware.database.databaseBackup.DatabaseBackupVO;
import com.sumavision.tetris.omms.hardware.database.databases.DatabasesDAO;
import com.sumavision.tetris.omms.hardware.database.databases.DatabasesPO;
import com.sumavision.tetris.omms.hardware.server.ServerDAO;
import com.sumavision.tetris.omms.hardware.server.ServerPO;
import com.sumavision.tetris.omms.software.service.deployment.ServiceDeploymentPO;
import com.sumavision.tetris.omms.software.service.deployment.ServiceDeploymentService;
import com.sumavision.tetris.omms.software.service.deployment.exception.FtpChangeFolderFailException;
import com.sumavision.tetris.omms.software.service.deployment.exception.FtpCreateFolderFailException;
import com.sumavision.tetris.omms.software.service.deployment.exception.HttpGadgetEnableFtpException;
import com.sumavision.tetris.omms.software.service.deployment.exception.HttpGadgetRecoverException;
import com.sumavision.tetris.omms.software.service.deployment.exception.HttpGadgetRestartProcessException;

@Service
@Transactional(rollbackFor = Exception.class)
public class DatabaseService {
	
	public static final String RELATIVE_FOLDER = "databasebackup";
	
	public static final String RECOVER_FOLDER = "recoverbackup";
	
	private static final Logger log = Logger.getLogger(DatabaseService.class);
	
	@Autowired
	private DatabaseDAO databaseDAO;
	
	@Autowired
	private DatabasesDAO databasesDAO;
	
	@Autowired
	private ServerDAO serverDAO;
	
	@Autowired
	private DatabaseBackupDAO databaseBackupDAO;
	
	@Autowired
	private ServiceDeploymentService serviceDeploymentService;
	

	/**
	 * 备份数据库<br/>
	 * <b>作者:</b>lqxuhv<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年12月9日 下午7:20:43
	 * @param id 数据库服务id
	 * @param databases 备份的数据库id
	 * @param name 备份之后的名字
	 * @param remark 备份之后的说明
	 * @return
	 */
	public Object backupDatabases(Long id, String[] databases, String name, String remark)throws Exception{
		
		CloseableHttpClient client = null;
		DatabasePO databasePO = databaseDAO.findOne(id);
		Set<Long> databasesIds = new HashSet<Long>();
		if(databases != null && databases.length > 0){
			for (String data : databases) {
				databasesIds.add(Long.valueOf(data));
			}
		}
		List<DatabasesPO> databasesPOs = databasesDAO.findByIdIn(databasesIds);
		
		try {
			
			ServerPO server = serverDAO.findOne(databasePO.getServerId());
			
			
			StringBufferWrapper databasesNames = new StringBufferWrapper();
			if(databasesPOs != null && databasesPOs.size() > 0){
				for (DatabasesPO databasesPO : databasesPOs) {
					databasesNames.append(" ").append(databasesPO.getRealName());
				}
			}
			
			
			CredentialsProvider credsProvider = new BasicCredentialsProvider();
			AuthScope authScope = new AuthScope(server.getIp(), Integer.parseInt(server.getGadgetPort()), "example.com", AuthScope.ANY_SCHEME);
	        credsProvider.setCredentials(authScope, new UsernamePasswordCredentials(server.getGadgetUsername(), server.getGadgetPassword()));
	        client = HttpClients.custom()
			        		    .setDefaultCredentialsProvider(credsProvider)
			        		    .setRetryHandler(new DefaultHttpRequestRetryHandler(1, true))
			        		    .build();
	        
	        String url = new StringBufferWrapper().append("http://").append(server.getIp()).append(":").append(server.getGadgetPort()).append("/action/execute_cmd").toString();
	        
	        System.out.println(url);
	        
	        String shell = new StringBufferWrapper().append("mysqldump")
	        		.append(" -u")
	        		.append(databasePO.getUsername())
	        		.append(" -p")
	        		.append(databasePO.getPassword())
	        		.append(" --databases")
	        		.append(databasesNames)
	        		.append(" > ").toString();
	        
	        Date date = new Date();
	        String namesql = new StringBufferWrapper().append(name).append(date.getTime()).append(".sql").toString();
	        String indexpath = new StringBufferWrapper().append(RELATIVE_FOLDER).append("/").toString();//备份到ftp的相对路径
	        System.out.println(namesql);
	        HttpPost httpPost = new HttpPost(url);
	        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
	        formparams.add(new BasicNameValuePair("cmd", shell));
	        formparams.add(new BasicNameValuePair("name", namesql));
	        formparams.add(new BasicNameValuePair("path", indexpath));
	        
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
			
			// 将backup文件夹中的文件下载到运维服务器上
			
			Path path = SpringContext.getBean(Path.class);
			String downloadPath = new StringBufferWrapper().append(path.webappPath())
															.append(indexpath)
															.append(server.getName())
															.append(File.separator)
															.append(date.getTime())
															.append(File.separator)
															.toString();
			String downuri = new StringBufferWrapper().append(RELATIVE_FOLDER)
													.append("/")
													.append(server.getName())
													.append("/")
													.append(date.getTime())
													.append("/")
													.append(namesql)
													.toString();
			String backupFullPath = new StringBufferWrapper().append(path.webappPath()).append(downuri).toString();
			File file = new File(downloadPath);
			if(!file.exists()){
				file.mkdirs();
			}
			
			enableFtp(server.getId());// 打开ftp端口
			
			Boolean bool = downFile(
				server.getIp(), 
				Integer.parseInt(server.getFtpPort()), 
				server.getFtpUsername(), 
				server.getFtpPassword(), 
				indexpath, 
				namesql, 
				downloadPath);
			
			serviceDeploymentService.disableFtp(server.getId());// 关闭ftp端口
			
			if (bool) {
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				DatabaseBackupPO databaseBackupPO  = new DatabaseBackupPO();
				databaseBackupPO.setName(name);
				databaseBackupPO.setFilename(namesql);
				databaseBackupPO.setDate(df.format(new Date()));
				databaseBackupPO.setPath(backupFullPath);
				databaseBackupPO.setRemark(remark);
				databaseBackupPO.setDatabaseId(id);
				databaseBackupPO.setDownuri(downuri);
				databaseBackupPO.setBackupname(databasesNames.toString());
				databaseBackupDAO.save(databaseBackupPO);
				return new DatabaseBackupVO().set(databaseBackupPO);
			}
			
		} finally {
			if(client != null) client.close();
		}
		
		return null;
	}
	
	//打开ftp的端口
	public void enableFtp(Long serverId) throws Exception{
		CloseableHttpClient client = null;
		try {
			ServerPO server = serverDAO.findOne(serverId);
			
			CredentialsProvider credsProvider = new BasicCredentialsProvider();
			AuthScope authScope = new AuthScope(server.getIp(), Integer.parseInt(server.getGadgetPort()), "example.com", AuthScope.ANY_SCHEME);
	        credsProvider.setCredentials(authScope, new UsernamePasswordCredentials(server.getGadgetUsername(), server.getGadgetPassword()));
	        client = HttpClients.custom()
			        		    .setDefaultCredentialsProvider(credsProvider)
			        		    .setRetryHandler(new DefaultHttpRequestRetryHandler(1, true))
			        		    .build();
	        
	        String url = new StringBufferWrapper().append("http://").append(server.getIp()).append(":").append(server.getGadgetPort()).append("/action/enable_ftp").toString();
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
	 * 上传<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>lqxuhv<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年12月9日 上午10:53:27
	 * @param hostname ip或域名地址
     * @param port  端口
     * @param username 用户名
     * @param password 密码
     * @param workingPath 服务器的工作目
     * @param inputStream 要上传文件的输入流
     * @param saveName    设置上传之后的文件名
	 */
	private Boolean upload(String hostname, int port, String username, String password, InputStream inputStream, String saveName ,ServerPO server) throws Exception{
        boolean flag = false;
        FTPClient ftpClient = new FTPClient();
        //1 测试连接
        if (connect(ftpClient, hostname, port, username, password)) {
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
                //2 检查工作目录是否存在
                if (ftpClient.changeWorkingDirectory(encodeFtpText(RELATIVE_FOLDER))) {
                    // 3 检查是否上传成功
                    if (storeFile(ftpClient, saveName, inputStream)) {
                        flag = true;
                        disconnect(ftpClient);
                    }
                }
            } catch (IOException e) {
            	log.error("工作目录不存在");
                e.printStackTrace();
                disconnect(ftpClient);
            }
        }
        return flag;
    }
	
	/**
	 * 断开连接<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>lqxuhv<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年12月9日 上午10:51:29
	 * @param ftpClient
	 */
	public void disconnect(FTPClient ftpClient) {
        if (ftpClient.isConnected()) {
            try {
                ftpClient.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
	
	/**
	 * 测试能否连接<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>lqxuhv<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年12月9日 上午10:50:02
	 * @param ftpClient
     * @param hostname  ip或域名地址
     * @param port      端口
     * @param username  用户名
     * @param password  密码
     * @return 返回真则能连接
	 */
	public boolean connect(FTPClient ftpClient, String hostname, int port, String username, String password) {
        boolean flag = false;
        try {
            //ftp初始化的一些参数
            ftpClient.connect(hostname, port);
            ftpClient.enterLocalPassiveMode();
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            ftpClient.setControlEncoding("UTF-8");
            if (ftpClient.login(username, password)) {
                log.info("连接ftp成功");
                flag = true;
            } else {
                log.error("连接ftp失败，可能用户名或密码错误");
                try {
                    disconnect(ftpClient);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            log.error("连接失败，可能ip或端口错误");
            System.out.println("连接失败，可能ip或端口错误");
            e.printStackTrace();
        }
        return flag;
    }
	
	/**
	 * 上传备份脚本<br/>
	 * <b>作者:</b>lqxuhv<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年12月9日 上午10:47:50
	 * @param ftpClient
	 * @param saveName 上传后的文件名
	 * @param fileInputStream
	 */
	public boolean storeFile(FTPClient ftpClient, String saveName, InputStream fileInputStream) {
        boolean flag = false;
        try {
            if (ftpClient.storeFile(saveName, fileInputStream)) {
                flag = true;
                log.error("上传成功");
                disconnect(ftpClient);
            }
        } catch (IOException e) {
        	log.error("上传失败");
            disconnect(ftpClient);
            e.printStackTrace();
        }
        return flag;
    }
	
	/**
	 * ftp中文编码iso-8859-1<br/>
	 * <b>作者:</b>lqxuhv<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年12月9日 上午10:45:34
	 */
	private String encodeFtpText(String text) throws Exception{
		return new String(text.getBytes("utf-8"), "iso-8859-1");
	}
	

	/**
	 * 恢复数据库<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>lqxuhv<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年12月21日 上午8:19:03
	 * @param databaseBackupId
	 * @param databaseName
	 * @return
	 * @throws Exception
	 */
	public Object recoverDatabase(Long databaseBackupId ,String databaseName) throws Exception{
		DatabaseBackupPO databaseBackupPO = databaseBackupDAO.findOne(databaseBackupId);
		DatabasePO databasePO = databaseDAO.findOne(databaseBackupPO.getDatabaseId());
		ServerPO server = serverDAO.findOne(databasePO.getServerId());
		enableFtp(server.getId());
		
		//上传备份数据库到需要恢复的ftp服务器
		uploadFile(
				server.getIp(), 
				Integer.parseInt(server.getFtpPort()), 
				server.getFtpUsername(), 
				server.getFtpPassword(),
				new StringBufferWrapper().append("/").append(RECOVER_FOLDER).append("/").toString(), 
				databaseBackupPO.getFilename(),
				databaseBackupPO.getPath()
		);
		serviceDeploymentService.disableFtp(server.getId());
		Path path = SpringContext.getBean(Path.class);
		
		CloseableHttpClient client = null;
		try {
			
			CredentialsProvider credsProvider = new BasicCredentialsProvider();
			AuthScope authScope = new AuthScope(server.getIp(), Integer.parseInt(server.getGadgetPort()), "example.com", AuthScope.ANY_SCHEME);
	        credsProvider.setCredentials(authScope, new UsernamePasswordCredentials(server.getGadgetUsername(), server.getGadgetPassword()));
	        client = HttpClients.custom()
			        		    .setDefaultCredentialsProvider(credsProvider)
			        		    .setRetryHandler(new DefaultHttpRequestRetryHandler(1, true))
			        		    .build();
	        
	        String shell = new StringBufferWrapper().append("mysqldump")
	        		.append(" -u")
	        		.append(databasePO.getUsername())
	        		.append(" -p")
	        		.append(databasePO.getPassword())
	        		.append(" --databases")
	        		.append(databaseName)
	        		.append(" < ").toString();
	        //String namesql = new StringBufferWrapper().append(databaseBackupPO.getName()).append(".sql").toString();
	        String url = new StringBufferWrapper().append("http://").append(server.getIp()).append(":").append(server.getGadgetPort()).append("/action/execute_cmd").toString();
	        String cmdpath = new StringBufferWrapper().append(RECOVER_FOLDER).append("/").toString();
	        HttpPost httpPost = new HttpPost(url);
	        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
	        formparams.add(new BasicNameValuePair("cmd", shell));
	        formparams.add(new BasicNameValuePair("name", databaseBackupPO.getFilename()));
	        formparams.add(new BasicNameValuePair("path", cmdpath));
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
				throw new HttpGadgetRecoverException(server.getIp(), server.getGadgetPort(), errormsg);
			}
			
			int code = response.getStatusLine().getStatusCode();
			if(code != 200){
				throw new HttpGadgetRecoverException(server.getIp(), server.getGadgetPort(), String.valueOf(code));
			}     
		}finally{
			if(client != null) client.close();
		} 	
		
		return null;
	}

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
			boolean changeResult = ftp.changeWorkingDirectory(encodeFtpText(RECOVER_FOLDER));
			if(!changeResult){
				boolean mdResult = ftp.makeDirectory(encodeFtpText(RECOVER_FOLDER));
				if(!mdResult){
					throw new FtpCreateFolderFailException(url, String.valueOf(port), RECOVER_FOLDER);
				}
				changeResult = ftp.changeWorkingDirectory(encodeFtpText(RECOVER_FOLDER));
				if(!changeResult){
					throw new FtpChangeFolderFailException(url, String.valueOf(port), RECOVER_FOLDER);
				}
			}
			reply = ftp.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)) {
				ftp.disconnect();
				return success;
			}
			ftp.deleteFile(filename);
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
	 * 删除数据库的备份<br/>
	 * <b>作者:</b>lqxuhv<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年12月21日 上午9:26:53
 	 * @param id 备份数据库的id
	 */   //此处未涉及服务器上数据的删除
	public Boolean deleteBackup(Long id) throws Exception{
		DatabaseBackupPO databaseBackupPO = databaseBackupDAO.findOne(id);
		boolean success = false;
		FTPClient ftp = new FTPClient();
		if (null != databaseBackupPO) {
			DatabasePO databasePO = databaseDAO.findOne(databaseBackupPO.getDatabaseId());
			ServerPO server = serverDAO.findOne(databasePO.getServerId());
			try{
				enableFtp(server.getId());
				int reply;
				ftp.connect(server.getIp(), Integer.valueOf(server.getFtpPort()));
				ftp.login(server.getFtpUsername(), server.getFtpPassword());
				reply = ftp.getReplyCode();
				if (!FTPReply.isPositiveCompletion(reply)) {
					ftp.disconnect();
					return success;
				}
				ftp.changeWorkingDirectory(encodeFtpText(RELATIVE_FOLDER));
				String filename = new StringBufferWrapper().append(databaseBackupPO.getName()).append(".sql").toString();
				ftp.deleteFile(filename);
			}finally{
				if (ftp.isConnected()) {
					try {
						ftp.disconnect();
					} catch (IOException ioe) {
					}
				}
			}
			databaseBackupDAO.delete(databaseBackupPO);
		}
		return success;
	}
	
	@Transactional(rollbackFor = Exception.class)
	public boolean downFile(String url, int port,String username, String password, String remotePath,String fileName,String localPath) throws Exception{
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
			//ftp.changeWorkingDirectory(remotePath);
			//转移到FTP服务器目录
			boolean changeResult = ftp.changeWorkingDirectory(encodeFtpText(RELATIVE_FOLDER));
			if(!changeResult){
				boolean mdResult = ftp.makeDirectory(encodeFtpText(RELATIVE_FOLDER));
				if(!mdResult){
					throw new FtpCreateFolderFailException(url, String.valueOf(port), RELATIVE_FOLDER);
				}
				changeResult = ftp.changeWorkingDirectory(encodeFtpText(RELATIVE_FOLDER));
				if(!changeResult){
					throw new FtpChangeFolderFailException(url, String.valueOf(port), RELATIVE_FOLDER);
				}
			}
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
	
}
