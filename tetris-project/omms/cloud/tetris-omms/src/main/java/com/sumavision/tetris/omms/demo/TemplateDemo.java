package com.sumavision.tetris.omms.demo;

import java.io.StringWriter;

import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class TemplateDemo {

	public static void main(String[] args) throws Exception{
		Configuration configuration = new Configuration(Configuration.getVersion());
		configuration.setDefaultEncoding("utf-8");
		String ts = "<#if users??><#list users as user >${user.id} - ${user.name}</#list><#else>${users!\"变量为空则给一个默认值\"}</#if>";
		
		Template template = new Template("aaa", ts, configuration);
		StringWriter out = new StringWriter();
		template.process(new HashMapWrapper<String, Object>().put("users1", new ArrayListWrapper<Object>().add(new HashMapWrapper<String, Object>().put("id", 0).put("name", "ldy").getMap())
															                                             .add(new HashMapWrapper<String, Object>().put("id", 1).put("name", "zsy").getMap()).getList()).getMap(), out);
		System.out.println(out.toString());
		
	}
	
}
