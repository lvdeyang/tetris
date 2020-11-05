package com.sumavision.tetris.system.storage.gadget;

import java.util.Collection;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.commons.util.file.FileUtil;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.system.storage.gadget.exception.CpuPerformanceException;

@Component(value = "com.sumavision.tetris.system.storage.gadget.SumavisionGadget")
public class SumavisionGadget implements Gadget{

	@Override
	public boolean heartbeat(String baseControlPath){
		return true;
	}
	
	@Override
	public Storage storageAbility(String baseControlPath, String rootPath) throws Exception{
		return null;
	}

	@Override
	public Cpu cpuPerformance(String baseControlPath) throws Exception{
		CloseableHttpClient httpclient = null;
		CloseableHttpResponse response = null;
		try{
			httpclient = HttpClients.createDefault();
			HttpPost httpPost = new HttpPost(new StringBufferWrapper().append(baseControlPath).append("action/get_capability_info").toString());
			response = httpclient.execute(httpPost);
			if(response.getStatusLine().getStatusCode() == 200){
	        	HttpEntity entity = response.getEntity();
	        	JSONObject storageInfo = JSON.parseObject(FileUtil.readAsString(entity.getContent()));
	 	        EntityUtils.consume(entity);
	 	        
	 	        String cpu_name = storageInfo.getJSONObject("cpu").getString("name");
	 	        String cpu_usage = storageInfo.getJSONObject("cpu").getString("usage");
	 	        String memory_total = storageInfo.getJSONObject("memory").getString("total");
	 	        String memory_used = storageInfo.getJSONObject("memory").getString("used");
	 	        Cpu cpu = new Cpu().setName(cpu_name)
	 	        				   .setUsageRate(cpu_usage)
	 	        				   .setTotalMemorySize(Long.parseLong(memory_total.replace("M", ""))*1024l)
	 	        				   .setUsedMemorySize(Long.parseLong(memory_used.replace("M", ""))*1024l);
	 	        return cpu;
	        }else{
	        	throw new CpuPerformanceException(baseControlPath);
	        }
		}finally{
			if(response != null) response.close();
			if(httpclient != null) httpclient.close();
		}
	}

	@Override
	public List<Directory> statisticsFolderSize(String baseControlPath, Collection<String> folderPathes) throws Exception{
		
		return null;
	}

}
