package com.sumavision.tetris.cs.channel;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.cs.channel.exception.ChannelUdpBroadCountIsFullException;
import com.sumavision.tetris.mims.config.server.MimsServerPropsQuery;
import com.sumavision.tetris.mims.config.server.ServerProps;

@Service
@Transactional(rollbackFor = Exception.class)
public class Adapter {
	@Autowired
	private MimsServerPropsQuery mimsServerPropsQuery;
	
	/**
	 * 适配：http地址转ftp地址(ftp账号密码在配置文件，默认ftp根目录为../webapps/ROOT)<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年8月1日 上午11:06:57
	 */
	public String changeHttpToFtp(String httpUrl) throws Exception {		

		String ftpPath = null;

		String[] split = httpUrl.split(":");
		
		ServerProps serverProps = mimsServerPropsQuery.queryProps();

		//没有端口的Url地址
		if (split.length == 2) {
			String[] splite2 = split[1].split("//");
			
			String path = splite2[1].substring(splite2[1].indexOf("/"));
			
			ftpPath = new StringBufferWrapper().append("ftp://").append(serverProps.getFtpUsername())
					  .append(":")
					  .append(serverProps.getFtpPassword())
					  .append("@")
					  .append(serverProps.getFtpIp())
					  .append(":")
					  .append(serverProps.getFtpPort())
					  .append(path)
					  .toString();
		//有端口的url地址
		} else if (split.length == 3) {

			String path = split[2].substring(split[2].indexOf("/"));

			ftpPath = new StringBufferWrapper().append("ftp://").append(serverProps.getFtpUsername())
					  .append(":")
					  .append(serverProps.getFtpPassword())
					  .append("@")
					  .append(serverProps.getFtpIp())
					  .append(":")
					  .append(serverProps.getFtpPort())
					  .append(path)
					  .toString();
		}

		return ftpPath;
	}
	
	/**
	 * 适配：根据ip和port生成udp的url<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年8月1日 上午11:06:57
	 */
	public String getUdpUrlFromIpAndPort(String ip,String port){
		if (ip == null || port == null) return null;
		
		return new StringBufferWrapper().append("udp://@").append(ip).append(":").append(port).toString();
	}
	
	/**
	 * 适配：根据udp的url获取ip和port<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年8月1日 上午11:06:57
	 * @throws Exception 
	 */
	public HashMapWrapper<String, String> getIpAndPortFromUdpUrl(String url) throws Exception{
		if (url == null || url.isEmpty()) return null;
		
		String[] split = url.split(":");
		
		String ip = split[1].split("@")[1];
		
		String port = split[2];
		
		return new HashMapWrapper<String, String>().put("ip", ip)
				.put("port", port);
	}
	
	/**
	 * 适配：获取新的能力播发id<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年8月1日 上午11:06:57
	 */
	public int getNewId(List<Integer> ids) throws Exception{
		List<Integer> parentList = new ArrayList<Integer>();
		for (int i = 0; i < 256; i++) {
			parentList.add(i);
		}
		parentList.removeAll(ids);
		if (parentList.isEmpty()) throw new ChannelUdpBroadCountIsFullException();
		return parentList.get(0);
	}
}
