package com.sumavision.tetris.cms.article.api.android;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.cms.article.ArticlePO;
import com.sumavision.tetris.cms.article.ArticleQuery;
import com.sumavision.tetris.cms.article.ArticleService;
import com.sumavision.tetris.cms.article.ArticleVO;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;

@Controller
@RequestMapping(value = "/api/android/article")
public class ApiAndroidArticleController {
	@Autowired
	private UserQuery userQuery;
	
	@Autowired
	private ArticleQuery articleQuery;
	
	@Autowired
	private ArticleService articleService;
	
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
	 * @param Boolean ifLive 是否是直播
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
			Boolean ifLive,
			HttpServletRequest request) throws Exception{
		
		//TODO:暂时自己创建一个UserVO
		UserVO user = userQuery.current();
		
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
				user,
				ifLive);
		return new ArticleVO().set(article);
	}
	
	/**
	 * 获取所有直播文章<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月12日 下午8:33:12
	 * @return List<ArticleVO> 所有直播文章
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/quest/with/live")
	public Object questLive(HttpServletRequest request) throws Exception{
		
		//TODO:暂时自己创建一个UserVO
		UserVO user = userQuery.current();
		
		return articleQuery.findAllLive(user);
	}
}
