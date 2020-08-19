package com.sumavision.tetris.omms.demo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Properties;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpProgressMonitor;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class SshDemo {

	/**
	 * ssh 执行 shell<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月24日 上午8:50:06
	 */
	private static void shellDeomo() throws Exception{
		JSch jSch = new JSch();
		Session session = jSch.getSession("root", "192.165.58.167", 22);
		session.setPassword("sumavisi0n");
		
		Properties sshConfig = new Properties();
        sshConfig.put("StrictHostKeyChecking", "no");
        session.setConfig(sshConfig);
        session.connect(30000);
        
        Channel channel = (Channel) session.openChannel("shell");
        channel.connect();
        
        //从远程端到达的所有数据都能从这个流中读取到
        InputStream inputStream = channel.getInputStream();
        //写入该流的所有数据都将发送到远程端。
        OutputStream outputStream = channel.getOutputStream();
        //使用PrintWriter流的目的就是为了使用println这个方法
        //好处就是不需要每次手动给字符串加\n
        PrintWriter printWriter = new PrintWriter(outputStream);
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
        String msg = null;
        
        printWriter.println("MYPATH=/usr/sbin/sumavision/lzp");
		printWriter.println("FILENAME=e.txt");
		printWriter.println("mkdir -p $MYPATH");
		printWriter.println("cd $MYPATH");
		printWriter.println("touch $FILENAME");
		printWriter.println("echo \"111111\">$FILENAME");
		printWriter.println("exit");
		printWriter.flush();
		while((msg = in.readLine()) != null){
			System.out.println(msg);
		}
		in.close();
		printWriter.close();
		channel.disconnect();
		
		channel = (Channel) session.openChannel("shell");
        channel.connect();
		inputStream = channel.getInputStream();
		outputStream = channel.getOutputStream();
		printWriter = new PrintWriter(outputStream);
		in = new BufferedReader(new InputStreamReader(inputStream));
		printWriter.println("MYPATH=/usr/sbin/sumavision");
		printWriter.println("cd $MYPATH");
		printWriter.println("rm -rf lzp");
		printWriter.println("exit");
		printWriter.flush();
		while((msg = in.readLine()) != null){
			System.out.println(msg);
		}
		
		channel.disconnect();
		session.disconnect();
	}
	
	private static void sftpDemo() throws Exception{
		StringBufferWrapper fileInClassPath = new StringBufferWrapper().append("com").append(File.separator)
																		.append("sumavision").append(File.separator)
																		.append("tetris").append(File.separator)
																		.append("omms").append(File.separator)
																		.append("demo").append(File.separator)
																		.append("a.sh");
		final InputStream fileStream = SshDemo.class.getClassLoader().getResourceAsStream(fileInClassPath.toString());
		
		JSch jSch = new JSch();
		Session session = jSch.getSession("root", "192.165.58.167", 22);
		session.setPassword("sumavisi0n");
		
		Properties sshConfig = new Properties();
        sshConfig.put("StrictHostKeyChecking", "no");
        session.setConfig(sshConfig);
        session.connect(30000);
        
        ChannelSftp sftpChannel = (ChannelSftp)session.openChannel("sftp");
        sftpChannel.connect();
        
        sftpChannel.cd("/usr/sbin/sumavision");
        sftpChannel.put(fileStream, "a.sh", new SftpProgressMonitor(){
			@Override
			public void init(int op, String src, String dest, long max) {
				System.out.println("--------------");
				System.out.println("init");
				System.out.println(op);
				System.out.println(src);
				System.out.println(dest);
				System.out.println(max);
			}
			@Override
			public boolean count(long count) {
				System.out.println("--------------");
				System.out.println("count");
				System.out.println(count);
				return false;
			}
			@Override
			public void end() {
				System.out.println("--------------");
				System.out.println("end");
				try{
					fileStream.close();
				}catch(Exception e){e.printStackTrace();}
			}
		}, ChannelSftp.OVERWRITE);
        sftpChannel.chmod(777, "a.sh");
        sftpChannel.disconnect();
		
		Channel shellChannel = session.openChannel("shell");
		shellChannel.connect();
		
		InputStream inputStream = shellChannel.getInputStream();
		OutputStream outputStream = shellChannel.getOutputStream();
		
		PrintWriter writer = new PrintWriter(outputStream);
		writer.println("cd /usr/sbin/sumavision");
		writer.println("sh a.sh");
		writer.println("exit");
		writer.flush();
		BufferedReader inputReader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
		String line = null;
		while((line=inputReader.readLine())!=null) {
			System.out.println(line);
		}
		
		inputStream.close();
		outputStream.close();
		shellChannel.disconnect();
		session.disconnect();
	}
	
	public static void main(String[] args) throws Exception{
		//shellDeomo();
		sftpDemo();
	}
	
}
