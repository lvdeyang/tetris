package com.sumavision.tetris.omms.graph;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.sumavision.tetris.commons.util.file.FileUtil;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.omms.graph.exception.ApplicationNotFoundException;
import com.sumavision.tetris.omms.graph.exception.GadgetRequestFailException;
import com.sumavision.tetris.spring.eureka.application.ApplicationQuery;
import com.sumavision.tetris.spring.eureka.application.ApplicationVO;

@Controller
@RequestMapping(value = "/server")
public class ServerController {

	@Autowired
	private ApplicationQuery applicationQuery;
	
	/**
	 * 获取服务器（小工具）状态<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月30日 下午1:17:46
	 * @param String instanceId 服务实例id
	 * @return 
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/status")
	public Object status(
			String instanceId, 
			HttpServletRequest request) throws Exception{
		ApplicationVO application = applicationQuery.findByInstanceId(instanceId);
		if(application != null){
			CloseableHttpClient httpclient = null;
			CloseableHttpResponse response = null;
			try{
				String url = new StringBufferWrapper().append("http://").append(application.getIp()).append(":").append(application.getGadgetPort()).append("/action/get_capability_info").toString();
				httpclient = HttpClients.createDefault();
				HttpPost httpPost = new HttpPost(url);
				response = httpclient.execute(httpPost);
				if(response.getStatusLine().getStatusCode() == 200){
		        	HttpEntity entity = response.getEntity();
		        	String status = FileUtil.readAsString(entity.getContent());
		 	        EntityUtils.consume(entity);
		 	        return JSON.parseObject(status);
		        }else{
		        	throw new GadgetRequestFailException(application.getIp(), application.getGadgetPort());
		        }
			}catch(Exception e){
				throw new GadgetRequestFailException(application.getIp(), application.getGadgetPort());
			}finally{
				if(response != null) response.close();
				if(httpclient != null) httpclient.close();
			}
		}else{
			throw new ApplicationNotFoundException(instanceId);
		}
	}
	
}
