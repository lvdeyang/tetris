
package com.sumavision.tetris.mims.app.store;

import java.io.StringWriter;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.orm.dao.BaseDAO;

import freemarker.template.Configuration;
import freemarker.template.Template;

@RepositoryDefinition(domainClass = PreRemoveFilePO.class, idClass = Long.class)
public interface PreRemoveFileDAO extends BaseDAO<PreRemoveFilePO>{
	
//	public static void main(String[] args) throws Exception{
//		Configuration configuration = new Configuration(Configuration.getVersion());
//		configuration.setDefaultEncoding("utf-8");
//		Template template = new Template("aaa", "${ldy}", configuration);
//		StringWriter out = new StringWriter();
//		template.process(new HashMapWrapper<String, Object>().put("ldy", "111").getMap(), out);
//		System.out.println(out.toString());
//	}
	
}
