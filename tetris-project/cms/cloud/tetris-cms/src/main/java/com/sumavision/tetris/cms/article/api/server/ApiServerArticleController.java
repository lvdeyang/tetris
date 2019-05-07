package com.sumavision.tetris.cms.article.api.server;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.cms.article.ArticlePO;
import com.sumavision.tetris.cms.article.ArticleService;
import com.sumavision.tetris.cms.article.ArticleVO;
import com.sumavision.tetris.easy.process.core.ProcessService;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.user.UserVO;

@Controller
@RequestMapping(value = "/api/server/article")
public class ApiServerArticleController {
	
	@Autowired
	private ArticleService articleService;

	@Autowired
	private ProcessService processService;
	
	/**
	 * 添加文章<br/>
	 * <b>作者:</b>ldy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月12日 下午8:33:12
	 * @param String column 栏目 -- "[code1, code2]"
	 * @param String name 文章名
	 * @param String type 文章类型  TEXT/AVIDEO
	 * @param String author 作者名
	 * @param String publishTime 发表时间
	 * @param String thumbnail 缩略图--媒资图片路径
	 * @param String remark 备注
	 * @param String keywords 关键字
	 * @param Boolean command 是否推荐
	 * @param String content 内容--json字符串，结构:[{"type":"内容类型",--yjgb_title(标题)/yjgb_txt(文本)/yjgb_picture(图片)/yjgb_video(视频)/yjgb_audio(音频)
	 * 										"value":"内容值",--标题和文本传对应的String,图片、视频和音频传对应的url}]
	 * @param region 地区 -- "[code1,code2]"
	 * @return ArticleVO 新建的文章数据
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/generate/with/internal/template")
	public Object generateWithInternalTemplate(
			String column,
			String name, 
			String type,
			String author,
			String publishTime,
			String thumbnail,
			String remark,
			String keywords,
			Boolean command,
			String content,
			String region, 
			HttpServletRequest request) throws Exception{
		
		//TODO:暂时自己创建一个UserVO
		UserVO user = new UserVO().setGroupId("2")
								  .setGroupName("数码视讯")
								  .setUuid("3")
								  .setNickname("yjgb");
		
		List<String> columns = JSONArray.parseArray(column, String.class);
		List<JSONObject> contents = JSONArray.parseArray(content, JSONObject.class);
		List<String> regions = JSONArray.parseArray(region, String.class);

		command = command== null?false:command;
		
		ArticlePO article = articleService.generateWithInternalTemplate(
				columns, 
				name, 
				type, 
				author, 
				publishTime, 
				thumbnail, 
				remark, 
				keywords, 
				command, 
				contents, 
				regions,
				user);
		return new ArticleVO().set(article);
	}

	/**
	 * 启动应急广播根据播发媒资生成广播流程<br/>
	 * <b>作者:</b>ldy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年4月4日 下午2:43:55
	 * @param Long id 播发媒资id
	 * @return String 流程实例id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/generate/with/compress")
	public Object generateWithCompress(
			Long id, 
			HttpServletRequest request) throws Exception{
		
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("_pa1_id", id);
		
		return processService.startByKey("_yjgb_generate_article_by_compress_", jsonObject.toJSONString());
	}
	
}
