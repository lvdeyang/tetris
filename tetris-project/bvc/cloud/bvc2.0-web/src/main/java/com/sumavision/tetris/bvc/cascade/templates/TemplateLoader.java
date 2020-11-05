package com.sumavision.tetris.bvc.cascade.templates;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * 协议模板加载器<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年3月30日 下午8:03:46
 */
@Component
public class TemplateLoader {

	private static final ConcurrentHashMap<String, String> templateCache = new ConcurrentHashMap<String, String>(); 
	
	private Configuration configuration = null;
	
	public TemplateLoader(){
		configuration = new Configuration(Configuration.getVersion());
		configuration.setDefaultEncoding("utf-8");
	}
	
	/**
	 * 加载模板<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月30日 下午7:55:08
	 * @param String fullName 包名+文件名
	 * @return String 模板
	 */
	public Template load(String fullName) throws Exception{
		String template = null;
		if(templateCache.contains(fullName)){
			template = templateCache.get(fullName);
		}else{
			InputStream in = null;
			try{
				in = this.getClass().getClassLoader().getResourceAsStream(fullName);
				BufferedReader reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
				StringBuffer templateBuffer = new StringBuffer();
				String line = null;
				while ((line=reader.readLine()) != null) {
					templateBuffer.append(line);
				}
				template = templateBuffer.toString();
				templateCache.put(fullName, template);
			}finally{
				if(in != null) in.close();
			}
		}
		return new Template(fullName, template, configuration);
	}
	
}
