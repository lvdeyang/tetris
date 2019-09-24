package com.sumavision.tetris.mims.config.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

@Component
public class MimsServerPropsQuery {

	@Autowired
	private ServerProps serverProps;
	
	/**
	 * 查询媒资服务属性<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月4日 下午3:11:36
	 * @return ServerProps 服务属性
	 */
	public ServerProps queryProps(){
		return serverProps;
	}

	/**
	 * 生成资源http访问全路径<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年7月22日 下午5:42:19
	 * @param String primeryPath 资源路径
	 * @return String 资源访问http地址
	 */
	public String generateHttpPreviewUrl(String primeryPath){
		return new StringBufferWrapper().append("http://").append(serverProps.getIp())
														  .append(":")
														  .append(serverProps.getPort())
														  .append("/")
														  .append(primeryPath)
														  .toString();
	}
	
}
