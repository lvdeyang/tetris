package com.suma.venus.resource.service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Properties;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;

@Service
@Transactional(rollbackFor = Exception.class)
public class LdapService {
	
	/**
	 * ldap数据备份<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月22日 下午1:23:35
	 */
	public void ldapBackup() throws Exception{
		
		String ldapIp = null;
		try {
			ApplicationContext act = new ClassPathXmlApplicationContext("classpath:ldap.xml");
			LdapContextSource ldapBean = (LdapContextSource)act.getBean("contextSourceTarget");
			
			String ldapUrl = ldapBean.getUrls()[0];
			ldapIp = ldapUrl.split("ldap://")[1].split(":")[0];
		} catch (Exception e) {
			e.printStackTrace();
			throw new BaseException(StatusCode.ERROR, "读取ldap配置有误！");
		}
		
		try {
			shell(ldapIp, "sh /usr/local/openldap-2.4.49/openldap-backupfiles-on-1-node.sh");
		} catch (Exception e) {
			e.printStackTrace();
			throw new BaseException(StatusCode.ERROR, "shell执行脚本有误！");
		}
	}
	
	/**
	 * ldap数据恢复<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月22日 下午1:23:12
	 */
	public void ldapResume() throws Exception{
		
		String ldapIp = null;
		try {
			ApplicationContext act = new ClassPathXmlApplicationContext("classpath:ldap.xml");
			LdapContextSource ldapBean = (LdapContextSource)act.getBean("contextSourceTarget");
			
			String ldapUrl = ldapBean.getUrls()[0];
			ldapIp = ldapUrl.split("ldap://")[1].split(":")[0];
		} catch (Exception e) {
			e.printStackTrace();
			throw new BaseException(StatusCode.ERROR, "读取ldap配置有误！");
		}
		
		try {
			shell(ldapIp, "sh /usr/local/openldap-2.4.49/openldap-restorefiles-on-1-node.sh");
		} catch (Exception e) {
			e.printStackTrace();
			throw new BaseException(StatusCode.ERROR, "shell执行脚本有误！");
		}
	}

	/**
	 * 服务器执行shell命令<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月22日 下午1:17:25
	 * @param String ip 远程服务器ip
	 * @param String cmd 需要执行的shell命令
	 */
	private void shell(String ip, String cmd) throws Exception{
		
		JSch jSch = new JSch();
		Session session = jSch.getSession("root", ip, 822);
		session.setPassword("sumavisionrd");
		
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
        
        printWriter.println(cmd);
        //需要logout
        printWriter.println("exit");
		printWriter.flush();
		while((msg = in.readLine()) != null){
			System.out.println(msg);
		}
		in.close();
		printWriter.close();
		channel.disconnect();
		session.disconnect();
	}
	
}
