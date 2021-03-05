/**
 * 
 */
package com.sumavision.tetris.user;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sumavision.tetris.loginpage.LoginPagePO;
import com.sumavision.tetris.loginpage.LoginPageQuery;
import com.sumavision.tetris.loginpage.PageVariableQuery;
import com.sumavision.tetris.loginpage.VariableTypeVO;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * 类型概述<br/>
 * <b>作者:</b>zhouaining<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2021年2月3日 上午10:56:24
 */
@Service
public class GetLoginPageService {
	@Autowired
	private PageVariableQuery pageVariableQuery;
	
	@Autowired
	private LoginPageQuery loginPageQuery;
		
	private Configuration configuration = null;
	
	public String getLoginPageString() throws Exception{
		
		Map<String, String> map = new HashMap<>();
		String tpl = null;
		//获取正在使用的页面
		List<LoginPagePO> pageList = loginPageQuery.listLoginPage();
		for(LoginPagePO page:pageList){
			if(page.getIsCurrent()==true){
				Long loginPageId = page.getId();
				tpl = page.getTpl();
				//获取该页面配置的变量
				List<VariableTypeVO> variableList = pageVariableQuery.getVariable(loginPageId);
				for(VariableTypeVO variable:variableList){
					map.put(variable.getVariableKey(), variable.getVariable().get(0).getValue());
				}
				break;
			}
		}
		
		configuration = new Configuration();
		configuration.setDefaultEncoding("utf-8");
		if(tpl==null){
			return null;
		}
		//获得完整模板
		String fullTPL = getFullTPL(tpl);
		Template template = new Template("测试",fullTPL, configuration);

		Writer out = new StringWriter();
		template.process(map, out);
		String resultHTML = out.toString();
		out.close();
		return resultHTML;
	}
	
	/**
	 * 拼接获得完整模板<br/>
	 * <b>作者:</b>zhouaining<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年2月4日 下午1:33:38
	 * @param tpl
	 * @return
	 */
	public String getFullTPL(String tpl){
		StringBuilder FullTPL = new StringBuilder().append(
				"<!DOCTYPE html>"
				+ "<html>"
				+ "<head lang=\"en\">"
				+ "<link rel=\"stylesheet\" type=\"text/css\" href=\"/web/lib/ui/element-ui/element-ui-2.4.3.min.css\"/>"
				+ "<link rel=\"stylesheet\" type=\"text/css\" href=\"/web/lib/animate/animate.css\">"
				+ "<link rel=\"stylesheet\" type=\"text/css\" href=\"/web/lib/icon/Font-Awesome-3.2.1/css/font-awesome.css\"/>"
				+ "<link rel=\"stylesheet\" type=\"text/css\" href=\"/web/lib/icon/feather/style.css\"/>"
				+ "<link rel=\"stylesheet\" type=\"text/css\" href=\"/web/lib/icon/brands/style.css\"/>"
				+ "<meta charset=\"UTF-8\"><meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">"
				+ "<title>用户登陆</title>"
				+ "<style>"
				+ "html,body{height:100%;width:100%;overflow:auto;padding:0;margin:0;}"
				+"#app{width:100%; height:100%; min-width:1024px; min-height:750px; position:relative;}"
				+ "</style>"
				+"<script type=\"text/javascript\">"
			    +"window.environment = 'prod';"
			    +"</script>"
				+ "</head>"
				+ "<body>"
				+ "<div id=\"app\">")
				.append(tpl)
				.append(
				"</div>"
				+ "</body>"
				+ "<script type=\"text/javascript\" src=\"/web/lib/frame/jQuery/jquery-2.2.3.min.js\">"
				+ "</script><script type=\"text/javascript\" src=\"/web/lib/frame/jQuery/jquery.json.js\">"
				+ "</script><script type=\"text/javascript\" src=\"/web/lib/frame/vue/vue-2.5.16.js\">"
				+ "</script><script type=\"text/javascript\" src=\"/web/lib/ui/element-ui/element-ui-2.4.3.min.js\">"
				+ "</script><script type=\"text/javascript\" src=\"/web/app/login/loginform/loginPage.js\">"
				+ "</script>"
				+ "</html>");
		return FullTPL.toString();
	}
	
	
}
