/**
 * 
 */
package com.sumavision.tetris.omms.software.service.deployment;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.transaction.annotation.Transactional;


public class Test01 {

	public static void main(String[] args) throws Exception {
//		FileInputStream in=new FileInputStream(new File("E:\\Project\\tetris\\tetris-project\\omms\\cloud\\tetris-omms\\src\\main\\webapp\\packages\\bvc30/bvc.zip"));
//		uploadFile("192.165.56.70", 21, "liuxuhui", "123", "/perl5/", "bvc.zip", "E:\\Project\\tetris\\tetris-project\\omms\\cloud\\tetris-omms\\src\\main\\webapp\\packages\\bvc30/bvc.zip");
//		downFile("192.165.56.70", 21, "liuxuhui", "123", "/perl5", "bvc.zip", "E:/");
//		File file = new File("ftp://192.165.56.70/perl5/backup");
//		if(!file.exists()){
//			file.mkdir();
//		}
		List<String> list = new ArrayList<String>();
		list.add("a");
		list.add("b");
		list.add("c");
		System.out.println(list.toString());
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
	 * @param path FTP服务器保存目录
	 * @param filename 上传到FTP服务器上的文件名
	 * @param inputPath 文件路径
	 * @return 成功返回true，否则返回false
	 */
	@Transactional(rollbackFor = Exception.class)
	public static boolean uploadFile(String url,int port,String username, String password, String path, String filename, String inputPath) throws Exception{
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
			ftp.setBufferSize(80*1024);
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
	 * Description: 从FTP服务器下载文件
	 * @Version1.0 Jul 27, 2008 5:32:36 PM by 崔红保（cuihongbao@d-heaven.com）创建
	 * @param url FTP服务器hostname
	 * @param port FTP服务器端口
	 * @param username FTP登录账号
	 * @param password FTP登录密码
	 * @param remotePath FTP服务器上的相对路径
	 * @param fileName 要下载的文件名
	 * @param localPath 下载后保存到本地的路径
	 * @return
	 */
	public static boolean downFile(String url, int port,String username, String password, String remotePath,String fileName,String localPath) {
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
